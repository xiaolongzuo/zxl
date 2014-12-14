/**
 * 
 */
package cn.zxl.orm.common.util;

import javax.persistence.Entity;

import cn.zxl.common.StringUtil;

/**
 * @author zuoxiaolong
 *
 */
public class AnnotationUtil {

	private AnnotationUtil() {
	}

	public static String getEntityAnnotationName(Class<?> clazz) {
		try {
			Entity entityAnnotation = clazz.getAnnotation(Entity.class);
			return StringUtil.isEmpty(entityAnnotation.name()) ? clazz.getSimpleName() : entityAnnotation.name();
		} catch (Exception e) {
			return clazz.getSimpleName();
		}
	}

}
