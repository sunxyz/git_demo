package cn.sunxyz.webcrawler.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.webcrawler.Site;

public class DeafultPipeLine<T> implements pipeline<T> {

	private static Logger logger = LoggerFactory.getLogger(DeafultPipeLine.class);
	
	@Override
	public void process(T t, Site site) {
		logger.debug("---===---");
		logger.debug(JSON.toJSONString(t));
	}

}
