package com.ibm.graph.client;

import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.Vertex;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.ArrayList;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wink.json4j.JSONObject;

import static org.junit.Assert.*;

/**
 * Test scope:
 * - class com.ibm.graph.client.Vertex
 */
public class VertexClassTests {

    private static Logger logger =  LoggerFactory.getLogger(VertexClassTests.class);

    @Test
    public void testVertexClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.Vertex");
        Vertex v1 = null;
        String v1_label = "person";
        try {
            // create vertex without label
            v1 = new Vertex();
            assertNotNull(v1);
            assertNull(v1.getId());         // not set
            assertNull(v1.getLabel());      // not set
            assertNull(v1.getProperties()); // not set
            assertNull(v1.getPropertyValue("notdefined"));
            v1.setPropertyValue("name", "John");
            v1.setPropertyValue("age", 43);
            assertEquals("John", v1.getPropertyValue("name"));
            assertEquals(43, v1.getPropertyValue("age"));
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Vertex() - unexpected exception: " + ex.getMessage(), true);
        }

        v1 = null;

        try {
            // create vertex with label
            v1 = new Vertex(v1_label);
            assertNotNull(v1);
            assertNull(v1.getId());         // not set
            assertEquals(v1_label, v1.getLabel());      
            assertNull(v1.getProperties()); // not set
            assertNull(v1.getPropertyValue("notdefined"));
            v1.setPropertyValue("name", "John");
            v1.setPropertyValue("age", 43);
            assertEquals("John", v1.getPropertyValue("name"));
            assertEquals(43, v1.getPropertyValue("age"));
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Vertex(" + v1_label + ") - unexpected exception: " + ex.getMessage(), true);
        }

        v1 = null;

        try {
            // create vertex with label and properties
            v1 = new Vertex(v1_label, new HashMap<String, Object>(){{put("name","John");}});
            assertNotNull(v1);
            assertNull(v1.getId());         // not set
            assertEquals(v1_label, v1.getLabel());      
            assertNotNull(v1.getProperties()); 
            assertEquals("John", v1.getPropertyValue("name"));
            v1.setPropertyValue("age", 43);
            assertEquals("John", v1.getPropertyValue("name"));
            assertEquals(43, v1.getPropertyValue("age"));
            v1.setPropertyValue("age", 34);
            assertEquals(34, v1.getPropertyValue("age"));
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Vertex(" + v1_label + ") - unexpected exception: " + ex.getMessage(), true);
        }  

        // test static fromJSONObject method

        try {
            JSONObject j1 = new JSONObject();
            v1 = Vertex.fromJSONObject(j1);
            assertNotNull(v1);
            assertNull(v1.toString(), v1.getId());         // not set
            assertNull(v1.toString(), v1.getProperties());   // not set
            assertNull(v1.toString(), v1.getLabel());  // not set

            j1 = null;
            v1 = null;

            j1 = new JSONObject();
            j1.put("label", "person");
            j1.put("properties", new HashMap<String, Object>(){{put("name", "Jane");put("age", 27);}});
            v1 = Vertex.fromJSONObject(j1);
            assertNotNull(v1);
            assertNull(v1.toString(), v1.getId());         // not set
            assertEquals(v1.toString(), "person", v1.getLabel());  
            assertNotNull(v1.toString(), v1.getProperties());  
            assertEquals("Jane", v1.getPropertyValue("name"));
            assertEquals(27, v1.getPropertyValue("age"));

            j1 = null;
            v1 = null;

            j1 = new JSONObject();
            j1.put("label", "person");
            j1.put("properties", new HashMap<String, Object>(){{put("name", "Jane");put("age", 27);}});
            j1.put("id", "1");
            v1 = Vertex.fromJSONObject(j1);
            assertNotNull(v1);
            assertEquals(v1.toString(), "1", v1.getId());  
            assertEquals(v1.toString(), "person", v1.getLabel());  
            assertNotNull(v1.toString(), v1.getProperties());  
            assertEquals("Jane", v1.getPropertyValue("name"));
            assertEquals(27, v1.getPropertyValue("age"));
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Vertex(" + v1_label + ") - unexpected exception: " + ex.getMessage(), true);
        } 
    }

    @Test
    public void testVertexClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.Vertex");
        Vertex v1 = null;
        try {
            v1 = Vertex.fromJSONObject(null);
            assertFalse("Vertex.fromJSONObject(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("Vertex.fromJSONObject(null) - unexpected exception: " + ex.getMessage(), true);
        }        
    }
}
