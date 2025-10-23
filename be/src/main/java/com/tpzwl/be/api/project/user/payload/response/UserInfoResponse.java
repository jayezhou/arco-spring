package com.tpzwl.be.api.project.user.payload.response;

import java.util.ArrayList;
import java.util.List;

public class UserInfoResponse {

	private Long id;
	private String name;
	private String email;
	private List<String> permissions = new ArrayList<>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

}
