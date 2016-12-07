package com.ibm.graph.client.response;


public class HTTPStatusInfo {
	
	protected int code;
	protected String reason;

	public HTTPStatusInfo(int code, String reason) throws IllegalArgumentException {
		if(code < 100)
			throw new IllegalArgumentException(code + " is not a valid HTTP status code.");
		this.code = code;
		this.reason = reason;
	}

	public int getStatusCode() {
		return this.code;
	}

	public String getReasonPhrase() {
		return this.reason;
	}

	public boolean isInformationalResponse() {
		return (this.code < 200);
	}

	public boolean isSuccessResponse() {
		return (this.code >= 200 &&  this.code < 300);
	}

	public boolean isRedirectResponse() {
		return (this.code >= 300 &&  this.code < 400);
	}

	public boolean isClientErrorResponse() {
		return (this.code >= 400 &&  this.code < 500);
	}

	public boolean isServerErrorResponse() {
		return (this.code >= 500);
	}

	public String toString() {
		return "HTTP code: " + this.code + " HTTP reason: \"" + this.reason + "\"";
	}
}