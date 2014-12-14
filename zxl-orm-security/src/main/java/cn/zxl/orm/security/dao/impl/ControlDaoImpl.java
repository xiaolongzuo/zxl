package cn.zxl.orm.security.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.zxl.orm.common.BaseDaoImpl;
import cn.zxl.orm.security.dao.ControlDao;
import cn.zxl.orm.security.domain.Control;
import cn.zxl.orm.security.domain.Role;

@Repository("controlDao")
public class ControlDaoImpl extends BaseDaoImpl<Control> implements ControlDao{

	@Override
	public Map<String, String> getControlMap() {
		Map<String, String> controlMap = new HashMap<String, String>();
		List<Control> controls = getAll();
		for (Control control : controls) {
			StringBuffer stringBuffer = new StringBuffer();
			for (Role role : control.getRoles()) {
				stringBuffer.append(role.getAuthority()).append(",");
			}
			controlMap.put(control.getName(), stringBuffer.length() == 0 ? "" : stringBuffer.substring(0, stringBuffer.length()-1));
		}
		return controlMap;
	}

}
