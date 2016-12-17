package com.ibm.graph.client;

import com.ibm.graph.client.exception.GraphClientException;

import org.apache.wink.json4j.JSONObject;

/**
 * Base class for elements stored or retrieved from a Graph.
 */
public class Element extends JSONObject {

    /**
     * Attempts to create an element from json. If json doesn't describe a valid element, null is returned.
     * @param json JSON representation of a an element
     * @return Element if json describes an element, null otherwise
     * @throws IllegalArgumentException json is null
     * @throws GraphClientException if an error occurred during the deserialization process 
     */
    public static Element fromJSONObject(JSONObject json) throws IllegalArgumentException, GraphClientException {
    	if(json == null) 
            throw new IllegalArgumentException("Parameter \"json\" cannot be null.");

        if (json.isNull("objects")) {
            return Entity.fromJSONObject(json);
        }
        else {
            return Path.fromJSONObject(json);
        }
    }
}
