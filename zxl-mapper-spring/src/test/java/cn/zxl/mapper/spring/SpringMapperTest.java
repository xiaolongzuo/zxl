package cn.zxl.mapper.spring;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.zxl.common.DateUtil;
import cn.zxl.mapper.Mapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-mapper.xml" })
public class SpringMapperTest {

	@Autowired
	private Mapper devModeAndPathMapper;

	@Autowired
	private Mapper devGlobalPathMapper;

	@Autowired
	private Mapper proModeAndPathMapper;

	@Autowired
	private Mapper proGlobalPathMapper;

	@Autowired
	private Mapper commonMapper;

	@Test
	public void commonTest() throws Exception {
		Book book = new Book();

		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");
		Date date = new Date();
		book.setDate(date);
		book.setAge(23);
		book.setExclude("summary");

		book.setInnerBook(innerBook);

		Note note = new Note();
		note.setIdNote("idnoye");

		note = (Note) devModeAndPathMapper.map("testMap", book, note);
		Assert.assertEquals(23, Integer.valueOf(note.getAgeNote()).intValue());
		Assert.assertNull(note.getIdNote());
		Assert.assertEquals(DateUtil.dateFormat(date), note.getDate());
		Assert.assertEquals("123", String.valueOf(note.getInnerNote().getId()));
	}

	@Test
	public void emptyRuleTest() throws Exception {
		Book book = new Book();

		InnerNote innerNote = new InnerNote();
		innerNote.setId(123);
		Date date = new Date();
		book.setDate(date);
		book.setAge(23);
		book.setExclude("summary");

		book.setInnerNote(innerNote);

		Note note = new Note();
		note.setIdNote("idnoye");

		note = (Note) devGlobalPathMapper.map("testEmptyRuleMap", book, note);
		Assert.assertEquals(23, Integer.valueOf(note.getAgeNote()).intValue());
		Assert.assertNull(note.getIdNote());
		Assert.assertEquals(DateUtil.dateFormat(date), note.getDate());
		Assert.assertEquals(123, note.getInnerNote().getId().intValue());
	}

	@Test
	public void reverseTest() throws Exception {
		Book book = new Book();

		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");

		Date date = new Date();
		book.setDate(date);
		book.setAge(23);
		book.setExclude("summary");

		book.setInnerBook(innerBook);

		Note note = new Note();
		note.setIdNote("idnoye");

		note = (Note) proModeAndPathMapper.map("testReverseMap", book, note);

		Assert.assertEquals(23, Integer.valueOf(note.getAgeNote()).intValue());
		Assert.assertNull(note.getIdNote());
		Assert.assertEquals(DateUtil.dateFormat(date), note.getDate());
		Assert.assertEquals("123", String.valueOf(note.getInnerNote().getId()));
	}

	@Test
	public void wildcardTest1() throws Exception {
		Book book = new Book();

		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");

		book.setDate(new Date());
		book.setAge(23);
		book.setExclude("summary");

		book.setInnerBook(innerBook);

		book = (Book) proGlobalPathMapper.map("testTypeRule1", book, null);
		Assert.assertEquals(23, book.getAge().intValue());
		Assert.assertEquals("123", book.getInnerBook().getName());
	}

	@Test
	public void wildcardTest2() throws Exception {
		Book book = new Book();

		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");

		book.setDate(new Date());
		book.setAge(23);
		book.setExclude("summary");

		book.setInnerBook(innerBook);

		book = (Book) commonMapper.map("testTypeRule2", book, null);
		Assert.assertEquals(23, book.getAge().intValue());
		Assert.assertEquals("123", book.getInnerBook().getName());
	}

	@Test
	public void wildcardTest3() throws Exception {
		Book book = new Book();
		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");
		book.setDate(new Date());
		book.setAge(23);
		book.setExclude("summary");
		book.setInnerBook(innerBook);

		Book targetBook = new Book();
		targetBook.setAge(100);
		InnerBook targetInnerBook = new InnerBook();
		targetInnerBook.setName("999");
		targetBook.setInnerBook(targetInnerBook);
		targetBook = (Book) commonMapper.map("testTypeRule3", book, targetBook);
		Assert.assertEquals(100, targetBook.getAge().intValue());
		Assert.assertEquals("999", targetBook.getInnerBook().getName());
	}

	@Test
	public void classMapTest1() throws Exception {
		Book book = new Book();
		InnerBook innerBook = new InnerBook();
		innerBook.setName("123");
		book.setDate(new Date());
		book.setAge(23);
		book.setExclude("summary");
		book.setInnerBook(innerBook);

		Book targetBook = new Book();
		targetBook.setAge(100);
		InnerBook targetInnerBook = new InnerBook();
		targetInnerBook.setName("999");
		targetBook.setInnerBook(targetInnerBook);
		targetBook = proModeAndPathMapper.map(book, targetBook);
		Assert.assertEquals(23, targetBook.getAge().intValue());
		Assert.assertEquals("123", targetBook.getInnerBook().getName());
		Assert.assertEquals("summary", targetBook.getExclude());
	}
}
