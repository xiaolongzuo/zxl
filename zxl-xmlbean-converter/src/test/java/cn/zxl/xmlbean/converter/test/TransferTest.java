package cn.zxl.xmlbean.converter.test;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cn.zxl.xmlbean.converter.container.ConfigurableContainer;
import cn.zxl.xmlbean.converter.container.Container;
import cn.zxl.xmlbean.converter.processor.XmlBulider;
import cn.zxl.xmlbean.converter.processor.XmlBulider.Format;
import cn.zxl.xmlbean.converter.support.DefaultConfigurableContainer;

public class TransferTest {
	
	private String xml1;
	
	private String xml2;
	
	@Before
	public void setUp(){
		try {
			xml1 = FileUtils.read(TransferTest.class.getClassLoader().getResource("test1.xml").getFile());
			xml2 = FileUtils.read(TransferTest.class.getClassLoader().getResource("test2.xml").getFile());
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
	}

	@Test
	public void main() {
		try {
			//测试不可配置的容器
			Assert.assertEquals(useUnconfigurableContainer(), xml1);
			//测试可配置的容器
			Assert.assertEquals(useConfigurableContainer(), xml2);
		} catch (IOException e) {
			Assert.assertTrue(false);
		}
	}
	
	private String useUnconfigurableContainer() throws IOException{
		//不可配置的容器
		Container container = new DefaultConfigurableContainer(XmlBean.class);
		//向容器中添加一个复杂的Test对象
		container.add(XmlBean.createObject());
		//获取容器自动解析的xml内容
		return container.getXml();
	}
	
	private String useConfigurableContainer() throws IOException{
		//可配置的容器，使用可配置的容器接口，推荐此种方式，比较灵活
		ConfigurableContainer configurableContainer = new DefaultConfigurableContainer(XmlBean.class);
		XmlBulider xmlBulider = configurableContainer.getXmlBulider();
		//设置构建器的xml格式
		xmlBulider.setFormat(Format.TAB_AND_LINE);
		//改变容器中的构建器
		configurableContainer.setXmlBulider(xmlBulider);
		//向可配置容器添加复杂对象
		configurableContainer.add(XmlBean.createObject());
		//获取容器自动解析的xml内容，比较下不能配置的容器构建的xml格式和日期格式
		String configXml = configurableContainer.getXml(0);//等同于getXml()
		//打印容器大小
		configurableContainer.add(configXml);
		XmlBean objectXmlBean = configurableContainer.getObject(1);
		Assert.assertEquals(2,configurableContainer.size());
		Assert.assertNotNull(objectXmlBean);
		return configXml;
	}
	
}
