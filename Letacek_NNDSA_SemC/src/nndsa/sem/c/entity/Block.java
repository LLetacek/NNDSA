package nndsa.sem.c.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ludek
 */
public class Block {
    private List<Word> elements;

    public Block(List<Word> elements) {
        this.elements = elements;
    }

    public Block(byte[] input, int sizeOfUnitedElement) {
        elements = new LinkedList<>();
        int elementsInBlock = input.length/sizeOfUnitedElement;
        for(int i=0; i<elementsInBlock; ++i) {
            elements.add(new Word(Arrays.copyOfRange(input, i*sizeOfUnitedElement, (i+1)*sizeOfUnitedElement)));
        }
    }
    
    public byte[] toBytes(int elementsInBlock) {
        if(elements==null || elements.isEmpty())
            return null;
        
        int elementSize = elements.get(0).getElementSizeInBytes();
        byte[] bytes = new byte[elementsInBlock * elementSize];
        int startPointer = 0;
        Word elementPadding = new Word();
        for(int i=0; i<elementsInBlock; ++i) {
            Word toWrite = (i>=elements.size()) ? elementPadding : elements.get(i);
            System.arraycopy(toWrite.getWordInBytes(), 0, bytes, startPointer * elementSize,  elementSize);
            ++startPointer;
        }
        return bytes;
    }

    public List<Word> getElements() {
        return elements;
    }

}
