package cn.zxl.orm.common;

import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.orm.common.domain.SystemConfig;

public class SimpleDataSource extends BasicDataSource implements ApplicationContextAware, InitializingBean {

	private String name;

	private ApplicationContext applicationContext;

	private CommonBaseDao commonBaseDao;

	public void setName(String name) {
		this.name = name;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.commonBaseDao = (CommonBaseDao) this.applicationContext.getBean(CommonBaseDao.COMMON_BASE_DAO_BEAN_NAME);
	}

	public void afterPropertiesSet() throws Exception {
		super.setPoolPreparedStatements(true);
		super.setInitialSize(5);
		super.setMaxActive(100);
		super.setMaxIdle(100);
		super.setMaxWait(60000);
		List<SystemConfig> systemConfigs = commonBaseDao.getAll(SystemConfig.class);
		for (SystemConfig systemConfig : systemConfigs) {
			if (systemConfig.getConfigKey().equals(name + ".username")) {
				super.setUsername(systemConfig.getConfigValue());
			}
			if (systemConfig.getConfigKey().equals(name + ".driverClassName")) {
				super.setDriverClassName(systemConfig.getConfigValue());
			}
			if (systemConfig.getConfigKey().equals(name + ".url")) {
				super.setUrl(systemConfig.getConfigValue());
			}
			if (systemConfig.getConfigKey().equals(name + ".password")) {
				super.setPassword(systemConfig.getConfigValue());
			}
			if (systemConfig.getConfigKey().equals(name + ".poolPreparedStatements")) {
				super.setPoolPreparedStatements(Boolean.valueOf(systemConfig.getConfigValue()));
			}
			if (systemConfig.getConfigKey().equals(name + ".initialSize")) {
				super.setInitialSize(Integer.valueOf(systemConfig.getConfigValue()));
			}
			if (systemConfig.getConfigKey().equals(name + ".maxActive")) {
				super.setMaxActive(Integer.valueOf(systemConfig.getConfigValue()));
			}
			if (systemConfig.getConfigKey().equals(name + ".maxIdle")) {
				super.setMaxIdle(Integer.valueOf(systemConfig.getConfigValue()));
			}
			if (systemConfig.getConfigKey().equals(name + ".maxWait")) {
				super.setMaxWait(Integer.valueOf(systemConfig.getConfigValue()));
			}
		}
		if (super.getUsername() == null) {
			throw new IllegalArgumentException(name + ".username is required!");
		}
		if (super.getDriverClassName() == null) {
			throw new IllegalArgumentException(name + ".driverClassName is required!");
		}
		if (super.getUrl() == null) {
			throw new IllegalArgumentException(name + ".url is required!");
		}
		if (super.getPassword() == null) {
			throw new IllegalArgumentException(name + ".password is required!");
		}
	}

}
