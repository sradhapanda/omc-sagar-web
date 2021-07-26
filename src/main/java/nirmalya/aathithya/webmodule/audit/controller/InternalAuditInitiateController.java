
package nirmalya.aathithya.webmodule.audit.controller;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
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
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nirmalya.aathithya.webmodule.audit.model.AuditDetailModel;
import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;
import nirmalya.aathithya.webmodule.audit.model.IntitiateInternalAuditModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author NirmalyLabs
 *
 */
@Controller
@RequestMapping(value = "audit")
public class InternalAuditInitiateController {

	Logger logger = LoggerFactory.getLogger(InternalAuditInitiateController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Initiate Audit' page
	 *
	 */
	@GetMapping("/initiate-internal-audit")
	public String initiateAudit(Model model, HttpSession session) {

		logger.info("Method : defaultCircleMaster starts");

		/**
		 * AUDIT TYPE DROP DOWN
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getInternalAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] org = restClient.getForObject(env.getAuditUrl() + "getRegion", DropDownModel[].class);
			List<DropDownModel> regionList = Arrays.asList(org);
			
			model.addAttribute("regionList", regionList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getQuarter", DropDownModel[].class);
			List<DropDownModel> getQuarterList = Arrays.asList(audit);

			model.addAttribute("getQuarterList", getQuarterList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		AuditInitiateModel auditInitiate = new AuditInitiateModel();
		try {
			AuditInitiateModel auditInitiateSession = (AuditInitiateModel) session.getAttribute("sauditInitiate");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (auditInitiateSession != null) {
				model.addAttribute("auditInitiate", auditInitiateSession);
				session.setAttribute("sauditInitiate", null);
			} else {
				model.addAttribute("auditInitiate", auditInitiate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultCircleMaster ends");
		return "audit/initiateInternalAudit";
	}

	
	
	/**
	* get Auditor
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getAuditor" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getAuditor(Model model,
			@RequestParam("region") String region, HttpSession session) {
		logger.info("Method : getAuditor starts");
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getInternalAuditor?region=" + region,
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
		logger.info("Method : getAuditor ends");
	//	System.out.println(res);
		return res;
	}
	/**
	* get Auditor
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getRegionalManager" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getRegionalManager(Model model,
			@RequestParam("region") String region, HttpSession session) {
		logger.info("Method : getRegionalManager starts");
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getRegionalManager?auditor=" + region,
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
		logger.info("Method : getRegionalManager ends");
		//System.out.println(res);
		return res;
	}
	
	/**
	* get Auditor
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getConcernFinance" })
	public @ResponseBody JsonResponse<List<DropDownModel>> getConcernFinance(Model model,
			@RequestParam("region") String region, HttpSession session) {
		logger.info("Method : getConcernFinance starts");
		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getConcernFinance?region=" + region,
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
		logger.info("Method : getConcernFinance ends");
		//System.out.println(res);
		return res;
	}
	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/initiate-internal-audit-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
			//System.out.println(inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "initiate-internal-audit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> initiateAudit(@RequestBody IntitiateInternalAuditModel audit, Model model,
			HttpSession session) throws IOException {

		logger.info("Method : initiateAudit function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile"+inputFile);
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
			//	System.out.println("CType: " + fileType[1]);

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.presentationml.presentation")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
					audit.setDocument(contentName);
					audit.setCreatedBy(userId);
				res = restClient.postForObject(env.getAuditUrl() + "initiate-internal-audit", audit, JsonResponse.class);

				if (res.getCode().contains("Data Saved Successfully")) {
					Path path = Paths.get(env.getFileUploadAudit() + contentName);
					if (fileType[1].contentEquals("pdf")) {
						Files.write(path, bytes);
					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
							|| fileType[1].contentEquals("msword")) {
						Files.write(path, bytes);
					} else {

						Files.write(path, bytes);

						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
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
							if (width != null && height != null) {
								Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
								BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
								imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

								ByteArrayOutputStream buffer = new ByteArrayOutputStream();

								ImageIO.write(imageBuff, "png", buffer);

								// ByteArrayOutputStream out = new ByteArrayOutputStream();
								byte[] thumb = buffer.toByteArray();
								Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
								Files.write(pathThumb, thumb);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				res = restClient.postForObject(env.getAuditUrl() + "initiate-internal-audit", audit, JsonResponse.class);
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		logger.info("Method : initiateAudit function Ends");
		return res;
	}
	
	

	
	/**
	*  saveSummery
	*
	*/
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "initiate-internal-audit-saveSummery" })
	public @ResponseBody JsonResponse<Object> saveSummery(Model model,
			@RequestBody String summery, BindingResult result) {
		logger.info("Method : saveSummery starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveSummery" , summery, JsonResponse.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveSummery ends");
		return res;
	}


	
	/**
	* get Auditor
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getDesignation" })
	public @ResponseBody JsonResponse<List<AuditInitiateModel>> getDesignation(Model model,
			@RequestParam("auditor") String auditor, HttpSession session) {
		logger.info("Method : getDesignation starts");
		JsonResponse<List<AuditInitiateModel>> res = new JsonResponse<List<AuditInitiateModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() +"getDesignation?auditor=" + auditor,
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
		logger.info("Method : getDesignation ends");
		//System.out.println(res);
		return res;
	}
	/**
	*  saveSummery
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getSummery" })
	public @ResponseBody JsonResponse<DropDownModel> getSummery(Model model,
			HttpSession session) {
		logger.info("Method : getSummery starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getSummery" ,JsonResponse.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getSummery ends");
		//System.out.println(res);
		return res;
	}
	/*
	* approve accept
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiate-internal-audit-details" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendDetails(Model model, @RequestBody AuditInitiateModel obj,BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : sendDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		obj.setCreatedBy(userId);
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile"+inputFile);
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				//System.out.println("CType: " + fileType[1]);

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.presentationml.presentation")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				}else if (fileType[1].contentEquals("xls")) {
					contentName = nowTime + ".xls";
				}  else {
					contentName = nowTime + "." + fileType[1];
				}

				obj.setDocument(contentName);
					resp = restClient.postForObject(env.getAuditUrl() + "sendDetails",obj,
							JsonResponse.class);
				
				if (resp.getCode().contains("Data Saved Successfully")) {
					Path path = Paths.get(env.getFileUploadAudit() + contentName);
					//System.out.println("Path"+path);
					if (fileType[1].contentEquals("pdf")) {
						Files.write(path, bytes);
					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
							|| fileType[1].contentEquals("msword")) {
						Files.write(path, bytes);
					}else {

						Files.write(path, bytes);

						ByteArrayInputStream in = new ByteArrayInputStream(bytes);
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
							if (width != null && height != null) {
								Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
								BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
								imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

								ByteArrayOutputStream buffer = new ByteArrayOutputStream();

								ImageIO.write(imageBuff, "png", buffer);

								// ByteArrayOutputStream out = new ByteArrayOutputStream();
								byte[] thumb = buffer.toByteArray();
								Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
								Files.write(pathThumb, thumb);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				resp = restClient.postForObject(env.getAuditUrl() + "sendDetails",obj,
						JsonResponse.class);
			
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : sendDetails ends");
		//System.out.println(resp);
		return resp;
	}
	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-initiate-internal-audit-uploadFile")
	public @ResponseBody JsonResponse<Object> viewUploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
			//System.out.println("inputFile" + inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}
	
	/*
	* get Mapping for view document
	*/

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-initiate-internal-audit-viewDetails", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<AuditDetailModel> viewDetails(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<AuditDetailModel> res = new JsonResponse<AuditDetailModel>();
		logger.info("Method : viewDetails function starts");
		try {
			
			res = restClient.getForObject(env.getAuditUrl() + "viewDetails?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		
		//System.out.println(res);
		logger.info("Method : viewDetails function Ends");
		return res;
	}
	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiate-internal-audit-saveNotes" })
	public @ResponseBody JsonResponse<Object> saveNotes(Model model, @RequestBody AuditInitiateModel obj, BindingResult result) {
		logger.info("Method : saveNotes starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveNotes" , obj,
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
		logger.info("Method : saveNotes ends");
		return res;
	}
	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiate-internal-audit-getNotes" })
	public @ResponseBody JsonResponse<DropDownModel> getNotes(Model model, @RequestBody String id, BindingResult result) {
		logger.info("Method : saveNotes starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "getNotes" , id,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getNotes ends");
		return res;
	}
	
	/**
	* get Organization
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-internal-audit-getOrganization" })
	public @ResponseBody JsonResponse<List<AuditInitiateModel>> getOrganization(Model model,
			@RequestParam("auditType") String auditType, HttpSession session) {
		logger.info("Method : getOrganization starts");
		JsonResponse<List<AuditInitiateModel>> res = new JsonResponse<List<AuditInitiateModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() +"getOrganization?auditType=" + auditType,
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
		///System.out.println(res);
		return res;
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-internal-audit")
	public String viewInitiatedAudit(Model model, HttpSession session) {
		logger.info("Method : viewInitiatedAudit starts");

		logger.info("Method : viewInitiatedAudit ends");
		return "audit/view-internal-audit-initiate";
	}

	/*
	* For view work type for dataTable Ajax call
	*/
	@SuppressWarnings("unchecked")
	@GetMapping("/view-initiated-internal-audit-ThroughAjax")
	public @ResponseBody DataTableResponse viewInitaiedAuditThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {

		logger.info("Method : viewInitaiedAuditThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
	
		try {
			List<String> userRole = (List<String>) session.getAttribute("USER_ROLES");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

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
				if (userRole.contains("rol021")) {
				if (m.getInitiatedStatus() == 2 && userRole.contains("rol021")) {
					s = s + " <i class='fa fa-calendar-check-o faIcon' aria-hidden=\"true\" ></i>";
				}  else if (m.getAuditTypeId().equals("ADTM006") && m.getInitiatedStatus() == 1  && m.getDtlStatus() ) {
					s = s + "<a title='Schedule' href='/audit/add-audit-master?id=" + new String(encodeId)
							+ "'> <i class='fa fa-calendar faIcon' aria-hidden=\"true\"></i></a>";
				} else if (m.getAuditTypeId().equals("ADTM005") && m.getInitiatedStatus() == 1  && m.getDtlStatus() ) {
					s = s + "<a title='Schedule' href='/audit/add-internal-audit-master?id=" + new String(encodeId)
							+ "'> <i class='fa fa-calendar faIcon' aria-hidden=\"true\"></i></a>";
				} else if( !userRole.contains("rol024") ){
					s = s + ("N/A");
				}
			}
				if (userRole.contains("rol024") ) {
					if(m.getDtlStatus() && m.getAuditTypeId().equals("ADTM006")) {
						s = s + "&nbsp;&nbsp;<a title='View Details' onclick='viewDetails(\"" +  new String(encodeId)+ "\")' ><i class='fa fa-book faIcon' aria-hidden=\"true\"></i> </a>";
						
					}if(m.getDtlStatus() && m.getAuditTypeId().equals("ADTM005")) {
						s = s + "&nbsp;&nbsp;<a title='View Details' onclick='viewDetails(\"" +  new String(encodeId)+ "\")' ><i class='fa fa-book faIcon' aria-hidden=\"true\"></i> </a>";
						
					}if(!m.getDtlStatus() && m.getAuditTypeId().equals("ADTM005")) {
						
						s = s + "&nbsp;&nbsp;<a title='Details Of Audit' onclick='detailsOfAudit(\"" +  new String(encodeId)
								+ "\")' ><i class='fa fa-file faIcon' aria-hidden=\"true\"></i> </a>";
					}if(!m.getDtlStatus() && m.getAuditTypeId().equals("ADTM006")) {
						
						s = s + "&nbsp;&nbsp;<a title='Details Of Audit' onclick='detailsOfAudit(\"" +  new String(encodeId)
								+ "\")' ><i class='fa fa-file faIcon' aria-hidden=\"true\"></i> </a>";
					}
					
				}
				
				String imagePath = "";
				if (m.getDocument() != null && m.getDocument() != "") {
					String[] extension = m.getDocument().split("\\.");
					if (extension[1].contains("doc") || extension[1].contains("docx")) {
						imagePath = "/document/audit/excel/" + m.getDocument();
					} else if (extension[1].contains("xls")) {
						imagePath = "/document/audit/excel/" + m.getDocument();
					} else {
						imagePath = "/document/audit/" + m.getDocument();
					}
				} else {
					m.setDocument("N/A");
				}
				m.setDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getDocument() + "</a>");

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
}
