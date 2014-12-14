package cn.zxl.mq.rabbit.converter;

import org.springframework.amqp.core.Message;

public interface MqMessageConverter {
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public <T> Message toSendMessage(T object);
	
	public <T> Message toSendAndReceiveMessage(T object);
	
	public <T> Message toReplyMessage(T object,String correlationId);
	
	public <T> T fromMessage(Message message,String className);
	
	public <T> T fromMessage(Message message,Class<T> clazz);
	
}
