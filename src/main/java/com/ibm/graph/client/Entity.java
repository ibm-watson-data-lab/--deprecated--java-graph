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

    protected void setId(Object id) throws Exception {
        this.id = id;
        this.put("id", this.id);
    }

    protected void setProperties(JSONObject jsonProperties) throws Exception {
        this.properties = new HashMap();
        if (jsonProperties != null) {
            for(Object key : jsonProperties.keySet()) {
                String name = key.toString();
                Object value;
                Object o = jsonProperties.get(name);
                if (o instanceof JSONArray) {
                    value = ((JSONArray)o).getJSONObject(0).get("value");
                }
                else if (o instanceof JSONObject && ((JSONObject)o).has("value")) {
                    value = ((JSONObject)o).get("value");
                }
                else {
                    value = o;
                }
                this.setPropertyValue(name, value, false);
            }
        }
        this.put("properties", this.properties);
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
        if (this.properties != null) {
            return this.properties.get(propertyName);
        }
        else {
            return null;
        }
    }

    public void setPropertyValue(String name, Object value) throws Exception {
        this.setPropertyValue(name, value, true);
    }

    private void setPropertyValue(String name, Object value, boolean updateJsonObject) throws Exception {
        if (this.properties == null) {
            this.properties = new HashMap();
        }
        this.properties.put(name, value);
        if (updateJsonObject) {
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
}
