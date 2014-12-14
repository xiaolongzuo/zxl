package cn.zxl.mapper.config;

public class Relation {

	private String leftName;

	private String rightName;

	private String dateFormat;

	private String rule;

	private boolean reverse;

	public Relation(String leftName, String rightName, String dateFormat, String rule, boolean reverse) {
		super();
		this.leftName = leftName;
		this.rightName = rightName;
		this.dateFormat = dateFormat;
		this.rule = rule;
		this.reverse = reverse;
	}

	public boolean isReverse() {
		return reverse;
	}

	public String getLeftName() {
		return leftName;
	}

	public String getRightName() {
		return rightName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getRule() {
		return rule;
	}

	@Override
	public String toString() {
		return "Relation [leftName=" + leftName + ", rightName=" + rightName + ", dateFormat=" + dateFormat + ", rule=" + rule + "]";
	}

}
