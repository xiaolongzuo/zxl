package cn.zxl.mvc.hbase.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.JstlView;

import cn.zxl.mvc.hbase.util.ViewUtil;

public class HBaseJstlView extends JstlView {

	protected void exposeModelAsRequestAttributes(Map<String, Object> model, HttpServletRequest request) throws Exception {
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			String modelName = entry.getKey();
			Object modelValue = entry.getValue();
			if (modelName.equals(ViewUtil.VIEW_KEY)) {
				request.setAttribute(ViewUtil.RESULT_KEY, ViewUtil.transfer(modelValue));
			} else if (modelValue != null) {
				request.setAttribute(modelName, modelValue);
			} else {
				request.removeAttribute(modelName);
			}
		}
	}
	
}
