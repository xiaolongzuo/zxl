package cn.zxl.mq.rabbit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import cn.zxl.mq.rabbit.converter.JsonMqMessageConverter;
import cn.zxl.mq.rabbit.converter.MqMessageConverter;

public abstract class AbstractMqBean {

	protected List<RabbitTemplate> mqTemplates;

	protected List<RabbitAdmin> mqAdmins;

	protected int size;

	protected MqMessageConverter mqMessageConverter = new JsonMqMessageConverter();

	protected boolean queueDurable = true;

	protected boolean queueExclusive = false;

	protected boolean queueAutoDelete = false;

	protected Map<String, Object> queueArguments = new HashMap<String, Object>();

	protected Map<String, Object> bindingArguments = new HashMap<String, Object>();

	protected List<RabbitTemplate> getMqTemplates() {
		return mqTemplates;
	}

	public void setMqTemplates(List<RabbitTemplate> mqTemplates) {
		this.mqTemplates = mqTemplates;
		this.size = mqTemplates.size();
	}

	protected int getSize() {
		return size;
	}

	protected MqMessageConverter getMqMessageConverter() {
		return mqMessageConverter;
	}

	public void setMqMessageConverter(MqMessageConverter mqMessageConverter) {
		this.mqMessageConverter = mqMessageConverter;
	}

	protected int chooseIndex(Message message) {
		return Math.abs(message.hashCode() % size);
	}

	protected List<RabbitAdmin> getMqAdmins() {
		return mqAdmins;
	}

	public void setMqAdmins(List<RabbitAdmin> mqAdmins) {
		this.mqAdmins = mqAdmins;
	}

	public void setQueueDurable(boolean queueDurable) {
		this.queueDurable = queueDurable;
	}

	public void setQueueExclusive(boolean queueExclusive) {
		this.queueExclusive = queueExclusive;
	}

	public void setQueueAutoDelete(boolean queueAutoDelete) {
		this.queueAutoDelete = queueAutoDelete;
	}

	public void setQueueArguments(Map<String, Object> queueArguments) {
		this.queueArguments = queueArguments;
	}

	public void setBindingArguments(Map<String, Object> bindingArguments) {
		this.bindingArguments = bindingArguments;
	}

	protected boolean isQueueDurable() {
		return queueDurable;
	}

	protected boolean isQueueExclusive() {
		return queueExclusive;
	}

	protected boolean isQueueAutoDelete() {
		return queueAutoDelete;
	}

	protected Map<String, Object> getQueueArguments() {
		return queueArguments;
	}

	protected Map<String, Object> getBindingArguments() {
		return bindingArguments;
	}

}
