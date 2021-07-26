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
import nirmalya.aathithya.webmodule.employee.model.GradeSalaryMasterModel;


/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class GradeSalaryMasterController {
	Logger logger = LoggerFactory.getLogger(GradeSalaryMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Grade Salary Master - add page
	 */

	@GetMapping("/add-grade-salary-master")
	public String addGradeSalaryMaster(Model model, HttpSession session) {

		logger.info("Method : addGradeSalaryMaster starts");

		GradeSalaryMasterModel gradeSalaryMaster = new GradeSalaryMasterModel();
		GradeSalaryMasterModel gradeSalarySession = (GradeSalaryMasterModel) session.getAttribute("sessionGradeSalary");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (gradeSalarySession != null) {
			model.addAttribute("gradeSalaryMaster", gradeSalarySession);
			session.setAttribute("gradeSalarySession", null);
		} else {
			model.addAttribute("gradeSalaryMaster", gradeSalaryMaster);
		}
		/*
		 * for viewing drop down list for Grade Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getGradeNameList",
					DropDownModel[].class);
			List<DropDownModel> gradeList = Arrays.asList(dropDownModel);
			model.addAttribute("gradeList", gradeList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list for Component list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getComponentList",
					DropDownModel[].class);
			List<DropDownModel> componentList = Arrays.asList(dropDownModel);
			model.addAttribute("componentList", componentList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addGradeSalaryMaster ends");

		return "employee/add-grade-salary-master";
	}

	/**
	 * get Description by the onChange of Grade selected
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-grade-salary-master-getDesc-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getGradeDesc(Model model, @RequestBody String tGradeId,
			BindingResult result) {
		logger.info("Method : getGradeDesc starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-gradeDesc?id=" + tGradeId,
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
		logger.info("Method : getGradeDesc ends");
		return res;
	}

	/**
	 * get Component Type by the onChange of Salary Component selected
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-grade-salary-master-getComponentType-throughAjax" })
	public @ResponseBody JsonResponse<GradeSalaryMasterModel> getComponentType(Model model,
			@RequestBody String tSalaryComponent, BindingResult result) {
		logger.info("Method : getComponentType starts");

		JsonResponse<GradeSalaryMasterModel> res = new JsonResponse<GradeSalaryMasterModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-componentType?id=" + tSalaryComponent,
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
		logger.info("Method : getComponentType ends");
		return res;
	}

	/*
	 * post Mapping for add Grade Salary Master details
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-grade-salary-master", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> addGradeSalaryMasterPost(
			@RequestBody List<GradeSalaryMasterModel> gradeSalaryMaster, Model model, HttpSession session) {
		logger.info("Method : addGradeSalaryMasterPost  starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (GradeSalaryMasterModel r : gradeSalaryMaster) {

				r.settCreatedBy(userId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "add-grade-salary-master", gradeSalaryMaster,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addGradeSalaryMasterPost  Ends");
		return res;
	}

	/*
	 * Get Mapping view Grade Salary Master details
	 */
	@GetMapping("/view-grade-salary-master")
	public String viewGradeSalaryMaster(Model model, HttpSession session) {

		logger.info("Method : viewGradeSalaryMaster starts");

		logger.info("Method : viewGradeSalaryMaster ends");

		return "employee/view-grade-salary-master";
	}
	/*
	 * For view employee Grade Salary Master details for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-grade-salary-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewGradeSalaryMasterThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewGradeSalaryMasterThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<GradeSalaryMasterModel>> jsonResponse = new JsonResponse<List<GradeSalaryMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getGradeSalaryMasterDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<GradeSalaryMasterModel> gradeSalaryMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<GradeSalaryMasterModel>>() {
					});

			String s = "";

			for (GradeSalaryMasterModel m : gradeSalaryMaster) {
				s = "";
				if (m.gettComponentType() == 1) {
					m.setStatus("Earning");
				} else {
					m.setStatus("Deduction");
				}

				if (m.gettCalculationType() == 1) {
					m.settCalculation("flat amount");
				} else if (m.gettCalculationType() == 2) {
					m.settCalculation("% of basic");
				} else {
					m.settCalculation("% of CTC");
				}

				byte[] encodeId = Base64.getEncoder().encode(m.gettGradeSalaryId().toString().getBytes());

				s = s + "<a href='view-grade-salary-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(gradeSalaryMaster);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewGradeSalaryMasterThroughAjax ends");

		return response;
	}
	/*
	 * edit Grade Salary Master details
	 */

	@GetMapping("/view-grade-salary-master-edit")
	public String editGradeSalaryMaster(Model model, @RequestParam("id") String dept, HttpSession session) {

		logger.info("Method : editGradeSalaryMaster starts");
		byte[] encodeByte = Base64.getDecoder().decode(dept.getBytes());
		String id = (new String(encodeByte));

		/*
		 * for viewing drop down list for Grade Name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getGradeNameList",
					DropDownModel[].class);
			List<DropDownModel> gradeList = Arrays.asList(dropDownModel);
			model.addAttribute("gradeList", gradeList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list for Component list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getComponentList",
					DropDownModel[].class);
			List<DropDownModel> componentList = Arrays.asList(dropDownModel);
			model.addAttribute("componentList", componentList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			GradeSalaryMasterModel[] gradeSalaryMaster = restClient.getForObject(
					env.getEmployeeUrl() + "edit-grade-salary-master-ById?id=" + id, GradeSalaryMasterModel[].class);
			List<GradeSalaryMasterModel> gradeSalaryMasterList = Arrays.asList(gradeSalaryMaster);
			for (GradeSalaryMasterModel m : gradeSalaryMasterList) {
				if (m.gettComponentType() == 1) {
					m.setStatus("Earning");
				} else {
					m.setStatus("Deduction");
				}
			}
			model.addAttribute("tComponentType", gradeSalaryMasterList.get(0).getStatus());
			model.addAttribute("id", gradeSalaryMasterList.get(0).gettGradeSalaryId());
			model.addAttribute("gradeSalaryMasterList", gradeSalaryMasterList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editGradeSalaryMaster ends");
		return "employee/add-grade-salary-master";

	}

	/*
	 * For Modal Grade Salary Master details
	 */
	/*
	 * For Modal Goal Master View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-grade-salary-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalGradeSalaryMaster(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGradeSalaryMaster starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "modelGradeSalaryMasterByTd?id=" + id,
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
		logger.info("Method :modalGradeSalaryMaster ends");
		return res;
	}

}
