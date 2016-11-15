package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EdgeLabel extends EntityLabel {

    public EdgeLabel(String name) throws Exception {
        super(name);
    }

    public static EdgeLabel fromJSONObject(JSONObject json) throws Exception {
        return new EdgeLabel(
                json.getString("name")
        );
    }
}
