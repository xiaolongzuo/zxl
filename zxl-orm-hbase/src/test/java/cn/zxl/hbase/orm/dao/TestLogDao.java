package cn.zxl.hbase.orm.dao;

import org.springframework.stereotype.Component;

import cn.zxl.hbase.orm.BaseDao;
import cn.zxl.hbase.orm.domain.TestLog;

@Component
public class TestLogDao extends BaseDao<TestLog> {

	@Override
	protected String rowKeyGeneratorName() {
		return "testLogRowKeyGenerator";
	}

}
