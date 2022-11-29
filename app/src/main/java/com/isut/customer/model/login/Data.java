package com.isut.customer.model.login;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("user_details")
	private UserDetails userDetails;

	@SerializedName("user")
	private User user;

	@SerializedName("token")
	private String token;

	public UserDetails getUserDetails(){
		return userDetails;
	}

	public User getUser(){
		return user;
	}

	public String getToken(){
		return token;
	}
}