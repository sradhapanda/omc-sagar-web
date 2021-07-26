package nirmalya.aathithya.webmodule.user.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	RestTemplate restClient;
	
	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	HttpSession session; 

    @SuppressWarnings("unchecked")
	@Override
    public UserDetails loadUserByUsername(String username) {
       
		User user = null;
		JsonResponse<User> jsonResponse = new JsonResponse<User>();
		
		try {
			
			jsonResponse = restClient.getForObject(env.getUserUrl()+"getUserByUsername?username="+username, JsonResponse.class);
			
			ObjectMapper mapper = new ObjectMapper();
			user = mapper.convertValue(jsonResponse.getBody(), User.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		if(user == null){
			session.setAttribute("loginMessage", "Invalid username or password");
			throw new UsernameNotFoundException(username + " not found");
		} else {
			session.setAttribute("loginMessage", "Wrong Password");
		}
		
        return new CustomUserDetails(user);
    }
	
}
