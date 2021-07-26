package nirmalya.aathithya.webmodule.audit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.audit.model.AuditHistoryModel;
import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;

@Controller
@RequestMapping(value = "audit")
public class AuditHistoryController {
	Logger logger = LoggerFactory.getLogger(AuditHistoryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/*
	 * Get Mapping auditHistory
	 */
	@GetMapping("/view-audit-history")
	public String viewhistory(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewhistory starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		/**
		 * AUDIT TYPE DROP DOWN
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditNos", DropDownModel[].class);
			List<DropDownModel> auditNoList = Arrays.asList(audit);
			
			model.addAttribute("auditNoList", auditNoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
			List<DropDownModel>financialYearList = Arrays.asList(audit);
			
			model.addAttribute("financialYearList", financialYearList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getDepartmentListSerach", DropDownModel[].class);
			List<DropDownModel>departmentList = Arrays.asList(audit);
			
			model.addAttribute("departmentList", departmentList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		model.addAttribute("auditType", id1);
		logger.info("Method : viewhistory ends");

		return "audit/auditHistoryReportView";
	}
	/*
	 * For view work type for dataTable Ajax call
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-history-ThroughAjax")
	public @ResponseBody DataTableResponse viewInitaiedAuditThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String param1,@RequestParam("param2") String param2,
			@RequestParam("param7") String param7,@RequestParam("param4") String param4,@RequestParam("param5") String param5,
			@RequestParam("param6") String param6, HttpSession session) {

		logger.info("Method : viewInitaiedAuditThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		List<String> role = null;
		try {
			role = (List<String>) session.getAttribute("USER_ROLES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//List<Integer> list = Arrays.asList(1, 2, 3);
        String delim = ",";
 
        String userrole = Joiner.on(delim).join(role);
		
        System.out.println("userrole12345 "+userrole);
        
		
		try {
			List<String> userRole = (List<String>) session.getAttribute("USER_ROLES");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			String userId = (String) session.getAttribute("USER_ID");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param2);
			tableRequest.setParam2(param1);
			tableRequest.setParam3(param7);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);
			tableRequest.setParam8(userrole);
			tableRequest.setUserId(userId);


			JsonResponse<List<AuditInitiateModel>> jsonResponse = new JsonResponse<List<AuditInitiateModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllInitiatedAudits", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditInitiateModel> auditInitiateModelList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditInitiateModel>>() {
					});

			String s = "";

			for (AuditInitiateModel m : auditInitiateModelList) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getAuditInitiate().getBytes());
				byte[] auditType = Base64.getEncoder().encode(m.getAuditType().getBytes());
				byte[] initiatedDate = Base64.getEncoder().encode(m.getInitiatedDate().getBytes());
				m.setStatus("Open");
			
				m.setAuditInitiate("<a title='view Report' href='view-audit-history-drillDown?param1=" + new String(encodeId)
						+ "' >"+m.getAuditInitiate()+"</a>");
			
			
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(auditInitiateModelList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewInitaiedAuditThroughAjax Theme ends");

		return response;
	}
	/*
	 * Get Mapping auditHistory
	 */
	@GetMapping("/view-audit-history-drillDown")
	public String viewhistoryDrillDown(Model model, @RequestParam String param1,HttpSession session) {

		logger.info("Method : viewhistoryDrillDown starts");
		byte[] encodeByte = Base64.getDecoder().decode(param1.getBytes());
		String id = (new String(encodeByte));
		
		
		logger.info("Method : viewhistoryDrillDown ends");
		model.addAttribute("param1",id);
		return "audit/view-audit-history";
	}

	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-history-ThroughAjaxData")
	public @ResponseBody DataTableResponse auditHistory(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : auditHistory ajax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			

			JsonResponse<List<AuditHistoryModel>> jsonResponse = new JsonResponse<List<AuditHistoryModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl()+ "getAuditHistoryDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditHistoryModel> history = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditHistoryModel>>() {
					});

			String s = "";
			Integer i=1;
			for(AuditHistoryModel m: history) {
				String[] time = m.getCreatedOn().split(" ");
				m.setCreatedOn(time[0]);
				m.setTime(time[1].split(":")[0] + ":" + time[1].split(":")[1]);
				
				//m.setSlNo(i.toString());
				//i++;
			}
	

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(history);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : auditHistory ajax  ends");
		return response;
	}
	
	/*@GetMapping("audit-history-report")
	public String generateAuditReport(Model model, HttpSession session) {

		logger.info("Method : generateAuditReport starts");
		*//**
		 * AUDIT TYPE DROP DOWN
		 *
		 *//*
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : generateAuditReport ends");
		return "audit/audit-history-report";
	}*/
	
	/**
	 * 
	 * PDF 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("audit-history-report-pdf")
	public void generateBedReportPdf(HttpServletResponse response, Model model,@RequestParam("param1") String encodedParam1, @RequestParam("param2") String encodedParam2,
			@RequestParam("param3") String encodedParam3) {
		byte[] encodeByte1 = Base64.getDecoder().decode(encodedParam1.getBytes());
		byte[] encodeByte2 = Base64.getDecoder().decode(encodedParam2.getBytes());
		byte[] encodeByte3 = Base64.getDecoder().decode(encodedParam3.getBytes());

		String param1 = (new String(encodeByte1));
		String param2 = (new String(encodeByte2));
		String param3 = (new String(encodeByte3));
		
		JsonResponse<List<AuditHistoryModel>> jsonResponse = new JsonResponse<List<AuditHistoryModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		try {
			
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAuditHistoryDetails", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AuditHistoryModel> hist = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<AuditHistoryModel>>() {
		});
		Map<String, Object> data = new HashMap<String, Object>();
		
		for(AuditHistoryModel m: hist) {
			String[] time = m.getCreatedOn().split(" ");
			m.setCreatedOn(time[0]);
			m.setTime(time[1].split(":")[0] + ":" + time[1].split(":")[1]);						
		}
		

		String curDate = "";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate = dateFormat.format(cal);
		String currdate = "";
		String printedBy = "";
		String printedBY = "Omraj";
		if (hist.size() != 0) {

		
			data.put("printedBy", printedBy);
			data.put("currdate", currdate);
		} else {
			data.put("printedBy", printedBY);
			data.put("currdate", curDate);
		}

		
		data.put("hist", hist);
		
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AuditHistroy.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("audit/pdf-audit-history", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
