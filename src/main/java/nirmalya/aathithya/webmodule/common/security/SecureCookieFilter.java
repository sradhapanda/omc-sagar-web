//package nirmalya.aathithya.webmodule.common.security;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class SecureCookieFilter implements Filter{
//
//	@Autowired
//	HttpSession session;
//	
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, 
//	        FilterChain chain) throws IOException, ServletException {
//	    
//	    // If there is no security context, we obviously don't need to do anything
//
//	    if (SecurityContextHolder.getContext() == null) {
//	        chain.doFilter(request, response);
//	    }
//	    
//	    // This represents the value we expect the HTTPS cookie to be set to
//
//	    String activeIdSessionValue = (String) session.getAttribute("SESSION_ID");
//	    
//	    if (StringUtils.isNotBlank(activeIdSessionValue) && request.isSecure()) {
//	        // The request is secure and and we've previously set a session 
//
//	        // fixation protection cookie
//
//
//	        String cookieValue = SessionFixationProtectionCookie.readActiveID(request);
//	        String decryptedActiveIdValue = encryptionModule.decrypt(cookieValue);
//	        
//	        if (!activeIdSessionValue.equals(decryptedActiveIdValue)) {
//	            abortUser(request, response); // Invalidate the session and clear cookies
//
//	        }
//	    } else if (request.isSecure()) {
//	        // The request is secure, but we haven't set a protection cookie yet
//
//
//	        String token;
//	        try {
//	            token = RandomGenerator.generateRandomId("SHA1PRNG", 32);
//	        } catch (NoSuchAlgorithmException e) {
//
//	            throw new ServletException(e);
//	        }
//	        
//	        // Note that since our value is completely random, this encryption is not
//
//	        // necessary. However, if you were to use user-identifying data as your 
//
//	        // secondary cookie value, you would definitely want to encrypt it.
//
//	        String encryptedAID = encryptionModule.encrypt(token);
//	        
//	        // Save the value into the session
//
//	        session.setAttribute(SESSION_ATTR, token);
//
//	        // Save the HTTPS cookie for the user
//
//	        SessionFixationProtectionCookie.writeActiveID(request, response, encryptedAID);
//	    }
//	            
//	    chain.doFilter(request, response);
//	}
//
//}
