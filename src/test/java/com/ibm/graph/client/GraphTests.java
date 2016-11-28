package com.ibm.graph.client;

import com.ibm.graph.client.actions.CreateGraphAction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
    public void createGraph() throws Exception {
        logger.info("Executing createGraph test.");
        // create a graph, verify it exists, and then delete it
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
    }

    @Test
    public void createGraphRandomId() throws Exception {
        logger.info("Executing createGraphRandomId test.");
        // create a graph, verify it exists, but don't delete it (it will be used for the rest of the tests)
        String randomGraphId = UUID.randomUUID().toString();
        String graphId = TestSuite.graphClient.createGraph(randomGraphId);
        assertEquals(randomGraphId, graphId);
        // verify graph exits
        String[] graphIds = TestSuite.graphClient.getGraphs();
        assertNotNull(graphIds);
        assertThat(Arrays.asList(graphIds), hasItems(graphId));
        // set the global graph id for the rest of the tests
        // and add the create graph action, so that it can be deleted at the end of the tests
        TestSuite.setGlobalGraphId(graphId);
//        TestSuite.addAction(new CreateGraphAction(graphId));
    }
}
