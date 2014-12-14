package cn.zxl.mapper.filter;

import cn.zxl.common.BeanUtil;
import cn.zxl.mapper.MapContext;

public class IfSourceIsNotEmptyFilter implements Filter {

	public static final IfSourceIsNotEmptyFilter INSTANCE = new IfSourceIsNotEmptyFilter();

	private IfSourceIsNotEmptyFilter() {
	}

	@Override
	public boolean doFilter(MapContext context) {
		if (!BeanUtil.isEmpty(context.getSourceFieldValue())) {
			return true;
		}
		return false;
	}

}
