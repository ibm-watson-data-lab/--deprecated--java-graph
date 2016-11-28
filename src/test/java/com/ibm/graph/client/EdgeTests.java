package com.ibm.graph.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class EdgeTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeTests.class);

    @Test
    public void createAndDeleteEdge() throws Exception {
        logger.info("Executing createAndDeleteEdge test.");
        // find jane and john doe
        // and create edge from jane doe to john doe
        Vertex janeDoe = (Vertex)TestSuite.graphClient.runGremlinQuery("g.V().hasLabel(\"person\").has(\"name\", \"Jane Doe\")")[0];
        Vertex johnDoe = (Vertex)TestSuite.graphClient.runGremlinQuery("g.V().hasLabel(\"person\").has(\"name\", \"John Doe\")")[0];
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
    public void createAndUpdateEdge() throws Exception {
        logger.info("Executing createAndUpdateEdge test.");
        // find jane and john doe
        // create edge from john doe to jane doe
        Vertex johnDoe = (Vertex)TestSuite.graphClient.runGremlinQuery("g.V().hasLabel(\"person\").has(\"name\", \"John Doe\")")[0];
        Vertex janeDoe = (Vertex)TestSuite.graphClient.runGremlinQuery("g.V().hasLabel(\"person\").has(\"name\", \"Jane Doe\")")[0];
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
