package cn.zxl.mvc.common.struts.support;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import cn.zxl.mvc.common.struts.AbstractStrutsAction;
import cn.zxl.orm.common.BaseEntity;

import com.opensymphony.xwork2.ModelDriven;

public abstract class AbstractRelatedbAction<T extends BaseEntity> extends AbstractStrutsAction<T> implements ApplicationContextAware, ServletContextAware, ServletRequestAware, ServletResponseAware, SessionAware, ModelDriven<T> {

	private static final long serialVersionUID = -2629565671192923347L;

}
