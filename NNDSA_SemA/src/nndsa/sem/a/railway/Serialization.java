package nndsa.sem.a.railway;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


public class Serialization {
    public static void saveToCSV(String fileBase, RailwayInfrastructure infrastructure) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(fileBase, "UTF-8");
        List<String> connection = new LinkedList<>();
        infrastructure.getSimpleRailwayList().forEach((railway) -> {
            pw.println(railway.toString());
            connection.add(railway.adjencyRailwaysToString());
        });
        connection.forEach(pw::println);
        pw.close();
    }
    
    /* TODO loadFromCSV*/
}
