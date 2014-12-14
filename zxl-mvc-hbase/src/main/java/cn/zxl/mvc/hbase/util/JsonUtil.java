package cn.zxl.mvc.hbase.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.hbase.orm.Family;
import cn.zxl.hbase.orm.Table;
import cn.zxl.hbase.orm.util.HBaseUtil;

public class JsonUtil {

	private static final Logger LOGGER = LogUtil.logger(JsonUtil.class);

	private JsonUtil() {
	}

	public static <E extends Table> Table parse(JSONObject jsonObject, Class<E> clazz) throws Exception {
		return parse(jsonObject.toString(), clazz);
	}

	public static <E extends Table> Table parse(String json, Class<E> clazz) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(json);
		E entity = clazz.newInstance();
		if (jsonObject.containsKey("id"))
			entity.setId(jsonObject.getString("id"));
		for (Field field : ReflectUtil.getAllFields(clazz)) {
			if (!HBaseUtil.isFamily(field)) {
				continue;
			}
			if (HBaseUtil.isBaseFamily(field)) {
				Family family = new Family();
				family.putJsonObject(jsonObject.getJSONObject(field.getName()));
				ReflectUtil.setFieldValue(entity, field, family);
			} else if (HBaseUtil.isArrayFamily(field)) {
				JSONArray familyArray = jsonObject.getJSONArray(field.getName());
				Class<?> genericClass = ReflectUtil.getParameterizedType(field);
				List<Object> list = new ArrayList<Object>();
				if (genericClass == String.class) {
					for (int i = 0; i < familyArray.size(); i++) {
						list.add(familyArray.getString(i));
					}
				} else if (Family.class.isAssignableFrom(genericClass)) {
					for (int i = 0; i < familyArray.size(); i++) {
						list.add(new Family(familyArray.getJSONObject(i)));
					}
				} else {
					LogUtil.warn(LOGGER, field.getName() + "����Family���ͣ�");
				}
			} else {
				LogUtil.warn(LOGGER, "������ݿ��ѯ���ʱ��������[" + field.getName() + "]");
			}
		}
		return entity;
	}

	public static <E extends Table> String buildJsonString(E entity) throws Exception {
		return buildJsonObject(entity).toString();
	}

	@SuppressWarnings("unchecked")
	public static <E extends Table> JSONObject buildJsonObject(E entity) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", entity.getId());
		Class<E> clazz = (Class<E>) entity.getClass();
		for (Field field : ReflectUtil.getAllFields(clazz)) {
			if (!HBaseUtil.isFamily(field)) {
				continue;
			}
			if (HBaseUtil.isBaseFamily(field)) {
				jsonObject.put(field.getName(), ((Family) ReflectUtil.getFieldValue(entity, field)).toJsonObject());
			} else if (HBaseUtil.isArrayFamily(field)) {
				JSONArray familyArray = new JSONArray();
				Class<?> genericClass = ReflectUtil.getParameterizedType(field);
				List<Object> list = (List<Object>) ReflectUtil.getFieldValue(entity, field);
				if (genericClass == String.class) {
					for (int i = 0; i < list.size(); i++) {
						familyArray.add(list.get(i));
					}
				} else if (Family.class.isAssignableFrom(genericClass)) {
					for (int i = 0; i < list.size(); i++) {
						familyArray.add(((Family) list.get(i)).toJsonObject());
					}
				} else {
					LogUtil.warn(LOGGER, field.getName() + "����Family���ͣ�");
				}
				jsonObject.put(field.getName(), familyArray);
			} else {
				LogUtil.warn(LOGGER, "������ݿ��ѯ���ʱ��������[" + field.getName() + "]");
			}
		}
		return jsonObject;
	}
}
