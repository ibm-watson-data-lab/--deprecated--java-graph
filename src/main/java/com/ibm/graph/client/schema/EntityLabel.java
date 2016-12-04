package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public abstract class EntityLabel extends JSONObject {

    private String name;

    /**
     * Constructor. 
     * @param name of this entity
     */
    protected EntityLabel(String name) {
        this.name = name;
        this.init();
    }

    /**
     * @return String the name of this EntityLabel or null if not defined
     */
    public String getName() {
        return name;
    }

    private void init() {
        try {
            this.put("name", this.name);
        }
        catch(JSONException jsonex) {
            // Thrown if key is null, not a string, or the value could not be converted to JSON.
            // ignore because input is safe 
        }
    }
}
