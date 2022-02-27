package nndsa.sem.a.railway;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

public class SimpleRailway {
    private final String key;
    private final int length;
    private final RailwayDirectionType direction;
    private final RailwayTrackType type;
    private final int occupancy;
    
    List<Pair<String, String>> adjencyRailways = new LinkedList<>();

    public SimpleRailway(String key, int length, RailwayDirectionType direction, RailwayTrackType type, int occupancy) {
        this.key = key;
        this.length = length;
        this.direction = direction;
        this.type = type;
        this.occupancy = occupancy;
    }
    
    public SimpleRailway(Railway railway) {
        this(railway, null);
    }
    
    public SimpleRailway(Railway railway, List<String> keyAdjencyRailways) {
        this.direction = railway.getDirection();
        this.key = railway.getKey().substring(direction.getPrefix().length());
        this.length = railway.getLength();
        this.type = railway.getType();
        this.occupancy = railway.getOccupancy();
        
        if (keyAdjencyRailways==null)
            return;
        
        keyAdjencyRailways.forEach((key) -> {
            adjencyRailways.add(new Pair<>(key.substring(direction.getPrefix().length()),key.substring(0, direction.getPrefix().length())));
        });
    }

    public String getKey() {
        return key;
    }

    public int getLength() {
        return length;
    }

    public RailwayDirectionType getDirection() {
        return direction;
    }

    public RailwayTrackType getType() {
        return type;
    }

    public int getOccupancy() {
        return occupancy;
    }
    
    public String adjencyRailwaysToString() {
        StringBuilder string =  new StringBuilder();
        
        adjencyRailways.forEach((neighbor) -> {
            string.append(key).append(";").append(direction.getPrefix()).append(";")
                .append(neighbor.getKey()).append(";").append(neighbor.getValue())
                .append("\n");
        });
        
        string.append(key).append(";").append(direction.getPrefix()).append(";")
                .append(key).append(";").append(direction.getPrefix());
        
        return string.toString();
    }

    @Override
    public String toString() {
        StringBuilder string =  new StringBuilder();
        string.append(key).append(";")
                .append(length).append(";")
                .append(direction.getPrefix()).append(";")
                .append(type.toString()).append(";")
                .append(occupancy);
        return string.toString();
    }
    
    
}
