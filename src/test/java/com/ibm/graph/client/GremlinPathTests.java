package com.ibm.graph.client;

import com.ibm.graph.client.Entity;
import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.schema.*;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.wink.json4j.JSONObject;

import static org.junit.Assert.*;

/**
 * Test scope: test gremlin queries returning a path
 * - graphClient.executeGremlin
 */
public class GremlinPathTests {

    private static Logger logger =  LoggerFactory.getLogger(GremlinPathTests.class);
    private static List<String> graphsCreated;
    private static String currentGraphId = null;

    @BeforeClass
    public static void setup() {

       graphsCreated = new ArrayList<>();

       try {
            // setup ...
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            String graphId = TestSuite.graphClient.createGraph(); 
            assertNotNull(graphId);
            graphsCreated.add(graphId); 

            // the graph should not be in use yet
            assertNotEquals(graphId, TestSuite.graphClient.getGraphId());
            // switch to graph
            TestSuite.graphClient.setGraph(graphId);
            assertEquals(graphId, TestSuite.graphClient.getGraphId());

            // populate graph
            // load schema file
            TestSuite.graphClient.saveSchema(Schema.fromJSONObject(new JSONObject(new FileInputStream(GremlinPathTests.class.getClassLoader().getResource("nxnw_schema.json").getFile()))));
            // load data file
            TestSuite.graphClient.loadGraphSONfromFile(GremlinPathTests.class.getClassLoader().getResource("nxnw_dataset.json").getFile());

        }
        catch(Exception ex) {
            // setup failure; terminate test
            logger.error("Setup for GramlinPathTests failed: ", ex);
            assertTrue(false);
        }

    }

    @AfterClass
    public static void teardown() {
        if ((! graphsCreated.isEmpty()) && (TestSuite.graphClient != null)){
            // try and clean up any graphs that didn't get deleted
            for (String orphanGraphId : graphsCreated) {
                try {
                    logger.debug("Deleting orphan graph with id " + orphanGraphId);                    
                    TestSuite.graphClient.deleteGraph(orphanGraphId);
                }
                catch(Exception ex) {}
            }
        }
        graphsCreated.clear();
    }

