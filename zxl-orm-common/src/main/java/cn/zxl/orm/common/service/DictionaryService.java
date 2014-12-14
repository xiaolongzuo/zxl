package cn.zxl.orm.common.service;

import java.util.List;

import cn.zxl.orm.common.BaseService;
import cn.zxl.orm.common.domain.Dictionary;

public interface DictionaryService extends BaseService<Dictionary>{

	public List<Dictionary> getOptions(String type);
	
	public Dictionary get(String type,String value);
	
}
