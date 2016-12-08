package com.ibm.graph.client;

import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.schema.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wink.json4j.JSONObject;

import static org.junit.Assert.*;

/**
 * 
 */
@Ignore("Test template only")
public class TestTemplate {

    private static Logger logger =  LoggerFactory.getLogger(TestTemplate.class);
    private static List<String> graphsCreated;

    @BeforeClass
    public static void setup() {

        assertNotNull(TestSuite.graphClient);
        graphsCreated = new ArrayList<>();      
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
    public void testXXX() throws Exception {

        logger.info("Executing graphClient.METHODNAME(...) test.");

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

            // ... test code starts here

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
    public void testXXXErrorHandling() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) error handling test.");
        //
        // parameter checking 
        //
        try {
            // failure condition
            // TODO
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
