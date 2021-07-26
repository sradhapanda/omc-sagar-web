package nirmalya.aathithya.webmodule.audit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditExecutiveModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
	@RequestMapping(value = "audit")
	public class AuditExecutiveController {
		Logger logger = LoggerFactory.getLogger(AuditExecutiveController.class);

		@Autowired
		RestTemplate restClient;

		@Autowired
		EnvironmentVaribles env;

		@GetMapping("/add-executive-summary")
		public String addAuditExecutive(Model model, HttpSession session) {
			logger.info("Method : addAuditExecutive function starts");
			AuditExecutiveModel executive = new AuditExecutiveModel();
			try {
				AuditExecutiveModel auditSession = (AuditExecutiveModel) session.getAttribute("executive");

				String message = (String) session.getAttribute("message");

				if (message != null && message != "") {
					model.addAttribute("message", message);

				}

				session.setAttribute("message", "");
				if (auditSession != null) {
					model.addAttribute("executiveAudit", auditSession);
					session.setAttribute("executive", null);
				} else {
					model.addAttribute("executive", executive);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			/*
			 * Financial year dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinanceYearDropdown", DropDownModel[].class);
				List<DropDownModel> auditFinanceYearList = Arrays.asList(audit);

				model.addAttribute("auditFinanceYearList", auditFinanceYearList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			

			/*
			 * department Dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getDepartmentDropdown", DropDownModel[].class);
				List<DropDownModel> departmentList = Arrays.asList(audit);

				model.addAttribute("departmentList", departmentList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			/*
			 * Audit Type dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditTypeDropdown", DropDownModel[].class);
				List<DropDownModel> auditTypeList = Arrays.asList(audit);

				model.addAttribute("auditTypeList", auditTypeList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			
			logger.info("Method : addAuditExecutive ends");
			return "audit/add-executive-summary";

		}
		
		@GetMapping("/view-executive-summary")
		public String viewAuditExecutive(Model model,@RequestParam("id") String id, HttpSession session){
			
			logger.info("Method : viewAuditExecutive function starts");
			
			byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
			String id1 = (new String(encodeByte));

			/*
			 * Financial year dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinanceYearDropdown", DropDownModel[].class);
				List<DropDownModel> auditFinanceYearList = Arrays.asList(audit);

				model.addAttribute("auditFinanceYearList", auditFinanceYearList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			

			/*
			 * department Dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getDepartmentDropdown", DropDownModel[].class);
				List<DropDownModel> departmentList = Arrays.asList(audit);

				model.addAttribute("departmentList", departmentList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			/*
			 * Audit Type dropdown
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditTypeDropdown", DropDownModel[].class);
				List<DropDownModel> auditTypeList = Arrays.asList(audit);

				model.addAttribute("auditTypeList", auditTypeList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			model.addAttribute("auditType",id1);
			logger.info("Method : viewAuditExecutive function ends");
			
			return "audit/view-executive-summary";
		}
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/add-executive-summary-ajax", method = { RequestMethod.POST })
		public @ResponseBody JsonResponse<Object> addExecutiveSummary(
				@RequestBody List<AuditExecutiveModel> auditExecutiveModel, Model model, HttpSession session) {
			JsonResponse<Object> res = new JsonResponse<Object>();
			logger.info("Method : addExecutiveSummary function starts");

			String userId = "";

			try {
				userId = (String) session.getAttribute("USER_ID");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {

				for (AuditExecutiveModel r : auditExecutiveModel) {

					r.setCreatedBy(userId);
					
				}

				res = restClient.postForObject(env.getAuditUrl() + "restAddAuditExecutive", auditExecutiveModel,
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			String message = res.getMessage();

			if (message != null && message != "") {

			} else {
				res.setMessage("Success");
			}
			logger.info("Method : addExecutiveSummary function Ends");
			return res;
		}

		@SuppressWarnings("unchecked")
		@GetMapping("/view-executive-summary-ThroughAjax")
		public @ResponseBody DataTableResponse viewExecutivejax(Model model, HttpServletRequest request,
				@RequestParam String param1,@RequestParam String param2,@RequestParam String param3) {

			logger.info("Method : viewExecutivejax method statrs");

			DataTableResponse response = new DataTableResponse();
			DataTableRequest tableRequest = new DataTableRequest();

			try {
				String start = request.getParameter("start");
				String length = request.getParameter("length");
				String draw = request.getParameter("draw");

				tableRequest.setStart(Integer.parseInt(start));
				tableRequest.setLength(Integer.parseInt(length));
				tableRequest.setParam1(param1);
				tableRequest.setParam2(param2);
				tableRequest.setParam3(param3);
				
				JsonResponse<List<AuditExecutiveModel>> jsonResponse = new JsonResponse<List<AuditExecutiveModel>>();

				jsonResponse = restClient.postForObject(env.getAuditUrl() + "getExecutiveDtls", tableRequest,
						JsonResponse.class);

				ObjectMapper mapper = new ObjectMapper();

				List<AuditExecutiveModel> auditExecutiveModel = mapper.convertValue(jsonResponse.getBody(),
						new TypeReference<List<AuditExecutiveModel>>() {
						});

				String s = "";
				int i=1;
				for (AuditExecutiveModel m : auditExecutiveModel) {
					s = "";
					byte[] encodeId = Base64.getEncoder().encode(m.getExecutiveId().getBytes());
					byte[] auditNo = Base64.getEncoder().encode(m.getAuditType().getBytes());
					s = "<a href='view-executive-summary-dtl?id=" + new String(auditNo)
							+ "' ><i class=\"fa fa-eye faIcon\" ></i></a>";
					if(m.getCheckListRefNo()!= null){
					if(m.getCheckListRefNo().equals("1")) {
						m.setCheckListRefNo("");
					}
					}
					m.setAction(s);
					s = "";
					m.setsNo(i);
					i++;
				}
			
				response.setRecordsTotal(jsonResponse.getTotal());
				response.setRecordsFiltered(jsonResponse.getTotal());
				response.setDraw(Integer.parseInt(draw));
				response.setData(auditExecutiveModel);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Method : viewExecutivejax method ends");

			return response;
		}
		@SuppressWarnings("unchecked")
		@GetMapping("/view-executive-summary-dtl")
		public String executiveSummary(Model model, @RequestParam("id") String encodeId, HttpSession session) {

			logger.info("Method :executiveSummary starts");
				
			
			
				byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
				
				String id = (new String(decodeId));
			
		
				AuditExecutiveModel[] executiveDtl = restClient.getForObject(env.getAuditUrl() + "getExecutiveDtlsList?id="+id, AuditExecutiveModel[].class);
				List<AuditExecutiveModel> executiveDtlList = Arrays.asList(executiveDtl);
				int i=1;
				for (AuditExecutiveModel m : executiveDtlList) {
					m.setsNo(i);
					i++;
				}
				
				model.addAttribute("executiveDtlList", executiveDtlList);

			
				
				
				AuditExecutiveModel[] executiveDtlCheckList = restClient.getForObject(env.getAuditUrl() + "getExecutiveDtlsListCheck?id="+id, AuditExecutiveModel[].class);
				List<AuditExecutiveModel> executiveDtlCheckLists = Arrays.asList(executiveDtlCheckList);
				int j=1;
				for (AuditExecutiveModel m : executiveDtlCheckLists) {
					m.setsNo(j);
					j++;
				}
				
				model.addAttribute("executiveDtlCheckLists", executiveDtlCheckLists);

			
			

			logger.info("Method : executiveSummary ends");

			return "audit/view-executive-summary-dtl";
		}

	}


