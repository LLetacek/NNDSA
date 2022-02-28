package nndsa.sem.a.railway;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import javafx.util.Pair;
import nndsa.sem.a.util.ErrorMessage;
import nndsa.sem.a.graph.Graph;
import nndsa.sem.a.graph.IGraph;

public class RailwayInfrastructure {

    private final IGraph<String, Railway> infrastructure;

    public RailwayInfrastructure() {
        infrastructure = new Graph<>();
    }

    public void addRailway(Railway railway) {
        infrastructure.addNode(railway.getKey(), railway);
    }
    
    public void addSimpleConnection(String keyStart, RailwayDirectionType typeStart, 
            String keyDestination, RailwayDirectionType typeDestination) {

        keyStart = typeStart.getPrefix().concat(keyStart);
        keyDestination = typeDestination.getPrefix().concat(keyDestination);
        infrastructure.addEdge(keyStart, keyDestination);
    }
    
    public void addRailway(String key, int length, RailwayTrackType type) {
        RailwayInfrastructure.this.addRailway(key, length, type, 0);
    }

    public void addRailway(String key, int length, RailwayTrackType type, int occupancy) {
        Railway railwayThere = new Railway(key, length, RailwayDirectionType.THERE, type, occupancy);
        Railway railwayBack = new Railway(key, length, RailwayDirectionType.BACK, type, occupancy);
        infrastructure.addNode(railwayThere.getKey(), railwayThere);
        infrastructure.addNode(railwayBack.getKey(), railwayBack);
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

        infrastructure.deleteEdgeIfExists(
                RailwayDirectionType.THERE.getPrefix().concat(keyStart),
                RailwayDirectionType.BACK.getPrefix().concat(keyDestination));

        infrastructure.deleteEdgeIfExists(
                RailwayDirectionType.BACK.getPrefix().concat(keyStart),
                RailwayDirectionType.THERE.getPrefix().concat(keyDestination));
    }

    public Railway getRailway(String key, RailwayDirectionType direction) {
        return infrastructure.getNodeData(direction.getPrefix().concat(key));
    }

    public void setOccupancy(String key, int length) {
        getRailway(key, RailwayDirectionType.THERE).setOccupancy(length);
        getRailway(key, RailwayDirectionType.BACK).setOccupancy(length);
    }

    public List<String> getAllRailwayKeys() {
        List<String> keys = infrastructure.getAllNodeKeys();
        return getUniqueKeysWithoutPrefix(keys);
    }
    
    public List<Pair<String, RailwayDirectionType>> getAllRailwayKeysDirection() {
        List<String> keys = infrastructure.getAllNodeKeys();
        List<Pair<String,RailwayDirectionType>> result = new LinkedList<>();
        int prefixLength = RailwayDirectionType.getPrefixLength();
        for (int i = 0; i < keys.size(); i++) {
            result.add(
                    new Pair<>(
                            keys.get(i).substring(prefixLength),
                            RailwayDirectionType.getValue(keys.get(i).substring(0, prefixLength))));
        }
        return result;
    }
    
