package cn.zxl.mapper;

import cn.zxl.mapper.config.Map;
import cn.zxl.mapper.config.XmlParser;
import cn.zxl.mapper.exception.MapException;

public class Mapper {

	private XmlParser xmlParser;

	public Mapper() throws Exception {
		xmlParser = new XmlParser();
	}

	public Mapper(String globalPath) throws Exception {
		xmlParser = new XmlParser(globalPath);
	}

	public Mapper(String mapperMode, String mapperPath) throws Exception {
		xmlParser = new XmlParser(mapperMode, mapperPath);
	}

	public Object map(String mapKey, Object left, Object right) throws Exception {
		Map map = xmlParser.getMap(mapKey);
		if (left == null) {
			left = map.getRule().getLeftClass().newInstance();
		}
		if (right == null) {
			right = map.getRule().getRightClass().newInstance();
		}
		if (left.getClass() != map.getRule().getLeftClass() || right.getClass() != map.getRule().getRightClass()) {
			throw new MapException("source对象或target对象类型不符！");
		}
		MapContext context = new MapContext(this, map, left, right);
		return map.convert(context);
	}

	@SuppressWarnings("unchecked")
	public <T> T map(T source, T target) throws Exception {
		if (target == null) {
			target = (T) source.getClass().newInstance();
		}
		Map map = xmlParser.getMap(source.getClass());
		if (source.getClass() != target.getClass()) {
			throw new MapException("source对象或target对象类型不符！");
		}
		MapContext context = new MapContext(this, map, source, target);
		return (T) map.convert(context);
	}

}
