package com.ibm.graph.client;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/8/16.
 */
public class Element extends JSONObject {

    protected static Element fromJSONObject(JSONObject json) throws Exception {
        if (json.isNull("objects")) {
            return Entity.fromJSONObject(json);
        }
        else {
            return Path.fromJSONObject(json);
        }
    }
}
