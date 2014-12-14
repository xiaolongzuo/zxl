package cn.zxl.mapper.config;

import cn.zxl.mapper.filter.IfSourceIsNotEmptyFilter;
import cn.zxl.mapper.filter.IfSourceIsNotNullFilter;
import cn.zxl.mapper.filter.IfTargetIsEmptyFilter;
import cn.zxl.mapper.filter.IfTargetIsNullFilter;
import cn.zxl.mapper.filter.ListFilter;

public class Set {

	private String leftFieldName;

	private boolean ifSourceIsNotNull;

	private boolean ifSourceIsNotEmpty;

	private boolean ifTargetIsNull;

	private boolean ifTargetIsEmpty;

	private ListFilter listFilter;

	public Set(String leftFieldName, boolean ifSourceIsNotNull, boolean ifSourceIsNotEmpty, boolean ifTargetIsNull, boolean ifTargetIsEmpty) {
		super();
		this.leftFieldName = leftFieldName;
		this.ifSourceIsNotNull = ifSourceIsNotNull;
		this.ifSourceIsNotEmpty = ifSourceIsNotEmpty;
		this.ifTargetIsNull = ifTargetIsNull;
		this.ifTargetIsEmpty = ifTargetIsEmpty;

		listFilter = new ListFilter();
		if (this.ifSourceIsNotEmpty) {
			listFilter.addFilter(IfSourceIsNotEmptyFilter.INSTANCE);
		}
		if (this.ifSourceIsNotNull) {
			listFilter.addFilter(IfSourceIsNotNullFilter.INSTANCE);
		}
		if (this.ifTargetIsEmpty) {
			listFilter.addFilter(IfTargetIsEmptyFilter.INSTANCE);
		}
		if (this.ifTargetIsNull) {
			listFilter.addFilter(IfTargetIsNullFilter.INSTANCE);
		}
	}

	public String getLeftFieldName() {
		return leftFieldName;
	}

	public ListFilter getListFilter() {
		return listFilter;
	}

	@Override
	public String toString() {
		return "Set [leftFieldName=" + leftFieldName + ", ifSourceIsNotNull=" + ifSourceIsNotNull + ", ifSourceIsNotEmpty=" + ifSourceIsNotEmpty + ", ifTargetIsNull=" + ifTargetIsNull + ", ifTargetIsEmpty=" + ifTargetIsEmpty + "]";
	}

}
