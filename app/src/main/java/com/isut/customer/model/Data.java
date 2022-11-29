package com.isut.customer.model;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("role")
	private int role;

	@SerializedName("mobileNumber")
	private String mobileNumber;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("active")
	private boolean active;

	@SerializedName("profileImage")
	private Object profileImage;

	@SerializedName("createdAt")
	private Object createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("licenceExpired")
	private Object licenceExpired;

	@SerializedName("appId")
	private Object appId;

	@SerializedName("verify")
	private boolean verify;

	@SerializedName("id")
	private int id;

	@SerializedName("rewardPoints")
	private Object rewardPoints;

	@SerializedName("email")
	private String email;

	@SerializedName("updatedAt")
	private String updatedAt;

	public int getRole(){
		return role;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public String getFullName(){
		return fullName;
	}

	public boolean isActive(){
		return active;
	}

	public Object getProfileImage(){
		return profileImage;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public String getPassword(){
		return password;
	}

	public Object getLicenceExpired(){
		return licenceExpired;
	}

	public Object getAppId(){
		return appId;
	}

	public boolean isVerify(){
		return verify;
	}

	public int getId(){
		return id;
	}

	public Object getRewardPoints(){
		return rewardPoints;
	}

	public String getEmail(){
		return email;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}