package com.ibm.graph.client.response;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

/**
 * Encapsulates status information that IBM Graph optionally returns with a request.
 * 
 */
public class GraphStatusInfo {
	
	protected String code = null;
	protected String message = null;

	/**
	 * Constructor.
	 * @param code IBM Graph status code
	 * @param message IBM Graph status message
	 * @throws IllegalArgumentException if code AND message are null or empty strings
	 */
	public GraphStatusInfo(String code, String message) throws IllegalArgumentException {
		if((code != null) && (code.trim().length() > 0))
			this.code = code;
		if((message != null) && (message.trim().length() > 0))			
			this.message = message;
		if((this.code == null) && (this.message == null))			
			throw new IllegalArgumentException("Parameter code and message are null or empty strings.");
	}

	/**
	 * @return the status code, or null
	 */
	public String getStatusCode() {
		return this.code;
	}

	/**
	 * @return the status message, or null
	 */
	public String getStatusMessage() {
		return this.message;
	}

	public String toString() {
		return "Graph code: \"" + this.code + "\" Graph message: \"" + this.message + "\"";
	}

	/**
	 * Instantiates a GraphStatusInfo object from json. json must define at least one of the following properties
	 * @param json from which the status code and message shall be extracted
	 * @return GraphStatusInfo if json defines at least one of the properties, or null if both are undefined
	 * @throws IllegalArgumentException is null
	 */
	public static GraphStatusInfo fromJSONObject(JSONObject json) throws IllegalArgumentException {
		if(json == null)
			throw new IllegalArgumentException("Parameter \"json\" is null.");
		String code = null;
		String message = null;
		try {
			if(json.has("status")) {
				if(json.getJSONObject("status").has("message"))
					message = json.getJSONObject("status").getString("message");
				if(json.getJSONObject("status").has("code"))
					code = json.getJSONObject("status").getString("code");
			}
			else {
				// sample data structure {"code":"NotFoundError","message":"graph not found"}
				if(json.has("code"))
					code = json.getString("code");
				if(json.has("message"))
					message = json.getString("message");
			}
			if((code != null) || (message != null))
				return new GraphStatusInfo(code, message);			
			else 
				return null;
		}
		catch(JSONException jsonex) {	
			throw new IllegalArgumentException("Graph response body could not be parsed.", jsonex);
		}
	}	
}