/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nndsa.sem.a.railway;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ludek
 */
public class RailwayInfrastructureTest {
    
    public RailwayInfrastructureTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    private RailwayInfrastructure instance;
    
    @Before
    public void setUp() {
        instance = new RailwayInfrastructure();
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findShortestPath method, of class RailwayInfrastructure.
     */
    @Test
    public void testFindShortestPath1() {
        int distance = instance.getShortestDistance("v5", "v4", RailwayDirectionType.THERE, RailwayDirectionType.BACK, 120);
        assertEquals(470, distance);
    }
    
    @Test
    public void testFindShortestPath2() {
        instance.setOccupancy("v5", 100);
        
        int distance = instance.getShortestDistance("v5", "v5", RailwayDirectionType.THERE, RailwayDirectionType.THERE, 20);
        
        // clear
        instance.setOccupancy("v5", 0);
        
        assertEquals(489, distance);
    }
    
    @Test
    public void testFindShortestPath3() {
        instance.setOccupancy("v5", 100);
        instance.setOccupancy("v4", 20);
        
        int distance = instance.getShortestDistance("v10", "v4", RailwayDirectionType.BACK, RailwayDirectionType.THERE, 20);
        
        // clear
        instance.setOccupancy("v5", 0);
        instance.setOccupancy("v4", 0);
        
        assertEquals(1655, distance);
    }
    
}
