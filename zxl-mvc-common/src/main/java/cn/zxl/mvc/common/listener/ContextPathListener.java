package cn.zxl.mvc.common.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ContextPathListener implements ServletContextListener {
	
	public static final String CONTEXT_PATH_ATTRIBUTE_NAME = "contextPath";
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		servletContext.setAttribute(CONTEXT_PATH_ATTRIBUTE_NAME, (String) applicationContext.getBean("contextPath"));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		if (servletContext.getAttribute(CONTEXT_PATH_ATTRIBUTE_NAME) != null) {
			servletContext.removeAttribute(CONTEXT_PATH_ATTRIBUTE_NAME);
		}
	}

}
