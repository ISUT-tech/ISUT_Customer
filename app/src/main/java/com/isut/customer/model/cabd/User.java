package com.isut.customer.model.cabd;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("role")
	private int role;

	@SerializedName("mobileNumber")
	private String mobileNumber;

	@SerializedName("appId")
	private Object appId;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("id")
	private int id;

	@SerializedName("profileImage")
	private Object profileImage;

	@SerializedName("updatedAt")
	private Object updatedAt;

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

	public Object getAppId(){
		return appId;
	}

	public String getFullName(){
		return fullName;
	}

	public int getId(){
		return id;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}
}