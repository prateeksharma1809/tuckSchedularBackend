package com.tuck.matches.beans;

public class UserDetails {

	private String userName;
	private String password;
	private String name;
	private String interFirm;
	private String fullTmOffer;
	private Boolean isMentor=false;
	private String numberOfMatches;
	private String caseName;
	private String officeLoc;
	

	public String getNumberOfMatches() {
		return numberOfMatches;
	}
	public void setNumberOfMatches(String numberOfMatches) {
		this.numberOfMatches = numberOfMatches;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInterFirm() {
		return interFirm;
	}
	public void setInterFirm(String interFirm) {
		this.interFirm = interFirm;
	}
	public String getFullTmOffer() {
		return fullTmOffer;
	}
	public void setFullTmOffer(String fullTmOffer) {
		this.fullTmOffer = fullTmOffer;
	}
	public Boolean getIsMentor() {
		return isMentor;
	}
	public void setIsMentor(Boolean isMentor) {
		this.isMentor = isMentor;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDetails [userName=");
		builder.append(userName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", name=");
		builder.append(name);
		builder.append(", interFirm=");
		builder.append(interFirm);
		builder.append(", fullTmOffer=");
		builder.append(fullTmOffer);
		builder.append(", isMentor=");
		builder.append(isMentor);
		builder.append(", numberOfMatches=");
		builder.append(numberOfMatches);
		builder.append(", caseName=");
		builder.append(caseName);
		builder.append(", officeLoc=");
		builder.append(officeLoc);
		builder.append("]");
		return builder.toString();
	}
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	public String getOfficeLoc() {
		return officeLoc;
	}
	public void setOfficeLoc(String officeLoc) {
		this.officeLoc = officeLoc;
	}
	
	
	
}