   @Test
    public void executePathGremlin() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) path test.");
  
        try {
            // no labels
            // Path: Vertex > Edge > Vertex
            // g.V().hasLabel("attendee").has("gender", "female").outE("bought_ticket").inV().hasLabel("band").path();
            logger.debug("Query: ");
            ResultSet rs = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"attendee\").has(\"gender\", \"female\").outE(\"bought_ticket\").inV().hasLabel(\"band\").path();");
            assertNotNull(rs);
            logger.debug("Results: " + rs.toString());            
            assertTrue(rs.hasResults());            
            assertEquals(rs.getResultCount(), 53, rs.getResultCount());
            Path p = null;
            int j = 0;
            Entity.EntityType[] ets = null;
            for(int i = 0; i < rs.getResultCount(); i++) {
                p = rs.getResultAsPath(i);
                assertNotNull(p);                               // result should be translatable to Path
                logger.debug("Path result:" + p.toString());
                assertEquals(3, p.getLabels().length);          // three label entries in data structure
                for(j = 0; j < p.getLabels().length; j++) {
                    assertEquals(0, p.getLabels()[j].length);   // no labels defined in traversal
                }
                assertEquals(3, p.getObjects().length);         // three elements (vertex, edge, vertex) in the path
                assertEquals(3, p.getSize());                   // three elements (vertex, edge, vertex) in the path
                assertEquals(((Entity)p.getObjects()[0]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[0]).getType());    // Vertex
                assertEquals(((Entity)p.getObjects()[1]).toString(), Entity.EntityType.Edge, ((Entity)p.getObjects()[1]).getType());      // Edge
                assertEquals(((Entity)p.getObjects()[2]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[2]).getType());    // Vertex
                ets = new Entity.EntityType[p.getObjects().length];
                ets[0] = Entity.EntityType.Vertex;
                ets[1] = Entity.EntityType.Edge;
                ets[2] = Entity.EntityType.Vertex;
                assertEquals(Arrays.toString(p.getObjectTypes()), Arrays.toString(ets), Arrays.toString(p.getObjectTypes()));
            }

            // one label
            // Path: Vertex > Edge > Vertex             
            // g.V().hasLabel("attendee").has("gender", "female").as("a").outE("bought_ticket").inV().hasLabel("band").path();
            logger.debug("Query: ");
            rs = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"attendee\").has(\"gender\", \"female\").as(\"a\").outE(\"bought_ticket\").inV().hasLabel(\"band\").path();");
            assertNotNull(rs);
            logger.debug("Results: " + rs.toString());            
            assertTrue(rs.hasResults());            
            assertEquals(rs.getResultCount(), 53, rs.getResultCount());
            p = null;
            j = 0;
            ets = null;
            for(int i = 0; i < rs.getResultCount(); i++) {
                p = rs.getResultAsPath(i);
                assertNotNull(p);                               // result should be translatable to Path
                logger.debug("Path result:" + p.toString());
                assertEquals(3, p.getLabels().length);          // three label entries in data structure
                assertEquals(1, p.getLabels()[0].length);       // one label defined in traversal
                assertEquals(0, p.getLabels()[1].length);       // no labels defined in traversal
                assertEquals(0, p.getLabels()[2].length);       // no labels defined in traversal                
                assertEquals(3, p.getObjects().length);         // three elements (vertex, edge, vertex) in the path
                assertEquals(3, p.getSize());                   // three elements (vertex, edge, vertex) in the path
                assertEquals(((Entity)p.getObjects()[0]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[0]).getType());    // Vertex
                assertEquals(((Entity)p.getObjects()[1]).toString(), Entity.EntityType.Edge, ((Entity)p.getObjects()[1]).getType());      // Edge
                assertEquals(((Entity)p.getObjects()[2]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[2]).getType());    // Vertex
                ets = new Entity.EntityType[p.getObjects().length];
                ets[0] = Entity.EntityType.Vertex;
                ets[1] = Entity.EntityType.Edge;
                ets[2] = Entity.EntityType.Vertex;
                assertEquals(Arrays.toString(p.getObjectTypes()), Arrays.toString(ets), Arrays.toString(p.getObjectTypes()));
            }

            // two labels 
            // Path: Vertex > Edge > Vertex > String
            // g.V().hasLabel("attendee").has("gender", "female").as("a").outE("bought_ticket").inV().as("b").hasLabel("band").values("name").path();
            logger.debug("Query: ");
            rs = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"attendee\").has(\"gender\", \"female\").as(\"a\").outE(\"bought_ticket\").inV().as(\"b\").hasLabel(\"band\").values(\"name\").path();");
            assertNotNull(rs);
            logger.debug("Results: " + rs.toString());            
            assertTrue(rs.hasResults());            
            assertEquals(rs.getResultCount(), 53, rs.getResultCount());
            p = null;
            j = 0;
            ets = null;
            for(int i = 0; i < rs.getResultCount(); i++) {
                p = rs.getResultAsPath(i);
                assertNotNull(p);                               // result should be translatable to Path
                logger.debug("Path result:" + p.toString());
                assertEquals(4, p.getLabels().length);          // four label entries in data structure
                assertEquals(1, p.getLabels()[0].length);       // one label defined in traversal
                assertEquals(0, p.getLabels()[1].length);       // no labels defined in traversal
                assertEquals(1, p.getLabels()[2].length);       // one label defined in traversal                
                assertEquals(0, p.getLabels()[3].length);       // no labels defined in traversal                
                assertEquals(4, p.getObjects().length);         // four elements (vertex, edge, vertex, String) in the path
                assertEquals(4, p.getSize());                   // four elements (vertex, edge, vertex, String) in the path
                assertEquals(((Entity)p.getObjects()[0]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[0]).getType());    // Vertex
                assertEquals(((Entity)p.getObjects()[1]).toString(), Entity.EntityType.Edge, ((Entity)p.getObjects()[1]).getType());      // Edge
                assertEquals(((Entity)p.getObjects()[2]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[2]).getType());    // Vertex
                assertFalse((p.getObjects()[3]) instanceof Entity);      // primitive type  String            
                ets = new Entity.EntityType[p.getObjects().length];
                ets[0] = Entity.EntityType.Vertex;
                ets[1] = Entity.EntityType.Edge;
                ets[2] = Entity.EntityType.Vertex;
                ets[3] = Entity.EntityType.Unknown;
                assertEquals(Arrays.toString(p.getObjectTypes()), Arrays.toString(ets), Arrays.toString(p.getObjectTypes()));
            }
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
        finally {
            // nothing to do
        }
    }

    @Test
    public void executeGremlinErrorHandling() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) path error handling test.");
        //
        // parameter checking (gremlin)
        //
        try {
            // gremlin is null
            TestSuite.graphClient.executeGremlin(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.executeGremlin(...) unexpected exception: " + ex.getMessage(), true);   
        }  
        try {
            // gremlin is empty string
            TestSuite.graphClient.executeGremlin("");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.executeGremlin(...) unexpected exception: " + ex.getMessage(), true);   
        }  
        try {
            // gremlin is empty string
            TestSuite.graphClient.executeGremlin("      ");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.executeGremlin(...) unexpected exception: " + ex.getMessage(), true);   
        }  

        //
        // parameter checking (gremlin + bindings)
        //
        String graphId = null;
        String currentGraphId = null;

        try {
            // setup ...
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            graphId = TestSuite.graphClient.createGraph(); 
            assertNotNull(graphId);
            graphsCreated.add(graphId); 

            // the graph should not be in use yet
            assertNotEquals(graphId, TestSuite.graphClient.getGraphId());
            // switch to graph
            TestSuite.graphClient.setGraph(graphId);
            assertEquals(graphId, TestSuite.graphClient.getGraphId());        

            // TODO

        }       
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
        finally {
            // cleanup ...
            if(currentGraphId != null) {             
                try {
                    TestSuite.graphClient.setGraph(currentGraphId); 
                    if(graphId != null)  {
                        TestSuite.graphClient.deleteGraph(graphId);
                        graphsCreated.remove(graphId);   
                    } 
                }
                catch(Exception ex) {
                    // ignore
                }
            }
        }

    }
}
