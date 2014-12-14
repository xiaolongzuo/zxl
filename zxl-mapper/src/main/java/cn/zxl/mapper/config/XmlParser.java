package cn.zxl.mapper.config;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.zxl.common.ClassUtil;
import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.common.StringUtil;
import cn.zxl.mapper.exception.NoSuchMapException;
import cn.zxl.mapper.exception.NoSuchRuleException;
import cn.zxl.mapper.exception.ParseException;
import cn.zxl.mapper.support.Converter;
import cn.zxl.mapper.support.Converters;

public class XmlParser {

	private static final Logger LOGGER = LogUtil.logger(XmlParser.class);

	private static final String GLOBAL_PATH = "global.xml";

	private static final String PRODUCT_MODE = "pro";

	private String mapperMode = PRODUCT_MODE;

	private String mapperPath;

	private java.util.Map<String, Element> ruleElementMap;

	private java.util.Map<String, Element> mapElementMap;

	private java.util.Map<String, Rule> ruleMap;

	private java.util.Map<String, Map> mapMap;

	private java.util.Map<Class<?>, Map> classMap;

	private String globalPath;

	public XmlParser() throws Exception {
		this(GLOBAL_PATH);
	}

	public XmlParser(String globalPath) throws Exception {

		this.globalPath = globalPath;

		this.classMap = new HashMap<Class<?>, Map>();

		loadGlobalConfiguation();

		if (isProductMode()) {
			loadMapperConfiguration();
			loadRuleAndMap();
		}
	}

	public XmlParser(String mapperMode, String mapperPath) throws Exception {

		this.mapperMode = mapperMode;

		this.mapperPath = mapperPath;

		this.classMap = new HashMap<Class<?>, Map>();

		if (isProductMode()) {
			loadMapperConfiguration();
			loadRuleAndMap();
		}
	}

