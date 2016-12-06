package com.ibm.graph.client.exception;

import com.ibm.graph.client.response.GraphStatusInfo;
import com.ibm.graph.client.response.HTTPStatusInfo;

/**
 * Thrown if IBM Graph encountered a fatal error.
 */
public class GraphException extends Exception {

	GraphStatusInfo graphStatus = null;
	HTTPStatusInfo httpStatus = null;

    public GraphException(String message) {
        super(message);
    }

    public GraphException(String message, GraphStatusInfo graphStatus, HTTPStatusInfo httpStatus) {
        super(message);
        this.graphStatus = graphStatus;
        this.httpStatus = httpStatus;
    }

    public GraphException(GraphStatusInfo graphStatus, HTTPStatusInfo httpStatus) {
        super();
        this.graphStatus = graphStatus;
        this.httpStatus = httpStatus;
    }
}