package cn.sunxyz.sprider.webmagic.domain;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@TargetUrl("https://www.zhihu.com/question/\\w+")
@HelpUrl("https://www.zhihu.com/question/\\w+")
public class ZhihuInfo implements AfterExtractor {

	private String id;

	@ExtractBy(value = "//head/title/text()", notNull = true)
	private String question;

	@ExtractBy(value = "//div[@id='zh-question-detail']/div[@class='zm-editable-content']/text()", notNull = false)
	private String description;

	@ExtractBy(value = "//h3[@data-num]/text()", notNull = false)
	private String answerNum;

	@ExtractBy(value = "//div[@class='zm-editable-content clearfix']/text()", notNull = false)
	private List<String> answerList;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(String answerNum) {
		this.answerNum = answerNum;
	}

	public List<String> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void afterProcess(Page page) {
		String url = page.getRequest().getUrl();
		int last = url.lastIndexOf("/");
		this.setId(url.substring(++last));
	}

}
