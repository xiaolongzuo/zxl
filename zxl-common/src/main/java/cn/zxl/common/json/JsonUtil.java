package cn.zxl.common.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;

public abstract class JsonUtil {

	private static final Logger LOGGER = LogUtil.logger(JsonUtil.class);

	public static void copyToBean(JSONObject jsonObject, Object bean) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (bean == null || jsonObject == null) {
			throw new NullPointerException("jsonObject:" + jsonObject + "    bean:" + bean);
		}
		Class<?> clazz = bean.getClass();
		for (Object key : jsonObject.keySet()) {
			try {
				Field field = clazz.getDeclaredField(key.toString());
				ReflectUtil.setFieldValue(bean, field, jsonObject.get(key));
			} catch (NoSuchFieldException e) {
				LogUtil.warn(LOGGER, "copyToBean未找到该属性：" + key.toString());
				continue;
			}
		}
	}
}
