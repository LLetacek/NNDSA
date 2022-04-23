package nndsa.sem.c.serialization;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import nndsa.sem.c.entity.Word;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
/**
 *
 * @author ludek
 */
public class Serialization {
    private final int elementsInBlock;
    private List<Word> inventory = new LinkedList<>();

    public Serialization() {
        this(100);
    }

    public Serialization(int elementsInBlock) {
        this.elementsInBlock = elementsInBlock;
    }
    
    public void buildBaseToBinaryFile(String csvFileBase, String binaryFileBase) throws IOException {
        loadBase(csvFileBase);
        saveBase(binaryFileBase);
    }
    
    private void loadBase(String csvFileBase) throws IOException {
        inventory = new LinkedList<>();
        try (Stream<String> file = Files.lines(Paths.get(csvFileBase), StandardCharsets.UTF_8)) {
            file.filter(t -> t != null)
                    .map((String line) -> {
                        String[] parse = line.split(";");
                        if(parse.length!=3)
                            throw new IllegalArgumentException("Base is not valid");
                        return new Word(parse[0], parse[1], parse[2]);
                    })
                    .forEach(((entity) -> {
                        inventory.add(entity);
                    }));
        }
        
        inventory.sort((a, b) -> {
            return a.getKey().compareTo(b.getKey());
        });
    }
    
    private void saveBase(String binaryFileBase) throws IOException {
        if(inventory.isEmpty())
            throw new IllegalArgumentException("Nothing to build");
        
        BufferedOutputStream out;
        FileOutputStream file;
        file = new FileOutputStream(binaryFileBase);
        out = new BufferedOutputStream(file);
        
        int elementsInBlock = this.elementsInBlock;
        writeHead(out, elementsInBlock);
        writeBlocks(out, elementsInBlock);
        
        out.close();
        file.close();
    }
    
    private void writeInteger(BufferedOutputStream out, int value) throws IOException {
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
        out.write(bytes);
    }
    
    private void writeHead(BufferedOutputStream out, int elementsInBlock) throws IOException {
        int numberOfBlocks = (int) Math.ceil((double)inventory.size()/elementsInBlock);
        int sizeOfUnitedElement = inventory.get(0).getElementSizeInBytes();
        int sizeOfHeadElementInBytes = 2 * inventory.get(0).getCzechWordBytes().length + Integer.BYTES;
        
        // head
        writeInteger(out, numberOfBlocks);
        writeInteger(out, elementsInBlock);
        writeInteger(out, sizeOfHeadElementInBytes);
        writeInteger(out, sizeOfUnitedElement);
    }

    private void writeBlocks(BufferedOutputStream out, int elementsInBlock) throws IOException {
        Word elementPadding = new Word();
        int indexStart = 0;
        // printout
        //int counter = 0;
        while(true) {
            if(indexStart>inventory.size())
                break;
            
            List<Word> blockToWrite = (indexStart + elementsInBlock)<=inventory.size() ? 
                    (inventory.subList(indexStart, indexStart + elementsInBlock)) :
                    (inventory.subList(indexStart, inventory.size()));
            
            // block head - first word - last word - id of last word
            out.write(blockToWrite.get(0).getCzechWordBytes());
            out.write(blockToWrite.get(blockToWrite.size()-1).getCzechWordBytes());
            writeInteger(out, blockToWrite.size()-1);
            
            // printout
            //System.out.println(counter++ + "---------------------------");
            
            // block elements
            for(int i=0; i<(elementsInBlock); ++i) {
                Word toWrite = (i>=blockToWrite.size()) ? elementPadding : blockToWrite.get(i);
                out.write(toWrite.getCzechWordBytes());
                out.write(toWrite.getEnglishWordBytes());
                out.write(toWrite.getGermanWordBytes());
                // printout
                //System.out.println(new String(toWrite.getCzechWordBytes(), Word.CHARSET));
            }
            
            indexStart += elementsInBlock;
        }
    }

    
}
