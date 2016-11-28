package com.ibm.graph.client;

import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Created by markwatson on 11/8/16.
 */
public class Edge extends Entity {

    private Object outV;
    private String outVLabel;
    private Object inV;
    private String inVLabel;

    public Edge(String label, Object outV, Object inV) throws Exception {
        this(label, outV, inV, null);
    }

    public Edge(String label, Object outV, Object inV, HashMap properties) throws Exception {
        super(label, properties);
        this.outV = outV;
        this.inV = inV;
        this.put("outV", this.outV);
        this.put("inV", this.inV);
    }

    private Edge(String id, String label, Object outV, Object inV, HashMap properties) throws Exception {
        super(label, properties);
        this.outV = outV;
        this.inV = inV;
        this.put("outV", this.outV);
        this.put("inV", this.inV);
    }

    protected void setOutVLabel(String outVLabel) throws Exception {
        this.outVLabel = outVLabel;
        if (this.outVLabel != null) {
            this.put("outVLabel", this.outVLabel);
        }
    }

    protected void setInVLabel(String inVLabel) throws Exception {
        this.inVLabel = inVLabel;
        if (this.inVLabel != null) {
            this.put("inVLabel", this.inVLabel);
        }
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

    public static Edge fromJSONObject(JSONObject json) throws Exception {
        Edge edge = new Edge(
                json.getString("label"),
                json.get("outV"),
                json.get("inV")
        );
        edge.setId(json.get("id"));
        edge.setProperties(json.optJSONObject("properties"));
        edge.setOutVLabel(json.optString("outVLabel"));
        edge.setInVLabel(json.optString("inVLabel"));
        return edge;
    }

}
