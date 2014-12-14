package cn.zxl.orm.common.domain.acl;

import javax.persistence.Entity;

import cn.zxl.orm.common.BaseEntity;

@Entity
public class AclSid extends BaseEntity{

	private static final long serialVersionUID = 4675215232277350070L;

	private Boolean principal;
	
	private String sid;

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
}
