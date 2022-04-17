
package nndsa.sem.c;

import nndsa.sem.c.entity.SearchEngine;
import nndsa.sem.c.entity.SearchType;
import nndsa.sem.c.serialization.Serialization;

/**
 *
 * @author ludek
 */
public class Letacek_NNDSA_SemC {

    public static void main(String[] args) {
        // TODO code application logic here
        Serialization serialization = new Serialization();
        SearchEngine searchEngine = new SearchEngine();
        
        try {
            serialization.buildBaseToBinaryFile("./main.csv", "./out");
            System.out.println(searchEngine.findInBinaryBase("kopie", "./out",SearchType.BINARY));
            System.out.println(searchEngine.findInBinaryBase("kopie", "./out",SearchType.INTERPOLATION));
            
            System.out.println(searchEngine.findInBinaryBase("abakus", "./out",SearchType.BINARY));
            System.out.println(searchEngine.findInBinaryBase("abakus", "./out",SearchType.INTERPOLATION));
        } catch(Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        
    }
    
}
