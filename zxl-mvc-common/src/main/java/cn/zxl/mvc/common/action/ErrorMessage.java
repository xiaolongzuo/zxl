package cn.zxl.mvc.common.action;

public enum ErrorMessage {

	NO_RESULT {
		@Override
		public String getErrorMessage() {
			return "没有返回结果！";
		}
	},
	DEFAULT {
		@Override
		public String getErrorMessage() {
			return "系统发生内部错误！";
		}
	};

	public abstract String getErrorMessage();

}
