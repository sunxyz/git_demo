package cn.sunxyz.sprider;

import java.io.Serializable;

public class Request implements Serializable {

	private static final long serialVersionUID = -2066123662930610580L;

	private String url;
	
	private MethodType method;

	public Request() {
		super();
	}

	public Request(String url) {
		super();
		this.url = url;
	}

	public Request(String url, MethodType method) {
		super();
		this.url = url;
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public MethodType getMethod() {
		return method;
	}

	public void setMethod(MethodType method) {
		this.method = method;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public enum MethodType{
		DetailsPage,
		ListPage
	}

}
