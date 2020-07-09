package com.zenglb.framework.news.http.NewsHttpService;


/**
 * 第三方新闻 这个接口返回的数据格式
 *
 * 和大家一般用的差异很大，这里故意演示不一样的情况
 *
 * 并不是都需要的
 *
 * Created by anylife.zlb@gmail.com on 2019/11/11.
 */
public class NewsHttpResponse<T> {
	private boolean error;
	private String status;
	private String message;
	private T results;


	public NewsHttpResponse(boolean error, String status, T results) {
		this.error = error;
		this.status = status;
		this.results = results;
	}

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

	public T getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Http3Response{" +
				"error=" + error +
				", status='" + status + '\'' +
				", message='" + message + '\'' +
				", results=" + results +
				'}';
	}
}
