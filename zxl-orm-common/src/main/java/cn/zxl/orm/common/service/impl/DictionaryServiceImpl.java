package cn.zxl.orm.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zxl.orm.common.BaseServiceImpl;
import cn.zxl.orm.common.dao.DictionaryDao;
import cn.zxl.orm.common.domain.Dictionary;
import cn.zxl.orm.common.service.DictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements DictionaryService{

	@Autowired
	private DictionaryDao dictionaryDao;
	
	public List<Dictionary> getOptions(String type) {
		return dictionaryDao.getOptions(type);
	}

	public Dictionary get(String type,String value) {
		return super.getUnique(new Dictionary(type,value));
	}
}
