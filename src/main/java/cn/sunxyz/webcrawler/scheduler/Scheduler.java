package cn.sunxyz.webcrawler.scheduler;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.sunxyz.bean.JavaBeanManager;
import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.webcrawler.Request;
import cn.sunxyz.webcrawler.Request.RequestLinkType;
import cn.sunxyz.webcrawler.Site;
import cn.sunxyz.webcrawler.builder.Builder;
import cn.sunxyz.webcrawler.builder.annotation.HelpLink;
import cn.sunxyz.webcrawler.builder.annotation.TargetLink;
import cn.sunxyz.webcrawler.download.DownLoader;
import cn.sunxyz.webcrawler.download.JsoupDownloader;
import cn.sunxyz.webcrawler.download.Page;
import cn.sunxyz.webcrawler.parser.LinkParser;
import cn.sunxyz.webcrawler.parser.PageLinkParser;
import cn.sunxyz.webcrawler.pipeline.DeafultPipeLine;
import cn.sunxyz.webcrawler.pipeline.pipeline;
import cn.sunxyz.webcrawler.queue.Crawl;
import cn.sunxyz.webcrawler.queue.QueueCrawl;
import cn.sunxyz.webcrawler.queue.component.HashSetVisitedSet;
import cn.sunxyz.webcrawler.queue.component.VisitedSet;

public class Scheduler {

	private static Logger logger = LoggerFactory.getLogger(Scheduler.class);

	// 待访问对列
	private Crawl<Request> urlsQueue;
	// 所有符合规则的链接
	private VisitedSet<String> urlsSet;

	private DownLoader downLoader;

	private LinkParser linkParser;

	private JavaBeanManager beanManager;

	private pipeline<Object> pipeline;

	// TODO 可以设置默认值
	Pattern targetLinkPattern = Pattern.compile("(https://www.zhihu.com/question/\\d*)");
	int targetLinkGroup = 1;
	Pattern helpLinkPattern = Pattern.compile("//title/text()");
	int helpLinkGroup = 0;

	{
		urlsQueue = new QueueCrawl<>();
		urlsSet = new HashSetVisitedSet<>();
		downLoader = new JsoupDownloader();
		linkParser = new PageLinkParser();
		pipeline = new DeafultPipeLine<>();
	}

	public Scheduler init(Class<?> clazz, String... urls) {
		return init(clazz, pipeline, urls);
	}

	// TODO object 需要改为持久化工具
	public Scheduler init(Class<?> clazz, pipeline<Object> pipeline, String... urls) {
		beanManager = JavaBeanManager.create().init(clazz, new Builder());
		this.pipeline = pipeline;
		this.push(urls);
		JavaBean bean = beanManager.getJavaBean();
		Annotation[] annotations = bean.getClassAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof HelpLink) {
				HelpLink helpLink = (HelpLink) annotation;
				helpLinkPattern = Pattern.compile(helpLink.value());
				helpLinkGroup = helpLink.group();
			} else if (annotation instanceof TargetLink) {
				TargetLink targetLink = (TargetLink) annotation;
				targetLinkPattern = Pattern.compile(targetLink.value());
				targetLinkGroup = targetLink.group();
			}
		}
		return this;
	}

	public void run() {
		this.download();
	}

	// TODO 设置缓存和日志记录与链接管理
	public Scheduler setCache() {
		return this;
	}

	private void download() {
		while (urlsQueue.size() > 0) {
			Request request = urlsQueue.pop();
			Page page = downLoader.dowloader(request);
			Set<String> links = linkParser.getLinks(page);
			Set<Request> requests = this.getRequests(links);
			for (Request request2 : requests) {
				logger.debug("urlsQueue.push {}", request2.getUrl());
			}
			Object owner = beanManager.setter(page.getDocument());// 注值
			pipeline.process(owner, new Site(page, requests));// 流处理
			// TODO 此处可以设置缓存
			this.push(requests);
		}
	}

	private Set<Request> getRequests(Set<String> links) {
		Set<Request> requests = new HashSet<>();
		for (String link : links) {
			Matcher tm = targetLinkPattern.matcher(link);
			if (tm.find()) {
				requests.add(new Request(tm.group(targetLinkGroup), RequestLinkType.TARGET));
			}
			Matcher hm = helpLinkPattern.matcher(link);
			if (hm.find()) {
				requests.add(new Request(tm.group(targetLinkGroup), RequestLinkType.HELP));
			}
		}
		return requests;
	}

	private void push(String... urls) {
		for (String url : urls) {
			if (urlsSet.hasExisted(url)) {
				urlsQueue.push(new Request(url, RequestLinkType.OTHER));
			}
		}
	}

	private void push(Collection<Request> requests) {
		for (Request request : requests) {
			if (urlsSet.hasExisted(request.getUrl())) {
				urlsQueue.push(request);
			}
		}
	}

}
