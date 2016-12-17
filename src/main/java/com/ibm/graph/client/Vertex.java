package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphClientException;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Describes a node stored in Graph.
 */
public class Vertex extends Entity {

    /**
     * Constructor
     * @throws GraphClientException if an error occurred      
     */
    public Vertex() throws GraphClientException {
        super(null);
        this.type = Entity.EntityType.Vertex;
    }

    /**
     * Constructor
     * @param label vertex label
     * @throws GraphClientException if an error occurred 
     */
    public Vertex(String label) throws GraphClientException {
        super(label);
        this.type = Entity.EntityType.Vertex;
    }

    /**
     * Constructor
     * @param label vertex label
     * @param properties vertex properties
     * @throws GraphClientException if an error occurred 
     */
    public Vertex(String label, HashMap<String, Object> properties) throws GraphClientException {
        super(label, properties);
        this.type = Entity.EntityType.Vertex;
    }

    /**
     * Creates a vertex object from JSON, if json is not null
     * @param json JSON representation of a vertex
     * @return a vertex
     * @throws IllegalArgumentException json is null
     * @throws GraphClientException if an error occurred during the deserialization process
     */
    public static Vertex fromJSONObject(JSONObject json) throws IllegalArgumentException, GraphClientException {
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
