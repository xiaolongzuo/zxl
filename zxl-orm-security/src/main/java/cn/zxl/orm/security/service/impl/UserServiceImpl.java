package cn.zxl.orm.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.zxl.orm.common.BaseServiceImpl;
import cn.zxl.orm.security.dao.UserDao;
import cn.zxl.orm.security.domain.User;
import cn.zxl.orm.security.service.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

	@Autowired
	private UserDao userDao;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = userDao.loadUserByUsername(username);
		if (userDetails == null) {
			throw new UsernameNotFoundException("Not found:" + username);
		}
		return userDetails;
	}
	
	public List<User> getUsersByAuthorities(Integer enabled,String... authorities){
		return userDao.getUsersByAuthorities(enabled,authorities);
	}

}
