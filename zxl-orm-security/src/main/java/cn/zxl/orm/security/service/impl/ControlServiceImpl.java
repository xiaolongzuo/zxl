package cn.zxl.orm.security.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zxl.orm.common.BaseServiceImpl;
import cn.zxl.orm.security.dao.ControlDao;
import cn.zxl.orm.security.domain.Control;
import cn.zxl.orm.security.service.ControlService;

@Service("controlService")
public class ControlServiceImpl extends BaseServiceImpl<Control> implements ControlService{

	@Autowired
	private ControlDao controlDao;

	public Map<String, String> getControlMap() {
		return controlDao.getControlMap();
	}
	
}
