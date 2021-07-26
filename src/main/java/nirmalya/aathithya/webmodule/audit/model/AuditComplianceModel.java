package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class AuditComplianceModel {

	private Integer quesId;
	private Integer auditId;
	private String comment;
	private String document;
	private String createdBy;
	private Byte auditStatus;
	private Byte obsType;
	private String agreedAct;
	private Byte severity;
	private String createdOn;
	private Byte replyStatus;
	private String person;
	private String personCC;
	private String roleName;
	private String documentName;
	private List<DropDownModel> docuList=new ArrayList<DropDownModel>();
	public List<DropDownModel> getDocuList() {
		return docuList;
	}
	public void setDocuList(List<DropDownModel> docuList) {
		this.docuList = docuList;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	private String date;
	private String time;
	public List<String> getFile() {
		return file;
	}
	public void setFile(List<String> file) {
		this.file = file;
	}
	private List<String> file=new ArrayList<String>();
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getPersonCC() {
		return personCC;
	}
	public void setPersonCC(String personCC) {
		this.personCC = personCC;
	}
	public Byte getReplyStatus() {
		return replyStatus;
	}
	public void setReplyStatus(Byte replyStatus) {
		this.replyStatus = replyStatus;
	}
	public AuditComplianceModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getQuesId() {
		return quesId;
	}
	public void setQuesId(Integer quesId) {
		this.quesId = quesId;
	}
	public Integer getAuditId() {
		return auditId;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Byte getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Byte auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Byte getObsType() {
		return obsType;
	}
	public void setObsType(Byte obsType) {
		this.obsType = obsType;
	}
	public String getAgreedAct() {
		return agreedAct;
	}
	public void setAgreedAct(String agreedAct) {
		this.agreedAct = agreedAct;
	}
	public Byte getSeverity() {
		return severity;
	}
	public void setSeverity(Byte severity) {
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
