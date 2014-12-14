package cn.zxl.mq.rabbit.util;

import java.util.Arrays;
import java.util.List;

public abstract class PrimitiveUtil {

	private static final Class<?>[] HAS_VALUEOF_CLASSES = new Class<?>[] { Integer.class, Long.class, Short.class, Byte.class, Float.class, Double.class, Boolean.class };

	private static final List<Class<?>> HAS_VALUEOF_CLASSES_LIST = Arrays.asList(HAS_VALUEOF_CLASSES);

	public static boolean isPrimitive(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		if (HAS_VALUEOF_CLASSES_LIST.contains(clazz) || clazz == Character.class || clazz == String.class) {
			return true;
		}
		return false;
	}

}
