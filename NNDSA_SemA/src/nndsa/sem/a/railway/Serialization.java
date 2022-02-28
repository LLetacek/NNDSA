package nndsa.sem.a.railway;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Serialization {

    private final static String SPLITTER = "#";
    private static RailwayInfrastructure infrastructure;

    public static void saveToCSV(String fileBase, RailwayInfrastructure infrastructure) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter pw = new PrintWriter(fileBase, "UTF-8");
        List<String> connection = new LinkedList<>();
        infrastructure.getSimpleRailwayList().forEach((railway) -> {
            pw.println(railway.toString());
            connection.add(railway.adjencyRailwaysToString());
        });
        pw.println(SPLITTER);
        connection.forEach(pw::print);
        pw.close();
    }

    public static void loadFromCSV(String fileBase, RailwayInfrastructure infrastructure) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        infrastructure.clear();
        Serialization.infrastructure = infrastructure;

        try (Stream<String> file = Files.lines(Paths.get(fileBase), StandardCharsets.UTF_8)) {
            file.filter(t -> t != null)
                    .forEach((String line) -> {
                        String[] parse = line.split(";");
                        switch (parse.length) {
                            case 5:
                                infrastructure.addSimpleRailway(
                                        new SimpleRailway(
                                                parse[0],
                                                Integer.valueOf(parse[1]),
                                                RailwayDirectionType.getValue(parse[2]),
                                                RailwayTrackType.valueOf(parse[3]),
                                                Integer.valueOf(parse[4])));
                                break;
                            case 4:
                                infrastructure.addSimpleConnection(
                                        parse[0],
                                        RailwayDirectionType.getValue(parse[1]),
                                        parse[2],
                                        RailwayDirectionType.getValue(parse[3]));
                                break;
                            default:
                        }
                    });
        }
    }

    private static Railway createRailway(String line) {
        return null;
    }

}
