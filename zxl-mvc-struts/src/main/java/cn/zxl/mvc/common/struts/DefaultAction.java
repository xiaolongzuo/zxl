package cn.zxl.mvc.common.struts;

import com.opensymphony.xwork2.ActionSupport;

public class DefaultAction extends ActionSupport {

	private static final long serialVersionUID = 8187101416431059625L;
	
	private static final String DEFAULT_RESULT = "error-page-404";

	@Override
	public String execute() throws Exception {
		return DEFAULT_RESULT;
	}
	
}
