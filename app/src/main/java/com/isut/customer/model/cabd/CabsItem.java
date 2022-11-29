package com.isut.customer.model.cabd;

import com.google.gson.annotations.SerializedName;

public class CabsItem{

	@SerializedName("carImages")
	private String carImages;

	@SerializedName("carName")
	private String carName;

	@SerializedName("city")
	private String city;

	@SerializedName("identityCard")
	private String identityCard;

	@SerializedName("userId")
	private int userId;

	@SerializedName("carNumber")
	private String carNumber;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("licenseNumber")
	private String licenseNumber;

	@SerializedName("location")
	private String location;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("email")
	private String email;

	@SerializedName("updatedAt")
	private String updatedAt;

	@SerializedName("carModel")
	private String carModel;

	public String getCarImages(){
		return carImages;
	}

	public String getCarName(){
		return carName;
	}

	public String getCity(){
		return city;
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

	public String getLicenseNumber(){
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

	public String getUpdatedAt(){
		return updatedAt;
	}

	public String getCarModel(){
		return carModel;
	}
}