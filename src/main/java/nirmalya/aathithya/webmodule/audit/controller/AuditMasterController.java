package nirmalya.aathithya.webmodule.audit.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditMasterModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.constant.Constant;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "audit")
public class AuditMasterController {

	Logger logger = LoggerFactory.getLogger(AuditMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	@Autowired
	MailService mailService;
	/**
	 * View Default 'Add Audit' page
	 *
	 */
	@GetMapping("/add-audit-master")
	public String addAuditMaster(Model model, @RequestParam("id") String id,@RequestParam("name") String name,HttpSession session) {
		logger.info("Method : addAuditMaster starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		
		byte[] encodeByte1 = Base64.getDecoder().decode(name.getBytes());
		String name1 = (new String(encodeByte1));
		 model.addAttribute("auditType", id1);
		 model.addAttribute("auditTypeName", name1);

		/**
		 * AUDIT TYPE DROP DOWN
		 *
		 *//*
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}*/
		 
		 try {
				DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
				List<DropDownModel> programList = Arrays.asList(program);
				model.addAttribute("empList", programList);

			} catch (RestClientException e) {
				e.printStackTrace();
			} 
		 
		try {
			DropDownModel[] org = restClient.getForObject(env.getAuditUrl() + "getOrganizationId",
					DropDownModel[].class);
			List<DropDownModel> orgList = Arrays.asList(org);

			model.addAttribute("orgList", orgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** Department **/
		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getDeptList", DropDownModel[].class);
			List<DropDownModel> deptList = Arrays.asList(dept);

			model.addAttribute("deptList", deptList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/** Sub Coordinator **/
		try {
			DropDownModel[] sub = restClient.getForObject(env.getAuditUrl() + "getSubCoordList", DropDownModel[].class);
			List<DropDownModel> subCoordinatorList = Arrays.asList(sub);
			
			model.addAttribute("subCoordinatorList", subCoordinatorList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * financial YearList
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
			List<DropDownModel>financialYearList = Arrays.asList(audit);
			
			model.addAttribute("financialYearList", financialYearList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		//*********** get region *************/
		try {
			DropDownModel[] org = restClient.getForObject(env.getAuditUrl() + "getRegion", DropDownModel[].class);
			List<DropDownModel> regionList = Arrays.asList(org);
			model.addAttribute("regionList", regionList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		// *********** getQuarter *************/
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getQuarter", DropDownModel[].class);
			List<DropDownModel> getQuarterList = Arrays.asList(audit);

			model.addAttribute("getQuarterList", getQuarterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addAuditMaster ends");
		return "audit/add-audit-master";
	}

	/**
	 * get Organization
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-audit-master-getOrganization" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getOrganization(Model model,
			@RequestParam("auditType") String audit, HttpSession session) {
		logger.info("Method : getOrganization starts");
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getAuditOrganization?audit=" + audit,
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
		logger.info("Method : getOrganization ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-audit-master-get-section" })
	public @ResponseBody JsonResponse<Object> getSectionList(Model model, @RequestBody String dept,
			BindingResult result) {
		logger.info("Method : getSectionList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getSectionListByDept?id=" + dept, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getSectionList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-audit-master-get-dept-head" })
	public @ResponseBody JsonResponse<Object> getDeptHeadList(Model model, @RequestParam("id") List<String> taskId,
			HttpSession session) {
		logger.info("Method : getDeptHeadList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String listProduct = "";
		for (String str : taskId) {
			str = str.replace("[", "").replace("]", "");
			listProduct = listProduct + "'" + str + "',";
		}

		listProduct = listProduct.substring(0, listProduct.length() - 1);
		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDeptHeadListByDept?id=" + listProduct, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getDeptHeadList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-audit-master-get-auditee" })
	public @ResponseBody JsonResponse<Object> getAuditeeList(Model model, @RequestBody String dept,
			BindingResult result) {
		logger.info("Method : getAuditeeList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getAuditeeListByDept?id=" + dept, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getAuditeeList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-audit-master-get-auditor" })
	public @ResponseBody JsonResponse<Object> getAuditorList(Model model, @RequestBody String org,
			BindingResult result) {
		logger.info("Method : getAuditorList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getAuditorListByOrg?id=" + org, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getAuditorList ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "add-audit-master-save", method = { RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<List<DropDownModel>> addNewAudit(@RequestBody List<AuditMasterModel> audit,
			Model model, HttpSession session) throws IOException {
		logger.info("Method : addNewAudit function starts");
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		String emalstatus=(String) session.getAttribute("MAILSTATUS");
		 String emailvalue="1";
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditMasterModel m : audit) {
			m.setCreatedBy(userId);
		}
		String userName="";
		try {
			userName = (String) session.getAttribute("USER_NAME");
		} catch (Exception e) {
		}
		int imagecount = 0;
	
		imagecount = imagecount + 1;
		if(audit.get(0).getDocuments().size()>0) {
		for (int i = 0; i < audit.get(0).getDocuments().size(); i++) {
			
			audit.get(0).getDocuments().get(i).setDoc(uploadPhoto(audit.get(0).getDocuments().get(i).getDoc(),audit.get(0).getDocuments().get(i).getFile()));
			}
		}
		res = restClient.postForObject(env.getAuditUrl() + "addNewAudit", audit, JsonResponse.class);

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			List<String> recipients=new ArrayList<String>();
			List<String> test=new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {
				
				recipients.add(m.getName());
			}
		
			List<String> recipients1=new ArrayList<String>();
			
					// message info
					if(audit.get(0).getAuditName().equals("ADTM001"))
					{
						audit.get(0).setAuditName("Supplementary Audit");	
					}
					if(audit.get(0).getAuditName().equals("ADTM006"))
					{
						audit.get(0).setAuditName("Statutory Audit");	
					}
					if(audit.get(0).getAuditName().equals("ADTM005"))
					{
						audit.get(0).setAuditName("Internal Audit");	
					}
					
					String subject = "From :-"+userName+System.lineSeparator() +"Subject:-"+audit.get(0).getSubject();
					String message1 = audit.get(0).getSummary();
					
					
					String[] attachFiles=null;
					if(audit.get(0).getDocuments().size()>0) {
					 attachFiles = new String[audit.get(0).getDocuments().size()];
				for(int i=0; i<attachFiles.length;i++) {
					attachFiles[i] = env.getFileUploadAudit() + audit.get(0).getDocuments().get(i).getDoc();
				}
					}
					
					
					try {
					if(emalstatus.equals(emailvalue)) 
					{
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port,userName,
							Constant.password, recipients,audit.get(0).getCcData(), subject, message1, attachFiles);
					System.out.println("Email sent.");
				}
				} catch (Exception ex) {
					System.out.println("Could not send email.");
					ex.printStackTrace();
				}
				
			}
		logger.info("Method : addNewAudit function Ends");
		return res;
	}

	public String  uploadPhoto(String document,List<String> file) {
		/* bikash changes */
		String documentname = document.split("\\.")[0];
				System.out.println("documentname "+documentname);
		if (document!= null && document != "") {
			String delimiters = "\\.";
			String[] x =document.split(delimiters);
			System.out.println("x delimiter"+Arrays.toString(x));
			if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
				System.out.println("in png ");
				for (String s1 : file) {
					System.out.println("in png 123 ");
					try {
						
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String imageName = saveAllImage(documentname,bytes);
						document=imageName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("pdf")) {
				for (String s1 : file) {
					try {
						
						
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllPdf(documentname,bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("docx")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllDocx(documentname,bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("doc")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllDoc(documentname,bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("xls")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllXls(documentname,bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("xlsx")) {
				for (String s1 :file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllXlsx(documentname,bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("document123 "+document);
		return document;
		}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/add-audit-master-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
			// System.out.println(inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

//	@GetMapping("/gm-audit-master-view")  
//	public String gmAuditObservation(Model model, HttpSession session, @RequestParam Integer id) {
//		logger.info("Method : gmAuditObservation starts");
//		
//		model.addAttribute("AuditId", id);
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
//			List<DropDownModel> stage = Arrays.asList(obs);
//			
//			model.addAttribute("UserStage", stage.get(0).getName());
//			model.addAttribute("AuthorityStage", stage.get(0).getKey());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			System.out.println(questionList);
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
//			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
//			
//			model.addAttribute("commentsList", commentsList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : gmAuditObservation starts");
//		return "audit/gm-audit-observation";
//	}
//	
//	@GetMapping("/gm-audit-master") 
//	public String gmAuditMaster(Model model, HttpSession session) {
//		logger.info("Method : gmAuditMaster starts");
//		
//		logger.info("Method : gmAuditMaster starts");
//		return "audit/approve-audit";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@GetMapping("/gm-audit-master-ThroughAjax")
//	public @ResponseBody DataTableResponse gmViewAuditMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, HttpSession session) {
//		logger.info("Method : gmViewAuditMasterThroughAjax statrs");
//
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//			tableRequest.setParam3(param3);
//			tableRequest.setUserId(userId);
//
//			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllAuditMaster", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<AuditMasterModel> auditMasterModelList = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<AuditMasterModel>>() {
//					});
//			List<String> roles = null;
//			try {
//				roles = (List<String>) session.getAttribute("USER_ROLES");
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//
//			for (AuditMasterModel m : auditMasterModelList) {
//				String s = "";
////				byte[] deptId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
////				byte[] sectionId = Base64.getEncoder().encode(m.getSectionId().getBytes());
//			
//				if(m.getAuditNameId().equals("ADTM002")) {
//					if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//						m.setAuditAt("Auditor");
//					} else if(m.getApproveStatus()==1) {
//						m.setAuditAt("Co-ordinator");
//					} else if(m.getApproveStatus()==2) {
//						m.setAuditAt("Financial");
//					} else if(m.getApproveStatus()==3) {
//						m.setAuditAt("Audit Committee");
//					} else if(m.getApproveStatus()==4) {
//						m.setAuditAt("Board Of Director");
//					} else {
//						m.setAuditAt("Auditor");
//					}
//				}
//				else if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//					m.setAuditAt("Auditor");
//				} else if(m.getApproveStatus()==1) {
//					m.setAuditAt("GM");
//				} else if(m.getApproveStatus()==2) {
//					m.setAuditAt("Director");
//				} else if(m.getApproveStatus()==3) {
//					m.setAuditAt("Audit Committee");
//				} else if(m.getApproveStatus()==4) {
//					m.setAuditAt("Board Of Director");
//				} else {
//					m.setAuditAt("Auditor");
//				}
//				if (m.getAuditStatus() == 0) {
//					if(roles.contains("rol024")) {
//						s = s + "<a href='/audit/gm-audit-master-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-info\"> Add Question </button></a>";
//					} else {
//						s = "N/A";
//					}
//					m.setStatus("N/A");
//				} else if (m.getAuditStatus() == 1) {
//					if(roles.contains("rol024")) {
//						s = s + "<a href='/audit/gm-audit-master-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-primary\"> Observation </button></a>&nbsp;&nbsp;";
//						s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'><i class='fa fa-download' style='font-size:20px;'></i></button></a>";
//					} else {
//						s = "N/A";
//					}
//					m.setStatus("Not Observed");
//				} else if(m.getAuditStatus() == 2) {
//					m.setStatus("Open");
//					s = s + "<a href='/audit/gm-audit-master-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//				} else if(m.getAuditStatus() == 3) {
//					m.setStatus("Closed");
//					s = s + "<a href='/audit/gm-audit-master-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-success\"> View Details </button></a>&nbsp;&nbsp;";
//					s = s + "<a href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'>Report</button></a>";
//				}
//				m.setAction(s);
//			}
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(auditMasterModelList);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		logger.info("Method : gmViewAuditMasterThroughAjax Theme ends");
//		return response;
//	}
//	
//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/add-audit-observation") 
//	public String addAuditObservation(Model model, HttpSession session, @RequestParam Integer id,
//			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
//		logger.info("Method : addAuditObservation starts");
//		
//		model.addAttribute("Status", status);
//		model.addAttribute("ApproveStatus", approveStatus);
//		model.addAttribute("AuditId", id);
//		model.addAttribute("DeptId", deptId);
//		model.addAttribute("SectionId", sectionId);
//		
//		byte[] encodeByte=Base64.getDecoder().decode(deptId.getBytes());
//		String deptId1 = (new String(encodeByte));
//		byte[] encodeByte1=Base64.getDecoder().decode(sectionId.getBytes());
//		String sectionId1 = (new String(encodeByte1));
//		System.out.println(env.getAuditUrl() + "getQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1);
//		try {
//			
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			System.out.println(questionList.get(0).getiRStatus());
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addAuditObservation ends");
//		return "audit/add-audit-observation";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "add-audit-observation-forward-to-approve", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> fowardedToApprove(@RequestBody List<AuditObservationModel> observation,
//			Model model, HttpSession session) {
//		logger.info("Method : fowardedToApprove function starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			// Variable question is used for ApprovalBy
//			for(AuditObservationModel m : observation) {
//				m.setQuestion(userId);
//			}
//			res = restClient.postForObject(env.getAuditUrl() + "approveObservation", observation, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : fowardedToApprove function Ends");
//		return res;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "gm-audit-master-forward-to-approve", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> fowardedToApproveStatus(@RequestBody List<AuditObservationModel> observation,
//			Model model, HttpSession session) {
//		logger.info("Method : fowardedToApproveStatus function starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			// Variable question is used for ApprovalBy
//			for(AuditObservationModel m : observation) {
//				m.setQuestion(userId);
//			}
//			res = restClient.postForObject(env.getAuditUrl() + "forwardToApproveObservation", observation, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : fowardedToApproveStatus function Ends");
//		return res;
//	}
//	
//	@SuppressWarnings({ "unchecked", "restriction" })
//	@RequestMapping(value = "add-audit-observation-save", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> addObservation(@RequestBody List<AuditObservationModel> observation,
//			Model model, HttpSession session) {
//		logger.info("Method : addObservation function starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		
//		int imagecount = 0;
//		for (AuditObservationModel a : observation) {
//
//			if (a.getDocument() != null && a.getDocument() != "") {
//				String delimiters = "\\.";
//				String[] x = a.getDocument().split(delimiters);
//
//				if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
//					for (String s1 : a.getFile()) {
//
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String imageName = saveAllImage(bytes);
//							a.setDocument(imageName);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (x[1].matches("pdf")) {
//					for (String s1 : a.getFile()) {
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String pdfName = saveAllPdf(bytes);
//							a.setDocument(pdfName);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (x[1].matches("docx")) {
//					for (String s1 : a.getFile()) {
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String pdfName = saveAllDocx(bytes);
//							a.setDocument(pdfName);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (x[1].matches("doc")) {
//					for (String s1 : a.getFile()) {
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String pdfName = saveAllDoc(bytes);
//							a.setDocument(pdfName);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			imagecount = imagecount + 1;
//		}
//
//		try {
//			
//			res = restClient.postForObject(env.getAuditUrl() + "addObservation", observation, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : addObservation function Ends");
//		return res;
//	}
//	
//	public String saveAllImage(byte[] imageBytes) {
//		logger.info("Method : saveAllImage starts");
//
//		String imageName = null;
//
//		try {
//			if (imageBytes != null) {
//				long nowTime = new Date().getTime();
//				imageName = nowTime + ".png";
//			}
//
//			Path path = Paths.get(env.getFileUploadAudit() + imageName);
//			if (imageBytes != null) {
//				Files.write(path, imageBytes);
//
//				ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
//				Integer height = 50;
//				Integer width = 50;
//
//				try {
//					BufferedImage img = ImageIO.read(in);
//					if (height == 0) {
//						height = (width * img.getHeight()) / img.getWidth();
//					}
//					if (width == 0) {
//						width = (height * img.getWidth()) / img.getHeight();
//					}
//
//					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//
//					BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
//
//					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//					ImageIO.write(imageBuff, "png", buffer);
//
//					byte[] thumb = buffer.toByteArray();
//
//					Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + imageName);
//					Files.write(pathThumb, thumb);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : saveAllImage ends");
//		return imageName;
//	}
//
//	/*
//	 * for save all pdf in folder and return name
//	 */
//
//	public String saveAllPdf(byte[] imageBytes) {
//		logger.info("Method : saveAllPdf starts");
//
//		String pdfName = null;
//
//		try {
//			if (imageBytes != null) {
//				long nowTime = new Date().getTime();
//				pdfName = nowTime + ".pdf";
//			}
//
//			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
//			if (imageBytes != null) {
//				Files.write(path, imageBytes);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : saveAllPdf ends");
//		return pdfName;
//	}
//	
//	public String saveAllDocx(byte[] imageBytes) {
//		logger.info("Method : saveAllDocx starts");
//		
//		String pdfName = null;
//		
//		try {
//			if (imageBytes != null) {
//				long nowTime = new Date().getTime();
//				pdfName = nowTime + ".docx";
//			}
//			
//			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
//			if (imageBytes != null) {
//				Files.write(path, imageBytes);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : saveAllDocx ends");
//		return pdfName;
//	}
//	
//	public String saveAllDoc(byte[] imageBytes) {
//		logger.info("Method : saveAllDoc starts");
//		
//		String pdfName = null;
//		
//		try {
//			if (imageBytes != null) {
//				long nowTime = new Date().getTime();
//				pdfName = nowTime + ".doc";
//			}
//			
//			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
//			if (imageBytes != null) {
//				Files.write(path, imageBytes);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : saveAllDoc ends");
//		return pdfName;
//	}
//	
//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/add-audit-question")
//	public String addAuditQuestion(Model model, HttpSession session, @RequestParam Integer id,
//			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status) {
//
//		logger.info("Method : addAuditQuestion starts");
//
//		model.addAttribute("initiatedId", id);
//		model.addAttribute("deptId", deptId);
//		model.addAttribute("sectionId", sectionId);
//
//		logger.info("Method : addAuditQuestion ends");
//
//		return "audit/add-audit-question";
//	}
//
//	/*
//	 * post mapping for add employee Education
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/add-audit-question-ajax", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> addServicePrice(
//			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		logger.info("Method : addServicePrice function starts");
//
//		try {
//
//			res = restClient.postForObject(env.getAuditUrl() + "restAddQuestion", auditObservationModel,
//					JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : addServicePrice function Ends");
//		return res;
//	}
//	
//	@PostMapping("/add-audit-observation-auditee-uploadFile")
//	public @ResponseBody JsonResponse<Object> auditeeUploadFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : auditeeUploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageAuditeeFile", inputFile);
//			System.out.println(inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : auditeeUploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@PostMapping("/add-audit-observation-auditee-comment-save")
//	public @ResponseBody JsonResponse<Object> auditeeCommentSave(Model model, @RequestBody AuditComplianceModel index, HttpSession session) throws IOException {
//		
//		logger.info("Method : auditeeCommentSave starts");
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageAuditeeFile");
//			
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: "+fileType[1]);
//				String contentName = "";
//				if(fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if(fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				
//				index.setDocument(contentName);
//
//				index.setCreatedBy(userId);
//		
//				resp = restClient.postForObject(env.getAuditUrl() + "auditeeCommentSave" , index, JsonResponse.class);
//
//				if (resp.getMessage() == null || resp.getMessage() == "") {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document") || fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				index.setDocument(null);
//				index.setCreatedBy(userId);
//				resp = restClient.postForObject(env.getAuditUrl() + "auditeeCommentSave" , index, JsonResponse.class);
//
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//			session.removeAttribute("imageAuditeeFile");
//		}
//		logger.info("Method : auditeeCommentSave ends");
//		return resp;
//	}
//	
//	@PostMapping("/add-audit-observation-auditor-uploadFile")
//	public @ResponseBody JsonResponse<Object> auditorUploadFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : auditorUploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageAuditorFile", inputFile);
//			System.out.println(inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : auditorUploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@PostMapping("/add-audit-observation-auditor-comment-save")
//	public @ResponseBody JsonResponse<Object> auditorCommentSave(Model model, @RequestBody AuditComplianceModel index, HttpSession session) throws IOException {
//		
//		logger.info("Method : auditorCommentSave starts");
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		if(index.getAuditStatus()==4) {
//			try {
//				index.setCreatedBy(userId);
//				resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave" , index, JsonResponse.class);
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				MultipartFile inputFile = (MultipartFile) session.getAttribute("imageAuditorFile");
//				
//				if (inputFile != null) {
//					long nowTime = new Date().getTime();
//
//					byte[] bytes = inputFile.getBytes();
//					String[] fileType = inputFile.getContentType().split("/");
//					System.out.println("CType: "+fileType[1]);
//					String contentName = "";
//					if(fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//						contentName = nowTime + ".docx";
//					} else if(fileType[1].contentEquals("msword")) {
//						contentName = nowTime + ".doc";
//					} else {
//						contentName = nowTime + "." + fileType[1];
//					}
//					if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//						contentName = nowTime + ".xls";
//					} else if (fileType[1].contentEquals(".xlsx")) {
//						contentName = nowTime + ".xls";
//					} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//						contentName = nowTime + ".xls";
//					} else {
//						contentName = nowTime + "." + fileType[1];
//					}
//					
//					index.setDocument(contentName);
//
//					index.setCreatedBy(userId);
//					resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave" , index, JsonResponse.class);
//
//					if (resp.getMessage() == null || resp.getMessage() == "") {
//						Path path = Paths.get(env.getFileUploadAudit() + contentName);
//						if (fileType[1].contentEquals("pdf")) {
//							Files.write(path, bytes);
//						} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document") || fileType[1].contentEquals("msword")) {
//							Files.write(path, bytes);
//						} else {
//
//							Files.write(path, bytes);
//
//							ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//							Integer height = 50;
//							Integer width = 50;
//
//							try {
//								BufferedImage img = ImageIO.read(in);
//								if (height == 0) {
//									height = (width * img.getHeight()) / img.getWidth();
//								}
//								if (width == 0) {
//									width = (height * img.getWidth()) / img.getHeight();
//								}
//
//								Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//								BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//								imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//								ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//								ImageIO.write(imageBuff, "png", buffer);
//
//								// ByteArrayOutputStream out = new ByteArrayOutputStream();
//								byte[] thumb = buffer.toByteArray();
//								Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//								Files.write(pathThumb, thumb);
//
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//						}
//					}
//
//				} else {
//					System.out.println("ABC");
//					index.setCreatedBy(userId);
//					index.setDocument(null);
//					resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave" , index, JsonResponse.class);
//
//				}
//			} catch (RestClientException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//			session.removeAttribute("imageAuditorFile");
//			System.out.println(session.getAttribute("imageAuditorFile"));
//		}
//		logger.info("Method : auditorCommentSave ends");
//		return resp;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "/add-audit-observation-view-comment" })
//	public @ResponseBody JsonResponse<Object> viewCompliance(Model model, @RequestBody Integer id,
//			BindingResult result) {
//
//		logger.info("Method : viewCompliance starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//
//		try {
//			res = restClient.getForObject(env.getAuditUrl() + "getViewCommentList?id=" + id, JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		if (res.getMessage() != null) {
//
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//
//		logger.info("Method : viewCompliance ends");
//		return res;
//	}
//	
//	/*
//	* approve accept
//	*
//	*
//	*/
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-audit-master-sendRequest" })
//	public @ResponseBody JsonResponse<Object> sendRequestRequisition(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendRequestRequisition starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				System.out.println(contentName);
//
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendRequestRequisition?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendRequestRequisition?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendRequestRequisition ends");
//		System.out.println(resp);
//		return resp;
//	}
//
//	/**
//	 * Web Controller - Upload Document
//	 *
//	 */
//	@PostMapping("/view-audit-master-uploadFile")
//	public @ResponseBody JsonResponse<Object> viewUploadFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : uploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageFile", inputFile);
//			System.out.println("inputFile" + inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : uploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//	
//	/**
//	* Web Controller - Upload Document
//	*
//	*/
//	@PostMapping("/view-audit-master-uploadIRFile")
//	public @ResponseBody JsonResponse<Object> viewUploadIRFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : uploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageFile", inputFile);
//			System.out.println("inputFile" + inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : uploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//
//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/view-requisition")
//	public String viewRequisition(Model model, HttpSession session) {
//		logger.info("Method : viewRequisition starts");
//
//		logger.info("Method : viewRequisition ends");
//		return "audit/viewRequisition";
//	}
//	/*
//	 * For view audit master Ajaxcall
//	 */
//
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-requisition-ThroughAjax")
//	public @ResponseBody DataTableResponse viewRequisitionThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
//			HttpSession session) {
//
//		logger.info("Method : viewRequisitionThroughAjax statrs");
//
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//			tableRequest.setParam3(param3);
//			tableRequest.setUserId(userId);
//
//			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllAuditMaster", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<AuditMasterModel> auditMasterModelList = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<AuditMasterModel>>() {
//					});
//			List<String> roles = null;
//			try {
//				roles = (List<String>) session.getAttribute("USER_ROLES");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			for (AuditMasterModel m : auditMasterModelList) {
//				String s = "";
//				String reqAct = "";
//				byte[] deptId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
//				byte[] sectionId = Base64.getEncoder().encode(m.getSectionId().getBytes());
//				if(m.getAuditStatus()==0) {
//					m.setStatus("N/A");
//				} else if (m.getAuditStatus() == 1) {
//					m.setStatus("Not Observed");
//				} else if (m.getAuditStatus() == 2) {
//					m.setStatus("Open");
//				} else if (m.getAuditStatus() == 3) {
//					m.setStatus("Close");
//				}
//				if (m.getReqStatus() == 1) {
//					s = s + "&nbsp;&nbsp;&nbsp;<a title='Upload' onclick='responseRequisition(\""
//							+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'> Reply</buttion> </a>";
//
//				} else if (m.getReqStatus() == 2) {
//					s = s + "&nbsp;&nbsp;&nbsp;<a title='Upload' onclick='viewDocument(\"" + m.getAuditAutoGenId()
//							+ "\")' ><button class='btn btn-success'>View</buttion> </a>";
//
//				} else {
//					s = s + ("N/A");
//				}
//			
//				m.setAction(s);
//
//			}
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(auditMasterModelList);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		logger.info("Method : viewRequisitionThroughAjax Theme ends");
//
//		return response;
//	}
//	
//
//	/*
//	 * send response to requisition
//	 *
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-requisition-sendResponse" })
//	public @ResponseBody JsonResponse<Object> sendResponse(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendResponse starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//// auditInitiate.setDocument(contentName);
//
//				try {
//					resp = restClient
//							.getForObject(
//									env.getAuditUrl() + "sendResponseRequisition?id=" + encodeId + "&createdBy="
//											+ userId + "&document=" + contentName + "&comment=" + comment,
//									JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient
//							.getForObject(
//									env.getAuditUrl() + "sendResponseRequisition?id=" + encodeId + "&createdBy="
//											+ userId + "&document=" + contentName + "&comment=" + comment,
//									JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendResponse ends");
//		System.out.println(resp);
//		return resp;
//	}
//	/*
//	 * get Mapping for view document
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-requisition-getDocument", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> viewDocument(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method : viewDocument function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "viewDocument?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : viewDocument function Ends");
//		return res;
//	}
//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/view-audit-master")
//	public String viewStatutaryAuditMaster(Model model, HttpSession session) {
//
//		logger.info("Method : viewStatutaryAuditMaster starts");
//
//		logger.info("Method : viewStatutaryAuditMaster ends");
//
//		return "audit/view-audit-master";
//	}
//
//	/*
//	 * For view audit master Ajax call
//	 */
//
//	@SuppressWarnings({ "unchecked" })
//	@GetMapping("/view-audit-master-ThroughAjax")
//	public @ResponseBody DataTableResponse viewAuditMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, HttpSession session) {
//		logger.info("Method : viewInitaiedAuditThroughAjax statrs");
//
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//			tableRequest.setParam3(param3);
//			tableRequest.setUserId(userId);
//
//			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllTransactionAudit", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<AuditMasterModel> auditMasterModelList = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<AuditMasterModel>>() {
//					});
//			List<String> roles = null;
//			try {
//				roles = (List<String>) session.getAttribute("USER_ROLES");
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//
//			for (AuditMasterModel m : auditMasterModelList) {
//				String s = ""; 
//				byte[] deptId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
//				byte[] sectionId = Base64.getEncoder().encode(m.getSectionId().getBytes());
//				
//				if(m.getAuditNameId().equals("ADTM002")) {
//					if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//						m.setAuditAt("Co-ordinator");
//					} else if(m.getApproveStatus()==1) {
//						m.setAuditAt("Financial Head");
//					} else if(m.getApproveStatus()==2) {
//						m.setAuditAt("Audit Committee");
//					} else if(m.getApproveStatus()==3) {
//						m.setAuditAt("Co-ordinator");
//					} else if(m.getApproveStatus()==4 || m.getApproveStatus()==6) {
//						m.setAuditAt("Financial Head");
//					} else {
//						m.setAuditAt("Co-ordinator");
//					}
//				}
//				else if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//					m.setAuditAt("Auditor");
//				} else if(m.getApproveStatus()==1) {
//					m.setAuditAt("GM");
//				} else if(m.getApproveStatus()==2) {
//					m.setAuditAt("Director");
//				} else if(m.getApproveStatus()==3) {
//					m.setAuditAt("Audit Committee");
//				} else if(m.getApproveStatus()==4) {
//					m.setAuditAt("Board Of Director");
//				} else {
//					m.setAuditAt("Auditor");
//				}
//				System.out.println("Sagar Audit "+m.getApproveStatus());
//				if (m.getAuditStatus() == 0) {
//					if(roles.contains("rol024")) {
//						s = s + "<a title='Question' href='/audit/add-audit-question?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-default\" style='font-size:10px;'>Question</button></a>";
//					
//						if (m.getReqStatus() == 1) {
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Requested' onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'>View</button> </a>";
//						} else if (m.getReqStatus() == 2) {
//
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Requested' onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")' ><button class='btn btn-success'>Response</button> </a>";
//
//						} else {
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Request' onclick='requestRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")'><button class='btn btn-info' style=\"font-size:10px;\">Requisition</button> </a>";
//
//						}
//					
//					} else if (m.getAuditStatus() == 0) {
//						if (roles.contains("rol021")) {
//							if (m.getIrStatus() != 1 && m.getIrStatus() != 2 && m.getIrStatus() != 3) {
//							}
//						}
//
//					}
////					else {
////						s = "N/A";
////					}
//					m.setStatus("N/A");
//					//s = s + "N/A";
//				} else if (m.getAuditStatus() == 1) {
//					if(roles.contains("rol024")) {
//						s = s + "<a href='/audit/add-audit-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-primary\"> Observation </button></a>&nbsp;&nbsp;";
//						s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'><i class='fa fa-download' style='font-size:20px;'></i></button></a>";
//					} else {
//						s = "N/A";
//					}
//					m.setStatus("Not Observed");
//				} else if(m.getAuditStatus() == 2 && ! roles.contains("rol021")) {
//					m.setStatus("Open");
//					s = s + "<a href='/audit/add-audit-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//				
//					
//				
//					
//					System.out.println(roles);
//					System.out.println(m.getAuditStatus() );
//				
//					
//				
//				}else if(m.getAuditStatus() == 3) {
//					m.setStatus("Closed");
//					s = s + "<a href='/audit/add-audit-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-success\"> View Details </button></a>&nbsp;&nbsp;";
//					if(roles.contains("rol024")) {
//						s = s + "<a href='javascript:void' onclick='submitReport(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-danger'>Submit Report</button></a>";
//					}
//					s = s + "<a href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'>Report</button></a>";
//				}
//				if (roles.contains("rol021")) {
//					if(m.getAuditStatus()==0) {
//						m.setStatus("N/A");
//					} else if (m.getAuditStatus() == 1) {
//						m.setStatus("Not Observed");
//					} else if (m.getAuditStatus() == 2) {
//						m.setStatus("Open");
//					} else if (m.getAuditStatus() == 3) {
//						m.setStatus("Close");
//					}
//					
//					if(m.getAuditNameId().equals("ADTM002")) {
//						
//					if (m.getIrStatus() == 1) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='generateIrReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><button class='btn btn-warning'>IR Report</button></a>";
//					} else if (m.getIrStatus() == 2 || m.getIrStatus() == 3) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewIRStatusReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><button class='btn btn-success'> View IR Report</button></a>";
//					}
//					}
//				}
//				if (roles.contains("rol022") || roles.contains("rol023")) {
//					if(m.getAuditStatus()==0) {
//						m.setStatus("N/A");
//					} else if (m.getAuditStatus() == 1) {
//						m.setStatus("Not Observed");
//					} else if (m.getAuditStatus() == 2) {
//						m.setStatus("Open");
//					} else if (m.getAuditStatus() == 3) {
//						m.setStatus("Close");
//					}
//					if (m.getIrStatus() == 2) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='uploadIrReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><button class='btn btn-info'>Upload Report</button></a>";
//					}
//					if (m.getIrStatus() == 3) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewIrRepotTo(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><button class='btn btn-success'> View IR Report</button></a>";
//					}
//					if (m.getIrStatus() == 5) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='complianceOfDraft(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><button class='btn btn-info'>compliance</button></a>";
//					}
//					if (m.getIrStatus() == 6) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewDraft(\""
//								+ m.getAuditAutoGenId()
//								+ "\")'><button class='btn btn-success'> View Draft</button></a>";
//					}
//					if (m.getIrStatus() == 8) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='complianceOfDraftPara(\""
//								+ m.getAuditAutoGenId()
//								+ "\",\"" + new String(deptId)+"\")'><button class='btn btn-info'> Draft Para Compliance</button></a>";
//					}
//					if (m.getIrStatus() == 9) {
//						s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewDraftPara(\""
//								+ m.getAuditAutoGenId()
//								+ "\")'><button class='btn btn-info'> View Draft Para</button></a>";									
//					}
//					
//				}
//				if(m.getAuditStatus() == 2 && roles.contains("rol021") ) {				
//					m.setStatus("Open");
//					s = s + "<a href='/audit/observation-forTransAudit?id=" + m.getAuditAutoGenId() + "&deptId="
//							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//				}
//			
//				m.setAction(s);
//			}
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(auditMasterModelList);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		logger.info("Method : viewAuditMasterThroughAjax Theme ends");
//
//		return response;
//	}
//	/*
//	* get Mapping for view document
//	*/
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-audit-master-getReqDocument", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getReqAudDocument(@RequestParam("id") String id,
//	Model model, HttpSession session) {
//	JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//	logger.info("Method : getReqAudDocument function starts");
//
//	try {
//	System.out.println(id);
//	res = restClient.getForObject(env.getAuditUrl() + "getReqDocument?id=" + id, JsonResponse.class);
//
//	} catch (RestClientException e) {
//	e.printStackTrace();
//	}
//
//	String message = res.getMessage();
//
//	if (message != null && message != "") {
//
//	} else {
//	res.setMessage("Success");
//	}
//	logger.info("Method : getReqAudDocument function Ends");
//	return res;
//	}
//
//	/*
//	 * send response to requisition
//	 *
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "view-audit-master-sendIrStatus" })
//	public @ResponseBody JsonResponse<Object> sendIrStatus(Model model, @RequestBody AuditMasterModel auditMasterModel,
//			HttpSession session) throws IOException {
//		logger.info("Method : sendIrStatus starts");
//		System.out.println(auditMasterModel.getAuditAutoGenId());
//		System.out.println(auditMasterModel.getDepartment());
//		System.out.println(auditMasterModel.getOrganisation());
//		// System.out.println(obj.charAt(3));
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		auditMasterModel.setCreatedBy(userId);
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				auditMasterModel.setGuideLineDoc(contentName);
//
//				try {
//					resp = restClient.postForObject(env.getAuditUrl() + "sendIRStatus", auditMasterModel,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.postForObject(env.getAuditUrl() + "sendIRStatus", auditMasterModel,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("Success");
//		}
//		logger.info("Method : sendIrStatus ends");
//		System.out.println(resp);
//		return resp;
//	}
//	/*
//	 * get Mapping for view IrStatus
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/get-IrStatusReportView", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getIrStatusReportView(@RequestParam("id") String id,
//			Model model, HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :getIrStatusReportView function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getIrStatusReportView?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getIrStatusReportView function Ends");
//		return res;
//	}
//	/*
//	 * get Mapping for view IrStatus
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-transaction-audit-master-getIrStatus", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> sendIrStatus(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :sendIrStatus function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getIrStatusReportView?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : sendIrStatus function Ends");
//		return res;
//	}
//
//	/*
//	 * send response to requisition
//	 *
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "get-IrStatusReport" })
//	public @ResponseBody JsonResponse<Object> IrStatusReport(Model model,
//			@RequestBody AuditMasterModel auditMasterModel, HttpSession session) throws IOException {
//		logger.info("Method : IrStatusReport starts");
//		//System.out.println(auditMasterModel.getAuditAutoGenId());
//		//System.out.println(auditMasterModel.getDepartment());
//		//System.out.println(auditMasterModel.getOrganisation());
//		// System.out.println(obj.charAt(3));
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		auditMasterModel.setCreatedBy(userId);
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				auditMasterModel.setGuideLineDoc(contentName);
//
//				try {
//					resp = restClient.postForObject(env.getAuditUrl() + "getIRStatus", auditMasterModel,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.postForObject(env.getAuditUrl() + "getIRStatus", auditMasterModel,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("Success");
//		}
//		logger.info("Method : IrStatusReport ends");
//		System.out.println(resp);
//		return resp;
//	}
//	/*
//	 * get Mapping for view document
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-audit-master-getDocument", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> viewAudDocument(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method : viewAudDocument function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "viewDocument?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : viewAudDocument function Ends");
//		return res;
//	}
//	/*
//	 * get Mapping for view document
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-requisition-getReqDocument", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getReqDocument(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method : getReqDocument function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getReqDocument?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getReqDocument function Ends");
//		return res;
//	}
//	
////	@SuppressWarnings("unchecked")
////	@PostMapping(value = { "gm-audit-master-view-get-stage" })
////	public @ResponseBody JsonResponse<Object> getApprovalStage(Model model, @RequestBody Integer dept,
////			BindingResult result, HttpSession ss) {
////		logger.info("Method : getApprovalStage starts");
////
////		JsonResponse<Object> res = new JsonResponse<Object>();
////		String userId = "";
////		try {
////			userId = (String) ss.getAttribute("USER_ID");
////		} catch (Exception e1) {
////			e1.printStackTrace();
////		}
////		try {
////			res = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + dept + "&userId=" + userId, JsonResponse.class);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		if (res.getMessage() != null) {
////			res.setCode(res.getMessage());
////			res.setMessage("Unsuccess");
////		} else {
////			res.setMessage("success");
////		}
////		logger.info("Method : getApprovalStage ends");
////		return res;
////	}
//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/observation-forTransAudit") 
//	public String addAuditObservationforTransAudit(Model model, HttpSession session, @RequestParam Integer id,
//			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
//		logger.info("Method : addAuditObservationforTransAudit starts");
//	
//		model.addAttribute("Status", status);
//		model.addAttribute("ApproveStatus", approveStatus);
//		model.addAttribute("AuditId", id);
//		model.addAttribute("DeptId", deptId);
//		model.addAttribute("SectionId", sectionId);
//		
//		byte[] encodeByte=Base64.getDecoder().decode(deptId.getBytes());
//		String deptId1 = (new String(encodeByte));
//		byte[] encodeByte1=Base64.getDecoder().decode(sectionId.getBytes());
//		String sectionId1 = (new String(encodeByte1));
//		System.out.println(env.getAuditUrl() + "getQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1);
//		try {
//			
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addAuditObservationforTransAudit ends");
//		return "audit/add-audit-observationForTransAudit";
//	}
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "observation-forTransAudit-forward-to-approve", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> fowardedToApproveforTransAudit(@RequestBody List<AuditObservationModel> observation,
//			Model model, HttpSession session) {
//		logger.info("Method : fowardedToApproveforTransAudit function starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			// Variable question is used for ApprovalBy
//			for(AuditObservationModel m : observation) {
//				m.setQuestion(userId);
//			}
//			res = restClient.postForObject(env.getAuditUrl() + "approveObsInTransAudit", observation, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : fowardedToApproveforTransAudit function Ends");
//		return res;
//	}
//	@GetMapping("/approve-transaction-audit") 
//	public String appTransAuditMaster(Model model, HttpSession session) {
//		logger.info("Method : appTransAuditMaster starts");
//		
//		logger.info("Method : appTransAuditMaster starts");
//		return "audit/approveTransactionAudit";
//	}
//	
//	@SuppressWarnings("unchecked")
//	@GetMapping("/approve-transaction-audit-ThroughAjax")
//	public @ResponseBody DataTableResponse appTransAuditMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3, HttpSession session) {
//		logger.info("Method : appTransAuditMasterThroughAjax statrs");
//
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//			tableRequest.setParam3(param3);
//			tableRequest.setUserId(userId);
//
//			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllTransAuditMaster", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<AuditMasterModel> auditMasterModelList = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<AuditMasterModel>>() {
//					});
//			List<String> roles = null;
//			try {
//				roles = (List<String>) session.getAttribute("USER_ROLES");
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//
//			for (AuditMasterModel m : auditMasterModelList) {
//				String s = "";
//				byte[] deptId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
//				byte[] sectionId = Base64.getEncoder().encode(m.getSectionId().getBytes());
//				System.out.println("m.getIrStatus()"+m.getIrStatus());
//				if(m.getAuditNameId().equals("ADTM002")) {
//					if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//						m.setAuditAt("Co-ordinator");
//					} else if(m.getApproveStatus()==1) {
//						m.setAuditAt("Financial Head");
//					} else if(m.getApproveStatus()==2) {
//						m.setAuditAt("Audit Committee");
//					} else if(m.getApproveStatus()==3) {
//						m.setAuditAt("Co-ordinator");
//					} else if(m.getApproveStatus()==4 || m.getApproveStatus()==6) {
//						m.setAuditAt("Financial Head");
//					} else {
//						m.setAuditAt("Co-ordinator");
//					}
//				}
//				else if(m.getApproveStatus()==0 || m.getApproveStatus()==5) {
//					m.setAuditAt("Auditor");
//				} else if(m.getApproveStatus()==1) {
//					m.setAuditAt("GM");
//				} else if(m.getApproveStatus()==2) {
//					m.setAuditAt("Director");
//				} else if(m.getApproveStatus()==3) {
//					m.setAuditAt("Audit Committee");
//				} else if(m.getApproveStatus()==4) {
//					m.setAuditAt("Board Of Director");
//				} else {
//					m.setAuditAt("Auditor");
//				}
//
//				if(m.getAuditStatus() == 2 && m.getAuditNameId().equals("ADTM002")) {
//					 if(m.getIrStatus() == 4 && roles.contains("rol021") ) {
//							System.out.println("here Draft");
//							m.setStatus("Open");
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Draft Note' onclick='draftNote(\""
//									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-danger'>Draft Note</button> </a>";	
//							}
//					 else if(m.getIrStatus() == 5 && roles.contains("rol021") ||m.getIrStatus() == 6 && roles.contains("rol021")) {
//							System.out.println("here Draft");
//							m.setStatus("Open");
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='View Draft ' onclick='viewDraft(\""
//									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-success'>View Draft</button> </a>";	
//							}
//					 else  if(m.getApproveStatus()== 4 ){
//						 m.setStatus("Open");
//						 	s = s + "<a href='/audit/approve-draft?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//					 
//					 }
//					 else  if(m.getApproveStatus()== 6 ){
//						 m.setStatus("Open");
//						 	s = s + "<a href='/audit/approve-draftPara?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//					 
//					 }
//					 else  if(m.getApproveStatus()== 5 ){
//						 if(m.getIrStatus()==7 &&  roles.contains("rol021") ) {
//						 m.setStatus("Open");
//						 s = s + "&nbsp;&nbsp;&nbsp;<a title='Draft Para ' onclick='draftPara(\""
//									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-success'>Draft Para</button> </a>";	
//						 }
//						 else if( roles.contains("rol021")&& (m.getIrStatus()==8 || m.getIrStatus()==9) ) {
//							 m.setStatus("Open");
//							 s = s + "&nbsp;&nbsp;&nbsp;<a title=' view Draft Para ' onclick='viewDraftPara(\""
//										+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'>View Draft Para</button> </a>";	
//							 
//						 }
//					
//					 }
////					 else if(roles.contains("rol021")&& (m.getIrStatus()==9)) {
////							s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='viewDraftPara(\""
////									+ m.getAuditAutoGenId()
////									+ "\")'><button class='btn btn-success'> Forward Draft Para</button></a>";
////					 }
//					 else {
//						 	m.setStatus("Open");
//						 	s = s + "<a href='/audit/approve-transaction-audit-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-warning\"> View </button></a>";
//					 	} 
//					 }
//				
//				
//				 else if(m.getAuditStatus() == 3) {
//					m.setStatus("Closed");
//					s = s + "<a href='/audit/gm-audit-master-view?id=" + m.getAuditAutoGenId() + "'> <button class=\"btn btn-success\"> View Details </button></a>&nbsp;&nbsp;";
//					s = s + "<a href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'>Report</button></a>";
//				}
//				
//				else {
//					s=s+"N/A";
//				}
//				
//				 if(m.getIrStatus() == 6 && roles.contains("rol021")){
//					 s = s + "<a href='/audit/observation-forTransAudit?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-warning\"> Forward Draft </button></a>";
//								
//				}
//				 if(roles.contains("rol021")&& (m.getIrStatus()==9) && m.getApproveStatus()==5) {
//					 s = s + "<a href='/audit/observation-forTransAudit?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-warning\"> Forward Draft Para</button></a>";
//			
//				 }
//				 
//				m.setAction(s);
//			}
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(auditMasterModelList);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		logger.info("Method : appTransAuditMasterThroughAjax Theme ends");
//		return response;
//	}
//	@GetMapping("/approve-transaction-audit-view")  
//	public String approveTransactionObservation(Model model, HttpSession session, @RequestParam Integer id) {
//		logger.info("Method : approveTransactionObservation starts");
//		
//		model.addAttribute("AuditId", id);
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
//			List<DropDownModel> stage = Arrays.asList(obs);
//			
//			model.addAttribute("UserStage", stage.get(0).getName());
//			model.addAttribute("AuthorityStage", stage.get(0).getKey());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			System.out.println(questionList);
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
//			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
//			
//			model.addAttribute("commentsList", commentsList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : approveTransactionObservation starts");
//		return "audit/approveTransactionObservation";
//	}
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "approve-transaction-audit-forward-to-approve", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> fowardedTransToApproveStatus(@RequestBody List<AuditObservationModel> observation,
//			Model model, HttpSession session) {
//		logger.info("Method : fowardedTransToApproveStatus function starts");
//		
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			// Variable question is used for ApprovalBy
//			for(AuditObservationModel m : observation) {
//				m.setQuestion(userId);
//			}
//			res = restClient.postForObject(env.getAuditUrl() + "forwardToApproveObservation", observation, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : fowardedTransToApproveStatus function Ends");
//		return res;
//	}
//
//	/**
//	 * Web Controller - Upload Document
//	 *
//	 */
//	@PostMapping("/approve-transaction-audit-uploadFile")
//	public @ResponseBody JsonResponse<Object> approveviewUploadFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : uploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageFile", inputFile);
//			System.out.println("inputFile" + inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : uploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//	/*
//	* approve accept
//	*
//	*
//	*/
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "approve-transaction-audit-master-sendDraft" })
//	public @ResponseBody JsonResponse<Object> sendDraft(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendRequestRequisition starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				System.out.println(contentName);
//
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendDraft?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendDraft?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendDraft ends");
//		System.out.println(resp);
//		return resp;
//	}
//	/*
//	 * get Mapping for view Draft
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/approve-transaction-audit-master-getDraft", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getDraft(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :getDraft function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getDraft?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getDraft function Ends");
//		return res;
//	}
//	/*
//	 * get Mapping for view IrStatus
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-audit-master-getDraft", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getmstDraft(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :getmstDraft function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getDraft?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getmstDraft function Ends");
//		return res;
//	}
//	/**
//	 * Web Controller - Upload Document
//	 *
//	 */
//	@PostMapping("/view-audit-master-uploadComplianceFile")
//	public @ResponseBody JsonResponse<Object> uploadComplianceFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : uploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("imageFile", inputFile);
//			System.out.println("inputFile" + inputFile);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : uploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//	/*
//	* sendCompliance
//	*
//	*
//	*/
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-audit-master-sendCompliance" })
//	public @ResponseBody JsonResponse<Object> sendCompliance(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendCompliance starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				System.out.println(contentName);
//
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendCompliance?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendCompliance?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendCompliance ends");
//		System.out.println(resp);
//		return resp;
//	}
//	@GetMapping("/approve-draft")  
//	public String approveDraftObservation(Model model, HttpSession session, @RequestParam Integer id) {
//		logger.info("Method : approveDraftObservation starts");
//		
//		model.addAttribute("AuditId", id);
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
//			List<DropDownModel> stage = Arrays.asList(obs);
//			
//			model.addAttribute("UserStage", stage.get(0).getName());
//			model.addAttribute("AuthorityStage", stage.get(0).getKey());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			System.out.println(questionList);
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
//			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
//			
//			model.addAttribute("commentsList", commentsList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : approveDraftObservation starts");
//		return "audit/approveDraftObservation";
//	}
//	/*
//	* approve accept
//	*
//	*
//	*/
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "approve-transaction-audit-master-sendDraftPara" })
//	public @ResponseBody JsonResponse<Object> sendDraftPara(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendDraftPara starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				System.out.println(contentName);
//
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendDraftPara?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendDraftPara?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendDraftPara ends");
//		System.out.println(resp);
//		return resp;
//	}
//	/*
//	 * get Mapping for view draft Para
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/approve-transaction-audit-master-getDraftPara", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getDraftPara(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :getDraftPara function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getDraftPara?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getDraftPara function Ends");
//		return res;
//	}
//	/*
//	 * get Mapping for view draft Para
//	 */
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/view-audit-master-getDraftPara", method = { RequestMethod.GET })
//	public @ResponseBody JsonResponse<RequisitionViewModel> getMstrDraftPara(@RequestParam("id") String id, Model model,
//			HttpSession session) {
//		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
//		logger.info("Method :getMstrDraftPara function starts");
//
//		try {
//			System.out.println(id);
//			res = restClient.getForObject(env.getAuditUrl() + "getDraftPara?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		String message = res.getMessage();
//
//		if (message != null && message != "") {
//
//		} else {
//			res.setMessage("Success");
//		}
//		logger.info("Method : getMstrDraftPara function Ends");
//		return res;
//	}
//	/*
//	* sendCompliance
//	*
//	*
//	*/
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-audit-master-sendParaCompliance" })
//	public @ResponseBody JsonResponse<Object> sendParaCompliance(Model model, @RequestParam("id") String encodeId,
//			@RequestParam("comment") String comment, HttpSession session) throws IOException {
//		logger.info("Method : sendCompliance starts");
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//		}
//		try {
//			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
//			String contentName = "";
//			if (inputFile != null) {
//				long nowTime = new Date().getTime();
//
//				byte[] bytes = inputFile.getBytes();
//				String[] fileType = inputFile.getContentType().split("/");
//				System.out.println("CType: " + fileType[1]);
//
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//					contentName = nowTime + ".docx";
//				} else if (fileType[1].contentEquals("msword")) {
//					contentName = nowTime + ".doc";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals(".xlsx")) {
//					contentName = nowTime + ".xls";
//				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
//					contentName = nowTime + ".xls";
//				} else {
//					contentName = nowTime + "." + fileType[1];
//				}
//
//				System.out.println(contentName);
//
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//				if (resp.getCode().contains("Data Saved Successfully")) {
//					Path path = Paths.get(env.getFileUploadAudit() + contentName);
//					if (fileType[1].contentEquals("pdf")) {
//						Files.write(path, bytes);
//					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
//							|| fileType[1].contentEquals("msword")) {
//						Files.write(path, bytes);
//					} else {
//
//						Files.write(path, bytes);
//
//						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//						Integer height = 50;
//						Integer width = 50;
//
//						try {
//							BufferedImage img = ImageIO.read(in);
//							if (height == 0) {
//								height = (width * img.getHeight()) / img.getWidth();
//							}
//							if (width == 0) {
//								width = (height * img.getWidth()) / img.getHeight();
//							}
//
//							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
//
//							ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//							ImageIO.write(imageBuff, "png", buffer);
//
//							// ByteArrayOutputStream out = new ByteArrayOutputStream();
//							byte[] thumb = buffer.toByteArray();
//							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
//							Files.write(pathThumb, thumb);
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//					}
//				}
//
//			} else {
//				try {
//					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
//							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
//							JsonResponse.class);
//				} catch (RestClientException e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : sendParaCompliance ends");
//		System.out.println(resp);
//		return resp;
//	}
//	@GetMapping("/approve-draftPara")  
//	public String approveDraftParaObservation(Model model, HttpSession session, @RequestParam Integer id) {
//		logger.info("Method : approveDraftParaObservation starts");
//		
//		model.addAttribute("AuditId", id);
//		
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		try {
//			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
//			List<DropDownModel> stage = Arrays.asList(obs);
//			
//			model.addAttribute("UserStage", stage.get(0).getName());
//			model.addAttribute("AuthorityStage", stage.get(0).getKey());
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			System.out.println(questionList);
//			model.addAttribute("questionList", questionList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
//			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
//			
//			model.addAttribute("commentsList", commentsList);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Method : approveDraftParaObservation starts");
//		return "audit/approveDraftObservation";
//	}
	public String saveAllImage(String document,byte[] imageBytes) {
		logger.info("Method : saveAllImage starts");

		String imageName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1=str.concat("-");
				 String str4 = str1.concat(document);
				 System.out.println("Bikash "+str4);
				imageName = str4+ ".png";
			}

			Path path = Paths.get(env.getFileUploadAudit() + imageName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);

				ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);
				Integer height = 50;
				Integer width = 50;

				try {
					BufferedImage img = ImageIO.read(in);
					if (height == 0) {
						height = (width * img.getHeight()) / img.getWidth();
					}
					if (width == 0) {
						width = (height * img.getWidth()) / img.getHeight();
					}

					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

					BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					ImageIO.write(imageBuff, "png", buffer);

					byte[] thumb = buffer.toByteArray();

					Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + imageName);
					Files.write(pathThumb, thumb);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllImage ends");
		System.out.println("imageName123 "+imageName);
		return imageName;
	}
/*
 * for save all pdf in folder and return name
 */

public String saveAllPdf(String document,byte[] imageBytes) {
	logger.info("Method : saveAllPdf starts");

	String pdfName = null;

	try {
		if (imageBytes != null) {
			long nowTime = new Date().getTime();
			
			String str = Long.toString(nowTime);
			String str1=str.concat("-");
			 String str4 = str1.concat(document);
			 System.out.println("Bikash "+str4);
			pdfName = str4 + ".pdf";
		}

		Path path = Paths.get(env.getFileUploadAudit() + pdfName);
		//System.out.println("path " + path);
		if (imageBytes != null) {
			Files.write(path, imageBytes);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	logger.info("Method : saveAllPdf ends");
	return pdfName;
}

public String saveAllDocx(String document,byte[] imageBytes) {
	logger.info("Method : saveAllDocx starts");

	String pdfName = null;

	try {
		if (imageBytes != null) {
			long nowTime = new Date().getTime();
			String str = Long.toString(nowTime);
			String str1=str.concat("-");
			 String str4 = str1.concat(document);
			 System.out.println("Bikash "+str4);
			pdfName = str4 + ".docx";
		}

		Path path = Paths.get(env.getFileUploadAudit() + pdfName);
		//System.out.println("path" + path);
		if (imageBytes != null) {
			Files.write(path, imageBytes);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	logger.info("Method : saveAllDocx ends");
	return pdfName;
}

public String saveAllDoc(String document,byte[] imageBytes) {
	logger.info("Method : saveAllDoc starts");

	String pdfName = null;

	try {
		if (imageBytes != null) {
			long nowTime = new Date().getTime();
			String str = Long.toString(nowTime);
			String str1=str.concat("-");
			 String str4 = str1.concat(document);
			 System.out.println("Bikash "+str4);
			pdfName = str4 + ".doc";
		}

		Path path = Paths.get(env.getFileUploadAudit() + pdfName);
		//System.out.println("path" + path);
		if (imageBytes != null) {
			Files.write(path, imageBytes);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	logger.info("Method : saveAllDoc ends");
	return pdfName;
}

public String saveAllXlsx(String document,byte[] imageBytes) {
	logger.info("Method : saveAllDoc starts");

	String pdfName = null;

	try {
		if (imageBytes != null) {
			long nowTime = new Date().getTime();
			String str = Long.toString(nowTime);
			String str1=str.concat("-");
			 String str4 = str1.concat(document);
			 System.out.println("Bikash "+str4);
			pdfName = str4 + ".xlsx";
		}

		Path path = Paths.get(env.getFileUploadAudit() + pdfName);
		//System.out.println("path" + path);
		if (imageBytes != null) {
			Files.write(path, imageBytes);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	logger.info("Method : saveAllDoc ends");
	return pdfName;
}

public String saveAllXls(String document,byte[] imageBytes) {
	logger.info("Method : saveAllDoc starts");

	String pdfName = null;

	try {
		if (imageBytes != null) {
			long nowTime = new Date().getTime();
			String str = Long.toString(nowTime);
			String str1=str.concat("-");
			 String str4 = str1.concat(document);
			 System.out.println("Bikash "+str4);
			pdfName = str4 + ".xls";
		}

		Path path = Paths.get(env.getFileUploadAudit() + pdfName);
		//System.out.println("path" + path);
		if (imageBytes != null) {
			Files.write(path, imageBytes);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	logger.info("Method : saveAllDoc ends");
	return pdfName;
}
}
