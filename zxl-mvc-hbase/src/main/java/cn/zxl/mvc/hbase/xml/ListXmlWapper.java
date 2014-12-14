package cn.zxl.mvc.hbase.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class ListXmlWapper implements Serializable {

	private static final long serialVersionUID = 8613332303380046116L;
	
	private List<?> entity;

	public ListXmlWapper() {
		super();
	}

	public ListXmlWapper(List<?> entity) {
		super();
		this.entity = entity;
	}
	
	public List<?> getEntity() {
		return entity;
	}

	public void setEntity(List<?> entity) {
		this.entity = entity;
	}

}
