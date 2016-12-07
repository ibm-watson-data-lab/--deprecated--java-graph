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
 * Tests for class com.ibm.graph.response.GraphResponse
 */
public class GraphResponseTests {

    private static Logger logger =  LoggerFactory.getLogger(GraphResponse.class);

   @Test
    public void testResultSetClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.response.GraphResponse");
        HTTPStatusInfo httpStatus = null;
        GraphResponse response = null;
        String graphResponseBody = null;
        try {
            //
            // success response with no payload (body)
            //
            httpStatus = new HTTPStatusInfo(200, "OK");
            response = new GraphResponse(httpStatus, null);
            assertEquals("Unexpected HTTP status: " + response.getHTTPStatus().toString(),
                         httpStatus.toString(), response.getHTTPStatus().toString());
            assertFalse(response.hasResultSet());
            assertNull(response.getResultSet());
            assertNull(response.getGraphStatus());
            assertEquals("", response.getResponseBody());

            //
            // success response with JSON payload (body)
            //
            httpStatus = new HTTPStatusInfo(200, "OK");
            // {"graphs":["1...3","1","g","zzz"]}
            JSONObject j = new JSONObject();
            j.put("graphs", new String[]{"g1", "g2"});

            response = new GraphResponse(httpStatus, j.toString());
            assertEquals("Unexpected HTTP status: " + response.getHTTPStatus().toString(),
                         httpStatus.toString(), response.getHTTPStatus().toString());
            assertTrue(response.hasResultSet());
            assertNotNull(response.getResultSet());
            assertNull(response.getGraphStatus());
            assertEquals(j.toString(), response.getResponseBody());


            //
            // success response with JSON payload (body)
            // defining graph status
            //
            httpStatus = new HTTPStatusInfo(200, "OK");
            // {"graphs":["1...3","1","g","zzz"]}
            j = new JSONObject();
            j.put("graphs", new String[]{"g1", "g2"});
            j.put("status", new HashMap<String,String>(){{put("code","OK");put("message", "hey bear");}});

            response = new GraphResponse(httpStatus, j.toString());
            assertEquals("Unexpected HTTP status: " + response.getHTTPStatus().toString(),
                         httpStatus.toString(), response.getHTTPStatus().toString());
            assertTrue(response.hasResultSet());
            assertNotNull(response.getResultSet());
            assertNotNull(response.getGraphStatus());
            assertEquals("OK", response.getGraphStatus().getStatusCode());
            assertEquals("hey bear", response.getGraphStatus().getStatusMessage());
            assertEquals(j.toString(), response.getResponseBody());

        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }
    }

    @Test
    public void testGraphResponseClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.response.GraphResponse");
        try {
            // HTTP status information cannot be null
            new GraphResponse(null, null);
            // fail
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            assertFalse(ex.getMessage(), true);
        }

        HTTPStatusInfo httpStatus = null;
        try {
            // 
            httpStatus = new HTTPStatusInfo(200, "OK");
            new GraphResponse(httpStatus, null);
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
