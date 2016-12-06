package com.ibm.graph.client.response;

import com.ibm.graph.client.exception.GraphClientException;

import com.ibm.graph.client.response.HTTPStatusInfo;
import com.ibm.graph.client.response.GraphStatusInfo;
import com.ibm.graph.client.response.ResultSet;

import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONObject;
import org.apache.wink.json4j.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphResponse {
	
   	private static Logger logger =  LoggerFactory.getLogger(GraphResponse.class);

	private HTTPStatusInfo httpStatus = null;
	private GraphStatusInfo graphStatus = null;
	private String httpResponseBody = null;
	private ResultSet resultSet = null;

	public GraphResponse(HTTPStatusInfo status, String body) throws GraphClientException, IllegalArgumentException {
		if(status == null)
			throw new IllegalArgumentException("HTTP response information is missing.");

		this.httpStatus = status;
		if((body != null) || (body.trim().length() > 0))
			this.httpResponseBody = body;			
		else
			this.httpResponseBody = "";

		if(logger.isDebugEnabled()) {
			logger.debug("HTTP response: " + this.httpStatus.toString());	
			logger.debug("HTTP response body: " + this.httpResponseBody);	
		}
		
		if(this.httpStatus.isSuccessResponse()) {
			// IBM Graph returned success response; the body's content should be JSON
			try {
				JSONObject jsonResponse = new JSONObject(this.httpResponseBody);
				this.graphStatus = GraphStatusInfo.fromJSONObject(jsonResponse);
				this.resultSet = new ResultSet(jsonResponse);	
			} 
			catch(GraphClientException gcex) {
				// result set processing failed
				throw gcex;
			}			
			catch(JSONException jsonex) {
				// the response body is not json
				throw new GraphClientException("IBM Graph response with HTTP code \"" + this.httpStatus.getStatusCode() + "\" and message body " + this.httpResponseBody + "\" cannot be processed.", jsonex);
			}
			catch(Exception ex) {
				// propagate error information
				throw new GraphClientException("IBM Graph response with HTTP code \"" + this.httpStatus.getStatusCode() + "\" and message body " + this.httpResponseBody + "\" cannot be processed.", ex);
			}
		}
		else {
			if(this.httpStatus.isClientErrorResponse()) {
				// IBM Graph returned an err response; the body's content should be JSON
				try {
					// sample IBM Graph responses: 
					//	{"code":"BadRequestError","message":"bad request: outV=null, inV=null, label=null"}
					// 	{"code":"NotFoundError","message":"graph not found"}
					this.graphStatus = GraphStatusInfo.fromJSONObject(new JSONObject(this.httpResponseBody));
					// no result set will be provided	
				} 
				catch(JSONException jsonex) {
					// the response body is not json
					throw new GraphClientException("IBM Graph response with HTTP code \"" + this.httpStatus.getStatusCode() + "\" and message body " + this.httpResponseBody + "\" cannot be processed.", jsonex);			
				}	
			}
			else {
				// generic server responses that we can't handle. raise error
				throw new GraphClientException("IBM Graph response with HTTP code \"" + this.httpStatus.getStatusCode() + "\" and message body " + this.httpResponseBody + "\" cannot be processed.");			
			}
		}
	}

	public HTTPStatusInfo getHTTPStatus() {
		return this.httpStatus;
	}

	public boolean hasResultSet() {
		return (this.resultSet != null);
	}

	public ResultSet getResultSet() {
		return this.resultSet;
	}

	public GraphStatusInfo getGraphStatus() {
		return this.graphStatus;
	}

	public String getResponseBody() {
		return this.httpResponseBody;
	}

}