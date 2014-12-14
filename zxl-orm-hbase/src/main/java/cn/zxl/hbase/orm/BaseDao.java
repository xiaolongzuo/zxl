package cn.zxl.hbase.orm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.BeanUtil;
import cn.zxl.common.LogUtil;
import cn.zxl.common.ReflectUtil;
import cn.zxl.hbase.orm.util.HBaseUtil;

public abstract class BaseDao<E extends Table> implements InitializingBean, ApplicationContextAware {

	private static final Logger LOGGER = LogUtil.logger(BaseDao.class);

	public static final String COUNT_COLUMN = "count";

	public static final String SERIAL_VERSION_UID = "serialVersionUID";

	public static final byte[] COUNT_COLUMN_BYTE_ARRAY = Bytes.toBytes(COUNT_COLUMN);

	private ApplicationContext applicationContext;

	private Class<E> clazz;

	private RowKeyGenerator<E> rowKeyGenerator;

	private String tableName;

	public BaseDao() {
		super();
		setClazz(ReflectUtil.getParameterizedType(getClass()));
	}

	@SuppressWarnings("unchecked")
	public void setClazz(Class<?> clazz) {
		if (clazz != null) {
			this.clazz = (Class<E>) clazz;
			this.tableName = clazz.getSimpleName();
		}
	}

