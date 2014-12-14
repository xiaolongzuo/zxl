package cn.zxl.orm.common.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.zxl.orm.common.BaseDaoImpl;
import cn.zxl.orm.common.dao.DictionaryDao;
import cn.zxl.orm.common.domain.Dictionary;

@Repository("dictionaryDao")
public class DictionaryDaoImpl extends BaseDaoImpl<Dictionary> implements DictionaryDao{

	public List<Dictionary> getOptions(String type) {
		return getList(new Dictionary(type));
	}
	
}
