package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditDraftReportModel {
	private String draftId;
	private String draftNo;
	private String version;
	private String date;
	private String auditType;
	private String initiateId;
	private String reportType;
	private String comment;

	private String fYear;
	private String type;
	private String attachDate;
	private String attachfileName;
	private String attachDocument;
	private String attachComment;
	private List<String> attachfile = new ArrayList<String>();

	private String infoFileName;
	private String infoDocument;
	private String infoComment;
	private List<String> infofile = new ArrayList<String>();

	private String qlfyFileName;
	private String qlfyDocument;
	private String qlfyComment;
	private List<String> qlfyfile = new ArrayList<String>();

	private List<AuditReportAdditionalInfoModel> additionalInfo;
	private List<AuditReportBasicsQualifyModel> basicOfQualify;
	private List<AuditDraftReportModel> attachmment;

	private List<String> personTo = new ArrayList<String>();
	private List<String> personCC = new ArrayList<String>();

	private String action;
	
	private String createdBy;
	private String edit;
	private String time;
	private String organisation;
	private String designation;
	private Integer commentId;

	
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public AuditDraftReportModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDraftId() {
		return draftId;
	}

	public void setDraftId(String draftId) {
		this.draftId = draftId;
	}

	public String getDraftNo() {
		return draftNo;
	}

	public void setDraftNo(String draftNo) {
		this.draftNo = draftNo;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getInitiateId() {
		return initiateId;
	}

	public void setInitiateId(String initiateId) {
		this.initiateId = initiateId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getfYear() {
		return fYear;
	}

	public void setfYear(String fYear) {
		this.fYear = fYear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttachDate() {
		return attachDate;
	}

	public void setAttachDate(String attachDate) {
		this.attachDate = attachDate;
	}

	public String getAttachfileName() {
		return attachfileName;
	}

	public void setAttachfileName(String attachfileName) {
		this.attachfileName = attachfileName;
	}

	public String getAttachDocument() {
		return attachDocument;
	}

	public void setAttachDocument(String attachDocument) {
		this.attachDocument = attachDocument;
	}

	public String getAttachComment() {
		return attachComment;
	}

	public void setAttachComment(String attachComment) {
		this.attachComment = attachComment;
	}

	public String getInfoFileName() {
		return infoFileName;
	}

	public void setInfoFileName(String infoFileName) {
		this.infoFileName = infoFileName;
	}

	public String getInfoDocument() {
		return infoDocument;
	}

	public void setInfoDocument(String infoDocument) {
		this.infoDocument = infoDocument;
	}

	public String getInfoComment() {
		return infoComment;
	}

	public void setInfoComment(String infoComment) {
		this.infoComment = infoComment;
	}

	public String getQlfyFileName() {
		return qlfyFileName;
	}

	public void setQlfyFileName(String qlfyFileName) {
		this.qlfyFileName = qlfyFileName;
	}

	public String getQlfyDocument() {
		return qlfyDocument;
	}

	public void setQlfyDocument(String qlfyDocument) {
		this.qlfyDocument = qlfyDocument;
	}

	public String getQlfyComment() {
		return qlfyComment;
	}

	public void setQlfyComment(String qlfyComment) {
		this.qlfyComment = qlfyComment;
	}

	public List<AuditReportAdditionalInfoModel> getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(List<AuditReportAdditionalInfoModel> additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<AuditReportBasicsQualifyModel> getBasicOfQualify() {
		return basicOfQualify;
	}

	public void setBasicOfQualify(List<AuditReportBasicsQualifyModel> basicOfQualify) {
		this.basicOfQualify = basicOfQualify;
	}

	public List<AuditDraftReportModel> getAttachmment() {
		return attachmment;
	}

	public void setAttachmment(List<AuditDraftReportModel> attachmment) {
		this.attachmment = attachmment;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getAttachfile() {
		return attachfile;
	}

	public void setAttachfile(List<String> attachfile) {
		this.attachfile = attachfile;
	}

	public List<String> getInfofile() {
		return infofile;
	}

	public void setInfofile(List<String> infofile) {
		this.infofile = infofile;
	}

	public List<String> getQlfyfile() {
		return qlfyfile;
	}

	public void setQlfyfile(List<String> qlfyfile) {
		this.qlfyfile = qlfyfile;
	}

	public List<String> getPersonTo() {
		return personTo;
	}

	public void setPersonTo(List<String> personTo) {
		this.personTo = personTo;
	}

	public List<String> getPersonCC() {
		return personCC;
	}

	public void setPersonCC(List<String> personCC) {
		this.personCC = personCC;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
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
