package com.tuck.matches.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "matches")
public class Matches implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId private MatchesId matchesId;
	
	@Column(name = "CASES")
	private String cases;

	public String getCases() {
		return cases;
	}


	public void setCases(String cases) {
		this.cases = cases;
	}


	public MatchesId getMatchesId() {
		return matchesId;
	}


	public void setMatchesId(MatchesId matchesId) {
		this.matchesId = matchesId;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Matches [matchesId=");
		builder.append(matchesId.toString());
		builder.append(", cases=");
		builder.append(cases);
		builder.append("]");
		return builder.toString();
	}
}
