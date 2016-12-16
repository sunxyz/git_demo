package cn.sunxyz.webcrawler.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.bean.bean.JavaBeanField;
import cn.sunxyz.bean.setter.Setter;
import cn.sunxyz.bean.utils.JavaBeanUtil;
import cn.sunxyz.webcrawler.builder.annotation.ExtractBy;
import us.codecraft.xsoup.Xsoup;

public class Builder implements Setter {

	private static Logger logger = LoggerFactory.getLogger(Setter.class);

	@Override
	public void setter(JavaBean bean, Object... args) {
		Object owner = bean.getOwner();
		JavaBeanField[] beanFields = bean.getBeanFields();
		for (Object object : args) {
			if (object instanceof Document) {
				Document document = (Document) object;
				logger.debug("------------");
				for (JavaBeanField beanField : beanFields) {
					Annotation[] annotations = beanField.getAnnotations();
					for (Annotation annotation : annotations) {
						if (annotation instanceof ExtractBy) {
							List<String> result = null;
							ExtractBy extractBy = (ExtractBy) annotation;
							String match = extractBy.value();
							switch (extractBy.type()) {
							case XSOUP:
								result = Xsoup.compile(match).evaluate(document).list();
								break;
							case SELECT:
								result = document.select(match).stream().map(e -> e.toString()).collect(Collectors.toList());
								break;
							default:
								break;
							}
							Field field = beanField.getField();
							setMethod(owner, field, result);
							logger.debug(JSON.toJSONString(owner));
						}
					}
				}
			}
		}
	}

	private void setMethod(Object owner, Field field, List<?> values) {
		if (!values.isEmpty()) {
			Class<?> fClazz = field.getType();
			// TODO 只实现了基本的容器
			Class<?> gtClazz = getClass(field.getGenericType(), 0);
			if (fClazz == Set.class) {
				Set<?> set = values.stream().map(item -> getValue(gtClazz, item)).collect(Collectors.toSet());
				JavaBeanUtil.setMethod(owner, field, set);
			} else if (fClazz == List.class) {
				List<?> list = values.stream().map(item -> getValue(gtClazz, item)).collect(Collectors.toList());
				JavaBeanUtil.setMethod(owner, field, list);
			} else if (fClazz == Collection.class) {
				Collection<?> list = values.stream().map(item -> getValue(gtClazz, item)).collect(Collectors.toList());
				JavaBeanUtil.setMethod(owner, field, list);
			} else if (fClazz == Map.class) {
				// TODO 暂时不考虑实现
			} else {
				JavaBeanUtil.setMethod(owner, field, getValue(field, values.get(0)));
			}
		}
	}

	// 非集合数据
	private Object getValue(Field field, Object obj) {
		Class<?> fieldClazz = field.getType();
		return getValue(fieldClazz, obj);
	}

	private Object getValue(Class<?> fieldClazz, Object obj) {
		String item = obj.toString();
		if (fieldClazz == FieldType.BYTE.getBaseClazz() || fieldClazz == FieldType.BYTE.getPackClazz()) {
			logger.debug("BYTE");
			return Byte.valueOf(item);
		} else if (fieldClazz == FieldType.INTEGER.getBaseClazz() || fieldClazz == FieldType.INTEGER.getPackClazz()) {
			logger.debug("INTEGER");
			return Integer.valueOf(item);
		} else if (fieldClazz == FieldType.SHORT.getBaseClazz() || fieldClazz == FieldType.SHORT.getPackClazz()) {
			logger.debug("SHORT");
			return Short.valueOf(item);
		} else if (fieldClazz == FieldType.LONG.getBaseClazz() || fieldClazz == FieldType.LONG.getPackClazz()) {
			logger.debug("LONG");
			return Long.valueOf(item);
		} else if (fieldClazz == FieldType.BOOLEAN.getBaseClazz() || fieldClazz == FieldType.BOOLEAN.getPackClazz()) {
			logger.debug("BOOLEAN");
			return Boolean.valueOf(item);
		} else if (fieldClazz == FieldType.CHAR.getBaseClazz() || fieldClazz == FieldType.CHAR.getPackClazz()) {
			logger.debug("CHAR");
			return Character.toChars(0);
		} else if (fieldClazz == FieldType.FLOAT.getBaseClazz() || fieldClazz == FieldType.FLOAT.getPackClazz()) {
			logger.debug("FLOAT");
			return Float.valueOf(item);
		} else if (fieldClazz == FieldType.DOUBLE.getBaseClazz() || fieldClazz == FieldType.DOUBLE.getPackClazz()) {
			logger.debug("DOUBLE");
			return Double.valueOf(item);
		} else if (fieldClazz == FieldType.STRING.getBaseClazz() || fieldClazz == FieldType.STRING.getPackClazz()) {
			logger.debug("STRING");
			return item;
		} else {
			return obj;
		}

	}

	/**
	 * 
	 * 得到泛型类对象
	 * 
	 * @param type
	 * @param i
	 * @return Class
	 */
	@SuppressWarnings("rawtypes")
	protected static Class getClass(Type type, int i) {
		if (type instanceof ParameterizedType) { // 处理泛型类型
			return getGenericClass((ParameterizedType) type, i);
		} else if (type instanceof TypeVariable) {
			return (Class) getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
		} else {// class本身也是type，强制转型
			return (Class) type;
		}
	}

	@SuppressWarnings("rawtypes")
	private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
		Object genericClass = parameterizedType.getActualTypeArguments()[i];
		if (genericClass instanceof ParameterizedType) { // 处理多级泛型
			return (Class) ((ParameterizedType) genericClass).getRawType();
		} else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
			return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
		} else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
			return (Class) getClass(((TypeVariable) genericClass).getBounds()[0], 0);
		} else {
			return (Class) genericClass;
		}
	}

	public enum FieldType {
		BYTE(byte.class, Byte.class), INTEGER(int.class, Integer.class), SHORT(short.class, Short.class), LONG(
				long.class, Long.class), BOOLEAN(boolean.class, Boolean.class), CHAR(char.class,
						Character.class), FLOAT(float.class,
								Float.class), DOUBLE(double.class, Double.class), STRING(String.class, String.class);

		private Class<?> baseClazz;
		private Class<?> packClazz;

		FieldType(Class<?> baseClazz, Class<?> packClazz) {
			this.baseClazz = baseClazz;
			this.packClazz = packClazz;
		}

		public Class<?> getBaseClazz() {
			return baseClazz;
		}

		public Class<?> getPackClazz() {
			return packClazz;
		}
	}

}
