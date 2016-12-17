package com.ibm.graph.client;

import com.ibm.graph.client.exception.*;
import com.ibm.graph.client.response.*;
import com.ibm.graph.client.schema.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by markwatson on 11/28/16.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GraphExceptionTests.class,      // tests class com.ibm.graph.client.exception.GraphException     
        HTTPStatusInfoTests.class,      // tests class com.ibm.graph.client.response.HTTPStatusInfo
        GraphStatusInfoTests.class,     // tests class com.ibm.graph.client.response.GraphStatusInfo
        GraphResponseTests.class,       // tests class com.ibm.graph.client.response.GraphResponse
        ResultSetTests.class,           // tests class com.ibm.graph.client.response.ResultSet
        PropertyKeyTests.class,         // tests class com.ibm.graph.client.schema.PropertyKey
        EntityLabelTests.class,         // tests class com.ibm.graph.client.schema.EntityLabel
        VertexLabelTests.class,         // tests class com.ibm.graph.client.schema.VertexLabel        
        EdgeLabelTests.class,           // tests class com.ibm.graph.client.schema.EdgeLabel
        EntityIndexTests.class,         // tests class com.ibm.graph.client.schema.EntityIndex
        VertexIndexTests.class,         // tests class com.ibm.graph.client.schema.VertexIndex
        EdgeIndexTests.class,           // tests class com.ibm.graph.client.schema.EdgeIndex
        SchemaClassTests.class,         // tests class com.ibm.graph.client.schema.Schema
        PathClassTests.class,           // tests class com.ibm.graph.client.Path
        EdgeClassTests.class,           // tests class com.ibm.graph.client.Edge
        VertexClassTests.class,         // tests class com.ibm.graph.client.Vertex
        GraphTests.class,               // tests graph specific methods in com.ibm.graph.client.IBMGraphClient
        SchemaTests.class,              // tests schema specific methods in com.ibm.graph.client.IBMGraphClient
        VertexTests.class,              // tests vertex specific methods in com.ibm.graph.client.IBMGraphClient
        EdgeTests.class,                // tests edge specific methods in com.ibm.graph.client.IBMGraphClient
        GremlinTests.class,             // tests gremlin specific methods in com.ibm.graph.client.IBMGraphClient
        GremlinPathTests.class,         // tests gremlin specific methods in com.ibm.graph.client.IBMGraphClient (path)
        GraphSONTests.class,            // tests graphSON load methods in com.ibm.graph.client.IBMGraphClient
        GraphMLTests.class,             // tests graphML load methods in com.ibm.graph.client.IBMGraphClient
        GraphMLTests2.class             // tests graphML load methods in com.ibm.graph.client.IBMGraphClient
})
public class TestSuite {

    public static IBMGraphClient graphClient;
    private static String graphId;
    private static Logger logger =  LoggerFactory.getLogger(TestSuite.class);

    @BeforeClass
    public static void setup() {
        Map envs = System.getenv();

        if((envs.get("TEST_API_URL") == null) || (envs.get("TEST_USERNAME") == null) || (envs.get("TEST_PASSWORD") == null)) {
            logger.error("JUnit test aborted. Environment variables TEST_API_URL, TEST_USERNAME and TEST_PASSWORD must be set");
            System.exit(1);
        }

        logger.info("******************************************************************************************************************");
        if(logger.isDebugEnabled()) 
            logger.info("DEBUG is enabled. To disable it, edit /src/test/resources/simplelogger.properties and follow the instructions.");
        else 
            logger.info("DEBUG is disabled. To enable it, edit /src/test/resources/simplelogger.properties and follow the instructions.");
        logger.info("******************************************************************************************************************");

        try {
           TestSuite.graphClient = new IBMGraphClient(
                                                        envs.get("TEST_API_URL").toString(),
                                                        envs.get("TEST_USERNAME").toString(),
                                                        envs.get("TEST_PASSWORD").toString());
           logger.info("Created graphClient instance.");
           graphId = TestSuite.graphClient.getGraphId();
           logger.debug("Current graph id: " + graphId);
        }
        catch(Exception ex) {
            logger.error("Error creating IBMGraphClient: ", ex);   
            TestSuite.graphClient = null;
            System.exit(1);
        }
    }

    @AfterClass
    public static void teardown() {
        if(TestSuite.graphClient != null) {
           try {
                logger.info("Switching to graph " + graphId);
                TestSuite.graphClient.setGraph(graphId);
            }
            catch(Exception ex) {
                logger.error("Error switching to graph " + graphId + ": ", ex);
            }
            TestSuite.graphClient = null;
        }
    }

    public static void setGlobalGraphId(String graphId) {
        if(TestSuite.graphClient != null) {
           try {
            TestSuite.graphClient.setGraph(graphId);
            TestSuite.graphId = graphId;
           }
           catch(Exception ex) {
                logger.error("Error switching to graph " + graphId + ": " , ex);
           }
        }

    }
}
