package cn.zxl.mq.rabbit.config;

import org.springframework.amqp.rabbit.config.NamespaceUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.zxl.mq.rabbit.MQServer;

public class MqServerParser extends MqBeanParser {

	private static final Class<MQServer> CLASS = MQServer.class;

	private static final String TARGET_OBJECT_NAME = "target-object";

	private static final String TARGET_METHOD_NAME = "target-method";

	private static final String QUEUE_NAME_NAME = "queue-name";

	private static final String RECEIVE_MESSAGE_CLASS_NAME_NAME = "receive-message-class-name";

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
		if (!NamespaceUtils.isAttributeDefined(element, ID_ATTRIBUTE) || !NamespaceUtils.isAttributeDefined(element, QUEUE_NAME_NAME) || !NamespaceUtils.isAttributeDefined(element, TARGET_OBJECT_NAME) || !NamespaceUtils.isAttributeDefined(element, TARGET_METHOD_NAME)) {
			parserContext.getReaderContext().error("HibernateTransactionManager must have id,queue-name,target-object and target-method", element);
		}
		builder.getBeanDefinition().setBeanClass(CLASS);
		builder.addPropertyReference("targetObject", element.getAttribute(TARGET_OBJECT_NAME));
		builder.addPropertyValue("targetMethod", element.getAttribute(TARGET_METHOD_NAME));
		builder.addPropertyValue("queueName", element.getAttribute(QUEUE_NAME_NAME));
		if (element.hasAttribute(RECEIVE_MESSAGE_CLASS_NAME_NAME)) {
			builder.addPropertyValue("receiveMessageClassName", element.getAttribute(RECEIVE_MESSAGE_CLASS_NAME_NAME));
		}
		parserContext.getDelegate().parsePropertyElements(element, builder.getBeanDefinition());
	}

}
