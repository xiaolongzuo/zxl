package cn.zxl.hbase.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.hbase.orm.util.HBaseUtil;

public class CreateTablePostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

	private static final int TIME_TO_LIVE = -1;

	private ApplicationContext applicationContext;

	private String[] hbaseDomainPackages;

	public void setHbaseDomainPackages(String[] hbaseDomainPackages) {
		this.hbaseDomainPackages = hbaseDomainPackages;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Set<Class<?>> classes = ReflectUtil.findAllClasses(applicationContext.getClassLoader(), hbaseDomainPackages);
		for (Class<?> clazz : classes) {
			if (!HBaseUtil.isTable(clazz)) {
				continue;
			}
			List<String> columnFamiliesList = new ArrayList<String>();
			for (Field field : ReflectUtil.getAllFields(clazz)) {
				if (HBaseUtil.isFamily(field)) {
					columnFamiliesList.add(field.getName());
				}
			}
			Object timeToLiveBean = applicationContext.getBean("timeToLive");
			Integer timeToLive = TIME_TO_LIVE;
			if (timeToLiveBean != null) {
				timeToLive = Integer.valueOf(timeToLiveBean.toString());
			}
			HBaseFactory.createTable(clazz.getSimpleName(), ArrayUtil.listToArray(columnFamiliesList), timeToLive);
		}
	}

}
