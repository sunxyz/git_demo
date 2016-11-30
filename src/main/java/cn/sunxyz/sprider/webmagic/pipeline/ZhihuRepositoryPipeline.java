package cn.sunxyz.sprider.webmagic.pipeline;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;
import cn.sunxyz.sprider.webmagic.repository.ZhihuRepository;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

@Component
public class ZhihuRepositoryPipeline implements PageModelPipeline<ZhihuInfo> {

	private final static Logger logger = LoggerFactory.getLogger(ZhihuRepositoryPipeline.class);
	
	@Autowired
	private ZhihuRepository repository;
	
	public void process(ZhihuInfo zhihu, Task task) {
		//logger.info(JSON.toJSONString(zhihu));
		int row = repository.add(zhihu);
		if (row < 1) {//删除已经存在的答案并更新新的数据
			repository.update(zhihu);
			repository.deleteAnswers(zhihu.getId());
		}
		List<String> answers = zhihu.getAnswerList();
		for (String string : answers) {
			repository.addAnswer(zhihu.getId(), string);
		}
		logger.info(System.out.format("zhihu: { questionId : %S }", zhihu.getId()).toString());
	}

}
