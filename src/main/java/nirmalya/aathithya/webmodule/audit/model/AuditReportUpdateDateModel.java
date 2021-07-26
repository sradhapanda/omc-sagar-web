package nirmalya.aathithya.webmodule.audit.model;

import java.util.List;

public class AuditReportUpdateDateModel {
	
	private String draftRepId;
	private String desc;
	private String docName;
	private String fileName;
	private String comment;
	private String date;
	private List<String> file;
	private String createdBy;
	private String action;
	private Boolean isEdit;
	private String auditNo;
	private String auditType;
	private String startDate;
	private String endDate;
	
	public Boolean getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}
	public String getAction() {
		return action;
	}
	public String getAuditNo() {
		return auditNo;
	}
	public void setAuditNo(String auditNo) {
		this.auditNo = auditNo;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public List<String> getFile() {
		return file;
	}
	public void setFile(List<String> file) {
		this.file = file;
	}
	public String getDraftRepId() {
		return draftRepId;
	}
	public void setDraftRepId(String draftRepId) {
		this.draftRepId = draftRepId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "AuditReportUpdateDateModel [draftRepId=" + draftRepId + ", desc=" + desc + ", docName=" + docName
				+ ", fileName=" + fileName + ", comment=" + comment + ", date=" + date + ", file=" + file + "]";
	}
	
	
}
