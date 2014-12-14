package cn.zxl.orm.common;

import java.util.List;

public interface BaseService<E extends BaseEntity> {

	public static final String BASE_SERVICE_BEAN_NAME = "baseService";

	public String save(E entity);

	public E merge(E entity);

	public void persist(E entity);

	public void update(E entity);

	public void delete(E entity);

	public List<String> save(List<E> entityList);

	public void update(List<E> entityList);

	public void delete(List<E> entityList);

	public List<E> getAll();

	public E get(String id);

	public E load(String id);

	public List<E> getList(E entity);

	public List<E> getList(E entity, boolean useLike);

	public E getUnique(E entity);

	public E getUnique(E entity, boolean useLike);

	public Pager<E> getByPager(Pager<E> pager);

	public Pager<E> getByPager(Pager<E> pager, E entity);

	public Pager<E> getByPager(Pager<E> pager, E entity, boolean useLike);

}