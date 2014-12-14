package cn.zxl.orm.common.config.domain;

import javax.persistence.Entity;

import cn.zxl.orm.common.BaseEntity;

@Entity
public class Book extends BaseEntity {

	private static final long serialVersionUID = -8603746201945723342L;

	private String name;

	private Integer page;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}
