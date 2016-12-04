package com.ibm.graph.client;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wink.json4j.JSONObject;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class GremlinTests {

    private static Logger logger =  LoggerFactory.getLogger(GremlinTests.class);
    private static List<String> graphsCreated;

    @BeforeClass
    public static void setup() {
        graphsCreated = new ArrayList<>();
    }

    @AfterClass
    public static void teardown() {
        if (graphsCreated.size() > 0) {
            // try and clean up any graphs that didn't get deleted
            for (String graphId : graphsCreated) {
                try {
                    TestSuite.graphClient.deleteGraph(graphId);
                }
                catch(Exception ex) {}
            }
        }
        graphsCreated.clear();
    }

   @Test
    public void executeGremlin() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) test.");
        Vertex v1 = null, v2 = null, v3 = null;
        try {
            assertNotNull(TestSuite.graphClient.getGraphId());
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

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
            // run tests ...

            // gremlin without bindings
            TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"nobody\");");
            TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"nobody\");", null);
            // gremlin and bindings
            TestSuite.graphClient.executeGremlin("g.V().has(\"name\", somebody);", "{\"somebody\":\"nobody\"}");            
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

    @Test
    public void executeGremlinErrorHandling() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) error handling test.");
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
        try {
            // binding is empty string (should be null to make intentions clear)
            TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"nobody\");", "");
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
            // not valid gremlin 
            TestSuite.graphClient.executeGremlin(null, "{\"vertexOne\": 4000, \"vertexTwo\": 4001, \"edgeLabel\": \"tweets\", \"propKey1\": \"city\", \"value1\": \"orlando\", \"propKey2\": \"temperature\", \"value2\": \"hot\"}");
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
            // not valid binding 
            TestSuite.graphClient.executeGremlin("g.V().has(\"name\", nobody);", " i am not json");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.executeGremlin(...) unexpected exception: " + ex.getMessage(), true);   
        }  
    }
}
