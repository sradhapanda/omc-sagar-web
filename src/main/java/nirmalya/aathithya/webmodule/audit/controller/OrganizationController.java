package nirmalya.aathithya.webmodule.audit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import nirmalya.aathithya.webmodule.audit.model.OrganizationModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.constant.Constant;

@Controller
@RequestMapping(value = "audit")
public class OrganizationController {

	Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	

	/*
	 * Get Mapping for add Organization
	 */

	@GetMapping("/add-organization-details")
	public String addOrganization(Model model, HttpSession session) {

		logger.info("Method : addOrganization starts");

		OrganizationModel org = new OrganizationModel();
		OrganizationModel sessionorg = (OrganizationModel) session.getAttribute("sessionorg");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionorg != null) {
			model.addAttribute("org", sessionorg);
			session.setAttribute("sessionorg", null);
		} else {
			model.addAttribute("org", org);
		}
		/*
		 * dropDown value for Country Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-countryName",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addOrganization ends");
		return "audit/add-organization-details";
	}

	/*
	 * dropDown value for District Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-organization-details-districtName-ajax" })
	public @ResponseBody JsonResponse<Object> getDistrictName(Model model, @RequestBody String tOrgState,
			BindingResult result) {
		logger.info("Method : getdistrictNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "rest-get-districtName?id=" + tOrgState, JsonResponse.class);
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
	@PostMapping(value = { "add-organization-details-stateName-ajax" })
	public @ResponseBody JsonResponse<Object> getStateName(Model model, @RequestBody String tOrgCountry,
			BindingResult result) {
		logger.info("Method : getStateNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "rest-get-stateName?id=" + tOrgCountry, JsonResponse.class);
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
	 * Post Mapping for adding new Organization
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-organization-details")
	public String addOrgPost(@ModelAttribute OrganizationModel org, Model model, HttpSession session) {

		logger.info("Method : addOrgPost starts");

		String userId = "";
		// String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			// companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		org.settCreatedBy(userId);
		// concession.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getAuditUrl() + "restAddOrganization", org, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionorg", org);
			return "redirect:/audit/add-organization-details";
		}
		logger.info("Method : addCampPost ends");

		return "redirect:/audit/view-organization-details";
	}

	// **************************
	/*
	 * Get Mapping Organization
	 */
	@GetMapping("/view-organization-details")
	public String viewOrganization(Model model, HttpSession session) {

		logger.info("Method : viewOrganization starts");
		
		/*try {
			List<String> recipients = new ArrayList<String>();
			recipients.add("suprava.swain@nirmalyalabs.com");
			List<String> recipients1 = new ArrayList<String>();
			EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, Constant.mailFrom,
					Constant.password, recipients,null, "2345678", "sdrcftvgyhu", null);
			System.out.println("Email sent.");
		} catch (Exception ex) {
			System.out.println("Could not send email.");
			ex.printStackTrace();
		}*/
		logger.info("Method : viewOrganization ends");

		return "audit/view-organization-details";
	}

	/*
	 * For view Organization for dataTable AjaxCall
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewOrganizationajax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewOrganizationajax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			JsonResponse<List<OrganizationModel>> jsonResponse = new JsonResponse<List<OrganizationModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getOrgDetails", tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<OrganizationModel> org = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<OrganizationModel>>() {
					});

			String s = "";

			for (OrganizationModel m : org) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.gettOrg().getBytes());
				s = s + "<a href='view-organization-details-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteOrganization(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View' href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
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

		logger.info("Method : viewOrganizationajax Theme ends");

		return response;
	}
	/**
	 * Edit Selected Details
	 * 
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-details-edit")
	public String editdetails(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editdetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<OrganizationModel> res = new JsonResponse<OrganizationModel>();
		OrganizationModel guestMaster = new OrganizationModel();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "edit-org-details?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		guestMaster = mapper.convertValue(res.getBody(), OrganizationModel.class);
		session.setAttribute("message", "");
		model.addAttribute("org", guestMaster);

		/*
		 * dropDown value for Country Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "rest-get-countryName",
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
					env.getAuditUrl() + "get-state-name?id=" + guestMaster.gettOrgCountry(),
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
					env.getAuditUrl() + "rest-get-distName?id=" + guestMaster.gettOrgState(),
					DropDownModel[].class);
			List<DropDownModel> districtList = Arrays.asList(dropDownModel);
			model.addAttribute("districtList", districtList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}


		logger.info("Method : editdetails ends");
		return "audit/add-organization-details";
	}

	/*
	 * For Delete Organization
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-organization-details-delete")
	public @ResponseBody JsonResponse<Object> deleteOrganization(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteOrganization ends");

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
			resp = restClient.getForObject(env.getAuditUrl() + "deleteOrgById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteOrganization  ends");

		return resp;
	}

	/*
	 * For Modal Organization View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-organization-details-modalView" })
	public @ResponseBody JsonResponse<Object> modalOrganization(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalOrganization starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getOrgById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalOrganization ends");
		//System.out.println("@@@@@@@@@@@@@@@" + res);
		return res;
	}

}
