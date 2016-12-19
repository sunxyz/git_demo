package cn.sunxyz.webcrawler.builder;

import cn.sunxyz.bean.JavaBeanManager;
import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.webcrawler.Page;

public class OwnerBuilderAdapter {
	
	private JavaBeanManager beanManager;
	
	private Builder builder;
	
	private OwnerBuilderAdapter(){
		beanManager = JavaBeanManager.create();
	}
	
	public OwnerBuilderAdapter(Class<?> clazz, Builder builder){
		this();
		this.init(clazz, builder);
	}
	
	public static OwnerBuilderAdapter create() {
		return new OwnerBuilderAdapter();
	}
	
	public void init(Class<?> clazz, Builder builder){
		this.builder = builder;
		beanManager.init(clazz);
	}
	
	public Object buildOwner(Page page){
		JavaBean bean = this.getNewBean();
		builder.buildOwnerByPage(bean, page);
		return bean.getOwner();
	}
	
	public JavaBean getNewBean(){
		return beanManager.getJavaBean();
	}

	public void setBuilder(Builder builder) {
		this.builder = builder;
	}
	
	

}
