package com.ibm.graph.client.exception;


import com.ibm.graph.client.response.*;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Test scope:
 * - class com.ibm.graph.client.exception.GraphException
 */
public class GraphExceptionTests {

    private static Logger logger =  LoggerFactory.getLogger(GraphExceptionTests.class);

    @Test
    public void testGraphExceptionClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.exception.GraphException");
        
        try {
 
            String message = null;
            HTTPStatusInfo httpStatus = new HTTPStatusInfo(404, "Not Found");
            String httpResponseBody = "Nope. Not there";
            String gc = "NotFoundError";
            String gm = "NotFoundError message";
            GraphStatusInfo gs = new GraphStatusInfo(gc, gm);
            GraphStatusInfo graphStatus = new GraphStatusInfo(gc, gm);

            GraphException gex = new GraphException(httpStatus, httpResponseBody, graphStatus);
            assertEquals(message, gex.getMessage());
            assertEquals(httpStatus.toString(), gex.getHTTPStatus().toString());
            assertEquals(graphStatus.toString(), gex.getGraphStatus().toString());
            assertTrue(gex.isGraphEngineException());
            assertEquals(httpResponseBody, gex.getResponseBody());

            gex = new GraphException(message, httpStatus, httpResponseBody, graphStatus);
            assertEquals(message, gex.getMessage());
            assertEquals(httpStatus.toString(), gex.getHTTPStatus().toString());
            assertEquals(graphStatus.toString(), gex.getGraphStatus().toString());
            assertTrue(gex.isGraphEngineException());
            assertEquals(httpResponseBody, gex.getResponseBody());

            message = null;
            gex = new GraphException(message, httpStatus, httpResponseBody, null);
            assertEquals(message, gex.getMessage());
            assertEquals(httpStatus.toString(), gex.getHTTPStatus().toString());
            assertNull(gex.getGraphStatus());
            assertFalse(gex.isGraphEngineException());
            assertEquals(httpResponseBody, gex.getResponseBody());

            message = "hello world";
            gex = new GraphException(message, httpStatus, httpResponseBody, null);
            assertEquals(message, gex.getMessage());
            assertEquals(httpStatus.toString(), gex.getHTTPStatus().toString());
            assertNull(gex.getGraphStatus());
            assertFalse(gex.isGraphEngineException());
            assertEquals(httpResponseBody, gex.getResponseBody());

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.exception.GraphException test failed.", ex);
            assertFalse(true);
        }        
    }

    @Test
    public void testGraphExceptionErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.exception.GraphException");
        try {
            // 2nd parm is required
            new GraphException(null, null, null);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.exception.GraphException error handling test failed.", ex);
            assertFalse(true);
        }
        try {
            // 2nd parm is required
            new GraphException(null, null, null, null);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.exception.GraphException error handling test failed.", ex);
            assertFalse(true);
        }
    }
}
