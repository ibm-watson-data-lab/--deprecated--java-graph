package com.ibm.graph.client;

import org.apache.wink.json4j.JSONException;

/**
 * Created by markwatson on 11/28/16.
 */
public class GraphException extends JSONException {

    public GraphException(String message) {
        super(message);
    }
}
