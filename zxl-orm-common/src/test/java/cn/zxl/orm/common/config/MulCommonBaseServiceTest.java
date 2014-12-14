package cn.zxl.orm.common.config;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cn.zxl.common.ArrayUtil;
import cn.zxl.orm.common.CommonBaseDao;
import cn.zxl.orm.common.CommonBaseService;
import cn.zxl.orm.common.config.domain.Book;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-datasource.xml" })
@TransactionConfiguration(transactionManager = "multestHibernateTransactionManager", defaultRollback = false)
public class MulCommonBaseServiceTest {

	private static final int TOTAL_COUNT = 100;

	@Autowired
	private CommonBaseDao multestCommonBaseDao;

	@Autowired
	private CommonBaseService multestCommonBaseService;

	private List<String> ids;

	@Before
	public void initContext() {
		List<Book> books = multestCommonBaseService.getAll(Book.class);
		if (!ArrayUtil.isEmpty(books)) {
			multestCommonBaseService.delete(books);
		}
		ids = new ArrayList<String>();
		for (int i = 0; i < TOTAL_COUNT; i++) {
			Book book = new Book();
			book.setName("name" + i);
			book.setPage(i);
			ids.add(multestCommonBaseService.save(book));
		}
		Assert.assertEquals(TOTAL_COUNT, ids.size());
		Assert.assertNotNull(multestCommonBaseDao);
		Assert.assertNotNull(multestCommonBaseService);
	}

	@Test
	public void get() {
		int index = 10;
		Book book = multestCommonBaseService.get(Book.class, ids.get(index));
		Assert.assertEquals(ids.get(index), book.getId());
		Assert.assertEquals("name" + index, book.getName());
		Assert.assertEquals(Integer.valueOf(index), book.getPage());
	}

	@Test
	public void getUnique() {
		int index = 10;
		Book book = new Book();
		book.setName("name" + index);
		Book bookInDB = multestCommonBaseService.getUnique(Book.class, book);
		Assert.assertEquals(ids.get(index), bookInDB.getId());
		Assert.assertEquals(book.getName(), bookInDB.getName());
		Assert.assertEquals(Integer.valueOf(index), bookInDB.getPage());
	}

}
