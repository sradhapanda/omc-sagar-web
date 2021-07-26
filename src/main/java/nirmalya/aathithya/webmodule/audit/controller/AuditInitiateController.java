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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.google.common.base.Joiner;

import nirmalya.aathithya.webmodule.audit.model.AuditAdditionalInfoModel;
import nirmalya.aathithya.webmodule.audit.model.AuditComplianceModel;
import nirmalya.aathithya.webmodule.audit.model.AuditDetailModel;
import nirmalya.aathithya.webmodule.audit.model.AuditInitiateModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMasterModel;
import nirmalya.aathithya.webmodule.audit.model.AuditMeetingModel;
import nirmalya.aathithya.webmodule.audit.model.AuditNoteModel;
import nirmalya.aathithya.webmodule.audit.model.AuditObservationModel;
import nirmalya.aathithya.webmodule.audit.model.AuditReportModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.audit.model.RequisitionViewModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EmailAttachmentSender;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MailService;
import nirmalya.aathithya.webmodule.constant.Constant;

/**
 * @author NirmalyLabs
 *
 */
@Controller
@RequestMapping(value = "audit")
public class AuditInitiateController {

	Logger logger = LoggerFactory.getLogger(AuditInitiateController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	MailService mailService;
	@Autowired
	EmailAttachmentSender emailAttachmentSender;
	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * Get Mapping view initiated audit
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-initiated-audit-dtl")
	public String viewInitiatedAudita(Model model, @RequestParam("id") String encodeId,
			@RequestParam("auditType") String auditType, HttpSession session) {
		logger.info("Method : viewInitiatedAudit starts");

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		byte[] auditType1 = Base64.getDecoder().decode(auditType.getBytes());

		String auditTypeID = (new String(auditType1));

		model.addAttribute("auditTypeId", auditTypeID);

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> roles = null;
		try {
			roles = (List<String>) session.getAttribute("USER_ROLES");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*******************************
		 * Meeting Details
		 *********************************************************/
		AuditMeetingModel[] meetingDetailsEachAudit = restClient
				.getForObject(env.getAuditUrl() + "getMeetingDetailsEachAudit?id=" + id, AuditMeetingModel[].class);
		List<AuditMeetingModel> meetingDtlechAudit = Arrays.asList(meetingDetailsEachAudit);
		for (AuditMeetingModel a : meetingDtlechAudit) {
			DropDownModel[] meetingDetailsDocAudit = restClient
					.getForObject(env.getAuditUrl() + "getMeetingDetailsDoc?id=" + a.getId(), DropDownModel[].class);
			List<DropDownModel> meetingDtlDocAudit = Arrays.asList(meetingDetailsDocAudit);

			for (DropDownModel m : meetingDtlDocAudit) {
				m.setKey(viewUploadedImage(m.getKey(), m.getName()));

				a.setDocuments(meetingDtlDocAudit);
			}

		}

		model.addAttribute("meetingDtlechAudit", meetingDtlechAudit);
		/********************************************************************************/
		/** Department Emp List **/
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

		/* audit Initiation Details */

		AuditMasterModel[] auditMasterModel = restClient
				.getForObject(env.getAuditUrl() + "getinitiateAuditById?id=" + id, AuditMasterModel[].class);
		List<AuditMasterModel> auditList = Arrays.asList(auditMasterModel);
		model.addAttribute("initiateId", auditList.get(0).getAuditInitiated());
		/** persons to forward **/
		try {
			DropDownModel[] dept = restClient.getForObject(
					env.getAuditUrl() + "getMeetSectionList?id=" + auditList.get(0).getDepartment(),
					DropDownModel[].class);
			List<DropDownModel> sectionList = Arrays.asList(dept);

			model.addAttribute("sectionList", sectionList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * Priority List
		 */
		try {
			DropDownModel[] priority = restClient.getForObject(env.getAuditUrl() + "getPriorityList",
					DropDownModel[].class);
			List<DropDownModel> getPriorityList = Arrays.asList(priority);

			model.addAttribute("getPriorityList", getPriorityList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		try {
			AuditAdditionalInfoModel[] docList = restClient.getForObject(
					env.getAuditUrl() + "getinitiateAuditDocsById?id=" + id, AuditAdditionalInfoModel[].class);
			for (AuditAdditionalInfoModel a : docList) {
				a.setDoc(viewUploadedImage(a.getDoc(), a.getDocName()));

			}

			model.addAttribute("initiateAuditDocs", docList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		AuditNoteModel[] aNoted = restClient.getForObject(env.getAuditUrl() + "getAuditNoteById?id=" + id,
				AuditNoteModel[].class);
		List<AuditNoteModel> aNote = Arrays.asList(aNoted);
		for (AuditNoteModel a : aNote) {
			String[] time = a.getDate().split(" ");
			a.setDate(time[0]);
			a.setTime(time[1].split(":")[0] + ":" + time[1].split(":")[1]);

			DropDownModel[] aNoteDoc = restClient
					.getForObject(env.getAuditUrl() + "getAuditNoteByIdDoc?id=" + a.getNoteId(), DropDownModel[].class);
			List<DropDownModel> aNoteDocDtl = Arrays.asList(aNoteDoc);

			for (DropDownModel m : aNoteDocDtl) {
				m.setKey(viewUploadedImage(m.getKey(), m.getName()));
			}
			a.setDocList(aNoteDocDtl);
		}
		if (aNote.equals(null)) {
			model.addAttribute("AuditNote", "");
		} else {
			model.addAttribute("AuditNote", aNote);
		}

		/**********************************************************************************************************/

		/* Question List */
		AuditObservationModel[] res = restClient.getForObject(env.getAuditUrl() + "getAuditorQuestionList?id=" + id,
				AuditObservationModel[].class);
		List<AuditObservationModel> auditorQuestions = Arrays.asList(res);
		for (AuditObservationModel a : auditorQuestions) {
			if (a.getSeverity() != null) {
				if (a.getSeverity().equals("1")) {
					a.setSeverityType("Blocker");
				} else if (a.getSeverity().equals("2")) {
					a.setSeverityType("High");

				} else if (a.getSeverity().equals("3")) {
					a.setSeverityType("Medium");

				} else if (a.getSeverity().equals("4")) {
					a.setSeverityType("Low");
				}
			}
			if (a.getObsType() != null) {
				if (a.getObsType().equals("1")) {
					a.setObsTypeName("Improvement");
				}
				if (a.getObsType().equals("2")) {
					a.setObsTypeName("POM");
				}
				if (a.getObsType().equals("3")) {
					a.setObsTypeName("NC");
				}
			}
			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getRiskRating(), a.getFinalObservation()));

		}

		model.addAttribute("auditorQuestions", auditorQuestions);
		/* IRS List */
		AuditObservationModel[] resIRS = restClient.getForObject(env.getAuditUrl() + "getAuditorIRSList?id=" + id,
				AuditObservationModel[].class);
		List<AuditObservationModel> auditorIRS = Arrays.asList(resIRS);
		for (AuditObservationModel a : auditorIRS) {

			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getRiskRating(), a.getFinalObservation()));

		}
		model.addAttribute("auditorIRS", auditorIRS);
		/* DRAFT PARA List */
		AuditObservationModel[] resDraftPara = restClient
				.getForObject(env.getAuditUrl() + "getAuditorDraftParaList?id=" + id, AuditObservationModel[].class);
		List<AuditObservationModel> auditorDarft = Arrays.asList(resDraftPara);
		for (AuditObservationModel a : auditorDarft) {

			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getRiskRating(), a.getFinalObservation()));

		}
		if (aNote.size() > 0 && auditorQuestions.size() == 0 && auditorIRS.size() == 0 && auditorDarft.size() == 0) {
			model.addAttribute("identifier", "REQ");
			// System.out.println("here REQ");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() == 0
				&& auditorDarft.size() == 0) {
			model.addAttribute("identifier", "POM");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() > 0
				&& auditorDarft.size() == 0) {
			model.addAttribute("identifier", "IRS");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorDarft.size() > 0
				&& auditorDarft.size() > 0) {
			model.addAttribute("identifier", "PARA");
		}
		
		

		model.addAttribute("auditorDarftpara", auditorDarft);

		/* NEW PARA List */
		AuditObservationModel[] restPara = restClient.getForObject(env.getAuditUrl() + "getAuditorParaList?id=" + id,
				AuditObservationModel[].class);
		List<AuditObservationModel> auditorPara1 = Arrays.asList(restPara);
		for (AuditObservationModel a : auditorPara1) {

			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getRiskRating(), a.getFinalObservation()));

		}
		if (aNote.size() > 0 && auditorQuestions.size() == 0 && auditorIRS.size() == 0 && auditorPara1.size() == 0) {
			model.addAttribute("identifier", "REQ");
			// System.out.println("here REQ");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() == 0
				&& auditorPara1.size() == 0) {
			model.addAttribute("identifier", "POM");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() > 0
				&& auditorPara1.size() == 0) {
			model.addAttribute("identifier", "IRS");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorPara1.size() > 0
				&& auditorPara1.size() > 0) {
			model.addAttribute("identifier", "PARA");
		}

		model.addAttribute("newpara", auditorPara1);

		AuditObservationModel[] restCoco = restClient.getForObject(env.getAuditUrl() + "getAuditorCocoList?id=" + id,
				AuditObservationModel[].class);
		List<AuditObservationModel> auditorCoco = Arrays.asList(restCoco);
		for (AuditObservationModel a : auditorCoco) {

			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getRiskRating(), a.getFinalObservation()));

		}
		if (aNote.size() > 0 && auditorQuestions.size() == 0 && auditorIRS.size() == 0 && auditorCoco.size() == 0) {
			model.addAttribute("identifier", "REQ");
			// System.out.println("here REQ");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() == 0
				&& auditorCoco.size() == 0) {
			model.addAttribute("identifier", "POM");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorIRS.size() > 0
				&& auditorCoco.size() == 0) {
			model.addAttribute("identifier", "IRS");
		} else if (aNote.size() > 0 && auditorQuestions.size() > 0 && auditorCoco.size() > 0
				&& auditorCoco.size() > 0) {
			model.addAttribute("identifier", "PARA");
		}

		model.addAttribute("coco", auditorCoco);
		/*******************************
		 * Meeting Details
		 *********************************************************/
		AuditMeetingModel[] meetingDetails = restClient.getForObject(
				env.getAuditUrl() + "getMeetingDetails?id=" + id + "&auditType=" + auditTypeID,
				AuditMeetingModel[].class);
		List<AuditMeetingModel> meetingDtl = Arrays.asList(meetingDetails);
		for (AuditMeetingModel a : meetingDtl) {
			DropDownModel[] meetingDetailsDoc = restClient
					.getForObject(env.getAuditUrl() + "getMeetingDetailsDoc?id=" + a.getId(), DropDownModel[].class);
			List<DropDownModel> meetingDtlDoc = Arrays.asList(meetingDetailsDoc);
			if (a.getMeetingType() != null && a.getMeetingType() != "") {
				if (a.getMeetingType().equals("1")) {
					a.setMeetingType("Entry Meeting");
				}
				if (a.getMeetingType().equals("2")) {
					a.setMeetingType("Exit Meeting");
				}
			}
			for (DropDownModel m : meetingDtlDoc) {
				m.setKey(viewUploadedImage(m.getKey(), m.getName()));

				a.setDocuments(meetingDtlDoc);
			}

		}

		model.addAttribute("meetingDtl", meetingDtl);
		/********************************************************************************/
		List<Object> questionList = new ArrayList<Object>();
		List<DropDownModel> questionVal = new ArrayList<DropDownModel>();
		int counter = 0;
		for (AuditMasterModel m : auditList) {
			/* table audit master details */
			List<AuditMasterModel> auditOpertaion = new ArrayList<AuditMasterModel>();
			if (m.getAuditName().equals("ADTM005")) {
				AuditMasterModel[] auditOpertaionDtl = restClient
						.getForObject(
								env.getAuditUrl() + "getAllStatutaryAuditList?id=" + m.getAuditAutoGenId()
										+ "&sectionId=" + m.getRegion() + "&userId=" + userId,
								AuditMasterModel[].class);
				auditOpertaion = Arrays.asList(auditOpertaionDtl);

			} else {
				AuditMasterModel[] auditOpertaionDtl = restClient
						.getForObject(
								env.getAuditUrl() + "getAllStatutaryAuditList?id=" + m.getAuditAutoGenId()
										+ "&sectionId=" + m.getSection() + "&userId=" + userId,
								AuditMasterModel[].class);
				auditOpertaion = Arrays.asList(auditOpertaionDtl);
			}

			if (auditOpertaion.size() > 0) {

				m.setCounter(counter);
				// System.out.println("data"+m.getCounter());

				counter++;
				/*******************************
				 * Meeting Details in Section
				 *********************************************************/
				AuditMeetingModel[] meetingDetailsSec = restClient.getForObject(env.getAuditUrl()
						+ "getMeetingDetails?id=" + m.getAuditAutoGenId() + "&auditType=" + auditTypeID,
						AuditMeetingModel[].class);
				List<AuditMeetingModel> meetingDtlSec = Arrays.asList(meetingDetailsSec);
				for (AuditMeetingModel a : meetingDtlSec) {
					if (a.getMeetingType() != null) {
						if (a.getMeetingType().equals("1")) {
							a.setMeetingType("Entry Meeting");
						}
						if (a.getMeetingType().equals("2")) {
							a.setMeetingType("Exit Meeting");
						}
					}
					DropDownModel[] meetingDetailsSecDoc = restClient.getForObject(
							env.getAuditUrl() + "getMeetingDetailsDoc?id=" + a.getId(), DropDownModel[].class);
					List<DropDownModel> meetingDtlSecDoc = Arrays.asList(meetingDetailsSecDoc);

					for (DropDownModel n : meetingDtlSecDoc) {
						n.setKey(viewUploadedImage(n.getKey(), n.getName()));
						a.setDocumentsSec(meetingDtlSecDoc);
					}

				}
				m.setMeetingDtlSec(meetingDtlSec);
				// model.addAttribute("meetingDtlSec",meetingDtlSec);
				/********************************************************************************/

				if (auditOpertaion.get(0).getAuditStatus() == "0") {
					m.setStatus("Not Observed");
				}
			}
			m.setAuditOperationDtl(auditOpertaion);
			/*******************************************************************************************************/
			RequisitionViewModel[] coforwardDetailsdata = restClient.getForObject(
					env.getAuditUrl() + "getCoForwardDetails?id=" + m.getAuditAutoGenId(),
					RequisitionViewModel[].class);
			List<RequisitionViewModel> coforwardDetails = Arrays.asList(coforwardDetailsdata);
			for (RequisitionViewModel a : coforwardDetails) {
				String[] time = a.getCreatedOn().split(" ");
				a.setDate(time[0]);
				a.setTime(time[1]);

				DropDownModel[] dropDownModel1 = restClient
						.getForObject(env.getAuditUrl() + "getForDocList?id=" + a.getReqId(), DropDownModel[].class);
				List<DropDownModel> docList = Arrays.asList(dropDownModel1);
				a.setDocumentList(docList);
				for (int i = 0; i < a.getDocumentList().size(); i++) {
					a.getDocumentList().get(i).setKey(viewUploadedImage(a.getDocumentList().get(i).getKey(),
							a.getDocumentList().get(i).getName()));

				}

			}
			m.setCoforwardDetail(coforwardDetails);
			model.addAttribute("coforwardDetails", coforwardDetails);
			/*******************************************************************************************************/

			/* requisition Details */
			try {

				RequisitionViewModel[] dropDownModel = restClient.getForObject(
						env.getAuditUrl() + "getStatuReqDocument?id=" + m.getAuditAutoGenId() + "&userId=" + userId,
						RequisitionViewModel[].class);
				List<RequisitionViewModel> reqDtl = Arrays.asList(dropDownModel);
//System.out.println("bikash 123 "+reqDtl);
//System.out.println("bikash 1234 "+dropDownModel[0].getPrevauditid());
//model.addAttribute("prevAuditid", dropDownModel[0].getPrevauditid());
				m.setRequisitionViewModel(reqDtl);

				for (int i = 0; i < m.getRequisitionViewModel().size(); i++) {
					String userName = "";

					userName = (String) session.getAttribute("USER_NAME");

					DropDownModel[] dropDownModel1 = restClient
							.getForObject(
									env.getAuditUrl() + "getReqDocList?id="
											+ m.getRequisitionViewModel().get(i).getReqId() + "&userId=" + userId,
									DropDownModel[].class);
					List<DropDownModel> docList = Arrays.asList(dropDownModel1);

					m.getRequisitionViewModel().get(i).setDocumentList(docList);

					for (DropDownModel a : docList) {
						a.setKey(viewUploadedImage(a.getKey(), a.getDocName()));
					}

					model.addAttribute("docList", docList);
					if (m.getRequisitionViewModel().get(i).getCommentStatus().equals("0")) {

						m.getRequisitionViewModel().get(i).setCommentStatusName("Requisition");
					}
					if (m.getRequisitionViewModel().get(i).getCommentStatus().equals("1")) {
						m.getRequisitionViewModel().get(i).setCommentStatusName("Response");

					}
					if (m.getRequisitionViewModel().get(i).getCommentStatus().equals("2")) {
						m.getRequisitionViewModel().get(i).setCommentStatusName("Assigned");
					}
					if (m.getRequisitionViewModel().get(i).getCommentStatus().equals("3")) {
						m.getRequisitionViewModel().get(i).setCommentStatusName("Send Back");
					}

					String[] time = m.getRequisitionViewModel().get(i).getCreatedOn().split(" ");

					m.getRequisitionViewModel().get(i).setDate(time[0]);
					m.getRequisitionViewModel().get(i).setTime(time[1].split(":")[0] + ":" + time[1].split(":")[1]);

				}

			} catch (RestClientException e) {
				e.printStackTrace();
			}

		}
		try {

			DropDownModel[] obs1 = restClient.getForObject(env.getAuditUrl() + "getConcernAuditees",
					DropDownModel[].class);
			List<DropDownModel> concernList = Arrays.asList(obs1);

			model.addAttribute("concernList", concernList);
			// model.addAttribute("auditQuestionStatus", auditQuestionStatus);
			model.addAttribute("questions", questionList);
			model.addAttribute("initiateAudit", auditList);

			System.out.println("auditList " + auditList);

			model.addAttribute("auditInitiate", auditList.get(0).getAuditInitiated());

			System.out.println("auditList1:- " + auditList.get(0).getAuditInitiated());

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		AuditAdditionalInfoModel[] auditDocs = restClient.getForObject(env.getAuditUrl() + "getAuditDocs?id=" + id,
				AuditAdditionalInfoModel[].class);
		List<AuditAdditionalInfoModel> concernList = Arrays.asList(auditDocs);
		for (AuditAdditionalInfoModel a : concernList) {
			a.setDoc(viewUploadedImage(a.getDoc(), a.getDocName()));
		}
		DropDownModel[] dropDownModel = restClient.getForObject(env.getAuditUrl() + "getOnlyQuestions?id=" + id,
				DropDownModel[].class);
		List<DropDownModel> questionListt = Arrays.asList(dropDownModel);
		// questionVal.addAll(questionListt);
		model.addAttribute("questionListt", questionListt);

		DropDownModel[] IRS = restClient.getForObject(env.getAuditUrl() + "getOnlyIRS?id=" + id, DropDownModel[].class);
		List<DropDownModel> IRSList = Arrays.asList(IRS);
		// questionVal.addAll(IRSList);
		model.addAttribute("IRSList", IRSList);

		DropDownModel[] DRAFT = restClient.getForObject(env.getAuditUrl() + "getOnlyDraft?id=" + id,
				DropDownModel[].class);
		List<DropDownModel> DRAFTList = Arrays.asList(DRAFT);
		// questionVal.addAll(IRSList);
		model.addAttribute("Draftlist", DRAFTList);

		DropDownModel[] PARA = restClient.getForObject(env.getAuditUrl() + "getOnlyPara?id=" + id,
				DropDownModel[].class);
		List<DropDownModel> PARAList = Arrays.asList(PARA);
		// questionVal.addAll(IRSList);
		model.addAttribute("Paralist", PARAList);

		model.addAttribute("AuditDocs", auditDocs);

		logger.info("Method : viewInitiatedAudit ends");
		return "audit/auditDetails";
	}

	/**
	 * View Default 'Initiate Audit' page
	 *
	 */
	@GetMapping("/initiate-audit")
	public String initiateAudit(Model model, HttpSession session) {

		logger.info("Method : defaultCircleMaster starts");

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
			DropDownModel[] org = restClient.getForObject(env.getAuditUrl() + "getOrganizationId",
					DropDownModel[].class);
			List<DropDownModel> orgList = Arrays.asList(org);

			model.addAttribute("orgList", orgList);

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
		return "audit/initiate-audit";
	}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/initiate-audit-uploadFile")
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

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "initiate-audit", method = { RequestMethod.POST })
	 * public @ResponseBody JsonResponse<Object> initiateAudit(@RequestBody
	 * List<AuditInitiateModel> audit, Model model, HttpSession session) throws
	 * IOException, MessagingException {
	 * 
	 * logger.info("Method : initiateAudit function starts"); JsonResponse<Object>
	 * res = new JsonResponse<Object>();
	 * 
	 * String userId = ""; try { userId = (String) session.getAttribute("USER_ID");
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * try { MultipartFile inputFile = (MultipartFile)
	 * session.getAttribute("imageFile"); String contentName = ""; if (inputFile !=
	 * null) { long nowTime = new Date().getTime();
	 * 
	 * byte[] bytes = inputFile.getBytes(); String[] fileType =
	 * inputFile.getContentType().split("/");
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
	 * } else { contentName = nowTime + "." + fileType[1]; }
	 * 
	 * for (int i = 0; i < audit.size(); i++) {
	 * audit.get(i).setDocument(contentName); audit.get(i).setCreatedBy(userId); }
	 * res = restClient.postForObject(env.getAuditUrl() + "initiate-audit", audit,
	 * JsonResponse.class);
	 * 
	 * if (res.getCode().contains("Data Saved Successfully")) { Path path =
	 * Paths.get(env.getFileUploadAudit() + contentName); if
	 * (fileType[1].contentEquals("pdf")) { Files.write(path, bytes); } else if
	 * (fileType[1].contentEquals(
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
	 * } else { res = restClient.postForObject(env.getAuditUrl() + "initiate-audit",
	 * audit, JsonResponse.class); } } catch (RestClientException e) {
	 * e.printStackTrace(); }
	 * 
	 * String message = res.getMessage();
	 * 
	 * if (message != null && message != "") {
	 * 
	 * } else { res.setMessage("Success");
	 * 
	 * }
	 * 
	 * logger.info("Method : initiateAudit function Ends"); return res; }
	 */

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit")
	public String viewInitiatedAudit(Model model, @RequestParam("id") String auditType, HttpSession session) {
		logger.info("Method : viewInitiatedAudit starts");
		byte[] encodeByte = Base64.getDecoder().decode(auditType.getBytes());
		String id = (new String(encodeByte));

		model.addAttribute("auditType", id);
		logger.info("Method : viewInitiatedAudit ends");
		return "audit/view-initiate-audit";
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-report")
	public String reports(Model model, @RequestParam("id") String id, HttpSession session) {
		logger.info("Method : reports starts");

		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(decodeId));
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
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditNos", DropDownModel[].class);
			List<DropDownModel> auditNoList = Arrays.asList(audit);

			model.addAttribute("auditNoList", auditNoList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYearList = Arrays.asList(audit);

			model.addAttribute("financialYearList", financialYearList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getDepartmentListSerach",
					DropDownModel[].class);
			List<DropDownModel> departmentList = Arrays.asList(audit);

			model.addAttribute("departmentList", departmentList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		model.addAttribute("auditType", id1);

		logger.info("Method : reports ends");
		return "audit/auditReportView";
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-document-report")
	public String documents(Model model, @RequestParam("id") String id, HttpSession session) {
		logger.info("Method : reports starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
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
		/**
		 * AUDIT NO
		 *
		 *//*
			 * try { DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() +
			 * "getAuditNos", DropDownModel[].class); List<DropDownModel> auditNoList =
			 * Arrays.asList(audit);
			 * 
			 * model.addAttribute("auditNoList", auditNoList);
			 * 
			 * } catch (RestClientException e) { e.printStackTrace(); }
			 */
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYearList = Arrays.asList(audit);

			model.addAttribute("financialYearList", financialYearList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getDepartmentListSerach",
					DropDownModel[].class);
			List<DropDownModel> departmentList = Arrays.asList(audit);

			model.addAttribute("departmentList", departmentList);

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

		model.addAttribute("auditType", id1);
		logger.info("Method : reports ends");
		return "audit/auditDocumentView";
	}

	@GetMapping("/view-initiated-audit-report-aftSerch")
	public String viewInitiatedAuditReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3,
			@RequestParam("param4") String param4, @RequestParam("param5") String param5,
			@RequestParam("param6") String param6, HttpSession session) throws ParseException {
		logger.info("Method : viewInitiatedAuditReport starts");
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);
		AuditReportModel[] auditMasterModel1 = restClient.postForObject(env.getAuditUrl() + "getAllAuditReportDtl",
				tableRequest, AuditReportModel[].class);
		List<AuditReportModel> auditList1 = Arrays.asList(auditMasterModel1);
		for (AuditReportModel m : auditList1) {

			AuditReportModel[] auditMasterModel = restClient.getForObject(
					env.getAuditUrl() + "getAllAuditReport?id=" + m.getAuditInitiate(), AuditReportModel[].class);
			List<AuditReportModel> auditList = Arrays.asList(auditMasterModel);
			for (AuditReportModel n : auditList) {
				n.setDocument(viewUploadedImage(n.getDocument(), n.getDocument()));
			}
			m.setDocuments(auditList);
		}

		model.addAttribute("auditReport", auditList1);
		logger.info("Method : viewInitiatedAuditReport ends");
		return "audit/audit-reports";
	}

	@GetMapping("/view-initiated-audit-documentAutoSearch-report")
	public String viewDocumentReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3,
			@RequestParam("param4") String param4, @RequestParam("param5") String param5,
			@RequestParam("param6") String param6, HttpSession session) throws ParseException {
		logger.info("Method : viewDocumentReport starts");
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam4(param4);
		tableRequest.setParam5(param5);
		tableRequest.setParam6(param6);

		DropDownModel[] auditMasterModel1 = restClient.postForObject(env.getAuditUrl() + "getAllDocumentReport",
				tableRequest, DropDownModel[].class);
		List<DropDownModel> auditList1 = Arrays.asList(auditMasterModel1);
		System.out.println("%%$$$$$^^^^^^^ " + auditList1);

		for (DropDownModel m : auditList1) {
			m.setDocName(m.getKey());
			m.setKey(viewUploadedImage(m.getKey(), m.getKey()));
			model.addAttribute("auditReport", auditList1);
		}

		logger.info("Method : viewDocumentReport ends");
		return "audit/audit-document-report";
	}

	/*
	 * Folder For Document view
	 */
	@GetMapping("/view-initiated-audit-documentAutoSearch-report-folder")
	public String viewDocumentFolder(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3,
			@RequestParam("param6") String param6, HttpSession session) throws ParseException {
		logger.info("Method : viewDocumentFolder starts");
		DataTableRequest tableRequest = new DataTableRequest();
		tableRequest.setParam1(param1);
		tableRequest.setParam2(param2);
		tableRequest.setParam3(param3);
		tableRequest.setParam6(param6);
		/*
		 * DropDownModel[] auditMasterModel1 = restClient
		 * .postForObject(env.getAuditUrl() + "getAllDocumentReport" ,tableRequest,
		 * DropDownModel[].class); List<DropDownModel> auditList1 =
		 * Arrays.asList(auditMasterModel1);
		 * 
		 * for(DropDownModel m:auditList1) { m.setDocName(m.getKey());
		 * m.setKey(viewUploadedImage(m.getKey(), m.getKey()));
		 * model.addAttribute("auditReport",auditList1); }
		 */

		model.addAttribute("param1", param1);
		model.addAttribute("param2", param2);
		model.addAttribute("param3", param3);
		model.addAttribute("param6", param6);

		// *********** folder type *************/
		try {
			DropDownModel[] folder = restClient.getForObject(env.getAuditUrl() + "getFolderName",
					DropDownModel[].class);
			List<DropDownModel> folderName = Arrays.asList(folder);

			model.addAttribute("folderNameList", folderName);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewDocumentFolder ends");
		return "audit/view-audit-folder";
	}

	/*
	 * For view work type for dataTable Ajax call
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-initiated-audit-ThroughAjax")
	public @ResponseBody DataTableResponse viewInitaiedAuditThroughAjax(Model model, HttpServletRequest request,
			@RequestParam("param1") String param1, @RequestParam("param2") String param2,
			@RequestParam("param3") String param3, HttpSession session) {

		logger.info("Method : viewInitaiedAuditThroughAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		
		/*
		 * String userrole = ""; try { userrole = (String)
		 * session.getAttribute("USER_ROLES"); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
		  
		 
		
		List<String> role = null;
		try {
			role = (List<String>) session.getAttribute("USER_ROLES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//List<Integer> list = Arrays.asList(1, 2, 3);
        String delim = ",";
 
        String userrole = Joiner.on(delim).join(role);
		
        System.out.println("userrole12345 "+userrole);
        
		try {
			List<String> userRole = (List<String>) session.getAttribute("USER_ROLES");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			String userId = (String) session.getAttribute("USER_ID");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setUserId(userId);
			tableRequest.setParam8(userrole);
			JsonResponse<List<AuditInitiateModel>> jsonResponse = new JsonResponse<List<AuditInitiateModel>>();
			
			System.out.println("tableRequest12345 "+tableRequest);
			
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
				byte[] auditType = Base64.getEncoder().encode(m.getAuditType().getBytes());
				byte[] initiatedDate = null;
				if (m.getInitiatedDate() != null) {
					initiatedDate = Base64.getEncoder().encode(m.getInitiatedDate().getBytes());
				}
				byte[] auditTypeId = Base64.getEncoder().encode(m.getAuditTypeId().getBytes());
				if (m.getApproveStatus().equals("0")) {
					m.setStatus("Open");
				} else if (m.getApproveStatus().equals("1")) {
					m.setStatus("Draft");
				} else if (m.getApproveStatus().equals("2")) {
					m.setStatus("Final");
				}
				if (userRole.contains("rol021")) {
					s = s + "<a title='Edit' href='view-initiated-audit-edit?id=" + new String(encodeId)
							+ "' ><i class=\"fa fa-edit faIcon\"></i></a>";
					if (m.getDtlStatus() == false) {
						s = s + " &nbsp;&nbsp;<a href='javascript:void(0)' onclick='deleteAudit(\""
								+ new String(encodeId) + "\")'><i class='fa fa-trash faIcon'></i></a>";

					}

					s = s + "&nbsp;&nbsp;<a  title='Manage Audit' href='view-initiated-audit-dtl?id="
							+ new String(encodeId) + "&auditType=" + new String(auditTypeId)
							+ "' ><i class=\"fa fa-eye faIcon\"></i></a>";

					/*
					 * s = s +
					 * "&nbsp;&nbsp;<a  title='Manage Update Date Status' href='view-initiated-audit-report-update-date?initiateId="
					 * + new String(encodeId) + "&auditType="+new
					 * String(auditType)+"&initiatedDate="+new
					 * String(initiatedDate)+"' ><i class=\"fa fa-calendar faIcon\"></i></a>";
					 */
				} else {
					s = s + "&nbsp;&nbsp;<a title='Manage Audit' href='view-initiated-audit-dtl?id="
							+ new String(encodeId) + "&auditType=" + new String(auditTypeId)
							+ "' ><i class=\"fa fa-eye faIcon\"></i></a>";

				}
				/*
				 * if (m.getReportStatus() != null) { if (m.getReportStatus() >= 1 &&
				 * !userRole.contains("rol024")) { s = s +
				 * "&nbsp;&nbsp;<a title='Audit Report' href='view-audit-draft-report-details?id="
				 * + new String(encodeId) +
				 * "' ><i class=\"fa fa-file-o faIcon\" aria-hidden=\"true\"></i></a>"; } }
				 */

				// if ( !userRole.contains("rol024")) {
				/*
				 * s = s +
				 * "&nbsp;&nbsp;<a title='Observation Report' href='view-initiated-audit-internal-audit-report-page?id="
				 * + new String(encodeId) +
				 * "' ><i class=\"fa fa-file-pdf-o faIcon\" aria-hidden=\"true\"></i></a>";
				 */
				// }

				/*
				 * if (userRole.contains("rol024")) {
				 * 
				 * s = s +
				 * "&nbsp;&nbsp;<a title='Manage Draft' href='view-audit-draft-report?id="+new
				 * String(auditTypeId) +"'><i class=\"fa fa-file faIcon\"></i></a>";
				 * 
				 * }
				 */

				/*
				 * if (!userRole.contains("rol024") && m.getApproveStatus().equals("2")) {
				 * 
				 * s = s +
				 * "&nbsp;&nbsp;<a  title='Manage Update Date Status' href='view-initiated-audit-report-update-date?initiateId="
				 * + new String(encodeId) + "&auditType="+new
				 * String(auditType)+"&initiatedDate="+new
				 * String(initiatedDate)+"' ><i class=\"fa fa-calendar faIcon\"></i></a>";
				 * 
				 * 
				 * }
				 */

				String imagePath = "";
				if (m.getDocument() != null && m.getDocument() != "") {
					String[] extension = m.getDocument().split("\\.");
					if (extension.length == 2) {
						if (extension[1].contains("doc") || extension[1].contains("docx")) {
							imagePath = "/document/audit/excel/" + m.getDocument();
						} else if (extension[1].contains("xls") || extension[1].contains("xlsx")) {
							imagePath = "/document/audit/excel/" + m.getDocument();
						} else {
							imagePath = "/document/audit/" + m.getDocument();
						}
					} else {
						m.setDocument("N/A");
					}
				} else {
					m.setDocument("N/A");
				}
				m.setDocument("<a href='" + imagePath + "'target='_blank' >" + m.getDocument() + "</a>");

				/*
				 * m.setAuditInitiate("<a href='view-initiated-audit-dtl?id=" + new
				 * String(encodeId) + "' >" + m.getAuditInitiate() + "</a>");
				 */

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

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-audit-master-saveSummery" })
	public @ResponseBody JsonResponse<Object> saveSummery(Model model, @RequestBody String summery,
			BindingResult result) {
		logger.info("Method : saveSummery starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveSummery", summery, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() == "") {
			res.setMessage("success");

		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : saveSummery ends");
		return res;
	}

	/**
	 * get Auditor
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-audit-master-getAuditor" })
	public @ResponseBody JsonResponse<List<AuditInitiateModel>> getAuditor(Model model,
			@RequestParam("organization") String organization, HttpSession session) {
		logger.info("Method : getAuditor starts");
		JsonResponse<List<AuditInitiateModel>> res = new JsonResponse<List<AuditInitiateModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getAuditor?organization=" + organization,
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
		return res;
	}

	/**
	 * get Auditor
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "initiate-audit-getDesignation" })
	public @ResponseBody JsonResponse<List<AuditInitiateModel>> getDesignation(Model model,
			@RequestParam("auditor") String auditor, HttpSession session) {
		logger.info("Method : getDesignation starts");
		JsonResponse<List<AuditInitiateModel>> res = new JsonResponse<List<AuditInitiateModel>>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getDesignation?auditor=" + auditor, JsonResponse.class);
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
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "add-audit-master-getSummery" })
	public @ResponseBody JsonResponse<DropDownModel> getSummery(Model model, HttpSession session) {
		logger.info("Method : getSummery starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "getSummery", JsonResponse.class);

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
		return res;
	}

	/*
	 * auditor details
	 *
	 *
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping(value = { "view-initiated-audit-details" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> sendDetails(Model model, @RequestBody List<AuditInitiateModel> obj,
			BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : sendDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = (String) session.getAttribute("USER_ID");
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";

		try {

			int imagecount = 0;
			for (AuditInitiateModel a : obj) {
				a.setCreatedBy(userId);

				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

			resp = restClient.postForObject(env.getAuditUrl() + "sendDetails", obj, JsonResponse.class);
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
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();
			String message1 = obj.get(0).getSummary();
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
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		logger.info("Method : sendDetails ends");
		return resp;
	}

	/**
	 * Web Controller - Upload Document
	 *
	 */
	@PostMapping("/view-initiated-audit-uploadFile")
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
	@RequestMapping(value = "/view-initiated-audit-viewDetails", method = { RequestMethod.GET })
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

		logger.info("Method : viewDetails function Ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-saveNotes" })
	public @ResponseBody JsonResponse<Object> saveNotes(Model model, @RequestBody AuditInitiateModel obj,
			BindingResult result) {
		logger.info("Method : saveNotes starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "saveNotes", obj, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : saveNotes ends");
		// System.out.println(res);
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-initiated-audit-viewNotification" })
	public @ResponseBody JsonResponse<Object> viewNotification(Model model, @RequestParam("id") String id,
			@RequestParam("type") String type, HttpSession session) {
		logger.info("Method : viewNotification starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = (String) session.getAttribute("USER_ID");
		try {
			res = restClient.getForObject(
					env.getAuditUrl() + "viewNotification?id=" + id + "&type=" + type + "&userId=" + userId,
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
		logger.info("Method : viewNotification ends");
		return res;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-getNotes" })
	public @ResponseBody JsonResponse<DropDownModel> getNotes(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : saveNotes starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.postForObject(env.getAuditUrl() + "getNotes", id, JsonResponse.class);

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

	/*
	 * for Edit
	 */
	@GetMapping("/view-initiated-audit-edit")
	public String editInitiateAudit(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editInitiateAudit starts");
		
		try {
			DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
			List<DropDownModel> programList = Arrays.asList(program);
			model.addAttribute("empList", programList);

		} catch (RestClientException e) {
			e.printStackTrace();
		} 

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			AuditMasterModel[] auditMasterModel = restClient
					.getForObject(env.getAuditUrl() + "getinitiateAuditById?id=" + id, AuditMasterModel[].class);
			List<AuditMasterModel> auditList = Arrays.asList(auditMasterModel);

			AuditAdditionalInfoModel[] personListModel = restClient.getForObject(
					env.getAuditUrl() + "getAdditionalInformation?id=" + id, AuditAdditionalInfoModel[].class);
			List<AuditAdditionalInfoModel> AdditionalInformation = Arrays.asList(personListModel);

			for (AuditAdditionalInfoModel a : AdditionalInformation) {
				a.setHiddenDoc(a.getDoc());
				a.setDoc(viewUploadedImage(a.getDoc(), a.getDocName()));
			}

			AuditAdditionalInfoModel[] auditDocs = restClient.getForObject(env.getAuditUrl() + "getAuditDocs?id=" + id,
					AuditAdditionalInfoModel[].class);
			List<AuditAdditionalInfoModel> AdditionalDocument = Arrays.asList(auditDocs);
			for (AuditAdditionalInfoModel a : AdditionalDocument) {
				a.setHiddenDoc(a.getDoc());
				a.setDoc(viewUploadedImage(a.getDoc(), a.getDocName()));
			}

			List<Object> deptList = new ArrayList<Object>();
			List<String> selectedDeptList = new ArrayList<String>();
			List<Object> secList = new ArrayList<Object>();
			List<String> selectedDeptHeadList = new ArrayList<String>();

			// List<Object> subCoordinatorList = new ArrayList<Object>();

			// List<Object> subCoordinatorList = new ArrayList<Object>();
			/**
			 * AUDIT NO
			 *
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear",
						DropDownModel[].class);
				List<DropDownModel> financialYearList = Arrays.asList(audit);

				model.addAttribute("financialYearList", financialYearList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			for (AuditMasterModel m : auditList) {

				// List<Object> concernedAuditee = new ArrayList<Object>();
				// List<String> SelectedConceAuditee = new ArrayList<String>();
				// List<String> subCoAudiList = new ArrayList<String>();

				/** Department **/
				try {
					DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getDeptList",
							DropDownModel[].class);
					List<DropDownModel> listDept = Arrays.asList(dept);
					deptList.addAll(listDept);
				} catch (RestClientException e) {
					e.printStackTrace();
				} /** Department **/

				/** Department Head **/

				/*
				 * try { DropDownModel[] head = restClient.getForObject( env.getAuditUrl() +
				 * "getDepartmentHeadList?id=" + m.getSection(), DropDownModel[].class);
				 * List<DropDownModel> headList = Arrays.asList(head);
				 * deptHeadList.addAll(headList);
				 * 
				 * } catch (RestClientException e) { e.printStackTrace(); }
				 */

				/** Concerned Auditee **/
				/*
				 * try { DropDownModel[] auditee = restClient.getForObject( env.getAuditUrl() +
				 * "getConcernAuditList?id=" + m.getSection(), DropDownModel[].class);
				 * List<DropDownModel> auditeeList = Arrays.asList(auditee);
				 * concernedAuditee.add(auditeeList); m.setEditConcernedAuditee(auditeeList); //
				 * System.out.println("concernedAuditee"+concernedAuditee); } catch
				 * (RestClientException e) { e.printStackTrace(); }
				 */
				/** Selected Concerned Auditee **/

				/*
				 * try { DropDownModel[] SelAuditee = restClient.getForObject( env.getAuditUrl()
				 * + "getSelectedConcernAuditList?id=" + m.getAuditAutoGenId(),
				 * DropDownModel[].class); List<DropDownModel> selAuditeeList =
				 * Arrays.asList(SelAuditee); //
				 * System.out.println("selAuditeeList"+selAuditeeList);
				 * SelectedConceAuditee.addAll(selAuditeeList); for (DropDownModel p :
				 * selAuditeeList) { SelectedConceAuditee.add(p.getKey()); }
				 * //System.out.println(SelectedConceAuditee);
				 * m.setFileArray(SelectedConceAuditee); } catch (RestClientException e) {
				 * e.printStackTrace(); }
				 */

				/** Sub Coordinator **/
				/*
				 * try { DropDownModel[] sub = restClient.getForObject(env.getAuditUrl() +
				 * "getSubCoordList", DropDownModel[].class); List<DropDownModel>
				 * listSubCoordinator = Arrays.asList(sub);
				 * subCoordinatorList.addAll(listSubCoordinator);
				 * 
				 * } catch (RestClientException e) { e.printStackTrace(); }
				 */
				/** Sub Coordinator **/
				/*
				 * try {
				 * 
				 * DropDownModel[] sub = restClient.getForObject( env.getAuditUrl() +
				 * "getSelectedSubCoordList?id=" + m.getAuditAutoGenId(),
				 * DropDownModel[].class); List<DropDownModel> selectedSubCo =
				 * Arrays.asList(sub);
				 * 
				 * for (DropDownModel p : selectedSubCo) { subCoAudiList.add(p.getKey()); }
				 * m.setDocumentName(subCoAudiList); } catch (RestClientException e) {
				 * e.printStackTrace(); }
				 */

			}
			// model.addAttribute("SelectedConceAuditee", SelectedConceAuditee);
			model.addAttribute("deptList", deptList);

			// model.addAttribute("deptHeadList", deptHeadList);

			// model.addAttribute("concernedAuditee", concernedAuditee);

			// model.addAttribute("subCoordinatorList", subCoordinatorList);
			// model.addAttribute("subCoAudiList", subCoAudiList);
			model.addAttribute("AdditionalInformation", AdditionalInformation);
			model.addAttribute("AdditionalDocument", AdditionalDocument);

			/**
			 * AUDIT TYPE DROP DOWN
			 *
			 */
			try {
				DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getAuditType",
						DropDownModel[].class);
				List<DropDownModel> auditTypeList = Arrays.asList(audit);

				model.addAttribute("auditTypeList", auditTypeList);

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
			try {
				DropDownModel[] dept = restClient.getForObject(
						env.getAuditUrl() + "getSectionList?id=" + auditList.get(0).getDepartment(),
						DropDownModel[].class);
				List<DropDownModel> listSec = Arrays.asList(dept);
				secList.addAll(listSec);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			try {
				DropDownModel[] cc = restClient.getForObject(
						env.getAuditUrl() + "getCCList?id=" + id,
						DropDownModel[].class);
				List<DropDownModel> ccList = Arrays.asList(cc);
				List<String> ccdata = new ArrayList<String>();
				for(DropDownModel m : ccList) {
					ccdata.add(m.getKey());
				}
				
				model.addAttribute("ccdata", ccdata);
				
			} catch (RestClientException e) {
				e.printStackTrace();
			}

			try {
				DropDownModel[] SelDept = restClient.getForObject(
						env.getAuditUrl() + "getSelectedDeptList?id=" + auditList.get(0).getAuditInitiated(),
						DropDownModel[].class);
				List<DropDownModel> SelDeptList = Arrays.asList(SelDept);
				for (DropDownModel p : SelDeptList) {
					selectedDeptList.add(p.getKey());
				}
				auditList.get(0).setFileArray(selectedDeptList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String listProduct = "";
			for (String str : auditList.get(0).getFileArray()) {
				str = str.replace("[", "").replace("]", "");
				listProduct = listProduct + "'" + str + "',";
			}

			listProduct = listProduct.substring(0, listProduct.length() - 1);
			DropDownModel[] departHeadList = restClient
					.getForObject(env.getAuditUrl() + "getDeptHeadList?id=" + listProduct, DropDownModel[].class);
			List<DropDownModel> departHeadDataList = Arrays.asList(departHeadList);

			DropDownModel[] selDepartHeadList = restClient.getForObject(
					env.getAuditUrl() + "getSelectedDeptHeadList?id=" + auditList.get(0).getAuditInitiated(),
					DropDownModel[].class);
			List<DropDownModel> selDepartHeadListData = Arrays.asList(selDepartHeadList);
			for (DropDownModel p : selDepartHeadListData) {
				selectedDeptHeadList.add(p.getKey());
			}
			model.addAttribute("selectedDeptHeadList", selectedDeptHeadList);
			auditList.get(0).setSubcoordinator(selectedDeptHeadList);
			;
System.out.println("Bikash 1 2345 "+auditList.get(0));
			model.addAttribute("secList", secList);
			// System.out.println("secList"+secList);
			model.addAttribute("departHeadDataList", departHeadDataList);
			model.addAttribute("initiateAudit", auditList.get(0));
			// System.out.println("initiateAudit"+auditList.get(0));

			model.addAttribute("edit", auditList.get(0).getAuditInitiated());

		} catch (RestClientException e) {

			e.printStackTrace();
		}
		try {
			DropDownModel[] org = restClient.getForObject(env.getAuditUrl() + "getRegion", DropDownModel[].class);
			List<DropDownModel> regionList = Arrays.asList(org);
			model.addAttribute("regionList", regionList);
//System.out.println("regionList"+regionList);
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

		logger.info("Method : editInitiateAudit ends");

		return "audit/add-audit-master";

	}

	/*
	 * Get Mapping view initiated audit report page
	 */

	@GetMapping("/view-initiated-audit-internal-audit-report-page")
	public String viewInitiatedAuditReportPage(@RequestParam("id") String id, Model model) {
		logger.info("Method : viewInitiatedAuditReportPage starts");

		byte[] array = Base64.getDecoder().decode(id.getBytes());
		String decodedId = new String(array);
		List<String> buttons = new ArrayList<String>();
		List<String> regions = new ArrayList<String>();
		AuditInitiateModel[] auditeeList = restClient.getForObject(env.getAuditUrl() + "getAudits?id=" + decodedId,
				AuditInitiateModel[].class);
		List<AuditInitiateModel> auditList = Arrays.asList(auditeeList);
		for (AuditInitiateModel m : auditList) {
			String button = "";
			String region = "";

			button = "view-initiated-audit-internal-audit-report-download?section=" + m.getFinancialYear()
					+ "&department=" + m.getAuditType() + "&auditId=" + m.getAudit() + "&type=" + "A" + "&auditType="
					+ m.getAuditTypeId();
			region = "(" + m.getInitiatedBy() + ")";

			buttons.add(button);
			regions.add(region);
			// System.out.println(m);

		}

		model.addAttribute("buttons", buttons);
		model.addAttribute("regions", regions);
		// System.out.println("regions"+regions);
		// System.out.println("buttons"+buttons);
		model.addAttribute("AuditType", auditList.get(0).getInitiatedDate());
		logger.info("Method : viewInitiatedAuditReportPage ends");
		return "audit/view-initiate-audit-report-page";
	}

	/*
	 * internalAudit Report Page Render
	 */

	@GetMapping("view-initiated-audit-internal-audit-report")
	public String internalAuditReportPage(Model model, @RequestParam("id") Integer id,
			@RequestParam("department") String department, @RequestParam("section") String section, HttpSession session,
			HttpServletResponse response) {
		logger.info("internalAuditReportPage sterts");

		String userId = (String) session.getAttribute("USER_ID");
		List<AuditObservationModel> questions = new ArrayList<AuditObservationModel>();
		model.addAttribute("auditId", id);
		model.addAttribute("section", section);
		model.addAttribute("department", department);

		try {

			AuditObservationModel[] res = restClient.getForObject(env.getAuditUrl() + "getQuestionList?userid=" + userId
					+ "&auditId=" + id + "&department=" + department + "&section=" + section,
					AuditObservationModel[].class);
			questions = Arrays.asList(res);

			for (AuditObservationModel a : questions) {

				if (a.getSeverity().equals("1")) {
					a.setSeverityType("Blocker");
				} else if (a.getSeverity().equals("2")) {
					a.setSeverityType("High");

				} else if (a.getSeverity().equals("3")) {
					a.setSeverityType("Medium");

				} else if (a.getSeverity().equals("4")) {
					a.setSeverityType("Low");
				}

				if (a.getObsType().equals("1")) {
					a.setObsTypeName("Improvement");
				}
				if (a.getObsType().equals("2")) {
					a.setObsTypeName("POM");
				}
				if (a.getObsType().equals("3")) {
					a.setObsTypeName("NC");
				}

				if (a.getDate() != null) {
					String[] arr = a.getDate().split(" ");
					a.setDate(arr[0]);
					a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
				}

				String docPath = "<a href=\"/document/audit/excel/" + a.getQusDoc()
						+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title=\"document.xl\"></i> </a>";
				a.setDocument(docPath);

				try {
					AuditComplianceModel[] comments = restClient.getForObject(
							env.getAuditUrl() + "getCommentList?id=" + a.getQuesId() + "&userId=" + userId,
							AuditComplianceModel[].class);

					List<AuditComplianceModel> compliances = Arrays.asList(comments);
					for (AuditComplianceModel c : compliances) {
						if (c.getDocument() != null && c.getDocument() != "") {
							if (c.getDocument().split("\\.")[1].equals("xls")
									|| c.getDocument().split("\\.")[1].equals("xlsx")) {
								String str = "<a href=\"/document/audit/excel/" + c.getDocument()
										+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title=\"" + c.getDocument()
										+ "\"></i></a>";
								c.setDocument(str);
							}
						}
					}
					a.setCompliances(compliances);
				} catch (Exception e) {
					e.printStackTrace();
				}

				RequisitionViewModel[] requisitionViewModel = restClient.getForObject(
						env.getAuditUrl() + "getCoObsForwardDetails?id=" + a.getAuditId() + "&userId=" + userId,
						RequisitionViewModel[].class);
				List<RequisitionViewModel> forwardDetails = Arrays.asList(requisitionViewModel);
				for (RequisitionViewModel dtl : forwardDetails) {

					DropDownModel[] dropDownModel1 = restClient.getForObject(
							env.getAuditUrl() + "getObsForDocList?id=" + dtl.getReqId(), DropDownModel[].class);
					List<DropDownModel> docList = Arrays.asList(dropDownModel1);
					for (DropDownModel doc : docList) {
						doc.setKey(viewUploadedImage(doc.getKey(), doc.getName()));
					}
					dtl.setDocumentList(docList);
					if (dtl.getCreatedOn() != null) {
						String[] arr = dtl.getCreatedOn().split(" ");
						dtl.setDate(arr[0]);
						dtl.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
					}
				}
				a.setForwardDetails(forwardDetails);

			}
			model.addAttribute("questions", questions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/************************************
		 * Deviation Question List
		 ***********************************************************/
		/* Question List */
		AuditObservationModel[] res1 = restClient.getForObject(env.getAuditUrl() + "getDeviationQuestionList?id=" + id
				+ "&deptId=" + department + "&sectionId=" + section + "&userId=" + userId,
				AuditObservationModel[].class);
		List<AuditObservationModel> DevQuestions = Arrays.asList(res1);

		for (AuditObservationModel a : DevQuestions) {

			if (a.getSeverity().equals("1")) {
				a.setSeverityType("Blocker");
			} else if (a.getSeverity().equals("2")) {
				a.setSeverityType("High");

			} else if (a.getSeverity().equals("3")) {
				a.setSeverityType("Medium");

			} else if (a.getSeverity().equals("4")) {
				a.setSeverityType("Low");
			}

			if (a.getObsType().equals("1")) {
				a.setObsTypeName("Improvement");
			}
			if (a.getObsType().equals("2")) {
				a.setObsTypeName("POM");
			}
			if (a.getObsType().equals("3")) {
				a.setObsTypeName("NC");
			}

			if (a.getDate() != null) {
				String[] arr = a.getDate().split(" ");
				a.setDate(arr[0]);
				a.setTime(arr[1].split(":")[0] + ":" + arr[1].split(":")[1]);
			}
			a.setDocument(viewUploadedImage(a.getDocument(), a.getDocument()));

			try {
				AuditComplianceModel[] comments = restClient.getForObject(
						env.getAuditUrl() + "getCommentList?id=" + a.getQuesId() + "&userId=" + userId,
						AuditComplianceModel[].class);

				List<AuditComplianceModel> compliances = Arrays.asList(comments);
				for (AuditComplianceModel c : compliances) {
					if (c.getDocument() != null && c.getDocument() != "") {
						if (c.getDocument().split("\\.")[1].equals("xls")
								|| c.getDocument().split("\\.")[1].equals("xlsx")) {
							String str = "<a href=\"\\fileupload\\audit\\excel\\" + c.getDocument()
									+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title=\"" + c.getDocument()
									+ "\"></i></a>";
							c.setDocument(str);
						}
					}
				}

				a.setCompliances(compliances);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		model.addAttribute("DevQuestions", DevQuestions);
		if (questions.size() > 0) {
			model.addAttribute("auditType", questions.get(0).getAuditType());
		}

		logger.info("internalAuditReportPage ends");
		return "audit/InternalAuditReort";
	}

	/*
	 * auditor details
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-meeting" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> meetingDetails(Model model, @RequestBody List<AuditMeetingModel> obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : meetingDetails starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String userId = (String) session.getAttribute("USER_ID");
		String userName = (String) session.getAttribute("USER_NAME");
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";

		try {

			for (AuditMeetingModel a : obj) {
				a.setCreatedBy(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

			resp = restClient.postForObject(env.getAuditUrl() + "meetingDetails", obj, JsonResponse.class);
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
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();
			String message1 = obj.get(0).getComments();
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
				}
				 System.out.println("Email Sent");
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Email Not Sent");
			}

		}
		logger.info("Method : meetingDetails ends");
		return resp;
	}
	
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-meeting-details-after-attend" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> meetingDetailsAfterAttend(Model model, @RequestBody List<AuditMeetingModel> obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : meetingDetailsAfterAttend starts");
		
		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = (String) session.getAttribute("USER_ID");
		String userName = (String) session.getAttribute("USER_NAME");
		String emalstatus = (String) session.getAttribute("MAILSTATUS");
		String emailvalue = "1";
		
		try {
			
			for (AuditMeetingModel a : obj) {
				a.setCreatedBy(userId);
				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}
			
			resp = restClient.postForObject(env.getAuditUrl() + "meetingDetailsAfterAttend", obj, JsonResponse.class);
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
			String subject = "From :-" + userName + System.lineSeparator() + "Subject:-" + obj.get(0).getSubject();
			String message1 = obj.get(0).getComments();
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
				}
				System.out.println("Email Sent");
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Email Not Sent");
			}
			
		}
		logger.info("Method : meetingDetailsAfterAttend ends");
		return resp;
	}

	/**
	 * saveSummery
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-documentAutoSearch" })
	public @ResponseBody JsonResponse<DropDownModel> documentAutoSearch(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : documentAutoSearch starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "documentAutoSearch?id=" + id, JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() == "" || res.getMessage() == null) {
			res.setMessage("success");

		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : documentAutoSearch ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-document-report" })
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

	/*
	 * Delete Item
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-initiated-audit-delete" })
	public @ResponseBody JsonResponse<Object> deleteAudit(Model model, @RequestParam("id") String encodeId,
			HttpSession session) {

		logger.info("Method : deleteAudit starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {

		}
		;
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			resp = restClient.getForObject(env.getAuditUrl() + "delete-audit?id=" + id + "&createdBy=" + userId,
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
		logger.info("Method : deleteAudit ends");
		return resp;
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-upload-documents")
	public String uploadDocuments(Model model, @RequestParam("id") String id, HttpSession session) {
		logger.info("Method : uploadDocuments starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
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

		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYearList = Arrays.asList(audit);

			model.addAttribute("financialYearList", financialYearList);

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
		// *********** folder type *************/
		try {
			DropDownModel[] folder = restClient.getForObject(env.getAuditUrl() + "getFolderName",
					DropDownModel[].class);
			List<DropDownModel> folderName = Arrays.asList(folder);

			model.addAttribute("folderName", folderName);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		model.addAttribute("auditType", id1);
		logger.info("Method : uploadDocuments ends");
		return "audit/documentUpload";
	}

	/*
	 * auditor details
	 *
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-upload-document" }, produces = "application/json")
	public @ResponseBody JsonResponse<Object> uploadDocument(Model model, @RequestBody List<RequisitionViewModel> obj,
			BindingResult result, HttpSession session) throws IOException {
		logger.info("Method : uploadDocument starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
		}
		try {

			for (RequisitionViewModel a : obj) {
				a.setCreatedby(userId);

				a.setDocument(uploadPhoto(a.getDocument(), a.getFile()));
			}

			resp = restClient.postForObject(env.getAuditUrl() + "uploadDocument", obj, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : uploadDocument ends");
		return resp;
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-draft-report-search")
	public String DraftReportSearch(Model model, HttpSession session) {
		logger.info("Method : DraftReportSearch starts");
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

		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYearList = Arrays.asList(audit);

			model.addAttribute("financialYearList", financialYearList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : DraftReportSearch ends");
		return "audit/auditDraftReportView";
	}

	@GetMapping("/view-initiated-audit-draftReportSearch-report")
	public String viewDraftFinalReport(Model model, @RequestParam("param1") String param1,
			@RequestParam("param2") String param2, @RequestParam("param3") String param3, HttpSession session)
			throws ParseException {
		logger.info("Method : viewDocumentReport starts");

		/**
		 * AUDIT NO
		 *
		 */
		try {
			DropDownModel[] audit = restClient.getForObject(
					env.getAuditUrl() + "getAllDraftNo?id=" + param1 + "&id2=" + param2 + "&id3=" + param3,
					DropDownModel[].class);
			List<DropDownModel> draftNO = Arrays.asList(audit);
			for (DropDownModel m : draftNO) {
				m.setName(m.getKey());
				byte[] encodeId = Base64.getEncoder().encode(m.getKey().getBytes());
				m.setKey(new String(encodeId));
			}
			model.addAttribute("draftNO", draftNO);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : viewDocumentReport ends");
		return "audit/draft-finalReport";
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-auditPending")
	public String viewInitiatedAuditAuditPending(Model model, @RequestParam("id") String auditType,
			HttpSession session) {
		logger.info("Method : viewInitiatedAuditAuditPending starts");
		byte[] encodeByte = Base64.getDecoder().decode(auditType.getBytes());
		String id = (new String(encodeByte));
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date dateobj = new Date();
		df.format(dateobj);
		Calendar calobj = Calendar.getInstance();
		df.format(calobj.getTime());
		model.addAttribute("currentDate", df);
		model.addAttribute("auditType", id);
		logger.info("Method : viewInitiatedAuditAuditPending ends");
		return "audit/view-initiate-audit";
	}
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "view-initiated-audit-email-password" })
	 * public @ResponseBody JsonResponse<Object> postEmailPassword(Model
	 * model, @RequestBody PersonModel obj, BindingResult result, HttpSession
	 * session) { logger.info("Method : postEmailPassword starts");
	 * 
	 * JsonResponse<Object> res = new JsonResponse<Object>();
	 * 
	 * byte[] decodeId = Base64.getDecoder().decode(obj.getValue().getBytes());
	 * String id = (new String(decodeId));
	 * 
	 * String userId = ""; try { userId = (String) session.getAttribute("USER_ID");
	 * } catch (RestClientException e) { e.printStackTrace(); } //String
	 * originalString = "howtodoinjava.com";
	 * 
	 * 
	 * String password = "";
	 * 
	 * password = obj.getName(); String encryptedString ="" ;
	 * 
	 * 
	 * if (password != null && password != "") { encryptedString =
	 * Constant.encrypt(password, secretKey) ; } try { res =
	 * restClient.getForObject( env.getAuditUrl() + "postEmailPassword?id=" + id +
	 * "&password=" + encryptedString+"&host="+obj.getLabel(), JsonResponse.class);
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * if (res.getMessage() != null) { res.setCode(res.getMessage());
	 * res.setMessage("Unsuccess"); } else { res.setMessage("success");
	 * if(userId.contentEquals(id)) { session.setAttribute("USER_PASSWORD",
	 * password); } }
	 * 
	 * logger.info("Method : postEmailPassword ends"); return res; }
	 */
	/*
	 * public String uploadPhoto(String document, List<String> file) { if (document
	 * != null && document != "") { String delimiters = "\\."; String[] x =
	 * document.split(delimiters); if (x[1].matches("png") || x[1].matches("jpg") ||
	 * x[1].matches("jpeg")) { for (String s1 : file) {
	 * 
	 * try {
	 * 
	 * byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1); String
	 * imageName = saveAllImage(bytes); document = imageName; } catch (Exception e)
	 * { e.printStackTrace(); } } } else if (x[1].matches("pdf")) { for (String s1 :
	 * file) { try {
	 * 
	 * byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1); String pdfName
	 * = saveAllPdf(bytes); document = pdfName; } catch (Exception e) {
	 * e.printStackTrace(); } } } else if (x[1].matches("docx")) { for (String s1 :
	 * file) { try { byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
	 * String pdfName = saveAllDocx(bytes); document = pdfName; } catch (Exception
	 * e) { e.printStackTrace(); } } } else if (x[1].matches("doc")) { for (String
	 * s1 : file) { try { byte[] bytes = new
	 * sun.misc.BASE64Decoder().decodeBuffer(s1); String pdfName =
	 * saveAllDoc(bytes); document = pdfName; } catch (Exception e) {
	 * e.printStackTrace(); } } } else if (x[1].matches("xls")) { for (String s1 :
	 * file) { try { byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
	 * String pdfName = saveAllXls(bytes); document = pdfName; } catch (Exception e)
	 * { e.printStackTrace(); } } } else if (x[1].matches("xlsx")) { for (String s1
	 * : file) { try { byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
	 * String pdfName = saveAllXlsx(bytes); document = pdfName; } catch (Exception
	 * e) { e.printStackTrace(); } } } } return document; }
	 */

	public String uploadPhoto(String document, List<String> file) {
		/* bikash changes */
		String documentname = document.split("\\.")[0];
		System.out.println("documentname " + documentname);
		if (document != null && document != "") {
			String delimiters = "\\.";
			String[] x = document.split(delimiters);
			System.out.println("x delimiter" + Arrays.toString(x));
			if (x[1].matches("png") || x[1].matches("jpg") || x[1].matches("jpeg")) {
				System.out.println("in png ");
				for (String s1 : file) {
					System.out.println("in png 123 ");
					try {

						byte[] bytes = new sun.misc.BASE64Decoder().decodeBuffer(s1);
						String imageName = saveAllImage(documentname, bytes);
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
		System.out.println("document123 " + document);
		return document;
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

	/*
	 * public String saveAllImage(byte[] imageBytes) {
	 * logger.info("Method : saveAllImage starts");
	 * 
	 * String imageName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime();
	 * imageName = nowTime + ".png"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + imageName); if (imageBytes
	 * != null) { Files.write(path, imageBytes);
	 * 
	 * ByteArrayInputStream in = new ByteArrayInputStream(imageBytes); Integer
	 * height = 50; Integer width = 50;
	 * 
	 * try { BufferedImage img = ImageIO.read(in); if (height == 0) { height =
	 * (width * img.getHeight()) / img.getWidth(); } if (width == 0) { width =
	 * (height * img.getWidth()) / img.getHeight(); }
	 * 
	 * Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	 * 
	 * BufferedImage imageBuff = new BufferedImage(width, height,
	 * BufferedImage.TYPE_INT_ARGB); imageBuff.getGraphics().drawImage(scaledImage,
	 * 0, 0, new Color(0, 0, 0), null);
	 * 
	 * ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	 * 
	 * ImageIO.write(imageBuff, "png", buffer);
	 * 
	 * byte[] thumb = buffer.toByteArray();
	 * 
	 * Path pathThumb = Paths.get(env.getFileUploadAudit() + "thumb/" + imageName);
	 * Files.write(pathThumb, thumb);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * logger.info("Method : saveAllImage ends"); return imageName; }
	 * 
	 * 
	 * for save all pdf in folder and return name
	 * 
	 * 
	 * public String saveAllPdf(byte[] imageBytes) {
	 * logger.info("Method : saveAllPdf starts");
	 * 
	 * String pdfName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime(); pdfName
	 * = nowTime + ".pdf"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + pdfName);
	 * //System.out.println("path " + path); if (imageBytes != null) {
	 * Files.write(path, imageBytes); } } catch (Exception e) { e.printStackTrace();
	 * }
	 * 
	 * logger.info("Method : saveAllPdf ends"); return pdfName; }
	 * 
	 * public String saveAllDocx(byte[] imageBytes) {
	 * logger.info("Method : saveAllDocx starts");
	 * 
	 * String pdfName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime(); pdfName
	 * = nowTime + ".docx"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + pdfName);
	 * //System.out.println("path" + path); if (imageBytes != null) {
	 * Files.write(path, imageBytes); } } catch (Exception e) { e.printStackTrace();
	 * }
	 * 
	 * logger.info("Method : saveAllDocx ends"); return pdfName; }
	 * 
	 * public String saveAllDoc(byte[] imageBytes) {
	 * logger.info("Method : saveAllDoc starts");
	 * 
	 * String pdfName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime(); pdfName
	 * = nowTime + ".doc"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + pdfName);
	 * //System.out.println("path" + path); if (imageBytes != null) {
	 * Files.write(path, imageBytes); } } catch (Exception e) { e.printStackTrace();
	 * }
	 * 
	 * logger.info("Method : saveAllDoc ends"); return pdfName; }
	 * 
	 * public String saveAllXlsx(byte[] imageBytes) {
	 * logger.info("Method : saveAllDoc starts");
	 * 
	 * String pdfName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime(); pdfName
	 * = nowTime + ".xlsx"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + pdfName);
	 * //System.out.println("path" + path); if (imageBytes != null) {
	 * Files.write(path, imageBytes); } } catch (Exception e) { e.printStackTrace();
	 * }
	 * 
	 * logger.info("Method : saveAllDoc ends"); return pdfName; }
	 * 
	 * public String saveAllXls(byte[] imageBytes) {
	 * logger.info("Method : saveAllDoc starts");
	 * 
	 * String pdfName = null;
	 * 
	 * try { if (imageBytes != null) { long nowTime = new Date().getTime(); pdfName
	 * = nowTime + ".xls"; }
	 * 
	 * Path path = Paths.get(env.getFileUploadAudit() + pdfName);
	 * //System.out.println("path" + path); if (imageBytes != null) {
	 * Files.write(path, imageBytes); } } catch (Exception e) { e.printStackTrace();
	 * }
	 * 
	 * logger.info("Method : saveAllDoc ends"); return pdfName; }
	 */
	public String viewUploadedImage(String document, String documentName) {
		String Document = "";
		if (document != null && document != "") {

			String[] x = document.split("\\.");
			if (x.length > 1) {
				if (x[1].equals("xls") || x[1].equals("xlsx")) {

					String docPath = "<a href=\"/document/audit/excel/" + document
							+ "\" target='_balnk'><i class=\"fa fa-file-excel-o fa-2x excel\" title='" + documentName
							+ "'></i> </a>";

					Document = docPath;
				}
			}
			if (x.length > 1) {
				if (x[1].equals("pdf")) {
					String docPath = "<a  href=\"/document/audit/excel/" + document
							+ "\" target='_balnk'><i class=\"fa fa-file-pdf-o fa-2x  pdf\" title='" + documentName
							+ "'></i> </a>";

					Document = docPath;
				}
			}
			if (x.length > 1) {
				if (x[1].equals("docx")) {
					String docPath = "<a href=\"/document/audit/excel/" + document
							+ "\" target='_balnk'><i class=\"fa fa-file-word-o  fa-2x word\" aria-hidden=\"true\" title='"
							+ documentName + "'></i> </a>";
					Document = docPath;
				}
			}
			if (x.length > 1) {
				if (x[1].equals("jpg") || x[1].equals("png")) {
					String docPath = "<a href=\"/document/audit/excel/" + document
							+ "\" target='_balnk'><i class=\"fa fa-file-image-o  fa-2x word\" aria-hidden=\"true\" title='"
							+ documentName + "'></i> </a>";
					Document = docPath;
				}
			}
		}
		return Document;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-initiated-audit-getAuditNameAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getAuditNameList(Model model,
			@RequestBody DropDownModel searchValue, BindingResult result) {
		logger.info("Method : getAuditNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "getAuditNameListByAutoSearch?id=" + searchValue.getKey()
					+ "&auditTypeId=" + searchValue.getName(), JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getAuditNameList ends");
		return res;
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-inprogress")
	public String viewInitiatedAuditInprogress(Model model, @RequestParam("id") String auditType, HttpSession session) {
		logger.info("Method : viewInitiatedAudit starts");
		byte[] encodeByte = Base64.getDecoder().decode(auditType.getBytes());
		String id = (new String(encodeByte));

		/*
		 * String value9=""; value9=(String) session.getAttribute("USER_ID");
		 * System.out.println("dfdfdfdfdf"+value9); byte[] encodeBytes =
		 * Base64.getDecoder().decode(value9.getBytes()); String id34 = (new
		 * String(encodeBytes));
		 */

		model.addAttribute("auditType", id);
		logger.info("Method : viewInitiatedAudit ends");
		return "audit/view-initiate-audit";
	}

	/*
	 * Get Mapping view initiated audit
	 */
	@GetMapping("/view-initiated-audit-inprogresscag")
	public String viewInitiatedAuditInprogressCAG(Model model, @RequestParam String id, HttpSession session) {
		logger.info("Method : viewInitiatedAuditcag starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		/*
		 * String value9=""; value9=(String) session.getAttribute("USER_ID");
		 * System.out.println("dfdfdfdfdf"+value9); byte[] encodeBytes =
		 * Base64.getDecoder().decode(value9.getBytes()); String id34 = (new
		 * String(encodeBytes));
		 */

		model.addAttribute("abc", id1);
		logger.info("Method : viewInitiatedAuditcag ends");
		return "audit/view-initiate-audit";
	}

	/*
	 * Post Mapping autocomplete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-initiated-audit-AutocompleteList1" })
	public @ResponseBody JsonResponse<DropDownModel> getFolderList1(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getFolderList1 starts");
		System.out.println("33222333" + searchValue);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getAuditUrl() + "getFolderNameListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getFolderList1 ends");
		return res;
	}
}
