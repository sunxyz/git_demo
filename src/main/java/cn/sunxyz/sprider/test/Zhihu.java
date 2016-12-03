package cn.sunxyz.sprider.test;

import cn.sunxyz.sprider.builder.annotation.ExtractBy;
import cn.sunxyz.sprider.builder.annotation.TargetLink;

@TargetLink("(https://www.zhihu.com/question/\\d*)")
public class Zhihu {

	@ExtractBy("//title/text()")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
