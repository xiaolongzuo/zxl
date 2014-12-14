package cn.zxl.mvc.common.springmvc.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.zxl.common.ReflectUtil;
import cn.zxl.common.StringUtil;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CommonBaseService;
import cn.zxl.orm.common.Pager;

public abstract class AbstractRelatedbBusinessLogicAction<T extends BaseEntity> extends AbstractRelatedbAction<T> {

	private CommonBaseService commonBaseService;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		this.commonBaseService = (CommonBaseService) getApplicationContext().getBean(CommonBaseService.COMMON_BASE_SERVICE_BEAN_NAME);
	}

	@RequestMapping(value = "/input.action")
	public String input(HttpServletRequest request, String id) {
		prepareInputView(request, id);
		return viewInNamespace(INPUT);
	}

	protected void prepareInputView(HttpServletRequest request, String id) {
		if (!StringUtil.isEmpty(id)) {
			setRequestAttribute(request, ENTITY_ATTRIBUTE, commonBaseService.load(getClazz(), id));
		}
	}

	@RequestMapping(value = "/list.action")
	public String list() {
		prepareListView();
		return viewInNamespace(LIST);
	}

	protected void prepareListView() {
	}

	@RequestMapping(value = "/pageData.json")
	public ModelAndView pageData(Integer page, Integer rows, T entity) {
		Pager<T> pager = beforePageData(page, rows);
		pager = doPageData(pager, entity);
		return afterPageData(pager, entity);
	}

	protected Pager<T> beforePageData(Integer page, Integer rows) {
		Pager<T> pager = new Pager<T>();
		if (page != null)
			pager.setPageNumber(page);
		if (rows != null)
			pager.setPageSize(rows);
		return pager;
	}

	protected Pager<T> doPageData(Pager<T> pager, T entity) {
		return commonBaseService.getByPager(getClazz(), pager, entity);
	}

	protected ModelAndView afterPageData(Pager<T> pager, T entity) {
		ModelAndView view = new ModelAndView("pageData");
		view.addObject(pager.getViewJsonData());
		return view;
	}

	@RequestMapping(value = "/save.json")
	public ModelAndView save(T entity) {
		beforeSave(entity);
		String id = doSave(entity);
		return afterSave(id);
	}

	protected void beforeSave(T entity) {
	}

	protected String doSave(T entity) {
		return commonBaseService.save(entity);
	}

	protected ModelAndView afterSave(String id) {
		return successView();
	}

	@RequestMapping(value = "/update.json")
	public ModelAndView update(T entity) {
		T entityInDB = beforeUpdate(entity);
		doUpdate(entityInDB);
		return afterUpdate(entityInDB);
	}

	protected T beforeUpdate(T entity) {
		return ReflectUtil.copyFieldsByName(entity, commonBaseService.get(getClazz(), entity.getId()));
	}

	protected void doUpdate(T entityInDB) {
		commonBaseService.update(entityInDB);
	}

	protected ModelAndView afterUpdate(T entityInDB) {
		return successView();
	}

	@RequestMapping(value = "/delete.json")
	public ModelAndView delete(T entity) {
		T entityInDB = beforeDelete(entity);
		doDelete(entityInDB);
		return afterDelete(entityInDB);
	}

	protected T beforeDelete(T entity) {
		return commonBaseService.get(getClazz(), entity.getId());
	}

	protected void doDelete(T entityInDB) {
		commonBaseService.delete(entityInDB);
	}

	protected ModelAndView afterDelete(T entityInDB) {
		return successView();
	}

}
