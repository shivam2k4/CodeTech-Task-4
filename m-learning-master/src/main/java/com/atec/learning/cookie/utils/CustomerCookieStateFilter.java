package com.atec.learning.cookie.utils;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.broadleafcommerce.common.security.util.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * 
 * @author mahbouba
 *
 */


@Component("rdrCustomerCookieStateFilter")
public class CustomerCookieStateFilter extends OncePerRequestFilter implements
		Ordered {

	@Value("${cookie.Filter.Order}")
	protected static int order;

	@Resource(name = "rdrCustomerCookieStateRequest")
	protected CustomerCookieStateRequestProcessor cookiefiltre;

	@Resource(name = "blCookieUtils")
	protected CookieUtils cookieUtils;

	public void doFilterInternal(HttpServletRequest baseRequest,
			HttpServletResponse baseResponse, FilterChain chain)
			throws IOException, ServletException {
		ServletWebRequest request = new ServletWebRequest(baseRequest,
				baseResponse);

		try {

			cookiefiltre.process(request);

			chain.doFilter(baseRequest, baseResponse);
		} finally {
			// Cookiefiltre.postProcess(request) ;
		}

	}

	public int getOrder() {
		// FilterChainOrder has been dropped from Spring Security 3
		// return FilterChainOrder.REMEMBER_ME_FILTER+1;
		return order;
	}

}
