package cn.zxl.orm.common;

import java.util.StringTokenizer;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class HibernateNamingStrategy extends ImprovedNamingStrategy {

	private static final long serialVersionUID = -6608653199171747403L;

	private static final String SEPARATOR = "_";

	private String prefix;

	private int maxLength = 30;

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String classToTableName(String className) {
		return prefix + SEPARATOR + abbreviateName(super.classToTableName(className), maxLength);
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return abbreviateName(super.propertyToColumnName(propertyName), maxLength);
	}

	@Override
	public String tableName(String tableName) {
		return prefix + SEPARATOR + abbreviateName(super.tableName(tableName), maxLength);
	}

	@Override
	public String columnName(String columnName) {
		return abbreviateName(super.columnName(columnName), maxLength);
	}

	public static String abbreviateName(String someName, int maxLength) {
		if (someName.length() <= maxLength)
			return someName;

		String[] tokens = splitName(someName);
		shortenName(someName, tokens, maxLength);

		return assembleResults(tokens);
	}

	private static String[] splitName(String someName) {
		StringTokenizer toki = new StringTokenizer(someName, SEPARATOR);
		String[] tokens = new String[toki.countTokens()];
		int i = 0;
		while (toki.hasMoreTokens()) {
			tokens[i] = toki.nextToken();
			i++;
		}
		return tokens;
	}

	private static void shortenName(String someName, String[] tokens, int maxLength) {
		int currentLength = someName.length();
		while (currentLength > maxLength) {
			if (isAllOneLength(tokens)) {
				String oldToken = tokens[tokens.length - 1];
				String[] newTokens = new String[tokens.length - 1];
				System.arraycopy(tokens, 0, newTokens, 0, newTokens.length);
				tokens = newTokens;
				currentLength -= oldToken.length() + 1;
			} else {
				int tokenIndex = getIndexOfLongest(tokens);
				String oldToken = tokens[tokenIndex];
				tokens[tokenIndex] = oldToken.substring(0, (oldToken.length() - 1 >= 0) ? (oldToken.length() - 1) : 0);
				currentLength -= oldToken.length() - tokens[tokenIndex].length();
			}
		}
	}

	private static boolean isAllOneLength(String[] tokens) {
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].length() > 1) {
				return false;
			}
		}
		return true;
	}

	private static String assembleResults(String[] tokens) {
		StringBuilder result = new StringBuilder(tokens[0]);
		for (int j = 1; j < tokens.length; j++) {
			if (tokens[j].trim().length() == 0) {
				continue;
			}
			result.append(SEPARATOR).append(tokens[j]);
		}
		return result.toString();
	}

	private static int getIndexOfLongest(String[] tokens) {
		int maxLength = 0;
		int index = -1;
		for (int i = 0; i < tokens.length; i++) {
			String string = tokens[i];
			if (maxLength < string.length()) {
				maxLength = string.length();
				index = i;
			}
		}
		return index;
	}

}
