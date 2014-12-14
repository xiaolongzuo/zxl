package cn.zxl.orm.security.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.zxl.orm.common.BaseDaoImpl;
import cn.zxl.orm.security.dao.UserDao;
import cn.zxl.orm.security.domain.User;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	public User loadUserByUsername(String username) {
		User entity = new User();
		entity.setUsername(username);
		return getUnique(entity);
	}

	public List<User> getUsersByAuthorities(Integer enabled, String... authorities) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("enabled", enabled);
		map.put("authorities", authorities);
		map.put("tablePrefix", getTablePrefix());
		return selectList("getUsersByAuthorities", map);
	}

}
