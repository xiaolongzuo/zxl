package cn.zxl.core.security.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import cn.zxl.common.ArrayUtil;
import cn.zxl.orm.common.CommonBaseService;
import cn.zxl.orm.security.domain.Resource;
import cn.zxl.orm.security.domain.Role;

@Component
public class ResourceSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

	@Autowired
	private CommonBaseService commonBaseService;

	private List<ConfigAttribute> allConfigAttributes;

	private Map<String, List<ConfigAttribute>> configAttributeMap = new HashMap<String, List<ConfigAttribute>>();

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		return configAttributeMap.get(((FilterInvocation) object).getRequestUrl());
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return allConfigAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<Role> roles = commonBaseService.getAll(Role.class);
		allConfigAttributes = new ArrayList<ConfigAttribute>();
		if (!ArrayUtil.isEmpty(roles)) {
			for (Role role : roles) {
				allConfigAttributes.add(role);
			}
		}
		List<Resource> resources = commonBaseService.getAll(Resource.class);
		for (Resource resource : resources) {
			configAttributeMap.put(resource.getValue(), new ArrayList<ConfigAttribute>(resource.getRoles()));
		}
	}

}
