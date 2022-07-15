package com.tuck.matches.beans;

import java.util.Date;
import java.util.List;

public class Availabilitys extends UserDetails {
	
	private Date startDate;
	private Date endDate;
	private List<String> options;
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Availabilitys [startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", options=");
		builder.append(options);
		builder.append(", getNumberOfMatches()=");
		builder.append(getNumberOfMatches());
		builder.append(", getName()=");
		builder.append(getName());
		builder.append(", getInterFirm()=");
		builder.append(getInterFirm());
		builder.append(", getFullTmOffer()=");
		builder.append(getFullTmOffer());
		builder.append(", getIsMentor()=");
		builder.append(getIsMentor());
		builder.append(", getUserName()=");
		builder.append(getUserName());
		builder.append(", getPassword()=");
		builder.append(getPassword());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append(", getCaseName()=");
		builder.append(getCaseName());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
