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
        instance.addRailway("v1", 157, RailwayTrackType.DIRECT);
        instance.addRailway("v2", 100, RailwayTrackType.DIRECT);
        instance.addRailway("v3", 20, RailwayTrackType.DIRECT);
        instance.addRailway("v4", 280, RailwayTrackType.DIRECT);
        instance.addRailway("v5", 259, RailwayTrackType.DIRECT);
        instance.addRailway("v6", 727, RailwayTrackType.DIRECT);
        instance.addRailway("v7", 40, RailwayTrackType.DIRECT);
        instance.addRailway("v8", 139, RailwayTrackType.DIRECT);
        instance.addRailway("v9", 302, RailwayTrackType.DIRECT);
        instance.addRailway("v10", 175, RailwayTrackType.DIRECT);
        instance.addRailway("v11", 140, RailwayTrackType.DIRECT);
        instance.addRailway("v12", 160, RailwayTrackType.DIRECT);
        instance.addRailway("v13", 30, RailwayTrackType.SWITCH);
        instance.addRailway("v14", 38, RailwayTrackType.SWITCH);
        instance.addRailway("v15", 42, RailwayTrackType.SWITCH);
        instance.addRailway("v16", 43, RailwayTrackType.SWITCH);
        instance.addRailway("v17", 34, RailwayTrackType.SWITCH);
        instance.addRailway("v18", 41, RailwayTrackType.SWITCH);
        instance.addRailway("v19", 34, RailwayTrackType.SWITCH);
        instance.addRailway("v20", 40, RailwayTrackType.SWITCH);
        instance.addRailway("v21", 38, RailwayTrackType.SWITCH);
        instance.addRailway("v22", 41, RailwayTrackType.SWITCH);
        instance.addRailway("v23", 36, RailwayTrackType.SWITCH);
        instance.addRailway("v24", 40, RailwayTrackType.SWITCH);
        instance.addRailway("v25", 33, RailwayTrackType.SWITCH);
        instance.addRailway("v26", 60, RailwayTrackType.SWITCH);

        instance.addConnection("v1", "v13");
        instance.addConnection("v2", "v15");
        instance.addConnection("v15", "v16");
        instance.addConnection("v15", "v14");
        instance.addConnection("v13", "v16");
        instance.addConnection("v14", "v3");
        instance.addConnection("v3", "v17");
        instance.addConnection("v3", "v18");
        instance.addConnection("v17", "v4");
        instance.addConnection("v18", "v5");
        instance.addConnection("v4", "v19");
        instance.addConnection("v5", "v20");
        instance.addConnection("v19", "v7");
        instance.addConnection("v20", "v7");
        instance.addConnection("v7", "v21");
        instance.addConnection("v7", "v22");
        instance.addConnection("v21", "v8");
        instance.addConnection("v22", "v9");
        instance.addConnection("v9", "v23");
        instance.addConnection("v8", "v25");
        instance.addConnection("v8", "v26");
        instance.addConnection("v25", "v10");
        instance.addConnection("v26", "v11");
        instance.addConnection("v16", "v6");
        instance.addConnection("v6", "v24");
        instance.addConnection("v23", "v12");
        instance.addConnection("v24", "v12");

        try {
            Serialization.saveToCSV("./test.csv", instance);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }

        try {
            Serialization.saveToCSV("./test.csv", instance);

            System.out.println(instance.getSize());
            instance.deleteRailway("v26");
            System.out.println(instance.getSize());

            Serialization.loadFromCSV("./test.csv", instance);

            System.out.println(instance.getSize());

            Serialization.saveToCSV("./test.csv", instance);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
