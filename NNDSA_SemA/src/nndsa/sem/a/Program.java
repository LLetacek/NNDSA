package nndsa.sem.a;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import nndsa.sem.a.railway.RailwayInfrastructure;
import nndsa.sem.a.railway.RailwayTrackType;
import nndsa.sem.a.railway.Serialization;

/**
 *
 * @author ludek
 */
public class Program {

    public static void main(String[] args) {
        RailwayInfrastructure instance = new RailwayInfrastructure();
        // V1 - V15
        instance.addRailway("v01", 157, RailwayTrackType.DIRECT);
        instance.addRailway("v02", 100, RailwayTrackType.DIRECT);
        instance.addRailway("v03", 20, RailwayTrackType.DIRECT);
        instance.addRailway("v04", 280, RailwayTrackType.DIRECT);
        instance.addRailway("v05", 259, RailwayTrackType.DIRECT);
        instance.addRailway("v06", 727, RailwayTrackType.DIRECT);
        instance.addRailway("v07", 40, RailwayTrackType.DIRECT);
        instance.addRailway("v08", 139, RailwayTrackType.DIRECT);
        instance.addRailway("v09", 302, RailwayTrackType.DIRECT);
        instance.addRailway("v10", 175, RailwayTrackType.DIRECT);
        instance.addRailway("v11", 270, RailwayTrackType.DIRECT);
        instance.addRailway("v12", 160, RailwayTrackType.DIRECT);
        instance.addRailway("v13", 160, RailwayTrackType.DIRECT);
        instance.addRailway("v14", 140, RailwayTrackType.DIRECT);
        instance.addRailway("v15", 160, RailwayTrackType.DIRECT);
        // S1
        instance.addRailway("v16", 30, RailwayTrackType.SWITCH);
        instance.addRailway("v17", 42, RailwayTrackType.SWITCH);
        instance.addRailway("v18", 38, RailwayTrackType.SWITCH);
        instance.addRailway("v19", 43, RailwayTrackType.SWITCH);
        // S2
        instance.addRailway("v20", 34, RailwayTrackType.SWITCH);
        instance.addRailway("v21", 41, RailwayTrackType.SWITCH);
        // S3
        instance.addRailway("v22", 34, RailwayTrackType.SWITCH);
        instance.addRailway("v23", 40, RailwayTrackType.SWITCH);
        // S4
        instance.addRailway("v24", 38, RailwayTrackType.SWITCH);
        instance.addRailway("v25", 41, RailwayTrackType.SWITCH);
        // S5
        instance.addRailway("v26", 36, RailwayTrackType.SWITCH);
        instance.addRailway("v27", 40, RailwayTrackType.SWITCH);
        instance.addRailway("v28", 32, RailwayTrackType.SWITCH);
        instance.addRailway("v29", 41, RailwayTrackType.SWITCH);
        // S6
        instance.addRailway("v30", 33, RailwayTrackType.SWITCH);
        instance.addRailway("v31", 60, RailwayTrackType.SWITCH);
        // S7   
        instance.addRailway("v32", 30, RailwayTrackType.SWITCH);
        instance.addRailway("v33", 42, RailwayTrackType.SWITCH);
        instance.addRailway("v34", 38, RailwayTrackType.SWITCH);
        instance.addRailway("v35", 43, RailwayTrackType.SWITCH);
        
        // connection
        // S1 S2 S3
        instance.addConnection("v01", "v16");
        instance.addConnection("v02", "v17");
        instance.addConnection("v16", "v19");
        instance.addConnection("v17", "v19");
        instance.addConnection("v17", "v18");
        instance.addConnection("v19", "v06");
        instance.addConnection("v18", "v03");
        instance.addConnection("v03", "v20");
        instance.addConnection("v03", "v21");
        instance.addConnection("v20", "v04");
        instance.addConnection("v21", "v05");
        instance.addConnection("v04", "v22");
        instance.addConnection("v05", "v23");
        instance.addConnection("v22", "v07");
        instance.addConnection("v23", "v07");
        instance.addConnection("v07", "v24");
        instance.addConnection("v07", "v25");
        // S4 S6
        instance.addConnection("v24", "v08");
        instance.addConnection("v08", "v30");
        instance.addConnection("v08", "v31");
        instance.addConnection("v30", "v10");
        instance.addConnection("v31", "v11");
        // S4 S5
        instance.addConnection("v25", "v09");
        instance.addConnection("v09", "v26");
        instance.addConnection("v06", "v27");
        instance.addConnection("v26", "v28");
        instance.addConnection("v26", "v29");
        instance.addConnection("v27", "v28");
        instance.addConnection("v29", "v13");
        instance.addConnection("v28", "v12");
        // S7
        instance.addConnection("v11", "v32");
        instance.addConnection("v12", "v33");
        instance.addConnection("v32", "v34");
        instance.addConnection("v32", "v35");
        instance.addConnection("v33", "v34");
        instance.addConnection("v33", "v35");
        instance.addConnection("v34", "v14");
        instance.addConnection("v35", "v15");

        try {
            Serialization.saveToCSV("./mainRailwayInfrastructure.csv", instance);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }

        /*
        instance.setOccupancy("v05", 100);
        instance.setOccupancy("v04", 20);
        
        List<Pair<Railway,Integer>> path = instance.getShortestPath("v10", "v4", RailwayDirectionType.BACK, RailwayDirectionType.THERE, 20);
        path.forEach((pair) -> {
            System.out.println(pair.getKey().getKeyWithoutPrefix() + " - " + pair.getValue());
        });
        */
    }
}
