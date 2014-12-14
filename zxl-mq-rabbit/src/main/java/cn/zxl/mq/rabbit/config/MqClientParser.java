package cn.zxl.mq.rabbit.config;

import org.springframework.amqp.rabbit.config.NamespaceUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.zxl.mq.rabbit.MQClient;

public class MqClientParser extends MqBeanParser {

	private static final Class<MQClient> CLASS = MQClient.class;

	private static final String ROUTING_KEY_NAME = "routing-key";

	private static final String EXCHANGE_NAME = "exchange";

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return CLASS;
	}

	@Override
	protected void doParseOther(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (!NamespaceUtils.isAttributeDefined(element, ID_ATTRIBUTE) || !NamespaceUtils.isAttributeDefined(element, ROUTING_KEY_NAME)) {
			parserContext.getReaderContext().error("MqClientParser must have id and routing-key", element);
		}
		builder.getBeanDefinition().setBeanClass(CLASS);
		builder.addPropertyValue("routingKey", element.getAttribute(ROUTING_KEY_NAME));
		if (element.hasAttribute(EXCHANGE_NAME)) {
			builder.addPropertyValue("exchange", element.getAttribute(EXCHANGE_NAME));
		}
	}

}
