package cn.zxl.orm.common;

import java.util.Arrays;
import java.util.List;

public class Options {

	private Options() {
	}

	private static final List<Option> yesOrNoOptions;

	static {
		yesOrNoOptions = Arrays.asList(new Option("0", "否"), new Option("1", "是"));
	}

	public static List<Option> yesOrNoOptions() {
		return yesOrNoOptions;
	}

	public static class Option {

		private String value;

		private String text;

		public Option() {
			super();
		}

		public Option(String value, String text) {
			super();
			this.value = value;
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}
}
