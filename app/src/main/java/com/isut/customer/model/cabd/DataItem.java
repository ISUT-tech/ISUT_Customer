package com.isut.customer.model.cabd;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem{

	@SerializedName("cabs")
	private List<CabsItem> cabs;
	@SerializedName("licenseNumber")
	private String licenseNumber;
	@SerializedName("user")
	private User user;

	public List<CabsItem> getCabs(){
		return cabs;
	}

	public User getUser(){
		return user;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}
}