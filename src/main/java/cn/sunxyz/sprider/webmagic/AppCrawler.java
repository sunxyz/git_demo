package cn.sunxyz.sprider.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;
import cn.sunxyz.sprider.webmagic.pipeline.ZhihuRepositoryPipeline;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

@Component
public class AppCrawler {

	@Autowired
	private ZhihuRepositoryPipeline pipeline;

	public void crawl() {
		OOSpider.create(
				Site.me().setUserAgent(
						"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36"),
				pipeline, ZhihuInfo.class).addUrl("https://www.zhihu.com/explore").thread(5).run();
	}

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:/spring/application*.xml");
		final AppCrawler appCrawler = applicationContext.getBean(AppCrawler.class);
		appCrawler.crawl();
	}

}
