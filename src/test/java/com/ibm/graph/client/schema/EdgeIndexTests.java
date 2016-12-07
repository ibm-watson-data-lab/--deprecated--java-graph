package com.ibm.graph.client.schema;

import com.ibm.graph.client.schema.EdgeIndex;
import com.ibm.graph.client.exception.GraphException;

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
 * Test scope:
 * - class com.ibm.graph.client.schema.EdgeIndex
 */
public class EdgeIndexTests {

    private static Logger logger =  LoggerFactory.getLogger(EdgeIndexTests.class);

    @Test
    public void testEdgeIndexClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.EdgeIndex");
        EdgeIndex ei = null;
        try {
 
            boolean composite = true;
            boolean unique = false;
            String[] propertyKeys = new String[]{"name"};
            String indexName = "marriedTo";

            // constructor
            ei = new EdgeIndex(indexName, propertyKeys, composite, unique);
            assertNotNull(ei);
            assertEquals(indexName, ei.getName());
            assertEquals(composite, ei.isComposite());
            assertEquals(unique, ei.isUnique());
            assertEquals(propertyKeys.toString(), ei.getPropertyKeys().toString());

            // constructor            
            propertyKeys = new String[]{"name", "age"};
            indexName = "marriedToAndAge";
            unique = true;
            ei = new EdgeIndex(indexName, propertyKeys, composite, unique);
            assertNotNull(ei);
            assertEquals(indexName, ei.getName());
            assertEquals(composite, ei.isComposite());
            assertEquals(unique, ei.isUnique());
            assertEquals(propertyKeys.toString(), ei.getPropertyKeys().toString()); 

            // from JSON
            propertyKeys = new String[]{"marriedOn"};
            indexName = "bymarriedOn";
            ArrayList<String> pk = new ArrayList(){{add("weddingDate");}};
            JSONObject pkJSONobj = new JSONObject();
            pkJSONobj.put("name", indexName);
            pkJSONobj.put("composite", composite);
            pkJSONobj.put("unique", unique);
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertNotNull(ei);
            assertEquals(indexName, ei.getName());
            assertEquals(composite, ei.isComposite());
            assertEquals(unique, ei.isUnique());
            assertEquals((Arrays.toString((String[])pk.toArray(new String[0]))), Arrays.toString(ei.getPropertyKeys())); 

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }        
    }

    @Test
    public void testEdgeIndexErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.schema.EdgeIndex");
        EdgeIndex ei = null;
        try { 
            // name cannot be null
            assertNull(new EdgeIndex(null, null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // name cannot be an empty string
            assertNull(new EdgeIndex("", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // name cannot be an empty string
            assertNull(new EdgeIndex("     ", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot be null
            assertNull(new EdgeIndex("marriedTo", null, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot be an empty array
            assertNull(new EdgeIndex("marriedTo", new String[]{}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain null values
            assertNull(new EdgeIndex("marriedTo", new String[]{"name",null}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain empty strings
            assertNull(new EdgeIndex("marriedTo", new String[]{"name",""}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // propertyKeys cannot contain empty strings
            assertNull(new EdgeIndex("marriedTo", new String[]{"    ","name"}, true, true));
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        //  
        // method fromJSONObject 
        // 
        ArrayList<String> pk = null;
        JSONObject pkJSONobj = null;

        try { 
            // json cannot be null
            ei = EdgeIndex.fromJSONObject(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
        try { 
            // json cannot be empty
            ei = EdgeIndex.fromJSONObject(new JSONObject());
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys is an empty array
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains an empty string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pk.add("");
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains an empty string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pk.add("   ");
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property propertyKeys contains a null string
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pk.add(null);
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property composite is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property composite is not a boolean
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", "notBoolean");
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property unique is missing
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", true);
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }

        try { 
            // property unique is not a boolean
            pkJSONobj = new JSONObject();
            pkJSONobj.put("name", "marriedTo");
            pk = new ArrayList();
            pk.add("name");
            pkJSONobj.put("propertyKeys", pk);
            pkJSONobj.put("composite", "notBoolean");
            ei = EdgeIndex.fromJSONObject(pkJSONobj);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.EdgeIndex test failed.", ex);
            assertFalse(true);
        }
    }
}
