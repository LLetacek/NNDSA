package nndsa.sem.a.railway;

public class Railway {
    
    private final String key;
    private int length;
    private final RailwayDirectionType direction;
    private final RailwayTrackType type;
    private int occupancy;

    public Railway(String key, int length, RailwayDirectionType direction, RailwayTrackType type, int occupancy) {
        this.key = direction.getPrefix().concat(key);
        this.length = length;
        this.direction = direction;
        this.type = type;
        this.occupancy = occupancy;
    }

    public int getLength() {
        return length;
    }
    
    public int getSpace() {
        return length - occupancy;
    }

    public void setLength(int length) {
        if(length < 0)
            throw new IllegalArgumentException("Length must be greater than or equal to 0!");
        this.length = length;
    }

    public RailwayDirectionType getDirection() {
        return direction;
    }

    public String getKey() {
        return key;
    }

    public RailwayTrackType getType() {
        return type;
    }
    
    public boolean isOccupied() {
        return occupancy != 0;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
    
    public boolean isTrainAllowedToStop(int trainLength) {
        return getSpace() >= trainLength && type.isAllowedToStop();
    }
}
