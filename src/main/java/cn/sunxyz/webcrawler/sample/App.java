package cn.sunxyz.webcrawler.sample;

import cn.sunxyz.webcrawler.Sprider;
import cn.sunxyz.webcrawler.Sprider.FetchType;

public class App {

	public static void main(String[] args) {		
		new Sprider().init(Zhihu.class, "https://www.zhihu.com/question/29073730").configer().run(FetchType.Eager);
	}
}
