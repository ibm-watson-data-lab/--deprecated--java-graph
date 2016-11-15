package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class PropertyKey extends JSONObject {

    private String name;
    private String dataType;
    private String cardinality;

    public PropertyKey(String name, String dataType, String cardinality) throws Exception {
        this.name = name;
        this.dataType = dataType;
        this.cardinality = cardinality;
        this.init();
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getCardinality() {
        return cardinality;
    }

    private void init() throws Exception {
        this.put("name", this.name);
        this.put("dataType", this.dataType);
        this.put("cardinality", this.cardinality);
    }

    public static PropertyKey fromJSONObject(JSONObject json) throws Exception {
        PropertyKey propertyKey = new PropertyKey(
            json.getString("name"),
            json.getString("dataType"),
            json.getString("cardinality")
        );
        return propertyKey;
    }
}
