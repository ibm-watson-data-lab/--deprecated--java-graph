package com.ibm.graph.client.schema;

import com.ibm.graph.client.schema.EdgeIndex;
import com.ibm.graph.client.schema.EdgeLabel;
import com.ibm.graph.client.schema.EntityIndex;
import com.ibm.graph.client.schema.EntityLabel;
import com.ibm.graph.client.schema.PropertyKey;
import com.ibm.graph.client.schema.Schema;
import com.ibm.graph.client.schema.VertexLabel;
import com.ibm.graph.client.schema.VertexIndex;
import com.ibm.graph.client.TestSuite;
import com.ibm.graph.client.Vertex;

import com.ibm.graph.client.exception.GraphException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.wink.json4j.JSONObject;
import static org.hamcrest.CoreMatchers.hasItem;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests class com.ibm.graph.client.schema.Schema.
 * No IBM Graph connection is required.
 */
public class SchemaClassTests {

    private static Logger logger =  LoggerFactory.getLogger(SchemaClassTests.class);

    @Test
    public void testSchemaClass() throws Exception {
        logger.info("Executing tests for class com.ibm.graph.client.schema.Schema");
        try {
            Schema s = new Schema();
            assertNotNull(s);
            assertFalse(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 0);
            assertFalse(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 0);
            assertFalse(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 0);
            assertFalse(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 0);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);

            s = new Schema(new PropertyKey[]{
                                    new PropertyKey("name", PropertyKey.PropertyKeyDataType.String, PropertyKey.PropertyKeyCardinality.SINGLE)
                                },
                                new VertexLabel[]{
                                    new VertexLabel("person")
                                },
                                new EdgeLabel[]{
                                    new EdgeLabel("friend")
                                },
                                new VertexIndex[]{
                                    new VertexIndex("vertexByName", new String[]{"name"}, true, true)
                                },
                                new EdgeIndex[]{});
            assertNotNull(s);
            assertTrue(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 1);
            assertTrue(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 1);
            assertTrue(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 1);
            assertTrue(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 1);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);

            s = null;
            // test method fromJSONObject
            s = Schema.fromJSONObject(new JSONObject());
            assertNotNull(s);
            assertFalse(s.definesPropertyKeys());
            assertTrue(s.getPropertyKeys().length == 0);
            assertFalse(s.definesVertexLabels());
            assertTrue(s.getVertexLabels().length == 0);
            assertFalse(s.definesEdgeLabels());
            assertTrue(s.getEdgeLabels().length == 0);
            assertFalse(s.definesVertexIndexes());
            assertTrue(s.getVertexIndexes().length == 0);
            assertFalse(s.definesEdgeIndexes());
            assertTrue(s.getEdgeIndexes().length == 0);            

        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.Schema test: unexpected exception.", ex);
            assertFalse("com.ibm.graph.client.schema.Schema test: unexpected exception: " + ex.getMessage(), true);   
        }
    }

   @Test
    public void testSchemaClassErrorHandling() throws Exception {
        logger.info("Executing error handling tests for class com.ibm.graph.client.schema.Schema");
        
        try {
            // null parameter
            Schema.fromJSONObject(null);
            assertFalse(true);
        }
        catch(IllegalArgumentException iaex) {
            // pass
        }
        catch(Exception ex) {
            // fail
            logger.error("com.ibm.graph.client.schema.Schema error handling test: unexpected exception.", ex);
            assertFalse("com.ibm.graph.client.schema.Schema error handling test: unexpected exception: " + ex.getMessage(), true);   
        }
    }

}
