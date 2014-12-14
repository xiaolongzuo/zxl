package cn.zxl.orm.common.domain;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import cn.zxl.orm.common.BaseEntity;

@Entity
@XmlRootElement
public class Dictionary extends BaseEntity{

	private static final long serialVersionUID = 4288115532182896569L;

	private String type;
	
	private String value;
	
	private String text;
	
	private String description;
	
	public Dictionary() {
		super();
	}

	public Dictionary(String type) {
		super();
		this.type = type;
	}
	
	public Dictionary(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	
	public static Dictionary valueOf(String id) {
		Dictionary dictionary = new Dictionary();
		dictionary.setId(id);
		return dictionary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
