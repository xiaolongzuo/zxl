package cn.zxl.mvc.hbase.springmvc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import cn.zxl.hbase.orm.BaseDao;
import cn.zxl.hbase.orm.Table;
import cn.zxl.mvc.common.springmvc.ActionResult;
import cn.zxl.mvc.hbase.util.JsonUtil;
import cn.zxl.mvc.hbase.util.ViewUtil;

public abstract class AbstractHBaseBusinessLogicAction<T extends Table> extends AbstractHBaseAction<T> {

	private BaseDao<T> actionDao;

	protected BaseDao<T> getActionDao() {
		return actionDao;
	}

	protected abstract String actionDaoName();

	@SuppressWarnings("unchecked")
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		String actionDaoName = actionDaoName();
		actionDao = (BaseDao<T>) applicationContext.getBean(actionDaoName);
		if (actionDaoName != null) {
			actionDao.setClazz(getClazz());
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/put", method = RequestMethod.POST)
	public ModelAndView put(@RequestBody String json) {
		try {
			String rowKey = actionDao.put((T) JsonUtil.parse(json, getClazz()));
			ModelAndView view = new ModelAndView();
			view.addObject(ViewUtil.VIEW_KEY, new ActionResult(ActionResult.SUCCESS_CODE, rowKey));
			return view;
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "put failed!", exception);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable String id) {
		try {
			actionDao.delete(id);
			return successView(ViewUtil.VIEW_KEY);
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "delete failed!", exception);
		}
	}

	@RequestMapping(value = "/page/{start}", method = RequestMethod.GET)
	public ModelAndView getPage(@PathVariable String start) {
		try {
			return modelAndView(actionDao.scan(start));
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "getPage failed!", exception);
		}
	}

	@RequestMapping(value = "/page/{start}/{stop}", method = RequestMethod.GET)
	public ModelAndView getPage(@PathVariable String start, @PathVariable String stop) {
		try {
			return modelAndView(actionDao.scan(start, stop));
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "getPage failed!", exception);
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getAll() {
		try {
			return modelAndView(actionDao.scan());
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "getAll failed!", exception);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id) {
		try {
			return modelAndView(actionDao.get(id));
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "get failed!", exception);
		}
	}

	@RequestMapping(value = "/{id}/{familyName}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, @PathVariable String familyName) {
		try {
			return modelAndView(actionDao.get(id, familyName));
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "get failed!", exception);
		}
	}

	@RequestMapping(value = "/{id}/{familyName}/{columnName}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id, @PathVariable String familyName, @PathVariable String columnName) {
		try {
			return modelAndView(actionDao.get(id, familyName, columnName));
		} catch (Exception exception) {
			return errorView(ViewUtil.VIEW_KEY, "get failed!", exception);
		}
	}

}
