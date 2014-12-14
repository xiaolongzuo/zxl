package cn.zxl.orm.common.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.spring.StringArrayFactoryBean;

public class StringArrayParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return String[].class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (StringUtil.isEmpty(element.getAttribute(ID_ATTRIBUTE))) {
			parserContext.getReaderContext().error("Properties must have id and value", element);
		}
		builder.getBeanDefinition().setBeanClass(StringArrayFactoryBean.class);
		NodeList nodeList = element.getElementsByTagName("value");
		int length = nodeList.getLength();
		String[] array = new String[length];
		for (int i = 0; i < nodeList.getLength(); i++) {
			array[i] = nodeList.item(i).getTextContent();
		}
		builder.addPropertyValue("values", array);
	}

}
