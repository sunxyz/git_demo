package cn.sunxyz.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.bean.bean.JavaBeanField;
import cn.sunxyz.bean.setter.MapSetter;
import cn.sunxyz.bean.setter.Setter;
import cn.sunxyz.bean.utils.JavaBeanUtil;

public class JavaBeanManager {

	private JavaBeanManager() {

	}

	private static Logger logger = LoggerFactory.getLogger(JavaBeanManager.class);

	private Setter setter;

	private Class<?> clazz;

	private JavaBean bean;

	public static JavaBeanManager create() {
		return new JavaBeanManager();
	}

	public JavaBeanManager init(Class<?> clazz, Object... args) {
		this.init(clazz, new MapSetter(), args);
		return this;
	}

	public JavaBeanManager init(Class<?> clazz, Setter setter, Object... args) {
		this.clazz = clazz;
		this.setter = setter;
		this.initClassInfo();
		if (args != null) {
			this.setter(args);
		}
		return this;
	}

	private void initClassInfo() {
		List<Field> fields = JavaBeanUtil.getAllField(clazz);
		int size = fields.size();
		JavaBeanField[] beanFields = new JavaBeanField[size];
		for (int i = 0; i < size; i++) {
			Field field = fields.get(i);
			Annotation[] annotations = field.getAnnotations();
			beanFields[i] = new JavaBeanField(field, annotations);
		}
		logger.debug(JSON.toJSONString(beanFields));
		Object owner = JavaBeanUtil.newInstance(clazz);
		Annotation[] classAnnotation = clazz.getAnnotations();
		bean = new JavaBean(owner, beanFields, classAnnotation);
	}

	public void setter(Object... args) {
		setter.setter(bean, args);
	}

	public JavaBean getBean() {
		return bean;
	}

}
