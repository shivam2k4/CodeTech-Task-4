package com.atec.learning.cookie.utils;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.broadleafcommerce.common.security.util.CookieUtils;
import org.broadleafcommerce.common.web.AbstractBroadleafWebRequestProcessor;
import org.broadleafcommerce.common.web.BroadleafRequestContext;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;


/**
 * 
 * @author mahbouba
 *
 */


@Component("rdrCustomerCookieStateRequest")
public class CustomerCookieStateRequestProcessor extends AbstractBroadleafWebRequestProcessor implements ApplicationEventPublisherAware {

	@Resource(name = "blCookieUtils")
	protected CookieUtils cookieUtils;
	
	@Value("${cookie.Max.Age}")
	protected static String cookieMaxAge ;
	
	@Value("${cookie.Secure}")
	protected static String cookieSecure;
	
	@Value("${rayondart.cookies.name}")
	protected static String rayondartCookieName=".rayondart";
	
	
	public void process(WebRequest request) {
		HttpServletRequest req = BroadleafRequestContext.getBroadleafRequestContext().getRequest();
		Cookie[] cookies = req.getCookies();
		Customer customer = null;
		
		if(!ArrayUtils.isEmpty(cookies)){
			// get value 
			Cookie cookieValue = getCookieByName(cookies);
			//LOG.trace("cookie name = " +cookieValue);
			if(Objects.nonNull(cookieValue)){
				
			} else {
				    Map<String, Object> ruleMap =(Map<String, Object>) request.getAttribute("blRuleMap",WebRequest.SCOPE_REQUEST);
				    if (ruleMap != null) {
			        customer=(Customer)ruleMap.get("customer");
			        			        }
				    if(customer==null)
				    {
				    
				    }else
				    {
				    	String sessionvalue=customer.getId().toString();
				    	String encrypted1 = Md5Crypt.md5Crypt(sessionvalue.getBytes());
						
						Cookie newCookie = new Cookie(rayondartCookieName,encrypted1);
						
						newCookie.setPath("/");
						 //24 heures
						
						newCookie.setMaxAge(Integer.parseInt(cookieMaxAge));
						  
						newCookie.setSecure(BooleanUtils.toBoolean(cookieSecure));
						
						BroadleafRequestContext.getBroadleafRequestContext().getResponse().addCookie(newCookie);
				
			}
			}
			 
			
			
		} 
	
	}


	public void setApplicationEventPublisher(ApplicationEventPublisher arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public Cookie getCookieByName(Cookie[] cookies) {
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (rayondartCookieName.equals(cookie.getName()))
                    return (cookie);
            }
        }
        
        return null;

    }


	

}
