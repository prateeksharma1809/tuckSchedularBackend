package com.tuck.matches.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "availabilities")
public class Availabilities {

	@EmbeddedId
	private AvailabilitiesId availabilitiesId;

	@Column(name = "IS_MENTOR")
	private Boolean isMentor;

	@Column(name = "CASES")
	private String cases;

	@Column(name = "IS_MATCHED")
	private Boolean isMatched;

	public Boolean getIsMentor() {
		return isMentor;
	}

	public void setIsMentor(Boolean isMentor) {
		this.isMentor = isMentor;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public AvailabilitiesId getAvailabilitiesId() {
		return availabilitiesId;
	}

	public void setAvailabilitiesId(AvailabilitiesId availabilitiesId) {
		this.availabilitiesId = availabilitiesId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Availabilities [availabilitiesId=");
		builder.append(availabilitiesId.toString());
		builder.append(", isMentor=");
		builder.append(isMentor);
		builder.append(", cases=");
		builder.append(cases);
		builder.append(", isMatched=");
		builder.append(isMatched);
		builder.append("]");
		return builder.toString();
	}

	public Boolean getIsMatched() {
		return isMatched;
	}

	public void setIsMatched(Boolean isMatched) {
		this.isMatched = isMatched;
	}

}
