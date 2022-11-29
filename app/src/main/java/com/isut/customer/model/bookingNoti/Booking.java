package com.isut.customer.model.bookingNoti;

import com.google.gson.annotations.SerializedName;

public class Booking{

	@SerializedName("destinationLocation")
	private String destinationLocation;

	@SerializedName("fair")
	private int fair;
	@SerializedName("discount")
	private String discount;

	@SerializedName("userId")
	private int userId;

	@SerializedName("bookingId")
	private int bookingId;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("driverId")
	private int driverId;

	@SerializedName("scheduleDate")
	private String scheduleDate;

	@SerializedName("sourceLocation")
	private String sourceLocation;

	@SerializedName("promoCode")
	private String promoCode;

	@SerializedName("id")
	private int id;

	@SerializedName("userMobileNumber")
	private String userMobileNumber;

	@SerializedName("updatedAt")
	private String updatedAt;

	@SerializedName("status")
	private int status;

	public String getDestinationLocation(){
		return destinationLocation;
	}

	public int getFair(){
		return fair;
	}

	public int getUserId(){
		return userId;
	}

	public int getBookingId(){
		return bookingId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public int getDriverId(){
		return driverId;
	}

	public String getScheduleDate(){
		return scheduleDate;
	}

	public String getSourceLocation(){
		return sourceLocation;
	}

	public String getPromoCode(){
		return promoCode;
	}

	public int getId(){
		return id;
	}

	public String getUserMobileNumber(){
		return userMobileNumber;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public int getStatus(){
		return status;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}
}