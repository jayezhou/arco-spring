package com.tpzwl.be.api.payload.response;

import java.io.Serializable;

public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	// 20000: Success;
    // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
	private Long code;
	private String msg;
	private T data;
	
	public Response(Long code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
