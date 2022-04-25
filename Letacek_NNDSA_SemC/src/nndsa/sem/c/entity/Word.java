package nndsa.sem.c.entity;

import java.util.Arrays;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author ludek
 */
public class Word implements Element<String> {
    public static final Charset CHARSET = StandardCharsets.UTF_16;
    private final int PADDING = 30;
    private final String czechWord;
    private final String englishWord;
    private final String germanWord;

    public Word() {
        this.czechWord = "";
        this.englishWord = "";
        this.germanWord = "";
    }
    
    public Word(String czechWord, String englishWord, String germanWord) {
        this.czechWord = czechWord;
        this.englishWord = englishWord;
        this.germanWord = germanWord;
    }
    
    public Word(byte[] input) {
        int lengthByWord = input.length/3;
        this.czechWord = (new String(Arrays.copyOfRange(input, 0, lengthByWord), CHARSET)).trim();
        this.englishWord = (new String(Arrays.copyOfRange(input, lengthByWord, 2*lengthByWord), CHARSET)).trim();
        this.germanWord = (new String(Arrays.copyOfRange(input, 2*lengthByWord, input.length), CHARSET)).trim();
    }

    public String getCzechWord() {
        return czechWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getGermanWord() {
        return germanWord;
    }
    
    private byte[] getStringInBytes(String word) {
        return padding(word).getBytes(CHARSET);
    }

    private String padding(String word) {
         return String.format("%-" + PADDING + "s", (word.length()<PADDING) ? word : word.substring(0, PADDING));
    }
    
    @Override
    public String getKey() {
        return getCzechWord();
    }

    @Override
    public int getElementSizeInBytes() {
        return getWordInBytes().length;
    }
    
    @Override
    public byte[] getKeyBytes() {
        return getStringInBytes(getKey());
    }
    
    @Override
    public byte[] getWordInBytes() {
        int stringLength = getStringInBytes(czechWord).length;
        byte[] bytes = new byte[stringLength * 3];
        System.arraycopy(getStringInBytes(czechWord), 0, bytes, 0, stringLength);
        System.arraycopy(getStringInBytes(englishWord), 0, bytes, stringLength, stringLength);
        System.arraycopy(getStringInBytes(germanWord), 0, bytes, 2*stringLength, stringLength);
        return bytes;
    }
    
}
