package com.isut.customer.model.bookingNoti;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("booking")
	private Booking booking;

	@SerializedName("discount")
	private String discount;

	@SerializedName("tip")
	private int tip;

	public Booking getBooking(){
		return booking;
	}

	public String getDiscount(){
		return discount;
	}

	public int getTip(){
		return tip;
	}
}