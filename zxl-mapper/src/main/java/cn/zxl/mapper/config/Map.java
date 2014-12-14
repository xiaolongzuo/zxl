package cn.zxl.mapper.config;

import java.util.HashSet;

import cn.zxl.common.BeanUtil;
import cn.zxl.common.StringUtil;
import cn.zxl.mapper.MapContext;
import cn.zxl.mapper.exception.ParseException;
import cn.zxl.mapper.filter.Filter;
import cn.zxl.mapper.support.MultipleKeyMap;

public class Map {

	private static final String WILDCARD = "*";

	private String id;

	private String direction = "left-to-right";

	private MultipleKeyMap<Filter> filterMap;

	private MultipleKeyMap<Map> subMapMap;

	private java.util.Set<String> leftFieldNameSet;

	private Rule rule;

	public Map(String id) {
		this.id = id;
		filterMap = new MultipleKeyMap<Filter>();
		subMapMap = new MultipleKeyMap<Map>();
		leftFieldNameSet = new HashSet<String>();
	}

	public Map(Rule rule) {
		this(rule, null);
	}

	public Map(Rule rule, String direction) {
		if (BeanUtil.isEmpty(rule)) {
			throw new ParseException("rule不能为空！");
		}
		this.rule = rule;
		if (!StringUtil.isEmpty(direction)) {
			this.direction = direction;
		}
		filterMap = new MultipleKeyMap<Filter>();
		subMapMap = new MultipleKeyMap<Map>();
		leftFieldNameSet = new HashSet<String>();
	}

	public void putFilterAndLeftFieldNameByRule(Filter listFilter) {
		for (String leftFieldName : rule.fieldKeySet()) {
			if (rule.getConverter(leftFieldName) instanceof Rule) {
				getSubMap(leftFieldName).putFilterAndLeftFieldNameByRule(listFilter);
				putLeftFieldName(leftFieldName);
			} else {
				putFilterAndLeftFieldName(listFilter, leftFieldName);
			}
		}
	}

	public void putFilterAndLeftFieldName(Filter filter, String leftFieldName) {
		if (leftFieldName.equals(WILDCARD)) {
			putFilterAndLeftFieldNameByRule(filter);
		} else {
			filterMap.put(filter, leftFieldName);
			leftFieldNameSet.add(leftFieldName);
		}
	}

	public void putLeftFieldName(String leftFieldName) {
		if (!leftFieldName.equals(WILDCARD)) {
			leftFieldNameSet.add(leftFieldName);
		}
	}

	public void putSubMap(Map map, String... keys) {
		subMapMap.put(map, keys);
	}

	public String getId() {
		return id;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}

	public String getDirection() {
		return direction;
	}

	public java.util.Set<String> leftFieldNameSet() {
		return leftFieldNameSet;
	}

	public Filter getFilter(String leftFieldName) {
		return filterMap.get(leftFieldName);
	}

	public Map getSubMap(String leftFieldName) {
		Map subMap = subMapMap.get(leftFieldName);
		if (subMap == null) {
			Rule tempRule = (Rule) rule.getConverter(leftFieldName);
			subMap = new Map(tempRule, direction);
			putSubMap(subMap, leftFieldName);
		}
		return subMap;
	}

	public String getSourceFieldName(String leftFieldName) {
		return leftIsSource() ? leftFieldName : rule.getRightFieldName(leftFieldName);
	}

	public String getTargetFieldName(String leftFieldName) {
		return leftIsSource() ? rule.getRightFieldName(leftFieldName) : leftFieldName;
	}

	public Object getSource(Object left, Object right) {
		return leftIsSource() ? left : right;
	}

	public Object getTarget(Object left, Object right) {
		return leftIsSource() ? right : left;
	}

	public Class<?> getSourceClass() {
		return leftIsSource() ? rule.getLeftClass() : rule.getRightClass();
	}

	public Class<?> getTargetClass(Class<?> leftClass, Class<?> rightClass) {
		return leftIsSource() ? rule.getRightClass() : rule.getLeftClass();
	}

	public Object convert(MapContext context) {
		return leftIsSource() ? rule.doLeftToRight(context) : rule.doRightToLeft(context);
	}

	public boolean leftIsSource() {
		return direction.equals("left-to-right");
	}

	public int leftFieldNameSetSize() {
		return leftFieldNameSet.size();
	}

	@Override
	public String toString() {
		return "Map [id=" + id + ", direction=" + direction + ", filterMap=" + filterMap + ", subMapMap=" + subMapMap + ", rule=" + rule + "]";
	}

}
