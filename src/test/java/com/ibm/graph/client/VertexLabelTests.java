package com.ibm.graph.client;

import com.ibm.graph.client.schema.VertexLabel;
import com.ibm.graph.GraphException;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by markwatson on 11/28/16.
 */
public class VertexLabelTests {

    private static Logger logger =  LoggerFactory.getLogger(VertexLabelTests.class);

    @Test
    public void testVertexLabelClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.VertexLabel");
        VertexLabel vl = null;
        try {
            // assign default
            vl = new VertexLabel();
            assertNotNull(vl);
            assertEquals(VertexLabel.DEFAULT_NAME, vl.getName());

            // assign default
            vl = new VertexLabel(null);
            assertNotNull(vl);
            assertEquals(VertexLabel.DEFAULT_NAME, vl.getName());

            // assign default
            vl = new VertexLabel("");
            assertNotNull(vl);
            assertEquals(VertexLabel.DEFAULT_NAME, vl.getName());

            // assign default
            vl = new VertexLabel("    ");
            assertNotNull(vl);
            assertEquals(VertexLabel.DEFAULT_NAME, vl.getName());

            // assign custom
            vl = new VertexLabel("some_label_name");
            assertNotNull(vl);
            assertEquals("some_label_name", vl.getName());
  
            // from JSON - custom
            vl = VertexLabel.fromJSONObject(new JSONObject(){{put("name","some_other_label_name");}});
            assertNotNull(vl);
            assertEquals("some_other_label_name", vl.getName());

            // from JSON - default
            vl = VertexLabel.fromJSONObject(new JSONObject(){{}});
            assertNotNull(vl);
            assertEquals(VertexLabel.DEFAULT_NAME, vl.getName());

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexLabel test failed.", ex);
            assertFalse(true);
        }
    }

    @Ignore("No tests defined")
    @Test
    public void testVertexLabelErrorHandling() throws Exception {
        // no tests yet
    }
}
