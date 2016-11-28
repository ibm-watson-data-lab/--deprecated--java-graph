package com.ibm.graph.client;

import com.ibm.graph.client.actions.CreateGraphAction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.ArrayList;
import java.util.List;
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
    private static List<TestAction> actions;

    @BeforeClass
    public static void setup() {
        Map envs = System.getenv();
        graphClient = new IBMGraphClient(
                envs.get("TEST_API_URL").toString(),
                envs.get("TEST_USERNAME").toString(),
                envs.get("TEST_PASSWORD").toString()
        );
        actions = new ArrayList<>();
    }

    @AfterClass
    public static void teardown() {
        if (actions.size() > 0) {
            for(int i=actions.size()-1; i>=0; i--) {
                actions.get(i).processTeardown(graphClient);
            }
        }
        actions.clear();
        graphClient = null;
    }

    public static void setGlobalGraphId(String graphId) {
        graphClient.setGraph(graphId);
        actions.add(new CreateGraphAction(graphId));
    }

//    public static void addAction(TestAction action) {
//        actions.add(action);
//    }
}
