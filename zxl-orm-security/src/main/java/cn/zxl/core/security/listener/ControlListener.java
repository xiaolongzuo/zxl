package cn.zxl.core.security.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import cn.zxl.orm.security.service.ControlService;

public class ControlListener implements ServletContextListener {

	private static final String SERVICE_BEAN_NAME = "controlService";

	public static final String CONTROL_ATTRIBUTE_NAME = "control";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		ControlService controlService = (ControlService) applicationContext.getBean(SERVICE_BEAN_NAME);
		servletContext.setAttribute(CONTROL_ATTRIBUTE_NAME, controlService.getControlMap());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		if (servletContext.getAttribute(CONTROL_ATTRIBUTE_NAME) != null) {
			servletContext.removeAttribute(CONTROL_ATTRIBUTE_NAME);
		}
	}

}
