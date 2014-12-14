package cn.zxl.orm.common.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.zxl.common.ArrayUtil;

public class StringArrayFactoryBean implements FactoryBean<String[]>, InitializingBean {

	private String[] values;

	public void setValues(String[] values) {
		this.values = values;
	}

	@Override
	public String[] getObject() throws Exception {
		return values;
	}

	@Override
	public Class<?> getObjectType() {
		return String[].class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (ArrayUtil.isEmpty(values)) {
			throw new RuntimeException("values is required!");
		}
	}

}
