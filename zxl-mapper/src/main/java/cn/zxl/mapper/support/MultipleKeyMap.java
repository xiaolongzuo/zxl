package cn.zxl.mapper.support;

import java.util.HashMap;
import java.util.Set;

public class MultipleKeyMap<V> {

	private HashMap<String, V> hashMap;

	public MultipleKeyMap() {
		super();
		hashMap = new HashMap<String, V>();
	}

	private String generateKey(String... keys) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < keys.length; i++) {
			if (i == 0) {
				stringBuffer.append(keys[i]);
			} else {
				stringBuffer.append("-" + keys[i]);
			}
		}
		return stringBuffer.toString();
	}

	public V get(String... keys) {
		return hashMap.get(generateKey(keys));
	}

	public V put(V value, String... keys) {
		return hashMap.put(generateKey(keys), value);
	}

	public Set<String> keySet() {
		return hashMap.keySet();
	}

	public String[] splitKey(String key) {
		return key.split("\\-");
	}

	@Override
	public String toString() {
		return "MultipleKeyMap [hashMap=" + hashMap + "]";
	}

}
