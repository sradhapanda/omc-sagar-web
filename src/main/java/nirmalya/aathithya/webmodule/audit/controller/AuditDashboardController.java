package nirmalya.aathithya.webmodule.audit.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.audit.model.AuditDashboardModel;
import nirmalya.aathithya.webmodule.audit.model.AuditNotificationModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller

public class AuditDashboardController {
	Logger logger = LoggerFactory.getLogger(AuditDashboardController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping View house keeping
	 */
	@GetMapping("/index-dashboard")
	public String dashboardCoordinator(HttpSession session, Model model) {

		logger.info("Method : dashboard starts");
		String userid="";
		userid=(String) session.getAttribute("USER_ID");
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditTypeDashboard",
					DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);
			model.addAttribute("auditTypeList1", auditTypeList);
			for (DropDownModel m : auditTypeList) {
				m.setDocName(m.getName());
				byte[] encodeId1 = Base64.getEncoder().encode(m.getKey().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.getName().getBytes());
				m.setKey(new String(encodeId1));
				m.setName(new String(encodeId2));

			}
			System.out.println("auditTypeList=="+auditTypeList);
			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditTypePie",
					DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);
			model.addAttribute("TypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		try {
			DropDownModel[] res = restClient.getForObject(env.getAuditUrl() + "/getTodaysCase?id="+userid, DropDownModel[].class);
			int count = 0;
			for (DropDownModel d : res) {
				count += Integer.parseInt(d.getKey());
			}
			model.addAttribute("todaysCase", count);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			@SuppressWarnings("unchecked")
			JsonResponse<Integer> res = restClient.getForObject(env.getAuditUrl() + "/getPendingCase?id="+userid,
					JsonResponse.class);
			model.addAttribute("todaysPendingCase", res.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditNoDash",
					AuditDashboardModel[].class);
			List<AuditDashboardModel> auditTypeNo = Arrays.asList(audit);
			model.addAttribute("auditTypeNo", auditTypeNo);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
		List<DropDownModel> financialYearList = Arrays.asList(audit);

		model.addAttribute("financialYearList", financialYearList);

		try {
			DropDownModel[] year = restClient.getForObject(env.getAuditUrl() + "getYearList", DropDownModel[].class);
			List<DropDownModel> yearList = Arrays.asList(year);

			model.addAttribute("yearList", yearList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] notification = restClient.getForObject(env.getAuditUrl() + "getNotification", DropDownModel[].class);
			List<DropDownModel> notificationList = Arrays.asList(notification);

			model.addAttribute("notificationList", notificationList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		logger.info("Method : dashboard ends");

		return "audit/coordinator-dashboard";
	}

	/*
	 * Get Mapping View house keeping
	 */
	@GetMapping("/index-dashboard-audit-link")
	public String auditLInk(Model model, @RequestParam("id") String id, @RequestParam("name") String name,
			HttpSession session) {

		logger.info("Method : auditLInk starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));

		try {
			DropDownModel[] res = restClient.getForObject(env.getAuditUrl() + "/getTodaysCaseById?id=" + id1,
					DropDownModel[].class);
			int count = 0;
			for (DropDownModel d : res) {
				count += Integer.parseInt(d.getKey());
			}
			model.addAttribute("todaysCase", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			@SuppressWarnings("unchecked")
			JsonResponse<Integer> res = restClient.getForObject(env.getAuditUrl() + "/getPendingCaseById?id=" + id1,
					JsonResponse.class);
			model.addAttribute("todaysPendingCase", res.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("auditType", id);
		model.addAttribute("auditTypeId", id1);
		model.addAttribute("auditTypeName", name);
		logger.info("Method : auditLInk ends");

		return "audit/auditLink";
	}

	/*
	 * Get Mapping View house keeping
	 */
	@GetMapping("/index-dashboard-supplementary-link")
	public String supplementaryLInk(HttpSession session, Model model) {

		logger.info("Method : supplementaryLInk starts");
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getSupplementaryTypeDashboard",
					DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);
			model.addAttribute("auditTypeList1", auditTypeList);
			for (DropDownModel m : auditTypeList) {
				m.setDocName(m.getName());
				byte[] encodeId1 = Base64.getEncoder().encode(m.getKey().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.getName().getBytes());
				m.setKey(new String(encodeId1));
				m.setName(new String(encodeId2));

			}
			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		DropDownModel[] year = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
		List<DropDownModel> financialYearList = Arrays.asList(year);

		model.addAttribute("financialYearList", financialYearList);

		try {
			AuditDashboardModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditNoDash",
					AuditDashboardModel[].class);
			List<AuditDashboardModel> auditTypeNo = Arrays.asList(audit);
			model.addAttribute("auditTypeNo", auditTypeNo);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditTypeCagPie",
					DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);
			model.addAttribute("TypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] res = restClient.getForObject(env.getAuditUrl() + "/getTodaysCaseById?id=" + "3",
					DropDownModel[].class);
			int count = 0;
			for (DropDownModel d : res) {
				count += Integer.parseInt(d.getKey());
			}
			model.addAttribute("todaysCase", count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : supplementaryLInk ends");

		return "audit/supplementaryType";
	}

	/*
	 * Get Mapping View house keeping
	 */
	@GetMapping("/auditor-dashboard")
	public String dashboardAuditor(HttpSession session, Model model) {

		logger.info("Method : dashboardAuditor starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardAuditor?userId=" + userId, AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			if (!dasboardDataList.isEmpty()) {
				model.addAttribute("Improvement", dasboardDataList.get(0).getTotalImprovement());
				model.addAttribute("POM", dasboardDataList.get(0).getTotalPom());
				model.addAttribute("NC", dasboardDataList.get(0).getTotalNc());
				model.addAttribute("Opened", dasboardDataList.get(0).getTotalOpen());
				model.addAttribute("Pending", dasboardDataList.get(0).getTotalPending());
			} else {
				model.addAttribute("Improvement", 0);
				model.addAttribute("POM", 0);
				model.addAttribute("NC", 0);
				model.addAttribute("Opened", 0);
				model.addAttribute("Pending", 0);
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}
		logger.info("Method : dashboardAuditor ends");

		return "audit/auditor-dashboard";
	}

	String currDate = "2020";

	/*
	 * Get Mapping View house keeping
	 */
	@GetMapping("/auditee-dashboard")
	public String dashboardAuditee(HttpSession session, Model model) {

		logger.info("Method : dashboardAuditee starts");

		String userId = "";
		String financialYear = "2020";
		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardAuditee?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			if (!dasboardDataList.isEmpty()) {
				model.addAttribute("Improvement", dasboardDataList.get(0).getTotalImprovement());
				model.addAttribute("POM", dasboardDataList.get(0).getTotalPom());
				model.addAttribute("NC", dasboardDataList.get(0).getTotalNc());
				model.addAttribute("Opened", dasboardDataList.get(0).getTotalOpen());
				model.addAttribute("Pending", dasboardDataList.get(0).getTotalPending());
				model.addAttribute("auditSectionStatus", dasboardDataList.get(0).getAuditSectionStatus());
				model.addAttribute("replied", dasboardDataList.get(0).getReplied());
				model.addAttribute("repliedConcernAudit", dasboardDataList.get(0).getRepliedByConcernAudit());
				model.addAttribute("pendingReply", dasboardDataList.get(0).getPendingReply());

			} else {
				model.addAttribute("Improvement", 0);
				model.addAttribute("POM", 0);
				model.addAttribute("NC", 0);
				model.addAttribute("Opened", 0);
				model.addAttribute("Pending", 0);
				model.addAttribute("auditSectionStatus", 0);
				model.addAttribute("replied", 0);
				model.addAttribute("repliedConcernAudit", 0);
				model.addAttribute("pendingReply", 0);
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardTransAuditee?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			if (!dasboardDataList.isEmpty()) {
				model.addAttribute("TraImprovement", dasboardDataList.get(0).getTotalImprovement());
				model.addAttribute("TraPOM", dasboardDataList.get(0).getTotalPom());
				model.addAttribute("TraNC", dasboardDataList.get(0).getTotalNc());
				model.addAttribute("TraOpened", dasboardDataList.get(0).getTotalOpen());
				model.addAttribute("TraPending", dasboardDataList.get(0).getTotalPending());
				model.addAttribute("TraauditSectionStatus", dasboardDataList.get(0).getAuditSectionStatus());
				model.addAttribute("Trareplied", dasboardDataList.get(0).getReplied());
				model.addAttribute("TrarepliedConcernAudit", dasboardDataList.get(0).getRepliedByConcernAudit());
				model.addAttribute("TrapendingReply", dasboardDataList.get(0).getPendingReply());

			} else {
				model.addAttribute("TraImprovement", 0);
				model.addAttribute("TraPOM", 0);
				model.addAttribute("TraNC", 0);
				model.addAttribute("TraOpened", 0);
				model.addAttribute("TraPending", 0);
				model.addAttribute("TraauditSectionStatus", 0);
				model.addAttribute("Trareplied", 0);
				model.addAttribute("TrarepliedConcernAudit", 0);
				model.addAttribute("TrapendingReply", 0);
			}

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		// supplementary audit

		try {
			DropDownModel[] year = restClient.getForObject(env.getAuditUrl() + "getYearList", DropDownModel[].class);
			List<DropDownModel> yearList = Arrays.asList(year);

			model.addAttribute("yearList", yearList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : dashboardAuditee ends");

		return "audit/auditee-dashboard";
	}

	@GetMapping(value = { "auditee-dashboard-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getPieDataAuditee(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getPieDataAuditee starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardAuditee?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditor ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-trans-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getDognoutDataDashboard(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getDognoutDataDashboard starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardTransactionData = restClient.getForObject(env.getAuditUrl()
					+ "getDashboardTransCoordinator?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardTransDataList = Arrays.asList(dasboardTransactionData);
			res.setBody(dasboardTransDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getDognoutDataDashboard ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-bar-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getBarDataDashboard(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getBarDataDashboard starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardTransactionData = restClient.getForObject(env.getAuditUrl()
					+ "getDashboardTransCoordinator?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardTransDataList = Arrays.asList(dasboardTransactionData);
			res.setBody(dasboardTransDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getDognoutDataDashboard ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getPieData(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getPieData starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardCoordinator?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditor ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-get-bar-from-pie-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getPieBarData(Model model,
			@RequestParam("label") String label, @RequestParam("financialYear") String financialYear,
			@RequestParam("type") String type, HttpSession session) {
		logger.info("Method : getPieBarData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient
					.getForObject(
							env.getAuditUrl() + "getDashboardBarFromPieCoordinator?userId=" + userId + "&label=" + label
									+ "&financialYear=" + financialYear + "&auditType=" + type,
							AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			// JsonResponse<List<AuditDashboardModel>> resp = new
			// JsonResponse<List<AuditDashboardModel>>();

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditor ends");
		System.out.println("barchart" + res);
		return res;
	}

	@GetMapping(value = { "index-dashboard-get-bar-from-pie-trans-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getPieBarTransData(Model model,
			@RequestParam("label") String label, @RequestParam("financialYear") String financialYear,
			HttpSession session) {
		logger.info("Method : getPieBarData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient
					.getForObject(env.getAuditUrl() + "getDashboardBarFromPieTransCoordinator?userId=" + userId
							+ "&label=" + label + "&financialYear=" + financialYear, AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			// JsonResponse<List<AuditDashboardModel>> resp = new
			// JsonResponse<List<AuditDashboardModel>>();

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getPieBarData ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-get-bar-from-bar-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getBarFromBarData(Model model,
			@RequestParam("label") String label, @RequestParam("financialYear") String financialYear,
			HttpSession session) {
		logger.info("Method : getBarFromBarData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient
					.getForObject(env.getAuditUrl() + "getDashboardBarFromBarCoordinator?userId=" + userId + "&label="
							+ label + "&financialYear=" + financialYear, AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			// JsonResponse<List<AuditDashboardModel>> resp = new
			// JsonResponse<List<AuditDashboardModel>>();

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditor ends");
		return res;
	}

	@GetMapping(value = { "auditee-dashboard-get-bar-from-pie-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getAPieBarData(Model model,
			@RequestParam("label") String label, @RequestParam("financialYear") String financialYear,
			HttpSession session) {
		logger.info("Method : getAuditeePieBarData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient
					.getForObject(env.getAuditUrl() + "getDashboardAuditeeBarFromPieCoordinator?userId=" + userId
							+ "&label=" + label + "&financialYear=" + financialYear, AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditeePieBarData ends");
		return res;
	}

	@GetMapping(value = { "auditee-dashboard-trans-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getAuditeeDashboard(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getAuditeeDashboard starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardTransactionData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardTransAuditee?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardTransDataList = Arrays.asList(dasboardTransactionData);
			res.setBody(dasboardTransDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditeeDashboard ends");
		return res;
	}

	@GetMapping(value = { "auditee-dashboard-trans-get-bar-from-pie-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getTransPieBarData(Model model,
			@RequestParam("label") String label, @RequestParam("financialYear") String financialYear,
			HttpSession session) {
		logger.info("Method : getTransPieBarData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient
					.getForObject(
							env.getAuditUrl() + "getDashboardAuditeeTransBarFromPieCoordinator?userId=" + userId
									+ "&label=" + label + "&financialYear=" + financialYear,
							AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : getTransPieBarData ends");
		return res;
	}

	@GetMapping(value = { "auditee-dashboard-bar-throughAjax" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getAuditeeBarDataDashboard(Model model,
			@RequestParam("financialYear") String financialYear, HttpSession session) {
		logger.info("Method : getAuditeeBarDataDashboard starts");
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(
					env.getAuditUrl() + "getDashboardAuditee?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);
			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getAuditeeBarDataDashboard ends");
		return res;
	}

	/*
	 * getSuplAuditDashData
	 */
	@GetMapping("/index-dashboard-audit-getSuplAuditDashData")
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getSuplAuditDashData(
			@RequestParam String financialYear, HttpSession session) {

		logger.info("Method : getSuplAuditDashData starts");
		String userId = (String) session.getAttribute("USER_ID");
		JsonResponse<List<AuditDashboardModel>> resp = new JsonResponse<List<AuditDashboardModel>>();
		try {
			AuditDashboardModel[] dasboardTransactionData = restClient.getForObject(
					env.getAuditUrl() + "getSuplAuditDashData?userId=" + userId + "&financialYear=" + financialYear,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardTransDataList = Arrays.asList(dasboardTransactionData);
			resp.setBody(dasboardTransDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getSuplAuditDashData ends");

		return resp;
	}

	/*
	 * getMonthlyInitData
	 */
	@GetMapping("/index-dashboard-audit-InitData")
	public @ResponseBody JsonResponse<Object> getMonthlyInitData(@RequestParam String financialYear,
			HttpSession session) {

		logger.info("Method : getMonthlyInitData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			DropDownModel[] MonthlyInitData = restClient.getForObject(
					env.getAuditUrl() + "getMonthlyInitData?financialYear=" + financialYear, DropDownModel[].class);
			// List<DropDownModel> MonthlyInitDataList = Arrays.asList(MonthlyInitData);
			resp.setBody(MonthlyInitData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getMonthlyInitData ends");

		return resp;
	}

	/*
	 * getMonthlyClosedData
	 */
	@GetMapping("/index-dashboard-audit-getMonthlyClosedData")
	public @ResponseBody JsonResponse<Object> getMonthlyClosedData(@RequestParam String financialYear,
			HttpSession session) {

		logger.info("Method : getMonthlyClosedDataList starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			DropDownModel[] MonthlyClosedData = restClient.getForObject(
					env.getAuditUrl() + "getMonthlyClosedData?financialYear=" + financialYear, DropDownModel[].class);
			// List<DropDownModel> MonthlyClosedDataList = Arrays.asList(MonthlyClosedData);
			resp.setBody(MonthlyClosedData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getMonthlyClosedDataList ends");

		return resp;
	}

	/*
	 * getAuditTypes
	 */
	@GetMapping("/index-dashboard-audit-getAuditTypes")
	public @ResponseBody JsonResponse<Object> getAuditTypes(HttpSession session) {

		logger.info("Method : getAuditTypes starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			DropDownModel[] auditTypes = restClient.getForObject(env.getAuditUrl() + "getAuditTypes",
					DropDownModel[].class);
			// List<DropDownModel> MonthlyClosedDataList = Arrays.asList(MonthlyClosedData);
			resp.setBody(auditTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getAuditTypes ends");

		return resp;
	}

	/*
	 * getInitData
	 */
	@GetMapping("/index-dashboard-audit-repliedData")
	public @ResponseBody JsonResponse<Object> getrepliedData(@RequestParam String financialYear, HttpSession session) {

		logger.info("Method : getrepliedData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			DropDownModel[] MonthlyInitData = restClient.getForObject(
					env.getAuditUrl() + "getRepliedData?financialYear=" + financialYear, DropDownModel[].class);
			// List<DropDownModel> MonthlyInitDataList = Arrays.asList(MonthlyInitData);
			resp.setBody(MonthlyInitData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getrepliedData ends");

		return resp;
	}

	/*
	 * getInitData
	 */
	@GetMapping("/index-dashboard-audit-pendingData")
	public @ResponseBody JsonResponse<Object> pendingData(@RequestParam String financialYear, HttpSession session) {

		logger.info("Method : pendingData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			DropDownModel[] MonthlyInitData = restClient.getForObject(
					env.getAuditUrl() + "getPendingData?financialYear=" + financialYear, DropDownModel[].class);
			// List<DropDownModel> MonthlyInitDataList = Arrays.asList(MonthlyInitData);
			resp.setBody(MonthlyInitData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : pendingData ends");

		return resp;
	}

	/*
	 * getPieData
	 */
	@GetMapping("/index-dashboard-audit-pieData")
	public @ResponseBody JsonResponse<Object> getPieData(@RequestParam String financialYear, @RequestParam String type,
			@RequestParam String auditType, @RequestParam String auditor, HttpSession session) {

		logger.info("Method : getPieData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] MonthlyInitData = restClient.getForObject(env.getAuditUrl()
					+ "getPieData?financialYear=" + financialYear + "&type=" + type + "&auditType=" + auditType+"&userId="+userId
					+ "&auditor=" + auditor,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> MonthlyInitDataList = Arrays.asList(MonthlyInitData);
			//System.out.println("total opens in chat"+MonthlyInitDataList.get(0).getTotalOpen()); 
			resp.setBody(MonthlyInitDataList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getPieData ends");
		System.out.println("bikash xyz "+" financialYear "+financialYear+" type "+type+" auditType "+auditType+" auditor "+auditor+" resp "+resp);
		return resp;
	}

	/*
	 * getPieToBarData
	 */
	@GetMapping("/index-dashboard-audit-pieToBarData")
	public @ResponseBody JsonResponse<Object> getPieToBarData(@RequestParam String financialYear,
			@RequestParam String type, @RequestParam String label, HttpSession session) {

		logger.info("Method : getPieToBarData starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			AuditDashboardModel[] MonthlyInitData = restClient.getForObject(env.getAuditUrl()
					+ "getPieToBarData?financialYear=" + financialYear + "&type=" + type + "&label=" + label,
					AuditDashboardModel[].class);
			// List<DropDownModel> MonthlyInitDataList = Arrays.asList(MonthlyInitData);
			resp.setBody(MonthlyInitData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : getPieToBarData ends");

		return resp;
	}

	@GetMapping(value = { "index-dashboard-get-tableData" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getTableData(Model model,
			@RequestParam("label") String action, @RequestParam("financialYear") String financialYear,
			@RequestParam("type") String type, HttpSession session) {
		logger.info("Method : getTableData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(env.getAuditUrl() + "getTableData?userId="
					+ userId + "&label=" + action + "&financialYear=" + financialYear + "&auditType=" + type,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getTableData ends");
		return res;
	}

	@GetMapping(value = { "index-dashboard-audit-getModalData" })
	public @ResponseBody JsonResponse<List<AuditDashboardModel>> getModalData(Model model,
			@RequestParam("financialYear") String financialYear, @RequestParam("type") String type,
			@RequestParam("status") String status, @RequestParam("auditId") String auditId,
			@RequestParam("section") String section, @RequestParam("department") String department,
			@RequestParam("label") String label, HttpSession session) {
		logger.info("Method : getModalData starts");
		if (financialYear == "") {
			LocalDate.now().getYear();
		}
		JsonResponse<List<AuditDashboardModel>> res = new JsonResponse<List<AuditDashboardModel>>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AuditDashboardModel[] dasboardData = restClient.getForObject(env.getAuditUrl()
					+ "getModalData?financialYear=" + financialYear + "&type=" + type + "&status=" + status
					+ "&auditId=" + auditId + "&section=" + section + "&department=" + department + "&label=" + label+"&userId="+userId,
					AuditDashboardModel[].class);
			List<AuditDashboardModel> dasboardDataList = Arrays.asList(dasboardData);

			res.setBody(dasboardDataList);

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
			}
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		logger.info("Method : getModalData ends");
		System.out.println(res);
		return res;
	}
	

}
