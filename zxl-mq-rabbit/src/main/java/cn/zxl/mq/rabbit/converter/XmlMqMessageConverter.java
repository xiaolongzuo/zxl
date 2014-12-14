package cn.zxl.mq.rabbit.converter;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import cn.zxl.mq.rabbit.Message;
import cn.zxl.mq.rabbit.Message.Head;

@Component
public class XmlMqMessageConverter extends AbstractMqMessageConverter {

	private static final Unmarshaller MESSAGE_UNMARSHALLER;

	private static final Marshaller MESSAGE_MARSHALLER;

	static {
		try {
			JAXBContext messageJaxbContext = JAXBContext.newInstance(Message.class);
			MESSAGE_UNMARSHALLER = messageJaxbContext.createUnmarshaller();
			MESSAGE_MARSHALLER = messageJaxbContext.createMarshaller();
		} catch (JAXBException exception) {
			throw new RuntimeException(exception);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromMessage(org.springframework.amqp.core.Message message, Class<T> clazz) {
		try {
			String content = new String(message.getBody(), DEFAULT_CHARSET);
			if (logger.isInfoEnabled()) {
				logger.info("获取将要转换的消息字符串：" + content);
			}
			Message messageBean = (Message) MESSAGE_UNMARSHALLER.unmarshal(new StringReader(content));
			Unmarshaller unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
			return (T) unmarshaller.unmarshal(new StringReader(messageBean.getContent()));
		} catch (Exception exception) {
			throw new RuntimeException("######消息转换失败！", exception);
		}
	}

	@Override
	public <T> byte[] getBytesAndSetMessageProperties(T object, MessageProperties messageProperties) {
		if (object == null)
			throw new RuntimeException("######发送的消息不能为null！");
		StringWriter stringWriter = new StringWriter();
		Message message = new Message(new Head(), null);
		try {
			Marshaller marshaller = JAXBContext.newInstance(object.getClass()).createMarshaller();
			marshaller.marshal(object, stringWriter);
			message.setContent(stringWriter.toString());
		} catch (Exception exception) {
			throw new RuntimeException("######消息转换失败！", exception);
		}
		if (logger.isInfoEnabled()) {
			logger.info("正在准备发送消息并等待接受，参数为：" + message == null ? "null" : JSONObject.fromObject(message));
		}
		stringWriter = new StringWriter();
		try {
			MESSAGE_MARSHALLER.marshal(message, stringWriter);
			return stringWriter.toString().getBytes(DEFAULT_CHARSET);
		} catch (Exception exception) {
			throw new RuntimeException("######消息转换失败！", exception);
		}
	}

}
