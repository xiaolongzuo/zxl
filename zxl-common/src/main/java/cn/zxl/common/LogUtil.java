package cn.zxl.common;

import org.apache.log4j.Logger;

public abstract class LogUtil {

	public static Logger logger(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}

	public static void debug(Logger logger, String message) {
		debug(logger, message, null);
	}

	public static void debug(Logger logger, String message, Exception exception) {
		if (logger.isDebugEnabled()) {
			if (exception != null) {
				logger.debug(message, exception);
			} else {
				logger.debug(message);
			}
		}
	}

	public static void info(Logger logger, String message) {
		info(logger, message, null);
	}

	public static void info(Logger logger, String message, Exception exception) {
		if (logger.isInfoEnabled()) {
			if (exception != null) {
				logger.info(message, exception);
			} else {
				logger.info(message);
			}
		}
	}

	public static void warn(Logger logger, String message) {
		warn(logger, message, null);
	}

	public static void warn(Logger logger, String message, Exception exception) {
		if (exception != null) {
			logger.warn(message, exception);
		} else {
			logger.warn(message);
		}
	}

	public static void error(Logger logger, String message) {
		error(logger, message, null);
	}

	public static void error(Logger logger, String message, Exception exception) {
		if (exception != null) {
			logger.error(message, exception);
		} else {
			logger.error(message);
		}
	}

}
