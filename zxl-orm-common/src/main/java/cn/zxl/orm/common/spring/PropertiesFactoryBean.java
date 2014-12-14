package cn.zxl.orm.common.spring;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.zxl.common.BeanUtil;

public class PropertiesFactoryBean implements FactoryBean<Properties>, InitializingBean {

	private Properties properties;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Properties getObject() throws Exception {
		return properties;
	}

	@Override
	public Class<?> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (BeanUtil.isEmpty(properties)) {
			throw new RuntimeException("properties is required!");
		}
	}

}
