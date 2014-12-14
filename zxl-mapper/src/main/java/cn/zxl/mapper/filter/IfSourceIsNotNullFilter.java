package cn.zxl.mapper.filter;

import cn.zxl.mapper.MapContext;

public class IfSourceIsNotNullFilter implements Filter {

	public static final IfSourceIsNotNullFilter INSTANCE = new IfSourceIsNotNullFilter();

	private IfSourceIsNotNullFilter() {
	}

	@Override
	public boolean doFilter(MapContext context) {
		if (context.getSourceFieldValue() != null) {
			return true;
		}
		return false;
	}

}
