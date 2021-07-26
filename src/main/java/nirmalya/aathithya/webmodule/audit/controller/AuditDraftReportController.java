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

import nirmalya.aathithya.webmodule.audit.model.AuditAttachmentNewModel;
import nirmalya.aathithya.webmodule.audit.model.AuditDraftReportModel;
import nirmalya.aathithya.webmodule.audit.model.AuditDraftReportModelNew;
import nirmalya.aathithya.webmodule.audit.model.AuditReportAdditionalInfoModel;
import nirmalya.aathithya.webmodule.audit.model.AuditReportBasicsQualifyModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping(value = "audit")
public class AuditDraftReportController {
	Logger logger = LoggerFactory.getLogger(AuditDraftReportController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/audit-draft-report")
	public String addAuditDraftReport(Model model, HttpSession session) {

		logger.info("Method : addAuditDraftReport starts");

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

		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditReportType",
					DropDownModel[].class);
			List<DropDownModel> reportTypeList = Arrays.asList(audit);

			model.addAttribute("reportTypeList", reportTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/** Department **/
		try {
			DropDownModel[] type = restClient.getForObject(env.getAuditUrl() + "getReportCtgList",
					DropDownModel[].class);
			List<DropDownModel> reportCtgList = Arrays.asList(type);

			model.addAttribute("reportCtgList", reportCtgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/* for employee to and cc details */
		try {
		DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getccAndToempList", DropDownModel[].class);
		List<DropDownModel> ccAndToList = Arrays.asList(program);
		model.addAttribute("empList", ccAndToList);

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
		logger.info("Method : addAuditDraftReport ends");

		return "audit/audit-draft-report";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "audit-draft-report-get-refno" })
	public @ResponseBody JsonResponse<Object> getRefList(Model model, @RequestBody String dept, BindingResult result) {
		logger.info("Method : getRefList starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getRefListByAudiType?id=" + dept, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getRefList ends");
		return res;
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "audit-draft-report-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addNewAuditReport(@RequestBody List<AuditDraftReportModel> audit,
			Model model, HttpSession session) throws IOException {

		logger.info("Method : addNewAuditReport function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int imagecount = 0;
		for (AuditDraftReportModel a : audit.get(0).getAttachmment()) {
			if (a.getAttachDocument() != null && a.getAttachDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getAttachDocument().split(delimiters);

				if (a.getAttachfile() != null && !a.getAttachfile().contains("empty")) {
					if (x[1].contentEquals("png") || x[1].contentEquals("jpg") || x[1].contentEquals("jpeg")) {

						for (String s1 : a.getAttachfile()) {
							if (s1 != null)
								try {
									byte[] bytes = Base64.getDecoder().decode(s1);
									String imageName = saveAllImage(bytes);
									a.setAttachDocument(imageName);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					} else if (x[1].contentEquals("pdf")) {
						for (String s1 : a.getAttachfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllPdf(bytes);
								a.setAttachDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("docx")) {
						for (String s1 : a.getAttachfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDocx(bytes);
								a.setAttachDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("doc")) {
						for (String s1 : a.getAttachfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDoc(bytes);
								a.setAttachDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xls")) {
						for (String s1 : a.getAttachfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXls(bytes);
								a.setAttachDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xlsx")) {
						for (String s1 : a.getAttachfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXlsx(bytes);
								a.setAttachDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

			}
			imagecount = imagecount + 1;
		}

		int imagecount2 = 0;
		for (AuditReportAdditionalInfoModel a : audit.get(0).getAdditionalInfo()) {
			if (a.getInfoDocument() != null && a.getInfoDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getInfoDocument().split(delimiters);

				if (a.getInfofile() != null && !a.getInfofile().contains("empty")) {
					if (x[1].contentEquals("png") || x[1].contentEquals("jpg") || x[1].contentEquals("jpeg")) {

						for (String s1 : a.getInfofile()) {
							if (s1 != null)
								try {
									byte[] bytes = Base64.getDecoder().decode(s1);
									String imageName = saveAllImage(bytes);
									a.setInfoDocument(imageName);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					} else if (x[1].contentEquals("pdf")) {
						for (String s1 : a.getInfofile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllPdf(bytes);
								a.setInfoDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("docx")) {
						for (String s1 : a.getInfofile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDocx(bytes);
								a.setInfoDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("doc")) {
						for (String s1 : a.getInfofile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDoc(bytes);
								a.setInfoDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xls")) {
						for (String s1 : a.getInfofile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXls(bytes);
								a.setInfoDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xlsx")) {
						for (String s1 : a.getInfofile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXlsx(bytes);
								a.setInfoDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					a.setInfoDocument(a.getInfoFileName());
				}

			}
			imagecount2 = imagecount2 + 1;
		}

		int imagecount3 = 0;
		for (AuditReportBasicsQualifyModel a : audit.get(0).getBasicOfQualify()) {
			if (a.getQlfyDocument() != null && a.getQlfyDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getQlfyDocument().split(delimiters);

				if (a.getQlfyfile() != null && !a.getQlfyfile().contains("empty")) {
					if (x[1].contentEquals("png") || x[1].contentEquals("jpg") || x[1].contentEquals("jpeg")) {

						for (String s1 : a.getQlfyfile()) {
							if (s1 != null)
								try {
									byte[] bytes = Base64.getDecoder().decode(s1);
									String imageName = saveAllImage(bytes);
									a.setQlfyDocument(imageName);
								} catch (Exception e) {
									e.printStackTrace();
								}
						}
					} else if (x[1].contentEquals("pdf")) {
						for (String s1 : a.getQlfyfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllPdf(bytes);
								a.setQlfyDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("docx")) {
						for (String s1 : a.getQlfyfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDocx(bytes);
								a.setQlfyDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("doc")) {
						for (String s1 : a.getQlfyfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllDoc(bytes);
								a.setQlfyDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xls")) {
						for (String s1 : a.getQlfyfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXls(bytes);
								a.setQlfyDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (x[1].contentEquals("xlsx")) {
						for (String s1 : a.getQlfyfile()) {
							try {
								byte[] bytes = Base64.getDecoder().decode(s1);
								String pdfName = saveAllXlsx(bytes);
								a.setQlfyDocument(pdfName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					a.setQlfyDocument(a.getQlfyFileName());
				}

			}
			imagecount3 = imagecount3 + 1;
		}
		
		try {
			res = restClient.postForObject(env.getAuditUrl() + "addNewAuditReport", audit, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		logger.info("Method : addNewAuditReport function Ends");
		return res;
	}
	@PostMapping("/add-draft-report-uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	@PostMapping("/add-draft-report-uploadFile1")
	public @ResponseBody JsonResponse<Object> uploadFile1(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	@PostMapping("/add-draft-report-uploadFile2")
	public @ResponseBody JsonResponse<Object> uploadFile2(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-audit-draft-report")
	public String viewAuditDraftReport(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewAuditDraftReport starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditNosByType?id="+id1, DropDownModel[].class);
			List<DropDownModel> auditNoList = Arrays.asList(audit);
			for(DropDownModel m:auditNoList) {
				byte[] encodeId = Base64.getEncoder().encode(m.getKey().getBytes());
				m.setName(new String(encodeId));
			}
			model.addAttribute("auditNoList", auditNoList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAuditDraftReport ends");
		model.addAttribute("auditType",id);
		model.addAttribute("auditTypeId",id1);
		return "audit/view-audit-draft-report";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-draft-report-ThroughAjax")
	public @ResponseBody DataTableResponse viewAuditDraftReport(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewAuditDraftReport statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam1(param2);
			//tableRequest.setParam1(param3);

			JsonResponse<List<AuditDraftReportModel>> jsonResponse = new JsonResponse<List<AuditDraftReportModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getauditDraftDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditDraftReportModel> concession = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditDraftReportModel>>() {
					});

			String s = "";

			for (AuditDraftReportModel m : concession) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getDraftId().getBytes());
				byte[] encodeId2 = Base64.getEncoder().encode(m.getInitiateId().getBytes());

				/*
				 * s = s + "<a href='view-audit-draft-report-edit-new?id=" + new
				 * String(encodeId) + "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;";
				 */
				s = s + "<a title='View'  href='/audit/view-audit-draft-report-details?id="+ new String(encodeId2)+"'><i class='fa fa-eye faIcon'></i></a>";
				m.setAction(s);

				m.setDraftId("<a href='/audit/view-audit-draft-report-details?id="+ new String(encodeId2)+"'>"
						+ m.getDraftId() + "</a>");

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(concession);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewAuditDraftReport Theme ends");

		return response;
	}

	/*
	 * Get Mapping view audit Draft
	 */
	@GetMapping("/draft-report")
	public String draftReport(Model model, HttpSession session) {

		logger.info("Method : draftReport starts");

		logger.info("Method : draftReport ends");

		return "audit/draft-report";
	}

	/*
	 * Get Mapping view audit master
	 */
	
	@GetMapping("view-audit-draft-report-detailss")
	public String draftReportDetail(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : draftReportDetail starts");
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		AuditDraftReportModel[] auditMasterModel = restClient
				.getForObject(env.getAuditUrl() + "getdraftReportById?id=" + id, AuditDraftReportModel[].class);
		List<AuditDraftReportModel> reportdtls = Arrays.asList(auditMasterModel);

		for (AuditDraftReportModel a : reportdtls) {

			if (a.getAttachDocument() != null && a.getAttachDocument() != "") {
				String docPath = "<a href=\"/document/audit/excel/" + a.getAttachDocument()
						+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title="+a.getAttachfileName()+"></i> </a>";
				// String docPath = "";
				a.setAttachDocument(docPath);
				model.addAttribute("reportdtls", reportdtls);

			}
		}

		logger.info("Method : draftReportDetail ends");

		return "audit/draft-report";
	}



	/*
	 * for Edit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-draft-report-edit")
	public String editReport(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editReport starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(decodeId));

		String userId = "";

		
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

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

		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditReportType",
					DropDownModel[].class);
			List<DropDownModel> reportTypeList = Arrays.asList(audit);

			model.addAttribute("reportTypeList", reportTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/** Department **/
		try {
			DropDownModel[] type = restClient.getForObject(env.getAuditUrl() + "getReportCtgList",
					DropDownModel[].class);
			List<DropDownModel> reportCtgList = Arrays.asList(type);

			model.addAttribute("reportCtgList", reportCtgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** Department **/
		try {
			DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
			List<DropDownModel> programList = Arrays.asList(program);

			model.addAttribute("empList", programList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getrefNoList",
					PersonListModel[].class);
			List<PersonListModel> refNoList = Arrays.asList(dept);

			model.addAttribute("refNoList", refNoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<List<AuditDraftReportModel>> response = new JsonResponse<List<AuditDraftReportModel>>();
		try {

			response = restClient.getForObject(env.getAuditUrl() + "getdraftReporteditById?id=" + id,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditDraftReportModel> report = mapper.convertValue(response.getBody(),
					new TypeReference<List<AuditDraftReportModel>>() {
					});

			model.addAttribute("reportdtls", report);
			model.addAttribute("edit", report.get(0).getDraftId());
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */

		logger.info("Method : editReport ends");

		return "audit/audit-draft-report";
	}
	/*
	 * for Edit draft report
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-draft-report-edit-new")
	public String editReportNew(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editReport starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(decodeId));

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

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

		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditReportType",
					DropDownModel[].class);
			List<DropDownModel> reportTypeList = Arrays.asList(audit);

			model.addAttribute("reportTypeList", reportTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/** Department **/
		try {
			DropDownModel[] type = restClient.getForObject(env.getAuditUrl() + "getReportCtgList",
					DropDownModel[].class);
			List<DropDownModel> reportCtgList = Arrays.asList(type);

			model.addAttribute("reportCtgList", reportCtgList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** Department **/
		try {
			DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
			List<DropDownModel> programList = Arrays.asList(program);

			model.addAttribute("empList", programList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getrefNoList",
					PersonListModel[].class);
			List<PersonListModel> refNoList = Arrays.asList(dept);

			model.addAttribute("refNoList", refNoList);


		} catch (RestClientException e) {
			e.printStackTrace();
		}

		JsonResponse<AuditDraftReportModelNew> response = new JsonResponse<AuditDraftReportModelNew>();
		try {

			response = restClient.getForObject(env.getAuditUrl() + "getdraftReporteditByIdNew?id=" + id,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			AuditDraftReportModelNew report = mapper.convertValue(response.getBody(),
					new TypeReference<AuditDraftReportModelNew>() {
					});

			model.addAttribute("reportdtls", report);
			model.addAttribute("edit", report.getDraftId());

			if (report != null) { 
				List<AuditAttachmentNewModel> attachmentList = report.getAuditAttachmentNewModelList();
				List<AuditReportAdditionalInfoModel> additionalDocList = report.getAdditionalInfoList();
				List<AuditReportBasicsQualifyModel> basicQualifInfoList = report.getBasicOfQualifyList();
				for (AuditAttachmentNewModel m : attachmentList) {

					String imagePath = "";
					if (m.getAttachDocument() != null && m.getAttachDocument() != "") {
						m.setAttachDocumentName(m.getAttachDocument());
						String[] extension = m.getAttachDocument().split("\\.");
						if (extension.length == 2) {
							if (extension[1].equals("xls") || extension[1].equals("xlsx")) {

								String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
										+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title="
										+ m.getAttachDocument() + "></i> </a>";

								m.setAttachDocument(docPath);
							}
							if (extension[1].equals("pdf")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
										+ "\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title="
										+ m.getAttachDocument() + " ;></i> </a>";

								m.setAttachDocument(docPath);
							}
							if (extension[1].equals("doc") || extension[1].equals("dox") || extension[1].equals("docx")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
										+ "\"><i class=\"fa fa-file-word-o fa-2x  \" aria-hidden=\"true\" title="
										+ m.getAttachDocument() + "></i> </a>";
								m.setAttachDocument(docPath);
							}
							if (extension[1].equals("png") || extension[1].equals("jpg")
									|| extension[1].equals("jpeg")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
										+ "\"><i class=\"fa fa-file-image-o \" aria-hidden=\"true\" title="
										+ m.getAttachDocument() + " ></i> </a>";
								m.setAttachDocument(docPath);
							}
						} else {
							m.setAttachDocument("N/A");
						}
					} else {
						m.setAttachDocument("N/A");
					}
					m.setAttachDocument(
							"<a href='" + imagePath + "'target=\"_blank\" >" + m.getAttachDocument() + "</a>");

				}

				for (AuditReportAdditionalInfoModel m : additionalDocList) {

					String imagePath = "";
					if (m.getInfoDocument() != null && m.getInfoDocument() != "") {
						m.setInfoDocumentName(m.getInfoDocument());
						String[] extension = m.getInfoDocument().split("\\.");
						if (extension.length == 2) {
							if (extension[1].equals("xls") || extension[1].equals("xlsx")) {

								String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
										+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\"  title=" + m.getInfoDocument()
										+ "></i> </a>";

								m.setInfoDocument(docPath);
							}
							if (extension[1].equals("pdf")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
										+ 			"\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title= "
										+ m.getInfoDocument() + "></i> </a>";

								m.setInfoDocument(docPath);
							}
							if (extension[1].equals("doc") || extension[1].equals("dox")  || extension[1].equals("docx")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
										+ "\"><i class=\"fa fa-file-word-o fa-2x \" aria-hidden=\"true\" title="
										+ m.getInfoDocument() + "></i> </a>";
								m.setInfoDocument(docPath);
							}
							if (extension[1].equals("png") || extension[1].equals("jpg")
									|| extension[1].equals("jpeg")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
										+ "\"><i class=\"fa fa-file-image-o fa-2x  \"\" aria-hidden=\"true\" title="
										+ m.getInfoDocument() + "></i> </a>";
								m.setInfoDocument(docPath);
							}
						} else {
							m.setInfoDocument("N/A");
						}
					} else {
						m.setInfoDocument("N/A");
					}
					m.setInfoDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getInfoDocument() + "</a>");

					/*
					 * m.setAction(s); s = "";
					 */

				}

				for (AuditReportBasicsQualifyModel m : basicQualifInfoList) {

					String imagePath = "";
					if (m.getQlfyDocument() != null && m.getQlfyDocument() != "") {
						m.setQlfyDocumentName(m.getQlfyDocument());
						String[] extension = m.getQlfyDocument().split("\\.");
						if (extension.length == 2) {
							if (extension[1].equals("xls") || extension[1].equals("xlsx")) {

								String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
										+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title= " + m.getQlfyDocument()
										+ "></i> </a>";

								m.setQlfyDocument(docPath);
							}
							if (extension[1].equals("pdf")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
										+ "\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title="
										+ m.getQlfyDocument() + " ;></i> </a>";

								m.setQlfyDocument(docPath);
							}
							if (extension[1].equals("doc") || extension[1].equals("dox") || extension[1].equals("docx")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
										+ "\"><i class=\"fa fa-file-word-o fa-2x  \" aria-hidden=\"true\" title="
										+ m.getQlfyDocument() + "></i> </a>";
								m.setQlfyDocument(docPath);
							}
							if (extension[1].equals("png") || extension[1].equals("jpg")
									|| extension[1].equals("jpeg")) {
								String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
										+ "\"><i class=\"fa fa-file-image-o fa-2x  \"\" aria-hidden=\"true\" title="
										+ m.getQlfyDocument() + "></i> </a>";
								m.setQlfyDocument(docPath);
							}
						} else {
							m.setQlfyDocument("N/A");
						}
					} else {
						m.setQlfyDocument("N/A");
					}
					m.setQlfyDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getQlfyDocument() + "</a>");

				}

				model.addAttribute("attchment", attachmentList);
				model.addAttribute("basicInfo", basicQualifInfoList);
				model.addAttribute("additionalInfo", additionalDocList);
				model.addAttribute("cc", report.getPersonCC());
				model.addAttribute("to", report.getPersonTo());

			}

			if (report != null) {
				try {
					DropDownModel[] audit = restClient.getForObject(
							env.getAuditUrl() + "getAuditInitiatedIdByAuditType?id=" + report.getAuditType(),
							DropDownModel[].class);
					List<DropDownModel> auditInitedList = Arrays.asList(audit);

					model.addAttribute("auditInitedList", auditInitedList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list
		 */

		logger.info("Method : editReport ends");

		return "audit/audit-draft-report";
	}

	/*
	 * for Edit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-draft-report-details")
	public String editdtls(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : manage audit Report starts");
		
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(decodeId));

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		JsonResponse<List<AuditDraftReportModel>> response = new JsonResponse<List<AuditDraftReportModel>>();
		try {
			response = restClient.getForObject(env.getAuditUrl() + "getdraftReporteditById?id=" + id,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditDraftReportModel> report = mapper.convertValue(response.getBody(),
					new TypeReference<List<AuditDraftReportModel>>() {
					});
			if(report!=null) {
			for (AuditDraftReportModel m : report) {

				String imagePath = "";
				if (m.getAttachDocument() != null && m.getAttachDocument() != "") {
					String[] extension = m.getAttachDocument().split("\\.");
					if (extension.length == 2) {
						if (extension[1].equals("xls")||extension[1].equals("xlsx") ) {

							String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
									+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title="+m.getAttachfileName()+"></i> </a>";

							m.setAttachDocument(docPath);
						}
						if (extension[1].equals("pdf")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
									+ "\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title="+m.getAttachfileName()+" ;></i> </a>";

							m.setAttachDocument(docPath);
						}
						if (extension[1].equals("doc")|| extension[1].equals("docx")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
									+ "\"><i class=\"fa fa-file-word-o\" aria-hidden=\"true\" title="+m.getAttachfileName()+"></i> </a>";
							m.setAttachDocument(docPath);
						}
						if (extension[1].equals("jpg")|| extension[1].equals("png")|| extension[1].equals("jpeg")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getAttachDocument()
							+ "\"><i class=\"fa fa-file-image-o\" aria-hidden=\"true\" title="+m.getAttachfileName()+"></i> </a>";
							m.setAttachDocument(docPath);
						}
					} else {
						m.setAttachDocument("N/A");
					}
				} else {
					m.setAttachDocument("N/A");
				}
				m.setAttachDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getAttachDocument() + "</a>");

				/*
				 * m.setAction(s); s = "";
				 */

			}
			}
			if(report.size()>0) {
			for (AuditReportAdditionalInfoModel m : report.get(0).getAdditionalInfo()) {

				String imagePath = "";
				if (m.getInfoDocument() != null && m.getInfoDocument() != "") {
					String[] extension = m.getInfoDocument().split("\\.");
					if (extension.length >1) {
						if (extension[1].equals("xls")|| extension[1].equals("xlsx")) {

							String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
									+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title="+m.getInfoFileName()+"></i> </a>";

							m.setInfoDocument(docPath);
						}
						if (extension[1].equals("pdf")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
									+ "\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title="+m.getInfoFileName()+" ;></i> </a>";

							m.setInfoDocument(docPath);
						}
						if (extension[1].equals("doc") || extension[1].equals("docx")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
									+ "\"><i class=\"fa fa-file-word-o\" aria-hidden=\"true\" title="+m.getInfoFileName()+"></i> </a>";
							m.setInfoDocument(docPath);
						}
						if (extension[1].equals("jpg")|| extension[1].equals("png")||extension[1].equals("jpeg")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getInfoDocument()
							+ "\"><i class=\"fa fa-file-image-o\" aria-hidden=\"true\" title="+m.getInfoFileName()+"></i> </a>";
							m.setInfoDocument(docPath);
						}
					} else {
						m.setInfoDocument("N/A");
					}
				} else {
					m.setInfoDocument("N/A");
				}
				m.setInfoDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getInfoDocument() + "</a>");

				/*
				 * m.setAction(s); s = "";
				 */

			}
			}
			if(report.size()>0) {
			for (AuditReportBasicsQualifyModel m : report.get(0).getBasicOfQualify()) {

				String imagePath = "";
				if (m.getQlfyDocument() != null && m.getQlfyDocument() != "") {
					String[] extension = m.getQlfyDocument().split("\\.");
					if (extension.length == 2) {
						if (extension[1].equals("xls")|| extension[1].equals("xlsx")) {

							String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
									+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title="+m.getQlfyDocumentName()+"></i> </a>";

							m.setQlfyDocument(docPath);
						}
						if (extension[1].equals("pdf")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
									+ "\"><i class=\"fa fa-file-pdf-o fa-2x excel pdf\" title="+m.getQlfyDocumentName()+" ;></i> </a>";

							m.setQlfyDocument(docPath);
						}
						if (extension[1].equals("doc")|| extension[1].equals("docx")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
									+ "\"><i class=\"fa fa-file-word-o\" aria-hidden=\"true\" title="+m.getQlfyFileName()+"></i> </a>";
							m.setQlfyDocument(docPath);
						}
						if (extension[1].equals("jpg")||extension[1].equals("png")||extension[1].equals("jpeg")) {
							String docPath = "<a href=\"/document/audit/excel/" + m.getQlfyDocument()
									+ "\"><i class=\"fa fa-file-image-o\" aria-hidden=\"true\" title="+m.getQlfyFileName()+"></i> </a>";
							m.setQlfyDocument(docPath);
						}
					} else {
						m.setQlfyDocument("N/A");
					}
				} else {
					m.setQlfyDocument("N/A");
				}
				m.setQlfyDocument("<a href='" + imagePath + "'target=\"_blank\" >" + m.getQlfyDocument() + "</a>");

				/*
				 * m.setAction(s); s = "";
				 */

			}
			}
			/** Department Emp List**/
			try {
				DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
				List<DropDownModel> programList = Arrays.asList(program); 
				model.addAttribute("empList", programList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			/** persons to forward **/
			try {
				PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
						PersonListModel[].class);
				List<PersonListModel> personList = Arrays.asList(dept);
				model.addAttribute("personList", personList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			if(report.size()>0) {
			model.addAttribute("TOList", report.get(0).getPersonTo());
			model.addAttribute("CCList", report.get(0).getPersonCC());
			model.addAttribute("edit", report.get(0).getDraftId());
			model.addAttribute("edit1", report.get(0).getAdditionalInfo().get(0).getInfofile());
			}
			model.addAttribute("reportdtls", report);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for getting the comments with docs
		 */
		try {
			AuditDraftReportModel[] comments = restClient
					.getForObject(env.getAuditUrl() + "getdraftCommentById?id=" + id, AuditDraftReportModel[].class);
			for (AuditDraftReportModel c : comments) {
				String[] arr = c.getDate().split(" ");
				c.setDate(arr[0]);
				c.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
				for (AuditReportAdditionalInfoModel ad : c.getAdditionalInfo()) {
					if(ad.getInfoFileName()!=null && ad.getInfoFileName()!="") {
					String suffix = ad.getInfoFileName().split("\\.")[1];
					if (suffix.equals("xls")|| suffix.equals("xlsx")) {
						String icon = "<a href=\"/document/audit/excel/" + ad.getInfoFileName()
								+ "\"><i class='fa fa-file-excel-o fa-2x excel' title="+ad.getInfoDocument()+"></i></a>";
						ad.setInfoFileName(icon);
					} else if (suffix.equals("pdf")) {
						String icon = "<a href=\"/document/audit/excel/" + ad.getInfoFileName()
								+ "\"><i class=\"fa fa-file-pdf-o fa-2x  pdf\" title="+ad.getInfoDocument()+"></i></a>";
						ad.setInfoFileName(icon);
					}
					else if (suffix.equals("docx")|| suffix.equals("doc")) {
						String icon = "<a href=\"/document/audit/excel/" + ad.getInfoFileName()
								+ "\"><i class=\"fa fa-file-word-o  fa-2x word\" title="+ad.getInfoDocument()+"></i></a>";
						ad.setInfoFileName(icon);
					}else if (suffix.equals("jpg")||suffix.equals("png")||suffix.equals("jpeg")) {
						String icon = "<a href=\"/document/audit/excel/" + ad.getInfoFileName()
								+ "\"><i class=\"fa fa-file-image-o fa-2x\" title="+ad.getInfoDocument()+"></i></a>";
						ad.setInfoFileName(icon);
					}
				}
				}

			}
			model.addAttribute("comments", comments);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : manage audit Report starts");

		return "audit/draft-report";
	}

	/*
	 * approve accept
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-audit-draft-report" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendDetails(Model model, @RequestBody List<AuditDraftReportModel> obj,
			BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : sendDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.get(0).setCreatedBy(userId);
		} catch (Exception e) {
		}
		try {

			for (AuditDraftReportModel a : obj) {
				a.setAttachDocument(uploadPhoto(a.getAttachDocument(), a.getAttachfile()));
			}

			resp = restClient.postForObject(env.getAuditUrl() + "sendDraftDetails", obj, JsonResponse.class);
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
		return resp;
	}

	// save allImage
	public String saveAllImage(byte[] imageBytes) {
		logger.info("Method : saveAllImage starts");

		String imageName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				imageName = nowTime + ".png";
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
		return imageName;
	}
	/*
	 * for save all pdf in folder and return name
	 */

	public String saveAllPdf(byte[] imageBytes) {
		logger.info("Method : saveAllPdf starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".pdf";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllPdf ends");
		return pdfName;
	}

	public String saveAllDocx(byte[] imageBytes) {
		logger.info("Method : saveAllDocx starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".docx";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDocx ends");
		return pdfName;
	}

	public String saveAllDoc(byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".doc";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}

	public String saveAllXlsx(byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".xlsx";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}

	public String saveAllXls(byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".xls";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}
	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-audit-draft-report-sendReply" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendReply(Model model,
			@RequestBody List<RequisitionViewModel> requisitionViewModel, HttpSession session) throws IOException {
		logger.info("Method : sendReply starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}

		
		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {
			resp = restClient.postForObject(env.getAuditUrl() + "sendDraftReply", requisitionViewModel,
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
		logger.info("Method : sendReply ends");
		return resp;
	}
	public String  uploadPhoto(String document,List<String> file) {
		if (document!= null && document != "") {
			String delimiters = "\\.";
			String[] x =document.split(delimiters);
			if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
				for (String s1 : file) {

					try {
						
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String imageName = saveAllImage(bytes);
						document=imageName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("pdf")) {
				for (String s1 : file) {
					try {
						
						
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllPdf(bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("docx")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllDocx(bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("doc")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllDoc(bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("xls")) {
				for (String s1 : file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllXls(bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (x[1].matches("xlsx")) {
				for (String s1 :file) {
					try {
						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String pdfName = saveAllXlsx(bytes);
						document=pdfName;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return document;
		}
}
