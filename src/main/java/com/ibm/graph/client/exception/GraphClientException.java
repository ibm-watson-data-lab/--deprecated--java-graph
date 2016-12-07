package com.ibm.graph.client.exception;


/**
 * Thrown if the Graph Java library encountered a fatal error.
 */
public class GraphClientException extends Exception {

	/**
	 * Constructor
	 * @param message text
	 **/
    public GraphClientException(String message) {
        super(message);
    }

	/**
	 * Constructor
	 * @param message text
	 * @param cause root cause
	 **/
    public GraphClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
