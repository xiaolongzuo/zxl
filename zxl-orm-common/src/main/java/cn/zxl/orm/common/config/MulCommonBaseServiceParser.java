package cn.zxl.orm.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.config.AdvisorComponentDefinition;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.config.PointcutComponentDefinition;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.BaseService;
import cn.zxl.orm.common.CommonBaseDaoImpl;
import cn.zxl.orm.common.CommonBaseServiceImpl;
import cn.zxl.orm.common.HibernateNamingStrategy;
import cn.zxl.orm.common.SimpleDataSource;

public class MulCommonBaseServiceParser extends AbstractSingleBeanDefinitionParser {

	private static final String DATA_SOURCE_SUFFIX = "DataSource";

	private static final String SESSION_FACTORY_SUFFIX = "SessionFactory";

	private static final String SQL_SESSION_FACTORY_SUFFIX = "SqlSessionFactory";

	private static final String SQL_SESSION_TEMPLATE_SUFFIX = "SqlSessionTemplate";

	private static final String COMMON_BASE_DAO_SUFFIX = "CommonBaseDao";

	private static final String HIBERNATE_TRANSACTION_MANAGER_SUFFIX = "HibernateTransactionManager";

	private static final String HIBERNATE_ADVICE_SUFFIX = "HibernateAdvice";

	private static final String TRANSACTION_ATTRIBUTE_SOURCE_SUFFIX = "TransactionAttributeSource";

	private static final Class<?> COMMON_BASE_DAO_CLASS = CommonBaseDaoImpl.class;

	private static final String CLASS_NAME = CommonBaseServiceImpl.class.getName();

	private static final String COMMON_BASE_SERVICE_SUFFIX = "CommonBaseService";

	private static final String SQL_SESSION_FACTORY_PARENT_BEAN_NAME = "abstractSqlSessionFactory";

	private static final String SESSION_FACTORY_PARENT_BEAN_NAME = "abstractSessionFactory";

	private static final String MAPPER_LOCATIONS_PREFIX = "classpath*:mybatis-";

	private static final String MAPPER_LOCATIONS_SUFFIX = "/*.xml";

	private static final String TABLE_PREFIX_NAME = "table-prefix";

	private static final String COMMON_BASE_DAO_FIELD_NAME = "commonBaseDao";

	private static final List<String> REQUIRED_METHODS = Arrays.asList("save*", "merge*", "update*", "delete*", "send*", "persist*");

