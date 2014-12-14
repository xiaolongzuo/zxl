package cn.zxl.hbase.orm;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zxl.common.DateUtil;
import cn.zxl.hbase.orm.dao.TestLogDao;
import cn.zxl.hbase.orm.domain.TestLog;

@Ignore
public class TestLogDaoTest extends AbstractHbaseTest {

	@Autowired
	private TestLogDao testLogDao;

	@Autowired
	private RowKeyGenerator<TestLog> testLogRowKeyGenerator;

	@Test
	public void put() {
		TestLog testLog = new TestLog();
		Family family = new Family();
		family.put("a", "1");
		family.put("b", "2");
		family.put("c", "3");
		family.put("ts", DateUtil.todaySortedTimestamp());
		testLog.setIn(family);

		String id = testLogRowKeyGenerator.generate(testLog);
		TestLog testLogInDB = testLogDao.get(id);
		Assert.assertNull(testLogInDB);

		id = testLogDao.put(testLog);
		Assert.assertNotNull(id);

		testLogInDB = testLogDao.get(id);
		Assert.assertNotNull(testLogInDB);
		Assert.assertEquals("1", testLogInDB.getIn().get("a"));
		Assert.assertEquals("2", testLogInDB.getIn().get("b"));
		Assert.assertEquals("3", testLogInDB.getIn().get("c"));
	}

}
