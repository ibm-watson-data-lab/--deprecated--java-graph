package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Created by markwatson on 11/8/16.
 */
public class Vertex extends Entity {

    public Vertex(String label) throws Exception {
        super(label);
    }

    public Vertex(String label, HashMap properties) throws Exception {
        super(label, properties);
    }

    protected static Vertex fromJSONObject(JSONObject json) throws Exception {
        Vertex vertex = new Vertex(
            json.getString("label"),
            json.optJSONObject("properties")
        );
        vertex.setId(json.get("id"));
        return vertex;
    }
}
