package cn.zxl.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {

	private static final long ONE_DAY = 1000 * 60 * 60 * 24;

	private static final Date DEFAULT_START_DATE = new Date(1199116800000L);

	public static DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	public static DateFormat getTimetampFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static DateFormat getSortedTimetampFormat() {
		return new SimpleDateFormat("yyyyMMddHHmmss");
	}

	public static Date defaultStart() {
		return DEFAULT_START_DATE;
	}

	public static String defaultStartDate() {
		return dateFormat(defaultStart());
	}

	public static String defaultStartTimestamp() {
		return timestampFormat(defaultStart());
	}

	public static String todayTimestamp() {
		return timestampFormat(today());
	}

	public static String todaySortedTimestamp() {
		return sortedTimestampFormat(today());
	}

	public static String todayDate() {
		return dateFormat(today());
	}

	public static String tomorrowTimestamp() {
		return timestampFormat(tomorrow());
	}

	public static String tomorrowDate() {
		return dateFormat(tomorrow());
	}

	public static Date today() {
		return new Date();
	}

	public static Date tomorrow() {
		return new Date(new Date().getTime() + ONE_DAY);
	}

	public static String dateFormat(long timestamp) {
		return dateFormat(new Date(timestamp));
	}

	public static String timestampFormat(long timestamp) {
		return timestampFormat(new Date(timestamp));
	}

	public static String dateFormat(Date date) {
		return getDateFormat().format(date);
	}

	public static String timestampFormat(Date date) {
		return getTimetampFormat().format(date);
	}

	public static String sortedTimestampFormat(Date date) {
		return getSortedTimetampFormat().format(date);
	}

	public static Date parse(String date) throws ParseException {
		try {
			return parseTimestamp(date);
		} catch (ParseException e) {
			return parseDate(date);
		}
	}

	public static Date parseDate(String date) throws ParseException {
		return getDateFormat().parse(date);
	}

	public static Date parseTimestamp(String date) throws ParseException {
		return getTimetampFormat().parse(date);
	}

}
