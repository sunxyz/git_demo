package cn.sunxyz.sprider.builder;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.bean.bean.JavaBeanField;
import cn.sunxyz.bean.setter.Setter;
import cn.sunxyz.sprider.builder.annotation.ExtractBy;
import cn.sunxyz.sprider.builder.annotation.ExtractBy.ExtractType;
import us.codecraft.xsoup.Xsoup;

public class ExtractSetter implements Setter {

	@Override
	public void setter(JavaBean bean, Object... args) {
		if(args.length==0){
			return;
		}
		System.out.println(args.toString());
		Document document = (Document)args[0];
		Object owner = bean.getOwner();
		JavaBeanField[] beanFields =bean.getBeanFields();
		for (JavaBeanField beanField : beanFields) {
			Annotation[] annotations = beanField.getAnnotations();
			for (Annotation annotation : annotations) {
				if(annotation instanceof ExtractBy){
					ExtractBy extractBy = (ExtractBy)annotation;
					String query = extractBy.value();
					ExtractType type = extractBy.type();
					List<String> filedvalue = null;
					switch (type) {
					case SELECT:
						filedvalue = document.select(query).parallelStream().map(e->e.toString()).collect(Collectors.toList());
						break;
					case XSOUP:
						filedvalue = Xsoup.select(document, query).list();
						break;

					default:
						break;
					}
					//此处需要设值
					System.out.println(JSON.toJSONString(filedvalue));
				}
			}
		}
	}

}
