package cn.zxl.orm.security.dao;

import java.util.List;

import cn.zxl.orm.common.BaseDao;
import cn.zxl.orm.security.domain.User;

public interface UserDao extends BaseDao<User> {

	public User loadUserByUsername(String username);
	
	public List<User> getUsersByAuthorities(Integer enabled,String... authorities);
	
}
