package nirmalya.aathithya.webmodule.master.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.AuditUploadDocumentReportModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class AuditUploadReportController {
	Logger logger = LoggerFactory.getLogger(AuditUploadReportController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;
	
	@GetMapping("/add-audit-upload-document-Report")
	public String addAuditUploadDocumentReport(Model model, HttpSession session) {
		logger.info("Method : addAuditUploadDocumentReport starts");

		

	/*	model.addAttribute("auditInitiate", id);
		
		model.addAttribute("initiate", index);*/
		try {
			DropDownModel[] audit = restTemplate.getForObject(env.getAuditUrl() + "getAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addAuditUploadDocumentReport ends");
		return "master/add-audit-upload-report";
	}
	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add-audit-upload-document-Report-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addUploadDocumentReportThroughAjax(
			@RequestBody List<AuditUploadDocumentReportModel> auditUploadDocumentReportModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addUploadDocumentReportThroughAjax function starts");
		int imagecount = 0;
		for (AuditUploadDocumentReportModel a : auditUploadDocumentReportModel) {
			if (a.getDocument() != null && a.getDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getDocument().split(delimiters);
				System.out.println("image" + x[1]);
				if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
					for (String s1 : a.getFile()) {

						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String imageName = saveAllImage(bytes);
							a.setDocument(imageName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("pdf")) {
					for (String s1 : a.getFile()) {
						try {
							// @SuppressWarnings("restriction")
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllPdf(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("docx")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllDocx(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("doc")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllDoc(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("xls")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllXls(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("xlsx")) {
					for (String s1 : a.getFile()) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllXlsx(bytes);
							a.setDocument(pdfName);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			imagecount = imagecount + 1;
		}
		try {

			res = restTemplate.postForObject(env.getMasterUrl() + "restAddAuditDocument", auditUploadDocumentReportModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addUploadDocumentReportThroughAjax function Ends");
		return res;
	}
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
	public String saveAllPdf(byte[] imageBytes) {
		logger.info("Method : saveAllPdf starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				pdfName = nowTime + ".pdf";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			System.out.println("path " + path);
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
			System.out.println("path" + path);
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
			System.out.println("path" + path);
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
			System.out.println("path" + path);
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
			System.out.println("path" + path);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}
	@GetMapping("/view-audit-document-upload")
	public String viewAuditDocumentUplad(Model model, HttpSession session) {
		logger.info("Method : viewAuditDocumentUplad starts");

		logger.info("Method : viewAuditDocumentUplad ends");
		return "master/view-audit-document-upload";
	}
	@SuppressWarnings("unchecked")
	@GetMapping("/view-audit-document-upload-ThroughAjax")
	public @ResponseBody DataTableResponse viewDocumentUploadAuditThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {

		logger.info("Method : viewDocumentUploadAuditThroughAjax statrs");

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

			JsonResponse<List<AuditUploadDocumentReportModel>> jsonResponse = new JsonResponse<List<AuditUploadDocumentReportModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllUploadDocument", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditUploadDocumentReportModel> AuditUploadDocumentReportModelList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditUploadDocumentReportModel>>() {
					});

			String s = "";

			for (AuditUploadDocumentReportModel m : AuditUploadDocumentReportModelList) {
				s = "";
				//byte[] encodeId = Base64.getEncoder().encode(m.getYear().getBytes());
			/*	if (m.getInitiatedStatus() == 2 && userRole.contains("rol021")) {
					s = s + " <i class='fa fa-calendar-check-o faIcon' aria-hidden=\"true\" ></i>";
				} else if (!m.getAuditTypeId().equals("ADTM006")
						&& (m.getInitiatedStatus() == 1  && userRole.contains("rol021"))) {
					s = s + "<a title='Schedule'  href='/audit/add-audit-master?id=" + new String(encodeId)
							+ "'>  <i class='fa fa-calendar faIcon' aria-hidden='true'></i></a>";
				} else if (m.getAuditTypeId().equals("ADTM006") && m.getInitiatedStatus() == 1 && userRole.contains("rol021") && m.getDtlStatus() ) {
					s = s + "<a title='Schedule' href='/audit/add-audit-master?id=" + new String(encodeId)
							+ "'> <i class='fa fa-calendar faIcon' aria-hidden=\"true\"></i></a>";
				} else if( !userRole.contains("rol024") ){
					s = s + ("N/A");
				}
*/
				/*if (userRole.contains("rol024") && m.getAuditTypeId().equals("ADTM006")) {
					if(m.getDtlStatus()) {
						s = s + "&nbsp;&nbsp;<a title='View Details' onclick='viewDetails(\"" +  new String(encodeId)+ "\")' ><i class='fa fa-book faIcon' aria-hidden=\"true\"></i> </a>";
						
					}else {
						
						s = s + "&nbsp;&nbsp;<a title='Details Of Audit' onclick='detailsOfAudit(\"" +  new String(encodeId)
								+ "\")' ><i class='fa fa-file faIcon' aria-hidden=\"true\"></i> </a>";
					}
					
				}*/
				
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
			response.setData(AuditUploadDocumentReportModelList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewDocumentUploadAuditThroughAjax Theme ends");

		return response;
	}
}
