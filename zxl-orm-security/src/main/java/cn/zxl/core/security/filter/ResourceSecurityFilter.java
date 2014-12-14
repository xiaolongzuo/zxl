package cn.zxl.core.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class ResourceSecurityFilter extends AbstractSecurityInterceptor implements Filter{

	private static final String FILTER_APPLIED = "__spring_security_resourceSecurityFilter_filterApplied";

	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	
	private boolean observeOncePerRequest = true;

	public void init(FilterConfig arg0) throws ServletException { }

	public void destroy() { }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		invoke(new FilterInvocation(request, response, chain));
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
		this.securityMetadataSource = newSource;
	}

	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation fi) throws IOException,
			ServletException {
		if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null) && observeOncePerRequest) {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} else {
			if (fi.getRequest() != null) {
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}
			InterceptorStatusToken token = super.beforeInvocation(fi);
			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			} finally {
				super.finallyInvocation(token);
			}
			super.afterInvocation(token, null);
		}
	}

	public boolean isObserveOncePerRequest() {
		return observeOncePerRequest;
	}

	public void setObserveOncePerRequest(boolean observeOncePerRequest) {
		this.observeOncePerRequest = observeOncePerRequest;
	}

}
