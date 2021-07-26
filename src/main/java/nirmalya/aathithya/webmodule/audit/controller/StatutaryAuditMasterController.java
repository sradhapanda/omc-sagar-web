/**
 * 
 */
package nirmalya.aathithya.webmodule.audit.controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
import nirmalya.aathithya.webmodule.audit.model.AuditDetailModel;
import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMasterModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMeetingModel;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.audit.model.DraftListModel;
import nirmalya.aathithya.webmodule.audit.model.DraftModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.audit.model.PersonModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.constant.Constant;
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeModel;

/**
 * @author USER
 *
 */
@Controller
@RequestMapping(value = "audit")
@Component
public class StatutaryAuditMasterController {
	Logger logger = LoggerFactory.getLogger(StatutaryAuditMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	@Autowired
	MailService mailService;

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-statutary-audit-master")
	public String viewStatutaryAuditMaster(Model model, HttpSession session) {

		logger.info("Method : viewStatutaryAuditMaster starts");
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditType", DropDownModel[].class);
			List<DropDownModel> auditTypeList = Arrays.asList(audit);

			model.addAttribute("auditTypeList", auditTypeList);

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

		logger.info("Method : viewStatutaryAuditMaster ends");

		return "audit/view-statutary-audit-master";
	}

	/*
	 * For view audit master Ajax call
	 */

	@SuppressWarnings({ "unchecked" })
	@GetMapping("/view-statutary-audit-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewAuditMasterThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4, @RequestParam String param5, @RequestParam String param6,
			HttpSession session) {
		logger.info("Method : viewAuditMasterThroughAjaxrrrrr statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			tableRequest.setParam5(param5);
			tableRequest.setParam6(param6);
			tableRequest.setUserId(userId);

			JsonResponse<List<AuditMasterModel>> jsonResponse = new JsonResponse<List<AuditMasterModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllStatutaryAudit", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<AuditMasterModel> auditMasterModelList = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditMasterModel>>() {
					});
			List<String> roles = null;
			try {
				roles = (List<String>) session.getAttribute("USER_ROLES");
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (AuditMasterModel m : auditMasterModelList) {
				byte[] deptId = {};
				byte[] sectionId = {};
				String s = "";
				if (m.getDepartmentId() != null) {
					deptId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
				}
				if (m.getSectionId() != null) {
					sectionId = Base64.getEncoder().encode(m.getSectionId().getBytes());
				}
//				if (m.getReqStatus() == 0) {
//					m.setAuditAt("Co-ordinator");
//				}
//				if (m.getReqStatus() == 1) {
//					m.setAuditAt("Section Head");
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
//					m.setAuditAt("Section Head");
//
//				}
//				if (m.getSectionHeadStatus() == 1) {
//					m.setAuditAt("Concern Auditee");
//
//				}
//				if (m.getSectionHeadStatus() == 2) {
//					m.setAuditAt("Section Head");
//				}
//				if (m.getSectionHeadStatus() == 3 && m.getCoStatus() == 1) {
//					m.setAuditAt("Auditor");
//				}
//				if (m.getCoStatus() == 0) {
//					m.setAuditAt("Co-ordinator");
//				}
//
//				if (m.getForwardStatus() == 1) {
//					m.setAuditAt("Waiting for approval");
//				}
//
//				if (m.getReqStatus() == 5 && m.getSectionHeadStatus() == 0 && m.getCoStatus() == 1) {
//
//					m.setAuditAt("Section Head");
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

				if (m.getAuditStatus() == "0") {

					if (roles.contains("rol024") && m.getReqStatus() == "3") {

						s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
								+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

						s = s + "<a title='Question' href='/audit/view-statutary-audit-master-question?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'><i class=\"fa fa-question-circle faIcon\" aria-hidden=\"true\"></i></a>";

						m.setStatus("open");

					} else if (roles.contains("rol021")) {
						if (m.getReqStatus() == "1") {
							s = s + "<a title='View Request' href=javascript:void(0)  onclick='reqRequisition(\""
									+ m.getAuditAutoGenId()
									+ "\")' ><i class=\"fa fa-street-view faIcon\" aria-hidden=\"true\" ></i> </a>";

						} else if (m.getReqStatus() == "2") {

							s = s + "<a title='Requested' href=javascript:void(0)  onclick='reqRequisition(\""
									+ m.getAuditAutoGenId() + "\",\"" + m.getReqStatus()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden='true'></i>  </a>";

							/*
							 * s = s +
							 * "<a title='Forward Requisition' href=javascript:void(0) onclick='forwardRequisition(\""
							 * + m.getAuditAutoGenId() +
							 * "\")' ><i class='fa fa-forward faIcon' aria-hidden=\"true\"></i> </a>"; s = s
							 * +
							 * "<a title='Reject Requisition' href=javascript:void(0) onclick='rejectFromCoordinator(\""
							 * + m.getAuditAutoGenId() + "\")' ><i class=\"fa fa-close faIcon\"></i> </a>";
							 */

						} else if (m.getReqStatus() == "0") {
							if (m.getDtlStatus()) {

								s = s + "<a title='Request Requisition' href=javascript:void(0 ) onclick='requestRequisition(\""
										+ m.getAuditAutoGenId()
										+ "\")'><i class='fa fa-exchange faIcon' aria-hidden='true'></i></a>";
								s = s + "&nbsp;&nbsp;<a title='View Details' onclick='viewDetails(\""
										+ m.getAuditInitiated()
										+ "\")' ><i class='fa fa-book faIcon' aria-hidden=\"true\"></i> </a>";
							}

						} else if (m.getReqStatus() == "3") {
							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

						}

						else {
							s = s + "N/A";
						}

					} else if (roles.contains("rol031")) {
						if (m.getSectionReqStatus() == "1" || m.getSectionReqStatus() == "3") {
							s = s + "<a title='Reply to requisition' href=javascript:void(0) onclick='responseRequisition(\""
									+ m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-reply faIcon' aria-hidden=\"true\"></i> </a>";

						} else if (m.getSectionReqStatus() == "2") {
							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

						} else if (m.getSectionReqStatus() == "2") {
							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

						} else {
							s = s + ("N/A");
						}
					} else if (roles.contains("rol023")) {
						if (m.getReqStatus() == "1") {
							s = s + "<a title='Reply to requisition' href=javascript:void(0) onclick='responseRequisition(\""
									+ m.getAuditAutoGenId() + "\",\"" + m.getSectionReqStatus()
									+ "\")' ><i class='fa fa-reply faIcon' aria-hidden=\"true\"></i> </a>";

						} else if (m.getReqStatus() == "2") {
							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

							s = s + "<a title='View Response' onclick='editRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-edit' style='font-size:25px;color:red' aria-hidden=\"true\"></i> </a>";
						} else if (m.getReqStatus() == "3") {
							s = s + "<a title='View Response' onclick='reqRequisition(\"" + m.getAuditAutoGenId()
									+ "\")' ><i class='fa fa-comments-o faIcon' aria-hidden=\"true\"></i> </a>";

						} else {
							s = s + ("N/A");
						}
					}
					m.setStatus("N/A");

				} else if (m.getAuditStatus() == "1") {
					if (roles.contains("rol024") && m.getReqStatus() == "3") {
						s = s + "<a Title='Observation' href='/audit/view-statutary-audit-master-observation?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'><i class='fa fa-search faIcon' aria-hidden='true'></i></a>&nbsp;&nbsp;";
						s = s + "<a href='javascript:void' onclick='pdfCreate(\"" + m.getAuditAutoGenId()
								+ "\")'><i class='fa fa-download faIcon' ></i></a>";

					}

//					else if(roles.contains("rol021") && m.getReqStatus()==3){
//						
//							s = s + "&nbsp;&nbsp;&nbsp;<a title='Requested' onclick='forwardQuestion(\""
//									+ m.getAuditAutoGenId() + "\")' ><button class='btn btn-info'>Forword Question</button> </a>";
//						}

					else {
						s = "N/A";
					}
					m.setStatus("Not Observed");
				} else if (m.getAuditStatus() == "2") {
					m.setStatus("Open");
					if (roles.contains("rol024") && m.getReqStatus() == "6") {
						s = s + "<a href='/audit/view-statutary-audit-master-observation?id=" + m.getAuditAutoGenId()
								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
								+ "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
					} else if (roles.contains("rol022") || roles.contains("rol023") && m.getReqStatus() == "5") {
						s = s + "<a Title='View' href='/audit/view-statutary-audit-master-observation?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'> <i class='fa fa-eye faIcon' aria-hidden=\\\"true\\\"></i></a>";

					} else if (roles.contains("rol021") && m.getReqStatus() == "3") {

						s = s + "<a Title='Forward Observation' href='/audit/view-statutary-audit-master-forwardObs?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
					}

					else if (roles.contains("rol021") && m.getCoStatus() == "0" && m.getReqStatus() != "4") {
						if (m.getForwardStatus() == "1") {
							s = s + "<a Title='Forwarded' href='/audit/view-statutary-audit-master-forwardObs?id="
									+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
									+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
									+ m.getApproveStatus()
									+ "'> <i class='fa fa-send faIcon' aria-hidden='true'></i></a>";
						} else {
							s = s + "<a Title='Forward Reply g ' href='/audit/view-statutary-audit-master-forwardObs?id="
									+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
									+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
									+ m.getApproveStatus()
									+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";

						}

					}
					/*
					 * else if (roles.contains("rol021") && m.getReqStatus() ==5 &&
					 * m.getCoStatus()==1) {
					 * 
					 * s = s +
					 * "<a Title='Forward Reply' href='/audit/view-statutary-audit-master-forwardObs?id="
					 * + m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId=" +
					 * new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus=" +
					 * m.getApproveStatus() +
					 * "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>"; }
					 */
					else if (roles.contains("rol021") && m.getCoStatus() == "3" && m.getReqStatus() == "5") {

						s = s + "<a Title='Forward Reply 1' href='/audit/view-statutary-audit-master-forwardObs?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
					} else if (roles.contains("rol021") && m.getCoStatus() == "1" && m.getReqStatus() == "6") {

						s = s + "<a Title='Forward Reply 2' href='/audit/view-statutary-audit-master-forwardObs?id="
								+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
								+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
								+ m.getApproveStatus()
								+ "'> <i class='fa fa-forward faIcon' aria-hidden='true'></i></a>";
					}

					else if (roles.contains("rol022") || roles.contains("rol023") && m.getReqStatus() == "4") {
						s = s + "<a href='/audit/view-statutary-audit-master-observation?id=" + m.getAuditAutoGenId()
								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
								+ "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";

					} else if (roles.contains("rol022") || roles.contains("rol023") && m.getReqStatus() == "3") {
						s = s + "N/A";
					} /*
						 * else if (m.getApproveStatus() > "0" && m.getApproveStatus() < "5") { s = s +
						 * "<a href='/audit/approve-statutary-Audit?id=" + m.getAuditAutoGenId() +
						 * "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) +
						 * "&status=" + m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus() +
						 * "'> <i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";
						 * 
						 * }
						 */else if (roles.contains("rol031")) {
						s = s + "<a href='/audit/view-statutary-audit-master-giveReply?id=" + m.getAuditAutoGenId()
								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
								+ "'><i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";

					}

					else {
						s = s + "<a href='/audit/view-statutary-audit-master-observation?id=" + m.getAuditAutoGenId()
								+ "&deptId=" + new String(deptId) + "&sectionId=" + new String(sectionId) + "&status="
								+ m.getAuditStatus() + "&approveStatus=" + m.getApproveStatus()
								+ "'><i class='fa fa-eye faIcon' aria-hidden='true'></i></a>";

					}

				} else if (m.getAuditStatus() == "3") {
					m.setStatus("Closed");
					s = s + "<a Title='View Details' href='/audit/view-statutary-audit-master-observation?id="
							+ m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId="
							+ new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus="
							+ m.getApproveStatus()
							+ "'> <i class='fa fa-list-alt faIcon' aria-hidden='true'></i>	</a>&nbsp;&nbsp;";
//					if(roles.contains("rol024")) {
//						s = s + "<a Title='Submit Report' href='javascript:void' onclick='submitReport(\"" + m.getAuditAutoGenId() + "\")'>\r\n" + 
//								"<i class=\"fa fa-send-o faIcon\"></i></a>";
//					}
					s = s + "<a Title='Report' href='javascript:void' onclick='reportCreate(\"" + m.getAuditAutoGenId()
							+ "\")'><i class='fa fa-file faIcon' aria-hidden='true'></i></a>";
				}
				if (roles.contains("rol021")) {
//					if (m.getAuditStatus() == 0) {
//						m.setStatus("N/A");
//					} else if (m.getAuditStatus() == 1) {
//						m.setStatus("Not Observed");
//					} else if (m.getAuditStatus() == 2) {
//						m.setStatus("Open");
//					} else if (m.getAuditStatus() == 3) {
//						m.setStatus("Close");
//					}

				}
//				if (roles.contains("rol022") || roles.contains("rol023")) {
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

				/*
				 * if(m.getAuditStatus() == 2 && roles.contains("rol021") ) {
				 * m.setStatus("Open"); s = s + "<a href='/audit/observation-forTransAudit?id="
				 * + m.getAuditAutoGenId() + "&deptId=" + new String(deptId) + "&sectionId=" +
				 * new String(sectionId) + "&status=" + m.getAuditStatus() + "&approveStatus=" +
				 * m.getApproveStatus() +
				 * "'> <button class=\"btn btn-warning\"> View </button></a>"; }
				 */
				model.addAttribute("reqSectionId", m.getSectionReqStatus());
				m.setAction(s);
			}
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(auditMasterModelList);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewAuditMasterThroughAjax Theme ends");

		return response;
	}
	/*
	 * get Mapping for view document
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-statutary-audit-master-getDocument", method = { RequestMethod.GET })
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
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "view-statutary-audit-master-sendRequest" }, produces
	 * = "application/json") public @ResponseBody JsonResponse<Object>
	 * sendRequestRequisition(Model model, @RequestBody RequisitionViewModel obj,
	 * BindingResult result, HttpSession session) throws IOException {
	 * logger.info("Method : sendRequestRequisition starts"); JsonResponse<Object>
	 * resp = new JsonResponse<Object>(); String userId = ""; try { userId =
	 * (String) session.getAttribute("USER_ID"); } catch (Exception e) { }
	 * 
	 * try { MultipartFile inputFile = (MultipartFile)
	 * session.getAttribute("imageFile"); System.out.println("inputFile" +
	 * inputFile); String contentName = ""; if (inputFile != null) { long nowTime =
	 * new Date().getTime();
	 * 
	 * byte[] bytes = inputFile.getBytes(); String[] fileType =
	 * inputFile.getContentType().split("/"); System.out.println("CType: " +
	 * fileType[1]);
	 * 
	 * if (fileType[1].contentEquals(
	 * "vnd.openxmlformats-officedocument.wordprocessingml.document")) { contentName
	 * = nowTime + ".docx"; } else if (fileType[1].contentEquals(
	 * "vnd.openxmlformats-officedocument.presentationml.presentation")) {
	 * contentName = nowTime + ".docx"; } else if
	 * (fileType[1].contentEquals("msword")) { contentName = nowTime + ".doc"; }
	 * else if (fileType[1].contentEquals(
	 * "vnd.openxmlformats-officedocument.spreadsheetml.sheet")) { contentName =
	 * nowTime + ".xls"; } else if (fileType[1].contentEquals(".xlsx")) {
	 * contentName = nowTime + ".xls"; } else if
	 * (fileType[1].contentEquals("vnd.ms-excel")) { contentName = nowTime + ".xls";
	 * } else if (fileType[1].contentEquals("xls")) { contentName = nowTime +
	 * ".xls"; } else { contentName = nowTime + "." + fileType[1]; }
	 * obj.setCreatedby(userId); obj.setDocument(contentName); resp =
	 * restClient.postForObject(env.getAuditUrl() + "sendStatuRequestRequisition",
	 * obj, JsonResponse.class);
	 * 
	 * if (resp.getCode().contains("Data Saved Successfully")) { Path path =
	 * Paths.get(env.getFileUploadAudit() + contentName); System.out.println("Path"
	 * + path); if (fileType[1].contentEquals("pdf")) { Files.write(path, bytes); }
	 * else if (fileType[1].contentEquals(
	 * "vnd.openxmlformats-officedocument.wordprocessingml.document") ||
	 * fileType[1].contentEquals("msword")) { Files.write(path, bytes); } else {
	 * 
	 * Files.write(path, bytes);
	 * 
	 * ByteArrayInputStream in = new ByteArrayInputStream(bytes); Integer height =
	 * 50; Integer width = 50;
	 * 
	 * try { BufferedImage img = ImageIO.read(in); if (height == 0) { height =
	 * (width * img.getHeight()) / img.getWidth(); } if (width == 0) { width =
	 * (height * img.getWidth()) / img.getHeight(); } if (width != null && height !=
	 * null) { Image scaledImage = img.getScaledInstance(width, height,
	 * Image.SCALE_SMOOTH); BufferedImage imageBuff = new BufferedImage(width,
	 * height, BufferedImage.TYPE_INT_ARGB);
	 * imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);
	 * 
	 * ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	 * 
	 * ImageIO.write(imageBuff, "png", buffer);
	 * 
	 * // ByteArrayOutputStream out = new ByteArrayOutputStream(); byte[] thumb =
	 * buffer.toByteArray(); Path pathThumb = Paths.get(env.getFileUploadAudit() +
	 * "thumb/" + contentName); Files.write(pathThumb, thumb); } } catch (Exception
	 * e) { e.printStackTrace(); }
	 * 
	 * } }
	 * 
	 * } else { obj.setCreatedby(userId); resp =
	 * restClient.postForObject(env.getAuditUrl() + "sendStatuRequestRequisition",
	 * obj, JsonResponse.class);
	 * 
	 * } } catch (RestClientException e) { e.printStackTrace(); }
	 * 
	 * if (resp.getMessage() != null && resp.getMessage() != "") {
	 * resp.setCode(resp.getMessage()); resp.setMessage("Unsuccess"); } else {
	 * resp.setMessage("success"); }
	 * logger.info("Method : sendRequestRequisition ends");
	 * System.out.println(resp); return resp; }
	 */
	@SuppressWarnings({ "unchecked", })
	// request requisition
	@RequestMapping(value = "/view-statutary-audit-master-sendRequest", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> sendRequestRequisition(
			@RequestBody List<RequisitionViewModel> requisitionViewModel, Model model, BindingResult result,
			HttpSession session) {

		logger.info("Method : sendRequestRequisition function starts");

		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("send requisitionViewModel" + requisitionViewModel);
		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			if (a.getEditDocName() != null && a.getEditDocName() != "") {
				a.setDocument(a.getEditDocName());
			} else {
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "sendStatuRequestRequisition", requisitionViewModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message == "") {
			res.setMessage("Success");

			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			// System.out.println("auditid "+auditid);
			String auditid = "";
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
				auditid = (m.getDocName());
			}
			// System.out.println("auditid "+auditid);
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			// System.out.println("attachFiles"+attachFiles[0].toString());
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
				}
				// System.out.println("Email sent.");
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

			res.setBody(auditid);
		}
		logger.info("Method : sendRequestRequisition function Ends");
		System.out.println("res " + res);
		return res;
	}

