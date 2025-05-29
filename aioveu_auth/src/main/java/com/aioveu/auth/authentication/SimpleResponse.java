package com.aioveu.auth.authentication;


/**
 * @description 简单响应的封装类
 * @author: 雒世松
 * @date: 2020/5/31 0031 21:13
 */
public class SimpleResponse {

	public SimpleResponse(Object data, Integer code, String message) {
		this.data = data;
		this.code = code;
		this.message = message;
	}

	private Object data;

	private Integer code;

	private String message;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
