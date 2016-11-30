package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Created by markwatson on 11/8/16.
 */
public class Vertex extends Entity {

    /**
     * Constructor
     * @param label vertex label
     * @throws Exception placeholder
     */
    public Vertex(String label) throws Exception {
        super(label);
    }

    /**
     * Constructor
     * @param label vertex label
     * @param properties vertex properties
     * @throws Exception placeholder
     */
    public Vertex(String label, HashMap properties) throws Exception {
        super(label, properties);
    }

    /**
     * Creates a vertex object from JSON, if json is not null
     * @param json JSON representation of a vertex
     * @return a vertex
     * @throws Exception placeholder
     */
    public static Vertex fromJSONObject(JSONObject json) throws Exception {
        if(json == null) 
            return null;
        Vertex vertex = new Vertex(
            json.getString("label")
        );
        vertex.setId(json.get("id"));
        vertex.setProperties(json.optJSONObject("properties"));
        return vertex;
    }
}
