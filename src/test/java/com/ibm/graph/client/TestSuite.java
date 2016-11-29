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
        TestSuite.graphClient = new IBMGraphClient(
                envs.get("TEST_API_URL").toString(),
                envs.get("TEST_USERNAME").toString(),
                envs.get("TEST_PASSWORD").toString()
        );
    }

    @AfterClass
    public static void teardown() {
        try {
            logger.debug(String.format("Deleting graph with ID %s",graphId));
            TestSuite.graphClient.deleteGraph(graphId);
        }
        catch(Exception ex) {
            logger.error("Error deleting graph", ex);
        }
        TestSuite.graphClient = null;
    }

    public static void setGlobalGraphId(String graphId) {
        TestSuite.graphClient.setGraph(graphId);
        TestSuite.graphId = graphId;
    }
}
