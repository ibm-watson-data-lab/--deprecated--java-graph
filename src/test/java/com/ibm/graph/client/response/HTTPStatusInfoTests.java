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
 * Tests for class com.ibm.graph.response.HTTPStatusInfo
 */
public class HTTPStatusInfoTests {

    private static Logger logger =  LoggerFactory.getLogger(HTTPStatusInfo.class);

   @Test
    public void testHTTPStatusClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.response.HTTPStatusInfo");
        HTTPStatusInfo status = null;
        String HTTPStatusInfoBody = null;
        try {
            //
            // status code without message
            //
            int code = 100;
            String text = null;

            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals("", status.getReasonPhrase());
            assertTrue(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());            

            code = 199;
            text = "";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals("", status.getReasonPhrase());
            assertTrue(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 200;
            text = "OK";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertTrue(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 299;
            text = "   ";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals("", status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertTrue(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 300;
            text = "Multiple Choices";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertTrue(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 399;
            text = "unassigned";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertTrue(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 400;
            text = "Bad Request";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertTrue(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 499;
            text = "unassigned";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertTrue(status.isClientErrorStatus());
            assertFalse(status.isServerErrorStatus());

            code = 500;
            text = "Internal Server Error";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertTrue(status.isServerErrorStatus());

            code = 500;
            text = "unassigned";
            status = new HTTPStatusInfo(code, text);
            assertNotNull(status);
            assertEquals(code, status.getStatusCode());
            assertEquals(text, status.getReasonPhrase());
            assertFalse(status.isInformationalStatus());
            assertFalse(status.isSuccessStatus());
            assertFalse(status.isRedirectStatus());
            assertFalse(status.isClientErrorStatus());
            assertTrue(status.isServerErrorStatus());

        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse("Unexpected exception was caught: " + ex.getMessage(), true);
        }
    }

    @Test
    public void testHTTPStatusInfoClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.response.HTTPStatusInfo");
        HTTPStatusInfo status = null;
        try {
            // code must be >= 100
            new HTTPStatusInfo(99, null);
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
            // code must be < 600
            new HTTPStatusInfo(600, null);
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


    }
}
