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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeSkillAssignModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeSkillAssignController {

	Logger logger = LoggerFactory.getLogger(HrmsEmployeeSkillAssignController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add assignSkill view page
	 */
	@GetMapping("/add-employee-skill")
	public String addassignSkillMaster(Model model, HttpSession session) {

		logger.info("Method : addassignSkillMaster starts");

		HrmsEmployeeSkillAssignModel assignSkill = new HrmsEmployeeSkillAssignModel();
		HrmsEmployeeSkillAssignModel sessionassignSkill = (HrmsEmployeeSkillAssignModel) session
				.getAttribute("sessionassignSkill");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionassignSkill != null) {
			model.addAttribute("assignSkill", sessionassignSkill);
			session.setAttribute("sessionassignSkill", null);
		} else {
			model.addAttribute("assignSkill", assignSkill);
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
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Skill = restClient.getForObject(env.getEmployeeUrl() + "getSkillList", DropDownModel[].class);
			List<DropDownModel> SkillList = Arrays.asList(Skill);
			model.addAttribute("SkillList", SkillList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addassignSkillMaster ends");

		return "employee/add-employee-skill-assign";
	}

	/*
	 * Post Mapping for adding new assignSkill
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-employee-skill")
	public String addassignSkillMasterPost(@ModelAttribute HrmsEmployeeSkillAssignModel assignSkill, Model model,
			HttpSession session) {

		logger.info("Method : addassignSkillMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s = "";

		for (String m : assignSkill.getSkillList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			assignSkill.setSkillId(s);
		}
		assignSkill.setCreatedby(userId);
		assignSkill.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddassignSkills", assignSkill, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionassignSkill", assignSkill);
			return "redirect:/employee/add-employee-skill";
		}
		logger.info("Method : addassignSkillMasterPost ends");

		return "redirect:/employee/view-employee-skill";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-employee-skill")
	public String viewassignSkillMaster(Model model, HttpSession session) {

		logger.info("Method : viewassignSkillMaster starts");

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
		logger.info("Method : viewassignSkillMaster ends");

		return "employee/view-employee-skill-assign";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-skill-ThroughAjax")
	public @ResponseBody DataTableResponse viewassignSkillMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewassignSkillMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmployeeSkillAssignModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeSkillAssignModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignSkillDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeSkillAssignModel> assignSkill = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeSkillAssignModel>>() {
					});

			String s = "";

			for (HrmsEmployeeSkillAssignModel m : assignSkill) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmployeeId().getBytes());
				byte[] encodeId1 = Base64.getEncoder().encode(m.getSkillId().getBytes());
				s = s + "<a href='view-employee-skill-edit?empId=" + new String(encodeId) + "&skillId="
						+ new String(encodeId1)
						+ "' ><i class=\"fa fa-edit\" ></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignSkill);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewassignSkillMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit assignSkill
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-skill-edit")
	public String editassignSkill(Model model, @RequestParam("empId") String encodeId,
			@RequestParam("skillId") String encodeId1, HttpSession session) {

		logger.info("Method :editassignSkill starts");

		HrmsEmployeeSkillAssignModel assignSkill = new HrmsEmployeeSkillAssignModel();
		JsonResponse<HrmsEmployeeSkillAssignModel> jsonResponse = new JsonResponse<HrmsEmployeeSkillAssignModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getAssignSkillById?empId=" + id,
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

		assignSkill = mapper.convertValue(jsonResponse.getBody(), HrmsEmployeeSkillAssignModel.class);
		session.setAttribute("message", "");

		assignSkill.setEditId("for Edit");
		String[] acList = assignSkill.getSkillId().split(",");
		assignSkill.setSkillList(Arrays.asList(acList));
		model.addAttribute("assignSkill", assignSkill);
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
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Skill = restClient.getForObject(env.getEmployeeUrl() + "getSkillList", DropDownModel[].class);
			List<DropDownModel> SkillList = Arrays.asList(Skill);
			model.addAttribute("SkillList", SkillList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editassignSkill ends");

		return "employee/add-employee-skill-assign";
	}

	/*
	 * For Modal assignSkill View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-skill-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalassignSkill starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getAssignSkillById?empId=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalassignSkill ends");
		return res;
	}

}
