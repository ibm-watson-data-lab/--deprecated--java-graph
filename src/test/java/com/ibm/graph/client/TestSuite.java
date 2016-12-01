package com.ibm.graph.client;

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
        GraphTests.class,
        SchemaTests.class,
        VertexTests.class,
        EdgeTests.class
})
public class TestSuite {

    public static IBMGraphClient graphClient;
    private static String graphId;
    private static Logger logger =  LoggerFactory.getLogger(TestSuite.class);

    @BeforeClass
    public static void setup() {
        Map envs = System.getenv();

        if((envs.get("TEST_API_URL") == null) || (envs.get("TEST_USERNAME") == null) || (envs.get("TEST_PASSWORD") == null)) {
            System.out.println("JUnit test aborted. Environment variables TEST_API_URL, TEST_USERNAME and TEST_PASSWORD must be set");
            System.exit(1);
        }

        try {
           TestSuite.graphClient = new IBMGraphClient(
                                                        envs.get("TEST_API_URL").toString(),
                                                        envs.get("TEST_USERNAME").toString(),
                                                        envs.get("TEST_PASSWORD").toString());
        }
        catch(Exception ex) {
            logger.error("Error creating IBMGraphClient", ex);   
            TestSuite.graphClient = null;
        }
    }

    @AfterClass
    public static void teardown() {
        if(TestSuite.graphClient != null) {
           try {
                logger.debug(String.format("Deleting graph with ID %s",graphId));
                TestSuite.graphClient.deleteGraph(graphId);
            }
            catch(Exception ex) {
                logger.error("Error deleting graph " + graphId + ": ", ex);
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
