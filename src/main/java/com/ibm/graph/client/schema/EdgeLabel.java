package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class EdgeLabel extends EntityLabel {

   public enum EdgeLabelMultiplicity {
        MULTI("MULTI"), 
        SIMPLE("SIMPLE"), 
        MANY2ONE("MANY2ONE"), 
        ONE2MANY("ONE2MANY"),
        ONE2ONE("ONE2ONE");

        private String string;

        EdgeLabelMultiplicity(String string) {
            this.string = string;
        }

        protected static EdgeLabelMultiplicity fromString(String string) 
                                               throws IllegalArgumentException {
            if(string != null) {
                for(EdgeLabelMultiplicity elm : EdgeLabelMultiplicity.values()) {
                    if(string.equalsIgnoreCase(elm.string))
                        return elm;
                }
            }
            throw new IllegalArgumentException("\"" + string + "\" is not a valid EdgeLabelMultiplicity.");
        }
    }

    private EdgeLabelMultiplicity multiplicity;

    /**
     * Constructor. Creates an EdgeLabel with cardinality com.ibm.graph.client.schema.EdgeLabelMultiplicity.SIMPLE.
     * @param name A unique string. A non-empty value is required.
     * @throws IllegalArgumentException if any parameter violates the stated constraints
     */
    public EdgeLabel(String name) throws IllegalArgumentException {
        this(name, null);
    }

    /**
     * Constructor. Creates an EdgeLabel with the specified EdgeLabelMultiplicity.
     * @param name A unique string. A non empty value is required.
     * @param multiplicity One of com.ibm.graph.client.schema.EdgeLabelMultiplicity.MULTI, 
     * com.ibm.graph.client.schema.EdgeLabelMultiplicity.SIMPLE, com.ibm.graph.client.schema.EdgeLabelMultiplicity.MANY2ONE, 
     * com.ibm.graph.client.schema.EdgeLabelMultiplicity.ONE2MANY, com.ibm.graph.client.schema.EdgeLabelMultiplicity.ONE2ONE. 
     * Defaults to com.ibm.graph.client.schema.EdgeLabelMultiplicity.SIMPLE if multiplicity is null.
     * @throws IllegalArgumentException if any parameter violates the stated constraints
     */
    public EdgeLabel(String name, EdgeLabelMultiplicity multiplicity) throws IllegalArgumentException {
        super(name);
        if((name == null) || (name.trim().length() == 0))
            throw new IllegalArgumentException("Parameter name is null or empty.");
        if(multiplicity == null)
            multiplicity = EdgeLabelMultiplicity.SIMPLE; // default

        this.multiplicity = multiplicity;
        try {
            this.put("multiplicity", this.multiplicity.name());
        }
        catch(JSONException jsonex) {
            // cannot be thrown
            throw new IllegalArgumentException("JSONException caught.", jsonex);
        }
    }

    public EdgeLabelMultiplicity getMultiplicity() {
        return multiplicity;
    }    

    /**
     * Creates an EdgeLabel object from json. 
     * Required properties: name
     * Optional properties: multiplicity (defaults to SIMPLE)
     * Example JSON: 
     *  {"name": "marriedTo"}
     *  {"name": "manages", "multiplicity": "ONE2MANY"}
     * @param json representation of the edge label, which must define the name and (optionally) multiplicity properties
     * @return EdgeLabel if json defines all required properties
     * @throws IllegalArgumentException if json is null or does not define the required properties
     */
    public static EdgeLabel fromJSONObject(JSONObject json) throws IllegalArgumentException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        if((! json.has("name")) || (json.optString("name") == null))
            throw new IllegalArgumentException("Parameter json does not define property \"name\" of type \"String\".");

        EdgeLabelMultiplicity multiplicity = EdgeLabelMultiplicity.SIMPLE; // default

        try {
            if(json.has("multiplicity"))
                multiplicity = EdgeLabelMultiplicity.fromString(json.optString("multiplicity"));
        }
        catch(IllegalArgumentException iaex) {
            throw iaex;
        }

        return new EdgeLabel(
                json.optString("name"),
                multiplicity
        );
    }
}
