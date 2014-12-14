package cn.zxl.mq.rabbit.converter;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import cn.zxl.common.LogUtil;
import cn.zxl.mq.rabbit.util.PrimitiveUtil;

public class JsonMqMessageConverter extends AbstractMqMessageConverter {

	private static final String CONTENT_TYPE_JSON = "text/json";

	public static final MessageConverter DEFAULT_MESSAGE_CONVERTER = new SimpleMessageConverter();

	@Override
	@SuppressWarnings("unchecked")
	public <T> T fromMessage(org.springframework.amqp.core.Message message, Class<T> clazz) {
		String content = null;
		if (clazz == null || PrimitiveUtil.isPrimitive(clazz)) {
			return (T) DEFAULT_MESSAGE_CONVERTER.fromMessage(message);
		}
		try {
			content = new String(message.getBody(), DEFAULT_CHARSET);
			return (T) JSONObject.toBean(JSONObject.fromObject(content), clazz);
		} catch (Exception warnException) {
			LogUtil.warn(logger, "#####��Ϣת��ʧ��", warnException);
			return null;
		}
	}

	@Override
	public <T> byte[] getBytesAndSetMessageProperties(T object, MessageProperties messageProperties) {
		messageProperties.setContentType(CONTENT_TYPE_JSON);
		byte[] bytes = null;
		if (object instanceof JSONObject) {
			try {
				bytes = object.toString().getBytes(DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException exception) {
				throw new RuntimeException("#####��Ϣת��ʧ��", exception);
			}
		} else if (PrimitiveUtil.isPrimitive(object.getClass())) {
			bytes = DEFAULT_MESSAGE_CONVERTER.toMessage(object, messageProperties).getBody();
		} else {
			try {
				bytes = JSONObject.fromObject(object).toString().getBytes(DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException exception) {
				throw new RuntimeException("#####��Ϣת��ʧ��", exception);
			}
		}
		return bytes;
	}

}