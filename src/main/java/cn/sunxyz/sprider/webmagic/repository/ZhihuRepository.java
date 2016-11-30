package cn.sunxyz.sprider.webmagic.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import cn.sunxyz.sprider.webmagic.domain.ZhihuInfo;

public interface ZhihuRepository {
	
	@Insert("insert into zhihu_q(id,question,description,answer_num) values (#{id},#{question},#{description},#{answerNum})")
	int add(ZhihuInfo zhihu);
	
	@Update("update from zhihu_q set question = #{question} ,description = #{description},answer_num = #{answerNum}) where id = #{id}")
	int update(ZhihuInfo zhihu);
	
	@Delete("delete from zhihu_a where q_id = #{qid}")
	int deleteAnswers(@Param("qid") String qId);
	
	@Insert("insert into zhihu_a(q_id,answer) values (#{qid},#{answer})")
	int addAnswer(@Param("qid") String qId, @Param("answer") String answer);

}
