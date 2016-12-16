package cn.sunxyz.webcrawler.queue;

public interface Crawl<R>{

	void push(R request);

	R pop();

	int size();

}
