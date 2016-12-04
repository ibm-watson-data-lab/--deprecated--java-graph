package com.ibm.graph.client;

import com.ibm.graph.client.schema.VertexIndex;
import com.ibm.graph.GraphException;

import java.util.ArrayList;
import java.util.Arrays;

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
public class VertexIndexTests {

    private static Logger logger =  LoggerFactory.getLogger(VertexIndexTests.class);

    @Test
    public void testVertexIndexClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.VertexIndex");
        VertexIndex vi = null;
        try {
 
            boolean composite = true;
            boolean unique = false;
            String[] propertyKeys = new String[]{"name"};
            String indexName = "byName";

            // constructor
            vi = new VertexIndex(indexName, propertyKeys, composite, unique);
            assertNotNull(vi);
            assertEquals(indexName, vi.getName());
            assertEquals(composite, vi.isComposite());
            assertEquals(unique, vi.isUnique());
            assertEquals(propertyKeys.toString(), vi.getPropertyKeys().toString());

            // constructor            
            propertyKeys = new String[]{"name", "age"};
            indexName = "byNameAndAge";
            unique = true;
            vi = new VertexIndex(indexName, propertyKeys, composite, unique);
            assertNotNull(vi);
            assertEquals(indexName, vi.getName());
            assertEquals(composite, vi.isComposite());
            assertEquals(unique, vi.isUnique());
            assertEquals(propertyKeys.toString(), vi.getPropertyKeys().toString()); 

            // from JSON
            propertyKeys = new String[]{"lastName"};
            indexName = "byLastName";
            ArrayList<String> pk = new ArrayList(){{add("lastName");}};
            JSONObject pkJSONobj = new JSONObject();
            pkJSONobj.put("name", indexName);
            pkJSONobj.put("composite", composite);
            pkJSONobj.put("unique", unique);
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertNotNull(vi);
            assertEquals(indexName, vi.getName());
            assertEquals(composite, vi.isComposite());
            assertEquals(unique, vi.isUnique());
            assertEquals((Arrays.toString((String[])pk.toArray(new String[0]))), Arrays.toString(vi.getPropertyKeys())); 

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }        
    }

    @Test
    public void testVertexIndexErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.schema.VertexIndex");
        VertexIndex vi = null;
        try { 
            // name cannot be null
            assertNull(new VertexIndex(null, null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // name cannot be an empty string
            assertNull(new VertexIndex("", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // name cannot be an empty string
            assertNull(new VertexIndex("     ", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot be null
            assertNull(new VertexIndex("byName", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot be an empty array
            assertNull(new VertexIndex("byName", new String[]{}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain null values
            assertNull(new VertexIndex("byName", new String[]{"name",null}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain empty strings
            assertNull(new VertexIndex("byName", new String[]{"name",""}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain empty strings
            assertNull(new VertexIndex("byName", new String[]{"    ","name"}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        //  
        // method fromJSONObject 
        // 
        ArrayList<String> pk = null;
        JSONObject pkJSONobj = null;

        try { 
            // json cannot be null
            vi = VertexIndex.fromJSONObject(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // json cannot be empty
            vi = VertexIndex.fromJSONObject(new JSONObject());
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys is an empty array
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains an empty string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pk.add("");
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains an empty string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pk.add("   ");
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains a null string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pk.add(null);
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property composite is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property composite is not a boolean
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", "notBoolean");
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property unique is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", true);
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property unique is not a boolean
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "byName");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", "notBoolean");
            vi = VertexIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.VertexIndex test failed.", ex);
            assertFalse(true);
        }
    }
}
