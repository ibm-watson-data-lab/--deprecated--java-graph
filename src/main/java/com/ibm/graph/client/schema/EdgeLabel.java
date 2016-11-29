package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EdgeLabel extends EntityLabel {

    private String multiplicity;

    public EdgeLabel(String name) throws Exception {
        this(name, "SIMPLE");
    }

    public EdgeLabel(String name, String multiplicity) throws Exception {
        super(name);
        this.multiplicity = multiplicity;
        this.put("multiplicity", this.multiplicity);
    }

    public static EdgeLabel fromJSONObject(JSONObject json) throws Exception {
        return new EdgeLabel(
                json.getString("name"),
                json.getString("multiplicity")
        );
    }
}
