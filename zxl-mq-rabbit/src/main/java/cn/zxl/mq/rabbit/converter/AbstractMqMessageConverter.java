package cn.zxl.mq.rabbit.converter;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import cn.zxl.common.LogUtil;

public abstract class AbstractMqMessageConverter implements MqMessageConverter {

	public static Logger logger = Logger.getLogger(AbstractMqMessageConverter.class);

	@Override
	public <T> Message toSendMessage(T object) {
		return toMessage(object, null, true);
	}

	@Override
	public <T> Message toSendAndReceiveMessage(T object) {
		return toMessage(object, null, false);
	}

	@Override
	public <T> Message toReplyMessage(T object, String correlationId) {
		return toMessage(object, correlationId, false);
	}

	protected <T> Message toMessage(T object, String correlationId, boolean isOnlySend) {
		if (object == null) {
			return null;
		}
		if (object instanceof Message) {
			return (Message) object;
		}
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setMessageId(UUID.randomUUID().toString());
		messageProperties.setContentEncoding(DEFAULT_CHARSET);
		try {
			if (!isOnlySend) {
				if (correlationId == null) {
					messageProperties.setCorrelationId(UUID.randomUUID().toString().getBytes(DEFAULT_CHARSET));
				} else {
					messageProperties.setCorrelationId(correlationId.getBytes(DEFAULT_CHARSET));
				}
			}
			byte[] bytes = getBytesAndSetMessageProperties(object, messageProperties);
			return new Message(bytes, messageProperties);
		} catch (Exception warnException) {
			throw new RuntimeException("#####��Ϣת��ʧ��", warnException);
		}
	}

	public abstract <T> byte[] getBytesAndSetMessageProperties(T object, MessageProperties messageProperties);

	@Override
	@SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message, String className) {
		if (message == null) {
			return null;
		}
		if (Message.class.getName().equals(className)) {
			return (T) message;
		}
		try {
			return (T) fromMessage(message, className == null ? null : getClass().getClassLoader().loadClass(className));
		} catch (Exception warnException) {
			LogUtil.warn(logger, "#####��Ϣת��ʧ��", warnException);
			return null;
		}
	}

}
