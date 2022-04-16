package nndsa.sem.c.entity;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author ludek
 */
public class Word implements Element<String> {
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
        return padding(czechWord);
    }

    public String getEnglishWord() {
        return padding(englishWord);
    }

    public String getGermanWord() {
        return padding(germanWord);
    }
    
    public byte[] getCzechWordBytes() {
        return padding(czechWord).getBytes(StandardCharsets.UTF_16);
    }

    public byte[] getEnglishWordBytes() {
        return padding(englishWord).getBytes(StandardCharsets.UTF_16);
    }

    public byte[] getGermanWordBytes() {
        return padding(germanWord).getBytes(StandardCharsets.UTF_16);
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
