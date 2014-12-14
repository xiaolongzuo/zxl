package cn.zxl.common;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class ClassUtil {

	private static final Class<?>[] PRIMITIVE_CLASSES = new Class<?>[] { String.class, Date.class, Character.class, Integer.class, Long.class, Short.class, Byte.class, Float.class, Double.class, Boolean.class };

	private static final List<Class<?>> PRIMITIVE_CLASSES_LIST = Arrays.asList(PRIMITIVE_CLASSES);

	public static boolean isPrimitiveClass(Class<?> clazz) {
		return PRIMITIVE_CLASSES_LIST.contains(clazz);
	}

	public static boolean isMapClass(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}

}
