package cn.sunxyz.sprider;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.bean.JavaBeanManager;
import cn.sunxyz.bean.setter.Setter;
import cn.sunxyz.sprider.builder.annotation.HelpLink;
import cn.sunxyz.sprider.builder.annotation.TargetLink;
import cn.sunxyz.sprider.download.DownLoader;
import cn.sunxyz.sprider.download.JsoupDownloader;
import cn.sunxyz.sprider.processor.Page;
import cn.sunxyz.sprider.processor.PageLinkProcessor;
import cn.sunxyz.sprider.scheduler.QueueScheduler;
import cn.sunxyz.sprider.scheduler.Scheduler;
import cn.sunxyz.sprider.scheduler.component.HashSetVisitedQueue;
import cn.sunxyz.sprider.scheduler.component.VisitedQueue;

public class Sprider {

	
	private static Pattern pTargetLink;
	private static int targetLinkGroup;
	private static Pattern pHelpLink;
	private static int helpLinkGroup;
	private static final Scheduler scheduler = new QueueScheduler();
	private static final VisitedQueue qc2 = new HashSetVisitedQueue();
	private static final VisitedQueue visitedQueue = new HashSetVisitedQueue();
	
	private Request request;
	private cn.sunxyz.sprider.processor.Processor process = new PageLinkProcessor();
	private JavaBeanManager beanManager;


	private Sprider(Request request) {
		super();
		this.request = request;
	}
	
	public static Sprider create(Request request) {
		return new Sprider(request);
	}

	public static Sprider create(String request) {
		return create(new Request(request));
	}

	public Sprider build(Class<?> clazz, Setter setter) {
		beanManager = JavaBeanManager.create(); 
		beanManager.init(clazz, setter);
		Annotation[] annotations = beanManager.getBean().getClassAnnotations();
		for (Annotation annotation : annotations) {
			if(annotation instanceof TargetLink){
				pTargetLink = Pattern.compile(((TargetLink)annotation).value());
				targetLinkGroup = ((TargetLink)annotation).group();
			}
			else if(annotation instanceof HelpLink){
				pHelpLink = Pattern.compile(((HelpLink)annotation).value());
				helpLinkGroup = ((HelpLink)annotation).group();
			}
		}
		return this;
	}
	
	public void go() {
		if(beanManager==null){
			new NullPointerException();
		}
		new Task().run(request);
	}

	//TODO 结构整理
	class Task {

		List<Request> qc = new ArrayList<>();


		String validateTargetLink(String url) {
			Matcher m = pTargetLink.matcher(url);
			if (m.find()) {
				// System.out.println(m.group(1));
				return m.group(targetLinkGroup);
			}
			return null;
		}
		
		String validateHelpLink(String url) {
			Matcher m = pHelpLink.matcher(url);
			if (m.find()) {
				// System.out.println(m.group(1));
				return m.group(helpLinkGroup);
			}
			return null;
		}

		void dowload(Request request) {
			DownLoader downLoader = new JsoupDownloader();
			Page page = downLoader.dowloader(request.getUrl());
			//解析html
			if(validateTargetLink(request.getUrl())!=null){
				beanManager.setter(page.getDocument());
			}
			process.process(page);
			// 添加进以访问队列
			visitedQueue.push(request);
			System.out.println("已访问： " + JSON.toJSON(request));
			this.fore(page);
		}

		// --------增加访问队列

		void fore(Page page) {
			List<Request> requests = page.getRequests();
			//TODO此处代码有待优化
			List<Request> targetLinkFilter = requests.parallelStream().filter(url -> pTargetLink.matcher(url.getUrl()).find())
					.map(url -> new Request(validateTargetLink(url.getUrl()))).collect(Collectors.toList());
			// 筛选查看是在url列表中存在
			// 不存在 添加入qc中 存在不添加
			this.qc1(targetLinkFilter);		
			if(pHelpLink!=null){
				List<Request> helpLinkFilter = requests.parallelStream().filter(url -> pHelpLink.matcher(url.getUrl()).find())
						.map(url -> new Request(validateHelpLink(url.getUrl()))).collect(Collectors.toList());
				this.qc1(helpLinkFilter);
			}
			this.push(qc);
			qc.clear();
		}

		void qc1(List<Request> urlsFilter) {
			for (Request request : urlsFilter) {
				if (!qc2(request)) {
					qc.add(request);
				}
			}
		}

		boolean qc2(Request request) {
			if (qc2.hasExisted(request)) {
				return true;
			} else {
				qc2.push(request);
				return false;
			}

		}

		void push(List<Request> requests) {
			for (Request request : requests) {
				// 如果没有访问 则添加待访问
				if (visitedQueue.hasExisted(request)) {
					System.out.println("添加：------------------------------------- " + JSON.toJSON(request));
					scheduler.push(request);
				}
			}
		}

		void run(Request request) {
			scheduler.push(request);
			while (scheduler.size() > 0) {
				Request pop = scheduler.pop();
				this.dowload(pop);
			}
		}
	}

}
