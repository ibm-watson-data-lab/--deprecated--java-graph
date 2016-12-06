package com.ibm.graph.client;

import com.ibm.graph.client.schema.EdgeLabel;
import com.ibm.graph.client.GraphException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class EdgeLabelTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeLabelTests.class);

    @Test
    public void testEdgeLabelClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.EdgeLabel");
        EdgeLabel el = null;
        try {
            // default multiplicity is assigned
            el = new EdgeLabel("name");
            assertNotNull(el);
            assertEquals("name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.SIMPLE, el.getMultiplicity());
  
            // default multiplicity is assigned
            el = new EdgeLabel("name", null);
            assertNotNull(el);
            assertEquals("name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.SIMPLE, el.getMultiplicity());

            el = new EdgeLabel("another_name", EdgeLabel.EdgeLabelMultiplicity.SIMPLE);
            assertNotNull(el);
            assertEquals("another_name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.SIMPLE, el.getMultiplicity());

            el = new EdgeLabel("another_name", EdgeLabel.EdgeLabelMultiplicity.MULTI);
            assertNotNull(el);
            assertEquals("another_name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.MULTI, el.getMultiplicity());

            el = new EdgeLabel("another_name", EdgeLabel.EdgeLabelMultiplicity.MANY2ONE);
            assertNotNull(el);
            assertEquals("another_name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.MANY2ONE, el.getMultiplicity());

            el = new EdgeLabel("another_name", EdgeLabel.EdgeLabelMultiplicity.ONE2MANY);
            assertNotNull(el);
            assertEquals("another_name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.ONE2MANY, el.getMultiplicity());

            el = new EdgeLabel("another_name", EdgeLabel.EdgeLabelMultiplicity.ONE2ONE);
            assertNotNull(el);
            assertEquals("another_name", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.ONE2ONE, el.getMultiplicity());

            // load from JSON
            // multiplicity defaults
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "very_edgy");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.SIMPLE, el.getMultiplicity());

            // multiplicity SIMPLE
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "very_edgy");put("multiplicity", "SiMpLE");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.SIMPLE, el.getMultiplicity());

            // multiplicity MULTI
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "very_edgy");put("multiplicity", "multi");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.MULTI, el.getMultiplicity());

            // multiplicity MANY2ONE
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "very_edgy");put("multiplicity", "MANY2ONe");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.MANY2ONE, el.getMultiplicity());

            // multiplicity ONE2MANY
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "very_edgy");put("multiplicity", "one2MANY");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.ONE2MANY, el.getMultiplicity());

            // multiplicity ONE2ONE
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("multiplicity", "one2one");put("name", "very_edgy");}});
            assertNotNull(el);
            assertEquals("very_edgy", el.getName());
            assertEquals(EdgeLabel.EdgeLabelMultiplicity.ONE2ONE, el.getMultiplicity());

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeLabel test failed.", ex);
            assertFalse(true);
        }
    }

    @Test
    public void testEdgeLabelErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.schema.EdgeLabel");

        EdgeLabel el = null;

        try {
            // name is required
            el = new EdgeLabel(null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("EdgeLabel(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            el = new EdgeLabel("");
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("EdgeLabel(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            el = new EdgeLabel("  ");
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("EdgeLabel(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            el = new EdgeLabel("  ", EdgeLabel.EdgeLabelMultiplicity.SIMPLE);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("EdgeLabel(...) test failed.", ex);
            assertFalse(true);
        }

        try {
            // multiplicity property is bogus
            el = EdgeLabel.fromJSONObject(new JSONObject(){{put("name", "meh");put("multiplicity", "ALL4NOTHIng");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("EdgeLabel.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 
    }
}
