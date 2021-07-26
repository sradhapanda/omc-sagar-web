package nirmalya.aathithya.webmodule.employee.controller;

import java.util.Base64;

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
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmsAppraisalFormModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsAppraisalFormModel;
import nirmalya.aathithya.webmodule.employee.model.HrmsAppraisalFormModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsAppraisalFormController {
	Logger logger = LoggerFactory.getLogger(HrmsAppraisalFormController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*************************************************************************************************************************************************/
	/****************************************
	 * WEB CONTROLLER FOR MANAGER
	 ****************************************/
	/*************************************************************************************************************************************************/

	/*
	 * Get Mapping view Appraisal Form for First Stage Approval
	 */
	@GetMapping("/view-manager-appraisal-form")
	public String viewManagerAppraisalForm(Model model, HttpSession session) {

		logger.info("Method : viewManagerAppraisalForm  starts");

		logger.info("Method : viewManagerAppraisalForm  ends");

		return "employee/view-Manager-Appraisal-Form";
	}

	/*
	 * view Appraisal Form First Stage Details 'Datatable' call
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-manager-appraisal-form-ThroughAjax")
	public @ResponseBody DataTableResponse viewManagerAppraisalFormThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {
		logger.info("Method : viewManagerAppraisalFormThroughAjax starts");
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
			tableRequest.setUserId(userId);

			JsonResponse<List<HrmsAppraisalFormModel>> jsonResponse = new JsonResponse<List<HrmsAppraisalFormModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getManagerAppraisalForm", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsAppraisalFormModel> managerAppraisalForm = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAppraisalFormModel>>() {
					});

			String s = "";

			for (HrmsAppraisalFormModel m : managerAppraisalForm) {

				String appId = Integer.toString(m.gettAppraisalSetupId());
				s = "";
				byte[] pId = Base64.getEncoder().encode(appId.getBytes());
				byte[] fId = Base64.getEncoder().encode(m.gettAppraisalFromDate().getBytes());
				System.out.println("#########" + m.gettStageNo());
				if (m.gettStageNo() == 2) {
					s = "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
							
				} else {
					s = s + "&nbsp;&nbsp;<a href='view-manager-appraisal-form-FillUp?id=" + new String(pId) + "&fId="
							+ new String(fId)
							+ "'><i class='fa fa-files-o' title='Appraisal Form' style=\"font-size:20px\"></i></a>";
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectAppraisal(\""
							+ new String(pId)+','+ new String(fId) + "\",1)'><i class='fa fa-close'style=\"font-size:24px\"></i></a> &nbsp;&nbsp; ";
				
				}
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(managerAppraisalForm);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewManagerAppraisalFormThroughAjax ends");

		return response;
	}

	/*
	 * for First Stage Approval
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-manager-appraisal-form-FillUp" })
	public String appraisalFormManagerApproval(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			@RequestParam("fId") String fromdate, Model model) {

		logger.info("Method :appraisalFormManagerApproval starts");
		String fDate = fromdate;
		byte[] decodeId2 = Base64.getDecoder().decode(fDate.getBytes());
		String fromDate = (new String(decodeId2));
		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id1 = (new String(decodeId));

		System.out.println("#####fromDate" + fromDate);
		JsonResponse<List<HrmsAppraisalFormModel>> jsonresponse = new JsonResponse<List<HrmsAppraisalFormModel>>();
		try {
			jsonresponse = restClient.getForObject(
					env.getEmployeeUrl() + "appraisalFormApproval-manager?id=" + id1 + "&fromDate=" + fromDate,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<HrmsAppraisalFormModel> managerAppraisalForm = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<HrmsAppraisalFormModel>>() {
				});

		model.addAttribute("managerAppraisalForm", managerAppraisalForm);

		logger.info("Method :appraisalFormManagerApproval ends");

		return "employee/add-Manager-Appraisal-Form";
	}

	/*
	 * for First Stage Approval - Submit
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-manager-appraisal-form-submit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> appraisalFormSubmit(
			@RequestBody List<HrmsAppraisalFormModel> managerAppraisalForm, Model model, HttpSession session) {
		logger.info("Method : appraisalFormSubmit  starts");
		System.out.println("managerAppraisalForm" + managerAppraisalForm);
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
			for (HrmsAppraisalFormModel r : managerAppraisalForm) {

				r.settCreatedBy(userId);
				r.settCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "submit-appraisal-form-manager", managerAppraisalForm,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : appraisalFormSubmit  Ends");
		return res;
	}

	/*
	 * Manager Modal View
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-manager-appraisal-form-modalView" })
	public @ResponseBody JsonResponse<List<HrmsAppraisalFormModel>> appraisalModalViewManager(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :appraisalModalViewManager starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id1 = (new String(decodeId));

		JsonResponse<List<HrmsAppraisalFormModel>> response = new JsonResponse<List<HrmsAppraisalFormModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "appraisalModalView-manager?id=" + id1,
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
		logger.info("Method : appraisalModalViewManager  ends ");
		return response;
	}

	
	/*
	 * Appraisal status change
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-manager-appraisal-form-change-status" })
	public @ResponseBody JsonResponse<Object> changeAppraisalSetupStatus( @RequestParam("id") String id1, @RequestParam("date") String fdate,
			 HttpSession session) {
		System.out.println("id" + id1);
		System.out.println("date" + fdate);
		logger.info("Method : changeAppraisalSetupStatus starts");

		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());
		String index = (new String(encodeByte));
		System.out.println(index);
		byte[] encodeByte1 = Base64.getDecoder().decode(fdate.getBytes());
		String fromDate = (new String(encodeByte1));
		System.out.println("fromDate"+fromDate);
		
		
		// Boolean status = adminAppraisalForm.gettAppraisalFinalStatus();
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "changeAppraisalSetupStatus?id=" + index+ "&fdate=" +fromDate,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :changeAppraisalSetupStatus  ends");

		return resp;
	}

	/*************************************************************************************************************************************************/
	/****************************************
	 * WEB CONTROLLER FOR SUPER ADMIN
	 ****************************************/
	/*************************************************************************************************************************************************/

	/*
	 * Get Mapping view Appraisal Form for Second Stage Approval
	 */

	@GetMapping("/view-superAdmin-appraisal-form")
	public String viewSuperAdminAppraisalForm(Model model, HttpSession session) {

		logger.info("Method : viewSuperAdminAppraisalForm  starts");

		logger.info("Method : viewSuperAdminAppraisalForm  ends");

		return "employee/view-SuperAdmin-Appraisal-Form";
	}

	/*
	 * view Appraisal Form Second Stage Details 'Datatable' call
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-superAdmin-appraisal-form-ThroughAjax")
	public @ResponseBody DataTableResponse viewSuperAdminAppraisalFormThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, HttpSession session) {
		logger.info("Method : viewSuperAdminAppraisalFormThroughAjax starts");
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
			tableRequest.setUserId(userId);

			JsonResponse<List<HrmsAppraisalFormModel>> jsonResponse = new JsonResponse<List<HrmsAppraisalFormModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getSuperAdminAppraisalForm", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsAppraisalFormModel> adminAppraisalForm = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAppraisalFormModel>>() {
					});

			String s = "";

			for (HrmsAppraisalFormModel m : adminAppraisalForm) {

				String appId = Integer.toString(m.gettAppraisalSetupId());
				s = "";
				byte[] fId = Base64.getEncoder().encode(m.gettAppraisalFromDate().getBytes());
				byte[] pId = Base64.getEncoder().encode(appId.getBytes());
				if (!m.gettAppraisalStatus()) {
					s = "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
							+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
					if (!m.gettAppraisalFinalStatus()) {
					s = s + "<a href='javascript:void(0)'" + "' onclick='ChangeStatus(\"" + new String(pId)
							+ "\")' ><i class=\"fa fa-times-circle\" title=\"Pending\" style=\"font-size:24px;color:#e30f0f\"></i></a>";
					} else {
						s = s + "&nbsp; &nbsp; <a href='javascript:void(0)'" + "' onclick='DeleteTblShift(\""
								+ new String(pId) 
								+ "\")' ><i class=\"fa fa-check-circle\" title=\"Approved\" style=\"font-size:24px;color:#090\"></i></a>";
					
					}
				} else {
					s = s + "&nbsp;&nbsp;<a href='view-superAdmin-appraisal-form-FillUp?id=" + new String(pId) + "&fId="
							+ new String(fId)
							+ "'><i class='fa fa-files-o' title='Appraisal Form' style=\"font-size:20px\"></i></a>";
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectAppraisal(\""
							+ new String(pId)+','+ new String(fId) + "\",1)'><i class='fa fa-close'style=\"font-size:24px\"></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='resubmitAppraisal(\""
							+ new String(pId)+','+ new String(fId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					
				
				}
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(adminAppraisalForm);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewSuperAdminAppraisalFormThroughAjax ends");
		System.out.println("response" + response);
		return response;
	}

	/*
	 * for Second Stage Approval
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-superAdmin-appraisal-form-FillUp" })
	public String appraisalFormAdminApproval(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			@RequestParam("fId") String fromdate, Model model) {

		logger.info("Method :appraisalFormAdminApproval starts");
		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id1 = (new String(decodeId));
		String fDate = fromdate;
		byte[] decodeId2 = Base64.getDecoder().decode(fDate.getBytes());
		String fromDate = (new String(decodeId2));
		JsonResponse<List<HrmsAppraisalFormModel>> jsonresponse = new JsonResponse<List<HrmsAppraisalFormModel>>();
		try {
			jsonresponse = restClient.getForObject(
					env.getEmployeeUrl() + "appraisalFormApproval-admin?id=" + id1 + "&fromDate=" + fromDate,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<HrmsAppraisalFormModel> adminAppraisalForm = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<HrmsAppraisalFormModel>>() {
				});

		model.addAttribute("adminAppraisalForm", adminAppraisalForm);

		logger.info("Method :appraisalFormAdminApproval ends");
		System.out.println("adminAppraisalForm" + adminAppraisalForm);
		return "employee/add-SuperAdmin-Appraisal-Form";
	}

	/*
	 * for Second Stage Approval - Submit
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-superAdmin-appraisal-form-submit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> appraisalFormAdminSubmit(
			@RequestBody List<HrmsAppraisalFormModel> adminAppraisalForm, Model model, HttpSession session) {
		logger.info("Method : appraisalFormAdminSubmit  starts");
		System.out.println("adminAppraisalForm" + adminAppraisalForm);
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
			for (HrmsAppraisalFormModel r : adminAppraisalForm) {

				r.settCreatedBy(userId);
				r.settCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "submit-appraisal-form-admin", adminAppraisalForm,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : appraisalFormAdminSubmit  Ends");
		return res;

	}

	/*
	 * Appraisal status change
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-superAdmin-appraisal-form-change-status" })
	public @ResponseBody JsonResponse<Object> changeAppraisalStatus( @RequestParam("id") String id1,
			 HttpSession session) {
		System.out.println("id" + id1);
		logger.info("Method : changeAppraisalStatus starts");

		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());

		String index = (new String(encodeByte));
		System.out.println(index);
		// Boolean status = adminAppraisalForm.gettAppraisalFinalStatus();
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "changeAppraisalStatus?id=" + index,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :changeAppraisalStatus  ends");

		return resp;
	}
	/*
	 * Reject Appraisal-Super Admin
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-superAdmin-appraisal-form-reject-appraisal" })
	public @ResponseBody JsonResponse<Object> rejectAppraisalToManager( @RequestParam("id") String id1, @RequestParam("date") String fdate,
			 HttpSession session) {
		System.out.println("id" + id1);
		System.out.println("date" + fdate);
		logger.info("Method : rejectAppraisalToManager starts");

		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());
		String index = (new String(encodeByte));
		System.out.println(index);
		byte[] encodeByte1 = Base64.getDecoder().decode(fdate.getBytes());
		String fromDate = (new String(encodeByte1));
		System.out.println("fromDate"+fromDate);
		
		
		// Boolean status = adminAppraisalForm.gettAppraisalFinalStatus();
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "rejectAppraisalToManager?id=" + index+ "&fdate=" +fromDate,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :rejectAppraisalToManager  ends");

		return resp;
	}
	/*
	 * Resubmit Appraisal-Super Admin
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-superAdmin-appraisal-form-resubmit-appraisal" })
	public @ResponseBody JsonResponse<Object> resubmitAppraisalToManager( @RequestParam("id") String id1, @RequestParam("date") String fdate,
			 HttpSession session) {
		System.out.println("id" + id1);
		System.out.println("date" + fdate);
		logger.info("Method : resubmitAppraisalToManager starts");

		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());
		String index = (new String(encodeByte));
		System.out.println(index);
		byte[] encodeByte1 = Base64.getDecoder().decode(fdate.getBytes());
		String fromDate = (new String(encodeByte1));
		System.out.println("fromDate"+fromDate);
		
		
		// Boolean status = adminAppraisalForm.gettAppraisalFinalStatus();
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "resubmitAppraisal?id=" + index+ "&fdate=" +fromDate,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method :resubmitAppraisalToManager  ends");

		return resp;
	}


	/*************************************************************************************************************************************************/
	/****************************************
	 * WEB CONTROLLER FOR COMMON USER
	 ****************************************/
	/*************************************************************************************************************************************************/
	/*
	 * Get Mapping view Appraisal Form for Second Stage Approval
	 */

	@GetMapping("/view-common-appraisal-form")
	public String viewCommonAppraisalForm(Model model, HttpSession session) {

		logger.info("Method : viewCommonAppraisalForm  starts");

		logger.info("Method : viewCommonAppraisalForm  ends");

		return "employee/view-Common-Appraisal-Form";
	}

	/*
	 * view Appraisal Common Form  Details 'Datatable' call
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-common-appraisal-form-ThroughAjax")
	public @ResponseBody DataTableResponse viewCommonAppraisalFormThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, HttpSession session) {
		logger.info("Method : viewCommonAppraisalFormThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			//String userId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			//tableRequest.setUserId(userId);

			JsonResponse<List<HrmsAppraisalFormModel>> jsonResponse = new JsonResponse<List<HrmsAppraisalFormModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getCommonAppraisalForm", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsAppraisalFormModel> commonAppraisalForm = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsAppraisalFormModel>>() {
					});

			String s = "";

			for (HrmsAppraisalFormModel m : commonAppraisalForm) {

				String appId = Integer.toString(m.gettAppraisalSetupId());
				s = "";
				byte[] fId = Base64.getEncoder().encode(m.gettAppraisalFromDate().getBytes());
				byte[] pId = Base64.getEncoder().encode(appId.getBytes());
				
					s = s + "&nbsp;&nbsp;<a href='view-common-appraisal-form-FillUp?id=" + new String(pId) + "&fId="
							+ new String(fId)
							+ "'><i class='fa fa-files-o' title='Appraisal Form' style=\"font-size:20px\"></i></a>";
				
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(commonAppraisalForm);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewCommonAppraisalFormThroughAjax ends");
		System.out.println("response" + response);
		return response;
	}
	
	/*
	 * for Common Comments
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-common-appraisal-form-FillUp" })
	public String appraisalCommonForm(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			@RequestParam("fId") String fromdate, Model model) {

		logger.info("Method :appraisalCommonForm starts");
		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id1 = (new String(decodeId));
		String fDate = fromdate;
		byte[] decodeId2 = Base64.getDecoder().decode(fDate.getBytes());
		String fromDate = (new String(decodeId2));
		JsonResponse<List<HrmsAppraisalFormModel>> jsonresponse = new JsonResponse<List<HrmsAppraisalFormModel>>();
		try {
			jsonresponse = restClient.getForObject(
					env.getEmployeeUrl() + "appraisalCommonForm?id=" + id1 + "&fromDate=" + fromDate,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<HrmsAppraisalFormModel> commonAppraisalForm = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<HrmsAppraisalFormModel>>() {
				});

		model.addAttribute("commonAppraisalForm", commonAppraisalForm);

		logger.info("Method :appraisalCommonForm ends");
		System.out.println("adminAppraisalForm" + commonAppraisalForm);
		return "employee/add-common-Appraisal-Form";
	}
	/*
	 * for Common Comments - Submit
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-common-appraisal-form-submit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> appraisalFormCommonSubmit(
			@RequestBody List<HrmsAppraisalFormModel> commonAppraisalForm, Model model, HttpSession session) {
		logger.info("Method : appraisalFormCommonSubmit  starts");
		System.out.println("commonAppraisalForm" + commonAppraisalForm);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		
		
		try {
					res = restClient.postForObject(env.getEmployeeUrl() + "submit-appraisal-form-common", commonAppraisalForm,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : appraisalFormCommonSubmit  Ends");
		 System.out.println("response"+res);
		return res;

	}
	/*
	 * Super Admin Modal View
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-superAdmin-appraisal-form-modalView" })
	public @ResponseBody JsonResponse<List<HrmsAppraisalFormModel>> appraisalModalViewSuperAdmin(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method :appraisalModalViewSuperAdmin starts");

		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id1 = (new String(decodeId));

		JsonResponse<List<HrmsAppraisalFormModel>> response = new JsonResponse<List<HrmsAppraisalFormModel>>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "appraisalModalView-superAdmin?id=" + id1,
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
		logger.info("Method : appraisalModalViewSuperAdmin  ends ");
		return response;
	}

	
}
