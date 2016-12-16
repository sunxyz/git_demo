package cn.sunxyz.webcrawler.pipeline;

import cn.sunxyz.webcrawler.Site;

public interface pipeline<T> {
	
	void process(T t, Site site);

}
