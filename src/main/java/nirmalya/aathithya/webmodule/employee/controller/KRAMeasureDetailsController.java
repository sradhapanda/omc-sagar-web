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
import nirmalya.aathithya.webmodule.employee.model.KRAMeasureDetailsModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class KRAMeasureDetailsController {
	Logger logger = LoggerFactory.getLogger(KRAMeasureDetailsController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add KRA Measure
	 */
	@GetMapping("/add-KRAMeasure-details")
	public String addKRAMeasureMaster(Model model, HttpSession session) {

		logger.info("Method : addKRAMeasureMaster starts");

		KRAMeasureDetailsModel measureDetails = new KRAMeasureDetailsModel();
		KRAMeasureDetailsModel measureDetailsSession = (KRAMeasureDetailsModel) session
				.getAttribute("sessionMeasureDetails");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (measureDetailsSession != null) {
			model.addAttribute("measureDetails", measureDetailsSession);
			session.setAttribute("measureDetailsSession", null);
		} else {
			model.addAttribute("measureDetails", measureDetails);
		}
		/*
		 * for viewing drop down list for department
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getDeptNameList",
					DropDownModel[].class);
			List<DropDownModel> deptName = Arrays.asList(dropDownModel);
			model.addAttribute("deptName", deptName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list for goal
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getGoalNameList",
					DropDownModel[].class);
			List<DropDownModel> goalList = Arrays.asList(dropDownModel);
			model.addAttribute("goalList", goalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addKRAMeasureMaster ends");

		return "employee/add-KRAMeasure-details";
	}
	
	/*
	 * dropDown value for Job Title through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-KRAMeasure-details-jobTitle-throughAjax" })
	public @ResponseBody JsonResponse<KRAMeasureDetailsModel> getJobTitleNameAJAX(Model model,
			@RequestBody String kDepartment, BindingResult result) {
		logger.info("Method : getJobTitleNameAJAX starts");
		JsonResponse<KRAMeasureDetailsModel> res = new JsonResponse<KRAMeasureDetailsModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-job-title?id=" + kDepartment,
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

		logger.info("Method : getJobTitleNameAJAX ends");

		return res;

	}

	/*
	 * post Mapping for add KRAMeasure details
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-KRAMeasure-details", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> addKRAMeasureDetails(
			@RequestBody List<KRAMeasureDetailsModel> measureDetails, Model model, HttpSession session) {
		logger.info("Method : addKRAMeasureDetails  starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for (KRAMeasureDetailsModel r : measureDetails) {

				r.setkCreatedBy(userId);
				r.setkCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "add-KRAMeasuer-details", measureDetails,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addKRAMeasureDetails  Ends");
		return res;
	}

	/*
	 * Get Mapping view KRAMeasure details
	 */
	@GetMapping("/view-KRAMeasure-details")
	public String viewKRAMeasureDetails(Model model, HttpSession session) {

		logger.info("Method : viewKRAMeasureDetails starts");

		logger.info("Method : viewKRAMeasureDetails ends");

		return "employee/view-KRAMeasure-details";
	}

	/*
	 * For view employee KRAMeasure details for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-KRAMeasure-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewKRAMeasureDetailsThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewKRAMeasureDetailsThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<KRAMeasureDetailsModel>> jsonResponse = new JsonResponse<List<KRAMeasureDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getKRAMeasureDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<KRAMeasureDetailsModel> measureDetails = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<KRAMeasureDetailsModel>>() {
					});

			String s = "";

			for (KRAMeasureDetailsModel m : measureDetails) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getDeptId().getBytes());
				byte[] encodeId1 = Base64.getEncoder().encode(m.getJobTitleId().getBytes());

				s = s + "<a href='view-KRAMeasure-details-edit?deptId=" + new String(encodeId) + "&jobId="
						+ new String(encodeId1)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(encodeId) + ',' + new String(encodeId1)
						+ "\")'><i class='fa fa-search search'style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(measureDetails);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewKRAMeasureDetailsThroughAjax ends");

		return response;
	}

	/*
	 * edit KRAMeasure details
	 */

	@GetMapping("/view-KRAMeasure-details-edit")
	public String editKRAMeasureDetails(Model model, @RequestParam("deptId") String dept,
			@RequestParam("jobId") String jobTitle, HttpSession session) {

		logger.info("Method : editKRAMeasureDetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(dept.getBytes());
		String deptId = (new String(encodeByte));
		byte[] encodeByte1 = Base64.getDecoder().decode(jobTitle.getBytes());
		String jobId = (new String(encodeByte1));

		/*
		 * JsonResponse<KRAMeasureDetailsModel> res = new
		 * JsonResponse<KRAMeasureDetailsModel>(); KRAMeasureDetailsModel
		 * measureDetails = new KRAMeasureDetailsModel();
		 */

		/*
		 * for viewing drop down list for department
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getDeptNameList",
					DropDownModel[].class);
			List<DropDownModel> deptName = Arrays.asList(dropDownModel);
			model.addAttribute("deptName", deptName);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list for goal
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getGoalNameList",
					DropDownModel[].class);
			List<DropDownModel> goalList = Arrays.asList(dropDownModel);
			model.addAttribute("goalList", goalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			KRAMeasureDetailsModel[] measureDetails = restClient.getForObject(
					env.getEmployeeUrl() + "edit-KRAMeasure-details-ById?id=" + deptId + "&jobId=" + jobId,
					KRAMeasureDetailsModel[].class);
			List<KRAMeasureDetailsModel> measureDetailsList = Arrays.asList(measureDetails);
			String department = measureDetailsList.get(0).getkDepartment();
			/*
			 * for viewing drop down list for job title
			 */

			try {
				DropDownModel[] dropDownModel = restClient.getForObject(
						env.getEmployeeUrl() + "rest-get-jobTitle?id=" + department, DropDownModel[].class);
				List<DropDownModel> getJobTitle = Arrays.asList(dropDownModel);
				model.addAttribute("getJobTitle", getJobTitle);
			} catch (RestClientException e) {
				e.printStackTrace();
			}
			model.addAttribute("id", measureDetailsList.get(0).getIsedit());
			model.addAttribute("measureDetailsList", measureDetailsList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editKRAMeasureDetails ends");
		return "employee/add-KRAMeasure-details";

	}

	/*
	 * For Modal KRAMeasure details
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-KRAMeasure-details-modalView" })
	public @ResponseBody JsonResponse<Object> modalKRAMeasureDetails(Model model,
			@RequestBody KRAMeasureDetailsModel index, BindingResult result) {

		logger.info("Method : modalKRAMeasureDetails starts");
		byte[] decodedByte = Base64.getDecoder().decode(index.getDeptId());
		String deptId = (new String(decodedByte));
		byte[] decodedByte1 = Base64.getDecoder().decode(index.getJobTitleId());
		String jobId = (new String(decodedByte1));
		System.out.println("+++++++++=" + index);
		System.out.println(index.getJobTitleId());

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "getModalKRAMeasureDetailsById?id=" + deptId + "&jobId=" + jobId,
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

		logger.info("Method : modalKRAMeasureDetails ends");
		return res;
	}

}
