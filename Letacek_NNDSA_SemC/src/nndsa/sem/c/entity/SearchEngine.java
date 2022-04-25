package nndsa.sem.c.entity;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.Math.max;
import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.copyOf;
import java.util.List;
import javafx.util.Pair;

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
    private final List<Word> buffer;

    public SearchEngine() {
        numberOfBlocks = 0;
        elementsInBlock = 0;
        sizeOfHeadElementInBytes = 0;
        sizeOfUnitedElement = 0;
        sizeOfString = 0;
        buffer = new ArrayList<>();
    }

    public List<Word> getBuffer() {
        return buffer;
    }

    private Word getWord(String key, SearchType type) {
        double shift = (double) 1 / 2;
        int startPosition = 0;
        int endPosition = buffer.size() - 1;
        int index = 0;

        while (true) {
            if (type == SearchType.INTERPOLATION) {
                String startWord = buffer.get(startPosition).getKey();
                String endWord = buffer.get(endPosition).getKey();
                getInterpolationShift(key, startWord, endWord);
            }
            index = startPosition + (int) ((endPosition - startPosition) * shift);
            if (key.equals(buffer.get(index).getKey())) {
                break;
            } else if (buffer.get(index).getKey().compareTo(key) < 0) {
                startPosition = index + 1;
            } else if (key.compareTo(buffer.get(index).getKey()) < 0) {
                endPosition = index - 1;
            }
            
            if(endPosition<startPosition)
                throw new IndexOutOfBoundsException("Not found in block!");
        }
        
        return buffer.get(index);
    }

    public Pair<Integer, Word> findInBinaryBase(String key, String binaryFileBase, SearchType type) throws FileNotFoundException, IOException {
        int counterOfBlockTransfers = 0;

        RandomAccessFile reader;
        reader = new RandomAccessFile(binaryFileBase,"r");

        readHead(reader);
        int sizeOfBlock = sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement);

        counterOfBlockTransfers = findInMedium(type, reader, key, sizeOfBlock);
        
        reader.close();
        return new Pair<>(counterOfBlockTransfers, getWord(key, type));
    }

    private void readHead(RandomAccessFile reader) throws IOException {
        byte[] head = new byte[4*Integer.BYTES];
        reader.read(head);
        numberOfBlocks = readInt(head, 0);
        elementsInBlock = readInt(head, Integer.BYTES);
        sizeOfHeadElementInBytes = readInt(head, 2*Integer.BYTES);
        sizeOfUnitedElement = readInt(head, 3*Integer.BYTES);
        sizeOfString = (sizeOfHeadElementInBytes - Integer.BYTES) / 2;
    }
    
    private int readInt(byte[] bytes, int from) {
        return ByteBuffer.wrap(Arrays.copyOfRange(bytes, from, from + Integer.BYTES)).getInt();
    }

    private int findInMedium(SearchType type, RandomAccessFile reader, String key, int sizeOfBlock) throws IndexOutOfBoundsException, IOException {
        int endPosition = numberOfBlocks - 1;
        int startPosition = 0;
        int counterOfBlockTransfers = 0;
        int shiftPosition = 0;
        double shift;
        String startBlockWord;
        String endBlockWord;
        long markPosition;
        BlockHead head;
        while (true) {
            markPosition = reader.getFilePointer();
            shift = (type == SearchType.BINARY)
                    ? (double) 1 / 2
                    : calculateShiftPosition(reader, key, startPosition, endPosition);

            shiftPosition = (int) ((endPosition - startPosition) * shift);
            reader.seek(reader.getFilePointer() + (shiftPosition * sizeOfBlock));

            // check of block
            ++counterOfBlockTransfers;
            head = getBlockHead(reader);
            
            if (head.getFirstWord().compareTo(key) <= 0 && key.compareTo(head.getLastWord()) <= 0) {
                break;
            }

            if (startPosition >= endPosition) {
                throw new IndexOutOfBoundsException("Not found");
            } else if (key.compareTo(head.getFirstWord()) < 0) {
                // reset -> after mark
                reader.seek(markPosition);
                endPosition = startPosition + shiftPosition - 1;
            } else if (head.getLastWord().compareTo(key) < 0) {
                // finish reading element's head + block
                reader.seek(reader.getFilePointer() + (elementsInBlock * sizeOfUnitedElement));
                startPosition = startPosition + shiftPosition + 1;
            }
        }
        loadBlock(reader, head.getPositionOfLastWordInBlock());
        return counterOfBlockTransfers;
    }

    private void loadBlock(RandomAccessFile reader, int lastValidWord) throws IOException {
        buffer.clear();
        byte[] blockInBytes = new byte[(lastValidWord+1) * sizeOfUnitedElement];
        reader.read(blockInBytes);
        Block block = new Block(blockInBytes, sizeOfUnitedElement);
        buffer.addAll(block.getElements());
    }
    
    private BlockHead getBlockHead(RandomAccessFile reader) throws IOException {
        byte[] headInBytes = new byte[sizeOfHeadElementInBytes];
        reader.read(headInBytes);
        return new BlockHead(headInBytes);
    }

    private double calculateShiftPosition(RandomAccessFile reader, String key, int startPosition, int endPosition) throws IOException {
        long markPosition = reader.getFilePointer();
        // read beginning of the range
        BlockHead head = getBlockHead(reader);
        String startWord = head.getFirstWord();
        // read end of the range
        reader.seek(markPosition + (endPosition - startPosition) * (sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement)));
        head = getBlockHead(reader);
        String endWord = head.getLastWord();
        // reset
        reader.seek(markPosition);
        double shift = getInterpolationShift(key, startWord, endWord);
        if(shift>1 || shift<0) {
            throw new IndexOutOfBoundsException("Word out of range");
        }
        return shift;
    }

    private static double getInterpolationShift(String key, String startWord, String endWord) {
        int maxLength = max(max(endWord.length(), startWord.length()), key.length());
        BigInteger end = new BigInteger(1, copyOf(endWord.getBytes(Word.CHARSET), maxLength));
        BigInteger start = new BigInteger(1, copyOf(startWord.getBytes(Word.CHARSET), maxLength));
        BigInteger target = new BigInteger(1, copyOf(key.getBytes(Word.CHARSET), maxLength));

        BigInteger divisor = end.subtract(start);
        return ZERO.equals(divisor) ? 0 : ((target.subtract(start)).doubleValue()/(divisor.doubleValue()));
    }

}
