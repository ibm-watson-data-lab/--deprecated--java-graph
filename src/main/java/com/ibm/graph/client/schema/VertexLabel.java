package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class VertexLabel extends EntityLabel {

    public VertexLabel(String name) throws Exception {
        super(name);
    }

    public static VertexLabel fromJSONObject(JSONObject json) throws Exception {
        return new VertexLabel(
                json.getString("name")
        );
    }
}
