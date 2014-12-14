package cn.zxl.hbase.orm;


public interface RowKeyGenerator<T extends Table> {
	
	public String generate(T entity);
	
}
