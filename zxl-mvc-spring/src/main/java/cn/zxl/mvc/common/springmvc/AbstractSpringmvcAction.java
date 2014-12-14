package cn.zxl.mvc.common.springmvc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.BeanUtil;
import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.mvc.common.action.ContentType;
import cn.zxl.mvc.common.action.ContentType.Extension;
import cn.zxl.mvc.common.action.ErrorMessage;
import cn.zxl.orm.common.CycleSupport;

public abstract class AbstractSpringmvcAction<T> implements ApplicationContextAware, ServletContextAware {

	private static final Logger LOGGER = LogUtil.logger(AbstractSpringmvcAction.class);

	private static final String DEFAULT_CHARSET = "UTF-8";

	protected static final String INPUT = "input";

	protected static final String LIST = "list";

	protected static final String ENTITY_ATTRIBUTE = "entity";

	private Class<T> clazz;

	private String mapping;

	private ApplicationContext applicationContext;

	private ServletContext servletContext;

	@SuppressWarnings("unchecked")
	public AbstractSpringmvcAction() {
		super();
		try {
			this.clazz = ((Class<T>) ReflectUtil.getParameterizedType(getClass()));
			RequestMapping mapping = getClass().getAnnotation(RequestMapping.class);
			this.mapping = mapping.value()[0];
		} catch (NullPointerException nullPointerException) {
			LogUtil.error(LOGGER, "Instantiation error", nullPointerException);
		}
	}

	protected Class<T> getClazz() {
		return clazz;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	protected ApplicationContext getApplicationContext() throws BeansException {
		return applicationContext;
	}

	protected void setRequestAttribute(HttpServletRequest request, String name, Object value) {
		request.setAttribute(name, value);
	}

	protected void setRequestJsonAttribute(HttpServletRequest request, String name, Object value) {
		if (!BeanUtil.isEmpty(value)) {
			request.setAttribute(name, toJSONObject(value, jsonConfigForPageData(value.getClass())));
		}
	}

	protected void setRequestJsonAttribute(HttpServletRequest request, String name, Collection<?> collection) {
		if (!ArrayUtil.isEmpty(collection)) {
			request.setAttribute(name, toJSONArray(collection, jsonConfigForPageData(collection.iterator().next().getClass())));
		}
	}

	protected void setRequestJsonAttribute(HttpServletRequest request, String name, Object[] array) {
		if (!ArrayUtil.isEmpty(array)) {
			request.setAttribute(name, toJSONArray(array, jsonConfigForPageData(array[0].getClass())));
		}
	}

	protected Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	protected Object getServletContextAttribute(String name) {
		return servletContext.getAttribute(name);
	}

	protected void setServletContextAttribute(String name, Object value) {
		servletContext.setAttribute(name, value);
	}

	/**
	 * util method
	 */
	protected String viewInNamespace(String name) {
		return mapping + "/" + name;
	}

	protected ModelAndView successView() {
		ModelAndView view = new ModelAndView();
		view.addObject(new ActionResult(ActionResult.SUCCESS_CODE, ActionResult.SUCCESS_MESSAGE));
		return view;
	}

	protected ModelAndView errorView(String message) {
		ModelAndView view = new ModelAndView();
		view.addObject(new ActionResult(ActionResult.ERROR_CODE, message == null ? ErrorMessage.DEFAULT.getErrorMessage() : message));
		return view;
	}

	protected ModelAndView successView(String key) {
		ModelAndView view = new ModelAndView();
		view.addObject(key, new ActionResult(ActionResult.SUCCESS_CODE, ActionResult.SUCCESS_MESSAGE));
		return view;
	}

	protected ModelAndView errorView(String key, String message, Exception exception) {
		if (message == null) {
			throw new NullPointerException("the arg [message] is required!");
		}
		if (exception != null) {
			LogUtil.error(LOGGER, message, exception);
		} else {
			LogUtil.error(LOGGER, message);
		}
		ModelAndView view = new ModelAndView("error");
		view.addObject(key, new ActionResult(ActionResult.ERROR_CODE, message));
		return view;
	}

	protected boolean isXmlResponse(HttpServletRequest request) {
		return request.getRequestURI().endsWith(".xml");
	}

	protected String toJSONObject(Object object) {
		return toJSONObject(object, null);
	}

	protected String toJSONObject(Object object, JsonConfig jsonConfig) {
		return jsonConfig == null ? JSONObject.fromObject(object).toString() : JSONObject.fromObject(object, jsonConfig).toString();
	}

	protected String toJSONArray(Collection<?> collection) {
		return toJSONArray(collection, null);
	}

	protected String toJSONArray(Collection<?> collection, JsonConfig jsonConfig) {
		return jsonConfig == null ? JSONArray.fromObject(collection).toString() : JSONArray.fromObject(collection, jsonConfig).toString();
	}

	protected String toJSONArray(Object[] array) {
		return toJSONArray(array, null);
	}

	protected String toJSONArray(Object[] array, JsonConfig jsonConfig) {
		return jsonConfig == null ? JSONArray.fromObject(array).toString() : JSONArray.fromObject(array, jsonConfig).toString();
	}

	protected JsonConfig jsonConfigForPageData(T entity) {
		return CycleSupport.class.isAssignableFrom(entity.getClass()) ? ((CycleSupport) entity).jsonConfig() : null;
	}

	protected JsonConfig jsonConfigForPageData(Class<?> clazz) {
		try {
			return CycleSupport.class.isAssignableFrom(clazz) ? ((CycleSupport) clazz.newInstance()).jsonConfig() : null;
		} catch (Exception e) {
			LogUtil.warn(LOGGER, "new instance error", e);
			return null;
		}
	}

	/**
	 * util method
	 */
	protected void ajaxPrimitiveType(HttpServletResponse response, Object primitiveObject) {
		if (primitiveObject == null) {
			throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
		}
		ajax(response, primitiveObject.toString());
	}

	protected void ajaxJson(HttpServletResponse response, Object object) {
		ajaxJson(response, object, null);
	}

	protected void ajaxList(HttpServletResponse response, Collection<?> collection) {
		ajaxList(response, collection, null);
	}

	protected void ajaxJson(HttpServletResponse response, Object object, JsonConfig jsonConfig) {
		ajax(response, toJSONObject(object, jsonConfig));
	}

	protected void ajaxList(HttpServletResponse response, Collection<?> collection, JsonConfig jsonConfig) {
		ajax(response, toJSONArray(collection, jsonConfig));
	}

	private void ajax(HttpServletResponse response, String content) {
		ajax(response, content, ContentType.getContentType(Extension.txt), DEFAULT_CHARSET);
	}

	private void ajax(HttpServletResponse response, String content, String contentType, String charset) {
		response.setCharacterEncoding(charset);
		response.setContentType(contentType);
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			printWriter.write(content);
			printWriter.flush();
		} catch (IOException ioException) {
			throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
		} finally {
			printWriter.close();
		}
	}

	protected final void ajaxStream(HttpServletResponse response, byte[] bytes) {
		ajaxStream(response, bytes, null);
	}

	protected final void ajaxStream(HttpServletResponse response, byte[] bytes, String charset) {
		response.setCharacterEncoding(charset);
		response.setContentType(ContentType.getContentType(Extension.all));
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			outputStream.write(bytes, 0, bytes.length);
			outputStream.flush();
		} catch (IOException ioException) {
			throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
			}
		}
	}

}
