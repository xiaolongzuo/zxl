package cn.zxl.core.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

public class SavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private RequestCache requestCache = new HttpSessionRequestCache();

	public static final String RANDOM_CODE_ATTRIBUTE = "randomCode";

	private String salt;

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		createNewSession(request, response);
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		clearAuthenticationAttributes(request);
		String targetUrl = appendToken(savedRequest.getRedirectUrl(), request);
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected void createNewSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession();
		if (httpSession != null) {
			httpSession.invalidate();
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length >= 1) {
				for (int i = 0; i < cookies.length; i++) {
					cookies[i].setMaxAge(0);
				}
			}
		}
		httpSession = request.getSession(true);
		httpSession.setAttribute(RANDOM_CODE_ATTRIBUTE, getRandomCode(request));
	}

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		return appendToken(super.determineTargetUrl(request, response), request);
	}

	protected String appendToken(String targetUrl, HttpServletRequest request) {
		targetUrl += "?" + RANDOM_CODE_ATTRIBUTE + "=" + getRandomCode(request);
		return targetUrl;
	}

	protected String getRandomCode(HttpServletRequest request) {
		return new Md5PasswordEncoder().encodePassword(request.getSession().getId(), salt);
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

}
