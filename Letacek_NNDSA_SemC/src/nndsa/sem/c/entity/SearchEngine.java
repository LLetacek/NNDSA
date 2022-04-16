package nndsa.sem.c.entity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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
    private List<Word> buffer;

    public SearchEngine() {
        numberOfBlocks = 0;
        elementsInBlock = 0;
        sizeOfHeadElementInBytes = 0;
        sizeOfUnitedElement = 0;
        buffer = new ArrayList<>();
    }
    
    public int findInBinaryBase(String key, String binaryFileBase) throws FileNotFoundException, IOException {
        int counterOfBlockTransfers = 0;
        
        BufferedInputStream inputStream;
        FileInputStream file;
        file = new FileInputStream(binaryFileBase);
        inputStream = new BufferedInputStream(file);
        buffer.clear();
        
        //head
        int sizeOfHead = 4 * Integer.BYTES;
        numberOfBlocks = readInteger(inputStream);
        elementsInBlock = readInteger(inputStream);
        sizeOfHeadElementInBytes = readInteger(inputStream);
        inputStream.mark(sizeOfHeadElementInBytes);
        sizeOfUnitedElement = readInteger(inputStream);
        System.out.println(sizeOfUnitedElement);
        if(inputStream.markSupported()) {
            inputStream.reset();
            System.out.println(readInteger(inputStream));
        }
        
        inputStream.close();
        file.close();
        
        return counterOfBlockTransfers;
    }

    private int readInteger(BufferedInputStream marker) throws IOException {
        byte[] value = new byte[Integer.BYTES];
        marker.read(value);
        return new BigInteger(value).intValue();
    }

    private void findWordByBinarySearch(ObjectInputStream in) {
        
    }
    
    private void findWordByInterpolationSearch(ObjectInputStream in) {
        
    }
    
}
