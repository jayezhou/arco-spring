package com.tpzwl.be.api.security.payload.response;

public class LoginResponse {
	private Long id;
	private String username;
	private String email;
	private String token;
	private String refreshToken;

	public LoginResponse(Long id, String username, String email, String token, String refreshToken) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
