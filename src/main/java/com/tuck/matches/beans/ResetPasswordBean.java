package com.tuck.matches.beans;

public class ResetPasswordBean extends LoginForm {
	
	private String otp;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResetPasswordBean [otp=");
		builder.append(otp);
		builder.append(", getUserName()=");
		builder.append(getUserName());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append(", getPassword()=");
		builder.append(getPassword());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	

}
