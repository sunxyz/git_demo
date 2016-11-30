package cn.sunxyz.sprider.webmagic.pipeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;
import cn.sunxyz.sprider.webmagic.service.ZhihuInfoService;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

@Component
public class ZhihuInfoRepositoryPipeline implements PageModelPipeline<ZhihuInfo> {

	@Autowired
	private ZhihuInfoService zhihuInfoService;

	public void process(ZhihuInfo zhihuInfo, Task task) {
		zhihuInfoService.save(zhihuInfo);
	}

}
