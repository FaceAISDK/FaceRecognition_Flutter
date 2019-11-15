package com.zlb.httplib;


/**
 * 这个类和具体的业务api 结构有关，本Demo的API 结构大致如下：
 *
 * Created by anylife.zlb@gmail.com on 2016/7/11.
 */
public class HttpResponse<T> {
	private int code;
	private String msg;
	private T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return "HttpResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}

}
