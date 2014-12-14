package cn.zxl.orm.common.domain.acl;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.zxl.orm.common.BaseEntity;

@Entity
public class AclObjectIdentity extends BaseEntity{

	private static final long serialVersionUID = -925391409953762505L;

	private AclClass clazz;
	
	private AclObjectIdentity parent;
	
	private AclSid sid;
	
	private String objectIdentity;
	
	private Boolean entriesInheriting;
	
	@ManyToOne
	@JoinColumn
	public AclClass getClazz() {
		return clazz;
	}

	public void setClazz(AclClass clazz) {
		this.clazz = clazz;
	}

	@ManyToOne
	@JoinColumn
	public AclObjectIdentity getParent() {
		return parent;
	}

	public void setParent(AclObjectIdentity parent) {
		this.parent = parent;
	}

	@ManyToOne
	@JoinColumn
	public AclSid getSid() {
		return sid;
	}

	public void setSid(AclSid sid) {
		this.sid = sid;
	}

	public String getObjectIdentity() {
		return objectIdentity;
	}

	public void setObjectIdentity(String objectIdentity) {
		this.objectIdentity = objectIdentity;
	}

	public Boolean getEntriesInheriting() {
		return entriesInheriting;
	}

	public void setEntriesInheriting(Boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}

}
