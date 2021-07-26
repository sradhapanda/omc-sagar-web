package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class AuditMeetingModel {
	private Integer id;
	private Integer audit;
	private String initiate;
	private String date;
	private String fromTime;
	private String toTime;
	private String section;
	private String sectionHead;
	private String createdBy;
	private String personTo;
	private String document;
	private String documentName;
	private List<String> file = new ArrayList<String>();
	private List<DropDownModel> documents = new ArrayList<DropDownModel>();
	private List<DropDownModel> documentsSec = new ArrayList<DropDownModel>();
	private String comment;
	private String meetingType;
	private String auditType;
	private String comments;
	private String subject;
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<DropDownModel> getDocumentsSec() {
		return documentsSec;
	}

	public void setDocumentsSec(List<DropDownModel> documentsSec) {
		this.documentsSec = documentsSec;
	}

	public List<DropDownModel> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DropDownModel> documents) {
		this.documents = documents;
	}

	public List<String> getFile() {
		return file;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}

	public AuditMeetingModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public String getInitiate() {
		return initiate;
	}

	public void setInitiate(String initiate) {
		this.initiate = initiate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSectionHead() {
		return sectionHead;
	}

	public void setSectionHead(String sectionHead) {
		this.sectionHead = sectionHead;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPersonTo() {
		return personTo;
	}

	public void setPersonTo(String personTo) {
		this.personTo = personTo;
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
