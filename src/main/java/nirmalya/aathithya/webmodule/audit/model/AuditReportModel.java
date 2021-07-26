package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditReportModel {
	private String auditInitiate;
	private String auditType;
	private String auditFromDate;
	private String auidtToDate;
	private String summary;
	private String status;
	private String document;
	private String documentName;
	private String comment;
	private List<AuditReportModel> documents = new ArrayList<AuditReportModel>(); 
	private List<String> documentNames = new ArrayList<String>(); 
	private List<String> comments = new ArrayList<String>(); 
	public List<String> getDocumentNames() {
		return documentNames;
	}
	public void setDocumentNames(List<String> documentNames) {
		this.documentNames = documentNames;
	}
	public List<String> getComments() {
		return comments;
	}
	public void setComments(List<String> comments) {
		this.comments = comments;
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
	public String getAuditFromDate() {
		return auditFromDate;
	}
	public void setAuditFromDate(String auditFromDate) {
		this.auditFromDate = auditFromDate;
	}
	public String getAuidtToDate() {
		return auidtToDate;
	}
	public void setAuidtToDate(String auidtToDate) {
		this.auidtToDate = auidtToDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

	public List<AuditReportModel> getDocuments() {
		return documents;
	}
	public void setDocuments(List<AuditReportModel> documents) {
		this.documents = documents;
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
