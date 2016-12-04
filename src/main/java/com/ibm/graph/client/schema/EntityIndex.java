package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public abstract class EntityIndex extends JSONObject {

    private String name;
    private String[] propertyKeys;
    private boolean composite;
    private boolean unique;

    /**
     * Index constructor.
     * @param name a unique, non-empty string
     * @param propertyKeys for which this index is defined
     * @param composite true if this is a composite index, false otherwise
     * @param unique true if this is a unique index, false otherwise
     * @throws IllegalArgumentException if name is null or an empty string or propertyKeys an empty array or contains null or empty strings
     */
    protected EntityIndex(String name, String[] propertyKeys, boolean composite, boolean unique) throws IllegalArgumentException {
        if((name == null) || (name.trim().length() == 0)) 
            throw new IllegalArgumentException("Parameter name cannot be null or an empty string.");
        if((propertyKeys == null) || (propertyKeys.length == 0)) 
            throw new IllegalArgumentException("Parameter name cannot be null or an empty array.");
        for(String pk : propertyKeys) {
            if((pk == null) || (pk.trim().length() == 0))
                throw new IllegalArgumentException("Parameter propertyKeys cannot contain null values or empty strings.");
        }

        this.name = name;
        this.propertyKeys = propertyKeys;
        this.composite = composite;
        this.unique = unique;
        this.init();
    }

    /**
     * Returns the name of this index
     * @return String name of this index
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the property keys for which this index is defined
     * @return String[] property keys
     */    
    public String[] getPropertyKeys() {
        return propertyKeys;
    }

    /**
     * Returns true if this is a composite index, false otherwise
     * @return boolean true if this is a composite index
     */    
    public boolean isComposite() {
        return composite;
    }

    /**
     * Returns true if this is a unique index, false otherwise
     * @return boolean true if this is a unique index
     */    
    public boolean isUnique() {
        return unique;
    }

    private void init() {
        try {
            this.put("name", this.name);
            this.put("propertyKeys", this.propertyKeys);
            this.put("composite", this.composite);
            this.put("unique", this.unique);
        }
        catch(JSONException jsonex) {
            // Thrown if key is null, not a string, or the value could not be converted to JSON.
            // ignore because input is safe 
        }
    }
}
