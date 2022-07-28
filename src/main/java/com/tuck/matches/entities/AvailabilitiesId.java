package com.tuck.matches.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AvailabilitiesId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "EMAIL_ID", nullable = false)
	private String username;
	
	@Column(name = "FROM_TIME", nullable = false)
	private Date from;
	
	@Column(name = "TO_TIME", nullable = false)
	private Date to;

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AvailabilitiesId [username=");
		builder.append(username);
		builder.append(", from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(to);
		builder.append("]");
		return builder.toString();
	}
	
}
