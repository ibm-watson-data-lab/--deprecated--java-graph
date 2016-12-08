package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphException;
import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.schema.*;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
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
 * - graphClient.loadGraphSON
 * - graphClient.loadGraphSONfromFile
 * An IBM Graph connection is required. 
 */
public class GraphSONTests {

    private static String currentGraphId = null;
    private static String testGraphId = null;
    private static Logger logger =  LoggerFactory.getLogger(GraphSONTests.class);

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

        logger.debug("Using graph " + testGraphId + " in test class GraphSONTests.");


        Schema  s1 = new Schema(new PropertyKey[]{
                                new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                            },
                            new VertexLabel[]{
                                new VertexLabel("person")
                            },
                            new EdgeLabel[]{},
                            new VertexIndex[]{
                                new VertexIndex("vertexByName", new String[]{"name"}, true, false)
                            },
                            new EdgeIndex[]{});

        // define schema
        assertNotNull(TestSuite.graphClient.saveSchema(s1));
      
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
                        logger.debug("Deleted test graph " + testGraphId + " in test class GraphSONTests.");
                }
            }                
        }
        catch(Exception ex) {
            // ignore
        }
    }


    @Test
    public void loadGraphSON() throws Exception {
        logger.info("Executing graphClient.loadGraphSON() test.");

        JSONObject content = null;

        try {
            // setup
            content = new JSONObject();
            content.put("id", GraphSONTests.getNextId());
            content.put("label", "person");
            String nameValue = "In-memory Humphrey";
            JSONArray nameProperty = new JSONArray();
            nameProperty.add(new JSONObject(){{put("id", GraphSONTests.getNextId());put("value", nameValue);}});
            content.put("properties", new HashMap(){{put("name", nameProperty);}});  
            // load
            assertTrue(TestSuite.graphClient.loadGraphSON(content.toString()));
            // test load accuracy
            ResultSet rs = TestSuite.graphClient.executeGremlin("g.V().has('name','" + nameValue + "');");
            assertNotNull(rs);
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(1, rs.getResultCount());
            Vertex result = rs.getResultAsVertex(0);
            assertNotNull(result);
            assertEquals(result.toString(), content.getString("label"), result.getLabel());
            assertEquals(result.toString(), nameValue, result.getPropertyValue("name"));
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
    public void testLoadGraphSONErrorHandling() throws Exception {
        logger.info("Executing error handling tests for graphClient.loadGraphSON(...).");

        try {
            // graphSON parameter cannot be null          
            TestSuite.graphClient.loadGraphSON(null);
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
            // graphSON parameter cannot be an empty string
            TestSuite.graphClient.loadGraphSON("");
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
            // graphSON parameter cannot be an empty string
            TestSuite.graphClient.loadGraphSON("     ");
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
            // graphSON parameter cannot be longer than 10MB            
            StringBuffer buff = new StringBuffer(10*1024*1024 + 1);
            for(int i = 0; i < 10*1024*1024 + 1; i++)
                buff.append("X");           
            TestSuite.graphClient.loadGraphSON(buff.toString());
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
            // graphSON parameter must be valid graphSON
            TestSuite.graphClient.loadGraphSON("Me:IamGraphSON.You:NoYouAreNot.");
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

   @Test
    public void testLoadGraphSONfromFile() throws Exception {
        logger.info("Executing tests for graphClient.loadGraphSONfromFile(...).");

        File temp = null;
        JSONObject content = null;

        try {
            // setup: create a valid graphSON file

            content = new JSONObject();
            content.put("id", GraphSONTests.getNextId());
            content.put("label", "person");
            String nameValue = "Persistent Humphrey";
            JSONArray nameProperty = new JSONArray();
            nameProperty.add(new JSONObject(){{put("id", GraphSONTests.getNextId());put("value", nameValue);}});
            content.put("properties", new HashMap(){{put("name", nameProperty);}});  

            temp = File.createTempFile("a_graphson_file", ".graphson");
            BufferedWriter out = new BufferedWriter(new FileWriter(temp.getAbsolutePath()));
            out.write(content.toString());
            out.close();

            // load valid graphSON
            assertTrue(TestSuite.graphClient.loadGraphSONfromFile(temp.getAbsolutePath()));

           // test load accuracy
            ResultSet rs = TestSuite.graphClient.executeGremlin("g.V().has('name','" + nameValue + "');");
            assertNotNull(rs);
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(1, rs.getResultCount());
            Vertex result = rs.getResultAsVertex(0);
            assertNotNull(result);
            assertEquals(result.toString(), content.getString("label"), result.getLabel());
            assertEquals(result.toString(), nameValue, result.getPropertyValue("name"));
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            if(content != null)
                logger.error("Payload: " + content.toString());
            assertFalse(true);
        }
        finally {
            // cleanup: delete temp file
            try {
                if(temp != null) {
                    temp.delete();
                }
            }
            catch(Exception ex) {
                //  ¯\_(ツ)_/¯
                logger.error("Could not delete temp file: ", ex);
            }
        }
    }

    @Test
    public void testLoadGraphSONfromFileErrorHandling() throws Exception {
        logger.info("Executing error handling tests for graphClient.loadGraphSONfromFile(...).");

        try {
            // filename parameter cannot be null          
            TestSuite.graphClient.loadGraphSONfromFile(null);
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
            // filename parameter must identify an existing/readable file
            TestSuite.graphClient.loadGraphSONfromFile("");
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
            // filename parameter must identify an existing/readable file
            TestSuite.graphClient.loadGraphSONfromFile("     ");
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
            // filename parameter must identify an existing/readable file
            TestSuite.graphClient.loadGraphSONfromFile("idontexist.graphson");
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("FileNotFound: " + iaex.getMessage());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        File temp = null;

        try {
            // setup: create invalid graphSON file
            temp = File.createTempFile("not_a_graphson_file", ".graphson");

            BufferedWriter out = new BufferedWriter(new FileWriter(temp.getAbsolutePath()));
            out.write("Me:IamGraphSON. You:NoYouAreNot. Me: Sigh");
            out.close();
    
            // graphSON parameter must be valid graphSON
            TestSuite.graphClient.loadGraphSONfromFile(temp.getAbsolutePath());
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
        finally {
            // cleanup: delete temp file
            try {
                if(temp != null)
                    temp.delete();
            }
            catch(Exception ex) {
                //  ¯\_(ツ)_/¯
                logger.error("Could not delete temp file: ", ex);
            }
        }

        try {
            // setup: create invalid graphSON file (too large)
            temp = File.createTempFile("large_file", ".graphson");

           StringBuffer buff = new StringBuffer(10*1024*1024 + 1);
            for(int i = 0; i < 10*1024*1024 + 1; i++)
                buff.append("X");           

            BufferedWriter out = new BufferedWriter(new FileWriter(temp.getAbsolutePath()));
            out.write(buff.toString());
            out.close();
    
            // file size must not exceed 10MB
            TestSuite.graphClient.loadGraphSONfromFile(temp.getAbsolutePath());
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
            logger.debug("InvalidPayloadSize: " + iaex.getMessage() + " " + iaex.toString());            
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
        finally {
            // cleanup: delete temp file
            try {
                if(temp != null)
                    temp.delete();
            }
            catch(Exception ex) {
                //  ¯\_(ツ)_/¯
                logger.error("Could not delete temp file: ", ex);
            }
        }


    }

    // --------------------------------------------------------
    // test utility functions
    // --------------------------------------------------------

    private static long getNextId() {
        return counter++;
    }

}
