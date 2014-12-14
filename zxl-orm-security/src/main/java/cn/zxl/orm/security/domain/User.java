package cn.zxl.orm.security.domain;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import net.sf.json.JsonConfig;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.BeanUtil;
import cn.zxl.common.ConstantUtil;
import cn.zxl.common.json.JsonConfigs;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CycleSupport;
import cn.zxl.orm.common.domain.Dictionary;
import cn.zxl.orm.common.util.EntityUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
public class User extends BaseEntity implements UserDetails, CycleSupport {

	private static final long serialVersionUID = 6284728821851698090L;

	private String username;

	private String password;

	private String name;

	private Dictionary status;

	private Set<Role> roles;

	private String description;

	private String roleIds;

	private String statusId;

	public User() {
		super();
	}

	public User(String username, String password, Dictionary status, Set<Role> roles, String description) {
		super();
		this.username = username;
		this.password = password;
		this.status = status;
		this.roles = roles;
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnore
	@XmlTransient
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	@Override
	@Column(nullable = false, updatable = false, unique = true)
	public String getUsername() {
		return username;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public Dictionary getStatus() {
		return status;
	}

	public void setStatus(Dictionary status) {
		this.status = status;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transient
	@JsonIgnore
	@XmlTransient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return ConstantUtil.isYes(BeanUtil.isEmpty(status) ? null : status.getValue());
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
	}

	public void setEnabled(boolean enabled) {
	}

	@Transient
	public String getRoleIds() {
		return EntityUtil.forTransientId(roleIds, roles);
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
		this.roles = valueOf(Role.class, this.roleIds.split(","));
	}

	@Transient
	public String getStatusId() {
		return EntityUtil.forTransientId(statusId, status);
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
		this.status = valueOf(Dictionary.class, this.statusId);
	}

	public boolean contains(String... authorities) {
		if (ArrayUtil.isEmpty(roles)) {
			return false;
		}
		for (int i = 0; i < authorities.length; i++) {
			boolean contains = false;
			for (Role role : roles) {
				if (role.getAuthority().equals(authorities[i])) {
					contains = true;
				}
			}
			if (!contains) {
				return false;
			}
		}
		return true;
	}

	@Override
	public JsonConfig jsonConfig() {
		return JsonConfigs.propertiesFilter("roles", "authorities");
	}

}
