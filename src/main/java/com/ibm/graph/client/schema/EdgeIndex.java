package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EdgeIndex extends EntityIndex {

    public EdgeIndex(String name, String[] propertyKeys, boolean composite, boolean unique) throws Exception {
        super(name, propertyKeys, composite, unique);
    }

    public static EdgeIndex fromJSONObject(JSONObject json) throws Exception {
        return new EdgeIndex(
                json.getString("name"),
                (String[])json.getJSONArray("propertyKeys").toArray(new String[0]),
                json.getBoolean("composite"),
                json.getBoolean("unique")
        );
    }
}
