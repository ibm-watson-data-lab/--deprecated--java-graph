package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphClientException;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Describes the connection between two vertices.
 */
public class Edge extends Entity {

    private String outV;
    private String outVLabel;
    private String inV;
    private String inVLabel;

    /**
     * Constructor
     * @param label Edge label
     * @param outV id of the vertex from which the edge starts
     * @param inV id of the vertex in which the edge ends
     * @throws IllegalArgumentException label, outV or inV is null
     * @throws GraphClientException if an error occurred 
     */
    public Edge(String label, String outV, String inV) throws IllegalArgumentException, GraphClientException {
        this(label, outV, inV, null);
    }

    /**
     * Constructor
     * @param label Edge label
     * @param outV id of the vertex from which the edge starts
     * @param inV id of the vertex in which the edge ends
     * @param properties of this edge, if defined
     * @throws GraphClientException if an error occurred 
     * @throws IllegalArgumentException label, outV or inV is null
     */
    public Edge(String label, String outV, String inV, HashMap<String, Object> properties) throws IllegalArgumentException, GraphClientException {
        super(label, properties);
        if(label == null)
            throw new IllegalArgumentException("label parameter cannot be null.");
        if(outV == null)
            throw new IllegalArgumentException("outV parameter cannot be null.");
        if(inV == null)
            throw new IllegalArgumentException("inV parameter cannot be null.");

        this.type = Entity.EntityType.Edge;

        this.outV = outV;
        this.inV = inV;
        try {
            this.put("outV", this.outV);
            this.put("inV", this.inV);
        }
        catch(JSONException jsonex) {
            // ignore: Thrown if key is null, not a string, or the value could not be converted to JSON. 
        }
    }

    protected void setOutVLabel(String outVLabel) {
        this.outVLabel = outVLabel;
        if (this.outVLabel != null) {
            try {
                this.put("outVLabel", this.outVLabel);
            }
            catch(JSONException jsonex) {
               // ignore: Thrown if key is null, not a string, or the value could not be converted to JSON. 
            }
        }
        else 
            this.remove("outVLabel");
    }

    protected void setInVLabel(String inVLabel) {
        this.inVLabel = inVLabel;
        if (this.inVLabel != null) {
            try {
                this.put("inVLabel", this.inVLabel);
            }
            catch(JSONException jsonex) {
               // ignore: Thrown if key is null, not a string, or the value could not be converted to JSON. 
            }            
        }
        else 
            this.remove("inVLabel");
    }

    public String getOutV() {
        return outV;
    }

    public String getInV() {
        return inV;
    }

    public String getOutVLabel() {
        return outVLabel;
    }

    public String getInVLabel() {
        return inVLabel;
    }

    /**
     * Instantiates an Edge object based on json's properties.
     * Required properties: label, outV, inV
     * Optional properties: id, outVLabel, inVLabel, properties
     * @param json defines the properties of the Edge object
     * @return Edge an edge object
     * @throws IllegalArgumentException if json is null or doesn't define one or more required properties
     * @throws GraphClientException if an error occurred 
     */
    public static Edge fromJSONObject(JSONObject json) throws IllegalArgumentException, GraphClientException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        if(! json.has("label"))
            throw new IllegalArgumentException("Property label in parameter json is required.");
        if(! json.has("outV"))
            throw new IllegalArgumentException("Property outV in parameter json is required.");
        if(! json.has("inV"))
            throw new IllegalArgumentException("Property inV in parameter json is required.");

        try {
            Edge edge = new Edge(
                    json.getString("label"),
                    json.getString("outV"),
                    json.getString("inV")
            );
            edge.setId(json.optString("id"));
            edge.setProperties(json.optJSONObject("properties"));
            edge.setOutVLabel(json.optString("outVLabel"));
            edge.setInVLabel(json.optString("inVLabel"));
            return edge;
        }
        catch(JSONException jsonex) {
            throw new GraphClientException("Error deserializing Edge properties.", jsonex);
        }
    }
}
