package com.ibm.graph;

import com.ibm.graph.client.GraphClientException;
import com.ibm.graph.client.Edge;
import com.ibm.graph.client.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSet {

	private static Logger logger =  LoggerFactory.getLogger(ResultSet.class);		

	private String requestId = null; // e.g. "7e2914a1-cf17-4571-9791-4411c15d5c96" (optional)
	private String statusMessage = null;
	private String statusCode = null;
	private JSONArray data = null;
	private JSONObject response = null;

	/**
	 * Constructor
	 * @param response a JSONObject containing the IBM Graph response
	 * @throws GraphClientException if the IBM Graph response could not be processed
	 * @throws IllegalArgumentException if response is null
	 */
	public ResultSet(JSONObject response) throws GraphClientException, IllegalArgumentException {
    	if(response == null)
    		throw new IllegalArgumentException("response parameter is missing.");

    	this.response = response;

    	logger.debug("Creating ResultSet for response: " + response.toString());

		try {
			if(response.has("requestId")) {
				this.requestId = response.getString("requestId");
			}
			// extract status information
			if(response.has("status")) {
				if(response.getJSONObject("status").has("message"))
					this.statusMessage = response.getJSONObject("status").getString("message");
				if(response.getJSONObject("status").has("code"))
					this.statusCode = response.getJSONObject("status").getString("code");
			}
			else {
				// fallback: status information can also be returned as follows:
				// sample IBM Graph responses: 
				//	{"code":"BadRequestError","message":"bad request: outV=null, inV=null, label=null"}
				// 	{"code":"NotFoundError","message":"graph not found"}
				if(response.has("message"))
					this.statusMessage = response.getString("message");
				if(response.has("code"))
					this.statusCode = response.getString("code");					
			}
			if(response.has("result")) {
				this.data = response.getJSONObject("result").getJSONArray("data");
			}
			else {
				this.data = new JSONArray();
			}
			logger.debug("Created ResultSet for response: " + this.toString());
		}
		catch(Exception ex) {
			throw new GraphClientException("The IBM Graph response " + response.toString() + " could not be parsed: " + ex.getMessage(), ex);
		}			
	}

	/**
	 * Returns the request id that produced this ResultSet
	 * @return String request id
	 */
	public String getRequestId() {
		return this.requestId;
	}

	public boolean hasMetadata() {
		return ((this.statusCode != null) || (this.statusMessage != null));
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
	 * Returns a result iterator 
	 * @return Iterator JSONObject iterator for the results
	 */
	public Iterator<JSONObject> getJSONObjectResultIterator() {
		return this.data.iterator();
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
	 * Attempts to interpret each result as an com.ibm.graph.client.Vertex and returns an iterator.
	 * @return Iterator Vertex iterator
	 */
	public Iterator<Vertex> getVertexResultIterator() {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				vertices.add(Vertex.fromJSONObject(this.data.getJSONObject(i)));
			}
			catch(Exception ex) {
				vertices.add(null);
			}			
		}
		return vertices.iterator();
	};

	/**
	 * Returns the index-th result from the result set as a com.ibm.graph.client.Edge or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Edge the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Edge getResultAsEdge(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
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
	 * Attempts to interpret each result as an com.ibm.graph.client.Edge and returns an iterator.
	 * @return Iterator Edge iterator
	 */
	public Iterator<Edge> getEdgeResultIterator() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				edges.add(Edge.fromJSONObject(this.data.getJSONObject(i)));
			}
			catch(Exception ex) {
				edges.add(null);
			}			
		}
		return edges.iterator();
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
	 * Returns the index-th result from the result set as a Boolean or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Boolean the index-th result from the result set
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Boolean getResultAsBoolean(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
			try {
				return new Boolean(this.data.getBoolean(index));
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
	 * Attempts to interpret each result as an String and returns an iterator.
	 * @return Iterator String iterator
	 */
	public Iterator<String> getStringResultIterator() {
		ArrayList<String> strings = new ArrayList<String>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				strings.add(this.data.getString(i));
			}
			catch(Exception ex) {
				strings.add(null);
			}			
		}
		return strings.iterator();
	};

	/**
	 * Returns the IBM Graph response that this ResultSet is based on
	 * @return JSONObject the Graph API response
	 */
	public JSONObject getResponse() {
		return this.response;
	}

	/**
	 * Returns a string representation of this result set.
	 * @return String the result set encoded as a string
	 */
	public String toString() {
		return "requestId: " + this.requestId + " status code: " + this.statusCode + " status message: " + 
		       this.getStatusMessage() + " result set size: " + this.data.length();
	}

}