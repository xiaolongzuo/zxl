/**
 * 
 */
package cn.zxl.common.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author zuoxiaolong
 *
 */
public class IOUtilTest {

	private static final String REAL_CONTENT = "this is a file!";

	@Test
	public void testRead() throws FileNotFoundException, IOException {
		String content = IOUtil.readString(IOUtilTest.class.getResourceAsStream("io-test.txt"), 4096);
		Assert.assertEquals(content, REAL_CONTENT);
	}

}
