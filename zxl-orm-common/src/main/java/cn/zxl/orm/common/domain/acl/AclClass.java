package cn.zxl.orm.common.domain.acl;

import javax.persistence.Column;
import javax.persistence.Entity;

import cn.zxl.orm.common.BaseEntity;

@Entity
public class AclClass extends BaseEntity{

	private static final long serialVersionUID = 3217203341274073223L;

	private String clazz;

	@Column(unique=true)
	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
}
