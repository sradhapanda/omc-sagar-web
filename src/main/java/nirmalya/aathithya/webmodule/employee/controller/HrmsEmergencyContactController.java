package nirmalya.aathithya.webmodule.employee.controller;

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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmergencyContactModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmergencyContactController {
	Logger logger = LoggerFactory.getLogger(HrmsEmergencyContactController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add emergency view page
	 */
	@GetMapping("/add-emergency-contact")
	public String addemergencyMaster(Model model, HttpSession session) {

		logger.info("Method : addemergencyMaster starts");

		HrmsEmergencyContactModel emergency = new HrmsEmergencyContactModel();
		HrmsEmergencyContactModel sessionemergency = (HrmsEmergencyContactModel) session
				.getAttribute("sessionemergency");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionemergency != null) {
			model.addAttribute("emergency", sessionemergency);
			session.setAttribute("sessionemergency", null);
		} else {
			model.addAttribute("emergency", emergency);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : addemergencyMaster ends");

		return "employee/add-emergency-contact";
	}

	/*
	 * Post Mapping for adding new emergency
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-emergency-contact")
	public String addemergencyMasterPost(@ModelAttribute HrmsEmergencyContactModel emergency, Model model,
			HttpSession session) {

		logger.info("Method : addemergencyMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		emergency.setCreatedBy(userId);
		emergency.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddemergencys", emergency, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionemergency", emergency);
			return "redirect:/employee/add-emergency-contact";
		}
		logger.info("Method : addemergencyMasterPost ends");

		return "redirect:/employee/view-emergency-contact";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-emergency-contact")
	public String viewemergencyMaster(Model model, HttpSession session) {

		logger.info("Method : viewemergencyMaster starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewemergencyMaster ends");

		return "employee/view-emergency-contact";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-emergency-contact-ThroughAjax")
	public @ResponseBody DataTableResponse viewemergencyMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewemergencyMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmergencyContactModel>> jsonResponse = new JsonResponse<List<HrmsEmergencyContactModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getemergencyDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmergencyContactModel> emergency = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmergencyContactModel>>() {
					});

			String s = "";

			for (HrmsEmergencyContactModel m : emergency) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmpId().getBytes());
				
				s = s + "<a href='view-emergency-contact-edit?empId=" + new String(encodeId) 
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick='deleteEmgry(\"" + new String(encodeId) + 
						 "\")'><i class='fa fa-trash'></i></a> &nbsp; &nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(emergency);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewemergencyMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit emergency
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-emergency-contact-edit")
	public String editemergency(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editemergency starts");

		HrmsEmergencyContactModel emergency = new HrmsEmergencyContactModel();
		JsonResponse<HrmsEmergencyContactModel> jsonResponse = new JsonResponse<HrmsEmergencyContactModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getemergencyById?empId=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		emergency = mapper.convertValue(jsonResponse.getBody(), HrmsEmergencyContactModel.class);
		session.setAttribute("message", "");

		emergency.setEditId("for Edit");
		
		model.addAttribute("emergency", emergency);
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		logger.info("Method : editemergency ends");

		return "employee/add-emergency-contact";
	}

	/*
	 * For Delete emergency
	 */

	@SuppressWarnings("unchecked")

	@GetMapping("/view-emergency-contact-delete")
	public @ResponseBody JsonResponse<Object> getemergencyForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getemergencyForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deleteemergencyById?id=" + id1 
					+ "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteemergency  ends");

		return resp;
	}

	/*
	 * For Modal emergency View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-emergency-contact-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalemergency starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getemergencyById?empId=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalemergency ends");
		return res;
	}

}

