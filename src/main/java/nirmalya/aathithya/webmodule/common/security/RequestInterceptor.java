/**
 *  Intercepts request for access validation
 */
package nirmalya.aathithya.webmodule.common.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Nirmalya Labs
 *
 */
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		HttpSession session = request.getSession();
		Integer PORT = (Integer)session.getAttribute("SESSION_PORT");
		
//		System.out.println("host: "+ request.getHeader("host"));
//		System.out.println("remote port: "+ request.getRemotePort()+"\t port : "+PORT);
//		System.out.println("dnt: " +request.getHeader("dnt"));
//		System.out.println("cookie: " +request.getHeader("cookie"));
//		System.out.println("connection: " +request.getHeader("connection"));
//		System.out.println("referer: " +request.getHeader("referer"));
//		System.out.println("accept-encoding: " +request.getHeader("accept-encoding"));
//		System.out.println("accept-language: " +request.getHeader("accept-language"));
//		System.out.println("user-agent: " +request.getHeader("user-agent"));
//		System.out.println("sec-fetch-user: " +request.getHeader("sec-fetch-user"));
//		System.out.println("sec-fetch-site: " +request.getHeader("sec-fetch-site"));
//		System.out.println("sec-fetch-mode: " +request.getHeader("sec-fetch-mode"));
//		System.out.println("upgrade-insecure-requests: "+request.getHeader("upgrade-insecure-requests"));
		
		try {
			String userId = (String)session.getAttribute("USER_ID");
			
			if(userId != null && userId != "" ) {
				
				List<String> uList = (List<String>)session.getAttribute("URL_LIST");
				
				String uri = request.getRequestURI();

				List<String> check = uList.stream().filter(s -> uri.contains(s)).collect(Collectors.toList());
				if(check.size() == 0 ) {
					response.sendRedirect("/access-denied");				
				}
				
			}
			
//			if(userId != null && userId != "" && request.getMethod().equalsIgnoreCase("POST")) {
//				
//				List<String> uList = (List<String>)session.getAttribute("URL_LIST");
//				
//				String uri = request.getRequestURI();
//				System.out.println("uri : "+uri);
//				List<String> check = uList.stream().filter(s -> uri.contains(s)).collect(Collectors.toList());
//
//				
//				if(check.size() == 0 ) {
//					response.sendRedirect("/access-denied");				
//				}
//				
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if returned false, we need to make sure 'response' is sent
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {


		
		
		//we can add attributes in the modelAndView and use that in the view page
	}
	
}
