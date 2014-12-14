package cn.zxl.orm.security.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.json.JsonConfig;
import cn.zxl.common.json.JsonConfigs;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CycleSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class Control extends BaseEntity implements CycleSupport {

	private static final long serialVersionUID = 229524694008687971L;

	private String name;

	private Set<Role> roles;

	private String description;

	@Column(unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public JsonConfig jsonConfig() {
		return JsonConfigs.propertiesFilter("roles");
	}

}
