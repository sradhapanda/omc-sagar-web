package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditInitiateModel {
	
	private String auditInitiate;
	
	private String auditType;
	
	private String financialYear;
	
	private String initiatedBy;
	
	private String initiatedDate;
	
	private String summary;
	
	private String document;
	
	private Byte initiatedStatus;
	
	private String createdBy;
	
	private String organizaionName;
	
	private String auditTypeId;
	
	private String letterNo;
	
	private String fromDate;
	
	private String toDate;
	
	private String auditorName;
	
	private String designation;
	private Boolean dtlStatus;
	private String region;
	private List<String> file = new ArrayList<String>();
	private String documentName;
	private Byte reportStatus;
	private String approveStatus;
	private String personTo;
	private String personCc;
	private String Status;
	private String documentComment;
	private String subject;
	
	

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDocumentComment() {
		return documentComment;
	}

	public void setDocumentComment(String documentComment) {
		this.documentComment = documentComment;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

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

	public Byte getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(Byte reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getDocumentName() {
		return documentName;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public List<String> getFile() {
		return file;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getRegionalManager() {
		return regionalManager;
	}

	public void setRegionalManager(String regionalManager) {
		this.regionalManager = regionalManager;
	}

	public String getConcernedFinance() {
		return concernedFinance;
	}

	public void setConcernedFinance(String concernedFinance) {
		this.concernedFinance = concernedFinance;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	private String auditor;
	
	private String regionalManager;
	
	private String concernedFinance;
	private String quarter;
	private Integer audit;
	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public String getLetterNo() {
		return letterNo;
	}

	public void setLetterNo(String letterNo) {
		this.letterNo = letterNo;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public Boolean getDtlStatus() {
		return dtlStatus;
	}

	public void setDtlStatus(Boolean dtlStatus) {
		this.dtlStatus = dtlStatus;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	private String action;

	public AuditInitiateModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getAuditInitiate() {
		return auditInitiate;
	}

	public void setAuditInitiate(String auditInitiate) {
		this.auditInitiate = auditInitiate;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public String getInitiatedDate() {
		return initiatedDate;
	}

	public void setInitiatedDate(String initiatedDate) {
		this.initiatedDate = initiatedDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public Byte getInitiatedStatus() {
		return initiatedStatus;
	}

	public void setInitiatedStatus(Byte initiatedStatus) {
		this.initiatedStatus = initiatedStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOrganizaionName() {
		return organizaionName;
	}

	public void setOrganizaionName(String organizaionName) {
		this.organizaionName = organizaionName;
	}

	public String getAuditTypeId() {
		return auditTypeId;
	}

	public void setAuditTypeId(String auditTypeId) {
		this.auditTypeId = auditTypeId;
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
