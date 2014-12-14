package cn.zxl.mq.rabbit.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import cn.zxl.common.StringUtil;
import cn.zxl.common.security.CommonEncrypter;

public class MqBeanParser extends AbstractSingleBeanDefinitionParser {

	private static final String ADDRESSES_NAME = "addresses";

	private static final String LOCAL_ADDRESS_NAME = "local-address";

	private static final String DEFAULT_HOST = "localhost";

	private static final String CONNECTION_FACTORY_SUFFIX = "ConnectionFactory";

	private static final String MQ_TEMPLATE_SUFFIX = "MqTemplate";

	private static final String MQ_ADMIN_SUFFIX = "MqAdmin";

	private static final String PROPERTIES_FILE_PATH = "properties-file-path";

	private static final String QUEUE_DURABLE_NAME = "queue-durable";

	private static final String QUEUE_EXCLUSIVE_NAME = "queue-exclusive";

	private static final String QUEUE_AUTO_DELETE_NAME = "queue-auto-delete";

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		doParseAddresses(element, parserContext.getRegistry(), builder);
		doParseOther(element, parserContext, builder);
	}

	protected void doParseAddresses(Element element, BeanDefinitionRegistry beanDefinitionRegistry, BeanDefinitionBuilder builder) {
		buildRemoteBeanDefinition(element, beanDefinitionRegistry, builder);
	}

	protected void buildRemoteBeanDefinition(Element element, BeanDefinitionRegistry beanDefinitionRegistry, BeanDefinitionBuilder builder) {
		ManagedList<RuntimeBeanReference> mqTemplates = new ManagedList<RuntimeBeanReference>();
		ManagedList<RuntimeBeanReference> mqAdmins = new ManagedList<RuntimeBeanReference>();
		String[] addresses = new String[] { DEFAULT_HOST };
		if (element.hasAttribute(ADDRESSES_NAME) && !StringUtil.isEmpty(element.getAttribute(ADDRESSES_NAME))) {
			String addressesAttributeValue = element.getAttribute(ADDRESSES_NAME);
			if (addressesAttributeValue.startsWith("${") && addressesAttributeValue.endsWith("}")) {
				try {
					InputStream inputStream = getClass().getClassLoader().getResourceAsStream(element.getAttribute(PROPERTIES_FILE_PATH));
					Properties properties = new Properties();
					properties.load(inputStream);
					addresses = properties.getProperty(addressesAttributeValue.substring(2, addressesAttributeValue.length() - 1)).split(",");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				addresses = new String[] { addressesAttributeValue };
			}
		}
		String localAddress = element.hasAttribute(LOCAL_ADDRESS_NAME) ? element.getAttribute(LOCAL_ADDRESS_NAME) : null;
		List<String> mqTemplateNames = new ArrayList<String>();
		for (int i = 0; i < addresses.length; i++) {
			String host = addresses[i];
			String prefix = CommonEncrypter.encryptMD5(host);
			String connectionFactoryBeanName = prefix + CONNECTION_FACTORY_SUFFIX;
			String mqTemplateBeanName = prefix + MQ_TEMPLATE_SUFFIX;
			String mqAdminBeanName = prefix + MQ_ADMIN_SUFFIX;
			if (!containsBeanDefinition(beanDefinitionRegistry, connectionFactoryBeanName)) {
				buildConnectionFactoryBeanDefinition(connectionFactoryBeanName, host, beanDefinitionRegistry);
				buildRabbitTemplateBeanDefinition(mqTemplateBeanName, connectionFactoryBeanName, beanDefinitionRegistry);
				buildRabbitAdminBeanDefinition(mqAdminBeanName, connectionFactoryBeanName, beanDefinitionRegistry);
			}
			if (!mqTemplateNames.contains(mqTemplateBeanName)) {
				mqTemplateNames.add(mqTemplateBeanName);
				if (host.equals(localAddress)) {
					mqTemplates.add(0, new RuntimeBeanReference(mqTemplateBeanName));
					mqAdmins.add(0, new RuntimeBeanReference(mqAdminBeanName));
				} else {
					mqTemplates.add(new RuntimeBeanReference(mqTemplateBeanName));
					mqAdmins.add(new RuntimeBeanReference(mqAdminBeanName));
				}
			}
		}
		builder.addPropertyValue("mqTemplates", mqTemplates);
		builder.addPropertyValue("mqAdmins", mqAdmins);
	}

	protected boolean containsBeanDefinition(BeanDefinitionRegistry beanDefinitionRegistry, String connectionFactoryBeanName) {
		return beanDefinitionRegistry.containsBeanDefinition(connectionFactoryBeanName);
	}

	protected void buildConnectionFactoryBeanDefinition(String beanName, String host, BeanDefinitionRegistry beanDefinitionRegistry) {
		AbstractBeanDefinition connectionFactoryBeanDefinition = new GenericBeanDefinition();
		connectionFactoryBeanDefinition.setBeanClass(CachingConnectionFactory.class);
		if (!StringUtil.isEmpty(host)) {
			MutablePropertyValues connectionFactoryPropertyValues = new MutablePropertyValues();
			connectionFactoryPropertyValues.add("host", host);
			connectionFactoryBeanDefinition.setPropertyValues(connectionFactoryPropertyValues);
		}
		beanDefinitionRegistry.registerBeanDefinition(beanName, connectionFactoryBeanDefinition);
	}

	protected void buildRabbitTemplateBeanDefinition(String beanName, String connectionFactoryBeanName, BeanDefinitionRegistry beanDefinitionRegistry) {
		AbstractBeanDefinition rabbitTemplateBeanDefinition = new GenericBeanDefinition();
		rabbitTemplateBeanDefinition.setBeanClass(RabbitTemplate.class);
		ConstructorArgumentValues rabbitTemplateConstructorArgumentValues = new ConstructorArgumentValues();
		rabbitTemplateConstructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(connectionFactoryBeanName));
		rabbitTemplateBeanDefinition.setConstructorArgumentValues(rabbitTemplateConstructorArgumentValues);
		beanDefinitionRegistry.registerBeanDefinition(beanName, rabbitTemplateBeanDefinition);
	}

	protected void buildRabbitAdminBeanDefinition(String beanName, String connectionFactoryBeanName, BeanDefinitionRegistry beanDefinitionRegistry) {
		AbstractBeanDefinition rabbitAdminBeanDefinition = new GenericBeanDefinition();
		rabbitAdminBeanDefinition.setBeanClass(RabbitAdmin.class);
		ConstructorArgumentValues rabbitAdminConstructorArgumentValues = new ConstructorArgumentValues();
		rabbitAdminConstructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(connectionFactoryBeanName));
		rabbitAdminBeanDefinition.setConstructorArgumentValues(rabbitAdminConstructorArgumentValues);
		beanDefinitionRegistry.registerBeanDefinition(beanName, rabbitAdminBeanDefinition);
	}

	protected void doParseOther(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (element.hasAttribute(QUEUE_DURABLE_NAME)) {
			builder.addPropertyValue("queueDurable", Boolean.valueOf(element.getAttribute(QUEUE_DURABLE_NAME)));
		}
		if (element.hasAttribute(QUEUE_EXCLUSIVE_NAME)) {
			builder.addPropertyValue("queueExclusive", Boolean.valueOf(element.getAttribute(QUEUE_EXCLUSIVE_NAME)));
		}
		if (element.hasAttribute(QUEUE_AUTO_DELETE_NAME)) {
			builder.addPropertyValue("queueAutoDelete", Boolean.valueOf(element.getAttribute(QUEUE_AUTO_DELETE_NAME)));
		}
	}

}
