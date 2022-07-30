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
		builder.append(", caseName=");
		builder.append(caseName);
		builder.append("]");
		return builder.toString();
	}

	
}