	/**
	 * final observation
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-internal-audit-final-Observation", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> finalObs(@RequestBody List<AuditObservationModel> obs,
			HttpSession session) {

		logger.info("Method : final Observation starts");
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> res = new JsonResponse<Object>();
		for (AuditObservationModel a : obs) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {
			res = restClient.postForObject(env.getAuditUrl() + "sendFinalObservation", obs, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");

			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obs.get(0).getSubject();

			String message1 = obs.get(0).getFinalObservation();

			String[] attachFiles = null;
			if (obs.get(0).getDocument() != null && obs.get(0).getDocument() != "") {
				attachFiles = new String[obs.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (obs.get(i).getDocument() != null && obs.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + obs.get(i).getDocument();
					}
				}
			}
			// System.out.println("attachFiles"+attachFiles[0].toString());
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
				}
				// System.out.println("Email sent.");
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}
		}
		// System.out.println(res);
		logger.info("Method : final Observation end");
		return res;
	}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-statutary-audit-master-uploadFile")
	public @ResponseBody JsonResponse<Object> viewUploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("imageFile", inputFile);
			// System.out.println("inputFile" + inputFile);
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
	@RequestMapping(value = "/view-statutary-audit-master-getReqDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getReqAudDocument(@RequestParam("id") String id,
			Model model, HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getReqAudDocument function starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			userId = (String) session.getAttribute("USER_ID");
			res = restClient.getForObject(env.getAuditUrl() + "getStatuReqDocument?id=" + id + "&userId=" + userId,
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
		// System.out.println(roles);
		if (roles.contains("rol023")) {

			res.setCode("rol023");
		}
		if (roles.contains("rol021")) {

			res.setCode("rol021");
		}
		// System.out.println(res);
		logger.info("Method : getReqAudDocument function Ends");
		return res;
	}

	/*
	 * send response to requisition
	 *
	 *
	 */
	@SuppressWarnings({ "unchecked", })
	@PostMapping(value = { "view-statutary-audit-master-sendResponse" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendResponse(Model model,
			@RequestBody List<RequisitionViewModel> responseData, BindingResult result, HttpSession session)
			throws IOException {
		logger.info("Method : sendResponse starts");

		System.out.println(" bikash res " + responseData);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (RequisitionViewModel a : responseData) {
				a.setCreatedby(userId);
				if(a.getDocument() != null && a.getDocument() != "") {
					a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
				}
			}

			resp = restClient.postForObject(env.getAuditUrl() + "sendStatuResponseRequisition", responseData,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");

			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(resp.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(resp.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ responseData.get(0).getSubject();

			String message1 = responseData.get(0).getComment();

			String[] attachFiles = null;
			if (responseData.get(0).getDocument() != null && responseData.get(0).getDocument() != "") {
				attachFiles = new String[responseData.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (responseData.get(i).getDocument() != null && responseData.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + responseData.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
				}
				// System.out.println("Email sent.");
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : sendResponse ends");
		// System.out.println(resp);
		return resp;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardReq", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> fowardedReq(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, HttpSession session, BindingResult result) {
		logger.info("Method : fowardedReq function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));

		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "fowardedStatuReq", requisitionViewModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message.equals("success")) {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}

		else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : fowardedReq function Ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardPom", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> forwardPom(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, HttpSession session, BindingResult result) {
		logger.info("Method : forwardPom function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));

		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardPom", requisitionViewModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message.equals("success")) {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}
		}

		else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : forwardPom function Ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardIRS", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> forwardIRS(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, HttpSession session, BindingResult result) {
		logger.info("Method : forwardIRS function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));

		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardIRS", requisitionViewModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message.equals("success")) {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}
		}

		else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : forwardIRS function Ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardPara", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> forwardPara(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, HttpSession session, BindingResult result) {
		logger.info("Method : forwardPara function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));

		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardPara", requisitionViewModel, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message.equals("success")) {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}
		}

		else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : forwardPara function Ends");
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-statutary-audit-master-question")
	public String addAuditQuestion(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status) {

		logger.info("Method : addAuditQuestion starts");

		model.addAttribute("initiatedId", id);
		model.addAttribute("deptId", deptId);
		model.addAttribute("sectionId", sectionId);

		logger.info("Method : addAuditQuestion ends");

		return "audit/add-audit-statutary-question";
	}

	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "/view-statutary-audit-master-question-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addQuestion(
			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addQuestion function starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : auditObservationModel) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "restAddStatuQuestion", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}

			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ auditObservationModel.get(0).getSubject();
			String message1 = auditObservationModel.get(0).getQuestion();

			String[] attachFiles = null;
			if (auditObservationModel.get(0).getDocument() != null
					&& auditObservationModel.get(0).getDocument() != "") {
				attachFiles = new String[auditObservationModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (auditObservationModel.get(i).getDocument() != null
							&& auditObservationModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + auditObservationModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : addQuestion function Ends");
		return res;
	}
	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "/view-statutary-audit-master-irs-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> InspectionReport(
			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : InspectionReport function starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : auditObservationModel) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "restAddStatuIrs", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}

			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ auditObservationModel.get(0).getSubject();
			String message1 = auditObservationModel.get(0).getQuestion();

			String[] attachFiles = null;
			if (auditObservationModel.get(0).getDocument() != null
					&& auditObservationModel.get(0).getDocument() != "") {
				attachFiles = new String[auditObservationModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (auditObservationModel.get(i).getDocument() != null
							&& auditObservationModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + auditObservationModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : InspectionReport function Ends");
		return res;
	}
	/*
	 * post mapping for add employee Education
	 */

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "/view-statutary-audit-master-para-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> DraftPara(@RequestBody List<AuditObservationModel> auditObservationModel,
			Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : DraftPara function starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : auditObservationModel) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "restAddStatuPara", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}

			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ auditObservationModel.get(0).getSubject();
			String message1 = auditObservationModel.get(0).getQuestion();

			String[] attachFiles = null;
			if (auditObservationModel.get(0).getDocument() != null
					&& auditObservationModel.get(0).getDocument() != "") {
				attachFiles = new String[auditObservationModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (auditObservationModel.get(i).getDocument() != null
							&& auditObservationModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + auditObservationModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : DraftPara function Ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-forwardQus", method = { RequestMethod.GET })
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
	@RequestMapping(value = "/view-statutary-audit-master-getQuestion", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getQuestion(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getQuestion function starts");

		try {
			// System.out.println(id);
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
		// System.out.println(res);
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-statutary-audit-master-observation")
	public String addTransAuditObservation(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status,
			@RequestParam Byte approveStatus) {
		logger.info("Method : addTransAuditObservation starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);

		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));

		try {
			List<String> auditQuestionStatus = new ArrayList<String>();

			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getStatuQuestionList?id=" + id
					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
					AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);

			DropDownModel[] obs1 = restClient.getForObject(env.getAuditUrl() + "getConcernAuditees?id=" + id,
					DropDownModel[].class);
			List<DropDownModel> concernList = Arrays.asList(obs1);
			for (AuditObservationModel m : questionList) {
				if (m.getAuditeeStatus() == "2") {
					int val = 2;
					model.addAttribute("replied", val);
				} else {
					model.addAttribute("replied", "");
				}
				auditQuestionStatus.add(m.getAuditStatus());

			}
			model.addAttribute("concernList", concernList);
			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
			// System.out.println("questionList" + questionList);
			model.addAttribute("questionList", questionList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addTransAuditObservation ends");
		return "audit/add-audit-observationForStatutaryAudit";
	}

	// @SuppressWarnings({ "unchecked", "restriction" })
	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-save", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addObservation(@RequestBody List<AuditObservationModel> observation,
			Model model, HttpSession session) {
		logger.info("Method : addObservation function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (AuditObservationModel a : observation) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}

		try {

			res = restClient.postForObject(env.getAuditUrl() + "addStatuObservation", observation, JsonResponse.class);
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

	// @SuppressWarnings({ "unchecked", "restriction" })
	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-saveDev", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addDevObservation(@RequestBody List<AuditObservationModel> observation,
			Model model, HttpSession session) {
		logger.info("Method : addDevObservation function starts");
//System.out.println("observation"+observation);
		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : observation) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}

		try {

			res = restClient.postForObject(env.getAuditUrl() + "addStatuDevObservation", observation,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addDevObservation function Ends");
		return res;
	}

	public String saveAllImage(String document, byte[] imageBytes) {
		logger.info("Method : saveAllImage starts");

		String imageName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				imageName = str4 + ".png";
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
System.out.println(width+" "+height+" "+Image.SCALE_SMOOTH);
					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
System.out.println(scaledImage);
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
		System.out.println("imageName123 " + imageName);
		return imageName;
	}
	/*
	 * for save all pdf in folder and return name
	 */

	public String saveAllPdf(String document, byte[] imageBytes) {
		logger.info("Method : saveAllPdf starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();

				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				pdfName = str4 + ".pdf";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			// System.out.println("path " + path);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllPdf ends");
		return pdfName;
	}

	public String saveAllDocx(String document, byte[] imageBytes) {
		logger.info("Method : saveAllDocx starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				pdfName = str4 + ".docx";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			// System.out.println("path" + path);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDocx ends");
		return pdfName;
	}

	public String saveAllDoc(String document, byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				pdfName = str4 + ".doc";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			// System.out.println("path" + path);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}

	public String saveAllXlsx(String document, byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				pdfName = str4 + ".xlsx";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			// System.out.println("path" + path);
			if (imageBytes != null) {
				Files.write(path, imageBytes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : saveAllDoc ends");
		return pdfName;
	}

	public String saveAllXls(String document, byte[] imageBytes) {
		logger.info("Method : saveAllDoc starts");

		String pdfName = null;

		try {
			if (imageBytes != null) {
				long nowTime = new Date().getTime();
				String str = Long.toString(nowTime);
				String str1 = str.concat("-");
				String str4 = str1.concat(document);
				System.out.println("Bikash " + str4);
				pdfName = str4 + ".xls";
			}

			Path path = Paths.get(env.getFileUploadAudit() + pdfName);
			// System.out.println("path" + path);
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
	@PostMapping("/view-statutary-audit-master-comment-save")
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
			// System.out.println("inputFile" + inputFile);
			String contentName = "";
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				// System.out.println("CType: " + fileType[1]);

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

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-statutary-audit-master-forwardObs")
	public String addAuditObservationforTransAudit(Model model, HttpSession session, @RequestParam Integer id,
			@RequestParam String deptId, @RequestParam String sectionId, @RequestParam Byte status,
			@RequestParam Byte approveStatus) {
		logger.info("Method : addAuditObservationforTransAudit starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);

		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));

		try {
			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getStatuQuestionList?id=" + id
					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
					AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);
//			for (AuditObservationModel m : questionList) {
//				auditQuestionStatus.add(m.getAuditStatus());
//			}
			model.addAttribute("questionList", questionList);
			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
		} catch (Exception e) {
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

		logger.info("Method : addAuditObservationforTransAudit ends");
		return "audit/add-audit-observationForStatutaryAudit";
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardObservationByCo", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> forwardObs(@RequestBody List<RequisitionViewModel> obj, Model model,
			HttpSession session) {
		logger.info("Method : forwardObs function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			for (RequisitionViewModel a : obj) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}
			res = restClient.postForObject(env.getAuditUrl() + "forwardStatuObs", obj, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();

			String message1 = obj.get(0).getComment();

			String[] attachFiles = null;
			if (obj.get(0).getDocument() != null && obj.get(0).getDocument() != "") {
				attachFiles = new String[obj.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (obj.get(i).getDocument() != null && obj.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + obj.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		} else {
			res.setMessage(res.getCode());

		}
		logger.info("Method : forwardObs function Ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forward-to-approve", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> fowardedToApproveforTransAudit(
			@RequestBody List<RequisitionViewModel> observation, Model model, HttpSession session) {
		logger.info("Method : fowardedToApproveforTransAudit function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (RequisitionViewModel a : observation) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
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
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {

					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ observation.get(0).getSubject();

			String message1 = observation.get(0).getComment();

			String[] attachFiles = null;
			if (observation.get(0).getDocument() != null && observation.get(0).getDocument() != "") {
				attachFiles = new String[observation.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (observation.get(i).getDocument() != null && observation.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + observation.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : fowardedToApproveforTransAudit function Ends");
		return res;
	}

	@GetMapping("/approve-statutary-Audit")
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
			// System.out.println("questionList" + questionList);
			model.addAttribute("questionList", questionList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			AuditCommentsModel[] cmnt = restClient.getForObject(env.getAuditUrl() + "getStatuCommentsList?id=" + id,
					AuditCommentsModel[].class);
			List<AuditCommentsModel> commentsList = Arrays.asList(cmnt);
			// System.out.println("commentsList" + commentsList);
			model.addAttribute("commentsList", commentsList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : approveStatutaryAudit starts");
		return "audit/approve-statutary-Audit";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "approve-statutary-Audit-forward-to-approve", method = { RequestMethod.POST })
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
	@GetMapping(value = { "/view-statutary-audit-master-view-comment" })
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

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardReply", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> forwardReply(@RequestBody List<RequisitionViewModel> obj, Model model,
			HttpSession session) {
		logger.info("Method : forwardReply function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			for (RequisitionViewModel a : obj) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

			res = restClient.postForObject(env.getAuditUrl() + "forwardStatuReply", obj, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {

					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();

			String message1 = obj.get(0).getComment();

			String[] attachFiles = null;
			if (obj.get(0).getDocument() != null && obj.get(0).getDocument() != "") {
				attachFiles = new String[obj.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (obj.get(i).getDocument() != null && obj.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + obj.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		} else {
			res.setMessage(res.getCode());

		}
		logger.info("Method : forwardReply function Ends");
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("/view-statutary-audit-master-giveReply")
	public String giveReply(Model model, HttpSession session, @RequestParam Integer id, @RequestParam String deptId,
			@RequestParam String sectionId, @RequestParam Byte status, @RequestParam Byte approveStatus) {
		logger.info("Method : giveReply starts");
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("Status", status);
		model.addAttribute("ApproveStatus", approveStatus);
		model.addAttribute("AuditId", id);
		model.addAttribute("DeptId", deptId);
		model.addAttribute("SectionId", sectionId);

		byte[] encodeByte = Base64.getDecoder().decode(deptId.getBytes());
		String deptId1 = (new String(encodeByte));
		byte[] encodeByte1 = Base64.getDecoder().decode(sectionId.getBytes());
		String sectionId1 = (new String(encodeByte1));

		try {
			List<Byte> auditQuestionStatus = new ArrayList<Byte>();
			AuditObservationModel[] obs = restClient.getForObject(env.getAuditUrl() + "getStatuQuestionList?id=" + id
					+ "&deptId=" + deptId1 + "&sectionId=" + sectionId1 + "&userId=" + userId,
					AuditObservationModel[].class);
			List<AuditObservationModel> questionList = Arrays.asList(obs);

//			for (AuditObservationModel m : questionList) {
//				auditQuestionStatus.addAll(m.getAuditStatus());
//			}

			model.addAttribute("auditQuestionStatus", auditQuestionStatus);
			// System.out.println(questionList.get(0).getiRStatus());
			model.addAttribute("questionList", questionList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : giveReply ends");
		return "audit/StatutaryObservationForConcernAuditee";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-forwardToCo", method = { RequestMethod.GET })
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

			res = restClient.getForObject(env.getAuditUrl() + "forwardToCo?id=" + id + "&createdBy=" + userId,
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
	@PostMapping("/view-statutary-audit-master-auditor-comment-save")
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
			// System.out.println(session.getAttribute("imageAuditorFile"));
		}
		logger.info("Method : auditorCommentSave ends");
		return resp;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-co-ordinatorforward", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> coordinatorforward(@RequestBody List<RequisitionViewModel> obj,
			Model model, HttpSession session) {
		logger.info("Method : coordinatorforward function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : obj) {

			a.setCreatedby(userId);

			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}

		try {

			res = restClient.postForObject(env.getAuditUrl() + "coordinatorforward", obj, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() == null || res.getMessage() == "") {
			res.setMessage("success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {

					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();
			String message1 = obj.get(0).getComment();
			String[] attachFiles = null;
			if (obj.get(0).getDocument() != null && obj.get(0).getDocument() != "") {
				attachFiles = new String[obj.size()];

				for (int i = 0; i < attachFiles.length; i++) {
					if (obj.get(i).getDocument() != null && obj.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + obj.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}

		else {
			res.setCode(res.getMessage());
		}

		logger.info("Method : coordinatorforward function Ends");
		return res;
	}

	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings({ "unchecked", })
	@PostMapping(value = { "view-statutary-audit-master-forwardToCrn" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> forwardToCrn(Model model,
			@RequestBody List<RequisitionViewModel> requisitionViewModel, BindingResult result, HttpSession session) {
		logger.info("Method : forwardToCrn starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			for (RequisitionViewModel a : requisitionViewModel) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}
			// System.out.println(requisitionViewModel);
			res = restClient.postForObject(env.getAuditUrl() + "forwardToCrn", requisitionViewModel,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {

					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();
			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];

				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

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
	@SuppressWarnings({ "unchecked", })
	@PostMapping(value = { "view-statutary-audit-master-reqForwardBysec" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> reqForwardBysec(Model model,
			@RequestBody List<RequisitionViewModel> requisitionViewModel, HttpSession session) throws IOException {
		logger.info("Method : sendResponse starts");
		// System.out.println("##$$$$" + requisitionViewModel);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";

		userId = (String) session.getAttribute("USER_ID");

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {
			System.out.println("reqForwardBysec " + requisitionViewModel);
			resp = restClient.postForObject(env.getAuditUrl() + "reqForwardBysec", requisitionViewModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(resp.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(resp.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();
			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}

			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : sendResponse ends");
		// System.out.println(resp);
		return resp;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-rejectReq", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> rejectReq(Model model,
			@RequestBody List<RequisitionViewModel> requisitionViewModel, HttpSession session) {
		logger.info("Method : rejectReq function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("send modele file" + requisitionViewModel);
		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		System.out.println("send modele file2" + requisitionViewModel);
		res = restClient.postForObject(env.getAuditUrl() + "rejectReq", requisitionViewModel, JsonResponse.class);

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}

		logger.info("Method : rejectReq function Ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "view-statutary-audit-master-saveComment" }, produces
	 * = "application/json") public @ResponseBody JsonResponse<Object>
	 * saveComment(Model model, @RequestBody RequisitionViewModel obj, BindingResult
	 * result) { logger.info("Method : saveComment starts"); JsonResponse<Object>
	 * res = new JsonResponse<Object>();
	 * 
	 * try { res = restClient.postForObject(env.getAuditUrl() +
	 * "saveRequisitionComment", obj, JsonResponse.class);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } if (res.getMessage() != null)
	 * { res.setCode(res.getMessage()); res.setMessage("Unsuccess"); } else {
	 * res.setMessage("success"); } logger.info("Method : saveComment ends"); return
	 * res; }
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveComment" })
	public @ResponseBody JsonResponse<Object> saveComment(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		// System.out.println(obj);
		logger.info("Method : saveComment starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveRequisitionComment", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(res);
		// System.out.println("abcde"+res.getMessage());
		if (res.getMessage() != null && res.getMessage() != "") {
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
	@GetMapping(value = { "view-statutary-audit-master-getComment" })
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
		/// System.out.println(res);
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-getReqResponse" })
	public @ResponseBody JsonResponse<DropDownModel> getReqResponse(Model model, @RequestParam("id") String id,
			HttpSession session) {
		logger.info("Method : getReqResponse starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getReqResponse?id=" + id, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getReqResponse ends");
		/// System.out.println(res);
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveResponse" })
	public @ResponseBody JsonResponse<Object> saveResponse(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		logger.info("Method : saveResponse starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveResponse", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveResponse ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveNotes" })
	public @ResponseBody JsonResponse<Object> saveNotes(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		logger.info("Method : saveNotes starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveNotes", obj, JsonResponse.class);

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

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveSecComment" })
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
	@GetMapping(value = { "view-statutary-audit-master-getSecComment" })
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
	@PostMapping(value = { "view-statutary-audit-master-saveObsComment" })
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
	@GetMapping(value = { "view-statutary-audit-master-getObsComment" })
	public @ResponseBody JsonResponse<DropDownModel> getObsComment(Model model, HttpSession session,
			@RequestParam("queId") String queId) {
		logger.info("Method : getObsComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getObsComment?queId=" + queId, JsonResponse.class);

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
		// System.out.println(res);
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveConAudiComment" })
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
	@GetMapping(value = { "view-statutary-audit-master-getConAudiComment" })
	public @ResponseBody JsonResponse<DropDownModel> getConAudiComment(Model model, HttpSession session,
			@RequestParam("queId") String queId) {
		logger.info("Method : getConAudiComment starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "getConAudiComment?queId=" + queId, JsonResponse.class);

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

	@SuppressWarnings({ "unchecked", })
	@PostMapping("/view-statutary-audit-master-comment-consernsave")
	public @ResponseBody JsonResponse<Object> consernsave(Model model,
			@RequestBody List<RequisitionViewModel> requisitionViewModel, HttpSession session) throws IOException {

		logger.info("Method : consernsave starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			for (RequisitionViewModel a : requisitionViewModel) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

			resp = restClient.postForObject(env.getAuditUrl() + "auditeeStatuCommentconsernsave", requisitionViewModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(resp.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(resp.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}

		else {
			resp.setCode(resp.getMessage());
		}
		logger.info("Method : auditeeStatuCommentSave ends");
		return resp;
	}

	/**
	 * Reject Response
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-rejectResponse" }, produces = "application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectResponse(Model model, RequisitionViewModel obj,
			HttpSession session) {
		logger.info("Method : rejectResponse starts");

		// System.out.println("obj" + obj);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
			res = restClient.postForObject(env.getAuditUrl() + "rejectResponse", obj, JsonResponse.class);

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

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "view-statutary-audit-master-forwardToPerson", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> forwardToPerson(
			@RequestBody List<RequisitionViewModel> requisitionViewModel, Model model, HttpSession session) {
		logger.info("Method : forwardToCo function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			for (RequisitionViewModel a : requisitionViewModel) {
				a.setCreatedby(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}
			System.out.println("requisitionViewModel" + requisitionViewModel);
			res = restClient.postForObject(env.getAuditUrl() + "forwardToPerson", requisitionViewModel,
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
	@PostMapping(value = { "view-statutary-audit-master-rejectObsBySec" })
	public @ResponseBody JsonResponse<DropDownModel> rejectObsBySec(Model model, @RequestBody AuditComplianceModel obj,
			HttpSession session) {
		logger.info("Method : rejectObsBySec starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedBy(userId);
			res = restClient.postForObject(env.getAuditUrl() + "rejectObsBySec", obj, JsonResponse.class);

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
	@PostMapping(value = { "view-statutary-audit-master-rejectObsByCoordinator" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> rejectObsByCoordinator(@RequestBody RequisitionViewModel obj, Model model,
			HttpSession session) {
		logger.info("Method : rejectObsByCoordinator starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			obj.setCreatedby(userId);
			res = restClient.postForObject(env.getAuditUrl() + "rejectObsByCoordinator", obj, JsonResponse.class);

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

	@SuppressWarnings({ "unchecked", })
	// My view Request
	@RequestMapping(value = "view-statutary-audit-master-save-viewRequest", method = {
			RequestMethod.POST }, produces = "application/json")
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

		for (RequisitionViewModel a : request) {

			a.setCreatedby(userId);

			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
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
	@RequestMapping(value = "/view-statutary-audit-master-editReqDocument", method = { RequestMethod.GET })
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
		if (roles.contains("rol023")) {
			res.setCode("rol023");
		}

		logger.info("Method : editReqAudDocument function Ends");
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-statutary-audit-master-forwardToAuditor")
	public String viewStatutaryAuditMastersdf(Model model, @RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewStatutaryAuditMaster starts");

		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);
			// System.out.println(personList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("auditor " + id);
		model.addAttribute("audit", id);
		logger.info("Method : viewStatutaryAuditMaster ends");

		return "audit/forwardToAuditor";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-forwardToAuditorDirect", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<Object> forwardToAuditoDirect(@RequestParam("audit") String id, Model model,
			HttpSession session) {
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
					env.getAuditUrl() + "forwardToAuditorDirect?id=" + id + "&createdBy=" + userId, JsonResponse.class);
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
	@PostMapping(value = { "view-statutary-audit-master-rejectObs" }, produces = "application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectObs(Model model, @RequestBody AuditComplianceModel obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : rejectObs starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
			res = restClient.postForObject(env.getAuditUrl() + "rejectObs", obj, JsonResponse.class);
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
	@SuppressWarnings({ "unchecked", })
	@PostMapping(value = { "view-statutary-audit-master-rejectResponse1" }, produces = "application/json")
	public @ResponseBody JsonResponse<DropDownModel> rejectResponse1(Model model,
			@RequestBody List<RequisitionViewModel> request, BindingResult result, HttpSession session) {
		logger.info("Method : rejectResponse1 starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : request) {

			a.setCreatedby(userId);

			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		res = restClient.postForObject(env.getAuditUrl() + "rejectResponse", request, JsonResponse.class);

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");

			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = request.get(0).getSubject();

			String message1 = request.get(0).getComment();

			String[] attachFiles = null;
			if (request.get(0).getDocument() != null && request.get(0).getDocument() != "") {
				attachFiles = new String[request.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (request.get(i).getDocument() != null && request.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + request.get(i).getDocument();
					}
				}
			}
			// System.out.println("attachFiles"+attachFiles[0].toString());
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : rejectResponse1 ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-getPersonList", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<List<PersonModel>> getPersonList(@RequestParam("id") String id, Model model,
			HttpSession session) {
		logger.info("Method : getPersonList function starts");

		JsonResponse<List<PersonModel>> res = new JsonResponse<List<PersonModel>>();

		try {

			res = restClient.getForObject(env.getAuditUrl() + "personList?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getBody() != null) {
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
	@GetMapping("view-statutary-audit-master-viewCompliance")
	public String viewCompliance(Model model, @RequestParam("id") String id, HttpSession session) {

		logger.info("Method : viewStatutaryAuditMaster starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		AuditComplianceModel[] requisitionViewModel = restClient.getForObject(
				env.getAuditUrl() + "getStatuViewCommentList?id=" + id + "&userId=" + userId,
				AuditComplianceModel[].class);

		List<AuditComplianceModel> complianceList = Arrays.asList(requisitionViewModel);
		// System.out.println("complianceList"+complianceList);
		for (AuditComplianceModel dtl : complianceList) {

			DropDownModel[] dropDownModel1 = restClient.getForObject(
					env.getAuditUrl() + "getComplianceDocList?id=" + dtl.getAuditId(), DropDownModel[].class);
			List<DropDownModel> docList = Arrays.asList(dropDownModel1);

			for (DropDownModel doc : docList) {

				AuditInitiateController auditInintiateConntroller = new AuditInitiateController();
				doc.setKey(auditInintiateConntroller.viewUploadedImage(doc.getKey(), doc.getName()));

			}
			// System.out.println("docListDetails"+docList);
			dtl.setDocuList(docList);
			if (dtl.getCreatedOn() != null) {
				String[] arr = dtl.getCreatedOn().split(" ");
				dtl.setDate(arr[0]);
				dtl.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
		}
		model.addAttribute("complianceList", complianceList);

		model.addAttribute("audit", id);
		logger.info("Method : viewCompliance ends");
		return "audit/viewCompliance";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-co-ordinatorforwardToAuditor", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> ordinatorforwardToAuditor(@RequestBody List<RequisitionViewModel> obj,
			Model model, HttpSession session) {
		logger.info("Method : ordinatorforwardToAuditor function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restClient.postForObject(env.getAuditUrl() + "cordinatorforwardToAuditor", obj, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : ordinatorforwardToAuditor function Ends");
		return res;
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-statutary-audit-master-forwardDetail")
	public String forwardDetail(Model model, @RequestParam("id") String id, HttpSession session) {

		logger.info("Method : forwardDetail starts");

		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getforwardPersonList?id=" + id,
					DropDownModel[].class);
			List<DropDownModel> frowardPersonList = Arrays.asList(dept);

			model.addAttribute("frowardPersonList", frowardPersonList);
			// System.out.println(frowardPersonList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getfrowardPersonCCList?id=" + id,
					DropDownModel[].class);
			List<DropDownModel> frowardPersonCCList = Arrays.asList(dept);

			model.addAttribute("frowardPersonCCList", frowardPersonCCList);
			// System.out.println(frowardPersonCCList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("auditor " + id);
		model.addAttribute("audit", id);
		logger.info("Method : forwardDetail ends");

		return "audit/forwardDetail";
	}

	/*
	 * Get Mapping view audit master
	 */
	@GetMapping("view-statutary-audit-master-forwardComment")
	public String forwardCommentDetail(Model model, @RequestParam("id") String id, HttpSession session) {

		logger.info("Method : forwardCommentDetail starts");

		// System.out.println("auditor " + id);
		model.addAttribute("audit", id);
		logger.info("Method : forwardCommentDetail ends");

		return "audit/statutaryForwardCommentDetails";
	}
	/*
	 * get Mapping for view document
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-statutary-audit-master-viewDetails", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<AuditDetailModel> viewDetails(@RequestParam("id") String id, Model model,
			HttpSession session) {
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

		// System.out.println(res);
		logger.info("Method : viewDetails function Ends");
		return res;
	}
	// Get MUltiple document

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-statutary-audit-master-getMulDocument", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<RequisitionViewModel> getMulDocument(@RequestParam("id") String id, Model model,
			HttpSession session) {
		JsonResponse<RequisitionViewModel> res = new JsonResponse<RequisitionViewModel>();
		logger.info("Method : getMulDocument function starts");

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
		// System.out.println(roles);
		if (roles.contains("rol023")) {

			res.setCode("rol023");
		}
		if (roles.contains("rol021")) {

			res.setCode("rol021");
		}
		// System.out.println(res);
		logger.info("Method : getMulDocument function Ends");
		return res;
	}
	// Get MUltiple document

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/view-statutary-audit-master-getConcernAuditees", method = { RequestMethod.GET })
	public @ResponseBody JsonResponse<DropDownModel> getConcernAuditees(@RequestParam("audit") String id, Model model,
			HttpSession session) {
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		logger.info("Method : getConcernAuditees function starts");

		try {

			res = restClient.getForObject(env.getAuditUrl() + "getConcernAuditees?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}

		// System.out.println(res);
		logger.info("Method : getConcernAuditees function Ends");
		return res;
	}

	@PostMapping("/view-statutary-audit-master-single-pdf-comment")
	public @ResponseBody JsonResponse<Object> pdfViewOfComment(Model model,
			@RequestBody AuditMeetingModel auditMeetingModel, HttpSession session) throws IOException {

		logger.info("Method : pdfViewOfComment starts");

		// String data = "";
		try {
			session.setAttribute("comment", auditMeetingModel.getComment());
			session.setAttribute("date", auditMeetingModel.getDate());
			session.setAttribute("time", auditMeetingModel.getFromTime());
			session.setAttribute("createdBy", auditMeetingModel.getCreatedBy());
			session.setAttribute("initiate", auditMeetingModel.getInitiate());
			session.setAttribute("personTo", auditMeetingModel.getPersonTo());
			session.setAttribute("personCc", auditMeetingModel.getToTime());

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		resp.setMessage("success");

		logger.info("Method : pdfViewOfComment ends");
		System.out.println(resp);
		return resp;
	}

	@GetMapping("view-statutary-audit-master-single-pdf-comment-view")
	public void generateStatuaryAuditPdfComment(HttpServletResponse response, Model model, HttpSession session) {

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("comment", session.getAttribute("comment"));
		data.put("commentBy", session.getAttribute("createdBy"));
		data.put("date", session.getAttribute("date"));
		data.put("time", session.getAttribute("time"));
		data.put("initiate", session.getAttribute("initiate"));
		data.put("personTo", session.getAttribute("personTo"));
		data.put("personCc", session.getAttribute("personCc"));
		session.removeAttribute("comment");
		session.removeAttribute("commentBy");
		session.removeAttribute("date");
		session.removeAttribute("time");
		session.removeAttribute("initiate");
		session.removeAttribute("personTo");
		session.removeAttribute("personCc");

		System.out.println("bikash data of pdf " + Arrays.asList(data));

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=commentDetails.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("audit/commentViewPdf", data);
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

	@SuppressWarnings({ "unused" })
	@GetMapping("view-statutary-audit-master-observation-pdf-comment")
	public void generateStatuaryAuditPdfObservation(HttpServletResponse response, Model model,
			@RequestParam("observation") String observation) {

		Map<String, Object> data = new HashMap<String, Object>();
		String s = "";

		data.put("comment", observation);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=studentEnrollmentSingle.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("audit/commentViewPdf", data);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "view-statutary-audit-master-forwardRemark", method = {
			RequestMethod.POST }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> forwardRemark(
			@RequestBody List<RequisitionViewModel> requisitionViewModel, Model model, HttpSession session,
			BindingResult result) {
		logger.info("Method : forwardRemark function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (RequisitionViewModel m : requisitionViewModel) {
			m.setCreatedby(userId);
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "forwardRemark", requisitionViewModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : forwardRemark function Ends");
		return res;
	}

	public String uploadPhoto(String document, List<String> file) {
		/* bikash changes */
		if (document != null) {
			String documentname = document.split("\\.")[0];
			if (document != null && document != "") {
				String delimiters = "\\.";
				String[] x = document.split(delimiters);
				if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
					for (String s1 : file) {
						try {

							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String imageName = null;
							if(bytes != null) {
								imageName = saveAllImage(documentname, bytes);
							}
							
							document = imageName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("pdf")) {
					for (String s1 : file) {
						try {

							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllPdf(documentname, bytes);
							document = pdfName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("docx")) {
					for (String s1 : file) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllDocx(documentname, bytes);
							document = pdfName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("doc")) {
					for (String s1 : file) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllDoc(documentname, bytes);
							document = pdfName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("xls")) {
					for (String s1 : file) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllXls(documentname, bytes);
							document = pdfName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (x[1].matches("xlsx")) {
					for (String s1 : file) {
						try {
							byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
							String pdfName = saveAllXlsx(documentname, bytes);
							document = pdfName;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return document;
	}

	@SuppressWarnings({ "unchecked", })
	// request requisition
	@RequestMapping(value = "/view-statutary-audit-master-revertBack", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> revertBack(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, BindingResult result, HttpSession session) {

		logger.info("Method : revertBack function starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "revertBack", requisitionViewModel, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message == "") {
			res.setMessage("Success");

			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}
			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ requisitionViewModel.get(0).getSubject();

			String message1 = requisitionViewModel.get(0).getComment();

			String[] attachFiles = null;
			if (requisitionViewModel.get(0).getDocument() != null && requisitionViewModel.get(0).getDocument() != "") {
				attachFiles = new String[requisitionViewModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (requisitionViewModel.get(i).getDocument() != null
							&& requisitionViewModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + requisitionViewModel.get(i).getDocument();
					}
				}
			}
			// System.out.println("attachFiles"+attachFiles[0].toString());
			System.out.println(Constant.host);
			System.out.println(Constant.port);
			System.out.println(userName);
			System.out.println(Constant.password);
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : revertBack function Ends");
		return res;
	}

	/*
	 * Replay auto Complete
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-getReplyAutocomplete" })
	public @ResponseBody JsonResponse<List<RequisitionViewModel>> getReplayAutocomplete(Model model,
			@RequestParam("id") String id, @RequestParam("reqId") String reqId, HttpSession session) {
		logger.info("Method : getReplayAutocomplete starts");
		JsonResponse<List<RequisitionViewModel>> res = new JsonResponse<List<RequisitionViewModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getReplyAutocomplete?id=" + id + "&reqId=" + reqId,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<RequisitionViewModel> resp = mapper.convertValue(res.getBody(),
				new TypeReference<List<RequisitionViewModel>>() {
				});

		for (RequisitionViewModel m : resp) {
			if (m.getDocumentComment() == null || m.getDocumentComment() == "null") {
				m.setDocumentComment("");
			}
			if (m.getDocumentName() == null || m.getDocumentName() == "null") {
				m.setDocumentName("");
			}
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getReplayAutocomplete ends");
		// System.out.println(res);
		return res;
	}

	/**
	 * saveForward
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-statutary-audit-master-saveForward" })
	public @ResponseBody JsonResponse<Object> saveForward(Model model, @RequestBody RequisitionViewModel obj,
			BindingResult result) {
		logger.info("Method : saveForward starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		// System.out.println(obj);

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveForward", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveForward ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Scheduled(cron = "0 5 */3 * * 1-5")
//	@Scheduled(cron = "0 */10 * * * *")
	public void performTaskUsingCron() {
		logger.info("Method : performTaskUsingCron starts");

		String dateNow = dateFormat.format(new Date());

		JsonResponse<List<RequisitionViewModel>> resp = new JsonResponse<List<RequisitionViewModel>>();

		try {
			resp = restClient.getForObject(env.getAuditUrl() + "getReminderDetails", JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String host = Constant.host;
		String port = Constant.port;
		String password = Constant.password;

		ObjectMapper mapper = new ObjectMapper();

		List<RequisitionViewModel> res = mapper.convertValue(resp.getBody(),
				new TypeReference<List<RequisitionViewModel>>() {
				});
		

		Date d1 = null;
		Date d2 = null;

		for (RequisitionViewModel m : res) {
			List<String> toAddress = new ArrayList<String>();
			List<String> ccAddress = new ArrayList<String>();
			try {
				if (m.getReminderDate() != null) {
					d1 = dateFormat.parse(dateNow); 
					d2 = dateFormat.parse(m.getReminderDate());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			boolean bik = false;
			String s = m.getEmailstatus();
			if (s.contains("1")) {
				bik = true;
			}

			Integer reqId = m.getReqId();
			Integer auditId = m.getAuditId();

			toAddress.add(m.getPersonTo());

			long diff = d2.getTime() - d1.getTime();

			EmailAttachmentSender email = new EmailAttachmentSender();
			String subject = "Reminder For Reply";
			String Content = "You have not replied to the mail '" + m.getSubject() + "' From '" + m.getMailFrom()
					+ "'";

			JsonResponse<Object> resnew = new JsonResponse<Object>();
			if (diff < 0) {
				try {
					if (bik) {
						email.sendEmailWithAttachments(host, port, null, password, toAddress, ccAddress, subject,
								Content, null);
						resnew = restClient.getForObject(
								env.getAuditUrl() + "getReminderDetailsupdate?reqId=" + reqId + "&auditId=" + auditId,
								JsonResponse.class);
					}
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		logger.info("Method : performTaskUsingCron ends");
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "/view-statutary-audit-master-new-para-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> DraftNewPara(
			@RequestBody List<AuditObservationModel> auditObservationModel, Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : DraftPara function starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : auditObservationModel) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "restAddnewPara", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}

			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ auditObservationModel.get(0).getSubject();
			String message1 = auditObservationModel.get(0).getQuestion();

			String[] attachFiles = null;
			if (auditObservationModel.get(0).getDocument() != null
					&& auditObservationModel.get(0).getDocument() != "") {
				attachFiles = new String[auditObservationModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (auditObservationModel.get(i).getDocument() != null
							&& auditObservationModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + auditObservationModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : DraftPara function Ends");
		return res;
	}

	@SuppressWarnings({ "unchecked", })
	@RequestMapping(value = "/view-statutary-audit-master-para1-ajax", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> Para(@RequestBody List<AuditObservationModel> auditObservationModel,
			Model model, HttpSession session) {
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : Para function starts");

		String userId = "";
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (AuditObservationModel a : auditObservationModel) {
			a.setCreatedBy(userId);
			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
		}
		try {

			res = restClient.postForObject(env.getAuditUrl() + "restforPara", auditObservationModel,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
			ObjectMapper mapper = new ObjectMapper();

			List<DropDownModel> dropDownModel = mapper.convertValue(res.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			String userName = "";
			try {
				userName = (String) session.getAttribute("USER_NAME");
			} catch (Exception e) {
			}

			List<String> recipients = new ArrayList<String>();
			for (DropDownModel m : dropDownModel) {

				recipients.add(m.getName());
			}
			List<DropDownModel> dropDownModel1 = mapper.convertValue(res.getSecondBody(),
					new TypeReference<List<DropDownModel>>() {
					});

			List<String> recipients1 = new ArrayList<String>();
			if (dropDownModel1 != null) {
				for (DropDownModel m : dropDownModel1) {
					recipients1.add(m.getName());
				}
			}
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-"
					+ auditObservationModel.get(0).getSubject();
			String message1 = auditObservationModel.get(0).getQuestion();

			String[] attachFiles = null;
			if (auditObservationModel.get(0).getDocument() != null
					&& auditObservationModel.get(0).getDocument() != "") {
				attachFiles = new String[auditObservationModel.size()];
				for (int i = 0; i < attachFiles.length; i++) {
					if (auditObservationModel.get(i).getDocument() != null
							&& auditObservationModel.get(i).getDocument() != "") {
						attachFiles[i] = env.getFileUploadAudit() + auditObservationModel.get(i).getDocument();
					}
				}
			}
			try {
				if (emalstatus.equals(emailvalue)) {
					EmailAttachmentSender.sendEmailWithAttachments(Constant.host, Constant.port, userName,
							Constant.password, recipients, recipients1, subject, message1, attachFiles);
					// System.out.println("Email sent.");
				}
			} catch (Exception ex) {
				// System.out.println("Could not send email.");
				ex.printStackTrace();
			}

		}
		logger.info("Method : Para function Ends");
		return res;
	}

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping("/view-statutary-audit-master-single-pdf-all")
	 * //@SuppressWarnings("unchecked") //@RequestMapping(value =
	 * "view-statutary-audit-master-single-pdf-all", method = { RequestMethod.POST
	 * }) //@PostMapping("/view-statutary-audit-master-single-pdf-comment")
	 * public @ResponseBody JsonResponse<Object> viewAllAudDocument(Model
	 * model, @RequestBody RequisitionViewModel requisitionViewModel, HttpSession
	 * session) throws IOException {
	 */

	/*
	 * public @ResponseBody List<String> viewAllAudDocument(@RequestParam String
	 * allpdfidid, Model model, HttpSession session) {
	 */

	/*
	 * @PostMapping("/view-statutary-audit-master-single-pdf-comment")
	 * public @ResponseBody JsonResponse<Object>pdfViewOfComment(Model
	 * model, @RequestBody AuditMeetingModel auditMeetingModel, HttpSession session)
	 * throws IOException {
	 * 
	 * logger.info("Method : pdfViewOfComment starts");
	 * 
	 * //String data = ""; try { session.setAttribute("comment",
	 * auditMeetingModel.getComment()); session.setAttribute("date",
	 * auditMeetingModel.getDate()); session.setAttribute("time",
	 * auditMeetingModel.getFromTime()); session.setAttribute("createdBy",
	 * auditMeetingModel.getCreatedBy()); session.setAttribute("initiate",
	 * auditMeetingModel.getInitiate()); session.setAttribute("personTo",
	 * auditMeetingModel.getPersonTo()); session.setAttribute("personCc",
	 * auditMeetingModel.getToTime());
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * JsonResponse<Object> resp = new JsonResponse<Object>();
	 * resp.setMessage("success");
	 * 
	 * logger.info("Method : pdfViewOfComment ends"); System.out.println(resp);
	 * return resp; }
	 * 
	 * @GetMapping("view-statutary-audit-master-single-pdf-comment-view") public
	 * void generateStatuaryAuditPdfComment(HttpServletResponse response, Model
	 * model, HttpSession session) {
	 * 
	 * Map<String, Object> data = new HashMap<String, Object>();
	 * 
	 * 
	 * data.put("comment", session.getAttribute("comment")); data.put("commentBy",
	 * session.getAttribute("createdBy")); data.put("date",
	 * session.getAttribute("date")); data.put("time",
	 * session.getAttribute("time")); data.put("initiate",
	 * session.getAttribute("initiate")); data.put("personTo",
	 * session.getAttribute("personTo")); data.put("personCc",
	 * session.getAttribute("personCc")); session.removeAttribute("comment");
	 * session.removeAttribute("commentBy"); session.removeAttribute("date");
	 * session.removeAttribute("time"); session.removeAttribute("initiate");
	 * session.removeAttribute("personTo"); session.removeAttribute("personCc");
	 * 
	 * System.out.println("bikash data of pdf "+Arrays.asList(data));
	 * 
	 * response.setContentType("application/pdf");
	 * response.setHeader("Content-disposition",
	 * "inline; filename=commentDetails.pdf"); File file; byte[] fileData = null;
	 * try { file = pdfGeneratorUtil.createPdf("audit/commentViewPdf", data);
	 * InputStream in = new FileInputStream(file); fileData =
	 * IOUtils.toByteArray(in); response.setContentLength(fileData.length);
	 * response.getOutputStream().write(fileData);
	 * response.getOutputStream().flush(); } catch (IOException e) {
	 * e.printStackTrace(); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-single-pdf-all" })
	public @ResponseBody JsonResponse<List<RequisitionViewModel>> viewAllAudDocument(Model model,
			@RequestParam("allpdfidid") String allpdfidid, HttpSession session) {

		logger.info("Method : viewAllAudDocument function starts");
		// JsonResponse<RequisitionViewModel> res = new
		// JsonResponse<RequisitionViewModel>();
		// String allpdfidid= requisitionViewModel.getPrevauditid();

		// List<String> res= new ArrayList<String>();
		// JsonResponse<Object> res = new JsonResponse<Object>();
		JsonResponse<List<RequisitionViewModel>> res = new JsonResponse<List<RequisitionViewModel>>();

		byte[] decodeId = Base64.getDecoder().decode(allpdfidid.getBytes());
		String prevauditid = (new String(decodeId));

		/*
		 * try { res = restClient.postForObject(env.getAuditUrl() + "saveForward", obj,
		 * JsonResponse.class);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

		try {
			res = restClient.getForObject(env.getAuditUrl() + "viewAllDocument?prevauditid=" + prevauditid,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		System.out.println("Bikash it's your response" + res);

		logger.info("Method : viewAllAudDocument function Ends");
		return res;
	}

	@GetMapping("view-statutary-audit-master-single-pdf-all-view")
	public void generatAllAuditPdfComment(HttpServletResponse response, @RequestParam("allpdfidid") String allpdfidid,
			Model model, HttpSession session) {
		logger.info("Method : generatAllAuditPdfComment function starts");
		// System.out.println("new data"+data1);

		JsonResponse<List<RequisitionViewModel>> res = new JsonResponse<List<RequisitionViewModel>>();

		byte[] decodeId = Base64.getDecoder().decode(allpdfidid.getBytes());
		String prevauditid = (new String(decodeId));
		// System.out.println("new data"+prevauditid);

		try {
			res = restClient.getForObject(env.getAuditUrl() + "viewAllDocument?prevauditid=" + prevauditid,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("Bikash it's your response2"+res);

		ObjectMapper mapper = new ObjectMapper();

		List<RequisitionViewModel> assignEdu = mapper.convertValue(res.getBody(),
				new TypeReference<List<RequisitionViewModel>>() {
				});
		// System.out.println("employee "+assignEdu);

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("alldata", assignEdu);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=commentDetails.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("audit/allViewPdf", data);
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

		logger.info("Method : generatAllAuditPdfComment function Ends");

	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-getReqResponse-previd" })
	public @ResponseBody JsonResponse<DropDownModel> getReqprevid(Model model, @RequestParam("reqid") String reqid,
			HttpSession session) {
		logger.info("Method : getReqprevid starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getReqorev?reqid=" + reqid, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res.getBody());
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getReqprevid ends");
		/// System.out.println(res);
		return res;
	}

	/*
	 * Draft Section by Deepak
	 */
	@SuppressWarnings({ "unchecked", })
	// request requisition
	@RequestMapping(value = "/view-statutary-audit-master-saveDraft", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> saveDraft(@RequestBody List<RequisitionViewModel> requisitionViewModel,
			Model model, BindingResult result, HttpSession session) {

		logger.info("Method : draftRequestRequisition function starts");

		// System.out.println("file123 "+requisitionViewModel.get(0).getFile());
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("draft requisitionViewModel"+requisitionViewModel);

		for (RequisitionViewModel a : requisitionViewModel) {
			a.setCreatedby(userId);
			if (a.getEditDocName() != null && a.getEditDocName() != "") {
				a.setDocument(a.getEditDocName());
			} else {
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}
//			a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			System.out.println(a.getDocument());
		}

		// System.out.println("draft requisitionViewModel 2"+requisitionViewModel);

		try {

			res = restClient.postForObject(env.getAuditUrl() + "saveDraft", requisitionViewModel, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message == "") {
			res.setMessage("Success");

		}

		logger.info("Method : draftRequestRequisition function Ends");

		return res;
	}

	/*
	 * draft LIst
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-getDraft-list" })
	public @ResponseBody JsonResponse<List<DraftListModel>> getDraftLIst(Model model, @RequestParam("id") String id,
			@RequestParam("userId") String userId, @RequestParam String status, HttpSession session) {
		logger.info("Method : getDraftLIst starts");
		JsonResponse<List<DraftListModel>> res = new JsonResponse<List<DraftListModel>>();

		try {
			res = restClient.getForObject(
					env.getAuditUrl() + "getDraftLIst?id=" + id + "&userId=" + userId + "&status=" + status,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getReplayAutocomplete ends");
		System.out.println(res);
		return res;
	}

	/*
	 * Draft comment
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-draft-comment" })
	public @ResponseBody JsonResponse<DraftListModel> getDraftComment(Model model, @RequestParam("id") String id,
			HttpSession session) {
		logger.info("Method : getDraftComment starts");
		JsonResponse<DraftListModel> res = new JsonResponse<DraftListModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDraftComment?id=" + id, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getDraftComment ends");
		System.out.println("Draft comment: " + res);
		return res;
	}

	/*
	 * Draft auto Complete
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-statutary-audit-master-getDraftAutocomplete" })
	public @ResponseBody JsonResponse<List<DraftModel>> draftAutocomplete(Model model, @RequestParam("id") String id,
			HttpSession session) {
		logger.info("Method : getReplayAutocomplete starts");
		JsonResponse<List<DraftModel>> res = new JsonResponse<List<DraftModel>>();
		System.out.println(id);
		try {
			res = restClient.getForObject(env.getAuditUrl() + "draftAutocomplete?id=" + id, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<DraftModel> resp = mapper.convertValue(res.getBody(), new TypeReference<List<DraftModel>>() {
		});

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : getReplayAutocomplete ends");
		System.out.println(res);
		return res;
	}
}
