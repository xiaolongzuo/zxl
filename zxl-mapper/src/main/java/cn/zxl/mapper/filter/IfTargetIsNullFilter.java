package cn.zxl.mapper.filter;

import cn.zxl.mapper.MapContext;

public class IfTargetIsNullFilter implements Filter {

	public static final IfTargetIsNullFilter INSTANCE = new IfTargetIsNullFilter();

	private IfTargetIsNullFilter() {
	}

	@Override
	public boolean doFilter(MapContext context) {
		if (context.getTargetFieldValue() == null) {
			return true;
		}
		return false;
	}
}
