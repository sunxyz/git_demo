package cn.sunxyz.webcrawler.builder;

import cn.sunxyz.bean.JavaBeanManager;
import cn.sunxyz.bean.bean.JavaBean;
import cn.sunxyz.webcrawler.Page;

public class BuilderAdapter {
	
	private JavaBeanManager beanManager;
	
	private Builder builder;
	
	private BuilderAdapter(){
		beanManager = JavaBeanManager.create();
	}
	
	public BuilderAdapter(Class<?> clazz, Builder builder){
		this();
		this.init(clazz, builder);
	}
	
	public static BuilderAdapter create() {
		return new BuilderAdapter();
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

}
