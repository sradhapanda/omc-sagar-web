package nirmalya.aathithya.webmodule.audit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditMasterPdfModel;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "audit")
public class AuditPdfController {
	Logger logger = LoggerFactory.getLogger(AuditInitiateController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	/**
	 * 
	 * PDF FOR Audit Question
	 * 
	 */
	@SuppressWarnings({ "unused" })
	@GetMapping("view-initiated-audit-getReport-Pdf")
	public void generateQuotationPdf(HttpServletResponse response,Model model,@RequestParam("auditNo") String id,@RequestParam("auditType") String auditTypeId ){
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		System.out.println("auditType"+auditTypeId);
		byte[] encodeByte1 = Base64.getDecoder().decode(auditTypeId.getBytes());
		String auditType1 = (new String(encodeByte1));
		System.out.println("auditType1"+auditType1);
		List<DropDownModel> questions = new ArrayList<DropDownModel>();
		List<DropDownModel> replies = new ArrayList<DropDownModel>();
		List<DropDownModel> finObs = new ArrayList<DropDownModel>();
		JsonResponse<List<AuditObservationModel>> jsonResponse = new JsonResponse<List<AuditObservationModel>>();
		try {

			DropDownModel[] res = restClient
					.getForObject(
							env.getAuditUrl() + "getAuditReportDtl?auditId=" + id1,DropDownModel[].class);
			questions = Arrays.asList(res);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			DropDownModel[] res = restClient
					.getForObject(
							env.getAuditUrl() + "getReplyOfManagement?auditId=" + id1,DropDownModel[].class);
			replies = Arrays.asList(res);
		//	System.out.println("questions:" + questions);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			DropDownModel[] res = restClient
					.getForObject(
							env.getAuditUrl() + "getAuditFinalObservation?auditId=" + id1,DropDownModel[].class);
			finObs = Arrays.asList(res);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (DropDownModel a : finObs) {
			
			if(a.getKey()!=null) {
			if (a.getKey().equals("1")) {
				a.setDocName("Blocker");
			} else if (a.getKey() .equals("2")) {
				a.setDocName("High");

			} else if (a.getKey() .equals("3")) {
				a.setDocName("Medium");

			} else if (a.getKey().equals("4")) {
				a.setDocName("Low");
			}
			}
		}
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		data.put("finObs",finObs);
		
		
	
		data.put("replies", replies);
		data.put("auditType1", auditType1);
		data.put("questions", questions);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=Audit Report.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("audit/pdf-audit-report", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * PDF FOR Audit Question
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-statutary-audit-master-question-pdf")
	public void generateStatuQuotationPdf(HttpServletResponse response,Model model,@RequestParam("id") Integer id ){
		
		JsonResponse<List<AuditObservationModel>> jsonResponse = new JsonResponse<List<AuditObservationModel>>();
		try {
			
			jsonResponse = restClient.getForObject(env.getAuditUrl() + "pdfStatuQuestionList?id=" + id, JsonResponse.class);
		
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<AuditObservationModel> ques = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<AuditObservationModel>>() { });
		
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		for(AuditObservationModel m : ques) {
		
		}
		
		
		
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		
		data.put("currdate",curDate);
		//String logo = logoList.get(0).getName();
		
	//    data.put("logoImage",env.getBaseUrlPath()+"document/hotel/"+logo+"");
		data.put("quesList", ques);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AuditQuestion.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("audit/pdf-audit-question", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * PDF FOR Audit Question
	 * 
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@GetMapping("view-internal-audit-question-pdf")
	public void generateiNTERNQuotationPdf(HttpServletResponse response,Model model,@RequestParam("id") Integer id ){
		
		JsonResponse<List<AuditObservationModel>> jsonResponse = new JsonResponse<List<AuditObservationModel>>();
		try {
			
			jsonResponse = restClient.getForObject(env.getAuditUrl() + "pdfStatuQuestionList?id=" + id, JsonResponse.class);
		
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		
		
		List<AuditObservationModel> ques = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<AuditObservationModel>>() { });
		
		Map<String,Object> data = new HashMap<String,Object>();
		String s = "";
		for(AuditObservationModel m : ques) {
		
		}
		
		
		
		String curDate="";
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date cal = new Date();
		curDate= dateFormat.format(cal); 
		
		data.put("currdate",curDate);
		//String logo = logoList.get(0).getName();
		
	//    data.put("logoImage",env.getBaseUrlPath()+"document/hotel/"+logo+"");
		data.put("quesList", ques);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AuditQuestion.pdf");
		File file;
		byte[] fileData = null;
		try{
			file= pdfGeneratorUtil.createPdf("audit/pdf-audit-question", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	 * Generate Pdf For Assigned Asset
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/audit-details" })
	public void generateAssignedAssetPdf(HttpServletResponse response, Model model,
			@RequestParam("param1") String param1) {

		JsonResponse<List<AuditMasterPdfModel>> jsonResponse = new JsonResponse<List<AuditMasterPdfModel>>();
		DataTableRequest tableRequest = new DataTableRequest();
		
		tableRequest.setParam1(param1);

		try {

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllStatuAuditDetailsByAuditId", tableRequest,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();

		List<AuditMasterPdfModel> AuditMasterPdfModelList = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<AuditMasterPdfModel>>() {
				});
		Map<String, Object> data = new HashMap<String, Object>();

		for(AuditMasterPdfModel a :AuditMasterPdfModelList) {
			if(a.getAuditType().contains("1")) {
				a.setAuditType("Improved");
			}else if(a.getAuditType().contains("2")) {
				a.setAuditType("POM");
			}else if (a.getAuditType().contains("3")) {
				a.setAuditType("NC");
			}
			
			if(a.getSeverity()==1) {
				a.setSeverityName("Blocker");
			} else if(a.getSeverity()==2) {
				a.setSeverityName("High");
			} else if(a.getSeverity()==3) {
				a.setSeverityName("Medium");
			} else if(a.getSeverity()==4) {
				a.setSeverityName("Low");
			} else {
				a.setSeverityName("- -");
			}
			
			if(a.getAgreedAction()== "" || a.getAgreedAction()==null) {
				a.setAgreedAction("N/A");
			}
		}
		data.put("AuditMasterPdfModelList", AuditMasterPdfModelList); 
		// data.put("a", assignedAsset);
		/**
		 * get Hotel Logo
		 *
		 */
//		List<DropDownModel> logoList = new ArrayList<DropDownModel>();
//		try {
//			DropDownModel[] logo = restClient.getForObject(
//					env.getAuditUrl() + "restLogoImage-Question?logoType=" + "header-Logo", DropDownModel[].class);
//			logoList = Arrays.asList(logo);
//			model.addAttribute("logoList", logoList);
//			data.put("logoList", logoList);
//			
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=AssignedAssets.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("audit/pdf-audit-details", data);
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
