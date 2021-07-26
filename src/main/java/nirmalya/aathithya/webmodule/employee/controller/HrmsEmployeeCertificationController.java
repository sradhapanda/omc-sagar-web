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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeCertificationModel;

@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeCertificationController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeCertificationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping for employee certification
	 */
	@GetMapping("/add-employee-certification")
	public String addEmploreeQualification(Model model, HttpSession session) {

		logger.info("Method : addEmploreeQualification starts");

		HrmsEmployeeCertificationModel certiAssign = new HrmsEmployeeCertificationModel();
		HrmsEmployeeCertificationModel sessioncertiAssign = (HrmsEmployeeCertificationModel) session
				.getAttribute("certiAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessioncertiAssign != null) {
			model.addAttribute("certiAssign", sessioncertiAssign);
			session.setAttribute("certiAssign", null);
		} else {
			model.addAttribute("certiAssign", certiAssign);
		}

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList2",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] qualif = restClient.getForObject(env.getEmployeeUrl() + "getCertifList", DropDownModel[].class);
			List<DropDownModel> certifList = Arrays.asList(qualif);
			model.addAttribute("certifList", certifList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addEmploreeQualification ends");
		return "employee/add-employee-certification";
	}

	/*
	 * post mapping for add employee  certification 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-employee-certification-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addCertification(
			@RequestBody List<HrmsEmployeeCertificationModel> hrmsEmployeeCertificationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addCertification function starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (HrmsEmployeeCertificationModel r : hrmsEmployeeCertificationModel) {

				r.setCreatedBy(userId);
				r.setCompanyId(companyId);

			}
			res = restClient.postForObject(env.getEmployeeUrl() + "restAddEmployeeCert", hrmsEmployeeCertificationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addCertification function Ends");
		return res;
	}

	/*
	 * Get Mapping view certification
	 */
	@GetMapping("/view-employee-certification")
	public String viewEmployecertification(Model model, HttpSession session) {

		logger.info("Method : viewEmployecertification starts");

		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] Employee = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeList2",
					DropDownModel[].class);
			List<DropDownModel> EmployeeList = Arrays.asList(Employee);
			model.addAttribute("EmployeeList", EmployeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewEmployecertification ends");

		return "employee/view-employee-certification";
	}

	/*
	 * For view certification for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-certification-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployecertification(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewEmployecertification statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsEmployeeCertificationModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeCertificationModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getAssignCertDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeCertificationModel> assignCert = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeCertificationModel>>() {
					});

			String s = "";

			for (HrmsEmployeeCertificationModel m : assignCert) {
				s = "";

				byte[] encodeId = Base64.getEncoder().encode(m.getEmpId().getBytes());

				s = s + "<a href='view-employee-certification-edit?empId=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(assignCert);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewEmployecertification ends");

		return response;
	}

	/*
	 * for Edit assign certification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-certification-edit")
	public String editAssigncertification(Model model, @RequestParam("empId") String encodeId, HttpSession session) {

		logger.info("Method :editAssigncertification starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		JsonResponse<List<HrmsEmployeeCertificationModel>> response = new JsonResponse<List<HrmsEmployeeCertificationModel>>();
		try {

			response = restClient.getForObject(
					env.getEmployeeUrl() + "getAssigncertificationById?empId=" + id, JsonResponse.class);

			
			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeCertificationModel> assignQualif = mapper.convertValue(response.getBody(),
					new TypeReference<List<HrmsEmployeeCertificationModel>>() {
					});
			if (assignQualif != null) {
				assignQualif.get(0).setEditId("edit");
			}
		
			model.addAttribute("certiAssign", assignQualif);
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
			DropDownModel[] qualif = restClient.getForObject(env.getEmployeeUrl() + "getCertifList", DropDownModel[].class);
			List<DropDownModel> certifList = Arrays.asList(qualif);
			model.addAttribute("certifList", certifList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editAssigncertification ends");

		return "employee/add-employee-certification";
	}

	/*
	 * For Modal certification 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-certification-modalView" })
	public @ResponseBody JsonResponse<List<HrmsEmployeeCertificationModel>> modalAssignmentEdu(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :modalAssignmentEdu starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		JsonResponse<List<HrmsEmployeeCertificationModel>> response = new JsonResponse<List<HrmsEmployeeCertificationModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getAssigncertificationById?empId=" + id,
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

