package com.ibm.graph.client;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Created by markwatson on 11/8/16.
 */
public class Entity extends Element {

    private Object id;
    private String label;
    private HashMap properties;

    public Entity(String label) throws Exception {
        this(label, null);
    }

    public Entity(String label, HashMap properties) throws Exception {
        this.label = label;
        this.properties = properties;
        this.put("label", this.label);
        if (this.properties != null && this.properties.size() > 0) {
            this.put("properties", this.properties);
        }
    }

    public static Entity fromJSONObject(JSONObject json) throws Exception {
        if (json.getString("type").toLowerCase().equals("edge")) {
            return Edge.fromJSONObject(json);
        }
        else {
            return Vertex.fromJSONObject(json);
        }
    }

    protected void setId(Object id) throws Exception {
        this.id = id;
        this.put("id", this.id);
    }

    public Object getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public HashMap getProperties() {
        return properties;
    }

    public Object getPropertyValue(String propertyName) throws Exception {
        Object o = this.properties.get(propertyName);
        if (o instanceof JSONArray) {
            return ((JSONArray)o).getJSONObject(0).get("value");
        }
        else if (o instanceof JSONObject && ((JSONObject)o).has("value")) {
            return ((JSONObject)o).get("value");
        }
        else {
            return o;
        }
    }
}
