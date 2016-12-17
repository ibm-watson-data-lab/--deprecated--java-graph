package com.ibm.graph.client.response;

import com.ibm.graph.client.exception.GraphClientException;
import com.ibm.graph.client.Edge;
import com.ibm.graph.client.Path;
import com.ibm.graph.client.Vertex;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to IBM Graph results.
 *
 */
public class ResultSet {

	private static Logger logger =  LoggerFactory.getLogger(ResultSet.class);		

	// the IBM Graph response on which this result set is based
	private JSONObject response = null;

	// optional; identifies the request that triggered the response
	private String requestId = null; 

	// the extracted result set data
	private JSONArray data = null;

	/**
	 * Constructor. Extracts result set information from IBM Graph response.
	 * @param response a JSONObject containing the IBM Graph response
	 * @throws GraphClientException if the IBM Graph response could not be processed
	 * @throws IllegalArgumentException if response is null
	 */
	protected ResultSet(JSONObject response) throws GraphClientException, IllegalArgumentException {
    	if(response == null)
    		throw new IllegalArgumentException("Parameter response is null.");

    	this.response = response;

    	logger.debug("Creating ResultSet from response: " + response.toString());

		try {
			if(response.has("requestId")) {
				this.requestId = response.getString("requestId");
			}

			if(response.has("result")) {
				if(response.getJSONObject("result").has("data"))
					this.data = response.getJSONObject("result").getJSONArray("data");
			}
			else {
				// some responses don't follow the result > data structure, e.g. {"graphs":["1...3","1","g","zzz"]}
				// if the response is TODO
				// wrap those responses in an array; this way a response is accessible as the first ResultSet element
				// [{"graphs":["1...3","1","g","zzz"]}]
				this.data = new JSONArray();
				if(response.length() > 0)
					this.data.add(response);
			}

			logger.debug("Created ResultSet for response: " + this.toString());
		}
		catch(Exception ex) {
			throw new GraphClientException("The IBM Graph response " + response.toString() + " could not be parsed: " + ex.getMessage(), ex);
		}			
	}

	/**
	 * Returns the request id that produced this ResultSet
	 * @return String request id, if set
	 */
	public String getRequestId() {
		return this.requestId;
	}

	/**
	 * Returns true if this result set contains data, false otherwise.
	 * @return boolean true if the result set contains data
	 */
	public boolean hasResults() {
		return (this.data.length() > 0);
	}

	/**
	 * Returns the number of results in this result set or 0 if no result is available
	 * @return int the number of results in the result set
	 */
	public int getResultCount() {
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
				if(! this.data.isNull(index))
					return this.data.getJSONObject(index);
				return null;
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to JSONObject.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");
		}
	};

	/**
	 * Returns a result iterator 
	 * @return Iterator JSONObject iterator for the results
	 */
	@SuppressWarnings("unchecked") 
	public Iterator<JSONObject> getJSONObjectResultIterator() {
		return (Iterator<JSONObject>)this.data.iterator();
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
				if(! this.data.isNull(index))
					return Vertex.fromJSONObject(this.data.getJSONObject(index));
				return null;
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to Vertex.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");			
		}
	};

	/**
	 * Attempts to interpret each result as an com.ibm.graph.client.Vertex and returns an iterator.
	 * @return Iterator Vertex iterator or null if the results cannot be converted to Vertex objects
	 */
	public Iterator<Vertex> getVertexResultIterator() {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				if(! this.data.isNull(i))
					vertices.add(Vertex.fromJSONObject(this.data.getJSONObject(i)));
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result set could not be interpreted as a Vertex array.", ex);
				logger.debug("Result set: " + this.data.toString());
				return null;								
			}			
		}
		return vertices.iterator();
	};

	/**
	 * Returns the index-th result from the result set as a com.ibm.graph.client.Edge or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Edge the index-th result from the result set, or null
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Edge getResultAsEdge(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
			try {
				if(! this.data.isNull(index))
					return Edge.fromJSONObject(this.data.getJSONObject(index));
				return null;				
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to Edge.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");
		}
	};

	/**
	 * Attempts to interpret each result as an com.ibm.graph.client.Edge and returns an iterator.
	 * @return Iterator Edge iterator or null if the result set does not represent an array of edges
	 */
	public Iterator<Edge> getEdgeResultIterator() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				if(! this.data.isNull(i))				
					edges.add(Edge.fromJSONObject(this.data.getJSONObject(i)));
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result set could not be interpreted as an Edge array.", ex);
				logger.debug("Result set: " + this.data.toString());
				return null;								
			}		
		}
		return edges.iterator();
	};

	/**
	 * Returns the index-th result from the result set as a com.ibm.graph.client.Path or null if the result cannot be converted
	 * @param index a number between 0 (first result) and (getResultCount() - 1)
	 * @return Path the index-th result from the result set, or null
	 * @throws IndexOutOfBoundsException if index is not valid for this result set
	 */
	public Path getResultAsPath(int index) throws IndexOutOfBoundsException {
		if((index >= 0) && (index <= this.data.length() - 1)) {
			try {
				if(! this.data.isNull(index))
					return Path.fromJSONObject(this.data.getJSONObject(index));
				return null;				
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to Path.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");
		}
	};

	/**
	 * Attempts to interpret each result as an com.ibm.graph.client.Path and returns an iterator.
	 * @return Iterator Path iterator or null if the result set does not represent an array of paths
	 */
	public Iterator<Path> getPathResultIterator() {
		ArrayList<Path> paths = new ArrayList<Path>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				if(! this.data.isNull(i))				
					paths.add(Path.fromJSONObject(this.data.getJSONObject(i)));
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result set could not be interpreted as a Path array.", ex);
				logger.debug("Result set: " + this.data.toString());
				return null;								
			}		
		}
		return paths.iterator();
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
				if(! this.data.isNull(index))
					return this.data.getString(index);
				return null;
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to String.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");
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
				if(! this.data.isNull(index)) {

					if(("false").equalsIgnoreCase(this.data.getString(index)) ||
					   ("true").equalsIgnoreCase(this.data.getString(index)))
						return new Boolean(this.data.getString(index));
					return null;
					
					// this doesn't seem to work as expected
					// return new Boolean(this.data.getString(index));

				}
				return null;
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result " + index + " cannot be converted to Boolean.", ex);
				logger.debug("Result: " + this.data.get(index).toString());
				return null;
			}
		}
		else {
			throw new IndexOutOfBoundsException("The result set contains " + this.data.length() + " results. Index starts at 0.");
		}
	};

	/**
	 * Attempts to interpret each result as an String and returns an iterator.
	 * @return Iterator String iterator or null if the result set does not represent an array of String
	 */
	public Iterator<String> getStringResultIterator() {
		ArrayList<String> strings = new ArrayList<String>();
		for(int i = 0; i < this.data.length(); i++) {
			try {
				strings.add(this.data.getString(i));
			}
			catch(Exception ex) {
				// suppress error
				logger.debug("Result set could not be interpreted as a String array.", ex);
				logger.debug("Result set: " + this.data.toString());
				return null;	
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

	public String toString() {
		StringBuffer buff = new StringBuffer("Request id: " + this.requestId + " ");
		if(response != null)
			buff.append("Response: " + this.response.toString() + " ");
		if(data != null)
			buff.append("Data: " + this.data.toString());
		return buff.toString();
	}
}