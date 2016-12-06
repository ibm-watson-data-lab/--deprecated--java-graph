package com.ibm.graph.client;

import com.ibm.graph.client.schema.EdgeIndex;
import com.ibm.graph.client.schema.EdgeLabel;
import com.ibm.graph.client.schema.EntityIndex;
import com.ibm.graph.client.schema.EntityLabel;
import com.ibm.graph.client.schema.PropertyKey;
import com.ibm.graph.client.schema.Schema;
import com.ibm.graph.client.schema.VertexLabel;
import com.ibm.graph.client.schema.VertexIndex;
import com.ibm.graph.client.Vertex;

import com.ibm.graph.client.GraphException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.wink.json4j.JSONObject;
import static org.hamcrest.CoreMatchers.hasItem;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SchemaTests {

    private static ArrayList<String> graphsCreated;
    private static Logger logger =  LoggerFactory.getLogger(SchemaTests.class);

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
    public void testSchemaClass() throws Exception {
        logger.info("Executing tests for com.ibm.graph.client.schema.Schema");
        try {
            Schema s = new Schema();
            assertNotNull(s);
            assertFalse(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 0);
            assertFalse(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 0);
            assertFalse(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 0);
            assertFalse(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 0);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);

            s = new Schema(new PropertyKey[]{
                                    new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                                },
                                new VertexLabel[]{
                                    new VertexLabel("person")
                                },
                                new EdgeLabel[]{
                                    new EdgeLabel("friend")
                                },
                                new VertexIndex[]{
                                    new VertexIndex("vertexByName", new String[]{"name"}, true, true)
                                },
                                new EdgeIndex[]{});
            assertNotNull(s);
            assertTrue(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 1);
            assertTrue(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 1);
            assertTrue(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 1);
            assertTrue(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 1);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);

            s = null;
            // test method fromJSONObject
            s = Schema.fromJSONObject(new JSONObject());
            assertNotNull(s);
            assertFalse(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 0);
            assertFalse(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 0);
            assertFalse(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 0);
            assertFalse(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 0);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);            

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.Schema test: unexpected exception.", ex);
            assertFalse("com.ibm.graph.client.schema.Schema test: unexpected exception: " + ex.getMessage(), true);   
        }
    }

   @Test
    public void testSchemaClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for com.ibm.graph.client.schema.Schema");
        
        try {
            // null parameter
            Schema.fromJSONObject(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.Schema error handling test: unexpected exception.", ex);
            assertFalse("com.ibm.graph.client.schema.Schema error handling test: unexpected exception: " + ex.getMessage(), true);   
        }
    }

    @Test
    public void getSchema() throws Exception {
        logger.info("Executing graphClient.getSchema() test.");
        // save current graph id
        String currentGraphId = TestSuite.graphClient.getGraphId();
        assertNotNull(currentGraphId);

        String graphId = null;

        try {
            // create new graph (no schema should be defined)
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            TestSuite.graphClient.setGraph(graphId);

            // start tests
            Schema s1 = TestSuite.graphClient.getSchema();
            assertNotNull("graphClient.getSchema()", s1);
            assertFalse(s1.definesPropertyKeys());
            assertFalse(s1.definesVertexLabels());
            assertFalse(s1.definesEdgeLabels());
            assertFalse(s1.definesVertexIndexes());
            assertFalse(s1.definesEdgeIndexes());
        }
        catch(Exception ex) {
            // fail
            logger.error("graphClient.getSchema() test: unexpected exception.", ex);
            assertFalse("graphClient.getSchema() test: unexpected exception: " + ex.getMessage(), true);   
        }
        finally {
            // cleanup
            try {
                // switch to previous graph
                TestSuite.graphClient.setGraph(currentGraphId);
                if(graphId != null) {
                    // delete graph
                    TestSuite.graphClient.deleteGraph(graphId);    
                    graphsCreated.remove(graphId);
                }
                
            }
            catch(Exception ex) {
                // ignore
            }
        }
    }

    @Ignore("not defined")
    @Test
    public void getSchemaErrorHandling() throws Exception {
        logger.info("Executing graphClient.getSchema() error handling test.");
        // no tests yet
    }

    @Test
    public void saveSchema() throws Exception {
        logger.info("Executing graphClient.saveSchema(...) test.");
        // save current graph id
        String currentGraphId = TestSuite.graphClient.getGraphId();
        assertNotNull(currentGraphId);

        String graphId = null;

        try {
            // create new graph (schema should be empty)
            graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            TestSuite.graphClient.setGraph(graphId);

            // start tests
            Schema s1 = TestSuite.graphClient.getSchema();
            assertNotNull("graphClient.saveSchema()", s1);
            assertFalse(s1.definesPropertyKeys());
            assertFalse(s1.definesVertexLabels());
            assertFalse(s1.definesEdgeLabels());
            assertFalse(s1.definesVertexIndexes());
            assertFalse(s1.definesEdgeIndexes());

            s1 = TestSuite.graphClient.saveSchema(s1);
            assertNotNull("graphClient.saveSchema()", s1);
            assertFalse(s1.definesPropertyKeys());
            assertFalse(s1.definesVertexLabels());
            assertFalse(s1.definesEdgeLabels());
            assertFalse(s1.definesVertexIndexes());
            assertFalse(s1.definesEdgeIndexes());

            s1 = null;
            s1 = new Schema(new PropertyKey[]{
                                new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                            },
                            new VertexLabel[]{
                                new VertexLabel("person")
                            },
                            new EdgeLabel[]{
                                new EdgeLabel("friend")
                            },
                            new VertexIndex[]{
                                new VertexIndex("vertexByName", new String[]{"name"}, true, true)
                            },
                            new EdgeIndex[]{});

            // update schema
            s1 = TestSuite.graphClient.saveSchema(s1);
            // verify results
            assertNotNull(s1);
            assertTrue(s1.toString(), s1.definesPropertyKeys());
            assertThat(Arrays.asList(s1.getPropertyKeys()), hasItem(new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)));
            assertTrue(s1.toString(), s1.definesVertexLabels());
            assertThat(Arrays.asList(s1.getVertexLabels()), hasItem(new VertexLabel("person")));
            assertTrue(s1.toString(), s1.definesEdgeLabels());
            assertThat(Arrays.asList(s1.getEdgeLabels()), hasItem(new EdgeLabel("friend")));
            assertTrue(s1.toString(), s1.definesVertexIndexes());
            assertThat(Arrays.asList(s1.getVertexIndexes()), hasItem(new VertexIndex("vertexByName", new String[]{"name"}, true, true)));
            assertFalse(s1.toString(), s1.definesEdgeIndexes());

            s1 = null;

/*            s1 = new Schema(new PropertyKey[]{
                                new PropertyKey("name", "String", "SINGLE"),
                                new PropertyKey("age", "Integer", "SINGLE"),
                                new PropertyKey("type", "String", "SINGLE")
                            },
                            new VertexLabel[]{
                                new VertexLabel("person"),
                                new VertexLabel("pet")
                            },
                            new EdgeLabel[]{
                                new EdgeLabel("friend"),
                                new EdgeLabel("owns")
                            },
                            new VertexIndex[]{
                                new VertexIndex("vertexByName", new String[]{"name"}, true, true),
                                new VertexIndex("vertexByAge", new String[]{"age"}, true, true)
                            },
                            new EdgeIndex[]{});

            // update schema again
            s1 = TestSuite.graphClient.saveSchema(s1);
            // verify results
            assertNotNull(s1);
            assertTrue(s1.toString(), s1.definesPropertyKeys());
            assertThat(Arrays.asList(s1.getPropertyKeys()), hasItem(new PropertyKey("name", "String", "SINGLE")));
            assertThat(Arrays.asList(s1.getPropertyKeys()), hasItem(new PropertyKey("age", "Integer", "SINGLE")));
            assertThat(Arrays.asList(s1.getPropertyKeys()), hasItem(new PropertyKey("type", "String", "SINGLE")));
            assertTrue(s1.toString(), s1.definesVertexLabels());
            assertThat(Arrays.asList(s1.getVertexLabels()), hasItem(new VertexLabel("person")));
            assertThat(Arrays.asList(s1.getVertexLabels()), hasItem(new VertexLabel("pet")));
            assertTrue(s1.toString(), s1.definesEdgeLabels());
            assertThat(Arrays.asList(s1.getEdgeLabels()), hasItem(new EdgeLabel("friend")));
            assertThat(Arrays.asList(s1.getEdgeLabels()), hasItem(new EdgeLabel("owns")));
            assertTrue(s1.toString(), s1.definesVertexIndexes());
            assertThat(Arrays.asList(s1.getVertexIndexes()), hasItem(new VertexIndex("vertexByName", new String[]{"name"}, true, true)));
            assertThat(Arrays.asList(s1.getVertexIndexes()), hasItem(new VertexIndex("vertexByName", new String[]{"age"}, true, true)));
            assertFalse(s1.toString(), s1.definesEdgeIndexes());
*/


        }
        catch(Exception ex) {
            // fail
            logger.error("graphClient.getSchema() test: unexpected exception.", ex);
            assertFalse("graphClient.getSchema() test: unexpected exception: " + ex.getMessage(), true);   
        }
        finally {
            // cleanup
            try {
                // switch to previous graph
                TestSuite.graphClient.setGraph(currentGraphId);
                if(graphId != null) {
                    // delete graph
                    TestSuite.graphClient.deleteGraph(graphId);    
                    graphsCreated.remove(graphId);
                }               
            }
            catch(Exception ex) {
                // ignore
            }
        }

    }

    @Test
    public void saveSchemaErrorHandling() throws Exception {
        logger.info("Executing graphClient.saveSchema(...) error handling test.");

        // save current graph id
        String currentGraphId = TestSuite.graphClient.getGraphId();
        assertNotNull(currentGraphId);

        String graphId = null;

        // create new graph (schema should be empty)
        graphId = TestSuite.graphClient.createGraph();
        assertNotNull(graphId);
        graphsCreated.add(graphId);
        TestSuite.graphClient.setGraph(graphId);

        try {
            // parameter cannot be null
            Schema s = TestSuite.graphClient.saveSchema(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("graphClient.saveSchema(...) test: unexpected exception.", ex);
            assertFalse("graphClient.saveSchema(...) test: unexpected exception: " + ex.getMessage(), true);   
        }

        Schema s1 = null;

        try {
    
            s1 = new Schema(new PropertyKey[]{
                                new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                            },
                            new VertexLabel[]{
                                new VertexLabel("person")
                            },
                            new EdgeLabel[]{
                                new EdgeLabel("friend")
                            },
                            new VertexIndex[]{
                                new VertexIndex("vertexByName", new String[]{"name"}, true, true)
                            },
                            new EdgeIndex[]{});

            // update schema
            s1 = TestSuite.graphClient.saveSchema(s1);
            // verify results
            assertNotNull(s1);
        }
        catch(Exception ex) {
            // fail
            logger.error("graphClient.saveSchema(...) error handling test: unexpected exception.", ex);
            assertFalse("graphClient.saveSchema(...) error handling test: unexpected exception: " + ex.getMessage(), true);   
        }            

        try {
            s1 = null;
            s1 = new Schema(new PropertyKey[]{
                                new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE),
                                new PropertyKey("age", PropertyKey.PropertyKeyDataType.Integer, PropertyKey.PropertyKeyCardinality.SINGLE),
                                new PropertyKey("type", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                            },
                            new VertexLabel[]{
                                new VertexLabel("person"),
                                new VertexLabel("pet")
                            },
                            new EdgeLabel[]{
                                new EdgeLabel("friend"),
                                new EdgeLabel("owns")
                            },
                            new VertexIndex[]{
                                new VertexIndex("vertexByName", new String[]{"name"}, true, true),
                                new VertexIndex("vertexByAge", new String[]{"age"}, true, true)
                            },
                            new EdgeIndex[]{});

            // update schema again; should fail
            s1 = TestSuite.graphClient.saveSchema(s1);
            assertFalse(true);
        }
        catch(GraphException gex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("graphClient.saveSchema(...) error handling test: unexpected exception.", ex);
            assertFalse("graphClient.saveSchema(...) error handling test: unexpected exception: " + ex.getMessage(), true);   
        }
        finally {
            // cleanup
            try {
                // switch to previous graph
                TestSuite.graphClient.setGraph(currentGraphId);
                if(graphId != null) {
                    // delete graph
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
