package nirmalya.aathithya.webmodule.audit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;

@Controller
@RequestMapping("audit/")
public class InternalAuditReportController {
	
	Logger logger = LoggerFactory.getLogger(InternalAuditReportController.class);
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles env;
	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	

	@GetMapping("view-initiated-audit-internal-audit-report-download")
	public String AuditreportDownload(Model model, HttpSession session, HttpServletResponse response,
			@RequestParam("section") String section, @RequestParam("department") String department,
			@RequestParam("auditId") String auditId, @RequestParam("type") String type, @RequestParam("auditType") String auditType) {
		logger.info("AuditreportDownload start");

		String userId = (String) session.getAttribute("USER_ID");
		List<AuditObservationModel> questions = new ArrayList<AuditObservationModel>();

		try {

			AuditObservationModel[] res = restTemplate
					.getForObject(
							env.getAuditUrl() + "getQuestionList?userid=" + userId + "&auditId=" + auditId
									+ "&department=" + department + "&section=" + section,
							AuditObservationModel[].class);
			questions = Arrays.asList(res);

			for (AuditObservationModel a : questions) {
				if(a.getSeverity()!=null) {
				if (a.getSeverity().equals("1")) {
					a.setSeverityType("Blocker");
				} else if (a.getSeverity().equals("2")) {
					a.setSeverityType("High");

				} else if (a.getSeverity() .equals("3")) {
					a.setSeverityType("Medium");

				} else if (a.getSeverity() .equals("4")) {
					a.setSeverityType("Low");
				}
				}
				if(a.getRiskRating()!=null) {
				if (a.getRiskRating().equals("1")) {
					a.setRiskRating("Blocker");
				} else if (a.getRiskRating() .equals("2")) {
					a.setRiskRating("High");

				} else if (a.getRiskRating() .equals("3")) {
					a.setRiskRating("Medium");

				} else if (a.getRiskRating().equals("4")) {
					a.setRiskRating("Low");
				}
				}
			
			

				RequisitionViewModel[] requisitionViewModel = restTemplate.getForObject(
						env.getAuditUrl() + "getCoObsForwardDetails?id=" + a.getAuditId()+"&userId="+userId,
						RequisitionViewModel[].class);
				List<RequisitionViewModel> forwardDetails = Arrays.asList(requisitionViewModel);
				
				a.setForwardDetails(forwardDetails);

			}
			//System.out.println("questions:" + questions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/***********************************
		 * Deviation Question List
		 **********************************************************/
		/* / Question List / */
		AuditObservationModel[] res1 = restTemplate.getForObject(env.getAuditUrl() + "getDeviationQuestionList?id="
				+ auditId + "&deptId=" + department + "&sectionId=" + section + "&userId=" + userId,
				AuditObservationModel[].class);
		List<AuditObservationModel> DevQuestions = Arrays.asList(res1);

		for (AuditObservationModel a : DevQuestions) {
			if (a.getRiskRating()!=null) {
				if (a.getRiskRating().equals("1")) {
					a.setRiskRating("Blocker");
				} else if (a.getRiskRating() .equals("2")) {
					a.setRiskRating("High");

				} else if (a.getRiskRating() .equals("3")) {
					a.setRiskRating("Medium");

				} else if (a.getRiskRating().equals("4")) {
					a.setRiskRating("Low");
				}
			}
			/*if (a.getSeverity().equals("1")) {
				a.setSeverityType("Blocker");
			} else if (a.getSeverity().equals("2")) {
				a.setSeverityType("High");

			} else if (a.getSeverity(). equals("3")) {
				a.setSeverityType("Medium");

			} else if (a.getSeverity() . equals("4")) {
				a.setSeverityType("Low");
			}*/
			RequisitionViewModel[] requisitionViewModel = restTemplate.getForObject(
					env.getAuditUrl() + "getCoObsForwardDetails?id=" + a.getAuditId()+"&userId="+userId,
					RequisitionViewModel[].class);
			List<RequisitionViewModel> forwardDetails = Arrays.asList(requisitionViewModel);
			
			a.setForwardDetails(forwardDetails);

		}
		//System.out.println("type=" + type);
		String heading = "";
		if (type.equals("A")) {			
			model.addAttribute("questions", questions);
			heading = "Auditor's Observations";
		} else if (type.equals("B")) {
			model.addAttribute("questions", DevQuestions);
			heading = "Deviation Points Of Check List";
		}
		model.addAttribute("auditType",auditType);
		model.addAttribute("heading", heading);
		logger.info("AuditreportDownload end");
		return "audit/InternalAuditReportPdf";
	}
	@GetMapping("view-initiated-audit-internal-audit-report-download-staReport")
	public String AuditreportDownload(Model model, HttpSession session, HttpServletResponse response,
			 @RequestParam("auditType") String auditType,@RequestParam("auditNo") String auditNo) {
		logger.info("AuditreportDownload start");
		byte[] encodeByte = Base64.getDecoder().decode(auditNo.getBytes());
		String id1 = (new String(encodeByte));
		
		byte[] encodeByte1 = Base64.getDecoder().decode(auditType.getBytes());
		String auditType1 = (new String(encodeByte1));
		
		String userId = (String) session.getAttribute("USER_ID");
		List<DropDownModel> questions = new ArrayList<DropDownModel>();
		List<DropDownModel> replies = new ArrayList<DropDownModel>();
		List<DropDownModel> finObs = new ArrayList<DropDownModel>();

		try {

			DropDownModel[] res = restTemplate
					.getForObject(
							env.getAuditUrl() + "getAuditReportDtl?auditId=" + id1,DropDownModel[].class);
			questions = Arrays.asList(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			DropDownModel[] res = restTemplate
					.getForObject(
							env.getAuditUrl() + "getReplyOfManagement?auditId=" + id1,DropDownModel[].class);
			replies = Arrays.asList(res);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			
			DropDownModel[] res = restTemplate
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
		String heading  = "Auditor's Observations";
		model.addAttribute("questions",questions);
		model.addAttribute("finObs",finObs);
		System.out.println("finObs"+finObs);
		model.addAttribute("replies",replies);
		model.addAttribute("auditType",auditType1);
		model.addAttribute("heading", heading);
		model.addAttribute("auditNo", id1);
		logger.info("AuditreportDownload end");
		return "audit/InternalAuditReportPdf";
	}
	
	
}
