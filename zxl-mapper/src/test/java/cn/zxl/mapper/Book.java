package cn.zxl.mapper;

import java.util.Date;

public class Book {

	private String id;

	private Integer age;

	private Date date;

	private InnerBook innerBook;

	private InnerNote innerNote;

	private String name;

	private String exclude;

	public String getId() {
		return id;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public InnerBook getInnerBook() {
		return innerBook;
	}

	public void setInnerBook(InnerBook innerBook) {
		this.innerBook = innerBook;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InnerNote getInnerNote() {
		return innerNote;
	}

	public void setInnerNote(InnerNote innerNote) {
		this.innerNote = innerNote;
	}

}
