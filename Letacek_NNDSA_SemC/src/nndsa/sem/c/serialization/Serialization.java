package nndsa.sem.c.serialization;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import nndsa.sem.c.entity.Word;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import javafx.util.Pair;
import nndsa.sem.c.entity.Block;
import nndsa.sem.c.entity.BlockHead;
import nndsa.sem.c.entity.Head;
/**
 *
 * @author ludek
 */
public class Serialization {
    private List<Pair<BlockHead,Block>> inventory = new LinkedList<>();
    private Head headOfFile;
    
    public Serialization() {
        this(100);
    }

    public Serialization(int elementsInBlock) {
        this.headOfFile = new Head(elementsInBlock);
    }
    
    public void buildBaseToBinaryFile(String csvFileBase, String binaryFileBase) throws IOException {
        List<Word> words = loadBaseToList(csvFileBase);
        createBlocks(words);
        saveBase(binaryFileBase);
    }
    private void createBlocks(List<Word> words) {
        int indexStart = 0;
        while(true) {
            if(indexStart>words.size())
                break;
            
            List<Word> blockToWrite = (indexStart + headOfFile.getElementsInBlock())<=words.size() ? 
                    (words.subList(indexStart, indexStart + headOfFile.getElementsInBlock())) :
                    (words.subList(indexStart, words.size()));
            
            BlockHead head = new BlockHead(blockToWrite.get(0), blockToWrite.get(blockToWrite.size()-1), blockToWrite.size()-1);
            Block content = new Block(blockToWrite);
            inventory.add(new Pair<>(head,content));
            
            indexStart += headOfFile.getElementsInBlock();
        }
    }
    
    private List<Word> loadBaseToList(String csvFileBase) throws IOException {
        List<Word> words = new LinkedList<>();
        try (Stream<String> file = Files.lines(Paths.get(csvFileBase), StandardCharsets.UTF_8)) {
            file.filter(t -> t != null)
                    .map((String line) -> {
                        String[] parse = line.split(";");
                        if(parse.length!=3)
                            throw new IllegalArgumentException("Base is not valid");
                        return new Word(parse[0], parse[1], parse[2]);
                    })
                    .forEach(((entity) -> {
                        words.add(entity);
                    }));
        }
        
        words.sort((a, b) -> {
            return a.getKey().compareTo(b.getKey());
        });
        
        headOfFile.setNumberOfBlocks((int) Math.ceil((double)words.size()/headOfFile.getElementsInBlock()));
        headOfFile.setSizeOfUnitedElement(words.get(0).getElementSizeInBytes());
        headOfFile.setSizeOfHeadElementInBytes(2 * words.get(0).getKeyBytes().length + Integer.BYTES);
        return words;
    }
    
    private void saveBase(String binaryFileBase) throws IOException {
        if(inventory.isEmpty())
            throw new IllegalArgumentException("Nothing to build");
        
        BufferedOutputStream out;
        FileOutputStream file;
        file = new FileOutputStream(binaryFileBase);
        out = new BufferedOutputStream(file);
        
        out.write(headOfFile.toByte());
        writeBlocks(out);
        
        out.close();
        file.close();
    }   

    private void writeBlocks(BufferedOutputStream out) throws IOException {
        for (Pair<BlockHead, Block> pair : inventory) {
            out.write(pair.getKey().toBytes());
            out.write(pair.getValue().toBytes(headOfFile.getElementsInBlock()));
        }
    }

}
