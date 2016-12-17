package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test scope:
 * - class com.ibm.graph.client.Path
 */
public class PathClassTests {

    private static Logger logger =  LoggerFactory.getLogger(PathClassTests.class);

    @Test
    public void testPathClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.Path");
        int i, j;
        Path p = null;
        JSONObject jo = null;
        ArrayList<Object> ll,ol = null;
        HashMap<String, Object> ole, pl = null;
        Entity.EntityType[] ets = null;
        try {
 
            jo = new JSONObject();

            // labels
            ll = new ArrayList<Object>();
            ll.add(new ArrayList<Object>());
            ll.add(new ArrayList<Object>());
            jo.put("labels", ll);

            // objects
            ol = new ArrayList<Object>();

            // add a vertex to Path
            ole = new HashMap<String,Object>();
            ole.put("id", 12504);
            ole.put("label", "attendee");
            ole.put("type", "vertex");
            pl = new HashMap<String,Object>();
            pl.put("gender", new HashMap<String,Object>(){{ put("id","id1"); put("value", "female");}});
            pl.put("name", new HashMap<String,Object>(){{ put("id","id2"); put("value", "Tina");}});
            pl.put("age", new HashMap<String,Object>(){{ put("id","id3"); put("value", 24);}});
            ole.put("properties", pl);
            ol.add(ole);

            // add an edge to Path
            ole = new HashMap<String,Object>();
            ole.put("id", "edge1");
            ole.put("label", "bought");
            ole.put("type", "edge");
            ole.put("inVLabel", "gift");
            ole.put("outVLabel", "person");
            ole.put("outV", 12504);
            ole.put("inV", 12505);
            pl = new HashMap<String,Object>();
            pl.put("properties", new HashMap<String,Object>(){{ put("date","01/01/16"); put("time", "10:10");}});
            ole.put("properties", pl);
            ol.add(ole);

            jo.put("objects", ol);

            // create an instance
            p = Path.fromJSONObject(jo);
            assertNotNull(p);
            logger.debug("Path result:" + p.toString());
            assertEquals(2, p.getLabels().length);          // two label entries in data structure
            for(j = 0; j < p.getLabels().length; j++) {
                assertEquals(0, p.getLabels()[j].length);   // no labels defined in traversal
            }
            assertEquals(2, p.getObjects().length);         // two elements (vertex, edge) in the path
            assertEquals(2, p.getSize());                   // two elements (vertex, edge) in the path
            assertEquals(((Entity)p.getObjects()[0]).toString(), Entity.EntityType.Vertex, ((Entity)p.getObjects()[0]).getType());    // Vertex
            assertEquals(((Entity)p.getObjects()[1]).toString(), Entity.EntityType.Edge, ((Entity)p.getObjects()[1]).getType());      // Edge
            ets = new Entity.EntityType[p.getObjects().length];
            ets[0] = Entity.EntityType.Vertex;
            ets[1] = Entity.EntityType.Edge;
            assertEquals(Arrays.toString(p.getObjectTypes()), Arrays.toString(ets), Arrays.toString(p.getObjectTypes()));

            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.Path test failed.", ex);
            assertFalse(true);
        }        
    }

    @Test
    public void testPathClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.Path");
        
        try { 
            //  json cannot be null
            Path.fromJSONObject(null);
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.Path error handling test failed.", ex);
            assertFalse(true);
        }

        try { 
            //  json must contain property "Labels"
            Path.fromJSONObject(new JSONObject());
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.Path error handling test failed.", ex);
            assertFalse(true);
        }

        try { 
            //  json must contain property "objects"
            Path.fromJSONObject(new JSONObject(){{put("labels", "dummy");}});
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.Path error handling test failed.", ex);
            assertFalse(true);
        }
    }
}
