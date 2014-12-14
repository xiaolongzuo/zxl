package cn.zxl.orm.common;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommonBaseServiceImpl implements CommonBaseService, InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;

	private CommonBaseDao commonBaseDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (commonBaseDao == null) {
			commonBaseDao = (CommonBaseDao) applicationContext.getBean(CommonBaseDao.COMMON_BASE_DAO_BEAN_NAME);
		}
	}

	public void setCommonBaseDao(CommonBaseDao commonBaseDao) {
		this.commonBaseDao = commonBaseDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public <T> String save(T entity) {
		return commonBaseDao.save(entity);
	}

	@Override
	public <T> T merge(T entity) {
		return commonBaseDao.merge(entity);
	}

	@Override
	public <T> void persist(T entity) {
		commonBaseDao.persist(entity);
	}

	@Override
	public <T> void update(T entity) {
		commonBaseDao.update(entity);
	}

	@Override
	public <T> void delete(T entity) {
		commonBaseDao.delete(entity);
	}

	@Override
	public <T> List<String> save(List<T> entityList) {
		return commonBaseDao.save(entityList);
	}

	@Override
	public <T> void update(List<T> entityList) {
		commonBaseDao.update(entityList);
	}

	@Override
	public <T> void delete(List<T> entityList) {
		commonBaseDao.delete(entityList);
	}

	public <T> List<T> getAll(Class<T> clazz) {
		return commonBaseDao.getAll(clazz);
	}

	public <T> T get(Class<T> clazz, String id) {
		return commonBaseDao.get(clazz, id);
	}

	public <T> T load(Class<T> clazz, String id) {
		return commonBaseDao.load(clazz, id);
	}

	public <T> List<T> getList(Class<T> clazz, T entity) {
		return commonBaseDao.getList(clazz, entity);
	}

	public <T> List<T> getList(Class<T> clazz, T entity, boolean useLike) {
		return commonBaseDao.getList(clazz, entity, useLike);
	}

	public <T> T getUnique(Class<T> clazz, T entity) {
		return commonBaseDao.getUnique(clazz, entity);
	}

	public <T> T getUnique(Class<T> clazz, T entity, boolean useLike) {
		return commonBaseDao.getUnique(clazz, entity, useLike);
	}

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager) {
		return commonBaseDao.getByPager(clazz, pager);
	}

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity) {
		return commonBaseDao.getByPager(clazz, pager, entity);
	}

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity, boolean useLike) {
		return commonBaseDao.getByPager(clazz, pager, entity, useLike);
	}

}
