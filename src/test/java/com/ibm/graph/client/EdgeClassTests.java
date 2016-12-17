package com.ibm.graph.client;

import com.ibm.graph.client.Edge;
import com.ibm.graph.client.exception.GraphException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test scope:
 * - class com.ibm.graph.client.Edge
 */
public class EdgeClassTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeClassTests.class);

    @Test
    public void testEdgeClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.Edge");
        Edge e1 = null;
        String e1_label = "e1_edgelabel";
        try {
            e1 = new Edge(e1_label, "1", "2");
            assertNotNull(e1);
            assertNull(e1.getId());
            assertEquals(e1_label, e1.getLabel());
            assertEquals("1", e1.getOutV());
            assertEquals("2", e1.getInV());
            assertNull(e1.getProperties());
            assertNull(e1.getPropertyValue("notdefined"));
            e1.setPropertyValue("property1", "value1");
            e1.setPropertyValue("property2", false);
            assertEquals("value1", e1.getPropertyValue("property1"));
            assertEquals(false, e1.getPropertyValue("property2"));
        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Edge(\""+ e1_label + "\", 1, 2) - unexpected exception: " + ex.getMessage(), true);
        }
        assertNotNull(e1);
        e1 = null;

        try {
            e1 = new Edge("label", "1", "2", null);
        }
        catch(Exception ex) {
            assertFalse("new Edge(\"label\", 1, 2, null): " + ex.getMessage(), true);
        }
        assertNotNull(e1);
        e1 = null;

        try {
            e1 = new Edge(e1_label, "1", "2", new HashMap<String, Object>(){{put("property1", "value1");}});
            assertNotNull(e1);
            assertNull(e1.getId());
            assertEquals(e1_label, e1.getLabel());
            assertEquals("1", e1.getOutV());
            assertEquals("2", e1.getInV());
            assertNotNull(e1.getProperties());
            assertEquals("value1", e1.getPropertyValue("property1"));
            e1.setPropertyValue("property1", "value2");
            e1.setPropertyValue("property2", false);
            assertEquals("value2", e1.getPropertyValue("property1"));
            assertEquals(false, e1.getPropertyValue("property2"));

        }
        catch(Exception ex) {
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("new Edge(\""+ e1_label + "\", 1, 2, new HashMap<String, Object>(){{put(\"property1\", \"value1\");}}) - unexpected exception: " + ex.getMessage(), true);
        }
        
        e1 = null;

        // test static fromJSONObject method

        JSONObject j1 = new JSONObject();
        j1.put("label", "likes");   // required
        j1.put("outV", 1);          // required
        j1.put("inV", 2);           // required
        e1 = Edge.fromJSONObject(j1);
        assertNotNull(e1);
        assertEquals("likes", e1.getLabel());
        assertEquals("1", e1.getOutV());
        assertEquals("2", e1.getInV());
        assertNull(e1.toString(), e1.getId());         // not set
        assertNull(e1.toString(), e1.getProperties());   // not set
        assertNull(e1.toString(), e1.getOutVLabel());  // not set
        assertNull(e1.toString(), e1.getInVLabel());   // not set

        j1 = new JSONObject();
        j1.put("label", "likes");   // required
        j1.put("outV", 1);          // required
        j1.put("inV", 2);           // required
        j1.put("outVLabel", "person"); // optional 
        j1.put("inVLabel", "pet");     // optional
        j1.put("id", "abc-def");       // optional
        j1.put("properties", new HashMap<String, Object>(){{put("how much","a little");}});          // optional

        e1 = Edge.fromJSONObject(j1);
        assertNotNull(e1);
        assertEquals("likes", e1.getLabel());
        assertEquals("1", e1.getOutV());
        assertEquals("2", e1.getInV());
        assertEquals("abc-def", e1.getId());         
        assertEquals("person", e1.getOutVLabel());         
        assertEquals("pet", e1.getInVLabel()); 
        assertEquals(1, e1.getProperties().size());  
        assertEquals("a little", e1.getPropertyValue("how much"));
    }

    @Test
    public void testEdgeClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.Edge");
        Edge e1 = null;
        try {
            e1 = new Edge(null, null, null);
            assertFalse("new Edge(null, null, null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        try {
            e1 = new Edge(null, null, null, null);
            assertFalse("new Edge(null, null, null, null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        try {
            e1 = new Edge("label", null, null, null);
            assertFalse("new Edge(\"label\", null, null, null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        try {
            Edge.fromJSONObject(new JSONObject());
            assertFalse("Edge.fromJSONObject(new JSONObject())", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
    }
}
