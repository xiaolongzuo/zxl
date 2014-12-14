package cn.zxl.orm.security.dao;

import java.util.Map;

import cn.zxl.orm.common.BaseDao;
import cn.zxl.orm.security.domain.Control;

public interface ControlDao extends BaseDao<Control> {

	public Map<String, String> getControlMap();
	
}
