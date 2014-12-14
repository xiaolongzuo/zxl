package cn.zxl.orm.common.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import cn.zxl.common.StringUtil;

public class StringFactoryBean implements FactoryBean<String>, InitializingBean {

	private String string;

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public String getObject() throws Exception {
		return string;
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (StringUtil.isNull(string)) {
			throw new RuntimeException("string is required!");
		}
	}

}
