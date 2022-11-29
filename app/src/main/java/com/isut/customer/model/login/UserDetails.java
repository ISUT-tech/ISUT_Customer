package com.isut.customer.model.login;

import com.google.gson.annotations.SerializedName;

public class UserDetails{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private int role;

	@SerializedName("mobileNumber")
	private String mobileNumber;

	public String getEmail() {
		return email;
	}

	@SerializedName("email")
	private String email;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedAt")
	private String updatedAt;

	public String getCreatedAt(){
		return createdAt;
	}

	public String getPassword(){
		return password;
	}

	public int getRole(){
		return role;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public String getFullName(){
		return fullName;
	}

	public int getId(){
		return id;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}


}