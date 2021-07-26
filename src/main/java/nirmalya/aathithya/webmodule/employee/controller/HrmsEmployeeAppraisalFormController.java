package nirmalya.aathithya.webmodule.employee.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import nirmalya.aathithya.webmodule.common.utils.DateFormatter;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmEmployeeReviewerDetailsModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeAppraisalFormModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeAppraisalFormController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeAppraisalFormController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view employee-appraisal-details
	 */
	@GetMapping("/view-employee-appraisal-form")
	public String viewEmployeeAppraisalForm(Model model, HttpSession session) {

		logger.info("Method : viewEmployeeAppraisalForm  starts");
		logger.info("Method : viewEmployeeAppraisalForm  ends");

		return "employee/view-Empoyee-Appraisal-Form";
	}

	/*
	 * view employee-appraisal-form 'Datatable' call
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-appraisal-form-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployeeAppraisalFormThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, HttpSession session) {

		logger.info("Method : viewEmployeeAppraisalFormThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String userId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setUserId(userId);
			JsonResponse<List<HrmsEmployeeAppraisalFormModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeAppraisalFormModel>>();
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getEmployeeAppraisalForm", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<HrmsEmployeeAppraisalFormModel> appraisalForm = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeAppraisalFormModel>>() {
					});
			String s = "";
			for (HrmsEmployeeAppraisalFormModel m : appraisalForm) {
				s = "";
				String appraisalId = Integer.toString(m.gettAppraisalSetupId());
				byte[] pId = Base64.getEncoder().encode(appraisalId.getBytes());

				String fdate = m.getFromDate();

				if (m.gettStatus() == true) {
					s = "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				} else {
					s = s + "&nbsp;&nbsp;<a href='view-employee-appraisal-form-FillUp?id=" + new String(pId)
							+ "'><i class='fa fa-files-o' title='Appraisal Form' style=\"font-size:20px\"></i></a>";
				}
				m.setAction(s);
				s = "";
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(appraisalForm);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewEmployeeAppraisalFormThroughAjax ends");

		return response;
	}

	/*
	 * for Edit employee-appraisal-details
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-employee-appraisal-form-FillUp" })
	public String fillAppraisalForm(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method :fillAppraisalForm starts");

		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id1 = (new String(decodeId));

		JsonResponse<HrmsEmployeeAppraisalFormModel> jsonresponse = new JsonResponse<HrmsEmployeeAppraisalFormModel>();
		try {

			jsonresponse = restClient.getForObject(env.getEmployeeUrl() + "fill-Appraisal-Form?id=" + id1,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		Calendar calender = Calendar.getInstance();
		HrmsEmployeeAppraisalFormModel appraisalForm = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<HrmsEmployeeAppraisalFormModel>() {
				});
		////////////////////////////////////////////////////////////////////
		int days = appraisalForm.getDueDate();
		String fromDate = appraisalForm.getFromDate();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		try {
			date = formatter.parse(fromDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			calender.setTime(formatter.parse(fromDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calender.add(Calendar.DAY_OF_MONTH, days);
		String dueDate = (String) DateFormatter.returnStringDateIndianFormat(formatter.format(calender.getTime()));
		/////////////////////////////////////////////////////////////////
		appraisalForm.setEmpDueDate(dueDate);
		model.addAttribute("appraisalForm", appraisalForm);

		logger.info("Method :fillAppraisalForm ends");

		return "employee/add-Empoyee-Appraisal-Form";
	}
	/*
	 * post Mapping for add Appraisal Form
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-employee-appraisal-form-submit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> submitAppraisalForm(
			@RequestBody List<HrmsEmployeeAppraisalFormModel> appraisalForm, Model model, HttpSession session) {
		logger.info("Method : submitAppraisalForm  starts");

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
			for (HrmsEmployeeAppraisalFormModel r : appraisalForm) {
				r.settCreatedBy(userId);
				r.settCompanyId(companyId);
			}
			res = restClient.postForObject(env.getEmployeeUrl() + "submit-appraisal-form", appraisalForm,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();
		if (message != null && message != "") {
		} else {
			res.setMessage("Success");
		}

		logger.info("Method : submitAppraisalForm  Ends");
		return res;
	}

	/*
	 * For Modal view
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-appraisal-form-modalView" })
	public @ResponseBody JsonResponse<List<HrmsEmployeeAppraisalFormModel>> employeeAppraisalModalView(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :employeeAppraisalModalView starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());
		String id1 = (new String(decodeId));
		JsonResponse<List<HrmsEmployeeAppraisalFormModel>> response = new JsonResponse<List<HrmsEmployeeAppraisalFormModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "employeeAppraisalModalView?id=" + id1,
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

		logger.info("Method : employeeAppraisalModalView  ends ");
		return response;
	}
}
