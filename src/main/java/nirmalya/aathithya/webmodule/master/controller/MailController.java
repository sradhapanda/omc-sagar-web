package nirmalya.aathithya.webmodule.master.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.FolderMasterModel;
import nirmalya.aathithya.webmodule.master.model.MailModel;

@Controller
@RequestMapping(value = "master")

public class MailController {
	Logger logger = LoggerFactory.getLogger(MailModel.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@GetMapping("/mail")
	public String sendMail(Model model, HttpSession session) {

		logger.info("Method : sendMail starts");

		MailModel mail = new MailModel();
		
		try {
			MailModel regionSession = (MailModel) session.getAttribute("sfolder");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (regionSession != null) {
				model.addAttribute("mail", regionSession);
				session.setAttribute("sfolder", null);
			} else {
				model.addAttribute("mail", mail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : sendMail ends");
		return "master/mail";
	}
	@SuppressWarnings("unchecked")
	@PostMapping("mail")
	public String addMailStatus(@ModelAttribute MailModel mail, Model model, HttpSession session) {

		logger.info("Method : addMailStatus starts");
		//System.out.println("Deepak"+mail);
		Boolean status=mail.getMailStatus();
		if(status== true)
			session.setAttribute("MAILSTATUS", "1");
		else
			session.setAttribute("MAILSTATUS", "0");
		/*
		 * String userId = ""; try { userId = (String) session.getAttribute("USER_ID");
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			//folder.setCreatedBy(userId);
			resp = restClient.getForObject(env.getMasterUrl() + "restMailStatus?id="+status, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sFolder", mail);
			return "redirect:/master/mail";
		}

		logger.info("Method : addMailStatus ends");
		return "redirect:/master/mail";
	}
}
