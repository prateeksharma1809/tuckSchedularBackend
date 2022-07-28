package com.tuck.matches.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MatchesId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "EMAIL_ID_MENTOR", nullable = false)
	private String email_mentor;
	
	@Column(name = "EMAIL_ID_MENTEE", nullable = false)
	private String email_mentee;
	
	@Column(name = "FROM_TIME", nullable = false)
	private Date from;
	
	@Column(name = "TO_TIME", nullable = false)
	private Date to;

	public String getEmail_mentor() {
		return email_mentor;
	}


	public void setEmail_mentor(String email_mentor) {
		this.email_mentor = email_mentor;
	}


	public String getEmail_mentee() {
		return email_mentee;
	}


	public void setEmail_mentee(String email_mentee) {
		this.email_mentee = email_mentee;
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
		builder.append("MatchesId [email_mentor=");
		builder.append(email_mentor);
		builder.append(", email_mentee=");
		builder.append(email_mentee);
		builder.append(", from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(to);
		builder.append("]");
		return builder.toString();
	}
	

	
}
