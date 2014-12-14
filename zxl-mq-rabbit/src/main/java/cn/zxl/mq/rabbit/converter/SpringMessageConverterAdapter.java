package cn.zxl.mq.rabbit.converter;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public class SpringMessageConverterAdapter implements MqMessageConverter {
	
	private MessageConverter messageConverter = new SimpleMessageConverter();
	
	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public <T> Message toSendMessage(T object) {
		return messageConverter.toMessage(object, null);
	}

	public <T> Message toSendAndReceiveMessage(T object) {
		return messageConverter.toMessage(object, null);
	}

	public <T> Message toReplyMessage(T object, String correlationId) {
		MessageProperties messageProperties = new MessageProperties();
		try {
			messageProperties.setCorrelationId(correlationId.getBytes(DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {}
		return messageConverter.toMessage(object, messageProperties);
	}

	@SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message, String className) {
		return (T) messageConverter.fromMessage(message);
	}

	@SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message, Class<T> clazz) {
		return (T) messageConverter.fromMessage(message);
	}
	
}
