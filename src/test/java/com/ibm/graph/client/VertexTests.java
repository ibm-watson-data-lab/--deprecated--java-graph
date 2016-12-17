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
 * - graphClient.addVertex
 * - graphClient.updateVertex
 * - graphClient.deleteVertex
 * - graphClient.getVertex
 */
public class VertexTests {

    private static Logger logger =  LoggerFactory.getLogger(VertexTests.class);

    private static ArrayList<String> graphsCreated;

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
    public void addVertex() throws Exception {
        logger.info("Executing graphClient.addVertex(...) test.");

        Vertex v1 = null, v2 = null, v3 = null;
        String graphId = null;
        String currentGraphId = null;
        try {

            // graph setup
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);

            v1 = new Vertex();
            assertNotNull(v1);
            v1 = TestSuite.graphClient.addVertex(v1);
            assertNotNull(v1);
            assertNotNull(v1.toString(), v1.getId());        
            assertNull(v1.toString(), v1.getProperties());
            assertEquals(v1.toString(), "vertex", v1.getLabel()); // default

            v2 = new Vertex(null);
            assertNotNull(v2);
            v2 = TestSuite.graphClient.addVertex(v2);
            assertNotNull(v2);
            assertNotNull(v2.toString(), v2.getId());        
            assertNull(v2.toString(), v2.getProperties());
            assertEquals(v2.toString(), "vertex", v2.getLabel()); // default

            v3 = new Vertex("person");
            assertNotNull(v3);
            v3.setPropertyValue("name", "Jill");
            v3.setPropertyValue("age", 33);
            v3 = TestSuite.graphClient.addVertex(v3);
            assertNotNull(v3);
            assertNotNull(v3.toString(), v3.getId());        
            assertEquals(v3.toString(), "person", v3.getLabel());
            assertNotNull(v3.toString(), v3.getProperties());
            assertEquals(v3.toString(), "Jill", v3.getPropertyValue("name"));
            assertEquals(v3.toString(), 33, v3.getPropertyValue("age"));
            assertEquals(v3.toString(), 2, v3.getProperties().size());
        }
        catch(Exception ex) {
            // fail
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("graphClient.addVertex(...) - unexpected exception: " + ex.getMessage(), true);
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
    public void addVertexErrorHandling() throws Exception {
        logger.info("Executing graphClient.addVertex(...) error handling test.");
        // parameter checking
        try {
            TestSuite.graphClient.addVertex(null);
            assertTrue("graphClient.addVertex(null)", false); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error(ExceptionUtils.getStackTrace(ex));
            assertFalse("graphClient.addVertex(null) - unexpected exception: " + ex.getMessage(), true);
        }
    }

    @Test
    public void updateVertex() throws Exception {
        logger.info("Executing graphClient.updateVertex(...) test.");
        Vertex v = null;
        String graphId = null;
        String currentGraphId = null;
        try {
            // graph setup
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);

            v = new Vertex("person");
            assertNotNull(v);
            v = TestSuite.graphClient.addVertex(v);
            assertNotNull(v);
            assertNotNull(v.toString(), v.getId());
            assertEquals(v.toString(), "person", v.getLabel());
            assertNull(v.toString(), v.getProperties());

            v.setPropertyValue("name", "Jim");
            v = TestSuite.graphClient.updateVertex(v);
            assertNotNull(v);
            assertEquals(v.toString(), "person", v.getLabel());
            assertNotNull(v.toString(), v.getId());
            assertEquals(v.toString(), 1, v.getProperties().size());
            assertEquals(v.toString(), "Jim", v.getPropertyValue("name"));

            v.setPropertyValue("age", 21);
            v = TestSuite.graphClient.updateVertex(v);
            assertNotNull(v);
            assertEquals(v.toString(), "person", v.getLabel());
            assertNotNull(v.toString(), v.getId());
            assertEquals(v.toString(), 2, v.getProperties().size());
            assertEquals(v.toString(), "Jim", v.getPropertyValue("name"));
            assertEquals(v.toString(), 21, v.getPropertyValue("age"));

            // cleanup
            assertTrue(TestSuite.graphClient.deleteVertex(v.getId()));
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.updateVertex(...): unexpected exception: " + ex.getMessage(), true);   
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
    public void updateVertexErrorHandling() throws Exception {
        logger.info("Executing graphClient.updateVertex(...) error handling test.");
        try {
            TestSuite.graphClient.updateVertex(null);
            assertFalse("TestSuite.graphClient.updateVertex(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass (vertex is null)
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.updateVertex(null): unexpected exception: " + ex.getMessage(), true);   
        }
        try {
            Vertex v = new Vertex();
            TestSuite.graphClient.updateVertex(v);  
            assertFalse("TestSuite.graphClient.updateVertex(v)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass (vertex is missing id property)
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.updateVertex(v): unexpected exception: " + ex.getMessage(), true);   
        }      
    }

    @Test
    public void deleteVertex() throws Exception {
        logger.info("Executing graphClient.deleteVertex(...) test.");

        String graphId = null;
        String currentGraphId = null;
        try {
            // graph setup
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);

            Vertex v = new Vertex("person");
            assertNotNull(v);
            v = TestSuite.graphClient.addVertex(v);
            assertNotNull(v);
            assertNotNull(v.toString(), v.getId());
            assertEquals(v.toString(), "person", v.getLabel());
            assertNull(v.toString(), v.getProperties());

            //vertex exists - delete should return true    
            assertTrue(TestSuite.graphClient.deleteVertex(v.getId()));
            //vertex doesn't exist anymore - delete should return false
            assertFalse(TestSuite.graphClient.deleteVertex(v.getId()));
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.deleteVertex(...): unexpected exception: " + ex.getMessage(), true);   
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
    public void deleteVertexErrorHandling() throws Exception {
        logger.info("Executing graphClient.deleteVertex(...) error handling test.");
        // parameter checking
        try {
            TestSuite.graphClient.deleteVertex(null);
            assertFalse("TestSuite.graphClient.deleteVertex(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.deleteVertex(null) unexpected exception: " + ex.getMessage(), true);   
        }        

        // delete a non-existing vertex
        try {
            assertFalse("TestSuite.graphClient.deleteVertex(123456789)", TestSuite.graphClient.deleteVertex(123456789));
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.deleteVertex(null) unexpected exception: " + ex.getMessage(), true);   
        }        
    }

    @Test
    public void getVertex() throws Exception {
        logger.info("Executing graphClient.getVertex(...) test.");
        String graphId = null;
        String currentGraphId = null;
        try {
            // graph setup
            currentGraphId = TestSuite.graphClient.getGraphId();
            assertNotNull(currentGraphId);
            // create graph with random name
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);

            // create an edge between two vertices
            Vertex v1 = new Vertex();
            v1.setPropertyValue("married",false);
            v1 = TestSuite.graphClient.addVertex(v1);
            assertNotNull(v1);
            Vertex v2 = new Vertex("person");
            v2.setPropertyValue("name","Joy");
            v2.setPropertyValue("age",47);
            v2 = TestSuite.graphClient.addVertex(v2);
            assertNotNull(v2);

            v1 = TestSuite.graphClient.getVertex(v1.getId());
            assertNotNull(v1);
            assertEquals(v1.toString(), "vertex", v1.getLabel());
            assertNotNull(v1.toString(), v1.getId());
            assertEquals(v1.toString(), 1, v1.getProperties().size());
            assertEquals(v1.toString(), false, v1.getPropertyValue("married"));

            v2 = TestSuite.graphClient.getVertex(v2.getId());
            assertNotNull(v2);
            assertEquals(v2.toString(), "person", v2.getLabel());
            assertNotNull(v2.toString(), v2.getId());
            assertNotNull(v2.toString(), v2.getProperties());
            assertEquals(v2.toString(), 2, v2.getProperties().size());
            assertEquals(v2.toString(), "Joy", v2.getPropertyValue("name"));
            assertEquals(v2.toString(), 47, v2.getPropertyValue("age"));

            // cleanup
            assertTrue(TestSuite.graphClient.deleteVertex(v1.getId()));
            assertTrue(TestSuite.graphClient.deleteVertex(v2.getId()));
        }
        catch(Exception ex) {
            // fail
            assertFalse("TestSuite.graphClient.getVertex(...) unexpected exception: " + ex.getMessage(), true);   
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
    public void getVertexErrorHandling() throws Exception {
        logger.info("Executing graphClient.getVertex(...) error handling test.");

        Vertex v1 = null;
        try {
            v1 = TestSuite.graphClient.getVertex(null);
            assertFalse("TestSuite.graphClient.getVertex(null)", true);
        }
        catch(IllegalArgumentException iaex) {
            // pass (vertex id is null)
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.getVertex(null): unexpected exception: " + ex.getMessage(), true);   
        }

        assertNull("TestSuite.graphClient.getVertex(null)", v1);
        v1 = null;
        try {
            v1 = TestSuite.graphClient.getVertex("1234567");
            assertNull("TestSuite.graphClient.getVertex(1234567)", v1);
        }
        catch(Exception ex) {
            // fail
            ex.printStackTrace();
            assertFalse("TestSuite.graphClient.getVertex(1234567): unexpected exception: " + ex.getMessage(), true);   
        }
    }

}
