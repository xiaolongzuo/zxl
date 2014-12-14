package cn.zxl.orm.common.config;

import java.util.Properties;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.spring.PropertiesFactoryBean;

public class PropertiesParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return Properties.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (StringUtil.isEmpty(element.getAttribute(ID_ATTRIBUTE))) {
			parserContext.getReaderContext().error("Properties must have id", element);
		}
		builder.getBeanDefinition().setBeanClass(PropertiesFactoryBean.class);
		builder.addPropertyValue("properties", parserContext.getDelegate().parseValueElement(element, null));
	}

}
