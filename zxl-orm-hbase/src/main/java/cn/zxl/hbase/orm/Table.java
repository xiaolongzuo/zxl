package cn.zxl.hbase.orm;

import java.io.Serializable;

public abstract class Table implements Serializable{

	private static final long serialVersionUID = -5828451975959188129L;
	
	public static final String BASE_FAMILY = "in";
	
	private String id;
	
	private Family in;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Family getIn() {
		return in;
	}

	public void setIn(Family in) {
		this.in = in;
	}
	
}
