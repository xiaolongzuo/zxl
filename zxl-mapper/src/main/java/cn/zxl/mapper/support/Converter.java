package cn.zxl.mapper.support;

import cn.zxl.mapper.MapContext;

public abstract class Converter {

	public Object leftToRight(MapContext context) {
		return doLeftToRight(context);
	}

	public Object rightToLeft(MapContext context) {
		return doRightToLeft(context);
	}

	public abstract Object doLeftToRight(MapContext context);

	public abstract Object doRightToLeft(MapContext context);

}
