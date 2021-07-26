package nirmalya.aathithya.webmodule.audit.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import nirmalya.aathithya.webmodule.audit.model.AddAuditPartyNewMoldel;
import nirmalya.aathithya.webmodule.audit.model.AddAuditPartyNewMoldel;
import nirmalya.aathithya.webmodule.audit.model.AddAuditPartyNewMoldel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping(value = "employee")
public class AddAduitPartyNewController {

	@Autowired
	RestTemplate restClient;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	EnvironmentVaribles env;
	/*
	 * Get Mapping for add Organization
	 */
	Logger logger = LoggerFactory.getLogger(AddAduitPartyNewController.class);

	@GetMapping("/add-employee-AddAuditPartyNew")
	public String addEmployee1(Model model, HttpSession session) {
		logger.info("Method : addAuditParty starts");

		AddAuditPartyNewMoldel orgAuditor = new AddAuditPartyNewMoldel();
		AddAuditPartyNewMoldel sessionorgauditor = (AddAuditPartyNewMoldel) session.getAttribute("sessionorgauditor");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionorgauditor != null) {
			model.addAttribute("orgAuditor", sessionorgauditor);
			session.setAttribute("sessionorgauditor", null);
		} else {
			model.addAttribute("orgAuditor", orgAuditor);
		}
		/*
		 * dropDown value for Country Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-countryName-New",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * 
		 * dropDown value for Organization Name
		 * 
		 */
		/*
		 * try { DropDownModel[] dropDownModel =
		 * restClient.getForObject(env.getEmployeeUrl() +
		 * "rest-get-organizationName-New", DropDownModel[].class); List<DropDownModel>
		 * orgList = Arrays.asList(dropDownModel); model.addAttribute("orgList",
		 * orgList); } catch (RestClientException e) { e.printStackTrace(); }
		 */

		/*
		 * Drop down for Region Name
		 */
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getEmployeeUrl() + "get-region-list-Name-New",
					DropDownModel[].class);

			List<DropDownModel> regionList = Arrays.asList(region);
			model.addAttribute("regionList", regionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addAuditParty ends");
		return "audit/AddAuditPartyNew";
	}

	/*
	 * dropDown value for District Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-employee-AddAuditPartyNew-districtName-ajax" })
	public @ResponseBody JsonResponse<Object> getDistrictName(Model model, @RequestBody String tAduitNewState,
			BindingResult result) {
		logger.info("Method : getdistrictNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-districtName-New?id=" + tAduitNewState,
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
		logger.info("Method : getdistrictNameAJAX ends");
		return res;

	}
	/*
	 * dropDown value for States Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-employee-AddAuditPartyNew-stateName-ajax" })
	public @ResponseBody JsonResponse<Object> getStateName(Model model, @RequestBody String tAduitNewCountry,
			BindingResult result) {
		logger.info("Method : getStateNameAJAX starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-stateName-New?id=" + tAduitNewCountry,
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
		System.out.println(res);
		logger.info("Method : getStateNameAJAX ends");
		return res;

	}

	/*
	 * Post Mapping for adding new Organization
	 */

	@SuppressWarnings("unchecked")

	@PostMapping("/add-employee-AddAuditPartyNew1")
	public String addOrgPost(@ModelAttribute AddAuditPartyNewMoldel orgAuditor, Model model, HttpSession session) {

		logger.info("Method : addOrgPost starts");

		System.out.println("rest connected");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddRegion", orgAuditor, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionorgauditor", orgAuditor);
			return "redirect:/employee/add-employee-AddAuditPartyNew";
		}
		logger.info("Method : addOrgPost ends");

		System.out.println("rest connected");
		return "redirect:/employee/add-employee-AddAuditPartyNew";
	}
	/*
	 * View AuditParty
	 * 
	 */

	@SuppressWarnings("unchecked")

	@GetMapping("/add-employee-view-auditparty-master")
	public String viewEmployee(Model model, HttpSession session) {

		logger.info("Method : AddAuditPartyNew starts");

		logger.info("Method : AddAuditPartyNew ends");

		return "audit/view-AddAuditPartyNew";
	}

	// VIEW THROUGH AJAX
	@SuppressWarnings("unchecked")
	@GetMapping("/add-employee-view-auditparty-master-through-ajax")
	public @ResponseBody DataTableResponse viewAuditPartyThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAuditPartyThroughAjax starts");
		System.out.println(param1);
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			System.out.println(param1);
			JsonResponse<List<AddAuditPartyNewMoldel>> jsonResponse = new JsonResponse<List<AddAuditPartyNewMoldel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAuditParty-New", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AddAuditPartyNewMoldel> audit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AddAuditPartyNewMoldel>>() {
					});

			
			for (AddAuditPartyNewMoldel m : audit) {
				String s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.gettAduitNewId().getBytes());
				s = s + "<a href='add-employee-view-auditparty-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteOrganizationAuditors(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId) + "\")'><i class='fa fa-search search'></i></a>";
				m.setOption(s);
				
				/*
				 * if (m.gettStatus()) { m.setStatusName("Active"); } else {
				 * m.setStatusName("Inactive"); }
				 */
			}
