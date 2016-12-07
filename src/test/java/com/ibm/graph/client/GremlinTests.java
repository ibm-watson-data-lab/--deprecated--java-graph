package com.ibm.graph.client;

import com.ibm.graph.client.response.ResultSet;
import com.ibm.graph.client.schema.*;

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
 * Test scope:
 * - graphClient.executeGremlin
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
    public void executeGremlin() throws Exception {
        logger.info("Executing graphClient.executeGremlin(...) test.");
        Vertex v1 = null, v2 = null, v3 = null;
        ResultSet rs = null;

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

            // create schema (indexes are required if graph scans are disabled)
            Schema s = new Schema(new PropertyKey[]{
                                    new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE),
                                    new PropertyKey("age", PropertyKey.PropertyKeyDataType.Integer, PropertyKey.PropertyKeyCardinality.SINGLE),
                                    new PropertyKey("married", PropertyKey.PropertyKeyDataType.Boolean, PropertyKey.PropertyKeyCardinality.SINGLE)
                                },
                                new VertexLabel[]{
                                    new VertexLabel("person")
                                },
                                new EdgeLabel[]{
                                },
                                new VertexIndex[]{
                                    new VertexIndex("vertexByName", new String[]{"name"}, true, false),
                                    new VertexIndex("vertexByAge", new String[]{"age"}, true, false),
                                    new VertexIndex("vertexByMarried", new String[]{"married"}, true, false)
                                },
                                new EdgeIndex[]{});

            // save schema
            TestSuite.graphClient.saveSchema(s);

            // run tests ...

            v1 = new Vertex("person");
            assertNotNull(v1);
            v1.setPropertyValue("name", "Jane");
            v1.setPropertyValue("age", 27);
            v1.setPropertyValue("married", true);
            v1 = TestSuite.graphClient.addVertex(v1);
            assertNotNull(v1);

            v2 = new Vertex("person");
            assertNotNull(v2);
            v2.setPropertyValue("name", "Peer");
            v2.setPropertyValue("age", 24);
            v2.setPropertyValue("married", false);
            v2 = TestSuite.graphClient.addVertex(v2);
            assertNotNull(v2);

            v3 = new Vertex("person");
            assertNotNull(v3);
            v3.setPropertyValue("name", "Jill");
            v3.setPropertyValue("age", 33);
            v3.setPropertyValue("married", true);
            v3 = TestSuite.graphClient.addVertex(v3);
            assertNotNull(v3);

            // gremlin without bindings
            rs = TestSuite.graphClient.executeGremlin("g.V(" + v1.getId() + ");");
            assertNotNull(rs);
            assertEquals(1, rs.getResultCount());
            rs = TestSuite.graphClient.executeGremlin("g.V(" + v2.getId() + ");");
            assertNotNull(rs);
            assertEquals(1, rs.getResultCount());
            rs = TestSuite.graphClient.executeGremlin("g.V(" + v3.getId() + ");");
            assertNotNull(rs);
            assertEquals(1, rs.getResultCount());
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"Peer\");");
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(),1, rs.getResultCount());
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"age\", 33);");
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(),1, rs.getResultCount()); // v2
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"nosuchproperty\", \"nobody\");", null);
            assertNotNull(rs);
            assertFalse(rs.hasResults());   
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"married\", true);");
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(), 2, rs.getResultCount());  // v1,v3

            //
            // gremlin and bindings
            //
            HashMap<String, Object> bindings = new HashMap();
            // no bindings
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"Peer\");", bindings);
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(), 1, rs.getResultCount());   // v2  

            bindings.put("v1_name", "Peer");
            // binding should be ignored because there are no variable references in the gremlin
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"name\", \"Peer\");", bindings);
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(), 1, rs.getResultCount());   // v2  

            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"name\", v1_name);", bindings);
            assertNotNull(rs);
            assertEquals(rs.getResponse().toString(), 1, rs.getResultCount());   // v2    

            bindings.clear();
            bindings.put("v1_name", "Jill");
            bindings.put("v2_age", 33);
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"name\", v1_name).has(\"age\", v2_age);", bindings);
            assertNotNull(rs);             
            assertEquals(rs.getResponse().toString(), 1, rs.getResultCount());   // v3

            bindings.clear();
            bindings.put("v1_married", true);
            rs = TestSuite.graphClient.executeGremlin("g.V().has(\"married\", v1_married);", bindings);
            assertNotNull(rs);            
            assertEquals(rs.getResponse().toString(), 2, rs.getResultCount());   // v1, v3            
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
