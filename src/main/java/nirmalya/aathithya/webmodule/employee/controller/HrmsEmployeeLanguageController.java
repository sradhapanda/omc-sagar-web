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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeLanguageModel;

@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeLanguageController {
	

	Logger logger = LoggerFactory.getLogger(HrmsEmployeeLanguageController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for add employee Language
	 */
	@GetMapping("/add-employee-language")
	public String addEmploreeLanguage(Model model, HttpSession session) {

		logger.info("Method : addEmploreeLanguage starts");

		HrmsEmployeeLanguageModel langAssign = new HrmsEmployeeLanguageModel();
		HrmsEmployeeLanguageModel sessionlangAssign = (HrmsEmployeeLanguageModel) session
				.getAttribute("langAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionlangAssign != null) {
			model.addAttribute("langAssign", sessionlangAssign);
			session.setAttribute("langAssign", null);
		} else {
			model.addAttribute("langAssign", langAssign);
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
			DropDownModel[] language = restClient.getForObject(env.getEmployeeUrl() + "getLangList", DropDownModel[].class);
			List<DropDownModel> LangList = Arrays.asList(language);
			model.addAttribute("LangList", LangList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] canDo = restClient.getForObject(env.getEmployeeUrl() + "getCanDo", DropDownModel[].class);
			List<DropDownModel> canDoList = Arrays.asList(canDo);
			model.addAttribute("canDoList", canDoList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addEmploreeLanguage ends");
		return "employee/add-employee-language";
	}

	/*
	 * post mapping for add employee Language
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-employee-language-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addServicePrice(
			@RequestBody List<HrmsEmployeeLanguageModel> HrmsEmployeeLanguageModel, Model model, HttpSession session) {
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

			for (HrmsEmployeeLanguageModel r : HrmsEmployeeLanguageModel) {

				r.setCreatedBy(userId);
				r.setCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "restAddEmployeeLang", HrmsEmployeeLanguageModel,
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
	 * Get Mapping view employee Language
	 */
	@GetMapping("/view-employee-language")
	public String viewEmployeLanguage(Model model, HttpSession session) {

		logger.info("Method : viewEmployeLanguage starts");

	
		logger.info("Method : viewEmployeLanguage ends");

		return "employee/view-employee-language";
	}

	/*
	 * For view employee Language for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-language-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployeLanguage(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewEmployeLanguage statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmployeeLanguageModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeLanguageModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignLangDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeLanguageModel> assignEdu = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeLanguageModel>>() {
					});

			String s = "";

			for (HrmsEmployeeLanguageModel m : assignEdu) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmpId().getBytes());

				s = s + "<a href='view-employee-language-edit?empId=" + new String(encodeId)
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

		logger.info("Method : viewEmployeLanguage ends");

		return response;
	}

	/*
	 * for Edit assign Edu
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-language-edit")
	public String editAssignLanguage(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editAssignLanguage starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmsEmployeeLanguageModel>> response = new JsonResponse<List<HrmsEmployeeLanguageModel>>();
		try {

			response = restClient.getForObject(
					env.getEmployeeUrl() + "getAssignLanguageById?empId=" + id, JsonResponse.class);

			
			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeLanguageModel> assignQualif = mapper.convertValue(response.getBody(),
					new TypeReference<List<HrmsEmployeeLanguageModel>>() {
					});
			if (assignQualif != null) {
				assignQualif.get(0).setEditId("edit");
			}
		
			model.addAttribute("langAssign", assignQualif);
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
			DropDownModel[] language = restClient.getForObject(env.getEmployeeUrl() + "getLangList", DropDownModel[].class);
			List<DropDownModel> LangList = Arrays.asList(language);
			model.addAttribute("LangList", LangList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] canDo = restClient.getForObject(env.getEmployeeUrl() + "getCanDo", DropDownModel[].class);
			List<DropDownModel> canDoList = Arrays.asList(canDo);
			model.addAttribute("canDoList", canDoList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editAssignLanguage ends");

		return "employee/add-employee-Language";
	}

	/*
	 * For Modal other employee Language
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-language-modalView" })
	public @ResponseBody JsonResponse<List<HrmsEmployeeLanguageModel>> modalAssignmentEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<HrmsEmployeeLanguageModel>> response = new JsonResponse<List<HrmsEmployeeLanguageModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getAssignLanguageById?empId=" + id,
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

