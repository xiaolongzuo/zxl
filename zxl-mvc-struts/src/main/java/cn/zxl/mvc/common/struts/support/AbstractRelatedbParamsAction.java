package cn.zxl.mvc.common.struts.support;

import org.apache.log4j.Logger;

import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.orm.common.BaseEntity;

public abstract class AbstractRelatedbParamsAction<T extends BaseEntity, P> extends AbstractRelatedbAction<T> {

	private static final long serialVersionUID = -2440988576930319398L;

	private static final Logger LOGGER = LogUtil.logger(AbstractRelatedbParamsAction.class);

	private static int PARAMETERIZED_TYPE_INDEX = 1;

	protected P params;

	@SuppressWarnings("unchecked")
	public AbstractRelatedbParamsAction() {
		super();
		try {
			params = ((Class<P>) ReflectUtil.getParameterizedType(getClass(), PARAMETERIZED_TYPE_INDEX)).newInstance();
		} catch (InstantiationException instantiationException) {
			LogUtil.error(LOGGER, "Instantiation error", instantiationException);
		} catch (IllegalAccessException illegalAccessException) {
			LogUtil.error(LOGGER, "Instantiation error", illegalAccessException);
		} catch (NullPointerException nullPointerException) {
			LogUtil.error(LOGGER, "Instantiation error", nullPointerException);
		}
	}

	public P getParams() {
		return params;
	}

	public void setParams(P params) {
		this.params = params;
	}

}
