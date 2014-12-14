package cn.zxl.mapper.spring;

import org.springframework.beans.factory.FactoryBean;

import cn.zxl.common.StringUtil;
import cn.zxl.mapper.Mapper;

public class MapperFactoryBean implements FactoryBean<Mapper> {

	private String globalPath;

	private String mapperMode;

	private String mapperPath;

	public void setMapperMode(String mapperMode) {
		this.mapperMode = mapperMode;
	}

	public void setMapperPath(String mapperPath) {
		this.mapperPath = mapperPath;
	}

	public void setGlobalPath(String globalPath) {
		this.globalPath = globalPath;
	}

	@Override
	public Mapper getObject() throws Exception {
		if (!StringUtil.isEmpty(mapperMode) && !StringUtil.isEmpty(mapperPath)) {
			return new Mapper(mapperMode, mapperPath);
		} else if (!StringUtil.isEmpty(globalPath)) {
			return new Mapper(globalPath);
		} else {
			return new Mapper();
		}
	}

	@Override
	public Class<?> getObjectType() {
		return Mapper.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
