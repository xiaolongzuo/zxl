package cn.zxl.mq.rabbit.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MqRabbitNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("mq-client", new MqClientParser());
		registerBeanDefinitionParser("mq-server", new MqServerParser());
	}

}
