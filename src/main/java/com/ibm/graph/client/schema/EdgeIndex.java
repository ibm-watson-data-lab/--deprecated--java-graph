package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Defines an Edge index that is associated with the Graph schema.
 */
public class EdgeIndex extends EntityIndex {

    /**
     * Edge index constructor.
     * @param name a unique, non-empty string
     * @param propertyKeys for which this index is defined, must not be null or empty
     * @param composite true if this is a composite index, false otherwise
     * @param unique true if this is a unique index, false otherwise
     * @throws IllegalArgumentException if name is null or an empty string or propertyKeys an empty array or contains null or empty strings
     */
    public EdgeIndex(String name, String[] propertyKeys, boolean composite, boolean unique) throws IllegalArgumentException {
        super(name, propertyKeys, composite, unique);
    }

    /**
     * Creates an EdgeIndex object from json. 
     * Required properties: name (not null, not empty string), propertyKeys (String[] containing only non null, non-empty values), composite (boolean) and unique (boolean)
     * Example JSON: 
     *  {"name": "byMarriedTo", "propertyKeys": ["name"], "composite": true, "unique": false}
     * @param json to be used to construct VertexLabel from
     * @return VertexIndex object
     * @throws IllegalArgumentException if json is null or doesn't define the required properties properly
     */
    public static EdgeIndex fromJSONObject(JSONObject json) throws IllegalArgumentException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        if((! json.has("name")) || (json.optString("name") == null))
            throw new IllegalArgumentException("Parameter json does not define property \"name\" of type String.");
        if(! json.has("propertyKeys"))
            throw new IllegalArgumentException("Parameter json does not define property \"propertyKeys\" of type String[].");
        if(! json.has("composite"))
            throw new IllegalArgumentException("Parameter json does not define property \"composite\" of type boolean.");
        if(! json.has("unique"))
            throw new IllegalArgumentException("Parameter json does not define property \"unique\" of type boolean.");
        String pn = null, t = null;
        try {
            // verify that the propertyKeys property is of type String[]
            pn = "propertyKeys";
            JSONArray propertyKeys = json.getJSONArray(pn);
            t = "String[]";
            // verify that the composite property is of type boolean
            pn = "composite";
            t = "boolean";
            // verify that the unique property is of type boolean
            boolean composite = json.getBoolean(pn);
            pn = "unique";
            boolean unique = json.getBoolean(pn);    
            return new EdgeIndex(json.getString("name"),
                                   (String[])propertyKeys.toArray(new String[0]),
                                   composite,
                                   unique);
        }
        catch(JSONException jsonex) {
            throw new IllegalArgumentException("Parameter json does not define property \"" + pn + "\" of type " + t + ".");
        }
        catch(Exception ex) {
            throw new IllegalArgumentException("Parameter json " + json.toString() + " does not define the required edge index properties: " + ex.getMessage());
        }        

    }
}
