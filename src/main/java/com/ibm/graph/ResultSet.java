package com.ibm.graph;

import com.ibm.graph.client.GraphClientException;
import com.ibm.graph.client.Edge;
import com.ibm.graph.client.Vertex;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

public class ResultSet {

 // {"requestId":"7e2914a1-cf17-4571-9791-4411c15d5c96","status":{"message":"","code":200,"attributes":{}},"result":{"data":[{"id":4288,"label":"band","type":"vertex","properties":{"monthly_listeners":[{"id":"1zs-3b4-3yd","value":"192302"}],"name":[{"id":"17c-3b4-sl","value":"Declan McKenna"}],"genre":[{"id":"1lk-3b4-35x","value":"Folk"}]}}],"meta":{}}}	public ResultSet() {

	private String requestId = null; // e.g. "7e2914a1-cf17-4571-9791-4411c15d5c96"
	private String statusMessage = null;
	private String statusCode = null;
	private JSONArray data = null;

	/**
	 * Constructor
	 * @param response a JSONObject containing the IBM Graph response
	 */
	public ResultSet(JSONObject response) throws GraphClientException {

		if(response != null) {
			try {
				this.requestId = response.getString("requestId");
				// extract status information
				if(response.has("status")) {
					this.statusMessage = response.getJSONObject("status").getString("message");
					this.statusCode = response.getJSONObject("status").getString("code");
				}
				if(response.has("result")) {
					this.data = response.getJSONObject("result").getJSONArray("data");
				}
				else {
					this.data = new JSONArray();
				}
			}
			catch(Exception ex) {
				throw new GraphClientException("The IBM Graph response could not be parsed: " + ex.getMessage(), ex);
			}			
		}
	}

	/**
	 * Returns the request id that produced this ResultSet
	 * @return String request id
	 */
	public String getRequestId() {
		return this.requestId;
	}

	/**
	 * Returns the response's status code
	 * @return String the response's HTTP status code (200 = ok)
	 */
	public String getStatusCode() {
		return this.statusCode;
	}

	/**
	 * Returns the response's message text
	 * @return String message text
	 */
	public String getStatusMessage() {
		return this.statusMessage;
	}

	/**
	 * Returns true if this result set contains data, false otherwise.
	 * @return boolean true if the result set contains data
	 */
	public boolean hasResults() {
		return (this.data.length() > 0);
	}

	/**
	 * Returns the number of results or 0 if no result is available
	 * @return long the number of results in the result set
	 */
	public long getResultCount() {
		return this.data.length();
	}

	/**
	 * Returns the index-th result from the result set as a JSONObject or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return JSONObject the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public JSONObject getResultAsJSONObject(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
			try {
				return this.data.getJSONObject(index);
			}
			catch(JSONException jsonex) {
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	};

	/**
	 * Returns the index-th result from the result set as a com.ibm.graph.client.Vertex object or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Vertex the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Vertex getResultAsVertex(int index) throws IndexOutOfBoundsException {
		if((this.data != null) && (index >= 0) && (index <= this.data.length() - 1)) {
			try {
				return Vertex.fromJSONObject(this.data.getJSONObject(index));
			}
			catch(Exception ex) {
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	};

	/**
	 * Returns the index-th result from the result set as a com.ibm.graph.client.Edge or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Edge the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Edge getResultAsEdge(int index) throws IndexOutOfBoundsException {
		if((this.data != null) && (index >= 0) && (index <= this.data.length() - 1)) {
			try {
				return Edge.fromJSONObject(this.data.getJSONObject(index));
			}
			catch(Exception ex) {
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	};

	/**
	 * Returns the index-th result from the result set as a String or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return String the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public String getResultAsString(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
			try {
				return this.data.getString(index);
			}
			catch(JSONException jsonex) {
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	};

	/**
	 * Returns a string representation of this result set.
	 * @return String the result set encoded as a string
	 */
	public String toString() {
		return "requestId: " + this.requestId + " status code: " + this.statusCode + " status message: " + 
		       this.getStatusMessage() + " result set size: " + this.data.length();
	}

}