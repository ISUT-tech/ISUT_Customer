package com.isut.customer.model.Booking;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("driverId")
	private int driverId;

	@SerializedName("destinationLocation")
	private String destinationLocation;

	@SerializedName("sourceLocation")
	private String sourceLocation;

	@SerializedName("id")
	private int id;

	@SerializedName("userMobileNumber")
	private String userMobileNumber;

	@SerializedName("fair")
	private int fair;

	@SerializedName("userId")
	private int userId;

	@SerializedName("updatedAt")
	private String updatedAt;

	@SerializedName("status")
	private int status;

	public String getCreatedAt(){
		return createdAt;
	}

	public int getDriverId(){
		return driverId;
	}

	public String getDestinationLocation(){
		return destinationLocation;
	}

	public String getSourceLocation(){
		return sourceLocation;
	}

	public int getId(){
		return id;
	}

	public String getUserMobileNumber(){
		return userMobileNumber;
	}

	public int getFair(){
		return fair;
	}

	public int getUserId(){
		return userId;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public int getStatus(){
		return status;
	}
}