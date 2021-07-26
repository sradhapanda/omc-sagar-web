package nirmalya.aathithya.webmodule.audit.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditDraftReportModel;
import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMasterModel;
import nirmalya.aathithya.webmodule.audit.model.AuditReportUpdateDateModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping("audit/")
public class AuditReportUpdateDateController {
	Logger logger = LoggerFactory.getLogger(AuditReportUpdateDateController.class);
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles env;
	
	
	/*
	 * Web-Update date Page Render
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-initiated-audit-report-update-date")
	public String getUpdateDatePage(@RequestParam("initiateId") String initiateId ,
			@RequestParam("auditType") String auditType ,
		@RequestParam("initiatedDate") String initiatedDate ,Model model, HttpSession session ) {
		logger.info("update date page sterts");
			
		byte[] decodeId = Base64.getDecoder().decode(initiateId.getBytes());
		String id = (new String(decodeId));
		
		byte[] auditType1 = Base64.getDecoder().decode(auditType.getBytes());
		String id1 = (new String(auditType1));
		
		byte[] auditType2 = Base64.getDecoder().decode(initiatedDate.getBytes());
		String id2 = (new String(auditType2));
		
		List<DropDownModel> reports = new  ArrayList<DropDownModel>();
		try {
			reports = restTemplate.getForObject(env.getAuditUrl()+"getReportIds?id="+id, List.class);
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		List<DropDownModel> description = new  ArrayList<DropDownModel>();
		try {
			description = restTemplate.getForObject(env.getAuditUrl()+"getDescription", List.class);
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("reports",reports);
		model.addAttribute("initiatedDate",id2);
		model.addAttribute("description",description);
		model.addAttribute("initiateId",id);
		model.addAttribute("auditType",id1);
		logger.info("update date page ends");
		return "audit/sample";
	}
	
	
	/*
	 * Web-Post-Update date
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("view-initiated-audit-report-update-date-save")
	public @ResponseBody JsonResponse<Object> updateDate(@RequestBody List<AuditReportUpdateDateModel> data,HttpSession session) {
		logger.info("update date sterts");
		
		
		JsonResponse<Object> response = new JsonResponse<Object>();
		//System.out.println("gettingData="+data);
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			for(AuditReportUpdateDateModel d : data) {
				d.setCreatedBy(userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {

			int imagecount = 0;
			for (AuditReportUpdateDateModel a : data) {
				
				a.setFileName(uploadPhoto(a.getFileName(),a.getFile()));
			}

			response = restTemplate.postForObject(env.getAuditUrl() + "update-date-forReport", data, JsonResponse.class); 
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		if (response.getMessage() != null && response.getMessage() != "") {
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}
		
		logger.info("update date  ends");
		return response;
	}
	
	@GetMapping("view-initiated-audit-report-update-date-view")
	public String viewUpdateDates() {
		return "audit/view-update-dates";
	}
	
	/*
	 * For view work type for dataTable Ajax call
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-initiated-audit-report-update-date-view-ThroughAjax")
	public @ResponseBody DataTableResponse viewInitaiedAuditThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, HttpSession session) {

		logger.info("Method : report UpdateDateThroughAjax statrs");

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

		
			
			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
			//System.out.println(env.getAuditUrl()+"getUpdatedReport");
			jsonResponse = restTemplate.postForObject(env.getAuditUrl()+"getUpdatedReport", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			List<AuditMasterModel> reportDateList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditMasterModel>>() {
					});
			for (AuditMasterModel m : reportDateList) {
				String s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getAuditee().getBytes());
				//byte[] auditType = Base64.getEncoder().encode(m.getDraftRepId().getBytes());
				
				s = s + "<a href='view-initiated-audit-report-update-date-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit faIcon\"></i></a>";
				String view = "&nbsp;&nbsp;<a onclick=showInModal('" + new String(encodeId)+"','"+m.getAuditInitiated()+"',\""+m.getAuditName().split(" ")[0]+"\")><i class=\"fa fa-eye faIcon\"></i></a>";
				s += view;
				m.setAction(s);
		
			}
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reportDateList);
		//	System.out.println("ResponseData:"+response);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewInitaiedAuditThroughAjax  ends");

		return response;
	}
	
	/*
	 * web - edit updated date
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-initiated-audit-report-update-date-edit")
	public String editUpdatedDate(Model model,@RequestParam String id) {
		
		logger.info("edit date page sterts");
		
		byte[] encoded = Base64.getDecoder().decode(id.getBytes());
		String decodedId = new String(encoded);
		
				
			AuditReportUpdateDateModel[] dept = restTemplate.getForObject(env.getAuditUrl() + "getUpdatedDateById?id="+decodedId,
					AuditReportUpdateDateModel[].class);
			List<AuditReportUpdateDateModel> reportDateList = Arrays.asList(dept);
		
		List<DropDownModel> reportids = new  ArrayList<DropDownModel>();
		try {
			reportids = restTemplate.getForObject(env.getAuditUrl()+"getReportIds?id="+decodedId, List.class);
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		List<DropDownModel> description = new  ArrayList<DropDownModel>();
		try {
			description = restTemplate.getForObject(env.getAuditUrl()+"getDescription", List.class);
					
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(AuditReportUpdateDateModel a:reportDateList) {
			AuditInitiateController  auditInintiateConntroller=new AuditInitiateController();
			a.setFileName(auditInintiateConntroller.viewUploadedImage(a.getFileName(),a.getDocName()));
			
		}
		
		
		model.addAttribute("description",description);
		model.addAttribute("reports",reportids);
		model.addAttribute("reportDateList",reportDateList);
		model.addAttribute("edit",true);
		logger.info("edit date page ends");
		return "audit/sample";
		
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("view-initiated-audit-report-update-date-getById")
	public @ResponseBody JsonResponse<Object> showInModal(@RequestParam  String id){
		
		byte[] encoded = Base64.getDecoder().decode(id.getBytes());
		String decodedId = new String(encoded);
		
		List<AuditReportUpdateDateModel> resp = new  ArrayList<AuditReportUpdateDateModel>();
		try {
			resp = restTemplate.getForObject(env.getAuditUrl()+"getUpdatedDateById?id="+decodedId, List.class);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		ObjectMapper mapper = new ObjectMapper();
		List<AuditReportUpdateDateModel> reportDateList = mapper.convertValue(resp,
				new TypeReference<List<AuditReportUpdateDateModel>>() {
				});
		
		for(AuditReportUpdateDateModel m : reportDateList) {
			if(m.getFileName()!=null) {
				if(m.getFileName().split("\\.")[1].equals("xls")) {
					String file = "<a href=\"/document/audit/excel/"+ m.getFileName()+"\"><i class=\"fa fa-file-excel-o fa-2x excel\" title=\""+m.getFileName()+"\" ></i> </a>"; 
					m.setFileName(file);
				}else {
					String file = "<a href=\"/document/audit/excel/"+ m.getFileName()+ "\"style=\"color:red;\"><i class=\"fa fa-file-pdf-o fa-2x pdf\" title=\""+m.getFileName()+"\" ></i> </a>"; 
					m.setFileName(file);
				}
			}
		}
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		res.setBody(reportDateList);
		
		//System.out.println("response:"+res);
		return  res;
	}
	
	/*
	 * File save procedures
	 */
	
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

			public String saveAllDoc(byte[] imageBytes) {
				logger.info("Method : saveAllDoc starts");

				String pdfName = null;

				try {
					if (imageBytes != null) {
						long nowTime = new Date().getTime();
						pdfName = nowTime + ".doc";
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

			public String saveAllXlsx(byte[] imageBytes) {
				logger.info("Method : saveAllDoc starts");

				String pdfName = null;

				try {
					if (imageBytes != null) {
						long nowTime = new Date().getTime();
						pdfName = nowTime + ".xlsx";
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

			public String saveAllXls(byte[] imageBytes) {
				logger.info("Method : saveAllDoc starts");

				String pdfName = null;

				try {
					if (imageBytes != null) {
						long nowTime = new Date().getTime();
						pdfName = nowTime + ".xls";
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
