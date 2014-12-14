package cn.zxl.hbase.orm;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.hadoop.hbase.util.Bytes;

public class Family implements Serializable {

	private static final long serialVersionUID = 8402683005259692207L;
	
	private JSONObject object = new JSONObject();
	
	public Family() {
		super();
	}
	
	public Family(JSONObject object) {
		super();
		putJsonObject(object);
	}

	public String get(String column){
		return object.getString(column);
	}
	
	public void put(String column,String value){
		object.put(column, value);
	}
	
	public void put(byte[] column,byte[] value){
		object.put(Bytes.toString(column), Bytes.toString(value));
	}
	
	public void putStringMap(Map<String, String> map){
		for (String key:map.keySet()) {
			put(key, map.get(key));
		}
	}
	
	public void putJsonObject(JSONObject object){
		for (Object key : object.keySet()) {
			this.object.put(key.toString(), object.get(key).toString());
		}
	}
	
	public void putByteMap(Map<byte[], byte[]> map){
		for (byte[] key:map.keySet()) {
			put(key, map.get(key));
		}
	}
	
	@SuppressWarnings("unchecked")
	public Set<String> keySet(){
		return object.keySet();
	}
	
	public JSONObject toJsonObject(){
		JSONObject jsonObject = new JSONObject();
		for (String key:keySet()) {
			jsonObject.put(key, get(key));
		}
		return jsonObject;
	}

	@Override
	public String toString() {
		return object.toString();
	}
	
}
