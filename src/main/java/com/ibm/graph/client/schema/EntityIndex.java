package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EntityIndex extends JSONObject {

    private String name;
    private String[] propertyKeys;
    private boolean composite;
    private boolean unique;

    public EntityIndex(String name, String[] propertyKeys, boolean composite, boolean unique) throws Exception {
        this.name = name;
        this.propertyKeys = propertyKeys;
        this.composite = composite;
        this.unique = unique;
        this.init();
    }

    public String getName() {
        return name;
    }

    public String[] getPropertyKeys() {
        return propertyKeys;
    }

    public boolean isComposite() {
        return composite;
    }

    public boolean isUnique() {
        return unique;
    }

    private void init() throws Exception {
        this.put("name", this.name);
        this.put("propertyKeys", this.propertyKeys);
        this.put("composite", this.composite);
        this.put("unique", this.unique);
    }
}
