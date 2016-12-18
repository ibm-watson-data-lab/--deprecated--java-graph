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
 * Test scope: test edge-specific methods
 * - graphClient.addEdge
 * - graphClient.getEdge
 * - graphClient.updateEdge
 * - graphClient.deleteEdge
 */
public class EdgeTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeTests.class);

   @Test
    public void addEdge() throws Exception {
        logger.info("Executing graphClient.addEdge(...) test.");
        // create an edge between two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);

        // should succeed
        Edge e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), v2.getId()));
        assertNotNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", v1.getId(), v2.getId()))", e1);
        Edge e2 = TestSuite.graphClient.addEdge(new Edge("likes", v2.getId(), v1.getId()));
        assertNotNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", v2.getId(), v1.getId()))", e2);

        // cleanup
        assertTrue(TestSuite.graphClient.deleteEdge(e1.getId()));
        assertTrue(TestSuite.graphClient.deleteEdge(e2.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));
    }

    @Test
    public void addEdgeErrorHandling() throws Exception {
        logger.info("Executing graphClient.addEdge(...) error handling test.");
        // parameter checking
        try {
            TestSuite.graphClient.addEdge(null);
            assertTrue("TestSuite.graphClient.addEdge(null)", false);
        }
        catch(IllegalArgumentException iaex) {
            // expected
        }

        // create an edge between two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);

        Edge e1 = null;
        try {
            // should fail            
            e1 = TestSuite.graphClient.addEdge(new Edge(null, null, null));
            assertTrue("TestSuite.graphClient.addEdge(new Edge(null, null, null))", false);
        }
        catch(IllegalArgumentException iaex) {
            // expected
        }
        assertNull("TestSuite.graphClient.addEdge(new Edge(null, null, null))", e1);

        e1 = null;
        try {
            // should fail    
            e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), null));
            assertTrue("TestSuite.graphClient.addEdge(new Edge(\"likes\", v1.getId(), null))", false);
        }
        catch(IllegalArgumentException iaex) {
            // expected
        }
        assertNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", v1.getId(), null))", e1);

        e1 = null;
        try {
            // should fail    
            e1 = TestSuite.graphClient.addEdge(new Edge("likes", null, v2.getId()));
            assertTrue("TestSuite.graphClient.addEdge(new Edge(\"likes\", null, v2.getId())", false);
        }
        catch(IllegalArgumentException iaex) {
            // expected
        }
        assertNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", null, v2.getId())", e1);

        e1 = null;
        try {
            // should fail    
            e1 = TestSuite.graphClient.addEdge(new Edge(null, v1.getId(), v2.getId()));
            assertTrue("TestSuite.graphClient.addEdge(new Edge(new Edge(null, v1.getId(), v2.getId())", false);
        }
        catch(IllegalArgumentException iaex) {
            // expected
        }
        assertNull("TestSuite.graphClient.addEdge(new Edge(null, v1.getId(), v2.getId())", e1);

        // cleanup
        assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));
    }

    @Test
    public void getEdge() throws Exception {
        logger.info("Executing graphClient.getEdge(...) test.");
        // create an edge between two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);

        // should succeed
        Edge e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), v2.getId()));
        assertNotNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", v1.getId(), v2.getId()))", e1);
        Edge e2 = TestSuite.graphClient.addEdge(new Edge("likes", v2.getId(), v1.getId()));
        assertNotNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", v2.getId(), v1.getId()))", e2);

        // cleanup
        assertTrue(TestSuite.graphClient.deleteEdge(e1.getId()));
        assertTrue(TestSuite.graphClient.deleteEdge(e2.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));
    }

    @Test
    public void getEdgeErrorHandling() throws Exception {
        logger.info("Executing graphClient.getEdge(...) error handling test.");

        Edge e1 = null;
        try {
            e1 = TestSuite.graphClient.getEdge(null);
            assertFalse("TestSuite.graphClient.getEdge(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.getEdge(null): unexpected exception: " + ex.getMessage(), true);   
        }
        
        assertNull("TestSuite.graphClient.getEdge(null)", e1);

        e1 = null;
        try {
            e1 = TestSuite.graphClient.getEdge("1234567");
            assertNull("TestSuite.graphClient.getEdge(1234567)", e1);
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.getEdge(1234567): unexpected exception: " + ex.getMessage(), true);   
        }
    }

    @Test
    public void updateEdge() throws Exception {
        logger.info("Executing graphClient.updateEdge(...) test.");
        // create two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        assertNotNull(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);
        assertNotNull(v2);

        Edge e1 = null;

        try {
            // create a new edge between the vertices
            e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), v2.getId(), new HashMap<String, Object>(){{put("since", "last week");}}));
            assertNotNull(e1);
            assertEquals("last week", e1.getPropertyValue("since"));
            assertNull(e1.getPropertyValue("how much"));
            assertEquals(1, e1.getProperties().size());

            // add a new property to the edge
            e1.setPropertyValue("how much", "very much");
            e1 = TestSuite.graphClient.updateEdge(e1);
            assertNotNull("TestSuite.graphClient.updateEdge(e1)", e1);
            assertEquals("last week", e1.getPropertyValue("since"));
            assertEquals("very much", e1.getPropertyValue("how much"));
            assertEquals(2, e1.getProperties().size());

            // update an existing property
            e1.setPropertyValue("since", "last month");
            e1 = TestSuite.graphClient.updateEdge(e1);
            assertNotNull("TestSuite.graphClient.updateEdge(e1)", e1);
            assertEquals("last month", e1.getPropertyValue("since"));
            assertEquals("very much", e1.getPropertyValue("how much"));
            assertEquals(2, e1.getProperties().size());

            // remove all properties
            e1.setPropertyValue("since", null);
            e1.setPropertyValue("how much", null);
            assertNull(e1.getProperties());
            e1 = TestSuite.graphClient.updateEdge(e1);
            assertNotNull("TestSuite.graphClient.updateEdge(e1)", e1);
            assertNull(e1.getProperties());
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.updateEdge(e1): unexpected exception: " + ex.getMessage(), true);   
        }

        // cleanup
        assertTrue(TestSuite.graphClient.deleteEdge(e1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));
    }

    @Test
    public void updateEdgeErrorHandling() throws Exception {
        logger.info("Executing graphClient.updateEdge(...) error handling test.");

        Edge e1 = null;
        try {
            e1 = TestSuite.graphClient.updateEdge(null);
            assertFalse("TestSuite.graphClient.updateEdge(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.updateEdge(null): unexpected exception: " + ex.getMessage(), true);   
        }
        
        assertNull("TestSuite.graphClient.updateEdge(null)", e1);

        e1 = new Edge("label", "1", "2");
        assertNotNull(e1);

        try {
            e1 = TestSuite.graphClient.updateEdge(e1);
            assertFalse("TestSuite.graphClient.updateEdge(new Edge(\"label\", 1, 2))", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.updateEdge(new Edge(\"label\", 1, 2)): unexpected exception: " + ex.getMessage(), true);   
        }
    }

    @Test
    public void deleteEdge() throws Exception {
        logger.info("Executing graphClient.deleteEdge(...) test.");

        // create two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        assertNotNull(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);
        assertNotNull(v2);

        // create edge
        Edge e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), v2.getId()));
        assertNotNull(e1);

        // edge should be deleted 
        assertTrue("TestSuite.graphClient.deleteEdge(e1.getId()) first invocation", TestSuite.graphClient.deleteEdge(e1.getId()));

        // edge no longer exists, fail silently
        assertFalse("TestSuite.graphClient.deleteEdge(e1.getId()) second invocation", TestSuite.graphClient.deleteEdge(e1.getId()));


        // cleanup
        assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
        assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));        
    }

    @Test
    public void deleteEdgeErrorHandling() throws Exception {
        logger.info("Executing graphClient.deleteEdge(...) error handling test.");
        // parameter checking
        try {
            TestSuite.graphClient.deleteEdge(null);
            assertFalse("TestSuite.graphClient.deleteEdge(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.deleteEdge(null) unexpected exception: " + ex.getMessage(), true);   
        }        

        try {
            // delete a non-existing edge
            assertFalse("TestSuite.graphClient.deleteEdge(123456789)", TestSuite.graphClient.deleteEdge(123456789));
            // pass
        }
        catch(GraphException gex) {
            // fail
            logger.error("TestSuite.graphClient.deleteEdge(123456789) unexpected exception.", gex.toString());
            assertFalse("TestSuite.graphClient.deleteEdge(123456789) unexpected GraphException: " + gex.getMessage(), true);   
        }
        catch(Exception ex) {
            // fail
            logger.error("TestSuite.graphClient.deleteEdge(123456789) unexpected exception.", ex);
            assertFalse("TestSuite.graphClient.deleteEdge(123456789) unexpected exception: " + ex.getMessage(), true);   
        }

    } 
}
