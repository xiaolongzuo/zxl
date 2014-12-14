package cn.zxl.orm.common.spring;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.common.LogUtil;
import cn.zxl.orm.common.CommonBaseDao;
import cn.zxl.orm.common.domain.SystemConfig;

public class DatabaseConfigurer extends PropertyPlaceholderConfigurer implements ApplicationContextAware {

	private static final Logger LOGGER = LogUtil.logger(DatabaseConfigurer.class);

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties properties = super.mergeProperties();
		CommonBaseDao commonBaseDao = (CommonBaseDao) applicationContext.getBean(CommonBaseDao.COMMON_BASE_DAO_BEAN_NAME);
		List<SystemConfig> systemConfigs = commonBaseDao.getAll(SystemConfig.class);
		for (SystemConfig systemConfig : systemConfigs) {
			LogUtil.info(LOGGER, "load properties : [" + systemConfig.getConfigKey() + " = " + systemConfig.getConfigValue() + "]");
			properties.put(systemConfig.getConfigKey(), systemConfig.getConfigValue());
		}
		return properties;
	}

}
