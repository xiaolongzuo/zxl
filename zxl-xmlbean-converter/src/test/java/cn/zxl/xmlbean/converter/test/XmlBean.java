package cn.zxl.xmlbean.converter.test;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.zxl.xmlbean.converter.annotation.ADate;

/**
 * 用于测试的对象
 * @author 左潇龙
 * @version 1.0.0
 * @since 1.0.0
 */
public class XmlBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String stringParam = "stringValue";
	
	@ADate(format = "yyyy-MM-dd hh")//日期注解，设置日期格式
	private Date dateParam;
	
	private XmlBean testParam;
	
	private List<XmlBean> testListParam;
	
	public XmlBean(){
		try {
			dateParam = new SimpleDateFormat("yyyy-MM-dd HH").parse("2014-12-14 08");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String getStringParam() {
		return stringParam;
	}

	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	public Date getDateParam() {
		return dateParam;
	}

	public void setDateParam(Date dateParam) {
		this.dateParam = dateParam;
	}

	public XmlBean getTestParam() {
		return testParam;
	}

	public void setTestParam(XmlBean testParam) {
		this.testParam = testParam;
	}

	public List<XmlBean> getTestListParam() {
		return testListParam;
	}

	public void setTestListParam(List<XmlBean> testListParam) {
		this.testListParam = testListParam;
	}
	
	/**
	 * 提供一个创建测试对象的方法
	 */
	public static XmlBean createObject(){
		XmlBean t = new XmlBean();
		XmlBean t1 = new XmlBean();
		XmlBean t2 = new XmlBean();
		XmlBean t3 = new XmlBean();
		XmlBean t4 = new XmlBean();
		XmlBean t5 = new XmlBean();
		XmlBean t6 = new XmlBean();
		XmlBean t7 = new XmlBean();
		XmlBean t8 = new XmlBean();
		XmlBean t9 = new XmlBean();
		XmlBean t10 = new XmlBean();
		List<XmlBean> testList2 = new ArrayList<XmlBean>();
		testList2.add(t1);
		testList2.add(t2);
		List<XmlBean> testList = new ArrayList<XmlBean>();
		t10.testListParam = testList2;
		testList.add(t10);
		testList.add(t9);
		testList.add(t8);
		testList.add(t7);
		testList.add(t6);
		t5.testListParam = testList;
		t5.testParam = t4;
		t3.testParam = t5;
		t.testParam = t3;
		return t;
	}
	
	public String toString(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("stringParam:" + (stringParam == null? "" : stringParam) + "\r\n")
		.append("dateParam:" + (dateParam == null ? "" : dateParam) + "\r\n")
		.append("testParam:" + (testParam == null ? "" : testParam) + "\r\n");
		if (testListParam != null) {
			stringBuffer.append("testListParam:\r\n");
			for (XmlBean temp : testListParam) {
				stringBuffer.append(temp + "\r\n");
			}
		}
		return stringBuffer.toString();
	}
}
