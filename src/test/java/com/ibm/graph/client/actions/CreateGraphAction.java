package com.ibm.graph.client.actions;

import com.ibm.graph.client.IBMGraphClient;
import com.ibm.graph.client.TestAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by markwatson on 11/28/16.
 */
public class CreateGraphAction implements TestAction {

    private String graphId;
    private static Logger logger =  LoggerFactory.getLogger(CreateGraphAction.class);


    public CreateGraphAction(String graphId) {
        this.graphId = graphId;
    }

    @Override
    public void processTeardown(IBMGraphClient graphClient) {
        try {
            logger.debug(String.format("Deleting graph with ID %s",this.graphId));
            graphClient.deleteGraph(this.graphId);
        }
        catch(Exception ex) {
            logger.error("Error deleting graph", ex);
        }
    }
}
