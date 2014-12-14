package cn.zxl.mvc.hbase.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.zxl.common.ReflectUtil;
import cn.zxl.hbase.orm.Family;
import cn.zxl.hbase.orm.Table;
import cn.zxl.mvc.common.springmvc.ActionResult;

public class ViewUtil {

	public static final String VIEW_KEY = "HBASE_VIEW_KEY";

	public static final String RESULT_KEY = "result";

	private ViewUtil() {
	}

	public static Object transfer(Object value) throws Exception {
		if (value == null) {
			return JSONObject.fromObject(new ActionResult(ActionResult.ERROR_CODE, "�޷��ؽ��"));
		}
		Class<?> clazz = value.getClass();
		if (value instanceof String) {
			return value;
		} else if (Table.class.isAssignableFrom(clazz)) {
			return JsonUtil.buildJsonObject((Table) value);
		} else if (Family.class.isAssignableFrom(clazz)) {
			return ((Family) value).toJsonObject();
		} else if (value instanceof ActionResult) {
			return JSONObject.fromObject(value);
		} else if (List.class.isAssignableFrom(clazz)) {
			JSONArray jsonArray = new JSONArray();
			List<?> list = (List<?>) value;
			Class<?> listClazz = ReflectUtil.getParameterizedType(list);
			for (int i = 0; i < list.size(); i++) {
				if (Table.class.isAssignableFrom(listClazz)) {
					jsonArray.add(JsonUtil.buildJsonObject((Table) list.get(i)));
				} else if (String.class == listClazz) {
					jsonArray.add(list.get(i));
				} else if (Family.class.isAssignableFrom(listClazz)) {
					jsonArray.add(((Family) list.get(i)).toJsonObject());
				}
			}
			return jsonArray;
		} else {
			return JSONObject.fromObject(new ActionResult(ActionResult.ERROR_CODE, "unknown data type"));
		}
	}
}
