package cn.zxl.mapper.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import cn.zxl.common.ClassUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.mapper.MapContext;
import cn.zxl.mapper.exception.InvalidKeyException;
import cn.zxl.mapper.filter.Filter;
import cn.zxl.mapper.support.Converter;
import cn.zxl.mapper.support.Converters;
import cn.zxl.mapper.support.MultipleKeyMap;

public class Rule extends Converter {

	private String id;

	private Class<?> leftClass;

	private Class<?> rightClass;

	private MultipleKeyMap<Converter> converterMap;

	private MultipleKeyMap<String> fieldMap;

	private Rule() {
		converterMap = new MultipleKeyMap<Converter>();
		fieldMap = new MultipleKeyMap<String>();
	}

	public Rule(String id) {
		this.id = id;
		converterMap = new MultipleKeyMap<Converter>();
		fieldMap = new MultipleKeyMap<String>();
	}

	public Rule(Class<?> type) {
		leftClass = type;
		rightClass = type;
		converterMap = new MultipleKeyMap<Converter>();
		fieldMap = new MultipleKeyMap<String>();
		Field[] fields = ReflectUtil.getAllFields(type);
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			Class<?> fieldType = fields[i].getType();
			if (!Modifier.isStatic(fields[i].getModifiers()) && !Modifier.isFinal(fields[i].getModifiers())) {
				if (ClassUtil.isPrimitiveClass(fieldType)) {
					putConverterAndPutField(Converters.getDefaultConverter(new Relation(fieldName, fieldName, null, null, false)), fieldName, fieldName);
				} else {
					putConverterAndPutField(new Rule(fieldType), fieldName, fieldName);
				}
			}
		}
	}

	public Object doLeftToRight(MapContext context) {
		for (String leftFieldName : context.getMap().leftFieldNameSet()) {
			context.setLeftFieldName(leftFieldName);
			Converter converter = getConverter(leftFieldName);
			Object value = null;
			if (converter instanceof Rule) {
				value = getNestedValue(context);
			} else if (doFilter(context)) {
				value = converter.doLeftToRight(context);
			} else {
				continue;
			}
			ReflectUtil.setFieldValue(context.getRight(), fieldMap.get(leftFieldName), value);
		}
		return context.getRight();
	}

	public Object doRightToLeft(MapContext context) {
		for (String leftFieldName : context.getMap().leftFieldNameSet()) {
			context.setLeftFieldName(leftFieldName);
			Converter converter = getConverter(leftFieldName);
			Object value = null;
			if (converter instanceof Rule) {
				value = getNestedValue(context);
			} else if (doFilter(context)) {
				value = converter.doRightToLeft(context);
			} else {
				continue;
			}
			ReflectUtil.setFieldValue(context.getLeft(), leftFieldName, value);
		}
		return context.getLeft();
	}

	private Object getNestedValue(MapContext context) {
		Object leftFieldValue = ReflectUtil.getFieldValue(context.getLeft(), context.getLeftFieldName());
		Object rightFieldValue = ReflectUtil.getFieldValue(context.getRight(), fieldMap.get(context.getLeftFieldName()));
		Map map = context.getMap().getSubMap(context.getLeftFieldName());
		return map.convert(new MapContext(context.getMapper(), map, leftFieldValue, rightFieldValue));
	}

	private boolean doFilter(MapContext context) {
		Filter filter = context.getMap().getFilter(context.getLeftFieldName());
		if (filter != null && !filter.doFilter(context)) {
			return false;
		}
		return true;
	}

	public String getId() {
		return id;
	}

	public String getRightFieldName(String leftFieldName) {
		return fieldMap.get(leftFieldName);
	}

	public void setLeftClass(Class<?> leftClass) {
		this.leftClass = leftClass;
	}

	public void setRightClass(Class<?> rightClass) {
		this.rightClass = rightClass;
	}

	public Class<?> getLeftClass() {
		return leftClass;
	}

	public Class<?> getRightClass() {
		return rightClass;
	}

	public void putConverterAndPutField(Converter converter, String leftName, String rightName) {
		converterMap.put(converter, leftClass.getName(), leftName, rightClass.getName(), rightName);
		fieldMap.put(rightName, leftName);
	}

	public Converter getConverter(String leftFieldName) {
		return converterMap.get(leftClass.getName(), leftFieldName, rightClass.getName(), getRightFieldName(leftFieldName)) == null ? converterMap.get(rightClass.getName(), getRightFieldName(leftFieldName), leftClass.getName(), leftFieldName) : converterMap.get(leftClass.getName(), leftFieldName, rightClass.getName(), getRightFieldName(leftFieldName));
	}

	public Set<String> fieldKeySet() {
		return fieldMap.keySet();
	}

	public int converterMapSize() {
		return converterMap.keySet().size();
	}

	public Rule reverseRule() {
		Rule rule = new Rule();
		rule.leftClass = this.rightClass;
		rule.rightClass = this.leftClass;
		for (String leftFieldName : this.fieldMap.keySet()) {
			rule.fieldMap.put(leftFieldName, this.fieldMap.get(leftFieldName));
		}
		for (String key : this.converterMap.keySet()) {
			String[] keys = converterMap.splitKey(key);
			if (keys == null || keys.length != 4) {
				throw new InvalidKeyException();
			}
			String leftClassName = keys[0];
			String leftName = keys[1];
			String rightClassName = keys[2];
			String rightName = keys[3];
			Converter converter = converterMap.get(key);
			if (converter instanceof Rule) {
				rule.converterMap.put(((Rule) converter).reverseRule(), rightClassName, rightName, leftClassName, leftName);
			} else {
				rule.converterMap.put(Converters.getReverseConverter(converter), rightClassName, rightName, leftClassName, leftName);
			}
		}
		return rule;
	}

	@Override
	public String toString() {
		return "Rule [id=" + id + ", leftClass=" + leftClass + ", rightClass=" + rightClass + ", converterMap=" + converterMap + ", fieldMap=" + fieldMap + "]";
	}

}
