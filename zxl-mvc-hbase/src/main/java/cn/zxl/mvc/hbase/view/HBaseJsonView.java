package cn.zxl.mvc.hbase.view;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import cn.zxl.mvc.hbase.util.ViewUtil;

public class HBaseJsonView implements View{

	private static final String CONTENT_TYPE = "application/json";
	
	private boolean cached = false;
	
	public String getContentType() {
		return CONTENT_TYPE;
	}
	
	public void setCached(boolean cached) {
		this.cached = cached;
	}

	public void render(Map<String, ?> model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (!cached) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-Type", "application/json;charset=utf-8");
		Object value = model.get(ViewUtil.VIEW_KEY);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(ViewUtil.transfer(value).toString());
		printWriter.flush();
	}
	
}
