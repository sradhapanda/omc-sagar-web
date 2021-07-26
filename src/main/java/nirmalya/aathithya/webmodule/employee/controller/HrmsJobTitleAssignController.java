package nirmalya.aathithya.webmodule.employee.controller;

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

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmsJobTitleAssignModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsJobTitleAssignModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsJobTitleAssignModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsJobTitleAssignController {
	Logger logger = LoggerFactory.getLogger(HrmsJobTitleAssignController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add employee job title assign
	 */
	@GetMapping("/add-department-jobTitle-assign")
	public String addJobTitleAssign(Model model, HttpSession session) {

		logger.info("Method : addJobTitleAssign  starts");

		HrmsJobTitleAssignModel jobTitle = new HrmsJobTitleAssignModel();
		HrmsJobTitleAssignModel sessionjobTitle = (HrmsJobTitleAssignModel) session.getAttribute("sessionjobTitle");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionjobTitle != null) {
			model.addAttribute("jobTitle", sessionjobTitle);
			session.setAttribute("sessionjobTitle", null);
		} else {
			model.addAttribute("jobTitle", jobTitle);
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-department",
					DropDownModel[].class);
			List<DropDownModel> deptList = Arrays.asList(dropDownModel);
			model.addAttribute("deptList", deptList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-jobTitleList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(dropDownModel);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addJobTitleAssign  ends");

		return "employee/add-jobTitle-assign";
	}

	/*
	 * Post Mapping for adding Department Job Title Assign
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-department-jobTitle-assign")
	public String addJobTitleAssigned(@ModelAttribute HrmsJobTitleAssignModel jobTitle, Model model,
			HttpSession session) {

		logger.info("Method : addJobTitleAssigned starts");

		JsonResponse<HrmsJobTitleAssignModel> jsonResponse = new JsonResponse<HrmsJobTitleAssignModel>();

		String s = "";

		for (String m : jobTitle.getRoleList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			jobTitle.settJobTitle(s);
			;
		}

		String empId = "";
		try {
			empId = (String) session.getAttribute("USER_ID");
			jobTitle.setCreatedBy(empId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "restAddDeptJobTitle", jobTitle,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("jobTitle", jobTitle);
			return "redirect:add-department-jobTitle-assign";
		}

		logger.info("Method :addJobTitleAssigned ends");
		return "redirect:view-department-jobTitle-assign";
	}

	/*
	 * Get Mapping view  Department Job Title Assign
	 */
	@GetMapping("/view-department-jobTitle-assign")
	public String viewDepartmentJobTitleAssign(Model model, HttpSession session) {

		logger.info("Method : viewDepartmentJobTitleAssign  starts");

		logger.info("Method : viewDepartmentJobTitleAssign  ends");

		return "employee/view-jobTitle-assign";
	}

	/*
	 * view All  Department Job Title Assign 'Datatable' call
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-jobTitle-assign-ThroughAjax")
	public @ResponseBody DataTableResponse viewDepartmentJobTitleAssignThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1) {
		logger.info("Method : viewDepartmentJobTitleAssignThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsJobTitleAssignModel>> jsonResponse = new JsonResponse<List<HrmsJobTitleAssignModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getDepartmentJobTitleAssign", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsJobTitleAssignModel> jobTitleList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsJobTitleAssignModel>>() {
					});

			String s = "";
			for (HrmsJobTitleAssignModel m : jobTitleList) {

				if (m.gettDeptAssignStatus()) {
					s = "";
					s = s + "Active";
				} else {
					s = "";
					s = s + "Inactive";
				}
				m.setCompanyId(s);
				;

				s = "";
				byte[] pId = Base64.getEncoder().encode(m.getDeptId().getBytes());
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				s = s + "<a  title='Edit' href='view-department-jobTitle-assign-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit' style=\"font-size:24px\"></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteJobTitle(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash' style=\"font-size:24px\"></i></a> ";

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(jobTitleList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewDepartmentJobTitleAssignThroughAjax ends");
		return response;
	}

	/*
	 * for Edit  Department Job Title Assign
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-jobTitle-assign-edit")

	public String editJobTitle(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editJobTitle starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<HrmsJobTitleAssignModel> res = new JsonResponse<HrmsJobTitleAssignModel>();
		HrmsJobTitleAssignModel jobTitle = new HrmsJobTitleAssignModel();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "edit-jobTitle-assign?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		jobTitle = mapper.convertValue(res.getBody(), HrmsJobTitleAssignModel.class);

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-department",
					DropDownModel[].class);
			List<DropDownModel> deptList = Arrays.asList(dropDownModel);
			model.addAttribute("deptList", deptList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-jobTitleList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(dropDownModel);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> roleList = new ArrayList<String>();

		String[] roles = jobTitle.gettJobTitle().split(",");
		for (int i = 0; i < roles.length; i++) {
			roleList.add(roles[i]);
		}
		jobTitle.setRoleList(roleList);
		model.addAttribute("jobTitle", jobTitle);
		if (roleList.isEmpty()) {
			model.addAttribute("selectedRoles", "");
		} else {

			model.addAttribute("selectedRoles", roleList);
		}
		logger.info("Method : editJobTitle ends");
		return "employee/add-jobTitle-assign";
	}

	/*
	 * For Delete assigned job title
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-jobTitle-assign-delete")
	public @ResponseBody JsonResponse<Object> deleteJobTitle(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteJobTitle ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "deleteJobTitleAssign?id=" + id1, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteJobTitle  ends");

		return resp;
	}

	/**
	 * View selected  Department Job Title Assign in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-department-jobTitle-assign-model")
	public @ResponseBody JsonResponse<HrmsJobTitleAssignModel> viewJobTitleModel(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : viewJobTitleModel starts");
		JsonResponse<HrmsJobTitleAssignModel> res = new JsonResponse<HrmsJobTitleAssignModel>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "viewJobTitleModel?id=" + id1, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewJobTitleModel ends");
		return res;
	}

}
