package cn.sunxyz.sprider;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.pipeline.JsonFilePageModelPipeline;

@TargetUrl("https://book.douban.com/subject/\\w+")
@HelpUrl("https://book.douban.com/subject/\\w+")
public class GithubRepo {

    @ExtractBy(value = "//h1/span/text()", notNull = true)
    private String name;
    
    @ExtractBy(value = "//div[@id='interest_sectl']//strong[@class='ll rating_num']/text()", notNull = false)
    private String start;

    public static void main(String[] args) {
        OOSpider.create(Site.me().setSleepTime(1000)
                , new JsonFilePageModelPipeline(), GithubRepo.class)
                .addUrl("https://book.douban.com/subject/1013208/").thread(5).run();
    }
    
    
}
