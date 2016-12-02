package com.ibm.graph.client;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Created by markwatson on 11/8/16.
 */
public class Entity extends Element {

    private Object id = null;
    private String label = null;
    private HashMap properties = null;

    public Entity(String label) throws Exception {
        this(label, null);
    }

    public Entity(String label, HashMap properties) throws Exception {
        this.id = null;
        this.label = label;
        if(properties != null)
            this.properties = properties;
        else {
            this.properties = new HashMap();
        }
        if(this.label != null)       
            this.put("label", this.label);
        if (this.properties.size() > 0) 
            this.put("properties", this.properties);
    }

    protected void setId(Object id) throws Exception {
        this.id = id;
        if(id  != null)
            this.put("id", this.id);
        else
            this.remove("id");
    }

    protected void setProperties(JSONObject jsonProperties) throws Exception {
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
        if (this.properties.size() > 0) 
            this.put("properties", this.properties);
    }

    public Object getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Returns the properties of this entity or null if no properties are defined
     *  @return properties of this entity
     */
    public HashMap getProperties() {
        if (this.properties.size() > 0)
            return this.properties;
        else 
            return null;
    }

    public Object getPropertyValue(String propertyName) throws Exception {
        return this.properties.get(propertyName);
    }

    public void setPropertyValue(String name, Object value) throws Exception {
        this.setPropertyValue(name, value, true);
    }

    private void setPropertyValue(String name, Object value, boolean updateJsonObject) throws Exception {
        if(value != null)
            this.properties.put(name, value);
        else
            this.properties.remove(name);

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
