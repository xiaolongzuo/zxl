/**
 * 
 */
package cn.zxl.orm.common.util;

import java.util.Collection;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.BaseEntity;

/**
 * @author zuoxiaolong
 *
 */
public class EntityUtil {

	private EntityUtil() {
	}

	public static <T extends BaseEntity> String forTransientId(String id, T entity) {
		return !StringUtil.isEmpty(id) ? id : (entity == null ? id : entity.getId());
	}

	public static <T extends BaseEntity> String forTransientId(String ids, Collection<T> entityList) {
		if (!StringUtil.isEmpty(ids) || ArrayUtil.isEmpty(entityList)) {
			return ids;
		}
		ids = "";
		int i = 0;
		for (T entity : entityList) {
			if (i == 0) {
				ids = entity.getId();
			} else {
				ids = ids + "," + entity.getId();
			}
			i++;
		}
		return ids;
	}

}
