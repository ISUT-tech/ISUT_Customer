package com.isut.customer.model.login;

import com.google.gson.annotations.SerializedName;

public class AuthoritiesItem{

	@SerializedName("authority")
	private String authority;

	public String getAuthority(){
		return authority;
	}
}