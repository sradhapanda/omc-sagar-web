/**
 * 
 */
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

import nirmalya.aathithya.webmodule.audit.model.AuditCommentsModel;
import nirmalya.aathithya.webmodule.audit.model.AuditComplianceModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMasterModel;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author USER
 *
 */
@Controller
@RequestMapping(value = "audit")
public class TransactionAuditController {
	Logger logger = LoggerFactory.getLogger(TransactionAuditController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-transaction-audit-master")
	public String viewTransactionAuditMaster(Model model, HttpSession session) {

		logger.info("Method : viewTransactionAuditMaster starts");

		logger.info("Method : viewTransactionAuditMaster ends");

		return "audit/view-transaction-audit-master";
	}

	/*
	 * For view audit master Ajax call
	 */

//	@SuppressWarnings({ "unchecked" })
//	@GetMapping("/view-transaction-audit-master-ThroughAjax")
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
//				
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
//				
//				/*
//				 * System.out.println("Sagar Audit "+m.getApproveStatus());
//				 * System.out.println("Sagar Audit "+m.getReqStatus());
//				 */
//				if (m.getAuditStatus() == 0 ) {
//					if(roles.contains("rol024")  && m.getReqStatus()==3) {
//						s = s + "<a title='Question' href='/audit/view-transaction-audit-master-question?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <i class=\"fa fa-question-circle faIcon\" aria-hidden=\"true\" ></i></a>";
//					m.setStatus("open");
//						
//					
//				} else if(roles.contains("rol021")) {
//						System.out.println(m.getAuditStatus()); 
//						if (m.getReqStatus() == 1) {
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='View Request' onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId() + "\")' ><i class='fa fa-street-view faIcon' aria-hidden='true' ></i> </a>";
//						
//						} else if (m.getReqStatus() == 2) {
//
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='View Request' onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-street-view faIcon\" aria-hidden=\"true\" ></i> </a>";
//						
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Forward Requisition' onclick='forwardRequisition(\""
//									+ m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-forward faIcon\" aria-hidden=\"true\" ></i> </a>";
//
//						} else if(m.getReqStatus()==0) {
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Request Requisition' onclick='requestRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")'><i class='fa fa-exchange faIcon' aria-hidden='true'></i></a>";
//						}
//						
//						else {
//							s=s+"N/A";
//						}
//					
//					}
//				else if(roles.contains("rol022") || roles.contains("rol023")) {
//					 if (m.getReqStatus() == 1) {
//						s = s + "&nbsp;&nbsp;&nbsp;<a title='Reply to Requisition' onclick='responseRequisition(\""
//								+ m.getAuditAutoGenId() + "\")' ><i class='fa fa-reply faIcon' aria-hidden='true'></i> </a>";
//	
//					} else if (m.getReqStatus() == 2) {
//						s = s + "&nbsp;&nbsp;&nbsp;<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//								+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden='true'></i> </a>";
//	
//					} else {
//						s = s + ("N/A");
//					}
//				}
//					m.setStatus("N/A");
//					//s = s + "N/A";
//				} else if (m.getAuditStatus() == 1) {
//					if(roles.contains("rol024") && m.getReqStatus()==3) {
//						s = s + "<a Title='Observation' href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-search faIcon' aria-hidden='true'></i></a>&nbsp;&nbsp;";
//						s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getAuditAutoGenId() + "\")'><button class='btn btn-info'><i class='fa fa-download faIcon' ></i></button></a>";
//					} 
////					else if(roles.contains("rol021") && m.getReqStatus()==3){
////						
////							s = s + "&nbsp;&nbsp;&nbsp;<a title='Requested' onclick='forwardQuestion(\""
////									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'>Forword Question</button> </a>";
////						}
//					
//					
//					else {
//						s = "N/A";
//					}
//					m.setStatus("Not Observed");
//				}  else if(m.getAuditStatus() == 2 ) {
//					m.setStatus("Open");
//					if(roles.contains("rol024")  && m.getReqStatus() == 6) {
//					s = s + "<a Title='View' href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-eye faIcon' aria-hidden='true'></a>";
//					}
//					else if(roles.contains("rol022") || roles.contains("rol023") && m.getReqStatus()==5){
//						s = s + "<a href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-eye faIcon' aria-hidden='true'></a>";
//					
//					}
//					else if(roles.contains("rol021") && m.getReqStatus()==3){
//						
//						s = s + "<a title='Forward Observation' href='/audit/view-transaction-audit-master-forwardObs?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-forward faIcon'aria-hidden='true' ></i></a>";
//					}
//					else if (roles.contains("rol021") && m.getReqStatus() ==5) {
//
//						s = s + "<a Title='Forward Replay' href='/audit/view-transaction-audit-master-forwardObs?id=" + m.getAuditAutoGenId()
//								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'><i class='fa fa-forward faIcon'aria-hidden='true' ></i></a>";
//					}
//					else if(roles.contains("rol022") || roles.contains("rol023") && m.getReqStatus()==4){
//							s = s + "<a href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//									+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//									+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-eye faIcon' aria-hidden='true'></button></a>";
//						
//						}
//					else if(m.getApproveStatus() > 0 && m.getApproveStatus() <7 ) {
//						s = s + "<a href='/audit/approve-transaction-audit-master?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-eye faIcon' aria-hidden='true'></a>";
//					
//					}
////					else  {
////						s = s + "<a href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
////								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
////								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-eye faIcon' aria-hidden='true'></a>";
////					
////					}
//					
//					System.out.println(roles);
//					System.out.println(m.getReqStatus() );
//				
//					
//				
//				}else if(m.getAuditStatus() == 3) {
//					m.setStatus("Closed");
//					s = s + "<a Title='View Details' href='/audit/view-transaction-audit-master-observation?id=" + m.getAuditAutoGenId() + "&deptId="
//							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <i class='fa fa-list-alt faIcon' aria-hidden='true'></i></a>&nbsp;&nbsp;";
//					if(roles.contains("rol024")) {
//						s = s + "<a Title='Submit Report' href='javascript:void' onclick='submitReport(\"" + m.getAuditAutoGenId() + "\")'><i class='fa fa-send-o faIcon'></i></a>";
//					}
//					s = s + "<a Title='Report' href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId() + "\")'><i class='fa fa-file faIcon' aria-hidden='true'></i></a>";
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
//					 
//						
//					if (m.getIrStatus() == 1) {
//						s = s + "&nbsp;&nbsp;<a Title='IR Report' href='javascript:void' onclick='generateIrReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><i class=\"fa fa-bookmark faIcon\"></i></a>";
//					} else if (m.getIrStatus() == 2 || m.getIrStatus() == 3) {
//						s = s + "&nbsp;&nbsp;<a Title='View IR Report' href='javascript:void' onclick='viewIRStatusReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><i class=\"fa fa-bookmark-o faIcon\"></i></a>";
//					}
//					if (m.getIrStatus() == 3) {
//						 s = s + "&nbsp;&nbsp;&nbsp; <a title='Forward IR Report' onclick='forwardIR(\""
//								+ m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-forward faIcon\" aria-hidden=\"true\" ></i> </a>";					
//				}
//					else if (m.getApproveStatus()==3 && m.getIrStatus() == 4) {
//					 m.setStatus("Open");
//						s = s + "&nbsp;&nbsp;&nbsp;<a title='Draft Note' onclick='draftNote(\""
//								+ m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-sticky-note-o faIcon\" aria-hidden=\"true\"></i> </a>";	
//					 }	
//					 else if(m.getIrStatus() == 5 && roles.contains("rol021") ||m.getIrStatus() == 6 ) {
//							System.out.println("here Draft");
//							m.setStatus("Open");
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='View Draft ' onclick='viewDraft(\""
//									+ m.getAuditAutoGenId() + "\")' ><i class='fa fa-file-text-o faIcon'aria-hidden='true'></i></a>";	
//							}
//					  if(m.getIrStatus() == 6 ){
//						 s = s +"&nbsp;&nbsp;&nbsp;<a  Title ='Forward Draft Note' href='/audit/view-transaction-audit-master-forwardObs?id=" + m.getAuditAutoGenId() + "&deptId="
//									+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//									+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-forward faIcon' aria-hidden='true' ></i></a>";
//									
//					}
//					  if(m.getIrStatus()==7  ) {
//							 m.setStatus("Open");
//							 s = s + "&nbsp;&nbsp;&nbsp;<a title='Draft Para ' onclick='draftPara(\""
//										+ m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-file-text faIcon\" aria-hidden=\"true\"></i> </a>";	
//							 }
//					  if((m.getIrStatus()==9) && m.getApproveStatus()==5 ) {
//							 s = s + "<a Title=' Forward Draft Para' href='/audit/view-transaction-audit-master-forwardObs?id=" + m.getAuditAutoGenId() + "&deptId="
//										+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//										+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'><i class='fa fa-forward faIcon' aria-hidden='true' ></i></a>";
//					
//						 }
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
//						s = s + "&nbsp;&nbsp;<a Title='Upload Report' href='javascript:void' onclick='uploadIrReport(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><i class=\"fa fa-bookmark faIcon\"></i></a>";
//					}
//					if (m.getIrStatus() == 3) {
//						s = s + "&nbsp;&nbsp;<a Title='View IR Report' href='javascript:void' onclick='viewIrRepotTo(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'>\r\n" + 
//								"<i class=\"fa fa-bookmark-o faIcon\"></i></a>";
//					}
//					if (m.getIrStatus() == 5) {
//						s = s + "&nbsp;&nbsp;<a Title='Compliance' href='javascript:void' onclick='complianceOfDraft(\""
//								+ m.getAuditAutoGenId() + "\",\"" + new String(deptId)
//								+ "\")'><i class=\"fa fa-reply-all faIcon\" aria-hidden=\"true\"></i></a>";
//					}
//					if (m.getIrStatus() == 6) {
//						s = s + "&nbsp;&nbsp;<a Title='View Draft' href='javascript:void' onclick='viewDraft(\""
//								+ m.getAuditAutoGenId()
//								+ "\")'> <i class=\"fa fa-file-text-o faIcon\" aria-hidden=\"true\"></i></a>";
//					}
//					
//					if (m.getIrStatus() == 8) {
//						s = s + "&nbsp;&nbsp;<a Title=' Draft Para Compliance' href='javascript:void' onclick='complianceOfDraftPara(\""
//								+ m.getAuditAutoGenId()
//								+ "\",\"" + new String(deptId)+"\")'><i class='fa fa-file-text faIcon' aria-hidden='true'></i></a>";
//					}
//					if (m.getIrStatus() == 9) {
//						s = s + "&nbsp;&nbsp;<a Title='View Draft Para' href='javascript:void' onclick='viewDraftPara(\""
//								+ m.getAuditAutoGenId()
//								+ "\")'><i class='fa fa-file-text faIcon' aria-hidden='true'></i></a>";									
//					}
//					
//				}
//				
////				if(m.getAuditStatus() == 2 && roles.contains("rol021") ) {				
////					m.setStatus("Open");
////					s = s + "<a href='/audit/observation-forTransAudit?id=" + m.getAuditAutoGenId() + "&deptId="
////							+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
////							+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() + "'> <button class=\"btn btn-warning\"> View </button></a>";
////				}
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
	/*
	 * get Mapping for view document
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> viewAudDocument(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : viewAudDocument function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "viewDocument?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : viewAudDocument function Ends");
		return res;
	}

	/*
	* approve accept
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-transaction-audit-master-sendRequest" })
	public @ResponseBody JsonResponse<Object> sendRequestRequisition(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendRequestRequisition starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				//System.out.println("CType: " + fileType[1]);

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendTransRequestRequisition?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendTransRequestRequisition?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendRequestRequisition ends");
		return resp;
	}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-transaction-audit-master-uploadFile")
	public @ResponseBody JsonResponse<Object> viewUploadFile(@RequestParam("file") MultipartFile inputFile,
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
	* get Mapping for view document
	*/

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getReqDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getReqAudDocument(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getReqAudDocument function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getTransReqDocument?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getReqAudDocument function Ends");
		return res;
	}
	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-transaction-audit-master-sendResponse" })
	public @ResponseBody JsonResponse<Object> sendResponse(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendResponse starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}

// auditInitiate.setDocument(contentName);

				try {
					resp = restClient
							.getForObject(
									env.getAuditUrl() + "sendTransResponseRequisition?id=" + encodeId + "&createdBy="
											+ userId + "&document=" + contentName + "&comment=" + comment,
									JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient
							.getForObject(
									env.getAuditUrl() + "sendTransResponseRequisition?id=" + encodeId + "&createdBy="
											+ userId + "&document=" + contentName + "&comment=" + comment,
									JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendResponse ends");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-transaction-audit-master-forwardReq", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> fowardedReq(@RequestParam ("id") String id,
			Model model, HttpSession session) {
		logger.info("Method : fowardedReq function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			
			res = restClient.getForObject(env.getAuditUrl() + "fowardedTransReq?id="+id+"&createdBy="+userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : fowardedReq function Ends");
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-transaction-audit-master-question")
	public String addAuditQuestion(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status) {

		logger.info("Method : addAuditQuestion starts");

		model.addAttribute("initiatedId", id);
		model.addAttribute("deptId", deptId);
		model.addAttribute("sectionId", sectionId);

		logger.info("Method : addAuditQuestion ends");

		return "audit/add-audit-tarns-question";
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-question-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addQuestion(
			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addQuestion function starts");

		try {

			res = restClient.postForObject(env.getAuditUrl() + "restTransAddQuestion", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addQuestion function Ends");
		return res;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-transaction-audit-master-forwardQus", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> fowardedQus(@RequestParam ("id") String id,
			Model model, HttpSession session) {
		logger.info("Method : fowardedQus function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			
			res = restClient.getForObject(env.getAuditUrl() + "fowardedTransQus?id="+id+"&createdBy="+userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : fowardedQus function Ends");
		return res;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getQuestion", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getQuestion(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getQuestion function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "pdfQuestionList?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getQuestion function Ends");
		return res;
	}
	
	
	
	
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-transaction-audit-master-observation") 
	public String addTransAuditObservation(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
		logger.info("Method : addTransAuditObservation starts");
		
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);
		
		byte[] encodeByte=Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1=Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));
		try {
			
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getTransQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			model.addAttribute("questionList", questionList);
		} catch(Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addTransAuditObservation ends");
		return "audit/add-trans-audit-observation";
	}
	
	
	
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "view-transaction-audit-master-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addObservation(@RequestBody List<AuditObservationModel> observation,
			Model model, HttpSession session) {
		logger.info("Method : addObservation function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		
		int imagecount = 0;
		for (AuditObservationModel a : observation) {

			if (a.getDocument() != null && a.getDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getDocument().split(delimiters);

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
				}
			}
			imagecount = imagecount + 1;
		}

		try {
			
			res = restClient.postForObject(env.getAuditUrl() + "addTransObservation", observation, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addObservation function Ends");
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
	@SuppressWarnings("unchecked")
	@PostMapping("/view-transaction-audit-master-comment-save")
	public @ResponseBody JsonResponse<Object> auditeeCommentSave(Model model, @RequestBody AuditComplianceModel index, HttpSession session) throws IOException {
		
		logger.info("Method : auditeeCommentSave starts");
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageAuditeeFile");
			
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = "";
				if(fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if(fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				
				index.setDocument(contentName);

				index.setCreatedBy(userId);
		
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeTransCommentSave" , index, JsonResponse.class);

				if (resp.getMessage() == null || resp.getMessage() == "") {
					Path path = Paths.get(env.getFileUploadAudit() + contentName);
					if (fileType[1].contentEquals("pdf")) {
						Files.write(path, bytes);
					} else if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document") || fileType[1].contentEquals("msword")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				index.setDocument(null);
				index.setCreatedBy(userId);
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeTransCommentSave" , index, JsonResponse.class);

			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
			session.removeAttribute("imageAuditeeFile");
		}
		logger.info("Method : auditeeCommentSave ends");
		return resp;
	}	
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/observation-forTransactionAudit") 
	public String addAuditforTransAudit(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
		logger.info("Method : addAuditforTransAudit starts");
	
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);
		
		byte[] encodeByte=Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1=Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));
		try {
			
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			
			model.addAttribute("questionList", questionList);
		} catch(Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addAuditforTransAudit ends");
		return "audit/add-audit-observationForTransAudit";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-transaction-audit-master-forwardObservationByCo", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardObs(@RequestParam ("id") String id,
			Model model, HttpSession session) {
		logger.info("Method : forwardObs function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			
			res = restClient.getForObject(env.getAuditUrl() + "forwardTransObs?id="+id+"&createdBy="+userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardObs function Ends");
		return res;
	}
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-transaction-audit-master-forwardObs") 
	public String addAuditObservationforTransAudit(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
		logger.info("Method : addAuditObservationforTransAudit starts");
	
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);
		
		byte[] encodeByte=Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1=Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));
		try {
			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getTransQuestionList?id="+id+"&deptId="+deptId1+"&sectionId="+sectionId1, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			for(AuditObservationModel m:questionList) {
//				auditQuestionStatus.add(m.getAuditStatus());
//			}
			model.addAttribute("questionList", questionList);
			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
		} catch(Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addAuditObservationforTransAudit ends");
		return "audit/add-audit-observationForTransAudit";
	}
	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-transaction-audit-master-sendIrStatus" })
	public @ResponseBody JsonResponse<Object> sendIrStatus(Model model, @RequestBody AuditMasterModel auditMasterModel,
			HttpSession session) throws IOException {
		logger.info("Method : sendIrStatus starts");
		// System.out.println(obj.charAt(3));
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		auditMasterModel.setCreatedBy(userId);
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}

				auditMasterModel.setGuideLineDoc(contentName);

				try {
					resp = restClient.postForObject(env.getAuditUrl() + "sendIRStatus", auditMasterModel,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.postForObject(env.getAuditUrl() + "sendIRStatus", auditMasterModel,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : sendIrStatus ends");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-transaction-audit-master-forwardIr", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardIr(@RequestParam ("id") String id,
			Model model, HttpSession session) {
		logger.info("Method : forwardIr function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			
			res = restClient.getForObject(env.getAuditUrl() + "forwardIr?id="+id+"&createdBy="+userId, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardIr function Ends");
		return res;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-transaction-audit-master-forward-to-approve", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> fowardedToApproveforTransAudit(@RequestBody List<AuditObservationModel> observation,
			Model model, HttpSession session) {
		logger.info("Method : fowardedToApproveforTransAudit function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			// Variable question is used for ApprovalBy
			for(AuditObservationModel m : observation) {
				m.setQuestion(userId);
			}
			res = restClient.postForObject(env.getAuditUrl() + "approveObsInTransAudit", observation, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : fowardedToApproveforTransAudit function Ends");
		return res;
	}
	/*
	 * get Mapping for view IrStatus
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getDraft", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getmstDraft(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :getmstDraft function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDraft?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getmstDraft function Ends");
		return res;
	}
	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-transaction-audit-master-uploadComplianceFile")
	public @ResponseBody JsonResponse<Object> uploadComplianceFile(@RequestParam("file") MultipartFile inputFile,
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
	* sendCompliance
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-transaction-audit-master-sendCompliance" })
	public @ResponseBody JsonResponse<Object> sendCompliance(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendCompliance starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendCompliance ends");
		return resp;
	}
	@GetMapping("/view-transaction-audit-master-approve-draft")  
	public String approveDraftObservation(Model model, HttpSession session, @RequestParam Integer id) {
		logger.info("Method : approveDraftObservation starts");
		
		model.addAttribute("AuditId", id);
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
			List<DropDownModel> stage = Arrays.asList(obs);
			
			model.addAttribute("UserStage", stage.get(0).getName());
			model.addAttribute("AuthorityStage", stage.get(0).getKey());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			model.addAttribute("questionList", questionList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
			
			model.addAttribute("commentsList", commentsList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : approveDraftObservation starts");
		return "audit/approveDraftObservation";
	}
	
	
	/*
	 * get Mapping for view draft Para
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getDraftPara", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getMstrDraftPara1(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :getMstrDraftPara function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDraftPara?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getMstrDraftPara function Ends");
		return res;
	}
	/*
	* sendCompliance
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-transaction-audit-master-sendParaCompliance" })
	public @ResponseBody JsonResponse<Object> sendParaCompliance1(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendCompliance starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendParaCompliance ends");
		return resp;
	}
	@GetMapping("/view-transaction-audit-master-approve-draftPara")  
	public String approveDraftParaObservation(Model model, HttpSession session, @RequestParam Integer id) {
		logger.info("Method : approveDraftParaObservation starts");
		
		model.addAttribute("AuditId", id);
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
			List<DropDownModel> stage = Arrays.asList(obs);
			
			model.addAttribute("UserStage", stage.get(0).getName());
			model.addAttribute("AuthorityStage", stage.get(0).getKey());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			model.addAttribute("questionList", questionList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
			
			model.addAttribute("commentsList", commentsList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : approveDraftParaObservation starts");
		return "audit/approveDraftObservation";
	}
	@GetMapping("/approve-transaction-audit-master")  
	public String approveTransactionObservation(Model model, HttpSession session, @RequestParam Integer id) {
		logger.info("Method : approveTransactionObservation starts");
		
		model.addAttribute("AuditId", id);
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			DropDownModel[] obs = restClient.getForObject(env.getAuditUrl() + "getApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
			List<DropDownModel> stage = Arrays.asList(obs);
			
			model.addAttribute("UserStage", stage.get(0).getName());
			model.addAttribute("AuthorityStage", stage.get(0).getKey());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getQuestionListForApprove?id="+id, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			model.addAttribute("questionList", questionList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getCommentsList?id="+id, AuditCommentsModel[].class);
			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
			
			model.addAttribute("commentsList", commentsList);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : approveTransactionObservation starts");
		return "audit/approveTransactionObservation";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "approve-transaction-audit-master-forward-to-approve", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> fowardedTransToApproveStatus(@RequestBody List<AuditObservationModel> observation,
			Model model, HttpSession session) {
		logger.info("Method : fowardedTransToApproveStatus function starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			// Variable question is used for ApprovalBy
			for(AuditObservationModel m : observation) {
				m.setQuestion(userId);
			}
			res = restClient.postForObject(env.getAuditUrl() + "forwardToApproveObservation", observation, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : fowardedTransToApproveStatus function Ends");
		return res;
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-transaction-audit-master-get-IrStatusReport" })
	public @ResponseBody JsonResponse<Object> IrStatusReport(Model model,
			@RequestBody AuditMasterModel auditMasterModel, HttpSession session) throws IOException {
		logger.info("Method : IrStatusReport starts");
		//System.out.println(auditMasterModel.getAuditAutoGenId());
		//System.out.println(auditMasterModel.getDepartment());
		//System.out.println(auditMasterModel.getOrganisation());
		// System.out.println(obj.charAt(3));
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		auditMasterModel.setCreatedBy(userId);
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}

				auditMasterModel.setGuideLineDoc(contentName);

				try {
					resp = restClient.postForObject(env.getAuditUrl() + "getIRStatus", auditMasterModel,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}

				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.postForObject(env.getAuditUrl() + "getIRStatus", auditMasterModel,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("Success");
		}
		logger.info("Method : IrStatusReport ends");
		return resp;
	}
	/*
	 * get Mapping for view IrStatus
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-get-IrStatusReportView", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getIrStatusReportView(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :getIrStatusReportView function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getIrStatusReportView?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getIrStatusReportView function Ends");
		return res;
	}
	/*
	 * get Mapping for view IrStatus
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-transaction-audit-master-getIrStatus", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> sendIrStatus(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :sendIrStatus function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getIrStatusReportView?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : sendIrStatus function Ends");
		return res;
	}
	/*
	* approve accept
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "approve-transaction-audit-master-sendDraft" })
	public @ResponseBody JsonResponse<Object> sendDraft(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendRequestRequisition starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendDraft?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendDraft?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendDraft ends");
		return resp;
	}
	/*
	* approve accept
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "approve-transaction-audit-master-sendDraftPara" })
	public @ResponseBody JsonResponse<Object> sendDraftPara(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendDraftPara starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendDraftPara?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendDraftPara?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendDraftPara ends");
		return resp;
	}
	/*
	 * get Mapping for view draft Para
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/approve-transaction-audit-master-getDraftPara", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getDraftPara(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :getDraftPara function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDraftPara?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getDraftPara function Ends");
		return res;
	}
	/*
	 * get Mapping for view draft Para
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-audit-master-getDraftPara", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getMstrDraftPara(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method :getMstrDraftPara function starts");

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDraftPara?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : getMstrDraftPara function Ends");
		return res;
	}
	/*
	* sendCompliance
	*
	*
	*/
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-audit-master-sendParaCompliance" })
	public @ResponseBody JsonResponse<Object> sendParaCompliance(Model model, @RequestParam("id") String encodeId,
			@RequestParam("comment") String comment, HttpSession session) throws IOException {
		logger.info("Method : sendCompliance starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");

				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
					contentName = nowTime + ".docx";
				} else if (fileType[1].contentEquals("msword")) {
					contentName = nowTime + ".doc";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals(".xlsx")) {
					contentName = nowTime + ".xls";
				} else if (fileType[1].contentEquals("vnd.ms-excel")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}


				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				if (resp.getCode().contains("Data Saved Successfully")) {
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

							Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
							BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
							imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

							ByteArrayOutputStream buffer = new ByteArrayOutputStream();

							ImageIO.write(imageBuff, "png", buffer);

							// ByteArrayOutputStream out = new ByteArrayOutputStream();
							byte[] thumb = buffer.toByteArray();
							Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + contentName);
							Files.write(pathThumb, thumb);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}

			} else {
				try {
					resp = restClient.getForObject(env.getAuditUrl() + "sendParaCompliance?id=" + encodeId
							+ "&createdBy=" + userId + "&document=" + contentName + "&comment=" + comment,
							JsonResponse.class);
				} catch (RestClientException e) {
					e.printStackTrace();
				}
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
		logger.info("Method : sendParaCompliance ends");
		return resp;
	}
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-transaction-audit-master-view-comment" })
	public @ResponseBody JsonResponse<Object> viewCompliance(Model model, @RequestBody Integer id,
			BindingResult result) {

		logger.info("Method : viewCompliance starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getTransViewCommentList?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : viewCompliance ends");
		return res;
	}
	
}
