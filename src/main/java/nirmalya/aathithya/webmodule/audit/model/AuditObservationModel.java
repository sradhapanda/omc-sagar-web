package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditObservationModel {
	private Integer auditId;
	private String deptId;
	private String sectionId;
	private Integer slNo;
	private String question;
	private String observation;
	private String document;	
	private List<String> file = new ArrayList<String>();
	private String obsType;
	private String auditStatus;
	private Integer quesId;
	private String severity;
	private String iRStatus;
	private String reqStatus;
	private String qusDoc;
	private String sectionStatus;
	private String auditorStatus;
	private String coStatus;
	private String qusCoStatus;
	private String auditeeStatus;
	private String comStatus;
	private String personForwardStatus;
	private String forwardStatus;
	private String obsForStatus;
	private Boolean rejectStatus;
	private String region;
	private String auditParty;
	private String assignStatus;
	private String date;
	private String time;
	private String initiateId;
	private String createdBy;
	private String severityType;
	private String obsTypeName;
	private List<RequisitionViewModel> forwardDetails = new ArrayList<RequisitionViewModel>();
	private String addObsStatus;
	private String checklistRef;
	private List<AuditComplianceModel> compliances = new ArrayList<AuditComplianceModel>();
	private String finalObservation;
	private String riskRating;
	private String auditType;
	private String subject;
	private String personTo ;
	private String personCc;


	public String getPersonTo() {
		return personTo;
	}

	public void setPersonTo(String personTo) {
		this.personTo = personTo;
	}

	public String getPersonCc() {
		return personCc;
	}

	public void setPersonCc(String personCc) {
		this.personCc = personCc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getFinalObservation() {
		return finalObservation;
	}

	public void setFinalObservation(String finalObservation) {
		this.finalObservation = finalObservation;
	}

	public String getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}

	public List<AuditComplianceModel> getCompliances() {
		return compliances;
	}

	public void setCompliances(List<AuditComplianceModel> compliances) {
		this.compliances = compliances;
	}

	public String getChecklistRef() {
		return checklistRef;
	}

	public void setChecklistRef(String checklistRef) {
		this.checklistRef = checklistRef;
	}

	public String getAddObsStatus() {
		return addObsStatus;
	}

	public void setAddObsStatus(String addObsStatus) {
		this.addObsStatus = addObsStatus;
	}

	public List<RequisitionViewModel> getForwardDetails() {
		return forwardDetails;
	}

	public void setForwardDetails(List<RequisitionViewModel> forwardDetails) {
		this.forwardDetails = forwardDetails;
	}

	public String getDocument() {
		return document;
	}

	public String getObsTypeName() {
		return obsTypeName;
	}

	public void setObsTypeName(String obsTypeName) {
		this.obsTypeName = obsTypeName;
	}

	public String getSeverityType() {
		return severityType;
	}

	public void setSeverityType(String severityType) {
		this.severityType = severityType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getInitiateId() {
		return initiateId;
	}

	public void setInitiateId(String initiateId) {
		this.initiateId = initiateId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAssignStatus() {
		return assignStatus;
	}

	public void setAssignStatus(String assignStatus) {
		this.assignStatus = assignStatus;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAuditParty() {
		return auditParty;
	}

	public void setAuditParty(String auditParty) {
		this.auditParty = auditParty;
	}

	public Boolean getRejectStatus() {
		return rejectStatus;
	}

	public void setRejectStatus(Boolean rejectStatus) {
		this.rejectStatus = rejectStatus;
	}

	public String getObsForStatus() {
		return obsForStatus;
	}

	public void setObsForStatus(String obsForStatus) {
		this.obsForStatus = obsForStatus;
	}

	public String getForwardStatus() {
		return forwardStatus;
	}

	public void setForwardStatus(String forwardStatus) {
		this.forwardStatus = forwardStatus;
	}

	public String getPersonForwardStatus() {
		return personForwardStatus;
	}

	public void setPersonForwardStatus(String personForwardStatus) {
		this.personForwardStatus = personForwardStatus;
	}

	public String getComStatus() {
		return comStatus;
	}

	public void setComStatus(String comStatus) {
		this.comStatus = comStatus;
	}

	public String getAuditeeStatus() {
		return auditeeStatus;
	}

	public void setAuditeeStatus(String auditeeStatus) {
		this.auditeeStatus = auditeeStatus;
	}

	public String getCoStatus() {
		return coStatus;
	}

	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}

	public String getAuditorStatus() {
		return auditorStatus;
	}

	public String getQusCoStatus() {
		return qusCoStatus;
	}

	public void setQusCoStatus(String qusCoStatus) {
		this.qusCoStatus = qusCoStatus;
	}

	public void setAuditorStatus(String auditorStatus) {
		this.auditorStatus = auditorStatus;
	}

	public String getSectionStatus() {
		return sectionStatus;
	}

	public void setSectionStatus(String sectionStatus) {
		this.sectionStatus = sectionStatus;
	}

	public String getQusDoc() {
		return qusDoc;
	}

	public void setQusDoc(String qusDoc) {
		this.qusDoc = qusDoc;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}

	public AuditObservationModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public String getiRStatus() {
		return iRStatus;
	}

	public void setiRStatus(String iRStatus) {
		this.iRStatus = iRStatus;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String string() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public List<String> getFile() {
		return file;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}
	public String getObsType() {
		return obsType;
	}

	public void setObsType(String obsType) {
		this.obsType = obsType;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getQuesId() {
		return quesId;
	}

	public void setQuesId(Integer quesId) {
		this.quesId = quesId;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonStr;
		try {
			jsonStr = mapperObj.writeValueAsString(this);
		} catch (IOException ex) {

			jsonStr = ex.toString();
		}
		return jsonStr;
	}

}
