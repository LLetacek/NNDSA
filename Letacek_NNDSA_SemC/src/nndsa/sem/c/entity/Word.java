package nndsa.sem.c.entity;

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

    public String getCzechWord() {
        return czechWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getGermanWord() {
        return germanWord;
    }
    
    public byte[] getCzechWordBytes() {
        return padding(czechWord).getBytes(CHARSET);
    }

    public byte[] getEnglishWordBytes() {
        return padding(englishWord).getBytes(CHARSET);
    }

    public byte[] getGermanWordBytes() {
        return padding(germanWord).getBytes(CHARSET);
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
        return getCzechWordBytes().length + getEnglishWordBytes().length + getGermanWordBytes().length;
    }
    
    
}
