package com.ibm.graph.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class VertexTests {

    private static Logger logger =  LoggerFactory.getLogger(VertexTests.class);

/*    @Test
    public void createDeleteEmptyVertex() throws Exception {
        logger.info("Executing createDeleteEmptyVertex test.");
        // create vertex
        Vertex addedVertex = TestSuite.graphClient.addVertex();
        assertNotNull(addedVertex);
        // query vertex
        Vertex vertex = TestSuite.graphClient.getVertex(addedVertex.getId());
        assertNotNull(vertex);
        assertEquals(vertex.getId(),addedVertex.getId());
        // delete vertex
        boolean deleted = TestSuite.graphClient.deleteVertex(vertex.getId());
        assertTrue(deleted);
        // make sure vertex is gone
        vertex = TestSuite.graphClient.getVertex(vertex.getId());
        assertNull(vertex);
    }
*/

    @Test
    public void createVertex() throws Exception {
        logger.info("Executing vertex constructor test.");
        Vertex vwl = new Vertex("mylabel");
        assertEquals("mylabel", vwl.getLabel());
        assertNull(vwl.getProperties());
        assertNull(vwl.getId());
        assertNull(vwl.getPropertyValue("notdefined"));
        vwl.setPropertyValue("key", "value");
        assertEquals("value", vwl.getPropertyValue("key"));
        assertNull(vwl.getPropertyValue(null));
        vwl = null;
        Vertex vwlaeh = new Vertex("mylabel", new HashMap());
        assertEquals("mylabel", vwlaeh.getLabel());
        assertNull(vwlaeh.getId());        
        assertTrue(vwlaeh.getProperties().isEmpty());
        assertNull(vwlaeh.getPropertyValue("notdefined"));
        vwlaeh = null;
        Vertex vwlah = new Vertex("mylabel", new HashMap(){{put("a","b");}});
        assertEquals("mylabel", vwlah.getLabel());
        assertNull(vwlah.getId());        
        assertFalse(vwlah.getProperties().isEmpty());
        vwlah.setPropertyValue("key", "value");
        assertEquals("value", vwlah.getPropertyValue("key"));
        assertEquals("b", vwlah.getPropertyValue("a"));
        vwlah = null;

        // TODO Vertex.fromJSONObject
 
    }

    @Test
    public void createUpdateJohnDoeVertex() throws Exception {
        logger.info("Executing createUpdateJohnDoeVertex test.");
        // create vertex
        Vertex originalVertex = new Vertex("person", new HashMap(){{
            put("name","John Doe");
            put("country","United States");
        }});
        Vertex addedVertex = TestSuite.graphClient.addVertex(originalVertex);
        assertNotNull(addedVertex);
        assertEquals(addedVertex.getPropertyValue("name"), originalVertex.getPropertyValue("name"));
        assertEquals(addedVertex.getPropertyValue("country"), originalVertex.getPropertyValue("country"));
        // get vertex
        Vertex vertex = TestSuite.graphClient.getVertex(addedVertex.getId());
        assertNotNull(vertex);
        assertEquals(vertex.getId(),addedVertex.getId());
        assertEquals(vertex.getPropertyValue("name"), addedVertex.getPropertyValue("name"));
        assertEquals(vertex.getPropertyValue("country"), addedVertex.getPropertyValue("country"));
        // update vertex
        vertex.setPropertyValue("country", "Canada");
        Vertex updatedVertex = TestSuite.graphClient.updateVertex(vertex);
        assertNotNull(updatedVertex);
        assertEquals(updatedVertex.getPropertyValue("name"), vertex.getPropertyValue("name"));
        // query vertex again to verify
        vertex = TestSuite.graphClient.getVertex(updatedVertex.getId());
        assertNotNull(vertex);
        assertEquals(vertex.getPropertyValue("name"), updatedVertex.getPropertyValue("name"));
        assertEquals(vertex.getPropertyValue("country"), updatedVertex.getPropertyValue("country"));
    }

    @Test
    public void createJaneDoeVertex() throws Exception {
        logger.info("Executing createJaneDoeVertex test.");
        // create vertex
        Vertex originalVertex = new Vertex("person", new HashMap() {{
            put("name", "Jane Doe");
            put("country", "United States");
        }});
        Vertex addedVertex = TestSuite.graphClient.addVertex(originalVertex);
        assertNotNull(addedVertex);
        assertEquals(addedVertex.getPropertyValue("name"), originalVertex.getPropertyValue("name"));
        assertEquals(addedVertex.getPropertyValue("country"), originalVertex.getPropertyValue("country"));
    }
}
