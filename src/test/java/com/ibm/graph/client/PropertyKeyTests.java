package com.ibm.graph.client;

import com.ibm.graph.client.schema.PropertyKey;
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
public class PropertyKeyTests {

    private static Logger logger =  LoggerFactory.getLogger(PropertyKeyTests.class);

    @Test
    public void testPropertyKeyClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.PropertyKey");
        PropertyKey pk = null;
        try {
            // default cardinality is assigned
            pk = new PropertyKey("name", PropertyKey.PropertyKeyDataType.String);
            assertNotNull(pk);
            assertEquals("name", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.String, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());

            // default cardinality is assigned            
            pk = new PropertyKey("married", PropertyKey.PropertyKeyDataType.Boolean);
            assertNotNull(pk);
            assertEquals("married", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.Boolean, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());

            // default cardinality is assigned
            pk = new PropertyKey("salary", PropertyKey.PropertyKeyDataType.Float);
            assertNotNull(pk);
            assertEquals("salary", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.Float, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());

            // default cardinality is assigned
            pk = new PropertyKey("kids", PropertyKey.PropertyKeyDataType.Integer);
            assertNotNull(pk);
            assertEquals("kids", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.Integer, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());

            // default cardinality is assigned
            pk = new PropertyKey("kids", PropertyKey.PropertyKeyDataType.Integer, null);
            assertNotNull(pk);
            assertEquals("kids", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.Integer, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());


            // PropertyKey with custom cardinality
            pk = new PropertyKey("property1", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE);
            assertNotNull(pk);
            assertEquals("property1", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.String, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());
            
            pk = new PropertyKey("property2", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.LIST);
            assertNotNull(pk);
            assertEquals("property2", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.String, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.LIST, pk.getCardinality());

            pk = new PropertyKey("property3", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SET);
            assertNotNull(pk);
            assertEquals("property3", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.String, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SET, pk.getCardinality());

            // data type and cardinality values are case insensitive
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dataType", "StrInG");put("name", "property4");put("cardinality", "siNgLE");}});
            assertNotNull(pk);
            assertEquals("property4", pk.getName());
            assertEquals(PropertyKey.PropertyKeyDataType.String, pk.getDataType());
            assertEquals(PropertyKey.PropertyKeyCardinality.SINGLE, pk.getCardinality());

            // pass

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.PropertyKey test failed.", ex);
            assertFalse(true);
        }
    }

    @Test
    public void testPropertyKeyErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.schema.PropertyKey");

        PropertyKey pk = null;

        try {
            // name is required
            pk = new PropertyKey(null, null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            pk = new PropertyKey(null, PropertyKey.PropertyKeyDataType.String);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            pk = new PropertyKey("", PropertyKey.PropertyKeyDataType.String);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // name is required
            pk = new PropertyKey("  ", PropertyKey.PropertyKeyDataType.String);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // data type is required
            pk = new PropertyKey("name", null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // data type is required
            pk = new PropertyKey("name", null, null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("Constructor test failed.", ex);
            assertFalse(true);
        } 

        try {
            // cannot be null
            pk = PropertyKey.fromJSONObject(null);
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(null) test failed.", ex);
            assertFalse(true);
        } 

       try {
            // cannot be empty
            pk = PropertyKey.fromJSONObject(new JSONObject());
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 

       try {
            // name and type property are missing
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dummy", "value");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // type property are missing
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("name", "value");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // type property is bogus
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dataType", "bogus");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // type property is bogus
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dataType", "bogus");put("name", "value");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        } 

        try {
            // type property is bogus
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dataType", "boGus");put("name", "value");put("cardinality", "SINGLE");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        }

        try {
            // cardinality property is bogus
            pk = PropertyKey.fromJSONObject(new JSONObject(){{put("dataType", "String");put("name", "value");put("cardinality", "bogus");}});
            assertFalse(true); // fail
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("PropertyKey.fromJSON(...) test failed.", ex);
            assertFalse(true);
        }
    }
}
