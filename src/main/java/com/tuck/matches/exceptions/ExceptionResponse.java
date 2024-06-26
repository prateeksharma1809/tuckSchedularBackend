package com.tuck.matches.exceptions;

import java.util.Date;

public class ExceptionResponse{

	private Date timestamp;
	private String message;
	private String details;
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExceptionResponse [timestamp=");
		builder.append(timestamp);
		builder.append(", message=");
		builder.append(message);
		builder.append(", details=");
		builder.append(details);
		builder.append("]");
		return builder.toString();
	}
	public ExceptionResponse(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	
}
