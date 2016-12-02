package cn.sunxyz.sprider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.sprider.download.DownLoader;
import cn.sunxyz.sprider.download.JsoupDownloader;
import cn.sunxyz.sprider.processor.Page;
import cn.sunxyz.sprider.processor.PageLinkProcessor;
import cn.sunxyz.sprider.scheduler.QueueScheduler;
import cn.sunxyz.sprider.scheduler.Scheduler;
import cn.sunxyz.sprider.scheduler.component.HashSetVisitedQueue;
import cn.sunxyz.sprider.scheduler.component.VisitedQueue;

public class Sprider {

	private static Request request;
	private static Pattern pl = Pattern.compile("(https://www.zhihu.com/question/\\d*)");
//	private static Pattern pd = Pattern.compile("https://www.zhihu.com/topic/\\w*");
	private static final Scheduler scheduler = new QueueScheduler();
	private static final VisitedQueue qc2 = new HashSetVisitedQueue();
	private static final VisitedQueue visitedQueue = new HashSetVisitedQueue();

	public static Sprider create(Request request) {
		return new Sprider(request);
	}

	public static Sprider create(String request) {
		return new Sprider(new Request(request));
	}

	public Sprider() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("static-access")
	public Sprider(Request request) {
		this.request = request;
	}

	public void go() {
		new Task().run(request);
	}

	class Task {

		List<Request> qc = new ArrayList<>();


		String validate(String url) {
			Matcher m = pl.matcher(url);
			if (m.find()) {
				// System.out.println(m.group(1));
				return m.group(1);
			}
			return null;
		}

		void dowload(Request request) {
			DownLoader downLoader = new JsoupDownloader();
			Page page = downLoader.dowloader(request.getUrl());
			cn.sunxyz.sprider.processor.Process process = new PageLinkProcessor();
			process.process(page);
			// 添加进以访问队列
			visitedQueue.push(request);
			System.out.println("已访问： " + JSON.toJSON(request));
			this.fore(page);
		}

		// --------增加访问队列

		void fore(Page page) {
			List<Request> requests = page.getRequests();
			// System.out.println(JSON.toJSON(requests));
			List<Request> urlsFilter = requests.parallelStream().filter(url -> pl.matcher(url.getUrl()).find())
					.map(url -> new Request(validate(url.getUrl()))).collect(Collectors.toList());
			// 筛选查看是在url列表中存在
			// 不存在 添加入qc中 存在不添加
			this.qc1(urlsFilter);
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
