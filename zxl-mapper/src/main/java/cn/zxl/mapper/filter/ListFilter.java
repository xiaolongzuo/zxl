package cn.zxl.mapper.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zxl.common.ArrayUtil;
import cn.zxl.mapper.MapContext;

public class ListFilter implements Filter {

	private List<Filter> filters;

	public ListFilter(Filter... filters) {
		if (!ArrayUtil.isEmpty(filters)) {
			this.filters = Arrays.asList(filters);
		} else {
			this.filters = new ArrayList<Filter>();
		}
	}

	public void addFilter(Filter filter) {
		filters.add(filter);
	}

	@Override
	public boolean doFilter(MapContext context) {
		if (ArrayUtil.isEmpty(filters)) {
			return true;
		}
		for (Filter filter : filters) {
			boolean result = filter.doFilter(context);
			if (!result) {
				return false;
			}
		}
		return true;
	}

}
