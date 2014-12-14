package cn.zxl.mvc.hbase.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class ValueXmlWapper implements Serializable{

	private static final long serialVersionUID = -2599203514414408627L;

	private Object value;
	
	public ValueXmlWapper() {
		super();
	}

	public ValueXmlWapper(Object value) {
		super();
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
