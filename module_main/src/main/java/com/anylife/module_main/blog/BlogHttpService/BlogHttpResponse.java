package com.anylife.module_main.blog.BlogHttpService;


/**
 * 第三方博客 这个接口返回的数据格式和主项目的不一样
 *
 * Created by anylife.zlb@gmail.com on 2019/11/11.
 */
public class BlogHttpResponse<T> {
	private boolean error;
	private String status;
	private String message;
	private T data;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LiveDataResponse{" +
				"error=" + error +
				", status='" + status + '\'' +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
