package com.isut.customer.model.promo;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("code")
	private String code;

	@SerializedName("endDate")
	private String endDate;

	@SerializedName("description")
	private String description;

	@SerializedName("discount")
	private int discount;

	@SerializedName("id")
	private int id;

	@SerializedName("startDate")
	private String startDate;

	@SerializedName("updatedAt")
	private Object updatedAt;

	@SerializedName("status")
	private boolean status;

	public String getCreatedAt(){
		return createdAt;
	}

	public String getCode(){
		return code;
	}

	public String getEndDate(){
		return endDate;
	}

	public String getDescription(){
		return description;
	}

	public int getDiscount(){
		return discount;
	}

	public int getId(){
		return id;
	}

	public String getStartDate(){
		return startDate;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public boolean isStatus(){
		return status;
	}
}