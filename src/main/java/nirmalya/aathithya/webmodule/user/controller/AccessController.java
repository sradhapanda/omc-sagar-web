package nirmalya.aathithya.webmodule.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditNotificationModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.constant.Constant;
import nirmalya.aathithya.webmodule.user.model.User;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@Component
public class AccessController {

	Logger logger = LoggerFactory.getLogger(AccessController.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Function to check connection
	 *
	 */
	@GetMapping("welcome")
	public String welcome(Model model, HttpSession session) {
		logger.info("Method : welcome starts");

		logger.info("Method : welcome ends");
		return "welcome";
	}

	/**
	 * Function for home page
	 *
	 */
	@GetMapping("/")
	public String home(Model model) {
		logger.info("Method : / starts");

		logger.info("Method : / ends");
		// return "app_index";
		// return "atithya_home";
		return "omc_home";
	}

	/**
	 * Function to show register user form
	 *
	 */
	@GetMapping("register")
	public String addUser(Model model, HttpSession session) {
		logger.info("Method : register starts");

		User user = new User();

		User form = (User) session.getAttribute("suser");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			form.setUserPassword(null);
			model.addAttribute("user", form);
			session.setAttribute("suser", null);
		} else {
			model.addAttribute("user", user);
		}

		logger.info("Method : register ends");
		return "register";
	}

	/**
	 * Function show login form
	 *
	 */
	@GetMapping("/login")
	public String login(Model model, HttpSession session) {
		logger.info("Method : login starts");

		String message = (String) session.getAttribute("loginMessage");

		if (message != null && message != "") {
			model.addAttribute("loginMessage", message);
			session.setAttribute("loginMessage", null);
		}

		logger.info("Method : login ends");
		// return "app_index";
		// return "atithya_home";
		// return "nerp_home";
		return "omc_home";
	}

	/**
	 * Function show index page after login
	 *
	 */

	@GetMapping("/index")
	public String index(Model model, HttpSession session) {
		logger.info("Method : index starts");

		String dashboard = (String) session.getAttribute("DASHBOARD");

		logger.info("Method : index endss");
		return dashboard;
	}

	@GetMapping("access-denied")
	public String accessDenied(Model model, HttpSession session) {
		logger.info("Method : access-denied starts");

		logger.info("Method : access-denied ends");
		return "accessDenied";
	}

	/**
	 * Function to logout user
	 *
	 */
	@GetMapping("logout")
	public String logout(Model model, HttpSession session) {
		logger.info("Method : logout Starts");

		session.invalidate();

		logger.info("Method : logout ends");
		return "redirect:";
	}

	/**
	 * Function to post register user form
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("addUser")
	public String addUserForm(@ModelAttribute User user, Model model, HttpSession session) {
		logger.info("Method POST : addUser starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			String enc = user.getUserPassword();
			if (enc != null && enc != "") {
				enc = passwordEncoder.encode(enc);
				user.setUserPassword(enc);
			}

			jsonResponse = restTemplate.postForObject(env.getUserUrl() + "registerUser", user, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("suser", user);
			return "redirect:register";
		}

		logger.info("Method POST : addUser ends");
		return "redirect:login";
	}

	/******* BIKASH FOR FORGET PASSWORD EMAIL ID SEND *****************************/

	@SuppressWarnings({ "static-access", "unchecked" })
	@GetMapping(value = { "/forget" })
	public @ResponseBody JsonResponse<Object> getemail(Model model, @RequestParam String emailid) {
		logger.info("Method : getemail List starts");

		JsonResponse<Object> response = new JsonResponse<Object>();
		List<DropDownModel> resp = new ArrayList<DropDownModel>();

		try {
			resp = restTemplate.getForObject(env.getUserUrl() + "getemail?emailid=" + emailid, ArrayList.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		EmailAttachmentSender emailact = new EmailAttachmentSender();
		String port = Constant.port;
		String host = Constant.host;
		String password = Constant.password;

		ObjectMapper mapper = new ObjectMapper();
		// resp= mapper.convertValue(resp,);

		List<DropDownModel> res = mapper.convertValue(resp, new TypeReference<List<DropDownModel>>() {
		});

		List<String> toAddress = new ArrayList<String>();

		toAddress.add(res.get(0).getName());

		// toAddress.add("bikashmohapatra1997@gmail.com");

		String message = "Your Verification Code is and it valid for 10 minutes " + res.get(0).getKey();
		String subject = "OTP Verification";

		try {
			emailact.sendEmailWithAttachments(host, port, null, password, toAddress, null, subject, message, null);
			response.setMessage("success");

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method :getemail List starts  ends");
		return response;
	}

	/*
	 * **********FORGET PASSWORD*****************
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/forgetpass" })
	public @ResponseBody JsonResponse<Object> postChangePassword(Model model, @RequestBody DropDownModel obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : postForgetPassword starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		String empemail = "";
		empemail = obj.getKey();

		String empotp = "";
		empotp = obj.getName();

		String password = "";
		password = obj.getDocName();

		try {
			if (password != null && password != "") {
				password = passwordEncoder.encode(password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			res = restTemplate.getForObject(
					env.getUserUrl() + "forgetpassword?email=" + empemail + "&otp=" + empotp + "&password=" + password,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : postForgetPassword ends");
		return res;
	}

	@SuppressWarnings("unchecked")

	@GetMapping(value = { "/notification-details" })
	public @ResponseBody JsonResponse<List<AuditNotificationModel>> getNotification(@RequestParam String roleId) {

		logger.info("Method : getNotification starts");

		JsonResponse<List<AuditNotificationModel>> resp = new JsonResponse<List<AuditNotificationModel>>();

		resp = restTemplate.getForObject(env.getUserUrl() + "getNotification?roleId=" + roleId, JsonResponse.class);

		// System.out.println("ASJSJSNSJ KJSKSJSK "+resp.getBody().toString());

		logger.info("Method : getNotification ends");

		return resp;
	}

//	@GetMapping(value = { "/error" })
//	public String getErroe() {
//		logger.info("Method : index starts");
//
//		logger.info("Method : index ends");
//		return "error";
//	}

}
