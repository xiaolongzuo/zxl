package cn.zxl.orm.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public interface BaseDao<E extends BaseEntity> {

	public static final String TABLE_PREFIX_BEAN_NAME = "tablePrefix";

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

	public <T> T selectOne(String statement);

	public <T> T selectOne(String statement, Map<String, Object> parameter);

	public <K, V> Map<K, V> selectMap(String statement, String mapKey);

	public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> parameter, String mapKey);

	public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> parameter, String mapKey, RowBounds rowBounds);

	public <T> List<T> selectList(String statement);

	public <T> List<T> selectList(String statement, Map<String, Object> parameter);

	public <T> List<T> selectList(String statement, Map<String, Object> parameter, RowBounds rowBounds);

	public void select(String statement, ResultHandler handler);

	public void select(String statement, Map<String, Object> parameter, ResultHandler handler);

	public void select(String statement, Map<String, Object> parameter, RowBounds rowBounds, ResultHandler handler);

}