package cn.zxl.mvc.common.struts;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.BeanUtil;
import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.mvc.common.action.ContentType;
import cn.zxl.mvc.common.action.ContentType.Extension;
import cn.zxl.mvc.common.action.ErrorMessage;
import cn.zxl.orm.common.CycleSupport;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class AbstractStrutsAction<T> extends ActionSupport implements ApplicationContextAware, ServletContextAware, ServletRequestAware, ServletResponseAware, SessionAware, ModelDriven<T> {

	private static final long serialVersionUID = -5236789144099156153L;

	private static final Logger LOGGER = LogUtil.logger(AbstractStrutsAction.class);

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final int SUCCESS_CODE = 200;

	private static final int ERROR_CODE = 500;

	private static final String CODE_KEY = "code";

	private static final String MESSAGE_KEY = "message";

	private static final String SUCCESS_MESSAGE = "success";

	protected static final String LIST = "list";

	private Class<T> clazz;

	protected T entity;

	private ApplicationContext applicationContext;

	private ServletContext servletContext;

	private HttpServletRequest servletRequest;

	private HttpServletResponse servletResponse;

	private Map<String, Object> session;

	@SuppressWarnings("unchecked")
	public AbstractStrutsAction() {
		super();
		try {
			clazz = ((Class<T>) ReflectUtil.getParameterizedType(getClass()));
			if (!Modifier.isAbstract(clazz.getModifiers())) {
				entity = clazz.newInstance();
			}
		} catch (InstantiationException instantiationException) {
			LogUtil.error(LOGGER, "Instantiation error", instantiationException);
		} catch (IllegalAccessException illegalAccessException) {
			LogUtil.error(LOGGER, "Instantiation error", illegalAccessException);
		} catch (NullPointerException nullPointerException) {
			LogUtil.error(LOGGER, "Instantiation error", nullPointerException);
		}
	}

	/**
	 * base method
	 */
	@Override
	public T getModel() {
		return entity;
	}

	public T getEntity() {
		return entity;
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

	@Override
	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

	@Override
	public void setServletResponse(HttpServletResponse servletResponse) {
		this.servletResponse = servletResponse;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	protected ApplicationContext getApplicationContext() throws BeansException {
		return applicationContext;
	}

	protected HttpServletRequest getHttpServletRequest() {
		return servletRequest;
	}

	protected void setRequestAttribute(String name, Object value) {
		servletRequest.setAttribute(name, value);
	}

	protected void setRequestJsonAttribute(String name, Object value) {
		if (!BeanUtil.isEmpty(value)) {
			setRequestAttribute(name, toJSONObject(value, jsonConfigForPageData(value.getClass())));
		}
	}

	protected void setRequestJsonAttribute(String name, Collection<?> collection) {
		if (!ArrayUtil.isEmpty(collection)) {
			setRequestAttribute(name, toJSONArray(collection, jsonConfigForPageData(collection.iterator().next().getClass())));
		}
	}

	protected void setRequestJsonAttribute(String name, Object[] array) {
		if (!ArrayUtil.isEmpty(array)) {
			setRequestAttribute(name, toJSONArray(array, jsonConfigForPageData(array[0].getClass())));
		}
	}

	protected HttpServletResponse getHttpServletResponse() {
		return servletResponse;
	}

	protected Map<String, Object> getSession() {
		return session;
	}

	protected Object getSession(String key) {
		return session.get(key);
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
	protected void ajaxPrimitiveType(Object primitiveObject) {
		if (primitiveObject == null) {
			throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
		}
		ajax(primitiveObject.toString());
	}

	protected void ajaxJson(Object object) {
		ajaxJson(object, null);
	}

	protected void ajaxList(Collection<?> collection) {
		ajaxList(collection, null);
	}

	protected void ajaxJson(Object object, JsonConfig jsonConfig) {
		ajax(toJSONObject(object, jsonConfig));
	}

	protected void ajaxList(Collection<?> collection, JsonConfig jsonConfig) {
		ajax(toJSONArray(collection, jsonConfig));
	}

	protected void ajaxSuccess() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE_KEY, SUCCESS_CODE);
		result.put(MESSAGE_KEY, SUCCESS_MESSAGE);
		ajax(toJSONObject(result));
	}

	protected void ajaxError(String message, Exception exception) {
		if (message == null) {
			throw new NullPointerException("the arg [message] is required!");
		}
		if (exception != null) {
			LogUtil.error(LOGGER, message, exception);
		} else {
			LogUtil.error(LOGGER, message);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(CODE_KEY, ERROR_CODE);
		result.put(MESSAGE_KEY, message);
		ajax(toJSONObject(result));
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

	private void ajax(String content) {
		ajax(content, ContentType.getContentType(Extension.txt), DEFAULT_CHARSET);
	}

	private void ajax(String content, String contentType, String charset) {
		servletResponse.setCharacterEncoding(charset);
		servletResponse.setContentType(contentType);
		PrintWriter printWriter = null;
		try {
			printWriter = servletResponse.getWriter();
			printWriter.write(content);
			printWriter.flush();
		} catch (IOException ioException) {
			throw new RuntimeException(ErrorMessage.NO_RESULT.getErrorMessage());
		} finally {
			printWriter.close();
		}
	}

	protected final void ajaxStream(byte[] bytes) {
		ajaxStream(bytes, null);
	}

	protected final void ajaxStream(byte[] bytes, String charset) {
		servletResponse.setCharacterEncoding(charset);
		servletResponse.setContentType(ContentType.getContentType(Extension.all));
		OutputStream outputStream = null;
		try {
			outputStream = servletResponse.getOutputStream();
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

	protected JsonConfig jsonConfigForPageData() {
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

}
