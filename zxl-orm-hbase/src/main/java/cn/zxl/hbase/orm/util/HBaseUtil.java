package cn.zxl.hbase.orm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import cn.zxl.common.ReflectUtil;
import cn.zxl.hbase.orm.Family;
import cn.zxl.hbase.orm.Table;

public class HBaseUtil {

	private HBaseUtil() {
	}

	public static boolean isFamily(Field field) {
		return isBaseFamily(field) || isArrayFamily(field);
	}

	public static boolean isBaseFamily(Field field) {
		return Family.class.isAssignableFrom(field.getType());
	}

	public static boolean isArrayFamily(Field field) {
		return List.class.isAssignableFrom(field.getType()) && (Family.class.isAssignableFrom(ReflectUtil.getParameterizedType(field)) || String.class.isAssignableFrom(ReflectUtil.getParameterizedType(field)));
	}

	public static boolean isTable(Class<?> clazz) {
		return Table.class.isAssignableFrom(clazz);
	}

	public static boolean isColumn(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers) && (field.getType() == String.class || field.getType().isArray() || field.getType() == Integer.class || field.getType() == Long.class || field.getType() == BigDecimal.class || field.getType() == Double.class || field.getType() == Float.class || field.getType() == Short.class);
	}

}
