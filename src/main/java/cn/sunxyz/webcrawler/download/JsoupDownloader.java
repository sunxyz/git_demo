package cn.sunxyz.webcrawler.download;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.sunxyz.webcrawler.Page;
import cn.sunxyz.webcrawler.Request;


public class JsoupDownloader implements DownLoader {

	private static Integer timeout = 5000;

	public Document downloadDocument(String url) {
		Document document = null;
		try {
			document = Jsoup.connect(url).timeout(timeout).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	@Override
	public Page dowloader(Request request) {
		Document document = this.downloadDocument(request.getUrl());
		return new Page().setRequest(request).setDocument(document);
	}

	@Override
	public Page dowloader(String request) {
		return dowloader(new Request(request));
	}

}
