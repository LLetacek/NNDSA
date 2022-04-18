package nndsa.sem.c.entity;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Math.max;
import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import java.util.ArrayList;
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
    private List<Word> buffer;

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
                getInterpolationShift(key, startWord, endWord, endPosition - startPosition);
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

        return new Pair<>(counterOfBlockTransfers, getWord(key, type));
    }

    private void readHead(BufferedInputStream inputStream) throws IOException {
        numberOfBlocks = readInteger(inputStream);
        elementsInBlock = readInteger(inputStream);
        sizeOfHeadElementInBytes = readInteger(inputStream);
        sizeOfUnitedElement = readInteger(inputStream);
        sizeOfString = (sizeOfHeadElementInBytes - Integer.BYTES) / 2;
    }

    private int findInBlock(SearchType type, BufferedInputStream inputStream, String key, int sizeOfBlock) throws IndexOutOfBoundsException, IOException {
        // -1 => pointer on last block
        int endPosition = numberOfBlocks - 1;
        int startPosition = 0;
        int counterOfBlockTransfers = 0;
        int shiftPosition = 0;
        double shift;
        String startBlockWord;
        String endBlockWord;
        while (true) {
            mark(inputStream, endPosition, startPosition);
            shift = (type == SearchType.BINARY)
                    ? (double) 1 / 2
                    : calculateShiftPosition(inputStream, key, startPosition, endPosition);

            shiftPosition = (int) ((endPosition - startPosition) * shift);
            skip(inputStream, shiftPosition * sizeOfBlock);

            // check of block
            ++counterOfBlockTransfers;
            startBlockWord = readWord(inputStream);
            endBlockWord = readWord(inputStream);
            if (startBlockWord.compareTo(key) <= 0 && key.compareTo(endBlockWord) <= 0) {
                break;
            }

            if (startPosition >= endPosition) {
                throw new IndexOutOfBoundsException("Not found");
            } else if (key.compareTo(startBlockWord) < 0) {
                // reset -> after mark (in beginning after the head)
                if (inputStream.markSupported()) {
                    inputStream.reset();
                }
                endPosition = startPosition + shiftPosition - 1;
            } else if (endBlockWord.compareTo(key) < 0) {
                // mark -> finish reading element's head + block
                skip(inputStream, Integer.BYTES + (elementsInBlock * sizeOfUnitedElement));
                startPosition = startPosition + shiftPosition + 1;
            }
        }
        return counterOfBlockTransfers;
    }

    private void mark(BufferedInputStream inputStream, int endPosition, int startPosition) {
        inputStream.mark((endPosition - startPosition) * (sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement)));
    }

    private void loadBlock(BufferedInputStream inputStream) throws IOException {
        buffer.clear();
        int endPosition = readInteger(inputStream);

        for (int i = 0; i <= endPosition; ++i) {
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

    private double calculateShiftPosition(BufferedInputStream inputStream, String key, int startPosition, int endPosition) throws IOException {
        //read
        String startWord = readWord(inputStream);
        //reset
        inputStream.reset();
        // +1 -> increase the space so that the mark is still valid -> because of reading of head)
        mark(inputStream, endPosition + 1, startPosition);
        skip(inputStream, (endPosition - startPosition) * (sizeOfHeadElementInBytes + (elementsInBlock * sizeOfUnitedElement)));
        //read
        readWord(inputStream);
        String endWord = readWord(inputStream);
        //reset
        inputStream.reset();
        mark(inputStream, endPosition, startPosition);
        double shift = getInterpolationShift(key, startWord, endWord, endPosition - startPosition);
        if(shift>1 || shift<0) {
            throw new IndexOutOfBoundsException("Word out of range");
        }
        return shift;
    }

    private static double getInterpolationShift(String key, String startWord, String endWord, int range) {
        int maxLength = max(max(endWord.length(), startWord.length()), key.length());
        BigInteger end = new BigInteger(1, copyOf(endWord.getBytes(Word.CHARSET), maxLength));
        BigInteger start = new BigInteger(1, copyOf(startWord.getBytes(Word.CHARSET), maxLength));
        BigInteger target = new BigInteger(1, copyOf(key.getBytes(Word.CHARSET), maxLength));

        BigInteger divisor = end.subtract(start);
        return ZERO.equals(divisor) ? 0 : ((target.subtract(start)).doubleValue()/(divisor.doubleValue()));
        //return (double) startWord.compareTo(key) / (startWord.compareTo(endWord));
    }

    private void skip(BufferedInputStream inputStream, long bytes) throws IOException {
        long realSkip;
        long difference;
        do {
            realSkip = inputStream.skip(bytes);
            difference = bytes - realSkip;
            bytes = (difference >= 0) ? difference : bytes;
        } while (realSkip > 0);
    }
}
