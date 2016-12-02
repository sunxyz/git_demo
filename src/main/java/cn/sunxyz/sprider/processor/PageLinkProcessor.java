package cn.sunxyz.sprider.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

import cn.sunxyz.sprider.Request;

public class PageLinkProcessor implements Process {
	
/*	private static final String HREF_MATCH = "//a/@href";*/
	
	@Override
	public Page process(Page page){
		Document document = page.getDocument();
		page.setRequests(this.getRequest(document));
		return page;
	}
	
	private List<Request> getRequest(Document document){
		List<String> hrefs = getHrefList(document);
		return hrefs.parallelStream().map(url -> new Request(url)).collect(Collectors.toList());
	}

/*	private List<String> getXsoupList(String xPath, Document document) {
		return Xsoup.compile(xPath).evaluate(document).list();
	}*/
	
	private List<String> getHrefList(Document document) {
		return document.select("a").parallelStream().map(link -> link.attr("abs:href")).collect(Collectors.toList());
	}
	
//	private String getXsoup(String xPath,Document document){
//		 return Xsoup.compile(xPath).evaluate(document).get();
//	}

}
