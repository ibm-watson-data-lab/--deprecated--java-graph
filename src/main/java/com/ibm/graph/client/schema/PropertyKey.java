package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class PropertyKey extends JSONObject {

    public enum PropertyKeyDataType {
        Integer("Integer"), 
        Float("Float"), 
        Boolean("Boolean"), 
        String("String");

        private String string;

        PropertyKeyDataType(String string) {
            this.string = string;
        }

        protected static PropertyKeyDataType fromString(String string) 
                                             throws IllegalArgumentException {
            if(string != null) {
                for(PropertyKeyDataType pkdt : PropertyKeyDataType.values()) {
                    if(string.equalsIgnoreCase(pkdt.string))
                        return pkdt;
                }
            }
            throw new IllegalArgumentException("\"" + string + "\" is not a valid PropertyKeyDataType.");
        }
    }

    public enum PropertyKeyCardinality {
        SINGLE("SINGLE"), 
        LIST("LIST"), 
        SET("SET");

        private String string;

        PropertyKeyCardinality(String string) {
            this.string = string;
        }

        protected static PropertyKeyCardinality fromString(String string) 
                                                throws IllegalArgumentException {
            if(string != null) {
                for(PropertyKeyCardinality pkc : PropertyKeyCardinality.values()) {
                    if(string.equalsIgnoreCase(pkc.string))
                        return pkc;
                }
            }
            throw new IllegalArgumentException("\"" + string + "\" is not a valid PropertyKeyCardinality.");
        }
    }

    private String name;
    private PropertyKeyDataType dataType;
    private PropertyKeyCardinality cardinality;

    /**
     * Constructor. Creates a PropertyKey with cardinality com.ibm.graph.client.schema.PropertyKeyCardinality.SINGLE.
     * @param name A unique string. This value is required.
     * @param dataType Supported data types are com.ibm.graph.client.schema.PropertyKeyDataType.Integer, com.ibm.graph.client.schema.PropertyKeyDataType.Float, com.ibm.graph.client.schema.PropertyKeyDataType.Boolean, and com.ibm.graph.client.schema.PropertyKeyDataType.String. This value is required.
     * @throws IllegalArgumentException if any parameter violates the stated constraints
     */
    public PropertyKey(String name, PropertyKeyDataType dataType) throws IllegalArgumentException {
        this(name, dataType, null);
    }

    /**
     * Constructor
     * @param name A unique string. This value is required.
     * @param dataType Supported data types are PropertyKeyDataType.Integer, PropertyKeyDataType.Float, PropertyKeyDataType.Boolean, and PropertyKeyDataType.String. This value is required.
     * @param cardinality One of SINGLE, LIST, or SET. The default is SINGLE.
     * @throws IllegalArgumentException if any parameter violates the stated constraints
     */
    public PropertyKey(String name, PropertyKeyDataType dataType, PropertyKeyCardinality cardinality) throws IllegalArgumentException {
        if((name == null) || (name.trim().length() == 0))
            throw new IllegalArgumentException("Parameter name is null or empty.");
        if(dataType == null)
            throw new IllegalArgumentException("Parameter dataType is null.");
        if(cardinality == null)
            cardinality = PropertyKeyCardinality.SINGLE; // default

        this.name = name;
        this.dataType = dataType;
        this.cardinality = cardinality;
        this.init();
    }

    public String getName() {
        return name;
    }

    public PropertyKeyDataType getDataType() {
        return dataType;
    }

    public PropertyKeyCardinality getCardinality() {
        return cardinality;
    }

    private void init()  {
        try {
            this.put("name", this.name);
            this.put("dataType", this.dataType.name());
            this.put("cardinality", this.cardinality.name());
        }
        catch(JSONException jsonex) {
            // Thrown if key is null, not a string, or the value could not be converted to JSON.
            // values are safe. ignore
        }
    }

    /**
     * Creates a PropertyKey object from json. 
     * Required properties: name, dataType
     * Optional properties: cardinality (defaults to SINGLE)
     * Example JSON: 
     *  {"name": "id", "dataType": "String"}
     *  {"name": "phone", "dataType": "String", "cardinality": "SET"}
     * @param json representation of the property key, which must define the name and dataType properties as Strings
     * @return PropertyKey if json defines all required properties
     * @throws IllegalArgumentException if json is null or does not define the required properties
     */
    public static PropertyKey fromJSONObject(JSONObject json) throws IllegalArgumentException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        if((! json.has("name")) || (json.optString("name") == null))
            throw new IllegalArgumentException("Parameter json does not define property \"name\" of type \"String\".");
        if((! json.has("dataType")) || (json.optString("dataType") == null))
            throw new IllegalArgumentException("Parameter json does not define property \"dataType\" of type \"String\".");

        String name = json.optString("name");
        PropertyKeyDataType dataType = null;
        PropertyKeyCardinality cardinality = PropertyKeyCardinality.SINGLE; // default

        try {
            dataType = PropertyKeyDataType.fromString(json.optString("dataType"));
            if(json.has("cardinality"))
                cardinality = PropertyKeyCardinality.fromString(json.optString("cardinality"));
        }
        catch(IllegalArgumentException iaex) {
            throw iaex;
        }

        PropertyKey propertyKey = new PropertyKey(name,
                                                  dataType,
                                                  cardinality);

        return propertyKey;
    }
}
