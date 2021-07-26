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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import nirmalya.aathithya.webmodule.employee.model.HrmEmployeeEducationModel;

@Controller
@RequestMapping(value = "employee")
public class HrmEmployeeEducationController {
	Logger logger = LoggerFactory.getLogger(HrmEmployeeEducationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee education
	 */
	@GetMapping("/add-employee-education")
	public String addEmploreeQualification(Model model, HttpSession session) {

		logger.info("Method : addEmploreeQualification starts");

		HrmEmployeeEducationModel qualifAssign = new HrmEmployeeEducationModel();
		HrmEmployeeEducationModel sessionqualifAssign = (HrmEmployeeEducationModel) session
				.getAttribute("qualifAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionqualifAssign != null) {
			model.addAttribute("qualifAssign", sessionqualifAssign);
			session.setAttribute("qualifAssign", null);
		} else {
			model.addAttribute("qualifAssign", qualifAssign);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] qualif = restClient.getForObject(env.getEmployeeUrl() + "getQualifList", DropDownModel[].class);
			List<DropDownModel> QualifList = Arrays.asList(qualif);
			model.addAttribute("QualifList", QualifList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addEmploreeQualification ends");
		return "employee/add-employee-education";
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-employee-education-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addServicePrice(
			@RequestBody List<HrmEmployeeEducationModel> hrmEmployeeEducationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addServicePrice function starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (HrmEmployeeEducationModel r : hrmEmployeeEducationModel) {

				r.setCreatedBy(userId);
				r.setCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "restAddEmployeeEdu", hrmEmployeeEducationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addServicePrice function Ends");
		return res;
	}

	/*
	 * Get Mapping view employee education
	 */
	@GetMapping("/view-employee-education")
	public String viewEmployeEducation(Model model, HttpSession session) {

		logger.info("Method : viewEmployeEducation starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewEmployeEducation ends");

		return "employee/view-employe-education";
	}

	/*
	 * For view employee education for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-education-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployeEducation(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewEmployeEducation statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmEmployeeEducationModel>> jsonResponse = new JsonResponse<List<HrmEmployeeEducationModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignEduDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmEmployeeEducationModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmEmployeeEducationModel>>() {
					});

			String s = "";

			for (HrmEmployeeEducationModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmpId().getBytes());

				s = s + "<a href='view-employee-education-edit?empId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignEdu);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewEmployeEducation ends");

		return response;
	}

	/*
	 * for Edit assign Edu
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-education-edit")
	public String editAssignEducation(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editAssignEducation starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmEmployeeEducationModel>> response = new JsonResponse<List<HrmEmployeeEducationModel>>();
		try {

			response = restClient.getForObject(
					env.getEmployeeUrl() + "getAssignEducationById?empId=" + id, JsonResponse.class);

			
			ObjectMapper mapper = new ObjectMapper();

			List<HrmEmployeeEducationModel> assignQualif = mapper.convertValue(response.getBody(),
					new TypeReference<List<HrmEmployeeEducationModel>>() {
					});
			if (assignQualif != null) {
				assignQualif.get(0).setEditId("edit");
			}
		
			model.addAttribute("qualifAssign", assignQualif);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList1",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] qualif = restClient.getForObject(env.getEmployeeUrl() + "getQualifList", DropDownModel[].class);
			List<DropDownModel> QualifList = Arrays.asList(qualif);
			model.addAttribute("QualifList", QualifList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editAssignEducation ends");

		return "employee/add-employee-education";
	}

	/*
	 * For Modal other employee education
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-education-modalView" })
	public @ResponseBody JsonResponse<List<HrmEmployeeEducationModel>> modalAssignmentEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<HrmEmployeeEducationModel>> response = new JsonResponse<List<HrmEmployeeEducationModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getAssignEducationById?empId=" + id,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		logger.info("Method : modalAssignmentEdu  ends ");
		return response;
	}

}
