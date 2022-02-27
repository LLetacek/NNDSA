package nndsa.sem.a.railway;

public enum RailwayDirectionType {
    THERE("2"),
    BACK("1");
    
    private final String prefix;
    
    private RailwayDirectionType(String prefix){
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
