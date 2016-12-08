package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

/**
 * Base class for elements stored or retrieved from a Graph.
 */
public class Element extends JSONObject {

    public static Element fromJSONObject(JSONObject json) throws Exception {
        if (json.isNull("objects")) {
            return Entity.fromJSONObject(json);
        }
        else {
            return Path.fromJSONObject(json);
        }
    }
}
