package com.ibm.graph;


/**
 * Created by markwatson on 11/28/16.
 */
public class GraphException extends Exception {

    public GraphException(String message) {
        super(message);
    }

    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }
}