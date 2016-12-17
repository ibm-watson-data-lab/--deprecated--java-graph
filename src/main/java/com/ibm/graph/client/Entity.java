package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphClientException;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

import java.util.HashMap;

/**
 * Base class for elements stored in a Graph.
 */
public class Entity extends Element {

    public enum EntityType {
        Vertex("vertex"), 
        Edge("edge"), 
        Unknown(null);

        private String string;

        EntityType(String string) {
            this.string = string;
        }

        public String toString() {
            return this.string;
        }

        protected static EntityType fromString(String string) throws IllegalArgumentException {
            if(string != null) {
                for(EntityType t : EntityType.values()) {
                    if(string.equalsIgnoreCase(t.string))
                        return t;
                }
            }
            throw new IllegalArgumentException("\"" + string + "\" is not a valid Entity type.");
        }
    }

    protected EntityType type = EntityType.Unknown;
    private String id = null;
    private String label = null;
    private HashMap<String, Object> properties = null;

    protected Entity(String label) throws GraphClientException {
        this(label, null);
    }

    protected Entity(String label, HashMap<String, Object> properties) throws GraphClientException {
        this.id = null;
        this.label = label;
        if(properties != null)
            this.properties = properties;
        else {
            this.properties = new HashMap<String, Object>();
        }
        try {
            if(this.label != null)       
                this.put("label", this.label);
            if (this.properties.size() > 0) 
                this.put("properties", this.properties);
        }
        catch(JSONException jsonex) {
            throw new GraphClientException("Error deserializing Entity properties.", jsonex);
        }
    }

    protected void setId(String id) {
        this.id = id;
        if(id  != null) {
            try {
                this.put("id", this.id);
            }
            catch(JSONException jsonex) {
                // TODO: String data type for 
                // Thrown if key is null, not a string, or the value could not be converted to JSON.
            }
        }
        else
            this.remove("id");
    }

    protected void setProperties(JSONObject jsonProperties) throws GraphClientException {
        try {
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
        catch(JSONException jsonex) {
            throw new GraphClientException("Error deserializing Entity properties.", jsonex);
        }   
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public EntityType getType() {
        return this.type;
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

    public Object getPropertyValue(String propertyName) {
        return this.properties.get(propertyName);
    }

    public void setPropertyValue(String name, Object value) throws GraphClientException {
        this.setPropertyValue(name, value, true);
    }

    private void setPropertyValue(String name, Object value, boolean updateJsonObject) throws GraphClientException {
        if(name == null)
            return; // nothing to do

        if(value != null)
            this.properties.put(name, value);
        else
            this.properties.remove(name);

        try {
            if (updateJsonObject) {
                this.put("properties", this.properties);
            }
        }
        catch(JSONException jsonex) {
            throw new GraphClientException("Error deserializing Entity properties.", jsonex);
        } 
    }

    /**
     * Attempts to create an entity from json. If json doesn't describe a valid entity, null is returned.
     * @param json JSON representation of a an entity or a basic data type, not null     
     * @return Entity if json describes an entity, null otherwise
     * @throws IllegalArgumentException json is null
     * @throws GraphClientException if an error occurred on the client       
     */
    public static Entity fromJSONObject(JSONObject json) throws IllegalArgumentException, GraphClientException{
        if(json == null) 
            throw new IllegalArgumentException("Parameter \"json\" cannot be null.");

        try {
            if(! json.has("type"))
                return null;        // JSON does not describe an Entity (vertex or edge)

            switch(json.getString("type").toLowerCase()) {
                case "vertex":
                                return Vertex.fromJSONObject(json);
                case "edge":
                                return Edge.fromJSONObject(json);
                default:
                                return null; // unknown type
            }
        }
        catch(Exception ex) {
            throw new GraphClientException("Error deserializing Entity object. ", ex);
        }
    }
}
