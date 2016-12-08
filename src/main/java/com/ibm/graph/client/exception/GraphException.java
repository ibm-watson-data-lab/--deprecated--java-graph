package com.ibm.graph.client.exception;

import com.ibm.graph.client.response.GraphStatusInfo;
import com.ibm.graph.client.response.HTTPStatusInfo;

/**
 * Thrown if the IBM Graph service (engine or web server) encountered a fatal error.
 * 
 */
public class GraphException extends Exception {

	String responseBody = null;
    GraphStatusInfo graphStatus = null;
	HTTPStatusInfo httpStatus = null;

    /**
     * Creates an instance of this exception that indicates that the Graph engine or the Graph web server reported a failure.
     * @param httpStatus HTTP status information (code and reason)
     * @param httpResponseBody body of response that was returned by the server
     * @param graphStatus IBM Graph status message (code and message), if set, this indicates that an error was reported by the graph engine
     * @throws IllegalArgumentException if httpStatus is null
     */
    public GraphException(HTTPStatusInfo httpStatus, String httpResponseBody, GraphStatusInfo graphStatus) throws IllegalArgumentException{
        this(null, httpStatus, httpResponseBody, graphStatus);
    } 

    /**
     * Creates an instance of this exception that indicates that the Graph engine or the Graph web server reported a failure.
     * @param message optional message text
     * @param httpStatus HTTP status information (code and reason)
     * @param httpResponseBody body of response that was returned by the server
     * @param graphStatus IBM Graph status message (code and message), if set, this indicates that an error was reported by the graph engine
     * @throws IllegalArgumentException if httpStatus is null
     */
    public GraphException(String message, HTTPStatusInfo httpStatus, String httpResponseBody, GraphStatusInfo graphStatus) throws IllegalArgumentException{
        super(message);
        if(httpStatus == null) 
            throw new IllegalArgumentException("Parameter httpStatus is null.");
        this.httpStatus = httpStatus;
        this.responseBody = httpResponseBody;
        this.graphStatus = graphStatus;
    } 

    /**
     * @return true if this exception was generated because of a Graph engine error.
     */
    public boolean isGraphEngineException() {
        return (this.graphStatus != null);
    }

    /**
     * Returns the HTTP status code and message
     * @return GraphStatusInfo
     */
    public HTTPStatusInfo getHTTPStatus() {
        return this.httpStatus;
    }

    /**
     * If provided, returns the graph engine status code and message
     * @return GraphStatusInfo
     */
    public GraphStatusInfo getGraphStatus() {
        return graphStatus;
    }

    /**
     * If provided, returns the server's reponse body as text
     * @return String response body
     */
    public String getResponseBody() {
        return this.responseBody;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer("Message: \"" + this.getMessage() + "\" ");
        if(this.httpStatus != null)
            buff.append(this.httpStatus.toString() + " ");
        if(this.graphStatus != null)
            buff.append(this.graphStatus.toString() + " ");
        buff.append("Response body: \"" + this.responseBody + "\"");
        return buff.toString();
    }
}