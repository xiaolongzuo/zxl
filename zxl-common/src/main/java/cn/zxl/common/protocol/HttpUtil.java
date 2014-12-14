package cn.zxl.common.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import cn.zxl.common.StringUtil;

public abstract class HttpUtil {

	private static final String DEFAULT_CHARSET = "utf-8";

	private static final int TIMEOUT = 2000;

	public static String get(String url) {
		return get(url, "", DEFAULT_CHARSET);
	}

	public static String get(String url, Map<String, String> map) {
		return get(url, createParam(map), DEFAULT_CHARSET);
	}

	public static String get(String url, String param) {
		return get(url, param, DEFAULT_CHARSET);
	}

	public static String get(String url, Map<String, String> map, String charset) {
		return get(url, createParam(map), charset);
	}

	public static String get(String url, String param, String charset) {
		return post(url + "?" + param, "", DEFAULT_CHARSET);
	}

	public static String delete(String url) {
		return delete(url, "", DEFAULT_CHARSET);
	}

	public static String delete(String url, Map<String, String> map) {
		return delete(url, map, DEFAULT_CHARSET);
	}

	public static String delete(String url, String param) {
		return delete(url, param, DEFAULT_CHARSET);
	}

	public static String delete(String url, Map<String, String> map, String charset) {
		return delete(url, createParam(map), charset);
	}

	public static String delete(String url, String param, String charset) {
		if (param == null) {
			param = "_method=DELETE";
		} else {
			param = param + "&_method=DELETE";
		}
		return post(url, param, charset);
	}

	public static String post(String url) {
		return post(url, "", DEFAULT_CHARSET);
	}

	public static String post(String url, Map<String, String> map) {
		return post(url, map, DEFAULT_CHARSET);
	}

	public static String post(String url, String param) {
		return post(url, param, DEFAULT_CHARSET);
	}

	public static String post(String url, Map<String, String> map, String charset) {
		return post(url, createParam(map), charset);
	}

	public static String post(String url, String param, String charset) {
		return sendHttpRequest(url, param, charset, null, TIMEOUT);
	}

	public static String sendJson(String url, String json) {
		return sendJson(url, json, TIMEOUT);
	}

	public static String sendJson(String url, String json, int timeout) {
		return sendHttpRequest(url, json, DEFAULT_CHARSET, "application/json", timeout);
	}

	public static String sendHttpRequest(String url, String param, String charset, String contentType, int timeout) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = null;
		try {
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			connection.setRequestProperty("accept", "application/json");
			connection.setRequestProperty("connection", "Keep-Alive");
			if (!StringUtil.isEmpty(contentType)) {
				connection.addRequestProperty("Content-type", contentType);
			}
			connection.setConnectTimeout(timeout);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			if (!StringUtil.isEmpty(param)) {
				out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), charset));
				out.print(param);
				out.flush();
			}
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				if (result == null) {
					result = "";
				}
				result += line;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
		return result;
	}

	public static String createParam(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for (String key : map.keySet()) {
			stringBuffer.append(key).append("=").append(map.get(key)).append("&");
		}
		return stringBuffer.substring(0, stringBuffer.length() - 1);
	}

}
