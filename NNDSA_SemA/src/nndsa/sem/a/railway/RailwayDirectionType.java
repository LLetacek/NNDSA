package nndsa.sem.a.railway;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum RailwayDirectionType {
    THERE("2"),
    BACK("1"),
    BOTH(" ");
    
    private static final int prefixLength = 1;
    private final String prefix;
    private static final List<RailwayDirectionType> values = Collections.unmodifiableList
                                                        (Arrays.asList(values()));
    
    private RailwayDirectionType(String prefix){
        if(prefixLength!=prefix.length())
            throw new IllegalArgumentException("Error in prefix");
        
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
    
    public static RailwayDirectionType getValue(String prefix) {
        for (RailwayDirectionType value : values) {
            if (value.prefix.compareToIgnoreCase(prefix)==0) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public static int getPrefixLength() {
        return prefixLength;
    }
}