     public List<Pair<String, RailwayDirectionType>> getConnectedRailwayKeysDirection(String key, RailwayDirectionType direction) {
        List<Pair<String, RailwayDirectionType>> neighbors = new LinkedList<>();
        int prefixLength = RailwayDirectionType.getPrefixLength();
        infrastructure.getAdjencyNodeKeys(direction.getPrefix().concat(key)).forEach((neighbor) -> {
            neighbors.add(new Pair<>(
                            neighbor.substring(prefixLength),
                            RailwayDirectionType.getValue(neighbor.substring(0, prefixLength))));
        });
        return neighbors;
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
            uniqueKeys.add(list.get(i).substring(RailwayDirectionType.getPrefixLength()));
        }
        return new LinkedList<>(uniqueKeys);
    }

    public int getShortestDistance(String keyStart, String keyDestination,
            RailwayDirectionType trainDirectionStart, RailwayDirectionType trainDirectionEnd, int lengthOfTrain) {

        return getShortestPathByNode(keyStart, keyDestination, trainDirectionStart, trainDirectionEnd, lengthOfTrain).distance + lengthOfTrain;
    }

    private Node getShortestPathByNode(String keyStart, String keyDestination,
            RailwayDirectionType trainDirectionStart, RailwayDirectionType trainDirectionEnd, int lengthOfTrain) {

        Railway startPosition = infrastructure.getNodeData(trainDirectionStart.getPrefix().concat(keyStart));
        Railway endPosition = infrastructure.getNodeData(trainDirectionEnd.getPrefix().concat(keyDestination));

        if (endPosition.getSpace() < lengthOfTrain || startPosition.getSpace() < lengthOfTrain) {
            throw new IllegalArgumentException("The train is too big!");
        }

        HashMap<String, Node> map = new HashMap<>();
        PriorityQueue<Node> keys = new PriorityQueue<>((a, b) -> {
            if(a.distance < b.distance)
                return -1;
            else if (a.distance > b.distance)
                return 1;
            else
                return 0;
        });
        insertAllRailwaysFromStartToNodes(map, startPosition);

        recalculateDistance(keys, map, lengthOfTrain, startPosition);

        Node end = map.get(trainDirectionEnd.getPrefix().concat(keyDestination));
        if (!end.railway.getKey().equals(endPosition.getKey())
                || end.previous == null) {
            throw new IllegalArgumentException("Path does not exist!");
        }
        return end;
    }

    public List<Pair<Railway, Integer>> getShortestPath(String keyStart, String keyDestination,
            RailwayDirectionType trainDirectionStart, RailwayDirectionType trainDirectionEnd, int lengthOfTrain) {

        Node tmp = getShortestPathByNode(keyStart, keyDestination, trainDirectionStart, trainDirectionEnd, lengthOfTrain);
        List<Pair<Railway, Integer>> path = new LinkedList<>();
        String realKeyStart = trainDirectionStart.getPrefix().concat(keyStart);
        boolean firstIteration = true;
        while (tmp != null) {
            path.add(new Pair<>(tmp.railway, tmp.distance));
            String originalKey = tmp.railway.getKeyWithoutPrefix();
            if (!firstIteration && realKeyStart.equals(originalKey)) {
                break;
            } else if (firstIteration && realKeyStart.equals(originalKey)) {
                firstIteration = false;
            }

            tmp = tmp.previous;
        }
        return path;
    }

    private void recalculateDistance(PriorityQueue<Node> keys, HashMap<String, Node> map, int lengthOfTrain, Railway startPosition) {
        keys.add(map.get(startPosition.getKey()));
        while (!keys.isEmpty()) {
            Node node = keys.poll();
            node.visited = true;
            Queue<String> adjencyNodes = new LinkedList<>(
                    infrastructure.getAdjencyNodeKeys(node.railway.getKey()));

            while (!adjencyNodes.isEmpty()) {
                Node neighbor = map.get(adjencyNodes.poll());
                int newDistance = node.distance;

                if (neighbor.railway.getDirection() == node.railway.getDirection()) {
                    if (node.railway.isOccupied()
                            && !node.railway.getKey().equals(startPosition.getKey())) {
                        // recalculating only the reverse edges
                        continue;
                    }

                    // full cost of node length
                    newDistance += (node.railway != startPosition) ? node.railway.getLength() : 0;
                } else {
                    // if train is allowedToStop, you can use reverse
                    if (!node.railway.isTrainAllowedToStop(lengthOfTrain)
                            || (node.railway.getKey().equals(startPosition.getKey())
                            && node.railway.isOccupied())) {
                        continue;
                    }

                    // train length
                    newDistance += (node.railway != startPosition) ? lengthOfTrain : 0;
                }

                if (!neighbor.visited) {
                    keys.add(neighbor);
                }

                if (neighbor.distance > newDistance || neighbor.railway == startPosition) {
                    neighbor.distance = newDistance;
                    neighbor.previous = node;
                }
            }
        }
    }

    private void insertAllRailwaysFromStartToNodes(HashMap<String, Node> map, Railway startPosition) {
        map.put(startPosition.getKey(), new Node(0, startPosition, null));
        infrastructure.getAllNodeKeys().forEach((key) -> {
            if (!startPosition.getKey().equals(key)) {
                map.put(key, new Node(Integer.MAX_VALUE, infrastructure.getNodeData(key), null));
            }
        });
    }
    
    public void clear() {
        infrastructure.clear();
    }
    
    public int getSize() {
        return infrastructure.getNodeCounter();
    }

    private class Node {

        boolean visited = false;
        int distance;
        Railway railway;
        Node previous;

        public Node(int distance, Railway node, Node previous) {
            this.distance = distance;
            this.railway = node;
            this.previous = previous;
        }

        public void setPreviousNode(Node previous, int distance) {
            this.previous = previous;
            this.distance = distance;
        }
    }
}
