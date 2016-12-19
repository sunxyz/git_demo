package cn.sunxyz.webcrawler.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.webcrawler.Site;
import cn.sunxyz.webcrawler.pipeline.DeafultPipeLine;

public class ZhihuPipeline extends DeafultPipeLine<Zhihu>{

	private static Logger logger = LoggerFactory.getLogger(ZhihuPipeline.class);
	
	@Override
	public void process(Zhihu t, Site site) {
		logger.debug(JSON.toJSONString(t));
		logger.debug("dowload {}", site.getPage().getRequest().getUrl());
	}

}
