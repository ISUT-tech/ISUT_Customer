package com.isut.customer.model.login;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("password")
	private String password;

	@SerializedName("credentialsNonExpired")
	private boolean credentialsNonExpired;

	@SerializedName("accountNonExpired")
	private boolean accountNonExpired;

	@SerializedName("authorities")
	private List<AuthoritiesItem> authorities;

	@SerializedName("enabled")
	private boolean enabled;

	@SerializedName("username")
	private String username;

	@SerializedName("accountNonLocked")
	private boolean accountNonLocked;

	public String getPassword(){
		return password;
	}

	public boolean isCredentialsNonExpired(){
		return credentialsNonExpired;
	}

	public boolean isAccountNonExpired(){
		return accountNonExpired;
	}

	public List<AuthoritiesItem> getAuthorities(){
		return authorities;
	}

	public boolean isEnabled(){
		return enabled;
	}

	public String getUsername(){
		return username;
	}

	public boolean isAccountNonLocked(){
		return accountNonLocked;
	}
}