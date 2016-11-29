package com.ibm.graph.client;

import com.ibm.graph.client.schema.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class SchemaTests {

    private static Logger logger =  LoggerFactory.getLogger(SchemaTests.class);

    @Test
    public void createSchema() throws Exception {
        logger.info("Executing createSchema test.");
        // make sure schema does not exist
        Schema schema = TestSuite.graphClient.getSchema();
        boolean schemaExists = (schema != null && schema.getPropertyKeys() != null && schema.getPropertyKeys().length > 0);
        assertFalse(schemaExists);
        // create new schema
        schema = new Schema(
                new PropertyKey[]{
                        new PropertyKey("name", "String", "SINGLE"),
                        new PropertyKey("country", "String", "SINGLE"),
                        new PropertyKey("date", "Integer", "SINGLE")
                },
                new VertexLabel[]{
                        new VertexLabel("person")
                },
                new EdgeLabel[]{
                        new EdgeLabel("friends", "SIMPLE")
                },
                new VertexIndex[]{
                        new VertexIndex("vertexByName", new String[]{"name"}, true, true)
                },
                new EdgeIndex[]{
                        new EdgeIndex("edgeByDate", new String[]{"date"}, true, false)
                }
        );
        schema = TestSuite.graphClient.saveSchema(schema);
        assertNotNull(schema);
    }

}
