package cn.zxl.orm.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import cn.zxl.orm.common.BaseService;
import cn.zxl.orm.security.domain.User;

public interface UserService extends BaseService<User> , UserDetailsService {
	
	public List<User> getUsersByAuthorities(Integer enabled,String... authorities);
	
}
