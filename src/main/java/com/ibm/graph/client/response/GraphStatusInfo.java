package com.ibm.graph.client.response;

import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

public class GraphStatusInfo {
	
	protected String code = null;
	protected String message = null;

	protected GraphStatusInfo(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getStatusCode() {
		return this.code;
	}

	public String getStatusMessage() {
		return this.message;
	}

	protected static GraphStatusInfo fromJSONObject(JSONObject json) throws IllegalArgumentException {
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
			return new GraphStatusInfo(code, message);			
		}
		catch(JSONException jsonex) {	
			throw new IllegalArgumentException("Graph response body could not be parsed.", jsonex);
		}
	}	
}