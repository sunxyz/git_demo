package cn.sunxyz.webcrawler.sample;

import cn.sunxyz.webcrawler.scheduler.Scheduler;

public class App {

	
	public static void main(String[] args) {
		new Scheduler().init(Zhihu.class, "https://www.zhihu.com/question/29073730").run();
	}
}
