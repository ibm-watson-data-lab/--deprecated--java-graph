package com.ibm.graph.client;

import com.ibm.graph.GraphException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class GraphTests {

    private static List<String> graphsCreated;
    private static Logger logger =  LoggerFactory.getLogger(GraphTests.class);

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
    public void getGraphId() throws Exception {
        logger.info("Executing IBMGraphClient.getGraphId() test.");
        try {
            assertNotNull(TestSuite.graphClient.getGraphId());
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

    @Test
    public void getGraphs() throws Exception {
        logger.info("Executing IBMGraphClient.getGraphs() test.");
        try {
            // create a graph, verify it exists, and then delete it (no valid name provided)
            String graphId = TestSuite.graphClient.createGraph(null);
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            // verify graph exits
            String[] graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), hasItems(graphId));
            // delete graph
            TestSuite.graphClient.deleteGraph(graphId);
            graphsCreated.remove(graphId);
            // verify graph no longer exists
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), not(hasItems(graphId)));
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

    @Test
    public void createGraph() throws Exception {
        logger.info("Executing IBMGraphClient.createGraph(...) test.");
        try {
            // create a graph, verify it exists, and then delete it (no valid name provided)
            String graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            // verify graph exits
            String[] graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), hasItems(graphId));
            // delete graph
            TestSuite.graphClient.deleteGraph(graphId);
            graphsCreated.remove(graphId);
            // verify graph no longer exists
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), not(hasItems(graphId)));

            // create a graph, verify it exists, and then delete it (no valid name provided)
            graphId = TestSuite.graphClient.createGraph(null);
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            // verify graph exits
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), hasItems(graphId));
            // delete graph
            TestSuite.graphClient.deleteGraph(graphId);
            graphsCreated.remove(graphId);
            // verify graph no longer exists
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), not(hasItems(graphId)));

            // create a graph, verify it exists, and then delete it (no valid name provided)
            graphId = TestSuite.graphClient.createGraph("      ");
            assertNotNull(graphId);
            graphsCreated.add(graphId);
            // verify graph exits
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), hasItems(graphId));
            // delete graph
            TestSuite.graphClient.deleteGraph(graphId);
            graphsCreated.remove(graphId);
            // verify graph no longer exists
            graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), not(hasItems(graphId)));
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

   @Test
    public void setGraph() throws Exception {
        logger.info("Executing IBMGraphClient.setGraph(...) test.");
        try {

        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }        

    @Test
    public void testSetGraphErrorhandling() throws Exception {
        logger.info("Executing IBMGraphClient.setGraph(...) error handling test.");
        // null parameter
        try {
            TestSuite.graphClient.setGraph(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        // empty parameter
        try {
            TestSuite.graphClient.setGraph("");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        // empty parameter
        try {
            TestSuite.graphClient.setGraph("      ");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        // unknown graph
        try {
            TestSuite.graphClient.setGraph(UUID.randomUUID().toString());
            assertFalse(true);
        }
        catch(GraphException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }  

    @Test
    public void testCreateGraphErrorHandling() throws Exception {
        logger.info("Executing IBMGraphClient.createGraph() error handling test.");
        
        try {
            TestSuite.graphClient.createGraph(null);
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

   @Test
    public void deleteGraph() throws Exception {
        logger.info("Executing IBMGraphClient.deleteGraph(...) test.");
        
        try {
            // create dummy graph
            String graphId = TestSuite.graphClient.createGraph();
            assertNotNull(graphId);
            assertTrue(TestSuite.graphClient.deleteGraph(graphId));
            // verify graph no longer exists
            String[] graphIds = TestSuite.graphClient.getGraphs();
            assertNotNull(graphIds);
            assertThat(Arrays.asList(graphIds), not(hasItems(graphId)));

        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

    @Test
    public void testDeleteGraphErrorHandling() throws Exception {
        logger.info("Executing IBMGraphClient.deleteGraph(...) error handling test.");
        
        try {
            // invalid parameter
            TestSuite.graphClient.deleteGraph(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // invalid parameter            
            TestSuite.graphClient.deleteGraph("");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // invalid parameter            
            TestSuite.graphClient.deleteGraph("     ");
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // unknown graph 
            assertFalse(TestSuite.graphClient.deleteGraph(UUID.randomUUID().toString()));
        }
        catch(Exception ex) {
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }


    @Test
    public void createGraphRandomId() throws Exception {
        logger.info("Executing IBMGraphClient.createGraph(...) test.");

        // create a graph, verify it exists, but don't delete it (it will be used for the rest of the tests)
        String randomGraphId = UUID.randomUUID().toString();
        String graphId = TestSuite.graphClient.createGraph(randomGraphId);
        assertEquals(randomGraphId, graphId);
        // verify graph exits
        String[] graphIds = TestSuite.graphClient.getGraphs();
        assertNotNull(graphIds);
        assertThat(Arrays.asList(graphIds), hasItems(graphId));
        // set the global graph id for the rest of the tests
        TestSuite.setGlobalGraphId(graphId);
        assertEquals(TestSuite.graphClient.getGraphId(), graphId);
    }
}
