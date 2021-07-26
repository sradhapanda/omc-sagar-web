package nirmalya.aathithya.webmodule.employee.controller;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
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
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.employee.model.EmployeePayrollMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class EmployeePayrollMasterController {
	Logger logger = LoggerFactory.getLogger(EmployeePayrollMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	/*
	 * GetMapping for Add department view page
	 */
	@GetMapping("/view-payroll-master")

	public String viewPayrollMaster(Model model, HttpSession session) {

		logger.info("Method : viewPayrollMaster starts");

		logger.info("Method : viewPayrollMaster ends");

		return "employee/view-payroll-mstr";
	}
	/*
	 * For view goal master for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-payroll-master-ThroughAjax")
	/*
	 * public @ResponseBody DataTableResponse viewPayrollMasterAjax(Model model,
	 * HttpServletRequest request,
	 * 
	 * @RequestParam String param1) {
	 */

	public @ResponseBody JsonResponse<List<EmployeePayrollMasterModel>> getProductionPlannaingDetails(
			@RequestParam String param1, HttpServletRequest request) {

		logger.info("Method : viewPayrollMasterAjax statrs");

		DataTableResponse response = new DataTableResponse();
		//// DataTableRequest tableRequest = new DataTableRequest();
		JsonResponse<List<EmployeePayrollMasterModel>> res = new JsonResponse<List<EmployeePayrollMasterModel>>();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			res = restClient.getForObject(env.getEmployeeUrl() + "getEmployeePayRollDetails?param1=" + param1,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<EmployeePayrollMasterModel> payrollMaster = mapper.convertValue(res.getBody(),
					new TypeReference<List<EmployeePayrollMasterModel>>() {
					});
			String s = "";
			for (EmployeePayrollMasterModel m : payrollMaster) {
				/*
				 * String name=""; name=m.gettEmployeeName();
				 */
				/*
				 * byte[] encodeId =
				 * Base64.getEncoder().encode(m.gettEmployeeId().getBytes());
				 * 
				 * m.
				 * settEmployeeId("<a href='javascript:void' onclick='viewInModel(\""
				 * + new String(encodeId) + "\")'>" + m.gettEmployeeId() +
				 * "</a>");
				 */

				Integer day = m.getLastDay();
				Integer noLeave = 0;
				double ctc = m.gettCTC();
				Double netSalary = 0.0;

				if (day == 30) {
					double daily = (ctc / 12) / day;
					BigDecimal leaveDay = m.gettLeaveDays();
					noLeave = day - leaveDay.intValue();
					netSalary = (double) Math.round(noLeave * daily);
				} else if (day == 31) {
					double daily = (ctc / 12) / day;
					BigDecimal leaveDay = m.gettLeaveDays();
					noLeave = day - leaveDay.intValue();
					netSalary = (double) Math.round(noLeave * daily);

				} else if (day == 29) {
					double daily = (ctc / 12) / day;
					BigDecimal leaveDay = m.gettLeaveDays();
					noLeave = day - leaveDay.intValue();
					netSalary = (double) Math.round(noLeave * daily);

				} else {
					double daily = (ctc / 12) / day;
					BigDecimal leaveDay = m.gettLeaveDays();
					noLeave = day - leaveDay.intValue();
					netSalary = (double) Math.round(noLeave * daily);

				}

				m.settNetSalary(netSalary);

				if (m.gettStatus() == null) {
					m.setStatus("Unpaid");
				}
				if (m.gettPayDate() == null) {
					m.settPayDate("--");
				}
				res.setBody(payrollMaster);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : viewPayrollMasterAjax Theme ends");

		return res;
	}

	/*
	 * For Offer Letter Details Modal View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payroll-master-modalView" })

	public @ResponseBody JsonResponse<Object> viewEmployeePayrollDetailsModal(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : viewEmployeePayrollDetailsModal starts");
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(decodeId));

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "employeePayrollDetailsModal?id=" + id,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		List<EmployeePayrollMasterModel> payrollMaster = mapper.convertValue(res.getBody(),
				new TypeReference<List<EmployeePayrollMasterModel>>() {
				});

		for (EmployeePayrollMasterModel m : payrollMaster) {

			Integer day = m.getLastDay();
			Integer noLeave = 0;
			double ctc = m.gettCTC();
			Double netSalary = 0.0;

			if (day == 30) {
				double daily = (ctc / 12) / day;
				BigDecimal leaveDay = m.gettLeaveDays();
				noLeave = day - leaveDay.intValue();
				netSalary = (double) Math.round(noLeave * daily);
			} else if (day == 31) {
				double daily = (ctc / 12) / day;
				BigDecimal leaveDay = m.gettLeaveDays();
				noLeave = day - leaveDay.intValue();
				netSalary = (double) Math.round(noLeave * daily);

			} else if (day == 29) {
				double daily = (ctc / 12) / day;
				BigDecimal leaveDay = m.gettLeaveDays();
				noLeave = day - leaveDay.intValue();
				netSalary = (double) Math.round(noLeave * daily);

			} else {
				double daily = (ctc / 12) / day;
				BigDecimal leaveDay = m.gettLeaveDays();
				noLeave = day - leaveDay.intValue();
				netSalary = (double) Math.round(noLeave * daily);

			}

			m.settNetSalary(netSalary);

		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		res.setBody(payrollMaster);
		logger.info("Method : viewEmployeePayrollDetailsModal ends");
		// System.out.println(" res++++++++++" + res);

		return res;
	}

	/*
	 * Post Mapping for adding new department
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-payroll-master-generate-payroll", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> addEmployeePayRollDetails(
			@RequestBody EmployeePayrollMasterModel payrollMaster, Model model, HttpSession session) {
		logger.info("Method : addEmployeePayRollDetails  starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { for (EmployeePayrollMasterModel r : payrollMaster) {
		 * 
		 * r.settCreatedBy(userId);
		 * 
		 * }
		 */
		res = restClient.postForObject(env.getEmployeeUrl() + "restAddEmployeePayRoll", payrollMaster,
				JsonResponse.class);
		/*
		 * } catch (RestClientException e) { e.printStackTrace(); }
		 */

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addEmployeePayRollDetails  Ends");

		return res;
	}

	/*
	 * Get Mapping view Goal Master
	 */
	@GetMapping("/view-payroll-master-details")
	public String viewPayRoleMasterDetails(Model model, HttpSession session) {

		logger.info("Method : viewPayRoleMasterDetails starts");

		logger.info("Method : viewPayRoleMasterDetails ends");

		return "employee/view-payroll-mstr-details";
	}

	/*
	 * For view goal master for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-payroll-master-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewPayRoleMasterDetailsAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewPayRoleMasterDetailsAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<EmployeePayrollMasterModel>> jsonResponse = new JsonResponse<List<EmployeePayrollMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getPayrollMasterDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<EmployeePayrollMasterModel> payrollMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<EmployeePayrollMasterModel>>() {
					});

			String s = "";

			for (EmployeePayrollMasterModel m : payrollMaster) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.gettId().getBytes());
				// byte[] stageId =
				// Base64.getEncoder().encode(m.getCurrentStageNo().toString().getBytes());

				s = s + "<a href='view-goal-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteGoalMaster(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
				if (m.gettStatus()) {
					m.setStatus("Paid");
				} else {
					m.setStatus("Unpaid");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(payrollMaster);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewPayRoleMasterDetailsAjax Theme ends");

		return response;
	}

	/**************************************************************************************************************/
	/*****************************************
	 * APPROVAL PROCESS
	 ****************************************************/
	/**************************************************************************************************************/
	/*
	 * 
	 * GetMapping For Listing ItemRequisition pending for apporve by approver
	 * 
	 * 
	 */
	@GetMapping(value = { "view-payroll-master-approval" })
	public String viewPayrollApproval(Model model) {
		logger.info("Method : viewPayrollApproval starts");
		JsonResponse<Object> payrollMaster = new JsonResponse<Object>();
		model.addAttribute("payrollMaster", payrollMaster);
		logger.info("Method : viewPayrollApproval ends");
		return "employee/approvePayrollMaster";
	}

	/*
	 * view Item throughAjax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-payroll-master-approval-list-throughAjax" })
	public @ResponseBody DataTableResponse viewPayrollApprovalListThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {
		logger.info("Method : viewPayrollApprovalListThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String UserId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			tableRequest.setUserId(UserId);

			JsonResponse<List<EmployeePayrollMasterModel>> jsonResponse = new JsonResponse<List<EmployeePayrollMasterModel>>();
			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "get-all-payroll-approve", tableRequest,
					JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<EmployeePayrollMasterModel> payrollMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<EmployeePayrollMasterModel>>() {
					});
			String s = "";

			for (EmployeePayrollMasterModel m : payrollMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettId().getBytes());
				byte[] eId = Base64.getEncoder().encode(m.gettEmployeeId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";

				if ((m.getCurrentStageNo() == m.getApproverStageNo()) && (m.getApproveStatus() != 1)) {
					if (m.getApproveStatus() != 3) {
						s = s + " &nbsp;&nbsp <a title='forward' href='javascript:void(0)' onclick='forwardPayRoll(\""
								+ new String(pId) + "\")'><i class='fa fa-forward'></i></a> &nbsp;&nbsp; ";
					} else {
						s = s + " &nbsp;&nbsp <a title='resubmit' href='javascript:void(0)' onclick='rejectRequisition(\""
								+ new String(pId) + "\",3)'><i class='fa fa-send'></i></a> &nbsp;&nbsp; ";
					}
					s = s + " &nbsp;&nbsp <a title='reject' href='javascript:void(0)' onclick='rejectRequisition(\""
							+ new String(pId) + "\",1)'><i class='fa fa-close'></i></a> &nbsp;&nbsp; ";
					s = s + " &nbsp;&nbsp <a title='return' href='javascript:void(0)' onclick='rejectRequisition(\""
							+ new String(pId) + "\",2)'><i class='fa fa-undo'></i></a> &nbsp;&nbsp; ";
				}
				if (m.getCurrentStageNo() == 3) {
					s = s + "<a title='create PDF' href='javascript:void' onclick='pdfCreate(\"" + new String(eId)
							+ "\")'><i class='fa fa-download'></i></a>";
				}
				m.setAction(s);
				s = "";

				if (m.gettStatus()) {
					m.setStatus("Paid");
				} else {
					m.setStatus("Unpaid");
				}
				// System.out.println("stage" + m.getCurrentStageNo());
				// System.out.println("approvestatu"+m.getApproveStatus());
				if (m.getApproveStatus() == 3)
					m.setApproveStatusName("Returned");
				else if (m.getApproveStatus() == 1)
					m.setApproveStatusName("Approved");
				else if (m.getApproveStatus() == 2)
					m.setApproveStatusName("Rejected");
				else
					m.setApproveStatusName("Open");
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(payrollMaster);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : viewPayrollApprovalListThroughAjax ends");
		return response;
	}

	/*
	 * Forward Requisition to next level of a stage
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-payroll-master-approval-save-action" })
	public @ResponseBody JsonResponse<Object> savePayrollApprovalAction(Model model,
			@RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : savePayrollApprovalAction starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "save-payroll-approval-action?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}

		logger.info("Method : savePayrollApprovalAction ends");
		return resp;
	}
	/*
	 * Reject Requisition
	 * 
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-payroll-master-approval-reject-action" })
	public @ResponseBody JsonResponse<Object> savePayrollRejectAction(Model model,
			@RequestBody EmployeePayrollMasterModel reqobject, BindingResult result, HttpSession session) {
		logger.info("Method : savePayrollRejectAction starts");

		byte[] encodeByte = Base64.getDecoder().decode(reqobject.gettId());
		String reqstnId = (new String(encodeByte));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;

		reqobject.settEmployeeName(userId);
		reqobject.settId(reqstnId);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getEmployeeUrl() + "save-payroll-reject-action", reqobject,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : savePayrollRejectAction ends");
		return res;
	}
	// PDF

	@SuppressWarnings("unchecked")
	@GetMapping("view-payroll-master-approval-EmpPDF")
	public void generateOneTaskAssignedPdf(HttpServletResponse response, Model model,
			@RequestParam("id") String encodeId) {
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		JsonResponse<EmployeePayrollMasterModel> jsonResponse = new JsonResponse<EmployeePayrollMasterModel>();
		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "get-one-emp-forPDF?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		EmployeePayrollMasterModel oneEmpPDF = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<EmployeePayrollMasterModel>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		String variable = env.getBaseURL();

		String name = oneEmpPDF.gettEmployeeName();
		String grade = oneEmpPDF.gettPayGrade();
		String city = oneEmpPDF.gettCity();
		String mobile = oneEmpPDF.gettMobile();
		String email = oneEmpPDF.gettEmailId();
		Boolean status = oneEmpPDF.gettStatus();
		Double salary = oneEmpPDF.gettNetSalary();

		data.put("oneEmpPDF", oneEmpPDF);
		data.put("name", name);
		data.put("grade", grade);
		data.put("city", city);
		data.put("mobile", mobile);
		data.put("email", email);
		data.put("status", status);
		data.put("salary", salary);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=EmployeePayroll.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("employee/EmployeePayrollPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * View selected ItemRequisition in Modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payroll-master-approval-modal" })
	public @ResponseBody JsonResponse<EmployeePayrollMasterModel> modalPayrollApprove(Model model,
			@RequestBody String index, BindingResult result) {

		logger.info("Method : modalPayrollApprove starts");

		JsonResponse<EmployeePayrollMasterModel> res = new JsonResponse<EmployeePayrollMasterModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getPayrollById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modalPayrollApprove ends");
		return res;
	}

}
