package cn.zxl.orm.common;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.common.ReflectUtil;

@SuppressWarnings("unchecked")
public class BaseServiceImpl<E extends BaseEntity> implements BaseService<E>, ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private Class<E> clazz;

	private CommonBaseDao commonBaseDao;

	public BaseServiceImpl() {
		clazz = (Class<E>) ReflectUtil.getParameterizedType(getClass());
		if (clazz == null || clazz == BaseEntity.class) {
			throw new IllegalArgumentException("must has parameterized type!");
		}
	}

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

	@Override
	public String save(E entity) {
		return commonBaseDao.save(entity);
	}

	@Override
	public E merge(E entity) {
		return commonBaseDao.merge(entity);
	}

	@Override
	public void persist(E entity) {
		commonBaseDao.persist(entity);
	}

	@Override
	public void update(E entity) {
		commonBaseDao.update(entity);
	}

	@Override
	public void delete(E entity) {
		commonBaseDao.delete(entity);
	}

	@Override
	public List<String> save(List<E> entityList) {
		return commonBaseDao.save(entityList);
	}

	@Override
	public void update(List<E> entityList) {
		commonBaseDao.update(entityList);
	}

	@Override
	public void delete(List<E> entityList) {
		commonBaseDao.delete(entityList);
	}

	@Override
	public List<E> getAll() {
		return commonBaseDao.getAll(clazz);
	}

	@Override
	public E get(String id) {
		return commonBaseDao.get(clazz, id);
	}

	@Override
	public E load(String id) {
		return commonBaseDao.load(clazz, id);
	}

	@Override
	public List<E> getList(E entity) {
		return commonBaseDao.getList(clazz, entity);
	}

	@Override
	public List<E> getList(E entity, boolean useLike) {
		return commonBaseDao.getList(clazz, entity, useLike);
	}

	@Override
	public E getUnique(E entity) {
		return commonBaseDao.getUnique(clazz, entity);
	}

	@Override
	public E getUnique(E entity, boolean useLike) {
		return commonBaseDao.getUnique(clazz, entity, useLike);
	}

	@Override
	public Pager<E> getByPager(Pager<E> pager) {
		return commonBaseDao.getByPager(clazz, pager);
	}

	@Override
	public Pager<E> getByPager(Pager<E> pager, E entity) {
		return commonBaseDao.getByPager(clazz, pager, entity);
	}

	@Override
	public Pager<E> getByPager(Pager<E> pager, E entity, boolean useLike) {
		return commonBaseDao.getByPager(clazz, pager, entity, useLike);
	}

}
