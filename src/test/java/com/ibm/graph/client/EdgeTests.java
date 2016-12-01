package com.ibm.graph.client;

import com.ibm.graph.client.Edge;
import com.ibm.graph.GraphException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class EdgeTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeTests.class);

    @Test
    public void testEdgeClass() throws Exception {
        logger.info("Executing testEdgeClass test.");
        Edge e1 = null;
        try {
            e1 = new Edge("label", 1, 2);
        }
        catch(Exception ex) {
            assertFalse("new Edge(\"label\", 1, 2): " + ex.getMessage(), true);
        }
        assertNotNull(e1);
        e1 = null;

        try {
            e1 = new Edge("label", 1, 2, null);
        }
        catch(Exception ex) {
            assertFalse("new Edge(\"label\", 1, 2, null): " + ex.getMessage(), true);
        }
        assertNotNull(e1);
        e1 = null;

        try {
            e1 = new Edge("label", 1, 2, new HashMap());
        }
        catch(Exception ex) {
            assertFalse("new Edge(\"label\", 1, 2, new HashMap()): " + ex.getMessage(), true);
        }
        assertNotNull(e1);
        e1 = null;

    }

    @Test
    public void testEdgeClassErrorHandling() throws Exception {
        logger.info("Executing testEdgeClassErrorHandling test.");
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

   @Test
    public void addEdge() throws Exception {
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
        logger.info("Executing addEdge error handling test.");
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
            e1 = TestSuite.graphClient.addEdge(new Edge("likes", new Object(), new Object()));
            assertTrue("TestSuite.graphClient.addEdge(new Edge(\"likes\", new Object(), new Object())", false);
        }
        catch(GraphException gex) {
            // expected
        }
        assertNull("TestSuite.graphClient.addEdge(new Edge(\"likes\", new Object(), new Object())", e1);

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
        logger.info("Executing getEdge test.");
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
        logger.info("Executing getEdge error handling test.");

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
            e1 = TestSuite.graphClient.getEdge(1234567);
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
        logger.info("Executing updateEdge test.");
        // create two vertices
        Vertex v1 = new Vertex("person");
        v1 = TestSuite.graphClient.addVertex(v1);
        assertNotNull(v1);
        Vertex v2 = new Vertex("person");
        v2 = TestSuite.graphClient.addVertex(v2);
        assertNotNull(v2);

        // create a new edge between the vertices
        Edge e1 = TestSuite.graphClient.addEdge(new Edge("likes", v1.getId(), v2.getId()));
        assertNotNull(e1);

System.out.println("***");
System.out.println(e1.toString());        
System.out.println("***");

        // add a new property to the edge
        e1.setPropertyValue("how much", "very much");

        // should succeed
        try {
            e1 = TestSuite.graphClient.updateEdge(e1);
            assertNotNull("TestSuite.graphClient.updateEdge(e1)", e1);
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
        logger.info("Executing updateEdge error handling test.");

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

        e1 = new Edge("label", 1, 2);
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
        logger.info("Executing deleteEdge test.");

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
        logger.info("Executing deleteEdge error handling test.");
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

        // delete a non-existing edge
        assertFalse("TestSuite.graphClient.deleteEdge(123456789)", TestSuite.graphClient.deleteEdge(123456789));
  
    }


   @Test
    public void createDeleteEdge() throws Exception {
        logger.info("Executing createAndDeleteEdge test.");
        // find jane and john doe
        // and create edge from jane doe to john doe
        Vertex janeDoe = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"person\").has(\"name\", \"Jane Doe\")").getResultAsVertex(0);
        Vertex johnDoe = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"person\").has(\"name\", \"John Doe\")").getResultAsVertex(0);
        Edge originalEdge = new Edge("friends", janeDoe.getId(), johnDoe.getId(), new HashMap() {{
            put("date",System.currentTimeMillis()/1000);
        }});
        Edge addedEdge = TestSuite.graphClient.addEdge(originalEdge);
        assertNotNull(addedEdge);
        // get edge
        Edge edge = TestSuite.graphClient.getEdge(addedEdge.getId());
        assertNotNull(edge);
        assertEquals(edge.getId(),addedEdge.getId());
        // delete edge
        boolean deleted = TestSuite.graphClient.deleteEdge(edge.getId());
        assertTrue(deleted);
        // make sure edge is gone
        edge = TestSuite.graphClient.getEdge(edge.getId());
        assertNull(edge);
    }

    @Test
    public void createUpdateEdge() throws Exception {
        logger.info("Executing createAndUpdateEdge test.");
        // find jane and john doe
        // create edge from john doe to jane doe
        Vertex johnDoe = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"person\").has(\"name\", \"John Doe\")").getResultAsVertex(0);
        Vertex janeDoe = TestSuite.graphClient.executeGremlin("g.V().hasLabel(\"person\").has(\"name\", \"Jane Doe\")").getResultAsVertex(0);
        Edge originalEdge = new Edge("friends", johnDoe.getId(), janeDoe.getId(), new HashMap() {{
            put("date",(int)(System.currentTimeMillis()/1000));
        }});
        Edge addedEdge = TestSuite.graphClient.addEdge(originalEdge);
        assertNotNull(addedEdge);
        assertEquals(addedEdge.getPropertyValue("date"), originalEdge.getPropertyValue("date"));
        // get edge
        Edge edge = TestSuite.graphClient.getEdge(addedEdge.getId());
        assertNotNull(edge);
        assertEquals(edge.getId(),addedEdge.getId());
        assertEquals(edge.getPropertyValue("date"), addedEdge.getPropertyValue("date"));
        // TODO: this is failing
//        // update edge
//        edge.setPropertyValue("date", (System.currentTimeMillis()/1000));
//        Edge updatedEdge = TestSuite.graphClient.updateEdge(edge);
//        assertNotNull(updatedEdge);
//        assertEquals(updatedEdge.getPropertyValue("date"), edge.getPropertyValue("date"));
//        // query edge again to verify
//        edge = TestSuite.graphClient.getEdge(updatedEdge.getId());
//        assertNotNull(edge);
//        assertEquals(edge.getPropertyValue("date"), updatedEdge.getPropertyValue("date"));
    }    
}
