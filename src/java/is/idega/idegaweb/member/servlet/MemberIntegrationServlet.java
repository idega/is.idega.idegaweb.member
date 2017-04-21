package is.idega.idegaweb.member.servlet;

import java.io.IOException;
import java.net.URI;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idega.restful.servlet.DefaultRestfulServlet;

public class MemberIntegrationServlet extends DefaultRestfulServlet {

	private static final long serialVersionUID = -100199126638713752L;

	@Override
	public int service(URI baseUri, URI requestUri, HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		return super.service(baseUri, requestUri, request, response);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.service(request, response);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		super.doFilter(request, response, chain);
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		super.doFilter(request, response, chain);
	}

}