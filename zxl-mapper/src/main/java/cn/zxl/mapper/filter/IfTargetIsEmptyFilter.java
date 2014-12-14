package cn.zxl.mapper.filter;

import cn.zxl.common.BeanUtil;
import cn.zxl.mapper.MapContext;

public class IfTargetIsEmptyFilter implements Filter {

	public static final IfTargetIsEmptyFilter INSTANCE = new IfTargetIsEmptyFilter();

	private IfTargetIsEmptyFilter() {
	}

	@Override
	public boolean doFilter(MapContext context) {
		if (BeanUtil.isEmpty(context.getTargetFieldValue())) {
			return true;
		}
		return false;
	}

}
