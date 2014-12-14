package cn.zxl.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author zuoxiaolong
 *
 */
public class ArrayUtilTest {

	@Test
	public void testIsEmpty() {
		Assert.assertEquals(true, ArrayUtil.isEmpty((Collection<?>) null));
		Assert.assertEquals(true, ArrayUtil.isEmpty(Collections.emptyList()));
		Assert.assertEquals(true, ArrayUtil.isEmpty((Object[]) null));
		Assert.assertEquals(true, ArrayUtil.isEmpty(new String[] {}));
		Assert.assertEquals(false, ArrayUtil.isEmpty(Arrays.asList("1", "2")));
		Assert.assertEquals(false, ArrayUtil.isEmpty(new String[] { "1", "3" }));
	}

	@Test
	public void testListToArray() {
		String[] strings1 = new String[] { "1", "2", "3" };
		String[] strings2 = ArrayUtil.listToArray(Arrays.asList(strings1));
		Assert.assertTrue(strings1.length == strings2.length);
		for (int i = 0; i < strings1.length && i < strings2.length; i++) {
			Assert.assertTrue(strings1[i].equals(strings2[i]));
		}
	}

}
