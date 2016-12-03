package cn.sunxyz.sprider.test;

import org.junit.Test;

import cn.sunxyz.sprider.Sprider;
import cn.sunxyz.sprider.builder.ExtractSetter;

public class ZhihuTest {

	@Test
	public void test(){
		Sprider.create("https://www.zhihu.com/explore").build(Zhihu.class, new ExtractSetter()).go();
	}
	
}