	private DocumentBuilder getDocumentBuilder() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		return factory.newDocumentBuilder();
	}

	private synchronized void loadGlobalConfiguation() throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(globalPath);
		Document globalDocument = getDocumentBuilder().parse(inputStream);
		NodeList mapperModeNodes = globalDocument.getElementsByTagName("mapper-mode");
		if (mapperModeNodes != null && mapperModeNodes.getLength() > 1) {
			throw new ParseException("mapper-mode标签只能有一个！");
		}
		if (mapperModeNodes != null && mapperModeNodes.getLength() == 1) {
			mapperMode = mapperModeNodes.item(0).getTextContent();
		}
		NodeList mapperPathNodes = globalDocument.getElementsByTagName("mapper-path");
		if (mapperPathNodes != null && mapperPathNodes.getLength() > 1) {
			throw new ParseException("mapper-path标签只能有一个！");
		}
		if (mapperPathNodes != null && mapperPathNodes.getLength() == 1) {
			mapperPath = mapperPathNodes.item(0).getTextContent();
		}
		inputStream.close();
		if (isProductMode()) {
			LogUtil.info(LOGGER, "-----------------进入生产模式-----------------");
		} else {
			LogUtil.info(LOGGER, "-----------------进入开发模式-----------------");
		}
	}

	private boolean isProductMode() {
		return mapperMode.equals(PRODUCT_MODE);
	}

	private synchronized void loadMapperConfiguration() throws Exception {
		LogUtil.info(LOGGER, "-------------开始加载mapper配置-------------");
		ruleElementMap = new HashMap<String, Element>();
		mapElementMap = new HashMap<String, Element>();
		File[] mapperFiles = findMapperFiles();
		for (int i = 0; i < mapperFiles.length; i++) {
			InputStream inputStream = new FileInputStream(mapperFiles[i]);
			Document document = getDocumentBuilder().parse(inputStream);
			loadRuleAndMapElement(document.getDocumentElement());
			inputStream.close();
		}
		LogUtil.info(LOGGER, "-------------加载mapper配置完毕：［rule:" + ruleElementMap.size() + ",map:" + mapElementMap.size() + "］-------------");
	}

	private synchronized File[] findMapperFiles() {
		File[] mapperFiles = new File[0];
		try {
			File file = new File(getClass().getClassLoader().getResource(mapperPath).getPath());
			mapperFiles = file.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (pathname.getName().endsWith(".xml")) {
						return true;
					}
					return false;
				}
			});
		} catch (Exception e) {
			LogUtil.warn(LOGGER, "寻找mapper配置文件失败：" + e.getMessage());
		}
		return mapperFiles;
	}

	private synchronized void loadRuleAndMapElement(Element rootElement) {
		NodeList nodeList = rootElement.getChildNodes();
		for (int j = 0; j < nodeList.getLength(); j++) {
			Node node = nodeList.item(j);
			if (node instanceof Element) {
				Element element = (Element) node;
				if (element.getNodeName().equals("rule")) {
					String id = element.getAttribute("id");
					if (StringUtil.isEmpty(id)) {
						throw new ParseException("id属性为必选项！");
					}
					ruleElementMap.put(id, element);
				} else if (element.getNodeName().equals("map")) {
					String id = element.getAttribute("id");
					if (StringUtil.isEmpty(id)) {
						throw new ParseException("id属性为必选项！");
					}
					mapElementMap.put(id, element);
				}
			}
		}
	}

	private synchronized void loadRuleAndMap() throws Exception {
		ruleMap = new HashMap<String, Rule>();
		mapMap = new HashMap<String, Map>();

		for (String rule : ruleElementMap.keySet()) {
			ruleMap.put(rule, parseRule(rule));
		}

		for (String map : mapElementMap.keySet()) {
			mapMap.put(map, parseMap(map));
		}
	}

	private Rule parseRule(String ruleId) throws Exception {
		if (StringUtil.isEmpty(ruleId)) {
			throw new ParseException("ruleId不能为空！");
		}
		Rule rule = new Rule(ruleId);
		Element element = ruleElementMap.get(ruleId);
		if (element == null) {
			throw new NoSuchRuleException();
		}
		try {
			String leftType = element.getAttribute("left-type");
			if (StringUtil.isEmpty(leftType)) {
				throw new ParseException("left-type属性为必选项！");
			}
			rule.setLeftClass(Class.forName(leftType));
			String rightType = element.getAttribute("right-type");
			if (StringUtil.isEmpty(rightType)) {
				throw new ParseException("right-type属性为必选项！");
			}
			rule.setRightClass(Class.forName(rightType));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element && node.getNodeName().equals("relation")) {
				Relation relation = parseRelation((Element) node);
				Converter converter = findConverter(rule, relation);
				rule.putConverterAndPutField(converter, relation.getLeftName(), relation.getRightName());
			}
		}
		if (rule.converterMapSize() == 0) {
			throw new ParseException("至少需要一个relation标签！");
		}
		return rule;
	}

	private Converter findConverter(Rule rule, Relation relation) throws Exception {
		Class<?> leftFieldClass = ReflectUtil.getAllField(rule.getLeftClass(), relation.getLeftName()).getType();
		Class<?> rightFieldClass = ReflectUtil.getAllField(rule.getRightClass(), relation.getRightName()).getType();
		if (StringUtil.isEmpty(relation.getRule())) {
			if (ClassUtil.isPrimitiveClass(leftFieldClass) && ClassUtil.isPrimitiveClass(rightFieldClass)) {
				return Converters.getConverter(rule.getLeftClass(), rule.getRightClass(), relation);
			} else {
				if (leftFieldClass != rightFieldClass) {
					throw new ParseException("未指定嵌套规则，但relation中" + relation.getLeftName() + "与" + relation.getRightName() + "的类型不相同！");
				} else {
					return new Rule(leftFieldClass);
				}
			}
		}
		if (relation.isReverse()) {
			return getRule(relation.getRule()).reverseRule();
		} else {
			return getRule(relation.getRule());
		}
	}

	private Relation parseRelation(Element element) {
		String leftName = element.getAttribute("left-name");
		if (StringUtil.isEmpty(leftName)) {
			throw new ParseException("left-name属性为必选项！");
		}
		String rightName = element.getAttribute("right-name");
		if (StringUtil.isEmpty(rightName)) {
			throw new ParseException("right-name属性为必选项！");
		}
		String dateFormat = element.getAttribute("date-format");
		String rule = element.getAttribute("rule");
		String reverseString = element.getAttribute("reverse");
		boolean reverse = false;
		if (!StringUtil.isEmpty(reverseString)) {
			reverse = Boolean.valueOf(reverseString);
		}
		return new Relation(leftName, rightName, dateFormat, rule, reverse);
	}

	private Map parseMap(String mapId) throws Exception {
		if (StringUtil.isEmpty(mapId)) {
			throw new ParseException("mapId不能为空！");
		}
		Map map = new Map(mapId);
		Element element = mapElementMap.get(mapId);
		if (element == null) {
			throw new NoSuchMapException();
		}
		String rule = element.getAttribute("rule");
		String direction = element.getAttribute("direction");
		String type = element.getAttribute("type");
		if (!StringUtil.isEmpty(rule) && !StringUtil.isEmpty(direction)) {
			map.setRule(getRule(rule));
			map.setDirection(direction);
		} else if (!StringUtil.isEmpty(type)) {
			map.setRule(new Rule(Class.forName(type)));
		} else {
			throw new ParseException("map标签的属性不合法！");
		}
		NodeList nodeList = element.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element && node.getNodeName().equals("set")) {
				Set set = parseSet((Element) node);
				String[] leftFieldNames = set.getLeftFieldName().split("\\.");
				Map childMap = map;
				for (int j = 1; j < leftFieldNames.length; j++) {
					childMap = childMap.getSubMap(leftFieldNames[j - 1]);
				}
				childMap.putFilterAndLeftFieldName(set.getListFilter(), leftFieldNames[leftFieldNames.length - 1]);
				map.putLeftFieldName(leftFieldNames[0]);
			}
		}
		if (map.leftFieldNameSetSize() == 0) {
			throw new ParseException("至少需要一个set标签！");
		}
		return map;
	}

	private Set parseSet(Element element) {
		String leftFieldName = element.getAttribute("left-field-name");
		if (StringUtil.isEmpty(leftFieldName)) {
			throw new ParseException("field-name属性为必选项！");
		}
		boolean ifSourceIsNotNull = Boolean.valueOf(element.getAttribute("if-source-is-not-null"));
		boolean ifSourceIsNotEmpty = Boolean.valueOf(element.getAttribute("if-source-is-not-empty"));
		boolean ifTargetIsNull = Boolean.valueOf(element.getAttribute("if-target-is-null"));
		boolean ifTargetIsEmpty = Boolean.valueOf(element.getAttribute("if-target-is-empty"));
		return new Set(leftFieldName, ifSourceIsNotNull, ifSourceIsNotEmpty, ifTargetIsNull, ifTargetIsEmpty);
	}

	public Rule getRule(String rule) throws Exception {
		if (!isProductMode()) {
			loadMapperConfiguration();
			return parseRule(rule);
		}
		if (ruleMap.containsKey(rule)) {
			return ruleMap.get(rule);
		} else if (ruleElementMap.containsKey(rule)) {
			return parseRule(rule);
		} else {
			throw new NoSuchRuleException();
		}
	}

	public Map getMap(String map) throws Exception {
		if (!isProductMode()) {
			loadMapperConfiguration();
			return parseMap(map);
		}
		if (mapMap.containsKey(map)) {
			return mapMap.get(map);
		} else if (mapElementMap.containsKey(map)) {
			return parseMap(map);
		} else {
			throw new NoSuchMapException();
		}
	}

	public Map getMap(Class<?> clazz) throws Exception {
		Map map = classMap.get(clazz);
		if (map == null) {
			synchronized (classMap) {
				map = classMap.get(clazz);
				if (map == null) {
					map = new Map(new Rule(clazz));
					map.putFilterAndLeftFieldNameByRule(null);
					classMap.put(clazz, map);
				}
			}
		}
		return map;
	}
}
