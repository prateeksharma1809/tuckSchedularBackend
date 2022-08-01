package com.tuck.matches.beans;

public class LoginResponse {
	
	private Boolean openPage=false;
	private Boolean isAdmin=false;
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public Boolean getOpenPage() {
		return openPage;
	}
	public void setOpenPage(Boolean openPage) {
		this.openPage = openPage;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginResponse [openPage=");
		builder.append(openPage);
		builder.append(", isAdmin=");
		builder.append(isAdmin);
		builder.append("]");
		return builder.toString();
	}
	

}
