package cn.sunxyz.sprider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.sprider.download.DownLoader;
import cn.sunxyz.sprider.download.JsoupDownloader;
import cn.sunxyz.sprider.processor.Page;
import cn.sunxyz.sprider.processor.PageLinkProcessor;
import cn.sunxyz.sprider.scheduler.QueueScheduler;
import cn.sunxyz.sprider.scheduler.Scheduler;
import cn.sunxyz.sprider.scheduler.component.HashSetVisitedQueue;
import cn.sunxyz.sprider.scheduler.component.VisitedQueue;

public class Test {

	Pattern pl = Pattern.compile("(https://www.zhihu.com/question/\\d*)");
	Pattern pd = Pattern.compile("https://www.zhihu.com/topic/\\w*");
	Scheduler scheduler = new QueueScheduler(); 
	List<Request> qc = new ArrayList<>();
	VisitedQueue qc2 = new HashSetVisitedQueue();
	VisitedQueue visitedQueue = new HashSetVisitedQueue();
	
	@org.junit.Test
	public void test(){
		DownLoader downLoader = new JsoupDownloader();
		Page page = downLoader.dowloader("https://www.zhihu.com/explore");
		cn.sunxyz.sprider.processor.Process process = new PageLinkProcessor();
		process.process(page);
		List<Request> requests = page.getRequests();
		System.out.println(JSON.toJSON(requests));
		List<String> urlsFilter = requests.parallelStream().filter(url -> pl.matcher(url.getUrl()).find()).map(url-> validate(url.getUrl())).collect(Collectors.toList());
		System.out.println(JSON.toJSON(urlsFilter));
		List<Request> urlsFilter2 = requests.parallelStream().filter(url -> pd.matcher(url.getUrl()).find()).collect(Collectors.toList());
		System.out.println(JSON.toJSON(urlsFilter2));
	}
	
	public String validate(String url){
		Matcher m = pl.matcher(url);
		if(m.find()){
//			System.out.println(m.group(1));
			return m.group(1);
		}
		return null;
	}
	
	public void dowload(Request request){
		DownLoader downLoader = new JsoupDownloader();
		Page page = downLoader.dowloader(request.getUrl());
		cn.sunxyz.sprider.processor.Process process = new PageLinkProcessor();
		process.process(page);
		//添加进以访问队列
		visitedQueue.push(request);
		System.out.println("已访问： "+JSON.toJSON(request));
		this.fore(page);
	}
	
	//--------增加访问队列
	
	public void fore(Page page){
		List<Request> requests = page.getRequests();
//		System.out.println(JSON.toJSON(requests));
		List<Request> urlsFilter = requests.parallelStream().filter(url -> pl.matcher(url.getUrl()).find()).map(url -> new Request(validate(url.getUrl()))).collect(Collectors.toList());
		//筛选查看是在url列表中存在
		//不存在 添加入qc中 存在不添加
		this.qc1(urlsFilter);
		this.push(qc);
		qc.clear();
	}
	
	public void qc1(List<Request> urlsFilter){
		for (Request request : urlsFilter) {
			if(!qc2(request)){
				qc.add(request);
			}
		}
	}
	
	public boolean qc2(Request request){
		if(qc2.hasExisted(request)){
			return true;
		}else{
			qc2.push(request);
			return false;
		}
		
	}
	
	public void push(List<Request> requests){
		for (Request request : requests) {
			//如果没有访问 则添加待访问
			if(visitedQueue.hasExisted(request)){
				System.out.println("添加：------------------------------------- "+JSON.toJSON(request));
				scheduler.push(request);
			}
		}
	}
	
	@org.junit.Test
	public void test2(){
		scheduler.push(new Request("https://www.zhihu.com/explore"));
		while(scheduler.size()>0){
			Request request = scheduler.pop();
			this.dowload(request);
		}
		
	}
}
