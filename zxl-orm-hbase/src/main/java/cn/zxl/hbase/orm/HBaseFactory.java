package cn.zxl.hbase.orm;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.log4j.Logger;

import cn.zxl.common.ArrayUtil;
import cn.zxl.common.LogUtil;

public class HBaseFactory {

	private static final Logger LOGGER = LogUtil.logger(HBaseFactory.class);

	private static final Configuration CONFIGURATION = HBaseConfiguration.create();

	private static final HBaseAdmin HBASE_ADMIN;

	private static final HConnection HBASE_CONNECTION;

	static {
		try {
			HBaseAdmin.checkHBaseAvailable(CONFIGURATION);
			HBASE_ADMIN = new HBaseAdmin(CONFIGURATION);
			HBASE_CONNECTION = HConnectionManager.createConnection(CONFIGURATION);
		} catch (Exception cause) {
			throw new RuntimeException("��ʼ��HBase������Ϣʧ�ܣ�", cause);
		}
	}

	private HBaseFactory() {
	}

	/**
	 * �û���ȡ��ӿڣ�ִ��CURD������
	 * 
	 * @param tableName
	 *            �����
	 * @return ��ӿ�
	 * @throws IOException
	 */
	public static HTableInterface getHTableInterface(String tableName) throws IOException {
		return HBASE_CONNECTION.getTable(tableName);
	}

	public static void closeHTableInterface(HTableInterface hTableInterface) throws IOException {
		hTableInterface.close();
	}

	/**
	 * ������
	 * 
	 * @param tableName
	 *            �����
	 * @param columnFamilies
	 *            �����������
	 */
	public static void createTable(String tableName, String[] columnFamilies, Integer timeToLive) {
		LogUtil.info(LOGGER, "��ʼ�����[" + tableName + "]......");
		try {
			if (!HBASE_ADMIN.tableExists(tableName)) {
				LogUtil.info(LOGGER, "�����ڵı?������[" + tableName + "]......");
				HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
				for (int i = 0; i < columnFamilies.length; i++) {
					LogUtil.info(LOGGER, "[" + tableName + "]��������[" + columnFamilies[i] + "]......");
					HColumnDescriptor newHColumnDescriptor = new HColumnDescriptor(columnFamilies[i]);
					newHColumnDescriptor.setTimeToLive(timeToLive);
					hTableDescriptor.addFamily(newHColumnDescriptor);
				}
				HBASE_ADMIN.createTable(hTableDescriptor);
				LogUtil.info(LOGGER, "������[" + tableName + "]�ɹ�......");
				return;
			}
			if (ArrayUtil.isEmpty(columnFamilies)) {
				throw new RuntimeException("������ʧ�ܣ�û�з������壡");
			}
			HTableDescriptor hTableDescriptor = HBASE_ADMIN.getTableDescriptor(TableName.valueOf(tableName));
			HColumnDescriptor[] hColumnDescriptor = hTableDescriptor.getColumnFamilies();
			boolean needModify = false;
			for (int i = 0; i < columnFamilies.length; i++) {
				boolean contains = false;
				for (int j = 0; j < hColumnDescriptor.length; j++) {
					if (hColumnDescriptor[j].getNameAsString().equals(columnFamilies[i]) && hColumnDescriptor[j].getTimeToLive() == timeToLive) {
						contains = true;
						break;
					}
				}
				if (!contains) {
					needModify = true;
					break;
				}
			}
			LogUtil.info(LOGGER, "�Ѵ��ڵı�[" + tableName + "]......");
			if (needModify) {
				LogUtil.info(LOGGER, "����Ҫ����[" + tableName + "]......");
				Set<String> columnFamiliesSet = new HashSet<String>();
				for (int i = 0; i < hColumnDescriptor.length; i++) {
					columnFamiliesSet.add(hColumnDescriptor[i].getNameAsString());
				}
				for (int i = 0; i < columnFamilies.length; i++) {
					columnFamiliesSet.add(columnFamilies[i]);
				}
				HTableDescriptor newHTableDescriptor = new HTableDescriptor(hTableDescriptor);
				HBASE_ADMIN.disableTable(tableName);
				if (HBASE_ADMIN.isTableDisabled(tableName)) {
					for (String columnFamily : columnFamiliesSet) {
						LogUtil.info(LOGGER, "[" + tableName + "]��������[" + columnFamily + "]......");
						HColumnDescriptor newHColumnDescriptor = new HColumnDescriptor(columnFamily);
						newHColumnDescriptor.setTimeToLive(timeToLive);
						newHTableDescriptor.addFamily(newHColumnDescriptor);
						HBASE_ADMIN.modifyColumn(tableName, newHColumnDescriptor);
					}
					HBASE_ADMIN.modifyTable(tableName, newHTableDescriptor);
					HBASE_ADMIN.enableTable(tableName);
					if (!HBASE_ADMIN.isTableEnabled(tableName)) {
						throw new RuntimeException("������[" + tableName + "]ʧ�ܣ�");
					}
				}
			}
		} catch (IOException cause) {
			throw new RuntimeException("������ʧ�ܣ�", cause);
		}
	}
}
