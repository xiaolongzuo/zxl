package cn.zxl.mq.rabbit;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;

import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;

import com.rabbitmq.client.ShutdownSignalException;

/**
 * 通用MQ服务端
 * 
 * @author xiaolongzuo
 *
 */
public class MQServer extends AbstractMqBean {

	public static Logger logger = Logger.getLogger(MQServer.class);

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String NOT_FOUND_MESSAGE = "reply-code=404";

	/** webservice实现的实例【必选】 */
	private Object targetObject;

	/** 该服务监听的序列【必选】 */
	private String queueName;

	/** 该服务接受消息的类型【可选】 */
	private String receiveMessageClassName;

	/** webservice被包装的服务方法【可选，与methodProvider两者必须提供其中一个】 */
	private String targetMethod;

	/**
	 * 该接口用于指定特定的方法，默认不启用，如果实现类没有重名方法，更简单的方式是提供targetMethod属性【可选，
	 * 与targetMethod两者必须提供其中一个】
	 */
	private MethodProvider methodProvider;

	/** 如果webservice的参数个数不是一个，则需要自己定制此接口【可选】 */
	private ParamsConverter paramsConverter = new DefaultParamsConverter();

	/** 如果webservice方法调用失败，此接口的handle方法用于处理【可选】 */
	private FailHandler failHandler;

	public final void receive() {
		Method method = null;
		if (methodProvider != null) {
			method = methodProvider.provide(targetObject);
		} else {
			method = ReflectUtil.getMethodByName(targetObject.getClass(), targetMethod);
		}
		if (method == null) {
			return;
		}
		for (int i = 0; i < size; i++) {
			while (true) {
				RabbitTemplate mqTemplate = mqTemplates.get(i);
				Message message = null;
				try {
					message = mqTemplate.receive(queueName);
				} catch (Exception warnException) {
					LogUtil.warn(logger, "######MQ接受消息时发生错误，queueName：" + getQueueName(), warnException);
					handleReceiveError(warnException, mqAdmins.get(i));
				}
				if (message == null) {
					break;
				}
				Object messageObject = mqMessageConverter.fromMessage(message, receiveMessageClassName);
				Object result = null;
				Exception exception = null;
				try {
					result = method.invoke(targetObject, paramsConverter.convert(messageObject));
				} catch (Exception warnException) {
					try {
						if (failHandler == null) {
							exception = warnException;
						} else {
							result = failHandler.handle(message, warnException);
						}
					} catch (Exception cause) {
						exception = cause;
					}
				}
				try {
					replyIfNecessary(message, result, mqTemplate);
				} catch (Exception replyException) {
					exception = replyException;
				}
				if (exception != null) {
					LogUtil.warn(logger, "消息处理中产生异常：", exception);
				}
			}
		}
	}

	private void replyIfNecessary(Message message, Object result, RabbitTemplate mqTemplate) {
		MessageProperties messageProperties = message.getMessageProperties();
		String correlationId = null;
		try {
			correlationId = new String(messageProperties.getCorrelationId(), DEFAULT_CHARSET);
		} catch (Exception ignored) {
			try {
				correlationId = (String) SerializationUtils.deserialize(messageProperties.getCorrelationId());
			} catch (Exception warnException) {
				LogUtil.warn(logger, "#####获取correlationId失败，可能导致客户端挂起", warnException);
			}
		}
		boolean isNecessary = result != null && messageProperties.getReplyTo() != null;
		if (isNecessary) {
			mqTemplate.send(messageProperties.getReplyTo(), correlationId == null ? mqMessageConverter.toSendMessage(result) : mqMessageConverter.toReplyMessage(result, correlationId));
		}
	}

	protected void handleReceiveError(Exception warnException, RabbitAdmin mqAdmin) {
		Throwable throwable = warnException;
		while ((throwable = throwable.getCause()) != null) {
			if (throwable instanceof ShutdownSignalException && throwable.getMessage().contains(NOT_FOUND_MESSAGE)) {
				try {
					mqAdmin.declareQueue(new Queue(queueName, queueDurable, queueExclusive, queueAutoDelete, queueArguments));
					mqAdmin.declareBinding(new Binding(queueName, DestinationType.QUEUE, MQClient.DEFAULT_EXCHANGE, queueName, bindingArguments));
				} catch (Exception e) {
				}
				break;
			}
		}
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public void setParamsConverter(ParamsConverter paramsConverter) {
		this.paramsConverter = paramsConverter;
	}

	public void setFailHandler(FailHandler failHandler) {
		this.failHandler = failHandler;
	}

	public void setMethodProvider(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	public void setReceiveMessageClassName(String receiveMessageClassName) {
		this.receiveMessageClassName = receiveMessageClassName;
	}

	protected Object getTargetObject() {
		return targetObject;
	}

	protected String getTargetMethod() {
		return targetMethod;
	}

	protected MethodProvider getMethodProvider() {
		return methodProvider;
	}

	protected ParamsConverter getParamsConverter() {
		return paramsConverter;
	}

	protected FailHandler getFailHandler() {
		return failHandler;
	}

	protected String getQueueName() {
		return queueName;
	}

	protected String getReceiveMessageClassName() {
		return receiveMessageClassName;
	}

	public static interface MethodProvider {

		Method provide(Object targetObject);

	}

	public static interface ParamsConverter {

		Object[] convert(Object message);

	}

	static class DefaultParamsConverter implements ParamsConverter {

		@Override
		public Object[] convert(Object message) {
			if (message == null) {
				return new Object[] {};
			} else {
				return new Object[] { message };
			}
		}

	}

	public static interface FailHandler {

		Object handle(Message message, Exception exception);

	}

}
