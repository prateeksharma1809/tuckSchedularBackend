package com.tuck.matches.beans;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsWithAvalabilites extends UserDetails {
	private List<Availabilitys> availabilitys = new ArrayList<Availabilitys>();

	public List<Availabilitys> getAvailabilitys() {
		return availabilitys;
	}

	public void setAvailabilitys(List<Availabilitys> availabilitys) {
		this.availabilitys = availabilitys;
	}
	
	
	
}
