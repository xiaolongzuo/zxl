package cn.zxl.orm.security.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.json.JsonConfig;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import cn.zxl.common.json.JsonConfigs;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CycleSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class Role extends BaseEntity implements GrantedAuthority, ConfigAttribute, CycleSupport {

	private static final long serialVersionUID = -8788840338759171500L;

	private String authority;

	private String description;

	private Set<Resource> resources;

	private Set<User> users;

	private Set<Control> controls;

	public Role() {
		super();
	}

	public Role(String authority) {
		super();
		this.authority = authority;
	}

	@ManyToMany(targetEntity = Control.class, mappedBy = "roles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<Control> getControls() {
		return controls;
	}

	public void setControls(Set<Control> controls) {
		this.controls = controls;
	}

	@ManyToMany(targetEntity = User.class, mappedBy = "roles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	@Column(nullable = false, updatable = false, unique = true)
	public String getAuthority() {
		return authority;
	}

	@ManyToMany(mappedBy = "roles", targetEntity = Resource.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	@Transient
	public String getAttribute() {
		return authority;
	}

	public void setAttribute(String attribute) {
		authority = attribute;
	}

	@Override
	public JsonConfig jsonConfig() {
		return JsonConfigs.propertiesFilter("resources", "users", "controls");
	}

}
