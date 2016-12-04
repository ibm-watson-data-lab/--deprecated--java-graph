package com.ibm.graph.client.schema;

import org.apache.wink.json4j.JSONObject;

/**
 * Created by markwatson on 11/15/16.
 */
public class VertexLabel extends EntityLabel {

	public static final String DEFAULT_NAME = "vertex";

    /**
     * Constructor. Creates a VertexLabel named VertexLabel.DEFAULT_NAME
     * Vertex labels cannot be modified.
     */
    public VertexLabel() {
        super(DEFAULT_NAME);    
    }

    /**
     * Constructor. Creates a VertexLabel. 
     * @param name labels a vertex. If no name (or an empty string) is specified, VertexLabel.DEFAULT_NAME will be used as default. 
     * Vertex labels cannot be modified.
     */
    public VertexLabel(String name) {
        super(getNameOrDefault(name));    
    }

    private static String getNameOrDefault(String name) {
    	if((name == null) || (name.trim().length() == 0))
    		return DEFAULT_NAME;
    	else
    		return name;
    }

    /**
     * Creates a VertexLabel object from json. If the name property is not defined, VertexLabel.DEFAULT_NAME will be used as default.
     * Optional properties: name
     * Example JSON: 
     *  {"name": "person"}
     * @param json to be used to construct VertexLabel from
     * @return VertexLabel object
     * @throws IllegalArgumentException if json is null 
     */
    public static VertexLabel fromJSONObject(JSONObject json) throws IllegalArgumentException {
        if(json == null) 
            throw new IllegalArgumentException("Parameter json cannot be null");
        String name = DEFAULT_NAME;
        if(json.has("name")) {
        	if(json.optString("name") == null)
            	throw new IllegalArgumentException("Parameter json defines property \"name\" which is not of type \"String\".");
            else
            	name = json.optString("name");
        }

        return new VertexLabel(getNameOrDefault(name));
    }
}
