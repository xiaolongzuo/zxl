package cn.zxl.orm.common;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.StringUtil;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3038921622628919854L;

	private String id;
	private Date createDate;
	private Date modifyDate;

	@Id
	@Column(length = 36, nullable = true)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(updatable = false)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public static <T extends BaseEntity> T valueOf(Class<T> clazz, String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}
		instance.setId(id);
		return instance;
	}

	public static <T extends BaseEntity> Set<T> valueOf(Class<T> clazz, String... ids) {
		if (ArrayUtil.isEmpty(ids)) {
			return null;
		}
		Set<T> set = new HashSet<T>();
		for (int i = 0; i < ids.length; i++) {
			T instance = null;
			try {
				instance = clazz.newInstance();
			} catch (Exception cause) {
				throw new RuntimeException(cause);
			}
			instance.setId(ids[i]);
			set.add(instance);
		}
		return set;
	}

	@Override
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass().getPackage() != obj.getClass().getPackage()) {
			return false;
		}
		final BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}

}
