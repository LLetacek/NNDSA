package nndsa.sem.c.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author ludek
 */
public class Head {
    private final int elementsInBlock;
    private int numberOfBlocks;
    private int sizeOfUnitedElement;
    private int sizeOfHeadElementInBytes;

    public Head(int elementsInBlock) {
        this.elementsInBlock = elementsInBlock;
    }

    public Head(byte[] input) {
        numberOfBlocks = readInt(input, 0);
        elementsInBlock = readInt(input, Integer.BYTES);
        sizeOfHeadElementInBytes = readInt(input, 2*Integer.BYTES);
        sizeOfUnitedElement = readInt(input, 3*Integer.BYTES);
    }
    
    private int readInt(byte[] bytes, int from) {
        return ByteBuffer.wrap(Arrays.copyOfRange(bytes, from, from + Integer.BYTES)).getInt();
    }

    public int getElementsInBlock() {
        return elementsInBlock;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }

    public int getSizeOfUnitedElement() {
        return sizeOfUnitedElement;
    }

    public int getSizeOfHeadElementInBytes() {
        return sizeOfHeadElementInBytes;
    }

    public void setNumberOfBlocks(int numberOfBlocks) {
        this.numberOfBlocks = numberOfBlocks;
    }

    public void setSizeOfUnitedElement(int sizeOfUnitedElement) {
        this.sizeOfUnitedElement = sizeOfUnitedElement;
    }

    public void setSizeOfHeadElementInBytes(int sizeOfHeadElementInBytes) {
        this.sizeOfHeadElementInBytes = sizeOfHeadElementInBytes;
    }
    
    public byte[] toByte() {
        byte[] head = new byte[4*Integer.BYTES];
        System.arraycopy(integerToByteArray(numberOfBlocks), 0, head, 0, Integer.BYTES);
        System.arraycopy(integerToByteArray(elementsInBlock), 0, head, Integer.BYTES, Integer.BYTES);
        System.arraycopy(integerToByteArray(sizeOfHeadElementInBytes), 0, head, 2*Integer.BYTES, Integer.BYTES);
        System.arraycopy(integerToByteArray(sizeOfUnitedElement), 0, head, 3*Integer.BYTES, Integer.BYTES);
        return head;
    }
    
    private byte[] integerToByteArray(int value) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
    }
}