	private static final List<String> READ_ONLY_METHODS = Arrays.asList("get*");

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return BaseService.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		if (StringUtil.isEmpty(element.getAttribute(NAME_ATTRIBUTE))) {
			parserContext.getReaderContext().error("CommonBaseService must have name attribute", element);
		}
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
		BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
		String name = element.getAttribute(NAME_ATTRIBUTE);
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + COMMON_BASE_SERVICE_SUFFIX);
		beanDefinition.setBeanClassName(CLASS_NAME);
		beanDefinitionRegistry.registerBeanDefinition(name + DATA_SOURCE_SUFFIX, buildDataSourceBeanDefinition(element, name));
		beanDefinitionRegistry.registerBeanDefinition(name + SQL_SESSION_FACTORY_SUFFIX, buildSqlSessionFactoryBeanDefinition(element, name));
		beanDefinitionRegistry.registerBeanDefinition(name + SESSION_FACTORY_SUFFIX, buildSessionFactoryBeanDefinition(element, name, parserContext.getDelegate(), beanDefinitionRegistry));
		beanDefinitionRegistry.registerBeanDefinition(name + SQL_SESSION_TEMPLATE_SUFFIX, buildSqlSessionTemplateBeanDefinition(element, name));
		beanDefinitionRegistry.registerBeanDefinition(name + COMMON_BASE_DAO_SUFFIX, buildCommonBaseDaoBeanDefinition(element, name));
		builder.addPropertyReference(COMMON_BASE_DAO_FIELD_NAME, name + COMMON_BASE_DAO_SUFFIX);
		element.setAttribute(ID_ATTRIBUTE, name + COMMON_BASE_SERVICE_SUFFIX);

		List<String> expressionList = buildExpressionList(element, delegate);
		if (expressionList.size() > 0) {
			beanDefinitionRegistry.registerBeanDefinition(name + HIBERNATE_TRANSACTION_MANAGER_SUFFIX, buildHibernateTransactionManagerBeanDefinition(element, name));
			beanDefinitionRegistry.registerBeanDefinition(name + TRANSACTION_ATTRIBUTE_SOURCE_SUFFIX, buildTransactionAttributeSourceBeanDefinition());
			beanDefinitionRegistry.registerBeanDefinition(name + HIBERNATE_ADVICE_SUFFIX, buildHibernateAdviceBeanDefinition(element, name));
			buildPointcutAndAdvisorBeanDefinition(name, expressionList, parserContext, beanDefinitionRegistry);
		}
	}

	private BeanDefinition buildDataSourceBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + DATA_SOURCE_SUFFIX);
		beanDefinition.setBeanClass(SimpleDataSource.class);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("name", name);
		beanDefinition.setPropertyValues(propertyValues);
		return beanDefinition;
	}

	private BeanDefinition buildSessionFactoryBeanDefinition(Element element, String name, BeanDefinitionParserDelegate beanDefinitionParserDelegate, BeanDefinitionRegistry beanDefinitionRegistry) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + SESSION_FACTORY_SUFFIX);
		beanDefinition.setBeanClass(LocalSessionFactoryBean.class);
		beanDefinition.setParentName(SESSION_FACTORY_PARENT_BEAN_NAME);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("dataSource", new RuntimeBeanReference(name + DATA_SOURCE_SUFFIX));
		if (element.hasAttribute(TABLE_PREFIX_NAME) && !StringUtil.isEmpty(element.getAttribute(TABLE_PREFIX_NAME))) {
			AbstractBeanDefinition namingStrategyBeanDefinition = new GenericBeanDefinition();
			String randomBeanName = UUID.randomUUID().toString();
			namingStrategyBeanDefinition.setAttribute(ID_ATTRIBUTE, randomBeanName);
			namingStrategyBeanDefinition.setBeanClass(HibernateNamingStrategy.class);
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			mutablePropertyValues.add("prefix", element.getAttribute(TABLE_PREFIX_NAME));
			namingStrategyBeanDefinition.setPropertyValues(mutablePropertyValues);
			beanDefinitionRegistry.registerBeanDefinition(randomBeanName, namingStrategyBeanDefinition);
			propertyValues.addPropertyValue("namingStrategy", new RuntimeBeanReference(randomBeanName));
		}
		beanDefinition.setPropertyValues(propertyValues);
		beanDefinitionParserDelegate.parsePropertyElements(element, beanDefinition);
		return beanDefinition;
	}

	private BeanDefinition buildSqlSessionFactoryBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + SQL_SESSION_FACTORY_SUFFIX);
		beanDefinition.setBeanClass(SqlSessionFactoryBean.class);
		beanDefinition.setParentName(SQL_SESSION_FACTORY_PARENT_BEAN_NAME);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("dataSource", new RuntimeBeanReference(name + DATA_SOURCE_SUFFIX));
		propertyValues.add("mapperLocations", MAPPER_LOCATIONS_PREFIX + name + MAPPER_LOCATIONS_SUFFIX);
		beanDefinition.setPropertyValues(propertyValues);
		return beanDefinition;
	}

	private BeanDefinition buildSqlSessionTemplateBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + SQL_SESSION_TEMPLATE_SUFFIX);
		beanDefinition.setBeanClass(SqlSessionTemplate.class);
		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addIndexedArgumentValue(0, new RuntimeBeanReference(name + SQL_SESSION_FACTORY_SUFFIX));
		beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
		return beanDefinition;
	}

	private BeanDefinition buildCommonBaseDaoBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + COMMON_BASE_DAO_SUFFIX);
		beanDefinition.setBeanClass(COMMON_BASE_DAO_CLASS);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("sessionFactory", new RuntimeBeanReference(name + SESSION_FACTORY_SUFFIX));
		propertyValues.add("sqlSessionTemplate", new RuntimeBeanReference(name + SQL_SESSION_TEMPLATE_SUFFIX));
		propertyValues.add("tablePrefix", element.getAttribute(TABLE_PREFIX_NAME));
		beanDefinition.setPropertyValues(propertyValues);
		return beanDefinition;
	}

	private List<String> buildExpressionList(Element element, BeanDefinitionParserDelegate delegate) {
		NodeList childNodes = element.getChildNodes();
		List<String> expressionList = new ArrayList<String>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node instanceof Element && delegate.nodeNameEquals(node, "mul-transaction-expression")) {
				Element mulTransactionExpressionElement = (Element) node;
				expressionList.add(mulTransactionExpressionElement.getAttribute("expression"));
			}
		}
		return expressionList;
	}

	private BeanDefinition buildHibernateTransactionManagerBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + HIBERNATE_TRANSACTION_MANAGER_SUFFIX);
		beanDefinition.setBeanClass(HibernateTransactionManager.class);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("sessionFactory", new RuntimeBeanReference(name + SESSION_FACTORY_SUFFIX));
		beanDefinition.setPropertyValues(propertyValues);
		return beanDefinition;
	}

	private BeanDefinition buildHibernateAdviceBeanDefinition(Element element, String name) {
		AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setAttribute(ID_ATTRIBUTE, name + HIBERNATE_ADVICE_SUFFIX);
		beanDefinition.setBeanClass(TransactionInterceptor.class);
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("transactionManager", new RuntimeBeanReference(name + HIBERNATE_TRANSACTION_MANAGER_SUFFIX));
		propertyValues.add("transactionAttributeSource", new RuntimeBeanReference(name + TRANSACTION_ATTRIBUTE_SOURCE_SUFFIX));
		beanDefinition.setPropertyValues(propertyValues);
		return beanDefinition;
	}

	private RootBeanDefinition buildTransactionAttributeSourceBeanDefinition() {
		ManagedMap<TypedStringValue, RuleBasedTransactionAttribute> transactionAttributeMap = new ManagedMap<TypedStringValue, RuleBasedTransactionAttribute>(READ_ONLY_METHODS.size() + READ_ONLY_METHODS.size());
		for (String method : REQUIRED_METHODS) {
			TypedStringValue nameHolder = new TypedStringValue(method);
			RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
			attribute.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + "REQUIRED");
			transactionAttributeMap.put(nameHolder, attribute);
		}
		for (String method : READ_ONLY_METHODS) {
			RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
			attribute.setReadOnly(true);
			transactionAttributeMap.put(new TypedStringValue(method), attribute);
		}
		RootBeanDefinition beanDefinition = new RootBeanDefinition(NameMatchTransactionAttributeSource.class);
		beanDefinition.getPropertyValues().add("nameMap", transactionAttributeMap);
		return beanDefinition;
	}

	private void buildPointcutAndAdvisorBeanDefinition(String name, List<String> expressionList, ParserContext parserContext, BeanDefinitionRegistry beanDefinitionRegistry) {
		CompositeComponentDefinition compositeComponentDefinition = new CompositeComponentDefinition("mul-transaction-expression", null);
		parserContext.pushContainingComponent(compositeComponentDefinition);

		BeanDefinition aspectJAutoProxyCreatorBeanDefinition = AopConfigUtils.registerAspectJAutoProxyCreatorIfNecessary(beanDefinitionRegistry);
		AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(beanDefinitionRegistry);
		if (aspectJAutoProxyCreatorBeanDefinition != null) {
			BeanComponentDefinition componentDefinition = new BeanComponentDefinition(aspectJAutoProxyCreatorBeanDefinition, AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
			parserContext.registerComponent(componentDefinition);
		}
		for (String expression : expressionList) {
			RootBeanDefinition pointcutDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
			pointcutDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			pointcutDefinition.setSynthetic(true);
			pointcutDefinition.getPropertyValues().add("expression", expression);
			String pointcutBeanName = parserContext.getReaderContext().registerWithGeneratedName(pointcutDefinition);
			parserContext.registerComponent(new PointcutComponentDefinition(pointcutBeanName, pointcutDefinition, expression));

			RootBeanDefinition advisorDefinition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
			advisorDefinition.getPropertyValues().add("adviceBeanName", new RuntimeBeanNameReference(name + HIBERNATE_ADVICE_SUFFIX));
			String advisorBeanName = parserContext.getReaderContext().registerWithGeneratedName(advisorDefinition);
			advisorDefinition.getPropertyValues().add("pointcut", new RuntimeBeanReference(pointcutBeanName));
			parserContext.registerComponent(new AdvisorComponentDefinition(advisorBeanName, advisorDefinition));
		}

		parserContext.popAndRegisterContainingComponent();
	}

}
