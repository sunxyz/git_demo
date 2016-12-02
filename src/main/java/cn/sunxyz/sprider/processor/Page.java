package cn.sunxyz.sprider.processor;

import java.util.List;

import org.jsoup.nodes.Document;

import cn.sunxyz.sprider.Request;

public class Page {

	private Request request;

	private Document document;

	private List<Request> requests;
	
	public Request getRequest() {
		return request;
	}

	public Page setRequest(Request request) {
		this.request = request;
		return this;
	}

	public Document getDocument() {
		return document;
	}

	public Page setDocument(Document document) {
		this.document = document;
		return this;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public Page setRequests(List<Request> requests) {
		this.requests = requests;
		return this;
	}

}
