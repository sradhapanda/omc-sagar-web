/**
 * 
 */
package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

/**
 * @author USER
 *
 */
public class RequisitionViewModel {
	private Integer draftHiddenId;
	private String draftStatus;
	public Integer getDraftHiddenId() {
		return draftHiddenId;
	}

	public void setDraftHiddenId(Integer draftHiddenId) {
		this.draftHiddenId = draftHiddenId;
	}

	public String getDraftStatus() {
		return draftStatus;
	}

	public void setDraftStatus(String draftStatus) {
		this.draftStatus = draftStatus;
	}
	private Integer reqId;
	private Integer auditId;
	private String document;
	private String comment;
	private String createdby;
	private String createdOn;
	private String commentStatus;
	private String role;
	private String draftComment;
	private String auditeeStatus;
	private List<String> personList = new ArrayList<String>();
	private List<String> file = new ArrayList<String>();
	private String createdRole;
	private String documentName;
	private String roleName;
	private String date;
	private List<DropDownModel> DocumentList = new ArrayList<DropDownModel>();
	private String commentStatusName;
	private Integer questionId;
	private String personTo;
	private String personCc;
	private String documentComment;
	private String subject;
	private String totalReq;
	private String identifier;
	private String createdById;
	private String initiatedId;
	private String priority;
	private String mailFrom;
	private String reminderDate;
	private String auditType;
	private List<String> personToId = new ArrayList<String>();
	private String personCcId;
	private String coStatus;
	private String sectionStatus;
	private String folder;
	private String emailstatus;
	private String prevauditid;
	private String auditTrialId;
	
	private String editDocName;
	private String section;
	
	
	
	public String getEditDocName() {
		return editDocName;
	}

	public void setEditDocName(String editDocName) {
		this.editDocName = editDocName;
	}

	public String getPrevauditid() {
		return prevauditid;
	}

	public void setPrevauditid(String prevauditid) {
		this.prevauditid = prevauditid;
	}

	public String getEmailstatus() {
		return emailstatus;
	}

	public void setEmailstatus(String emailstatus) {
		this.emailstatus = emailstatus;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getReminderDate() {
		return reminderDate;
	}

	public List<String> getPersonToId() {
		return personToId;
	}

	public void setPersonToId(List<String> personToId) {
		this.personToId = personToId;
	}

	public String getPersonCcId() {
		return personCcId;
	}

	public void setPersonCcId(String personCcId) {
		this.personCcId = personCcId;
	}

	public String getCoStatus() {
		return coStatus;
	}

	public void setCoStatus(String coStatus) {
		this.coStatus = coStatus;
	}

	public String getSectionStatus() {
		return sectionStatus;
	}

	public void setSectionStatus(String sectionStatus) {
		this.sectionStatus = sectionStatus;
	}

	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getInitiatedId() {
		return initiatedId;
	}

	public void setInitiatedId(String initiatedId) {
		this.initiatedId = initiatedId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTotalReq() {
		return totalReq;
	}

	public void setTotalReq(String totalReq) {
		this.totalReq = totalReq;
	}

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

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public List<DropDownModel> getDocumentList() {
		return DocumentList;
	}

	public void setDocumentList(List<DropDownModel> documentList) {
		DocumentList = documentList;
	}

	public String getCommentStatusName() {
		return commentStatusName;
	}

	public void setCommentStatusName(String commentStatusName) {
		this.commentStatusName = commentStatusName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	private String time;
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCreatedRole() {
		return createdRole;
	}

	public void setCreatedRole(String createdRole) {
		this.createdRole = createdRole;
	}

	public RequisitionViewModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Integer getReqId() {
		return reqId;
	}

	public RequisitionViewModel(Integer reqId, Integer auditId, String document, String comment, String createdby,
			String createdOn, String commentStatus, String role, String draftComment, String auditeeStatus,
			List<String> personList, List<String> file,String roleName) {
		super();
		this.reqId = reqId;
		this.auditId = auditId;
		this.document = document;
		this.comment = comment;
		this.createdby = createdby;
		this.createdOn = createdOn;
		this.commentStatus = commentStatus;
		this.role = role;
		this.draftComment = draftComment;
		this.auditeeStatus = auditeeStatus;
		this.personList = personList;
		this.file = file;
		this.roleName=roleName;
	}

	public void setReqId(Integer reqId) {
		this.reqId = reqId;
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDraftComment() {
		return draftComment;
	}

	public void setDraftComment(String draftComment) {
		this.draftComment = draftComment;
	}

	
	public String getAuditeeStatus() {
		return auditeeStatus;
	}

	public void setAuditeeStatus(String auditeeStatus) {
		this.auditeeStatus = auditeeStatus;
	}

	public List<String> getPersonList() {
		return personList;
	}

	public void setPersonList(List<String> personList) {
		this.personList = personList;
	}

	public List<String> getFile() {
		return file;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
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
	public RequisitionViewModel(String personTo, String personCc, String auditTrialId,String document, String documentName, String documentComment, String comment, List<String> file,
			String createdby) {
		super();
		
		this.personTo = personTo;
		this.personCc = personCc;
		this.auditTrialId = auditTrialId;
		this.document = document;
		this.documentName = documentName;
		this.documentComment = documentComment;
		this.comment = comment;
		this.file = file;
		this.createdby = createdby;
		
	}

	public String getAuditTrialId() {
		return auditTrialId;
	}

	public void setAuditTrialId(String auditTrialId) {
		this.auditTrialId = auditTrialId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
}
