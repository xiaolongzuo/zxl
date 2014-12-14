package cn.zxl.orm.security.service;

import java.util.Map;

import cn.zxl.orm.common.BaseService;
import cn.zxl.orm.security.domain.Control;

public interface ControlService extends BaseService<Control> {

	public Map<String, String> getControlMap();
	
}

