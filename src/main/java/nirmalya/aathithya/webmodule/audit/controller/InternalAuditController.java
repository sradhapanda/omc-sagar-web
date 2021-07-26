
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

import nirmalya.aathithya.webmodule.audit.model.AuditCommentsModel;
import nirmalya.aathithya.webmodule.audit.model.AuditComplianceModel;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.audit.model.InternalRequisitionViewModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.audit.model.PersonModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "audit")
public class InternalAuditController {

	Logger logger = LoggerFactory.getLogger(InternalAuditController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-internal-audit")
	public String viewInitiatedAudit(Model model, HttpSession session) {
		logger.info("Method : viewInitiatedAudit starts");
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
		logger.info("Method : viewInitiatedAudit ends");
		return "audit/viewInternalAudit";
	}
	
	/*
	 * For view audit master Ajax call
	 */

//
//	@SuppressWarnings({ "unchecked" })
//	@GetMapping("/view-internal-audit-ThroughAjax")
//	public @ResponseBody DataTableResponse viewAuditMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
//			@RequestParam String param4,
//			HttpSession session) {
//		logger.info("Method : viewAuditMasterThroughAjaxrrrrr statrs");
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
//			tableRequest.setParam4(param4);
//			//tableRequest.setParam5(param5);
//			//tableRequest.setParam6(param6);
//			tableRequest.setUserId(userId);
//
//			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();
//
//			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllInternalAudit", tableRequest,
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
//				byte[] deptId = Base64.getEncoder().encode(m.getRegion().getBytes());
//				byte[] sectionId = Base64.getEncoder().encode(m.getAuditParty().getBytes());
//
//				if (m.getReqStatus() == 0) {
//					m.setAuditAt("Co-ordinator");
//				}
//				if (m.getReqStatus() == 1) {
//					m.setAuditAt("Regional Manager");
//				}
//				if (m.getReqStatus() == 2) {
//					m.setAuditAt("Co-ordinator");
//				}
//				if (m.getReqStatus() == 3) {
//					m.setAuditAt("Auditor");
//				}
//				if (m.getReqStatus() == 3 && m.getAuditStatus() == 2) {
//					m.setAuditAt("Co-ordinator");
//				}
//				if (m.getReqStatus() == 4) {
//					m.setAuditAt("Regional Manager");
//
//				}
//				if (m.getSectionHeadStatus() == 1) {
//					m.setAuditAt("Concern Finance");
//
//				}
//				if (m.getSectionHeadStatus() == 2) {
//					m.setAuditAt("Regional Manager");
//				}
//				if (m.getSectionHeadStatus() == 3 && m.getCoStatus() == 1) {
//					m.setAuditAt("Auditor");
//				}
//				if (m.getCoStatus() == 0) {
//					m.setAuditAt("Co-ordinator");
//				}
//				/*
//				 * if( m.getCoStatus()==1 ) { m.setAuditAt("Section Head"); }
//				 */
//
//				if (m.getReqStatus() == 5 && m.getSectionHeadStatus() == 0 && m.getCoStatus() == 1) {
//
//					m.setAuditAt("Regional Manager");
//				}
//				if (m.getReqStatus() == 6 && m.getAuditStatus() == 2) {
//					m.setAuditAt("Auditor");
//				}
//				/*
//				 * if( m.getCoStatus()==3 && m.getReqStatus()==6 && m.getAuditStatus()==2) {
//				 * m.setAuditAt("Section Head"); }
//				 */ if (m.getCoStatus() == 1 && m.getReqStatus() == 6 && m.getAuditStatus() == 2) {
//					m.setAuditAt("Co-ordinator");
//				}
//				if (m.getApproveStatus() == 1) {
//					m.setAuditAt("GM");
//				}
//				if (m.getApproveStatus() == 2) {
//					m.setAuditAt("Director");
//				}
//				if (m.getApproveStatus() == 3) {
//					m.setAuditAt("Audit Committee");
//				}
//				if (m.getApproveStatus() == 4) {
//					m.setAuditAt("Board Of Director");
//				}
//				if (m.getApproveStatus() == 5) {
//					m.setAuditAt("AG");
//				}
//
//				if (m.getAuditStatus() == 0) {
//
//					if (roles.contains("rol024") && m.getReqStatus() == 3) {
//
//						s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//								+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//						s = s + "<a title='Question' href='/audit/view-internal-audit-question?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'><i class=\"fa fa-question-circle faIcon\" aria-hidden=\"true\"></i></a>";
//
//						m.setStatus("open");
//
//					} else if (roles.contains("rol021")) {
//						
//						if (m.getReqStatus() == 1) {
//							s = s + "<a title='View Request' href=javascript:void(0)  onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")' ><i class=\"fa fa-street-view faIcon\" aria-hidden=\"true\" ></i> </a>";
//
//						} else if (m.getReqStatus() == 2) {
//
//							s = s + "<a title='Requested' href=javascript:void(0)  onclick='reqRequisition(\""
//									+ m.getAuditAutoGenId()+ "\",\""+m.getReqStatus()+"\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\\\"true\\\"></i>  </a>";
//
//							/*
//							 * s = s +
//							 * "<a title='Forward Requisition' href=javascript:void(0) onclick='forwardRequisition(\""
//							 * + m.getAuditAutoGenId() +
//							 * "\")' ><i class='fa fa-forward faIcon' aria-hidden=\"true\"></i> </a>"; s = s
//							 * +
//							 * "<a title='Reject Requisition' href=javascript:void(0) onclick='rejectFromCoordinator(\""
//							 * + m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-close faIcon\"></i> </a>";
//							 */
//
//						} else if (m.getReqStatus() == 0 && !m.getDtlStatus()) {
//							s = s + "<a title='Request Requisition' href=javascript:void(0 ) onclick='requestRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")'><i class='fa fa-exchange faIcon' aria-hidden='true'></i></a>";
//
//						} else if (m.getReqStatus() == 3) {
//							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//									+ "\",\""+m.getReqStatus()+"\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//						}
//
//						else {
//							s = s + "N/A";
//						}
//
//					} else if (roles.contains("rol033")) {
//						if (m.getSectionReqStatus() == 1 || m.getSectionReqStatus() == 3) {
//							s = s + "<a title='Reply to requisition' href=javascript:void(0) onclick='responseRequisition(\""
//									+ m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-reply faIcon' aria-hidden=\"true\"></i> </a>";
//
//						} else if (m.getSectionReqStatus() == 2) {
//							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//						} else if (m.getSectionReqStatus() == 2) {
//							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//						} else {
//							s = s + ("N/A");
//						}
//					} else if (roles.contains("rol032")) {
//						if (m.getReqStatus() == 1) {
//							s = s + "<a title='Reply to requisition' href=javascript:void(0) onclick='responseRequisition(\""
//									+ m.getAuditAutoGenId() + "\",\"" + m.getSectionReqStatus()
//									+ "\")' ><i class='fa fa-reply faIcon' aria-hidden=\"true\"></i> </a>";
//
//						} else if (m.getReqStatus() == 2) {
//							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//							s = s + "<a title='View Response' onclick='editRequisition(\"" + m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-edit' style='font-size:25px;color:red' aria-hidden=\"true\"></i> </a>";
//						} else if (m.getReqStatus() == 3) {
//							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
//									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";
//
//						} else {
//							s = s + ("N/A");
//						}
//					}
//					m.setStatus("N/A");
//
//				} else if (m.getAuditStatus() == 1) {
//					if (roles.contains("rol024") && m.getReqStatus() == 3) {
//						s = s + "<a Title='Observation' href='/audit/view-internal-audit-observation?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'><i class='fa fa-search faIcon' aria-hidden='true'></i></a>&nbsp;&nbsp;";
//						s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getAuditAutoGenId()
//								+ "\")'><i class='fa fa-download faIcon' ></i></a>";
//
//					}
//
////					else if(roles.contains("rol021") && m.getReqStatus()==3){
////						
////							s = s + "&nbsp;&nbsp;&nbsp;<a title='Requested' onclick='forwardQuestion(\""
////									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'>Forword Question</button> </a>";
////						}
//
//					else {
//						s = "N/A";
//					}
//					m.setStatus("Not Observed");
//				} else if (m.getAuditStatus() == 2) {
//					m.setStatus("Open");
//					if (roles.contains("rol024") && m.getReqStatus() == 6) {
//						s = s + "<a href='/audit/view-internal-audit-observation?id=" + m.getAuditAutoGenId()
//								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
//					} else if (roles.contains("rol022") || roles.contains("rol032") && m.getReqStatus() == 5) {
//						s = s + "<a Title='View' href='/audit/view-internal-audit-observation?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'> <i class='fa fa-eye faIcon' aria-hidden=\\\"true\\\"></i></a>";
//
//					} else if (roles.contains("rol021") && m.getReqStatus() == 3) {
//
//						s = s + "<a Title='Forward Observation' href='/audit/view-internal-audit-forwardObs?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
//					}
//
//					else if (roles.contains("rol021") && m.getCoStatus() == 0 && m.getReqStatus() != 4) {
//						System.out.println("here");
//						if (m.getForwardStatus() == 1) {
//							System.out.println("if");
//							s = s + "<a Title='Forwared' href='/audit/view-internal-audit-forwardObs?id="
//									+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//									+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//									+ m.getApproveStatus()
//									+ "'> <i class='fa fa-send faIcon' aria-hidden='true'></i></a>";
//						} else {
//							System.out.println("else");
//							s = s + "<a Title='Forward Reply g ' href='/audit/view-internal-audit-forwardObs?id="
//									+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//									+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//									+ m.getApproveStatus()
//									+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
//							/*
//							 * s = s +
//							 * "<a title='Reject Reply' href=javascript:void(0) onclick='rejectObsByCo(\"" +
//							 * m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-close faIcon\"></i> </a>";
//							 */
//						}
//
//					}
//					/*
//					 * else if (roles.contains("rol021") && m.getReqStatus() ==5 &&
//					 * m.getCoStatus()==1) {
//					 * 
//					 * s = s +
//					 * "<a Title='Forward Reply' href='/audit/view-internal-audit-forwardObs?id="
//					 * + m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId=" +
//					 * new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus=" +
//					 * m.getApproveStatus() +
//					 * "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>"; }
//					 */
//					else if (roles.contains("rol021") && m.getCoStatus() == 3 && m.getReqStatus() == 5) {
//
//						s = s + "<a Title='Forward Reply 1' href='/audit/view-internal-audit-forwardObs?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
//					} else if (roles.contains("rol021") && m.getCoStatus() == 1 && m.getReqStatus() == 6) {
//
//						s = s + "<a Title='Forward Reply 2' href='/audit/view-internal-audit-forwardObs?id="
//								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//								+ m.getApproveStatus()
//								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
//					}
//
//					else if (roles.contains("rol022") || roles.contains("rol032") && m.getReqStatus() == 4) {
//						s = s + "<a href='/audit/view-internal-audit-observation?id=" + m.getAuditAutoGenId()
//								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
//
//					} else if (roles.contains("rol022") || roles.contains("rol032") && m.getReqStatus() == 3) {
//						s = s + "N/A";
//					} else if (m.getApproveStatus() > 0 && m.getApproveStatus() < 5) {
//						s = s + "<a href='/audit/approve-internal-audit?id=" + m.getAuditAutoGenId() + "&deptId="
//								+ new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
//
//					} else if (roles.contains("rol033")) {
//						s = s + "<a href='/audit/view-internal-audit-giveReply?id=" + m.getAuditAutoGenId()
//								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'><i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
//
//					}
//
//					else {
//						s = s + "<a href='/audit/view-internal-audit-observation?id=" + m.getAuditAutoGenId()
//								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
//								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
//								+ "'><i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
//
//					}
//
//				} else if (m.getAuditStatus() == 3) {
//					m.setStatus("Closed");
//					s = s + "<a Title='View Details' href='/audit/view-internal-audit-observation?id="
//							+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
//							+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
//							+ m.getApproveStatus()
//							+ "'> <i class='fa fa-list-alt faIcon' aria-hidden='true'></i>	</a>&nbsp;&nbsp;";
////					if(roles.contains("rol024")) {
////						s = s + "<a Title='Submit Report' href='javascript:void' onclick='submitReport(\"" + m.getAuditAutoGenId() + "\")'>\r\n" + 
////								"<i class=\"fa fa-send-o faIcon\"></i></a>";
////					}
//					s = s + "<a Title='Report' href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId()
//							+ "\")'><i class='fa fa-file faIcon' aria-hidden='true'></i></a>";
//				}
//				if (roles.contains("rol021")) {
//					if (m.getAuditStatus() == 0) {
//						m.setStatus("N/A");
//					} else if (m.getAuditStatus() == 1) {
//						m.setStatus("Not Observed");
//					} else if (m.getAuditStatus() == 2) {
//						m.setStatus("Open");
//					} else if (m.getAuditStatus() == 3) {
//						m.setStatus("Close");
//					}
//
//				}
//				if (roles.contains("rol022") || roles.contains("rol032")) {
//					if (m.getAuditStatus() == 0) {
//						m.setStatus("N/A");
//					} else if (m.getAuditStatus() == 1) {
//						m.setStatus("Not Observed");
//					} else if (m.getAuditStatus() == 2) {
//						m.setStatus("Open");
//					} else if (m.getAuditStatus() == 3) {
//						m.setStatus("Close");
//					}
//
//				}
//
//				/*
//				 * if(m.getAuditStatus() == 2 && roles.contains("rol021") ) {
//				 * m.setStatus("Open"); s = s + "<a href='/audit/observation-forTransAudit?id="
//				 * + m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId=" +
//				 * new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus=" +
//				 * m.getApproveStatus() +
//				 * "'> <button class=\"btn btn-warning\"> View </button></a>"; }
//				 */
//				model.addAttribute("reqSectionId", m.getSectionReqStatus());
//				System.out.println(m.getSectionReqStatus() + "m.getSectionReqStatus()");
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
	@RequestMapping(value = "/view-internal-audit-getDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> viewAudDocument(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : viewAudDocument function starts");

		try {
			//System.out.println(id);
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
	@PostMapping(value = { "view-internal-audit-sendRequest" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendRequestRequisition(Model model, @RequestBody InternalRequisitionViewModel obj,
			BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : sendRequestRequisition starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}

		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile" + inputFile);
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
				} else if (fileType[1].contentEquals("xls")) {
					contentName = nowTime + ".xls";
				} else {
					contentName = nowTime + "." + fileType[1];
				}
				obj.setCreatedby(userId);
				obj.setDocument(contentName);
				resp = restClient.postForObject(env.getAuditUrl() + "sendInternRequestRequisition", obj,
						JsonResponse.class);

				if (resp.getCode().contains("Data Saved Successfully")) {
					Path path = Paths.get(env.getFileUploadAudit() + contentName);
				//	System.out.println("Path" + path);
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
				obj.setCreatedby(userId);
				resp = restClient.postForObject(env.getAuditUrl() + "sendInternRequestRequisition", obj,
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
		logger.info("Method : sendRequestRequisition ends");
		//System.out.println(resp);
		return resp;
	}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-internal-audit-uploadFile")
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
	@RequestMapping(value = "/view-internal-audit-getReqDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getReqAudDocument(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getReqAudDocument function starts");
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			res = restClient.getForObject(env.getAuditUrl() + "getInternReqDocument?id=" + id + "&userId=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		List<String> roles = null;
		try {
			roles = (List<String>) session.getAttribute("USER_ROLES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(roles);
		if (roles.contains("rol032")) {

			res.setCode("rol032");
		}
		if (roles.contains("rol021")) {

			res.setCode("rol021");
		}
		//System.out.println(res);
		logger.info("Method : getReqAudDocument function Ends");
		return res;
	}

	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-sendResponse" })
	public @ResponseBody JsonResponse<Object> sendResponse(Model model, @RequestBody  RequisitionViewModel obj,BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : sendResponse starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		obj.setCreatedby(userId);
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile" + inputFile);
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
					obj.setDocument(contentName);
				try {
					resp = restClient
							.postForObject(
									env.getAuditUrl() + "sendStatuResponseRequisition",obj,
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
				
				resp = restClient
						.postForObject(
								env.getAuditUrl() + "sendStatuResponseRequisition",obj,
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
		logger.info("Method : sendResponse ends");
		//System.out.println(resp);
		return resp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardReq", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> fowardedReq(@RequestParam("id") String id, Model model,
			HttpSession session) {
		logger.info("Method : fowardedReq function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			res = restClient.getForObject(env.getAuditUrl() + "fowardedStatuReq?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
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
	@GetMapping("/view-internal-audit-question")
	public String addAuditQuestion(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status) {

		logger.info("Method : addAuditQuestion starts");

		model.addAttribute("initiatedId", id);
		model.addAttribute("deptId", deptId);
		model.addAttribute("sectionId", sectionId);

		logger.info("Method : addAuditQuestion ends");

		return "audit/add-audit-internal-question";
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-internal-audit-question-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addQuestion(
			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		//System.out.println("auditObservationModel"+auditObservationModel);
		logger.info("Method : addQuestion function starts");
		int imagecount = 0;
		for (AuditObservationModel a : auditObservationModel) {
			if (a.getDocument() != null && a.getDocument() != "") {
				String delimiters = "\\.";
				String[] x = a.getDocument().split(delimiters);
				//System.out.println("image" + x[1]);
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

			res = restClient.postForObject(env.getAuditUrl() + "restAddInternQuestion", auditObservationModel,
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
	@RequestMapping(value = "view-internal-audit-forwardQus", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> fowardedQus(@RequestParam("id") String id, Model model,
			HttpSession session) {
		logger.info("Method : fowardedQus function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			res = restClient.getForObject(env.getAuditUrl() + "fowardedQus?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
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
	@RequestMapping(value = "/view-internal-audit-getQuestion", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getQuestion(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getQuestion function starts");

		try {
		//	System.out.println(id);
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
	//	System.out.println(res);
		return res;
	}

//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/view-internal-audit-observation")
//	public String addInternAuditObservation(Model model, HttpSession session, @RequestParam Integer id,
//			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status,
//			@RequestParam Byte approveStatus) {
//		logger.info("Method : addInternAuditObservation starts");
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.addAttribute("Status", status);
//		model.addAttribute("ApproveStatus", approveStatus);
//		model.addAttribute("AuditId", id);
//		model.addAttribute("DeptId", deptId);
//		model.addAttribute("SectionId", sectionId);
//
//		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
//		String deptId1 = (new String(encodeByte));
//		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
//		String sectionId1 = (new String(encodeByte1));
//		
//		try {
//			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getInternQuestionList?id=" + id
//					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
//					AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//
//			for (AuditObservationModel m : questionList) {
//				auditQuestionStatus.addAll(m.getAuditStatus());
//			}
//
//			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
//			model.addAttribute("questionList", questionList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addInternAuditObservation ends");
//		return "audit/add-audit-observationForInternalAudit";
//	}

	// @SuppressWarnings({ "unchecked", "restriction" })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-save", method = { RequestMethod.POST })
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

			res = restClient.postForObject(env.getAuditUrl() + "addInternObservation", observation, JsonResponse.class);
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

	@SuppressWarnings("unchecked")
	@PostMapping("/view-internal-audit-comment-save")
	public @ResponseBody JsonResponse<Object> auditeeStatuCommentSave(Model model,
			@RequestBody AuditComplianceModel index, HttpSession session) throws IOException {

		logger.info("Method : auditeeStatuCommentSave starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile" + inputFile);
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
				index.setDocument(contentName);

				index.setCreatedBy(userId);

				// System.out.println("env.getAuditUrl() + \"addNewAudit\"");
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeStatuCommentSave", index,
						JsonResponse.class);

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
				index.setCreatedBy(userId);
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeStatuCommentSave", index,
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
			session.removeAttribute("imageAuditeeFile");
		}
		logger.info("Method : auditeeStatuCommentSave ends");
		return resp;
	}

//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/view-internal-audit-forwardObs")
//	public String addAuditObservationforTransAudit(Model model, HttpSession session, @RequestParam Integer id,
//			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status,
//			@RequestParam Byte approveStatus) {
//		logger.info("Method : addAuditObservationforTransAudit starts");
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.addAttribute("Status", status);
//		model.addAttribute("ApproveStatus", approveStatus);
//		model.addAttribute("AuditId", id);
//		model.addAttribute("DeptId", deptId);
//		model.addAttribute("SectionId", sectionId);
//
//		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
//		String deptId1 = (new String(encodeByte));
//		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
//		String sectionId1 = (new String(encodeByte1));
//		System.out.println(env.getAuditUrl() + "getInternQuestionList?id=" + id + "&deptId=" + deptId1 + "&sectionId="
//				+ sectionId1);
//		try {
//			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getInternQuestionList?id=" + id
//					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
//					AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			for (AuditObservationModel m : questionList) {
//				auditQuestionStatus.add(m.getAuditStatus());
//			}
//			model.addAttribute("questionList", questionList);
//			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		/** persons to forward **/
//		try {
//			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getInternPersonList",
//					PersonListModel[].class);
//			List<PersonListModel> personList = Arrays.asList(dept);
//
//			model.addAttribute("personList", personList);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addAuditObservationforTransAudit ends");
//		return "audit/add-audit-observationForInternalAudit";
//	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardObservationByCo", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> forwardObs(@RequestBody RequisitionViewModel obj, Model model,
			HttpSession session) {
		logger.info("Method : forwardObs function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.setCreatedby(userId);
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardInternObs",obj,
					JsonResponse.class);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forward-to-approve", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> fowardedToApproveforTransAudit(
			@RequestBody List<AuditObservationModel> observation, Model model, HttpSession session) {
		logger.info("Method : fowardedToApproveforTransAudit function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// Variable question is used for ApprovalBy
			for (AuditObservationModel m : observation) {
				m.setQuestion(userId);
			}
			res = restClient.postForObject(env.getAuditUrl() + "approveObsInStatuAudit", observation,
					JsonResponse.class);
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

	@GetMapping("/approve-internal-audit")
	public String approveStatutaryAudit(Model model, HttpSession session, @RequestParam Integer id) {
		logger.info("Method : approveStatutaryAudit starts");

		model.addAttribute("AuditId", id);

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			DropDownModel[] obs = restClient.getForObject(
					env.getAuditUrl() + "getStatuApprovalStage?id=" + id + "&userId=" + userId, DropDownModel[].class);
			List<DropDownModel> stage = Arrays.asList(obs);

			model.addAttribute("UserStage", stage.get(0).getName());
			model.addAttribute("AuthorityStage", stage.get(0).getKey());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			AuditObservationModel[] obs = restClient.getForObject(
					env.getAuditUrl() + "getQuestionListForApprove?id=" + id, AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
			//System.out.println("questionList" + questionList);
			model.addAttribute("questionList", questionList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getStatuCommentsList?id=" + id,
					AuditCommentsModel[].class);
			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
			//System.out.println("commentsList" + commentsList);
			model.addAttribute("commentsList", commentsList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : approveStatutaryAudit starts");
		return "audit/approve-internal-audit";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "approve-internal-audit-forward-to-approve", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> fowardedToApproveStatus(
			@RequestBody List<AuditObservationModel> observation, Model model, HttpSession session) {
		logger.info("Method : fowardedToApproveStatus function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// Variable question is used for ApprovalBy
			for (AuditObservationModel m : observation) {
				m.setQuestion(userId);
			}
			res = restClient.postForObject(env.getAuditUrl() + "forwardStatutaryToApprove", observation,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : fowardedToApproveStatus function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-internal-audit-view-comment" })
	public @ResponseBody JsonResponse<Object> viewCompliance(Model model, @RequestParam("id") Integer id,
			HttpSession session) {

		logger.info("Method : viewCompliance starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getStatuViewCommentList?id=" + id + "&userId=" + userId,
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

		logger.info("Method : viewCompliance ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardReply", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardReply(@RequestParam("id") String id, Model model,
			HttpSession session) {
		logger.info("Method : forwardReply function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			res = restClient.getForObject(env.getAuditUrl() + "forwardInternReply?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardReply function Ends");
		return res;
	}

//	/*
//	 * Get Mapping view audit master
//	 */
//	@GetMapping("/view-internal-audit-giveReply")
//	public String giveReply(Model model, HttpSession session, @RequestParam Integer id, @RequestParam String deptId,
//			@RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
//		logger.info("Method : giveReply starts");
//		String userId = "";
//		try {
//			userId = (String) session.getAttribute("USER_ID");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		model.addAttribute("Status", status);
//		model.addAttribute("ApproveStatus", approveStatus);
//		model.addAttribute("AuditId", id);
//		model.addAttribute("DeptId", deptId);
//		model.addAttribute("SectionId", sectionId);
//
//		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
//		String deptId1 = (new String(encodeByte));
//		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
//		String sectionId1 = (new String(encodeByte1));
//		System.out.println(env.getAuditUrl() + "getStatuQuestionList?id=" + id + "&deptId=" + deptId1 + "&sectionId="
//				+ sectionId1);
//		try {
//			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
//			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getInternQuestionList?id=" + id
//					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
//					AuditObservationModel[].class);
//			List<AuditObservationModel> questionList = Arrays.asList(obs);
//
//			for (AuditObservationModel m : questionList) {
//				auditQuestionStatus.add(m.getAuditStatus());
//			}
//
//			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
//			System.out.println(questionList.get(0).getiRStatus());
//			model.addAttribute("questionList", questionList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : giveReply ends");
//		return "audit/InternalObservationForConcernFinance";
//	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardToCo", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardToCo(@RequestParam("id") String id, Model model,
			HttpSession session) {
		logger.info("Method : forwardToCo function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			res = restClient.getForObject(env.getAuditUrl() + "forwardInternToCo?id=" + id + "&createdBy=" + userId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardToCo function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/view-internal-audit-auditor-comment-save")
	public @ResponseBody JsonResponse<Object> auditorCommentSave(Model model, @RequestBody AuditComplianceModel index,
			HttpSession session) throws IOException {

		logger.info("Method : auditorCommentSave starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		index.setCreatedBy(userId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		if (index.getAuditStatus() == 4) {
			try {
				index.setCreatedBy(userId);
				resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave", index, JsonResponse.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			index.setCreatedBy(userId);
			try {
				MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
				String contentName = "";
				if (inputFile != null) {
					long nowTime = new Date().getTime();

					byte[] bytes = inputFile.getBytes();
					String[] fileType = inputFile.getContentType().split("/");
					
					if (fileType[1].contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")) {
						contentName = nowTime + ".docx";
					} else if (fileType[1]
							.contentEquals("vnd.openxmlformats-officedocument.presentationml.presentation")) {
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
					index.setDocument(contentName);

					try {
						resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave", index,
								JsonResponse.class);

					} catch (RestClientException e) {
						e.printStackTrace();
					}
					if (resp.getCode().contains("Data Saved Successfully")) {
						Path path = Paths.get(env.getFileUploadAudit() + contentName);
						if (fileType[1].contentEquals("pdf")) {
							Files.write(path, bytes);
						} else if (fileType[1]
								.contentEquals("vnd.openxmlformats-officedocument.wordprocessingml.document")
								|| fileType[1].contentEquals("msword")) {
							Files.write(path, bytes);
						} else if (fileType[1].contentEquals("docx") || fileType[1].contentEquals("doc")) {
							Files.write(path, bytes);
						} else if (fileType[1].contentEquals("xls") || fileType[1].contentEquals("xlsx")) {
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
									BufferedImage imageBuff = new BufferedImage(width, height,
											BufferedImage.TYPE_INT_ARGB);
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

					resp = restClient.postForObject(env.getAuditUrl() + "auditorCommentSave", index,
							JsonResponse.class);

				}
			} catch (RestClientException e) {
				e.printStackTrace();
			}
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
			session.removeAttribute("imageAuditorFile");
			//System.out.println(session.getAttribute("imageAuditorFile"));
		}
		logger.info("Method : auditorCommentSave ends");
		return resp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-co-ordinatorforward", method = { RequestMethod.POST },produces="application/json")
	public @ResponseBody JsonResponse<Object> coordinatorforward(@RequestBody AuditComplianceModel obj, Model model, HttpSession session) {
		logger.info("Method : coordinatorforward function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		obj.setCreatedBy(userId);
		try {

			res = restClient.postForObject(
					env.getAuditUrl() + "coordinatorInternforward",obj,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		logger.info("Method : coordinatorforward function Ends");
		return res;
	}
	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-forwardToCrn" },produces="application/json")
	public @ResponseBody JsonResponse<Object> forwardToCrn(Model model, @RequestBody AuditComplianceModel obj,
			BindingResult result ,HttpSession session) {
		logger.info("Method : forwardToCrn starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
			
		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedBy(userId);
			//System.out.println(obj);
			res = restClient.postForObject(env.getAuditUrl() + "forwardToCrn",obj,	JsonResponse.class);
			obj.setCreatedBy(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : forwardToCrn ends");
		return res;
	}
	

	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-reqForwardBysec" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> reqForwardBysec(Model model, @RequestBody RequisitionViewModel obj,
			HttpSession session) throws IOException {
		logger.info("Method : sendResponse starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
		} catch (Exception e) {
		}
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
			//System.out.println("inputFile" + inputFile);
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
				obj.setDocument(contentName);
				try {
					resp = restClient.postForObject(env.getAuditUrl() + "reqInternForwardBysec", obj, JsonResponse.class);
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

				resp = restClient.postForObject(env.getAuditUrl() + "reqInternForwardBysec", obj, JsonResponse.class);

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
		//System.out.println(resp);
		return resp;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-rejectReq", method = { RequestMethod.POST },produces="application/json")
	public @ResponseBody JsonResponse<Object> rejectReq(Model model, @RequestBody RequisitionViewModel obj, HttpSession session) {
		logger.info("Method : rejectReq function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.setCreatedby(userId);
		try {

			res = restClient.postForObject(
					env.getAuditUrl() + "rejectReq",obj,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : rejectReq function Ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-saveComment" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> saveComment(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		logger.info("Method : saveComment starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveRequisitionComment", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveComment ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-internal-audit-getComment" })
	public @ResponseBody JsonResponse<DropDownModel> getSummery(Model model, @RequestParam("id") String id,
			HttpSession session) {
		logger.info("Method : getComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getRequisitionComment?id=" + id, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getComment ends");
		//System.out.println(res);
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-saveResponse" })
	public @ResponseBody JsonResponse<Object> saveResponse(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		logger.info("Method : saveResponse starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveInternResponse", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveResponse ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-saveSecComment" })
	public @ResponseBody JsonResponse<Object> saveSecComment(Model model, @RequestBody AuditComplianceModel index,
			BindingResult result, HttpSession session) {
		logger.info("Method : saveSecComment tarts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> res = new JsonResponse<Object>();
		index.setCreatedBy(userId);

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveSecComment", index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : saveSecComment ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-internal-audit-getSecComment" })
	public @ResponseBody JsonResponse<DropDownModel> getSecComment(Model model, HttpSession session,
			@RequestParam("queId") String queId) {
		logger.info("Method : getSecComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getSecComment?queId=" + queId, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getSecComment ends");
		
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-saveObsComment" })
	public @ResponseBody JsonResponse<Object> saveObsComment(Model model, @RequestBody AuditComplianceModel index,
			BindingResult result, HttpSession session) {
		logger.info("Method : saveSecComment starts");
		
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JsonResponse<Object> res = new JsonResponse<Object>();
		index.setCreatedBy(userId);
		
		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveObsComment", index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : saveObsComment ends");
		return res;
	}
	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-internal-audit-getObsComment" })
	public @ResponseBody JsonResponse<DropDownModel> getObsComment(Model model, HttpSession session, @RequestParam("queId") String queId) {
		logger.info("Method : getObsComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getObsComment?queId=" + queId,  JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getObsComment ends");
	//	System.out.println(res);
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-saveConAudiComment" })
	public @ResponseBody JsonResponse<Object> saveConAudiComment(Model model, @RequestBody AuditComplianceModel index,
			BindingResult result, HttpSession session) {
		logger.info("Method : saveConAudiComment starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		index.setCreatedBy(userId);
		
		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveConAudiComment", index, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveConAudiComment ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-internal-audit-getConAudiComment" })
	public @ResponseBody JsonResponse<DropDownModel> getConAudiComment(Model model, HttpSession session, @RequestParam("queId") String queId) {
		logger.info("Method : getConAudiComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
	try {
			res = restClient.getForObject(env.getAuditUrl() + "getConAudiComment?queId=" + queId,  JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getConAudiComment ends");

		return res;
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/view-internal-audit-comment-consernsave")
	public @ResponseBody JsonResponse<Object> consernsave(Model model, @RequestBody AuditComplianceModel index,
			HttpSession session) throws IOException {

		logger.info("Method : consernsave starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			MultipartFile inputFile = (MultipartFile) session.getAttribute("imageFile");
		//	System.out.println("inputFile" + inputFile);
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
				index.setDocument(contentName);

				index.setCreatedBy(userId);

				// System.out.println("env.getAuditUrl() + \"addNewAudit\"");
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeStatuCommentconsernsave", index,
						JsonResponse.class);

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
				index.setCreatedBy(userId);
				resp = restClient.postForObject(env.getAuditUrl() + "auditeeStatuCommentconsernsave", index,
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
			session.removeAttribute("imageAuditeeFile");
		}
		logger.info("Method : auditeeStatuCommentSave ends");
		return resp;
	}

	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-rejectResponse" },produces="application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectResponse(Model model, RequisitionViewModel obj ,
			 HttpSession session) {
		logger.info("Method : rejectResponse starts");
		
	//	System.out.println("obj"+obj);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
			res = restClient.postForObject(
					env.getAuditUrl() + "rejectResponse",obj,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectResponse ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardToPerson", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> forwardToPerson(@RequestBody AuditComplianceModel obj, Model model,
			HttpSession session) {
		logger.info("Method : forwardToCo function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.setCreatedBy(userId);
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardToInternPerson",obj,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardToCo function Ends");
		return res;
	}

	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-internal-audit-rejectObsBySec" })
	public @ResponseBody JsonResponse<DropDownModel> rejectObsBySec(Model model, @RequestParam("id") String id,
			@RequestParam("comment") String comment, HttpSession session) {
		logger.info("Method : rejectObsBySec starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			res = restClient.getForObject(
					env.getAuditUrl() + "rejectObsBySec?id=" + id + "&comment=" + comment + "&createdBy=" + userId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectObsBySec ends");
		return res;
	}

	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-rejectObsByCoordinator" },produces="application/json")
	public @ResponseBody JsonResponse<Object> rejectObsByCoordinator(@RequestBody RequisitionViewModel obj ,Model model, HttpSession session) {
		logger.info("Method : rejectObsByCoordinator starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
			res = restClient.postForObject(env.getAuditUrl() + "rejectObsByCoordinator",obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectObsByCoordinator ends");
		return res;
	}

//	// My view Request
//	@SuppressWarnings({ "unchecked", "restriction" })
//	@RequestMapping(value = "view-internal-audit-save-viewRequest", method = { RequestMethod.POST })
//	public @ResponseBody JsonResponse<Object> addViewRequest(@RequestBody List<RequisitionViewModel> request,
//			Model model, HttpSession session) {
//		logger.info("Method : addview request function starts");
//
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		System.out.println("@@" + request);
//		int imagecount = 0;
//		for (RequisitionViewModel a : request) {
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
//				} else if (x[1].matches("xls")) {
//					for (String s1 : a.getFile()) {
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String pdfName = saveAllXls(bytes);
//							a.setDocument(pdfName);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} else if (x[1].matches("xlsx")) {
//					for (String s1 : a.getFile()) {
//						try {
//							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
//							String pdfName = saveAllXlsx(bytes);
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
//			res = restClient.postForObject(env.getAuditUrl() + "addDocument", request, JsonResponse.class);
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

	// My view Request
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "view-internal-audit-save-viewRequest", method = { RequestMethod.POST },produces="application/json")
	public @ResponseBody JsonResponse<Object> addViewRequest(@RequestBody List<RequisitionViewModel> request,
			Model model, HttpSession session) {
		logger.info("Method : addview request function starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> res = new JsonResponse<Object>();
		//System.out.println("@@" + request);
		int imagecount = 0;
		for (RequisitionViewModel a : request) {

			a.setCreatedby(userId);

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

			res = restClient.postForObject(env.getAuditUrl() + "addDocument", request, JsonResponse.class);
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

	// ############ EditViewResponse ############
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-internal-audit-editReqDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> editReqAudDocument(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : editReqAudDocument function starts");
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			res = restClient.getForObject(env.getAuditUrl() + "editStatuReqDocument?id=" + id + "&userId=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		List<String> roles = null;
		try {
			roles = (List<String>) session.getAttribute("USER_ROLES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("@@@@@@@@@@@@@@@ROLE###############" + roles);
		if (roles.contains("rol032")) {

			res.setCode("rol032");
		}
		//System.out.println("@@@@@@@@@@@@@@@ROLE###############" + res);

		logger.info("Method : editReqAudDocument function Ends");
		return res;
	}
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-internal-audit-forwardToAuditor")
	public String viewStatutaryAuditMastersdf(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewStatutaryAuditMaster starts");
		
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getInternPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);
			//System.out.println(personList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		//System.out.println("auditor "+id);
		model.addAttribute("audit", id);
		logger.info("Method : viewStatutaryAuditMaster ends");

		return "audit/forwardInternalAuditToAuditor";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-forwardToAuditorDirect", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardToAuditoDirect(@RequestParam("audit") String id, Model model, HttpSession session) {
		logger.info("Method : forwardToAuditoDirect function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {

			res = restClient.getForObject(
					env.getAuditUrl() + "forwardToAuditorDirect?id=" + id + "&createdBy=" + userId ,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardToAuditoDirect function Ends");
		return res;
	}
	
	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-rejectObs" },produces="application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectObs(Model model, @RequestBody AuditComplianceModel obj,
			BindingResult result ,HttpSession session) {
		logger.info("Method : rejectObs starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
			
		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedBy(userId);
			res = restClient.postForObject(
					env.getAuditUrl() + "rejectObs",obj,
					JsonResponse.class);
			obj.setCreatedBy(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectObs ends");
		return res;
	}
	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-internal-audit-rejectResponse1" },produces="application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectResponse1(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result ,HttpSession session) {
		logger.info("Method : rejectResponse1 starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
			//System.out.println(obj );
		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
		//	System.out.println(obj);
			res = restClient.postForObject(
					env.getAuditUrl() + "rejectResponse",obj,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectResponse1 ends");
		return res;
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-getPersonList", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<List<PersonModel>> getPersonList(@RequestParam("id") String id, Model model, HttpSession session) {
		logger.info("Method : getPersonList function starts");

		JsonResponse<List<PersonModel>> res = new JsonResponse<List<PersonModel>>();
		
		
		try {

			res = restClient.getForObject(
					env.getAuditUrl() + "personInternList?id=" + id  ,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		if (res.getBody() != null ) {
			res.setMessage("success");
		} else {
			res.setMessage("unsuccess");
		}
		logger.info("Method : getPersonList function Ends");
		return res;
	}
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-internal-audit-viewCompliance")
	public String viewCompliance(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewStatutaryAuditMaster starts");
		
		
	//	System.out.println("auditor "+id);
		model.addAttribute("audit", id);
		logger.info("Method : viewCompliance ends");

		return "audit/viewInternalCompliance";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-co-ordinatorforwardToAuditor", method = { RequestMethod.POST },produces="application/json")
	public @ResponseBody JsonResponse<Object> ordinatorforwardToAuditor(@RequestBody AuditComplianceModel obj,Model model,  HttpSession session) {
		logger.info("Method : ordinatorforwardToAuditor function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		obj.setCreatedBy(userId);
		
		try {

			res = restClient.postForObject(
					env.getAuditUrl() + "cordinatorforwardToAuditor",obj,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		
		logger.info("Method : ordinatorforwardToAuditor function Ends");
		return res;
	}
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-internal-audit-forwardDetail")
	public String forwardDetail(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : forwardDetail starts");
		
		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getforwardInternPersonList?id="+id,
					DropDownModel[].class);
			List<DropDownModel> frowardPersonList = Arrays.asList(dept);

			model.addAttribute("frowardPersonList", frowardPersonList);
			//System.out.println(frowardPersonList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getfrowardInternPersonCCList?id="+id,
					DropDownModel[].class);
			List<DropDownModel> frowardPersonCCList = Arrays.asList(dept);

			model.addAttribute("frowardPersonCCList", frowardPersonCCList);
			//System.out.println(frowardPersonCCList);
			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		//System.out.println("auditor "+id);
		model.addAttribute("audit", id);
		logger.info("Method : forwardDetail ends");

		return "audit/forwardDetail";
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-internal-audit-master-view-forwardDetails" })
	public @ResponseBody JsonResponse<Object> forwardDetails(Model model, @RequestParam("id") Integer id,
			HttpSession session) {

		logger.info("Method : forwardDetails starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "forwardDetails?id=" + id + "&userId=" + userId,
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

		logger.info("Method : forwardDetails ends");
		return res;
	}
	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-internal-audit-forwardComment")
	public String forwardCommentDetail(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : forwardCommentDetail starts");
		
	
		//System.out.println("auditor "+id);
		model.addAttribute("audit", id);
		logger.info("Method : forwardCommentDetail ends");

		return "audit/forwardCommentDetails";
	}
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-internal-audit-master-view-forwardCommentDetails" })
	public @ResponseBody JsonResponse<Object> forwardCommentDetails(Model model, @RequestParam("id") Integer id,
			HttpSession session) {

		logger.info("Method : forwardCommentDetails starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "forwardCommentDetails?id=" + id + "&userId=" + userId,
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

		logger.info("Method : forwardCommentDetails ends");
		return res;
	}

}
