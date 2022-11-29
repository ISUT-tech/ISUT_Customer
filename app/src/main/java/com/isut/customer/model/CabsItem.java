package com.isut.customer.model;

import com.google.gson.annotations.SerializedName;

public class CabsItem{

	@SerializedName("carImages")
	private String carImages;

	@SerializedName("carName")
	private String carName;

	@SerializedName("city")
	private String city;

	@SerializedName("active")
	private boolean active;

	@SerializedName("identityCard")
	private String identityCard;

	@SerializedName("userId")
	private int userId;

	@SerializedName("carNumber")
	private String carNumber;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("carType")
	private int carType;

	@SerializedName("licenseNumber")
	private Object licenseNumber;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("email")
	private String email;

	@SerializedName("updatedAt")
	private Object updatedAt;

	@SerializedName("carModel")
	private String carModel;

	@SerializedName("status")
	private int status;

	public String getCarImages(){
		return carImages;
	}

	public String getCarName(){
		return carName;
	}

	public String getCity(){
		return city;
	}

	public boolean isActive(){
		return active;
	}

	public String getIdentityCard(){
		return identityCard;
	}

	public int getUserId(){
		return userId;
	}

	public String getCarNumber(){
		return carNumber;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getCarType(){
		return carType;
	}

	public Object getLicenseNumber(){
		return licenseNumber;
	}

	public String getLocation(){
		return location;
	}

	public int getId(){
		return id;
	}

	public String getState(){
		return state;
	}

	public String getEmail(){
		return email;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public String getCarModel(){
		return carModel;
	}

	public int getStatus(){
		return status;
	}
}