package cn.zxl.orm.security.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import cn.zxl.orm.common.BaseEntity;

@Entity
@XmlRootElement
public class PersistentLogins extends BaseEntity {

	private static final long serialVersionUID = 7167788984352338998L;

	private String username;
	private String series;
	private String tokenValue;
	private Date lastUsedDate;
	
	public PersistentLogins() {
		super();
	}
	
	public PersistentLogins(PersistentRememberMeToken token) {
		super();
		this.series = token.getSeries();
		this.username = token.getUsername();
		this.tokenValue = token.getTokenValue();
		this.lastUsedDate = token.getDate();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(unique=true)
	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public Date getLastUsedDate() {
		return lastUsedDate;
	}

	public void setLastUsedDate(Date lastUsedDate) {
		this.lastUsedDate = lastUsedDate;
	}

}
