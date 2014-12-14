package cn.zxl.mq.rabbit;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.InitializingBean;

import cn.zxl.common.LogUtil;

/**
 * 通用MQ客户端
 * 
 * @author xiaolongzuo
 *
 */
public class MQClient extends AbstractMqBean implements InitializingBean {

	public static Logger logger = Logger.getLogger(MQClient.class);

	public static final String DEFAULT_EXCHANGE = "";

	/** 该属性指定客户端发送的交换器，默认为direct类型的空交换器【可选】 */
	private String exchange = DEFAULT_EXCHANGE;

	/** 该属性指定客户端发送的路由键，当exchange的值不是fanout类型的交换器时，该属性为必选【可选】 */
	private String routingKey;

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void send(Object message) {
		if (message == null) {
			throw new NullPointerException("不能发送空消息！");
		}
		Message sendMessage = null;
		try {
			sendMessage = mqMessageConverter.toSendMessage(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		int index = chooseIndex(sendMessage);
		try {
			mqTemplates.get(index).send(exchange, routingKey, sendMessage);
		} catch (Exception exception) {
			LogUtil.warn(logger, "发送消息失败，尝试重发！", exception);
			resend(index, sendMessage, true);
		}
	}

	public <T> T sendAndReceive(Object message, Class<T> receiveMessageClass) {
		if (message == null) {
			throw new NullPointerException("不能发送空消息！");
		}
		if (receiveMessageClass == null) {
			throw new NullPointerException("接受的消息类型不能为空！");
		}
		Message sendMessage = null;
		try {
			sendMessage = mqMessageConverter.toSendMessage(message);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Message messageBean = null;
		int index = chooseIndex(sendMessage);
		try {
			messageBean = mqTemplates.get(index).sendAndReceive(exchange, routingKey, sendMessage);
		} catch (Exception exception) {
			LogUtil.warn(logger, "发送消息失败，尝试重发！", exception);
			messageBean = resend(index, sendMessage, false);
		}
		T result = null;
		if (messageBean != null) {
			try {
				result = mqMessageConverter.fromMessage(messageBean, receiveMessageClass);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("接受的返回结果为空！");
		}
		return result;
	}

	private Message resend(int excludeIndex, Message sendMessage, boolean isAsyn) {
		Message messageBean = null;
		for (int i = 0; i < size && i != excludeIndex; i++) {
			try {
				if (isAsyn) {
					mqTemplates.get(i).send(exchange, routingKey, sendMessage);
				} else {
					messageBean = mqTemplates.get(i).sendAndReceive(exchange, routingKey, sendMessage);
				}
				LogUtil.info(logger, "已成功重发消息！");
				break;
			} catch (Exception e) {
				LogUtil.warn(logger, "尝试重发失败！", e);
				continue;
			}
		}
		return messageBean;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<RabbitAdmin> rabbitAdmins = getMqAdmins();
		for (int i = 0; i < rabbitAdmins.size(); i++) {
			RabbitAdmin rabbitAdmin = rabbitAdmins.get(i);
			while (true) {
				if (!DEFAULT_EXCHANGE.equals(exchange)) {
					break;
				}
				try {
					rabbitAdmin.declareQueue(new Queue(routingKey, queueDurable, queueExclusive, queueAutoDelete, queueArguments));
					rabbitAdmin.declareBinding(new Binding(routingKey, DestinationType.QUEUE, MQClient.DEFAULT_EXCHANGE, routingKey, bindingArguments));
					break;
				} catch (Exception e) {
					LogUtil.warn(logger, "创建序列［" + routingKey + "］失败", e);
				}
			}
		}
	}

}
