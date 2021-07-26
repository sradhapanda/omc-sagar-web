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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeDependentModel;;
/**
 * 
 * @author Nirmalya labs
 *
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeDependentController {

	Logger logger = LoggerFactory.getLogger(HrmsEmployeeDependentController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee dependent
	 */
	@GetMapping("/add-employee-dependent")
	public String addEmploreedependent(Model model, HttpSession session) {

		logger.info("Method : addEmploreedependent starts");

		HrmsEmployeeDependentModel dpndAssign = new HrmsEmployeeDependentModel();
		HrmsEmployeeDependentModel sessiondpndAssign = (HrmsEmployeeDependentModel) session
				.getAttribute("dpndAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiondpndAssign != null) {
			model.addAttribute("dpndAssign", sessiondpndAssign);
			session.setAttribute("dpndAssign", null);
		} else {
			model.addAttribute("dpndAssign", dpndAssign);
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
			DropDownModel[] canDo = restClient.getForObject(env.getEmployeeUrl() + "getRelations", DropDownModel[].class);
			List<DropDownModel> canDoList = Arrays.asList(canDo);
			model.addAttribute("canDoList", canDoList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addEmploreedependent ends");
		return "employee/add-employee-dependent";
	}

	/*
	 * post mapping for add employee dependent
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-employee-dependent-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addDependent(
			@RequestBody List<HrmsEmployeeDependentModel> HrmsEmployeeDependentModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addDependent function starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (HrmsEmployeeDependentModel r : HrmsEmployeeDependentModel) {

				r.setCreatedBy(userId);
				r.setCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "restAddEmployeeDepnd", HrmsEmployeeDependentModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addDependent function Ends");
		return res;
	}

	/*
	 * Get Mapping view employee dependent
	 */
	@GetMapping("/view-employee-dependent")
	public String viewEmployedependent(Model model, HttpSession session) {

		logger.info("Method : viewEmployedependent starts");

	
		logger.info("Method : viewEmployedependent ends");

		return "employee/view-employee-dependent";
	}

	/*
	 * For view employee dependent for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-dependent-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployedependent(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewEmployedependent statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmployeeDependentModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeDependentModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignDepndDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeDependentModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeDependentModel>>() {
					});

			String s = "";

			for (HrmsEmployeeDependentModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmpId().getBytes());

				s = s + "<a href='view-employee-dependent-edit?empId=" + new String(encodeId)
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

		logger.info("Method : viewEmployedependent ends");

		return response;
	}

	/*
	 * for Edit assign Dependent 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-dependent-edit")
	public String editAssigndependent(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editAssigndependent starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmsEmployeeDependentModel>> response = new JsonResponse<List<HrmsEmployeeDependentModel>>();
		try {

			response = restClient.getForObject(
					env.getEmployeeUrl() + "getAssigndDepndById?empId=" + id, JsonResponse.class);

			
			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeDependentModel> assignDepnd = mapper.convertValue(response.getBody(),
					new TypeReference<List<HrmsEmployeeDependentModel>>() {
					});
			if (assignDepnd != null) {
				assignDepnd.get(0).setEditId("edit");
			}
		
			model.addAttribute("dpndAssign", assignDepnd);
			model.addAttribute("Edit", "For Edit");
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList3",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] canDo = restClient.getForObject(env.getEmployeeUrl() + "getRelations", DropDownModel[].class);
			List<DropDownModel> canDoList = Arrays.asList(canDo);
			model.addAttribute("canDoList", canDoList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editAssigndependent ends");

		return "employee/add-employee-dependent";
	}

	/*
	 * For Modal other employee dependent
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-dependent-modalView" })
	public @ResponseBody JsonResponse<List<HrmsEmployeeDependentModel>> modalAssignmentDepnd(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentDepnd starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<HrmsEmployeeDependentModel>> response = new JsonResponse<List<HrmsEmployeeDependentModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getAssigndDepndById?empId=" + id,
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
		logger.info("Method : modalAssignmentDepnd  ends ");
		return response;
	}

}


