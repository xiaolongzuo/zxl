package cn.zxl.orm.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.common.BeanUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.orm.common.util.AnnotationUtil;

public final class CommonBaseDaoImpl implements CommonBaseDao, ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	private SessionFactory sessionFactory;

	private SqlSessionTemplate sqlSessionTemplate;

	private String tablePrefix;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (sessionFactory == null) {
			sessionFactory = (SessionFactory) applicationContext.getBean(SESSION_FACTORY_BEAN_NAME);
		}
		if (sqlSessionTemplate == null) {
			sqlSessionTemplate = (SqlSessionTemplate) applicationContext.getBean(SQL_SESSION_TEMPLATE_BEAN_NAME);
		}
		if (tablePrefix == null) {
			tablePrefix = (String) applicationContext.getBean(TABLE_PREFIX_BEAN_NAME);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	@Override
	public String getTablePrefix() {
		return tablePrefix;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public <T> String save(T entity) {
		return (String) getHibernateSession().save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T merge(T entity) {
		return (T) getHibernateSession().merge(entity);
	}

	@Override
	public <T> void persist(T entity) {
		getHibernateSession().persist(entity);
	}

	@Override
	public <T> void update(T entity) {
		getHibernateSession().update(entity);
	}

	@Override
	public <T> void delete(T entity) {
		getHibernateSession().delete(entity);
	}

	@Override
	public <T> List<String> save(List<T> entityList) {
		List<String> idList = new ArrayList<String>();
		Session session = getHibernateSession();
		for (T entity : entityList) {
			idList.add((String) session.save(entity));
		}
		return idList;
	}

	@Override
	public <T> void update(List<T> entityList) {
		Session session = getHibernateSession();
		for (T entity : entityList) {
			session.update(entity);
		}
	}

	@Override
	public <T> void delete(List<T> entityList) {
		Session session = getHibernateSession();
		for (T entity : entityList) {
			session.delete(entity);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> clazz) {
		Query query = getHibernateSession().createQuery("from " + AnnotationUtil.getEntityAnnotationName(clazz));
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz, String id) {
		return (T) getHibernateSession().get(clazz, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T load(Class<T> clazz, String id) {
		return (T) getHibernateSession().load(clazz, id);
	}

	@Override
	public <T> List<T> getList(Class<T> clazz, T entity) {
		return getList(clazz, entity, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getList(Class<T> clazz, T entity, boolean useLike) {
		StringBuffer sqlBuffer = new StringBuffer("from " + AnnotationUtil.getEntityAnnotationName(clazz) + " where 1=1 ");

		List<Object> valueList = generateValueListAndSetSql(clazz, entity, sqlBuffer, useLike);

		String querySql = sqlBuffer.toString() + " order by createDate desc";
		Query query = getHibernateSession().createQuery(querySql);
		setParameters(query, valueList);

		return query.list();
	}

	@Override
	public <T> T getUnique(Class<T> clazz, T entity) {
		return getUnique(clazz, entity, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getUnique(Class<T> clazz, T entity, boolean useLike) {
		StringBuffer sqlBuffer = new StringBuffer("from " + AnnotationUtil.getEntityAnnotationName(clazz) + " where 1=1 ");

		List<Object> valueList = generateValueListAndSetSql(clazz, entity, sqlBuffer, useLike);

		String querySql = sqlBuffer.toString();
		Query query = getHibernateSession().createQuery(querySql);
		setParameters(query, valueList);

		return (T) query.uniqueResult();
	}

	@Override
	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager) {
		return getByPager(clazz, pager, null);
	}

	@Override
	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity) {
		return getByPager(clazz, pager, entity, true);
	}

	@Override
	public <T extends BaseEntity> Pager<T> getByPager(Class<T> clazz, Pager<T> pager, T entity, boolean useLike) {
		if (pager == null) {
			pager = new Pager<T>();
		}

		StringBuffer sqlBuffer = new StringBuffer("from " + AnnotationUtil.getEntityAnnotationName(clazz) + " where 1=1 ");

		List<Object> valueList = generateValueListAndSetSql(clazz, entity, sqlBuffer, useLike);

		pager.setDataList(getDataList(valueList, sqlBuffer, pager));

		pager.setTotalCount(getTotalCount(sqlBuffer, valueList));

		return pager;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity> List<T> getDataList(List<Object> valueList, StringBuffer sqlBuffer, Pager<T> pager) {
		String querySql = sqlBuffer.toString() + " order by createDate desc";
		Query query = getHibernateSession().createQuery(querySql);
		setParameters(query, valueList);
		query.setFirstResult(pager.getFirstIndex());
		query.setMaxResults(pager.getPageSize());
		return query.list();
	}

	private <T extends BaseEntity> int getTotalCount(StringBuffer sqlBuffer, List<Object> valueList) {
		sqlBuffer.insert(0, "select count(id) ");
		Query query = getHibernateSession().createQuery(sqlBuffer.toString());
		setParameters(query, valueList);
		return ((Long) query.uniqueResult()).intValue();
	}

	private <T> void setParameters(Query query, List<Object> valueList) {
		for (int i = 0; i < valueList.size(); i++) {
			query.setParameter(String.valueOf(i), valueList.get(i));
		}
	}

	private <T> List<Object> generateValueListAndSetSql(Class<T> clazz, T entity, StringBuffer sqlBuffer, boolean useLike) {
		List<Object> valueList = new ArrayList<Object>();
		if (entity != null) {
			Field[] fields = ReflectUtil.getAllFields(entity);
			for (int i = 0, index = 0; i < fields.length; i++) {
				Field field = fields[i];
				int modifiers = field.getModifiers();
				if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers) || BeanUtil.isTransientId(clazz, field)) {
					continue;
				}
				Object value = ReflectUtil.getFieldValue(entity, field);
				if (BeanUtil.isEmpty(value)) {
					continue;
				}
				if (field.getType() == String.class && useLike) {
					sqlBuffer.append("and " + field.getName() + " like ?" + index++ + " ");
					valueList.add("%" + value + "%");
				} else {
					sqlBuffer.append("and " + field.getName() + "=?" + index++ + " ");
					valueList.add(value);
				}
			}
		}
		return valueList;
	}

	private Session getHibernateSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception ignored) {
			return sessionFactory.openSession();
		}
	}

	@Override
	public <T> T selectOne(String statement) {
		return sqlSessionTemplate.selectOne(statement, putTablePrefix(null));
	}

	@Override
	public <T> T selectOne(String statement, Map<String, Object> parameter) {
		return sqlSessionTemplate.selectOne(statement, putTablePrefix(parameter));
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return sqlSessionTemplate.selectMap(statement, putTablePrefix(null), mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> parameter, String mapKey) {
		return sqlSessionTemplate.selectMap(statement, putTablePrefix(parameter), mapKey);
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Map<String, Object> parameter, String mapKey, RowBounds rowBounds) {
		return sqlSessionTemplate.selectMap(statement, putTablePrefix(parameter), mapKey, rowBounds);
	}

	@Override
	public <T> List<T> selectList(String statement) {
		return sqlSessionTemplate.selectList(statement, putTablePrefix(null));
	}

	@Override
	public <T> List<T> selectList(String statement, Map<String, Object> parameter) {
		return sqlSessionTemplate.selectList(statement, putTablePrefix(parameter));
	}

	@Override
	public <T> List<T> selectList(String statement, Map<String, Object> parameter, RowBounds rowBounds) {
		return sqlSessionTemplate.selectList(statement, putTablePrefix(parameter), rowBounds);
	}

	@Override
	public void select(String statement, ResultHandler handler) {
		sqlSessionTemplate.select(statement, putTablePrefix(null), handler);
	}

	@Override
	public void select(String statement, Map<String, Object> parameter, ResultHandler handler) {
		sqlSessionTemplate.select(statement, putTablePrefix(parameter), handler);
	}

	@Override
	public void select(String statement, Map<String, Object> parameter, RowBounds rowBounds, ResultHandler handler) {
		sqlSessionTemplate.select(statement, putTablePrefix(parameter), rowBounds, handler);
	}

	private Map<String, Object> putTablePrefix(Map<String, Object> parameter) {
		if (parameter == null) {
			parameter = new HashMap<String, Object>();
		}
		parameter.put("tablePrefix", getTablePrefix());
		return parameter;
	}
}
