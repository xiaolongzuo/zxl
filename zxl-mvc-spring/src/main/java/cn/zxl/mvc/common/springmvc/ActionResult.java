package cn.zxl.mvc.common.springmvc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ActionResult implements Serializable{
	
	public static final int SUCCESS_CODE = 200;
	
	public static final int ERROR_CODE = 500;
	
	public static final String SUCCESS_MESSAGE = "success";

	private static final long serialVersionUID = 3646600911121581694L;

	private Integer code;
	
	private String message;
	
	public ActionResult() {
		super();
	}
	
	public ActionResult(Integer code,String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
