package cn.zxl.hbase.orm.generator;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import cn.zxl.hbase.orm.RowKeyGenerator;
import cn.zxl.hbase.orm.domain.TestLog;

@Component
public class TestLogRowKeyGenerator implements RowKeyGenerator<TestLog> {

	private AtomicInteger atomicInteger = new AtomicInteger(1);

	@Override
	public String generate(TestLog entity) {
		return entity.getIn().get("ts") + atomicInteger.getAndIncrement();
	}

}
