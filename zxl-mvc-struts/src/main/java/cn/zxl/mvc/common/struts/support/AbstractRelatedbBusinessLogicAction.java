package cn.zxl.mvc.common.struts.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import cn.zxl.common.ReflectUtil;
import cn.zxl.orm.common.BaseEntity;
import cn.zxl.orm.common.CommonBaseService;
import cn.zxl.orm.common.Pager;

public abstract class AbstractRelatedbBusinessLogicAction<T extends BaseEntity> extends AbstractRelatedbAction<T> {

	private static final long serialVersionUID = 3543276946836718120L;

	private CommonBaseService commonBaseService;

	private String rows;

	private String page;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		super.setApplicationContext(applicationContext);
		this.commonBaseService = (CommonBaseService) getApplicationContext().getBean(CommonBaseService.COMMON_BASE_SERVICE_BEAN_NAME);
	}

	@Override
	public String input() {
		prepareInputView();
		return INPUT;
	}

	protected void prepareInputView() {
		if (entity.getId() != null) {
			entity = commonBaseService.load(getClazz(), entity.getId());
		}
	}

	public String list() {
		prepareListView();
		return LIST;
	}

	protected void prepareListView() {
	}

	public String pageData() {
		Pager<T> pager = beforePageData();
		pager = doPageData(pager);
		return afterPageData(pager);
	}

	protected Pager<T> beforePageData() {
		Pager<T> pager = new Pager<T>();
		if (page != null)
			pager.setPageNumber(Integer.valueOf(page));
		if (rows != null)
			pager.setPageSize(Integer.valueOf(rows));
		return pager;
	}

	protected Pager<T> doPageData(Pager<T> pager) {
		return commonBaseService.getByPager(getClazz(), pager, entity);
	}

	protected String afterPageData(Pager<T> pager) {
		ajaxJson(pager.getViewJsonData(), jsonConfigForPageData());
		return null;
	}

	public String save() {
		beforeSave();
		String id = doSave();
		return afterSave(id);
	}

	protected void beforeSave() {
	}

	protected String doSave() {
		return commonBaseService.save(entity);
	}

	protected String afterSave(String id) {
		ajaxSuccess();
		return null;
	}

	public String update() {
		T entityInDB = beforeUpdate();
		doUpdate(entityInDB);
		return afterUpdate(entityInDB);
	}

	protected T beforeUpdate() {
		return ReflectUtil.copyFieldsByName(entity, commonBaseService.get(getClazz(), entity.getId()));
	}

	protected void doUpdate(T entityInDB) {
		commonBaseService.update(entityInDB);
	}

	protected String afterUpdate(T entityInDB) {
		ajaxSuccess();
		return null;
	}

	public String delete() {
		T entityInDB = beforeDelete();
		doDelete(entityInDB);
		return afterDelete(entityInDB);
	}

	protected T beforeDelete() {
		return commonBaseService.get(getClazz(), entity.getId());
	}

	protected void doDelete(T entityInDB) {
		commonBaseService.delete(entityInDB);
	}

	protected String afterDelete(T entityInDB) {
		ajaxSuccess();
		return null;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
