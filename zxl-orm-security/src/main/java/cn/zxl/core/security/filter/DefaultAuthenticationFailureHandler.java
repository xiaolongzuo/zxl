package cn.zxl.core.security.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler, InitializingBean {

	private String loginUrl;

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (loginUrl == null) {
			throw new NullPointerException();
		}
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(loginUrl);
		if (exception instanceof UsernameNotFoundException) {
			request.setAttribute("error", "用户名不存在！");
		} else if (exception instanceof DisabledException) {
			request.setAttribute("error", "用户已被禁用！");
		} else if (exception instanceof BadCredentialsException) {
			request.setAttribute("error", "用户名或密码错误！");
		} else {
			request.setAttribute("error", "登陆失败！");
		}
		dispatcher.forward(request, response);
	}

}
