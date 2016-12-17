package com.ibm.graph.client;

import util.FileUtil;

import com.ibm.graph.client.exception.GraphException;
import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.schema.*;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Tests for class com.ibm.graph.client.IBMGraphClient:
 * - graphClient.loadGraphML
 * - graphClient.loadGraphMLfromFile
 * An IBM Graph connection is required. 
 */
public class GraphMLTests {

    private static String currentGraphId = null;
    private static String testGraphId = null;
    private static Logger logger =  LoggerFactory.getLogger(GraphMLTests.class);

    private static long counter = 0;

    @BeforeClass
    public static void setup() throws Exception {

        // create a temporary graph on which all tests operate on

        currentGraphId = TestSuite.graphClient.getGraphId();
        assertNotNull(currentGraphId);

        // create graph with random name
        testGraphId = TestSuite.graphClient.createGraph();
        assertNotNull(testGraphId);

        TestSuite.graphClient.setGraph(testGraphId);

        logger.debug("Using graph " + testGraphId + " in test class GraphMLTests.");

        try {
         // create test schema   
         assertNotNull(TestSuite.graphClient.saveSchema(Schema.fromJSONObject(new JSONObject(new FileReader("./target/test-classes/tinkerpop_modern_schema.json"))))); 
        }
        catch(Exception ex) {
            // setup fail
            logger.error("Test schema creation failed.", ex);
            assertTrue(false);
        }

        logger.debug("*************************************************************************");
        logger.debug("********************** test setup complete ******************************");
        logger.debug("*************************************************************************");
    }

    @AfterClass
    public static void teardown() throws Exception {
    
        try {
            // switch to previous graph
            if(currentGraphId != null) {
                TestSuite.graphClient.setGraph(currentGraphId);
                if(testGraphId != null) {
                    // delete graph
                    if(TestSuite.graphClient.deleteGraph(testGraphId)) 
                        logger.debug("Deleted test graph " + testGraphId + " in test class GraphMLTests.");
                }
            }                
        }
        catch(Exception ex) {
            // ignore
        }
    }

    @Test
    public void loadGraphML() throws Exception {
        logger.info("Executing graphClient.loadGraphML() test.");

        JSONObject content = null;

        try {
            // load content of https://raw.githubusercontent.com/apache/tinkerpop/master/data/tinkerpop-modern.xml 
            String graphml = FileUtil.loadFile("./target/test-classes/tinkerpop_modern.graphml");
            assertTrue(TestSuite.graphClient.loadGraphML(graphml));

            // test load accuracy
            String name = "marko";
            ResultSet rs = TestSuite.graphClient.executeGremlin("g.V().has('name','" + name + "');");
            assertNotNull(rs);
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(1, rs.getResultCount());
            Vertex result = rs.getResultAsVertex(0);
            assertNotNull(result);
            assertEquals(result.toString(), "person", result.getLabel());
            assertEquals(result.toString(), name, result.getPropertyValue("name"));
            // pass

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            if(content != null)
                logger.error("Payload: " + content.toString());
            assertFalse(true);
        } 
    }

    @Test
    public void testLoadGraphMLErrorHandling() throws Exception {
        logger.info("Executing error handling tests for graphClient.loadGraphML(...).");

        try {
            // GraphML parameter cannot be null          
            TestSuite.graphClient.loadGraphML(null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("NotNullParm: " + iaex.getMessage());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // GraphML parameter cannot be an empty string
            TestSuite.graphClient.loadGraphML("");
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("NotEmptyParm: " + iaex.getMessage());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // GraphML parameter cannot be an empty string
            TestSuite.graphClient.loadGraphML("     ");
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("NotEmptyParm: " + iaex.getMessage());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // GraphML parameter cannot be longer than 10MB            
            StringBuffer buff = new StringBuffer(10*1024*1024 + 1);
            for(int i = 0; i < 10*1024*1024 + 1; i++)
                buff.append("X");           
            TestSuite.graphClient.loadGraphML(buff.toString());
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("TooLargeParm: " + iaex.getMessage());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // GraphML parameter must be valid GraphML
            TestSuite.graphClient.loadGraphML("Me:IamGraphML.You:NoYouAreNot.");
            assertFalse(true); // fail
        }
        catch(GraphException gex) {
            // pass
            logger.debug("InvalidPayload: " + gex.getMessage() + " " + gex.toString());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

    // --------------------------------------------------------
    // test utility functions
    // --------------------------------------------------------

    private static long getNextId() {
        return counter++;
    }

}
