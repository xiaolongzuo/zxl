package cn.zxl.orm.common.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class OrmCommonNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("mul-common-base-service", new MulCommonBaseServiceParser());
		registerBeanDefinitionParser("string", new StringParser());
		registerBeanDefinitionParser("string-array", new StringArrayParser());
		registerBeanDefinitionParser("properties", new PropertiesParser());
	}

}
