package cn.zxl.common;

public abstract class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isNull(String s) {
		return s == null;
	}

	public static String getZero(int number) {
		if (number == 0) {
			return "";
		} else if (number == 1) {
			return "0";
		} else if (number == 2) {
			return "00";
		} else if (number == 3) {
			return "000";
		} else if (number == 4) {
			return "0000";
		} else if (number == 5) {
			return "00000";
		} else {
			throw new IllegalArgumentException("number must >=0 & <= 6");
		}
	}

}
