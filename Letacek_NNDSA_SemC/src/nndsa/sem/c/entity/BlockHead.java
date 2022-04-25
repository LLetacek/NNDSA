package nndsa.sem.c.entity;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author ludek
 */
public class BlockHead {

    private Word firstWord;
    private Word lastWord;
    private int positionOfLastWordInBlock;

    public BlockHead(Word firstWord, Word lastWord, int positionOfLastWordInBlock) {
        this.firstWord = firstWord;
        this.lastWord = lastWord;
        this.positionOfLastWordInBlock = positionOfLastWordInBlock;
    }

    public BlockHead(byte[] input) {
        int lengthByWord = (input.length-Integer.BYTES)/2;
        this.firstWord = new Word((new String(Arrays.copyOfRange(input, 0, lengthByWord), Word.CHARSET)).trim(),"","");
        this.lastWord = new Word((new String(Arrays.copyOfRange(input, lengthByWord, 2*lengthByWord), Word.CHARSET)).trim(),"","");
        this.positionOfLastWordInBlock = ByteBuffer.wrap(Arrays.copyOfRange(input, 2*lengthByWord, input.length)).getInt();
    }

    public byte[] toBytes() {
        int firstKeyLength = lastWord.getKeyBytes().length;
        byte[] bytes = new byte[firstKeyLength * 2 + Integer.BYTES];
        System.arraycopy(firstWord.getKeyBytes(), 0, bytes, 0, firstKeyLength);
        System.arraycopy(lastWord.getKeyBytes(), 0, bytes, firstKeyLength, firstKeyLength);
        System.arraycopy(integerToByteArray(positionOfLastWordInBlock), 0, bytes, 2*firstKeyLength, Integer.BYTES);
        return bytes;
    }
    
    private byte[] integerToByteArray(int value) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
    }

    public String getFirstWord() {
        return firstWord.getKey();
    }

    public String getLastWord() {
        return lastWord.getKey();
    }

    public int getPositionOfLastWordInBlock() {
        return positionOfLastWordInBlock;
    }

}
