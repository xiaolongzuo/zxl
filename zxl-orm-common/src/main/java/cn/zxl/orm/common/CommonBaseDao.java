package cn.zxl.orm.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public interface CommonBaseDao {

	public static final String COMMON_BASE_DAO_BEAN_NAME = "commonBaseDao";

	public static final String SQL_SESSION_TEMPLATE_BEAN_NAME = "sqlSessionTemplate";

	public static final String SESSION_FACTORY_BEAN_NAME = "sessionFactory";

	public static final String TABLE_PREFIX_BEAN_NAME = "tablePrefix";

	public String getTablePrefix();

	public <T> String save(T entity);

	public <T> T merge(T entity);

	public <T> void persist(T entity);

	public <T> void update(T entity);

	public <T> void delete(T entity);

	public <T> List<String> save(List<T> entityList);

	public <T> void update(List<T> entityList);

	public <T> void delete(List<T> entityList);

	public <T> List<T> getAll(Class<T> clazz);

	public <T> T get(Class<T> clazz, String id);

	public <T> T load(Class<T> clazz, String id);

	public <T> List<T> getList(Class<T> clazz, T entity);

	public <T> List<T> getList(Class<T> clazz, T entity, boolean useLike);

	public <T> T getUnique(Class<T> clazz, T entity);

	public <T> T getUnique(Class<T> clazz, T entity, boolean useLike);

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager);

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity);

	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity, boolean useLike);

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
