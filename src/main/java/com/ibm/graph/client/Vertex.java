package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Describes a node stored in Graph.
 */
public class Vertex extends Entity {

    /**
     * Constructor
     * @throws Exception placeholder
     */
    public Vertex() throws Exception {
        super(null);
    }

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
    public Vertex(String label, HashMap<String, Object> properties) throws Exception {
        super(label, properties);
    }

    /**
     * Creates a vertex object from JSON, if json is not null
     * @param json JSON representation of a vertex
     * @return a vertex
     * @throws Exception if an error was encountered
     */
    public static Vertex fromJSONObject(JSONObject json) throws Exception {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        Vertex vertex = new Vertex(
            json.optString("label") // optional
        );
        vertex.setId(json.optString("id")); // optional
        vertex.setProperties(json.optJSONObject("properties")); // optional
        return vertex;
    }
}
