package nndsa.sem.a.railway;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

public class Railway {

    private final String key;
    private int length;
    private final RailwayDirectionType direction;
    private final RailwayTrackType type;
    private int occupancy;

    public Railway(Railway railway) {
        this.key = railway.key;
        this.length = railway.length;
        this.direction = railway.direction;
        this.type = railway.type;
        this.occupancy = railway.occupancy;
    }

    public Railway(String key, int length, RailwayDirectionType direction, RailwayTrackType type, int occupancy) {
        if (occupancy < 0 || length <= 0) {
            throw new IllegalArgumentException("Length must be greather than 0 and occupancy cannot be less than 0!");
        }
        checkOccupancyLength(occupancy,length);
        this.key = (direction == RailwayDirectionType.BOTH) ? key : direction.getPrefix().concat(key);
        this.length = length;
        this.direction = direction;
        this.type = type;
        this.occupancy = occupancy;
    }

    public int getLength() {
        return length;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public int getSpace() {
        return length - occupancy;
    }

    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be greater than or equal to 0!");
        }
        
        checkOccupancyLength(this.occupancy, length);
        
        this.length = length;
    }

    private void checkOccupancyLength(int newOccupancy, int newLength) throws IllegalArgumentException {
        if (newOccupancy > newLength) {
            throw new IllegalArgumentException("Occupancy must be less than or equal to length!");
        }
    }

    public RailwayDirectionType getDirection() {
        return direction;
    }

    public String getKey() {
        return key;
    }

    public String getKeyWithoutPrefix() {
        return key.substring(direction.getPrefix().length());
    }

    public RailwayTrackType getType() {
        return type;
    }

    public boolean isOccupied() {
        return occupancy != 0;
    }

    public void setOccupancy(int occupancy) {
        if (occupancy < 0) {
            throw new IllegalArgumentException("Occupancy cannot be less than 0!");
        }
        
        checkOccupancyLength(occupancy, this.length);
        
        this.occupancy = occupancy;
    }

    public boolean isTrainAllowedToStop(int trainLength) {
        return getSpace() >= trainLength && type.isAllowedToStop();
    }

    public String adjencyRailwaysToString(List<Pair<String, RailwayDirectionType>> keyAdjencyRailways) {
        StringBuilder string = new StringBuilder();
        List<Pair<String, String>> adjencyRailways = new LinkedList<>();

        if (keyAdjencyRailways == null) {
            return string.toString();
        }

        int prefixLength = RailwayDirectionType.getPrefixLength();
        keyAdjencyRailways.forEach((key) -> {
            string.append(this.key.substring(prefixLength)).append(";").append(this.direction.getPrefix()).append(";")
                    .append(key.getKey()).append(";").append(key.getValue().getPrefix())
                    .append("\n");
        });

        return string.toString();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        int prefixLength = RailwayDirectionType.getPrefixLength();
        string.append(key.substring(prefixLength)).append(";")
                .append(length).append(";")
                .append(direction.getPrefix()).append(";")
                .append(type.toString()).append(";")
                .append(occupancy);
        return string.toString();
    }
}
