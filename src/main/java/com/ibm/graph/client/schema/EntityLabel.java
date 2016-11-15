package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EntityLabel extends JSONObject {

    private String name;

    public EntityLabel(String name) throws Exception {
        this.name = name;
        this.init();
    }

    public String getName() {
        return name;
    }

    private void init() throws Exception {
        this.put("name", this.name);
    }
}
