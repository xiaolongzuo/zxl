package cn.zxl.common.json;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

public abstract class JsonConfigs {

	public static JsonConfig propertiesFilter(final String... names) {
		final List<String> nameList = Arrays.asList(names);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if (nameList.contains(name)) {
					return true;
				}
				return false;
			}
		});
		return jsonConfig;
	}

	public static JsonConfig propertiesFilter(final Map<Class<?>, List<String>> clazzNamesMap) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if (clazzNamesMap.containsKey(source.getClass()) && clazzNamesMap.get(source.getClass()).contains(name)) {
					return true;
				}
				return false;
			}
		});
		return jsonConfig;
	}

}
