package cn.sunxyz.sprider.webmagic.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;
import cn.sunxyz.sprider.webmagic.repository.ZhihuInfoRepository;

@Service
public class ZhihuInfoService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ZhihuInfoRepository repository;

	public void save(ZhihuInfo zhihuInfo) {
		// logger.info(JSON.toJSONString(zhihu));
		String anum = zhihuInfo.getAnswerNum();
		int index = anum == null ? 0 : anum.indexOf("个回答");
		if (index > 0) {
			zhihuInfo.setaNum(Integer.valueOf(zhihuInfo.getAnswerNum().substring(0, index).trim()));
		}
		int row = repository.add(zhihuInfo);
		if (row < 1) {// 删除已经存在的答案并更新新的数据
			repository.update(zhihuInfo);
			repository.deleteAnswers(zhihuInfo.getId());
		}
		List<String> answers = zhihuInfo.getAnswerList();
		for (String string : answers) {
			repository.addAnswer(zhihuInfo.getId(), string);
		}
		logger.info("zhihuInfo questionId : {}", zhihuInfo.getId());
	}

}
