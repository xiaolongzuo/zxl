package cn.zxl.mvc.hbase.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="xml")
public class StringListXmlWapper implements Serializable {

	private static final long serialVersionUID = 8613332303380046116L;
	
	private List<String> value;

	public StringListXmlWapper() {
		super();
	}

	public StringListXmlWapper(List<String> value) {
		super();
		this.value = value;
	}

	@XmlJavaTypeAdapter(StringListXmlAdapter.class)
	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

}
