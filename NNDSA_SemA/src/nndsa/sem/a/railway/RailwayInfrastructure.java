package nndsa.sem.a.railway;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import nndsa.sem.a.util.ErrorMessage;
import nndsa.sem.a.graph.Graph;
import nndsa.sem.a.graph.IGraph;

public class RailwayInfrastructure {

    IGraph<String, Railway> infrastructure;

    public RailwayInfrastructure() {
        infrastructure = new Graph<>();
    }

    public void addRailway(String key, int length, RailwayTrackType type) {
        addRailway(key, length, type, 0);
    }

    public void addRailway(String key, int length, RailwayTrackType type, int occupancy) {
        Railway railwayThere = new Railway(key, length, RailwayDirectionType.THERE, type, occupancy);
        Railway railwayBack = new Railway(key, length, RailwayDirectionType.BACK, type, occupancy);
        infrastructure.addNode(key, railwayThere);
        infrastructure.addNode(key, railwayBack);
    }

    public void addConnection(String keyStart, String keyDestination) {
        String keyStartThere = RailwayDirectionType.THERE.getPrefix().concat(keyStart);
        String keyDestinationThere = RailwayDirectionType.THERE.getPrefix().concat(keyDestination);
        Railway startThere = null;
        Railway destinationThere = null;

        try {
            startThere = infrastructure.getNodeData(keyStartThere);
            destinationThere = infrastructure.getNodeData(keyDestinationThere);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ErrorMessage.doesNotExist);
        }

        if (startThere.getType() == RailwayTrackType.DIRECT
                && destinationThere.getType() == RailwayTrackType.DIRECT) {
            throw new IllegalArgumentException(ErrorMessage.cantConnect);
        }

        String keyStartBack = RailwayDirectionType.BACK.getPrefix().concat(keyStart);
        String keyDestinationBack = RailwayDirectionType.BACK.getPrefix().concat(keyDestination);

        infrastructure.addEdge(keyDestinationBack, keyStartBack);
        infrastructure.addEdge(keyStartThere, keyDestinationThere);

        // line to change direction
        if (startThere.getType() == RailwayTrackType.DIRECT
                && destinationThere.getType() == RailwayTrackType.SWITCH) {
            infrastructure.addEdge(keyStartBack, keyDestinationThere);
        } else if (startThere.getType() == RailwayTrackType.SWITCH
                && destinationThere.getType() == RailwayTrackType.DIRECT) {
            infrastructure.addEdge(keyDestinationThere, keyStartBack);
        }
    }

    public void deleteRailway(String key) {
        try {
            infrastructure.deleteNode(RailwayDirectionType.THERE.getPrefix().concat(key));
            infrastructure.deleteNode(RailwayDirectionType.BACK.getPrefix().concat(key));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ErrorMessage.doesNotExist);
        }
    }

    public void deleteConnection(String keyStart, String keyDestination) {
        try {
            infrastructure.deleteEdge(
                    RailwayDirectionType.THERE.getPrefix().concat(keyStart),
                    RailwayDirectionType.THERE.getPrefix().concat(keyDestination));
            infrastructure.deleteEdge(
                    RailwayDirectionType.BACK.getPrefix().concat(keyDestination),
                    RailwayDirectionType.BACK.getPrefix().concat(keyStart));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ErrorMessage.doesNotExist);
        }
    }
    
    public Railway getRailway(String key, RailwayDirectionType direction) {
        return infrastructure.getNodeData(direction.getPrefix().concat(key));
    }
    
    public List<String> getAllRailwayKeys() {
        List<String> keys = infrastructure.getAllNodeKeys();
        return getUniqueKeysWithoutPrefix(keys);
    }
    
    public List<String> getConnectedRailwayKeys(String key) {
        List<String> keys = new LinkedList<>();
        keys.addAll(infrastructure.getAdjencyNodeKeys(
                RailwayDirectionType.THERE.getPrefix().concat(key)));
        keys.addAll(infrastructure.getAdjencyNodeKeys(
                RailwayDirectionType.BACK.getPrefix().concat(key)));
        
        return getUniqueKeysWithoutPrefix(keys);
    }
    
    private List<String> getUniqueKeysWithoutPrefix(List<String> list) {
        Set<String> uniqueKeys = new LinkedHashSet<>();
        for (int i = 0; i < list.size(); i++) {
            uniqueKeys.add(list.get(i).substring(1));
        }
        return new LinkedList<>(uniqueKeys);
    }

    public List<Railway> findShortestPath(String keyStart, String keyDestination, int lengthOfTrain) {
        throw new UnsupportedOperationException();
    }

}
