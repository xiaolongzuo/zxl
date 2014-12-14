package cn.zxl.mvc.hbase.xml;

import java.util.Arrays;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.zxl.common.ArrayUtil;

public class StringListXmlAdapter extends XmlAdapter<Object, List<?>> {

	@Override
	public Object marshal(List<?> list) throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element element = document.createElement("value");
		String arrayString = list.toString();
		String text = "";
		if (!ArrayUtil.isEmpty(list)) {
			text = arrayString.substring(1, arrayString.length() - 1);
		}
		Node node = document.createTextNode(text);
		element.appendChild(node);
		return element;
	}

	@Override
	public List<String> unmarshal(Object element) throws Exception {
		if (element instanceof Element) {
			String content = ((Element) element).getTextContent();
			return Arrays.asList(content.split(","));
		} else {
			throw new OperationNotSupportedException();
		}
	}

}
