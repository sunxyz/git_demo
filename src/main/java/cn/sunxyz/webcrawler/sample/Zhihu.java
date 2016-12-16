package cn.sunxyz.webcrawler.sample;

import java.util.List;

import cn.sunxyz.webcrawler.builder.annotation.ExtractBy;
import cn.sunxyz.webcrawler.builder.annotation.TargetLink;

@TargetLink("(https://www.zhihu.com/question/\\d*)")
public class Zhihu {

	@ExtractBy("//title/text()")
	private String title;
	
	@ExtractBy(value = "//div[@class='zm-editable-content clearfix']/text()")
	private List<String> answerList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
	}
	
	

}
