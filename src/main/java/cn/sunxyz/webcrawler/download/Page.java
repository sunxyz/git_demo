package cn.sunxyz.webcrawler.download;

import org.jsoup.nodes.Document;

import cn.sunxyz.webcrawler.Request;


public class Page {

	private Request request;

	private Document document;
	
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
}
