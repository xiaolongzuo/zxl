package cn.zxl.mapper;

import cn.zxl.common.ReflectUtil;
import cn.zxl.mapper.config.Map;

public class MapContext {

	private Mapper mapper;

	private Map map;

	private Object left;

	private Object right;

	private String leftFieldName;

	public MapContext(Mapper mapper, Map map, Object left, Object right) {
		super();
		this.mapper = mapper;
		this.map = map;
		try {
			this.left = (left == null ? map.getRule().getLeftClass().newInstance() : left);
			this.right = (right == null ? map.getRule().getRightClass().newInstance() : right);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void setLeftFieldName(String leftFieldName) {
		this.leftFieldName = leftFieldName;
	}

	public String getLeftFieldName() {
		return leftFieldName;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public Map getMap() {
		return map;
	}

	public Object getLeft() {
		return left;
	}

	public Object getRight() {
		return right;
	}

	public Object getSource() {
		return map.getSource(left, right);
	}

	public Object getTarget() {
		return map.getTarget(left, right);
	}

	public String getSourceFieldName() {
		return map.getSourceFieldName(leftFieldName);
	}

	public String getTargetFieldName() {
		return map.getTargetFieldName(leftFieldName);
	}

	public Object getSourceFieldValue() {
		return ReflectUtil.getFieldValue(getSource(), getSourceFieldName());
	}

	public Object getTargetFieldValue() {
		return ReflectUtil.getFieldValue(getTarget(), getTargetFieldName());
	}

}
