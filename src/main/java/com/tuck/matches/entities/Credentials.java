package com.tuck.matches.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "credentials")
public class Credentials {

	@Id
	@Column(name = "EMAIL_ID")
	private String userName;
	
	@Column(name = "PASS")
	private String password;
	
	@Column(name = "FULL_NAME")
	private String name;
	
	@Column(name = "INTERNSHIP_FIRM_NAME")
	private String interFirm;
	
	@Column(name = "FULL_TIME_OFFER")
	private String fullTmOffer;
	
	@Column(name = "IS_MENTOR")
	private Boolean isMentor;
	
	@Column(name = "CASE_NAME")
	private String caseName;
	
	@Column(name = "OFFICE_LOCATION")
	private String officeLocation;
	
	@Column(name = "NUMBER_OF_CASES")
	private Integer numberOfCases;
	
	public Integer getNumberOfCases() {
		return numberOfCases;
	}
	public void setNumberOfCases(Integer numberOfCases) {
		this.numberOfCases = numberOfCases;
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
	
	public String getCaseName() {
		return caseName;
	}
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	
	
	@Override
	public String toString() {
		return "Credentials [userName=" + userName + ", password=" + password + ", name=" + name + ", interFirm="
				+ interFirm + ", fullTmOffer=" + fullTmOffer + ", isMentor=" + isMentor + ", caseName=" + caseName
				+ ", officeLocation=" + officeLocation + ", numberOfCases=" + numberOfCases + "]";
	}
	public String getOfficeLocation() {
		return officeLocation;
	}
	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	
}
