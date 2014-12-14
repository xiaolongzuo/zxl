package cn.zxl.orm.common.dao;

import java.util.List;

import cn.zxl.orm.common.BaseDao;
import cn.zxl.orm.common.domain.Dictionary;

public interface DictionaryDao extends BaseDao<Dictionary>{

	public List<Dictionary> getOptions(String type);
	
}
