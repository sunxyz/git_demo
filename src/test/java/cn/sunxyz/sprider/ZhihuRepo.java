package cn.sunxyz.sprider;

import java.util.List;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("https://www.zhihu.com/question/\\w+")
@HelpUrl("https://www.zhihu.com/question/\\w+")
public class ZhihuRepo {

	@ExtractBy(value = "//head/title/text()", notNull = true)
	private String question;

	@ExtractBy(value = "//div[@id='zh-question-detail']/div[@class='zm-editable-content']/text()", notNull = false)
	private String description;

	@ExtractBy(value = "//h3/@data-num", notNull = false)
	private String answerNum;
	
	@ExtractBy(value = "//div[@class='zm-editable-content clearfix']/text()", notNull = false)
	private List<String> test;

	public static void main(String[] args) {
		OOSpider.create(Site.me().setSleepTime(1000), new ConsolePageModelPipeline(), ZhihuRepo.class)
				.addUrl("https://www.zhihu.com/explore").thread(5).run();
	}

}
