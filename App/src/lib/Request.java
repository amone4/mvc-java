package lib;

import java.util.ArrayList;

public class Request {
	private String url;
	private boolean isApiRequest;
	private String component;
	private String method;
	private ArrayList<String> params;

	Request(String url, Boolean isAPIRequest, String component, String method, ArrayList<String> params) {
		this.url = url;
		this.isApiRequest = isAPIRequest;
		this.component = component;
		this.method = method;
		this.params = params;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) { this.url = url; }

	public boolean getApiRequest() {
		return isApiRequest;
	}

	public void setApiRequest(boolean isApiRequest) {
		this.isApiRequest = isApiRequest;
	}

	public String getComponent() { return component; }

	public void setComponent(String component) {
		this.component = component;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ArrayList<String> getParams() {
		return params;
	}

	public void setParams(ArrayList<String> params) {
		this.params = params;
	}
}