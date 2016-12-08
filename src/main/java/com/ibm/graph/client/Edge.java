package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Describes the connection between two vertices.
 */
public class Edge extends Entity {

    private Object outV;
    private String outVLabel;
    private Object inV;
    private String inVLabel;

    /**
     * Constructor
     * @param label Edge label
     * @param outV id of the vertex from which the edge starts
     * @param inV id of the vertex in which the edge ends
     * @throws Exception if any of the parameters is missing
     */
    public Edge(String label, Object outV, Object inV) throws Exception {
        this(label, outV, inV, null);
    }

    /**
     * Constructor
     * @param label Edge label
     * @param outV id of the vertex from which the edge starts
     * @param inV id of the vertex in which the edge ends
     * @param properties of this edge, if defined
     * @throws Exception if the label, outV or inV parameter is missing
     */
    public Edge(String label, Object outV, Object inV, HashMap<String, Object> properties) throws Exception {
        super(label, properties);
        if(label == null)
            throw new IllegalArgumentException("label parameter cannot be null.");
        if(outV == null)
            throw new IllegalArgumentException("outV parameter cannot be null.");
        if(inV == null)
            throw new IllegalArgumentException("inV parameter cannot be null.");

        this.outV = outV;
        this.inV = inV;
        this.put("outV", this.outV);
        this.put("inV", this.inV);
    }

    protected void setOutVLabel(String outVLabel) throws Exception {
        this.outVLabel = outVLabel;
        if (this.outVLabel != null) 
            this.put("outVLabel", this.outVLabel);
        else 
            this.remove("outVLabel");
    }

    protected void setInVLabel(String inVLabel) throws Exception {
        this.inVLabel = inVLabel;
        if (this.inVLabel != null) 
            this.put("inVLabel", this.inVLabel);
        else 
            this.remove("inVLabel");
    }

    public Object getOutV() {
        return outV;
    }

    public Object getInV() {
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
     * @throws Exception if an error was encountered
     */
    public static Edge fromJSONObject(JSONObject json) throws Exception {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        if(! json.has("label"))
            throw new IllegalArgumentException("Property label in parameter json is required.");
        if(! json.has("outV"))
            throw new IllegalArgumentException("Property outV in parameter json is required.");
        if(! json.has("inV"))
            throw new IllegalArgumentException("Property inV in parameter json is required.");

        Edge edge = new Edge(
                json.getString("label"),
                json.get("outV"),
                json.get("inV")
        );
        edge.setId(json.optString("id"));
        edge.setProperties(json.optJSONObject("properties"));
        edge.setOutVLabel(json.optString("outVLabel"));
        edge.setInVLabel(json.optString("inVLabel"));
        return edge;
    }
}
