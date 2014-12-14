package cn.zxl.mvc.hbase.springmvc;

import org.springframework.web.servlet.ModelAndView;

import cn.zxl.hbase.orm.Table;
import cn.zxl.mvc.common.springmvc.AbstractSpringmvcAction;
import cn.zxl.mvc.hbase.util.ViewUtil;

public abstract class AbstractHBaseAction<T extends Table> extends AbstractSpringmvcAction<T> {

	protected ModelAndView modelAndView(Object result) {
		if (result == null)
			return errorView(ViewUtil.VIEW_KEY, "无查询结果", null);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(ViewUtil.VIEW_KEY, result);
		return modelAndView;
	}

	protected ModelAndView modelAndView(String viewName, Object result) {
		if (result == null)
			return errorView(ViewUtil.VIEW_KEY, "无查询结果", null);
		ModelAndView modelAndView = new ModelAndView(viewName);
		modelAndView.addObject(ViewUtil.VIEW_KEY, result);
		return modelAndView;
	}

}
