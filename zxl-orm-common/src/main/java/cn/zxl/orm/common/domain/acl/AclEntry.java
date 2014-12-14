package cn.zxl.orm.common.domain.acl;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.zxl.orm.common.BaseEntity;

@Entity
public class AclEntry extends BaseEntity{

	private static final long serialVersionUID = 5132367792633893171L;

	private AclObjectIdentity objectIdentity;
	
	private AclSid sid;
	
	private Integer aceOrder;
	
	private Integer mask;
	
	private Boolean granting;
	
	private Boolean auditSuccess;
	
	private Boolean auditFailure;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn
	public AclObjectIdentity getObjectIdentity() {
		return objectIdentity;
	}

	public void setObjectIdentity(AclObjectIdentity objectIdentity) {
		this.objectIdentity = objectIdentity;
	}

	public Integer getAceOrder() {
		return aceOrder;
	}

	public void setAceOrder(Integer aceOrder) {
		this.aceOrder = aceOrder;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn
	public AclSid getSid() {
		return sid;
	}

	public void setSid(AclSid sid) {
		this.sid = sid;
	}
	
	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Boolean getGranting() {
		return granting;
	}

	public void setGranting(Boolean granting) {
		this.granting = granting;
	}

	public Boolean getAuditSuccess() {
		return auditSuccess;
	}

	public void setAuditSuccess(Boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public Boolean getAuditFailure() {
		return auditFailure;
	}

	public void setAuditFailure(Boolean auditFailure) {
		this.auditFailure = auditFailure;
	}
	
}
