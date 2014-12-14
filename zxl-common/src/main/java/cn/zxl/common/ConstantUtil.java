package cn.zxl.common;

public abstract class ConstantUtil {

	public static final Integer YES = 1;

	public static final Integer NO = 0;

	public static boolean isYes(String value) {
		return YES.toString().equals(value);
	}

	public static boolean isNo(String value) {
		return !isYes(value);
	}

	public static boolean isYes(Integer value) {
		return value != null && value == YES;
	}

	public static boolean isNo(Integer value) {
		return !isYes(value);
	}

}
