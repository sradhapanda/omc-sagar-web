package nirmalya.aathithya.webmodule;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.twilio.Twilio;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

@SpringBootApplication
@EnableScheduling
public class WebModuleApplication implements ApplicationRunner  {
	
	 private final static String ACCOUNT_SID = "AC69d74aa9cc068ee53516b9eea3c8da82";
	 private final static String AUTH_ID = "cfbbd0907235c613a0196738f33a5a07";
	 
	   static {
	      Twilio.init(ACCOUNT_SID, AUTH_ID);
	   }
	  
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   return builder.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public EnvironmentVaribles environmentVaribles() {
		EnvironmentVaribles env = new EnvironmentVaribles();
		return env; 
	}
	
	@Bean
	public EmailAttachmentSender emailAttachmentSender() {
		EmailAttachmentSender email = new EmailAttachmentSender();
		return email; 
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WebModuleApplication.class, args);
	}	

	 @Override
	   public void run(ApplicationArguments arg0) throws Exception {
//	      Message.creator(new PhoneNumber("919776120300"), new PhoneNumber("12013667134"),
//	         "Message from Spring Boot Application").create();
//	      Call.creator(new PhoneNumber("<919776120300>"), new PhoneNumber("<12013667134>"),
//	 	         new URI("http://demo.twilio.com/docs/voice.xml")).create();
	   }
	

}
