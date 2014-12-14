package cn.zxl.core.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class TokenFilter implements Filter {
	
	private static final String REFERERS_BEAN_NAME = "referers";
	
	private String[] referers;

	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = null;
		String randomCodeInSession = null;
		String randomCode = request.getParameter(SavedRequestAwareAuthenticationSuccessHandler.RANDOM_CODE_ATTRIBUTE);
		if (request instanceof HttpServletRequest) {
			httpServletRequest = (HttpServletRequest) request;
			randomCodeInSession = (String) httpServletRequest.getSession().getAttribute(SavedRequestAwareAuthenticationSuccessHandler.RANDOM_CODE_ATTRIBUTE);
		} else {
			throw new RuntimeException("unsupport op!");
		}
		String referer = httpServletRequest.getHeader("Referer");
		boolean isLocalReferer = false;
		for (int i = 0; i < referers.length; i++) {
			if (referer.startsWith(referers[i])) {
				isLocalReferer = true;
				break;
			}
		}
		if (!isLocalReferer) {
			throw new RuntimeException("url[" + httpServletRequest.getRequestURI() + "] : code is bad!");
		}
		if (randomCode == null || !randomCode.equals(randomCodeInSession)) {
			throw new RuntimeException("url[" + httpServletRequest.getRequestURI() + "] : code is bad!");
		}
		chain.doFilter(request,response);
		request.setAttribute(SavedRequestAwareAuthenticationSuccessHandler.RANDOM_CODE_ATTRIBUTE, randomCodeInSession);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ApplicationContext applicationContext = (ApplicationContext) filterConfig.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		referers = ((String)applicationContext.getBean(REFERERS_BEAN_NAME)).split(",");
	}

}
