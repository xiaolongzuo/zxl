package cn.zxl.orm.security.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.json.JsonConfig;
import cn.zxl.common.json.JsonConfigs;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CycleSupport;
import cn.zxl.orm.common.util.EntityUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class Resource extends BaseEntity implements CycleSupport {

	private static final long serialVersionUID = 7327117436800308639L;

	private String name;

	private String type;

	private String value;

	private String description;

	private Set<Role> roles;

	private String roleIds;

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getRoleIds() {
		return EntityUtil.forTransientId(roleIds, roles);
	}

	void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
		this.roles = valueOf(Role.class, this.roleIds.split(","));
	}

	@Override
	public JsonConfig jsonConfig() {
		return JsonConfigs.propertiesFilter("roles");
	}

}
