package cn.zxl.orm.common.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.spring.StringFactoryBean;

public class StringParser extends AbstractSingleBeanDefinitionParser {

	private static final String VALUE_NAME = "value";

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return String.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (StringUtil.isEmpty(element.getAttribute(ID_ATTRIBUTE)) || StringUtil.isEmpty(element.getAttribute(VALUE_NAME))) {
			parserContext.getReaderContext().error("Properties must have id and value", element);
		}
		builder.getBeanDefinition().setBeanClass(StringFactoryBean.class);
		builder.addPropertyValue("string", element.getAttribute(VALUE_NAME));
	}

}