System.out.println(audit);
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(audit);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewAuditPartyThroughAjax ends");
		System.out.println(response);
		return response;
	}

	/**
	 * Edit
	 * 
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-employee-view-auditparty-master-edit")
	public String editdetails(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editdetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<AddAuditPartyNewMoldel> res = new JsonResponse<AddAuditPartyNewMoldel>();
		AddAuditPartyNewMoldel orgAuditor = new AddAuditPartyNewMoldel();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "edit-org-auditor-details-New?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		System.out.println(res);
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		orgAuditor = mapper.convertValue(res.getBody(), AddAuditPartyNewMoldel.class);
		session.setAttribute("message", "");
		// System.out.println("orgAuditor: "+orgAuditor);
		model.addAttribute("orgAuditor", orgAuditor);

		/*
		 * dropDown value for Country Name
		 */

		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-countryName-New",
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
					env.getEmployeeUrl() + "get-st-name-New?id=" + orgAuditor.gettAduitNewCountry(), DropDownModel[].class);
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
					env.getEmployeeUrl() + "rest-get-disName-New?id=" + orgAuditor.gettAduitNewState(), DropDownModel[].class);
			List<DropDownModel> districtList = Arrays.asList(dropDownModel);
			model.addAttribute("districtList", districtList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * Drop down for Region Name for edit
		 */
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getEmployeeUrl() + "get-region-list-Name-New",
					DropDownModel[].class);

			List<DropDownModel> regionList = Arrays.asList(region);
			model.addAttribute("regionList", regionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * dropDown for Organization Name
		 * 
		 * 
		 * try { DropDownModel[] dropDownModel =
		 * restClient.getForObject(env.getEmployeeUrl() + "rest-get-organizationName",
		 * DropDownModel[].class); List<DropDownModel> orgList =
		 * Arrays.asList(dropDownModel); model.addAttribute("orgList", orgList); } catch
		 * (RestClientException e) { e.printStackTrace(); }
		 */

		/*
		 * dropDown value for User Type
		 * 
		 * 
		 * try { DropDownModel[] dropDownModel =
		 * restClient.getForObject(env.getEmployeeUrl() + "rest-get-userType",
		 * DropDownModel[].class); List<DropDownModel> UserType =
		 * Arrays.asList(dropDownModel); model.addAttribute("UserType", UserType); }
		 * catch (RestClientException e) { e.printStackTrace(); }
		 */

		logger.info("Method : editdetails ends");
		return "audit/AddAuditPartyNew";
	}
	/*
	 * For Modal Organization Auditors View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-organization-auditor-modalView" })
	public @ResponseBody JsonResponse<Object> modalRegionAuditors(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalRegionAuditors starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getOrgAuditorsById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalRegionAuditors ends");
		//System.out.println("@@@@@@@@@@@@@@@" + res);
		return res;
	}

}
