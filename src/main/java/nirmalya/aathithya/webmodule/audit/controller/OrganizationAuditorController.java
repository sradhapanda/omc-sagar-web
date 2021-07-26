package nirmalya.aathithya.webmodule.audit.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nirmalya.aathithya.webmodule.audit.model.OrganizationAuditorModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.constant.Constant;

@Controller
@RequestMapping(value = "audit")
public class OrganizationAuditorController {
	
	Logger logger = LoggerFactory.getLogger(OrganizationAuditorController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	MailService mailService;

	@Autowired 
	ApplicationArguments applicationArguments;
	
	@SuppressWarnings({"unused" })
	@GetMapping(value = { "add-organization-auditor-send-mail" })
	public void verifyotp(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
		logger.info("Method : verifyotp postmapping starts");
		Authentication auth = null;
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

	
		/*String name = "";
		String email = "";
		String mobile = "";
		String password = "";
		String p = password;

		name = (String) session.getAttribute("USER_NAME");
		email = "supravaswn@gmail.com";
		mobile = (String) session.getAttribute("USER_MOBILE");
		//.out.println(name);
		//System.out.println(email);
		try {
			mailService.sendEmail(email, "From Stayin ", "Dear  You are  successfully registered");
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		
		/* Message.creator(new PhoneNumber("919776120300"), new PhoneNumber("12013667134"),
		         "Message from OMC Application").create();	
		 Call.creator(new PhoneNumber("<919776120300>"), new PhoneNumber("<12013667134>"),
	 	         new URI("http://demo.twilio.com/docs/voice.xml")).create();*/
		logger.info("Method : verifyotp postmapping ends");
	}
	

	/*
	 * Get Mapping for add Organization Auditor
	 */

	@GetMapping("/add-organization-auditor")
	public String addOrganizationAuditor(Model model, HttpSession session) {

		logger.info("Method : addOrganizationAuditor starts");

		OrganizationAuditorModel orgAuditor = new OrganizationAuditorModel();
		OrganizationAuditorModel sessionorgauditor = (OrganizationAuditorModel) session.getAttribute("sessionorgauditor");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionorgauditor != null) {
			model.addAttribute("orgAuditor", sessionorgauditor);
			session.setAttribute("sessionorg", null);
		} else {
			model.addAttribute("orgAuditor", orgAuditor);
		}
		
		/*
		 * dropDown value for Country Name
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-countyName",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropDown value for Organization Name
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-organizationName",
					DropDownModel[].class);
			List<DropDownModel> orgList = Arrays.asList(dropDownModel);
			model.addAttribute("orgList", orgList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropDown value for User Type
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-userType",
					DropDownModel[].class);
			List<DropDownModel> UserType = Arrays.asList(dropDownModel);
			model.addAttribute("UserType", UserType);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addOrganizationAuditor ends");
		return "audit/add-organization-auditor";
	}
	
	/*
	 * dropDown value for District through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-organization-auditor-districtName-ajax" })
	public @ResponseBody JsonResponse<Object> getDistrictName(Model model, @RequestBody String tOrgState,
			BindingResult result) {
		logger.info("Method : getdistrictNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "rest-get-distrName?id=" + tOrgState, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getdistrictNameAJAX ends");
		return res;

	}

	/*
	 * dropDown value for District Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-organization-auditor-stateName-ajax" })
	public @ResponseBody JsonResponse<Object> getStateName(Model model, @RequestBody String tOrgCountry,
			BindingResult result) {
		logger.info("Method : getStateNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "rest-get-stName?id=" + tOrgCountry, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getStateNameAJAX ends");
		return res;

	}

	/*
	 * Post Mapping for adding new Organization Auditors
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-organization-auditor")
	public String addOrgAuditorsPost(@ModelAttribute OrganizationAuditorModel orgAuditor, Model model, HttpSession session) {

		logger.info("Method : addOrgAuditorsPost starts");

		String userId = "";
		// String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			// companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		orgAuditor.settCreatedBy(userId);
		String enc = orgAuditor.gettPassword();
		if (enc != null && enc != "") {
			enc = passwordEncoder.encode(enc);
			orgAuditor.settPassword(enc);;
		}
		orgAuditor.settAuthNo("Auth001");
		orgAuditor.settIMEI("IMEI001");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getAuditUrl() + "restAddOrganizationAuditor", orgAuditor, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionorgAuditor", orgAuditor);
			return "redirect:/audit/add-organization-auditor";
		}
		logger.info("Method : addOrgAuditorsPost ends");

		return "redirect:/audit/view-organization-auditor";
	}

	// **************************
	/*
	 * Get Mapping Organization
	 */
	@GetMapping("/view-organization-auditor")
	public String viewOrganizationAuditors(Model model, HttpSession session) {

		logger.info("Method : viewOrganizationAuditors starts");

		logger.info("Method : viewOrganizationAuditors ends");

		return "audit/view-organization-auditor";
	}

	/*
	 * For view Organization for dataTable AjaxCall
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-auditor-ThroughAjax")
	public @ResponseBody DataTableResponse viewOrganizationAuditorsajax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewOrganizationAuditorsajax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<OrganizationAuditorModel>> jsonResponse = new JsonResponse<List<OrganizationAuditorModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getOrgAuditorsDetails", tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<OrganizationAuditorModel> org = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<OrganizationAuditorModel>>() {
					});

			String s = "";

			for (OrganizationAuditorModel m : org) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.gettOrgId().getBytes());
				s = s + "<a href='view-organization-auditor-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteOrganizationAuditors(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.gettStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(org);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewOrganizationAuditorsajax Theme ends");
		return response;
	}
	/**
	 * Edit 
	 * 
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-auditor-edit")
	public String editdetails(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editdetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<OrganizationAuditorModel> res = new JsonResponse<OrganizationAuditorModel>();
		OrganizationAuditorModel orgAuditor = new OrganizationAuditorModel();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "edit-org-auditor-details?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		orgAuditor = mapper.convertValue(res.getBody(), OrganizationAuditorModel.class);
		session.setAttribute("message", "");
		//System.out.println("orgAuditor: "+orgAuditor);
		model.addAttribute("orgAuditor", orgAuditor);

		/*
		 * dropDown value for Country Name
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-countyName",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for State Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(
					env.getAuditUrl() + "get-st-name?id=" + orgAuditor.gettOrgCountry(),
					DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
			model.addAttribute("stateList", stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for District Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(
					env.getAuditUrl() + "rest-get-disName?id=" + orgAuditor.gettOrgState(),
					DropDownModel[].class);
			List<DropDownModel> districtList = Arrays.asList(dropDownModel);
			model.addAttribute("districtList", districtList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown for Organization Name
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-organizationName",
					DropDownModel[].class);
			List<DropDownModel> orgList = Arrays.asList(dropDownModel);
			model.addAttribute("orgList", orgList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		/*
		 * dropDown value for User Type
		 */
		
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-userType",
					DropDownModel[].class);
			List<DropDownModel> UserType = Arrays.asList(dropDownModel);
			model.addAttribute("UserType", UserType);
		} catch (RestClientException e) {
			e.printStackTrace();
		}


		logger.info("Method : editdetails ends");
		return "audit/add-organization-auditor";
	}

	/*
	 * For Delete Organization
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-auditor-delete")
	public @ResponseBody JsonResponse<Object> deleteOrganizationAuditors(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteOrganizationAuditors ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(env.getAuditUrl() + "deleteOrgAuditorsById?id=" + id1 + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteOrganizationAuditors  ends");

		return resp;
	}

	/*
	 * For Modal Organization Auditors View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-organization-auditor-modalView" })
	public @ResponseBody JsonResponse<Object> modalOrganizationAuditors(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalOrganizationauditors starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getOrgAuditorsById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalOrganizationauditors ends");
		//System.out.println("@@@@@@@@@@@@@@@" + res);
		return res;
	}

}
