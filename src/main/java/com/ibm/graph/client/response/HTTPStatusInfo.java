package com.ibm.graph.client.response;

/**
 * Encapsulates HTTP Status information.
 *
 */
public class HTTPStatusInfo {
	
	protected int code;
	protected String reason = null;

	/**
	 * Constructor 
	 * @param code HTTP code, as defined in http://www.iana.org/assignments/http-status-codes/http-status-codes.xhtml
	 * @param reason message, if defined 
	 * @throws IllegalArgumentException if code &lt; 100
	 */
	public HTTPStatusInfo(int code, String reason) throws IllegalArgumentException {
		if((code < 100) || (code > 599))
			throw new IllegalArgumentException(code + " is not a valid HTTP status code.");
		this.code = code;
		if((reason != null) && (reason.trim().length() > 0))
			this.reason = reason;
	}

	/**
	 * @return int HTTP status code
	 */
	public int getStatusCode() {
		return this.code;
	}

	/**
	 * @return String HTTP status reason message. if not defined, an empty string is returned
	 */
	public String getReasonPhrase() {
		return this.reason;
	}

	/**
	 * @return boolean true if this is an informational status (1xx)
	 */
	public boolean isInformationalStatus() {
		return (this.code < 200);
	}

	/**
	 * @return boolean true if this is a success status (2xx)
	 */
	public boolean isSuccessStatus() {
		return (this.code >= 200 &&  this.code < 300);
	}

	/**
	 * @return boolean true if this is a redirect status (3xx)
	 */
	public boolean isRedirectStatus() {
		return (this.code >= 300 &&  this.code < 400);
	}

	/**
	 * @return boolean true if this is a server error status (5xx)
	 */
	public boolean isClientErrorStatus() {
		return (this.code >= 400 &&  this.code < 500);
	}

	/**
	 * @return boolean true if this is an informational status (1xx)
	 */

	public boolean isServerErrorStatus() {
		return (this.code >= 500);
	}

	public String toString() {
		return "HTTP code: " + this.code + " HTTP reason: \"" + this.reason + "\"";
	}
}