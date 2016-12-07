package com.ibm.graph.client.response;


import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;

import static org.junit.Assert.*;

/**
 * Tests for class com.ibm.graph.response.GraphStatusInfo
 */
public class GraphStatusInfoTests {

    private static Logger logger =  LoggerFactory.getLogger(GraphStatusInfo.class);

   @Test
    public void testGraphStatusClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.response.GraphStatusInfo");
        GraphStatusInfo status = null;
        String GraphStatusInfoBody = null;
        try {
            //
            // status code without message
            //
            status = new GraphStatusInfo("OK", null);
            assertNotNull(status);
            assertEquals("OK", status.getStatusCode());
            assertNull(status.getStatusMessage());

            //  
            // status message without code
            //
            status = new GraphStatusInfo(null, "somemessage");
            assertNotNull(status);
            assertNull(status.getStatusCode());
            assertEquals("somemessage", status.getStatusMessage());

            //
            // status code with message
            //
            status = new GraphStatusInfo("OK", "somemessage");
            assertNotNull(status);
            assertEquals("OK", status.getStatusCode());
            assertEquals("somemessage", status.getStatusMessage());
            status = null;

            //
            // success response with JSON payload (body)
            // containing no status information
            //
            JSONObject j = new JSONObject();
            // {"graphs":["1...3","1","g","zzz"]}            
            j.put("graphs", new String[]{"g1", "g2"});

            status = GraphStatusInfo.fromJSONObject(j);
            assertNull(status);

            //
            // success response with JSON payload (body)
            // containing status information
            //
            j = new JSONObject();
            status = GraphStatusInfo.fromJSONObject(j);
            assertNull(status);

            //
            // success response with JSON payload (body)
            // containing status information
            //
            j = new JSONObject();
            // {"graphs":["1...3","1","g","zzz"]}            
            j.put("graphs", new String[]{"g1", "g2"});
            j.put("status", new HashMap<String,String>(){{put("code","OK");}});

            status = GraphStatusInfo.fromJSONObject(j);
            assertNotNull(status);
            assertEquals("OK", status.getStatusCode());
            assertNull(status.getStatusMessage());
            status = null;          

            j = new JSONObject();
            // {"graphs":["1...3","1","g","zzz"]}            
            j.put("graphs", new String[]{"g1", "g2"});
            j.put("status", new HashMap<String,String>(){{put("message","yessss");}});

            status = GraphStatusInfo.fromJSONObject(j);
            assertNotNull(status);
            assertEquals("yessss", status.getStatusMessage());
            assertNull(status.getStatusCode());
            status = null;          

            j = new JSONObject();
            // {"graphs":["1...3","1","g","zzz"]}            
            j.put("graphs", new String[]{"g1", "g2"});
            j.put("status", new HashMap<String,String>(){{put("code","OK");put("message","meh meh");}});

            status = GraphStatusInfo.fromJSONObject(j);
            assertNotNull(status);
            assertEquals("OK", status.getStatusCode());
            assertEquals("meh meh", status.getStatusMessage());
            status = null;          

        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }
    }

    @Test
    public void testGraphStatusInfoClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.response.GraphStatusInfo");
        GraphStatusInfo status = null;
        try {
            // at least one of the two parameters must be not null, not empty string
            new GraphStatusInfo(null, null);
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }

        try {
            // at least one of the two parameters must be not null, not empty string
            new GraphStatusInfo("", "    ");
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }

        try {
            // json must not be null
            status = GraphStatusInfo.fromJSONObject(null);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }
    }
}
