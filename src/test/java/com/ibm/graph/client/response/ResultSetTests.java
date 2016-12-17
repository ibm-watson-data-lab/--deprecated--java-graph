package com.ibm.graph.client.response;

import com.ibm.graph.client.Edge;
import com.ibm.graph.client.Vertex;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONArray;

import static org.junit.Assert.*;

/**
 * Tests for class com.ibm.graph.ResultSet
 */
public class ResultSetTests {

    private static Logger logger =  LoggerFactory.getLogger(ResultSetTests.class);

   @Test
    public void testResultSetClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.response.ResultSet");
        ResultSet rs = null;
        JSONObject response = null,
                   result = null,
                   jo1 = null;
        JSONArray ja1 = null;
        Vertex v = null;
        Edge e = null;
        Iterator<String> si = null;

        try {           
            // response body is empty
            response = new JSONObject();
            rs = new ResultSet(response);
            assertNotNull(rs);
            assertNull(rs.toString(), rs.getRequestId());
            assertFalse(rs.toString(), rs.hasResults());
            assertEquals(rs.toString(), 0, rs.getResultCount());
            assertNotNull(rs.toString(), rs.getJSONObjectResultIterator());
            assertFalse(rs.toString(), rs.getJSONObjectResultIterator().hasNext());
            assertNotNull(rs.toString(), rs.getVertexResultIterator());
            assertFalse(rs.toString(), rs.getVertexResultIterator().hasNext());
            assertNotNull(rs.toString(), rs.getEdgeResultIterator());
            assertFalse(rs.toString(), rs.getEdgeResultIterator().hasNext());
            assertNotNull(rs.toString(), rs.getStringResultIterator());
            assertFalse(rs.toString(), rs.getEdgeResultIterator().hasNext());
            assertNotNull(rs.toString(), rs.getResponse());
            assertEquals(rs.toString(), new JSONObject().toString().length(), rs.getResponse().toString().length());           

            /*
                GET /_graphs success response: 
                {"graphs":["1...3","1","g","zzz"]}
            */
            response = new JSONObject(){{put("graphs", new String[]{"g1","g2","g","zzzz"});}};
            rs = new ResultSet(response);
            assertNotNull(rs);
            assertNull(rs.toString(), rs.getRequestId());
            assertEquals(rs.toString(), response.toString(), rs.getResponse().toString());
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(rs.toString(), 1, rs.getResultCount());
            assertNotNull(rs.toString(), rs.getJSONObjectResultIterator());
            result = rs.getResultAsJSONObject(0);
            assertNotNull(result);
            assertEquals(response.toString(), response.toString(), result.toString());

            // try to interpret te result as Vertex
            // the generated "vertex" should be just a meaningless shell containing no id, label or properties
            v = rs.getResultAsVertex(0); 
            assertNotNull(rs.toString(), v);
            assertNull(v.toString(), v.getId());                     
            assertNull(v.toString(), v.getLabel());                     
            assertNull(v.toString(), v.getProperties());                     
            assertNotNull(rs.toString(), rs.getVertexResultIterator());                

            // try to interpret te result as Edge
            assertNull(rs.toString(), rs.getResultAsEdge(0));                       
            assertNull(rs.toString(), rs.getEdgeResultIterator());                  

            // try to interpret te result as String
            assertEquals(rs.toString(), response.toString(), rs.getResultAsString(0));    
            si = rs.getStringResultIterator();
            assertNotNull(rs.toString(), si);
            assertEquals(rs.toString(), response.toString(), si.next());             

            // try to interpret te result as Boolean
            assertNull(rs.toString(), rs.getResultAsBoolean(0));                    

            /*
                Sample response POST /vertices 
                {
                    "requestId":"c0a06041-21c1-4973-87e9-f8856d7b718b",
                    "status":{
                                "message":"",
                                "code":200,
                                "attributes":{}
                             },
                    "result":{
                        "data":[
                                {
                                    "id":327684168,
                                    "label":"person",
                                    "type":"vertex",
                                    "properties":
                                        {
                                            "name": [
                                                        {
                                                            "id":"6rv6kp-5f3eq0-sl",
                                                            "value":"Tina"
                                                        }
                                                    ]
                                        }
                                }
                               ],
                        "meta":{}
                    }
                }
            */
            response = new JSONObject("{\"requestId\":\"c0a06041-21c1-4973-87e9-f8856d7b718b\",\"status\":{\"message\":\"\",\"code\":200,\"attributes\":{}},\"result\":{\"data\":[{\"id\":327684168,\"label\":\"person\",\"type\":\"vertex\",\"properties\":{\"name\":[{\"id\":\"6rv6kp-5f3eq0-sl\",\"value\":\"Tina\"}]}}],\"meta\":{}}}");
            
            rs = new ResultSet(response);
            assertNotNull(rs);
            assertEquals(rs.toString(), response.getString("requestId"), rs.getRequestId());
            assertEquals(rs.toString(), response.toString(), rs.getResponse().toString());
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(rs.toString(), 1, rs.getResultCount());

            // try to interpret the result as a JSONObject
            result = rs.getResultAsJSONObject(0);         
            assertNotNull(result);
            assertEquals(result.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), result.toString());
            assertNotNull(rs.toString(), rs.getJSONObjectResultIterator());                  
            assertEquals(rs.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), rs.getJSONObjectResultIterator().next().toString());                  

            // try to interpret te result as Vertex
            v = rs.getResultAsVertex(0); 
            assertNotNull(rs.toString(), v);
            assertEquals(v.toString(), "327684168", v.getId()); 
            assertEquals(v.toString(), "person", v.getLabel());           
            assertEquals(v.toString(), "Tina", v.getPropertyValue("name"));       
            assertNotNull(rs.toString(), rs.getVertexResultIterator()); 
            assertEquals(rs.toString(), v.toString(), rs.getVertexResultIterator().next().toString());

            // try to interpret te result as Edge
            assertNull(rs.toString(), rs.getResultAsEdge(0));                       
            assertNull(rs.toString(), rs.getEdgeResultIterator());                  

            // try to interpret te result as String
            assertEquals(result.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), rs.getResultAsString(0));
            si = rs.getStringResultIterator();
            assertNotNull(rs.toString(), si);
            assertEquals(rs.toString(), rs.getResultAsString(0), si.next());             

            // try to interpret te result as Boolean
            assertNull(rs.toString(), rs.getResultAsBoolean(0));                    

            /*
                Sample response: POST /edges
            
                {
                  "requestId": "10a21834-d4cd-4a8e-8a15-3f1610471b50",
                  "status": {
                    "message": "",
                    "code": 200,
                    "attributes": {}
                  },
                  "result": {
                    "data": [
                      {
                        "id": "7g92q1-5f3eq0-6mmt-4qphvk",
                        "label": "likes",
                        "type": "edge",
                        "inVLabel": "person",
                        "outVLabel": "person",
                        "inV": 286724288,
                        "outV": 327684168
                      }
                    ],
                    "meta": {}
                  }
                }            
            */

            response = new JSONObject("{\"requestId\":\"10a21834-d4cd-4a8e-8a15-3f1610471b50\",\"status\":{\"message\":\"\",\"code\":200,\"attributes\":{}},\"result\":{\"data\":[{\"id\":\"7g92q1-5f3eq0-6mmt-4qphvk\",\"label\":\"likes\",\"type\":\"edge\",\"inVLabel\":\"person\",\"outVLabel\":\"person\",\"inV\":286724288,\"outV\":327684168}],\"meta\":{}}}");
            
            rs = new ResultSet(response);
            assertNotNull(rs);
            assertEquals(rs.toString(), response.getString("requestId"), rs.getRequestId());
            assertEquals(rs.toString(), response.toString(), rs.getResponse().toString());
            assertTrue(rs.toString(), rs.hasResults());
            assertEquals(rs.toString(), 1, rs.getResultCount());

            // try to interpret the result as a JSONObject
            result = rs.getResultAsJSONObject(0);         
            assertNotNull(result);
            assertEquals(result.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), result.toString());
            assertNotNull(rs.toString(), rs.getJSONObjectResultIterator());                  
            assertEquals(rs.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), rs.getJSONObjectResultIterator().next().toString());                  

            // try to interpret the result as Edge
            e = rs.getResultAsEdge(0);
            assertNotNull(rs.toString(), e); 
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("id"), e.getId());                       
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("label"), e.getLabel());                       
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("inV"), e.getInV());                       
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("outV"), e.getOutV());                       
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("inVLabel"), e.getInVLabel());                       
            assertEquals(e.toString(), response.getJSONObject("result").getJSONArray("data").getJSONObject(0).getString("outVLabel"), e.getOutVLabel());                       

            assertNotNull(rs.toString(), rs.getEdgeResultIterator());                  
            assertEquals(rs.toString(), e.toString(), rs.getEdgeResultIterator().next().toString());

            // try to interpret the result as String
            assertEquals(result.toString(), response.getJSONObject("result").getJSONArray("data").getString(0), rs.getResultAsString(0));
            si = rs.getStringResultIterator();
            assertNotNull(rs.toString(), si);
            assertEquals(rs.toString(), rs.getResultAsString(0), si.next());             

            // try to interpret te result as Vertex
            v = rs.getResultAsVertex(0); 
            assertNotNull(rs.toString(), v);

            // try to interpret te result as Boolean
            assertNull(rs.toString(), rs.getResultAsBoolean(0)); 

        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }
    }

    @Test
    public void testResultSetClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.response.ResultSet");

        ResultSet rs = null;

        try {
            // parameter response cannot be null
            new ResultSet(null);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        // test GETters (outofboundsexception)

        try {
            // empty response
            rs = new ResultSet(new JSONObject());
            rs.getResultAsJSONObject(-1);
            assertFalse(true);
        }
        catch(IndexOutOfBoundsException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }

        try {
            // empty response
            rs = new ResultSet(new JSONObject());
            rs.getResultAsJSONObject(rs.getResultCount() + 1);
            assertFalse(true);
        }
        catch(IndexOutOfBoundsException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Unexpected exception was caught: ", ex);
            assertFalse(true);
        }




    }
}
