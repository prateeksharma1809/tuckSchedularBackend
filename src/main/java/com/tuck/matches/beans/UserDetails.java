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
	private Integer numberOfCases;
	private String otp;
	

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
		return "UserDetails [userName=" + userName + ", password=" + password + ", name=" + name + ", interFirm="
				+ interFirm + ", fullTmOffer=" + fullTmOffer + ", isMentor=" + isMentor + ", numberOfMatches="
				+ numberOfMatches + ", caseName=" + caseName + ", officeLoc=" + officeLoc + ", numberOfCases="
				+ numberOfCases + "]";
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
	public Integer getNumberOfCases() {
		return numberOfCases;
	}
	public void setNumberOfCases(Integer numberOfCases) {
		this.numberOfCases = numberOfCases;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
	
}
