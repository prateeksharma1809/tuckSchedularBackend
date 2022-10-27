package com.tuck.matches.beans;

public class DeleteUserBean extends LoginForm {
	
	private String userToDelete;

	public String getUserToDelete() {
		return userToDelete;
	}

	public void setUserToDelete(String userToDelete) {
		this.userToDelete = userToDelete;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeleteUserBean [userToDelete=");
		builder.append(userToDelete);
		builder.append("]");
		return builder.toString();
	}
	

}
