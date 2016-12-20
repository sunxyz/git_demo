package cn.sunxyz.webcrawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.sunxyz.webcrawler.builder.Builder;
import cn.sunxyz.webcrawler.download.DownLoader;
import cn.sunxyz.webcrawler.pipeline.Pipeline;
import cn.sunxyz.webcrawler.scheduler.Scheduler;
import cn.sunxyz.webcrawler.scheduler.cache.Cache;

/**
 * TODO 此处 WSprider 是否线程安全
 */
public final class SpriderManager {

	private static SpriderManager manager;

	private static Configer configer;

	private static int nThreads = 10;

	private static int task = 10;

	private static ExecutorService executor;

	private static WSprider[] wSpriders;

	static {
		manager = new SpriderManager();
		configer = new Configer();
	}

	private SpriderManager() {

	}

	public static SpriderManager create(Class<?> clazz, String... urls) {
		// 创建任务
		WSprider.scheduler.push(urls);
		wSpriders = new WSprider[task];
		for (int i = 0; i < nThreads; i++) {
			wSpriders[i] = new WSprider(clazz);
		}
		return manager;
	}

	public Configer configer() {
		return configer;
	}

	@SuppressWarnings("static-access")
	public void run() {
		// 执行任务
		executor = Executors.newFixedThreadPool(nThreads);
		for (int i = 0; i < nThreads; i++) {
			WSprider wSprider = wSpriders[i];
			try {
				Thread.currentThread().sleep(WSprider.sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executor.execute(wSprider);
		}
		executor.shutdown();
	}

	public static class Configer {

		public Configer setThread(int size) {
			SpriderManager.nThreads = size;
			return this;
		}

		public Configer setTask(int task) {
			SpriderManager.task = task;
			return this;
		}

		public Configer setSleep(int sleep) {
			WSprider.sleep = sleep;
			return this;
		}

		public void run() {
			manager.run();
		}


		// TODO 此处是否让任务持有相同对象的引用
		private Cache cache;

		public Configer setDownLoader(DownLoader downLoader) {
			WSprider.downLoader = downLoader;
			return this;
		}

		public Configer setScheduler(Scheduler scheduler) {
			WSprider.scheduler = scheduler;
			WSprider.scheduler.setCache(cache);
			return this;
		}

		public Configer setCache(Cache cache) {
			this.cache = cache;
			WSprider.scheduler.setCache(cache);
			return this;
		}

		public Configer setBuilder(Builder builder) {
			WSprider.builderAdapter.setBuilder(builder);
			return this;
		}

		@SuppressWarnings("unchecked")
		public Configer setPipeline(Pipeline<? extends Object> pipeline) {
			WSprider.pipeline = (Pipeline<Object>) pipeline;
			return this;
		}

		public Configer addRequest(String... requests) {
			WSprider.scheduler.push(requests);
			return this;
		}
	}
}
