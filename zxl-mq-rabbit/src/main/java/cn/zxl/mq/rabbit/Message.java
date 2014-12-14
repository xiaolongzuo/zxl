package cn.zxl.mq.rabbit;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Message {
	
	private static final String CHARSET = "utf-8";
	
	private Head head;
	
	private byte[] body;
	
	private String content;
	
	public Message() {
		super();
	}

	public Message(Head head, String content) {
		super();
		this.head = head;
		this.content = content;
	}

	public Head getHead() {
		return head;
	}

	public void setHead(Head head) {
		this.head = head;
	}

	public byte[] getBody() {
		return body;
	}
	
	public void setBody(byte[] body) {
		this.body = body;
		if (this.body != null && this.body.length > 0) {
			try {
				this.content = new String(this.body,CHARSET);
			} catch (UnsupportedEncodingException e) {}
		}
	}
	
	@XmlTransient
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		if (this.content != null && this.content.length() > 0) {
			try {
				this.body = this.content.getBytes(CHARSET);
			} catch (UnsupportedEncodingException e) {}
		}
	}

	public static class Head {
		
		private String messageId;
		
		public Head() {
			super();
		}

		public Head(String messageId) {
			super();
			this.messageId = messageId;
		}

		public String getMessageId() {
			return messageId;
		}

		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		
	}
}
