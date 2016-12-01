package cn.sunxyz.sprider.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;
import cn.sunxyz.sprider.webmagic.pipeline.ZhihuInfoRepositoryPipeline;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

@Component
public class AppCrawler {

	@Autowired
	private ZhihuInfoRepositoryPipeline pipeline;

	public void crawl() {
		OOSpider.create(Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36"),pipeline, ZhihuInfo.class)
				.scheduler(new FileCacheQueueScheduler("/data/temp/webmagic/cache/"))
				.addUrl("https://www.zhihu.com/question/46220469/answer/133834559").thread(20).run();
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:/spring/application*.xml");
		final AppCrawler appCrawler = applicationContext.getBean(AppCrawler.class);
		appCrawler.crawl();
	}

}
