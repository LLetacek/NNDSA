package nndsa.sem.c.entity;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ludek
 */
public class SearchEngine {
    
    private int numberOfBlocks;
    private int elementsInBlock;
    private int sizeOfHeadElementInBytes;
    private int sizeOfUnitedElement;
    private int sizeOfString;
    private int markLimit;
    private List<Word> buffer;

    public SearchEngine() {
        numberOfBlocks = 0;
        elementsInBlock = 0;
        sizeOfHeadElementInBytes = 0;
        sizeOfUnitedElement = 0;
        sizeOfString = 0;
        markLimit = 0;
        buffer = new ArrayList<>();
    }
    
    public int findInBinaryBase(String key, String binaryFileBase, SearchType type) throws FileNotFoundException, IOException {
        int counterOfBlockTransfers = 0;

        BufferedInputStream inputStream;
        FileInputStream file;
        file = new FileInputStream(binaryFileBase);
        inputStream = new BufferedInputStream(file);
        
        readHead(inputStream);
        int sizeOfBlock = sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement);
        
        counterOfBlockTransfers = findInBlock(type, inputStream, key, sizeOfBlock);
        loadBlock(inputStream);
        
        inputStream.close();
        file.close();
        
        return counterOfBlockTransfers;
    }

    private void readHead(BufferedInputStream inputStream) throws IOException {
        numberOfBlocks = readInteger(inputStream);
        elementsInBlock = readInteger(inputStream);
        sizeOfHeadElementInBytes = readInteger(inputStream);
        sizeOfUnitedElement = readInteger(inputStream);
        sizeOfString = (sizeOfHeadElementInBytes - Integer.BYTES) / 2;
        markLimit = numberOfBlocks * (sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement));
    }

    private int findInBlock(SearchType type, BufferedInputStream inputStream, String key, int sizeOfBlock) throws IndexOutOfBoundsException, IOException {
        // -1 => pointer on last block
        int endPosition = numberOfBlocks-1;
        int startPosition = 0;
        int counterOfBlockTransfers = 0;
        int shiftPosition = 0;
        double shift;
        String startBlockWord;
        String endBlockWord;
        while(true) {
            shift = (type==SearchType.BINARY)
                    ? (double) 1/2
                    : calculateShiftPosition(inputStream, key, startPosition, endPosition);

            inputStream.mark(getMarkLimit(shift));
            
            shiftPosition = (int)((endPosition - startPosition) * shift);
            skip(inputStream, shiftPosition * sizeOfBlock);
            
            // TODO check of block
            ++counterOfBlockTransfers;
            startBlockWord = readWord(inputStream);
            endBlockWord = readWord(inputStream);
            if(startBlockWord.compareTo(key) <= 0 && key.compareTo(endBlockWord) <= 0) {
                break;
            }
            
            if(startPosition==endPosition) {
                throw new IndexOutOfBoundsException("Not found");
            }
            else if(key.compareTo(startBlockWord) < 0) {
                // reset -> after mark (in beginning after the head)
                if(inputStream.markSupported()) 
                    inputStream.reset();
                endPosition = shiftPosition - 1;
            }
            else if(endBlockWord.compareTo(key) < 0) {
                // mark -> finish reading element's head + block
                skip(inputStream, Integer.BYTES + (elementsInBlock * sizeOfUnitedElement));
                startPosition = shiftPosition + 1;
            }
        }
        return counterOfBlockTransfers;
    }
    
    private void loadBlock(BufferedInputStream inputStream) throws IOException {
        buffer.clear();
        int endPosition = readInteger(inputStream);
        
        for(int i=0; i<=endPosition; ++i) {
            buffer.add(new Word(readWord(inputStream), readWord(inputStream), readWord(inputStream)));
        }
    }
    
    private String readWord(BufferedInputStream inputStream) throws IOException {
        byte[] value = new byte[sizeOfString];
        inputStream.read(value);
        return (new String(value, Word.CHARSET)).trim();
    }

    private int readInteger(BufferedInputStream inputStream) throws IOException {
        byte[] value = new byte[Integer.BYTES];
        inputStream.read(value);
        return new BigInteger(value).intValue();
    }

    private double calculateShiftPosition(BufferedInputStream inputStream, String key, int startPosition, int endPosition) {
        return (double) 1/2;
    }
    
    private int getMarkLimit(double usedPart) {
        markLimit = (int)Math.ceil(markLimit * (double) usedPart);
        return markLimit;
    }

    private void skip(BufferedInputStream inputStream, long bytes) throws IOException {
        long realSkip;
        long difference;
        do {
            realSkip = inputStream.skip(bytes);
            difference = bytes - realSkip;
            bytes = (difference>=0) ? difference : bytes;
        } while(realSkip>0);
    }
}