	protected abstract String rowKeyGeneratorName();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		rowKeyGenerator = (RowKeyGenerator<E>) applicationContext.getBean(rowKeyGeneratorName());
		if (rowKeyGenerator == null) {
			throw new NullPointerException("rowKeyGenerator is null!");
		}
	}

	protected HTableInterface getHTableInterface() {
		try {
			return HBaseFactory.getHTableInterface(tableName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void closeHTableInterface(HTableInterface hTableInterface) {
		try {
			HBaseFactory.closeHTableInterface(hTableInterface);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public final String put(E entity) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Put put = buildPut(entity);
			String rowKey = Bytes.toString(put.getRow());
			hTableInterface.put(put);
			return rowKey;
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final void put(List<E> entityList) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			List<Put> puts = buildPutList(entityList);
			hTableInterface.put(puts);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final void delete(String id) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Delete delete = new Delete(Bytes.toBytes(id));
			hTableInterface.delete(delete);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final void delete(String id, String familyName) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Delete delete = new Delete(Bytes.toBytes(id));
			delete.deleteFamily(Bytes.toBytes(familyName));
			hTableInterface.delete(delete);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final E get(String id) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Get get = new Get(Bytes.toBytes(id));
			Result result = hTableInterface.get(get);
			return parse(result);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final Object get(String id, String familyName) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Get get = new Get(Bytes.toBytes(id));
			get.addFamily(Bytes.toBytes(familyName));
			Result result = hTableInterface.get(get);
			return parseFamily(familyName, result);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final Object get(String id, String familyName, String columnName) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Get get = new Get(Bytes.toBytes(id));
			get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
			Result result = hTableInterface.get(get);
			return parseColumn(familyName, columnName, result);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan() {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan();
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan(String start) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan(Bytes.toBytes(start));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan(String start, String stop) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan(Bytes.toBytes(start), Bytes.toBytes(stop));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan(Filter... filters) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan();
			scan.setFilter(new FilterList(filters));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan(String start, Filter... filters) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan(Bytes.toBytes(start));
			scan.setFilter(new FilterList(filters));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scan(String start, String stop, Filter... filters) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan(Bytes.toBytes(start), Bytes.toBytes(stop));
			scan.setFilter(new FilterList(filters));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	public final List<E> scanByRowPrefix(String prefix) {
		HTableInterface hTableInterface = getHTableInterface();
		try {
			Scan scan = new Scan();
			scan.setFilter(new PrefixFilter(Bytes.toBytes(prefix)));
			ResultScanner resultScanner = hTableInterface.getScanner(scan);
			return parse(resultScanner);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		} finally {
			closeHTableInterface(hTableInterface);
		}
	}

	protected final List<E> parse(ResultScanner resultScanner) {
		List<E> resultList = new ArrayList<E>();
		if (BeanUtil.isEmpty(resultScanner)) {
			return resultList;
		}
		for (Result result : resultScanner) {
			resultList.add(parse(result));
		}
		return resultList;
	}

	public final List<Put> buildPutList(List<E> entityList) {
		List<Put> puts = new ArrayList<Put>();
		if (ArrayUtil.isEmpty(entityList)) {
			return puts;
		}
		for (E entity : entityList) {
			puts.add(buildPut(entity));
		}
		return puts;
	}

	protected final E parse(Result result) {
		if (result.isEmpty()) {
			return null;
		}
		try {
			E entity = clazz.newInstance();
			entity.setId(Bytes.toString(result.getRow()));
			for (Field familyField : ReflectUtil.getAllFields(clazz)) {
				if (!HBaseUtil.isFamily(familyField)) {
					continue;
				}
				byte[] familyName = Bytes.toBytes(familyField.getName());
				if (HBaseUtil.isBaseFamily(familyField)) {
					ReflectUtil.setFieldValue(entity, familyField, parseNotArrayClass(familyField, familyName, result));
				} else if (HBaseUtil.isArrayFamily(familyField)) {
					byte[] countBytes = result.getValue(familyName, COUNT_COLUMN_BYTE_ARRAY);
					if (countBytes != null) {
						int count = Bytes.toInt(countBytes);
						Map<byte[], byte[]> familyMap = result.getFamilyMap(familyName);
						if (familyMap != null) {
							ReflectUtil.setFieldValue(entity, familyField, parseArrayClass(familyField, count, familyMap));
						}
					}
				} else {
					LogUtil.warn(LOGGER, "������ݿ��ѯ���ʱ��������[" + familyField.getName() + "]");
				}
			}
			return entity;
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}
	}

	protected final Object parseFamily(String familyName, Result result) {
		if (result.isEmpty()) {
			return null;
		}
		try {
			Field familyField = clazz.getDeclaredField(familyName);
			byte[] familyNameBytes = Bytes.toBytes(familyField.getName());
			if (HBaseUtil.isBaseFamily(familyField)) {
				return parseNotArrayClass(familyField, familyNameBytes, result);
			} else if (HBaseUtil.isArrayFamily(familyField)) {
				byte[] countBytes = result.getValue(familyNameBytes, COUNT_COLUMN_BYTE_ARRAY);
				if (countBytes != null) {
					int count = Bytes.toInt(countBytes);
					Map<byte[], byte[]> familyMap = result.getFamilyMap(familyNameBytes);
					if (familyMap != null) {
						return parseArrayClass(familyField, count, familyMap);
					}
				}
			} else {
				LogUtil.warn(LOGGER, "������ݿ��ѯ���ʱ��������[" + familyField.getName() + "]");
			}
			return null;
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}
	}

	protected final Family parseNotArrayClass(Field familyField, byte[] familyName, Result result) throws Exception {
		Family family = new Family();
		family.putByteMap(result.getFamilyMap(familyName));
		return family;
	}

	protected final List<Object> parseArrayClass(Field familyField, int count, Map<byte[], byte[]> familyMap) throws Exception {
		Class<?> genericClass = ReflectUtil.getParameterizedType(familyField);
		List<Object> list = new ArrayList<Object>();
		if (genericClass == String.class) {
			for (int i = 0; i < count; i++) {
				list.add(Bytes.toString(familyMap.get(Bytes.toBytes(String.valueOf(i)))));
			}
		} else if (Family.class.isAssignableFrom(genericClass)) {
			for (int i = 0; i < count; i++) {
				Family family = new Family();
				family.putByteMap(familyMap);
				list.add(family);
			}
		} else {
			LogUtil.warn(LOGGER, familyField.getName() + "����Family���ͣ�");
		}
		return list;
	}

	protected final String parseColumn(String familyName, String columnName, Result result) {
		if (result.isEmpty()) {
			return null;
		}
		try {
			byte[] value = result.getValue(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
			return Bytes.toString(value);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}
	}

	protected final Put buildPut(E entity) {
		byte[] id = null;
		if (entity.getId() != null) {
			id = Bytes.toBytes(entity.getId());
		} else {
			id = Bytes.toBytes(rowKeyGenerator.generate(entity));
		}
		Put put = new Put(id);
		for (Field familyField : ReflectUtil.getAllFields(entity)) {
			if (!HBaseUtil.isFamily(familyField)) {
				continue;
			}
			byte[] familyName = Bytes.toBytes(familyField.getName());
			if (HBaseUtil.isBaseFamily(familyField)) {
				put = buildNotArrayPut(put, familyField, familyName, entity);
			} else if (HBaseUtil.isArrayFamily(familyField)) {
				delete(Bytes.toString(id), Bytes.toString(familyName));
				put = buildArrayPut(put, familyField, familyName, entity);
			} else {
				LogUtil.warn(LOGGER, "������ݿ��ѯ���ʱ��������[" + familyField.getName() + "]");
			}
		}
		return put;
	}

	protected final Put buildNotArrayPut(Put put, Field familyField, byte[] familyName, E entity) {
		Family family = (Family) ReflectUtil.getFieldValue(entity, familyField);
		for (String key : family.keySet()) {
			put.add(familyName, Bytes.toBytes(key), Bytes.toBytes(family.get(key)));
		}
		return put;
	}

	protected final Put buildArrayPut(Put put, Field familyField, byte[] familyName, E entity) {
		List<?> list = (List<?>) ReflectUtil.getFieldValue(entity, familyField);
		Class<?> genericClass = ReflectUtil.getParameterizedType(familyField);
		if (list != null) {
			put.add(familyName, COUNT_COLUMN_BYTE_ARRAY, Bytes.toBytes(list.size()));
			if (genericClass == String.class) {
				for (int i = 0; i < list.size(); i++) {
					put.add(familyName, Bytes.toBytes(String.valueOf(i)), Bytes.toBytes(list.get(i).toString()));
				}
			} else if (Family.class.isAssignableFrom(genericClass)) {
				for (int i = 0; i < list.size(); i++) {
					Family family = (Family) list.get(i);
					for (String key : family.keySet()) {
						put.add(familyName, Bytes.toBytes(key), Bytes.toBytes(family.get(key)));
					}
				}
			}
		}
		return put;
	}

}
