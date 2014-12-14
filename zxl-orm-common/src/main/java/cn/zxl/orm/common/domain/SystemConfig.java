package cn.zxl.orm.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import cn.zxl.orm.common.BaseEntity;

@Entity
@XmlRootElement
public class SystemConfig extends BaseEntity{

	private static final long serialVersionUID = 4288115532182896569L;

	private String configKey;
	
	private String configValue;

	@Column(unique=true)
	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
}
