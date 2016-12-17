package cn.sunxyz.webcrawler;

import java.util.Set;

import cn.sunxyz.webcrawler.Request.RequestLinkType;
import cn.sunxyz.webcrawler.builder.BuilderAdapter;
import cn.sunxyz.webcrawler.builder.OwnerBuilder;
import cn.sunxyz.webcrawler.download.DownLoader;
import cn.sunxyz.webcrawler.download.JsoupDownloader;
import cn.sunxyz.webcrawler.parser.link.LinksFilter;
import cn.sunxyz.webcrawler.pipeline.DeafultPipeLine;
import cn.sunxyz.webcrawler.pipeline.Pipeline;
import cn.sunxyz.webcrawler.scheduler.QueueScheduler;
import cn.sunxyz.webcrawler.scheduler.Scheduler;
import cn.sunxyz.webcrawler.scheduler.cache.Cache;

public class Sprider {

	private DownLoader downLoader;// 下载器

	private LinksFilter linksFilter;// 链接筛选匹配

	private Scheduler scheduler;// 队列管理

	private BuilderAdapter builderAdapter; // 对象构建

	private Pipeline<Object> pipeline;// 对象信息 管道

	private Configer configer;

	{
		downLoader = new JsoupDownloader();
		pipeline = new DeafultPipeLine<>();
		scheduler = new QueueScheduler();
		configer = new Configer();
	}

	public Sprider init(Class<?> clazz, String... urls) {
		return init(clazz, pipeline, urls);
	}

	public Sprider init(Class<?> clazz, Pipeline<Object> pipeline, String... urls) {
		builderAdapter = new BuilderAdapter(clazz, new OwnerBuilder());
		linksFilter = new LinksFilter(builderAdapter);
		this.pipeline = pipeline;
		scheduler.push(urls);
		return this;
	}

	public Configer configer() {
		return configer;
	}

	public void run() {
		this.download();
	}

	public void run(FetchType fetchType) {
		if (fetchType == null) {
			throw new NullPointerException();
		}
		this.download(fetchType);
	}

	private void download() {
		while (scheduler.size() > 0) {
			Request request = scheduler.pop();
			Page page = downLoader.dowloader(request);
			Set<Request> requests = linksFilter.getRequests(page);
			if (!RequestLinkType.HELP.equals(request.getMethod())) {
				Object owner = builderAdapter.buildOwner(page);// 注值
				pipeline.process(owner, new Site(page, requests));// 流处理
			}
			scheduler.push(requests);
		}
	}

	private void download(FetchType fetchType) {
		switch (fetchType) {
		case Eager:
			this.download();
			break;
		default:
			while (scheduler.size() > 0) {
				Request request = scheduler.pop();
				Page page = downLoader.dowloader(request);
				if (!RequestLinkType.HELP.equals(request.getMethod())) {
					Object owner = builderAdapter.buildOwner(page);// 注值
					pipeline.process(owner, new Site(page));// 流处理
				}
			}
			break;
		}
	}

	public class Configer {

		private Sprider that = Sprider.this;

		public Configer setCache(Cache cache) {
			that.scheduler.setCache(cache);
			return this;
		}

		public Configer setDownLoader(DownLoader downLoader) {
			that.downLoader = downLoader;
			return this;
		}

		public Configer setScheduler(Scheduler scheduler) {
			that.scheduler = scheduler;
			return this;
		}

		public Configer setPipeline(Pipeline<Object> pipeline) {
			that.pipeline = pipeline;
			return this;
		}

		public Configer addRequest(String... requests) {
			that.scheduler.push(requests);
			return this;
		}

		public void run() {
			that.run();
		}

		public void run(FetchType fetchType) {
			that.run(fetchType);
		}
	}

	public enum FetchType {
		Eager, Lazy
	}

}
