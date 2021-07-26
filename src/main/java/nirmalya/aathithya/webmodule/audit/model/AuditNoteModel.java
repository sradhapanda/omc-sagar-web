package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;

public class AuditNoteModel {
	
	private String noteId;
	private String doc;
	private String date;
	private String time;
	private String organisation;
	private String createdBy;
	private String designtion;
	private String comment;
	private List<DropDownModel> docList = new ArrayList<DropDownModel>();
	private String personTo;
	private String personCc;
	private String totalAuditorReq;
	public String getTotalAuditorReq() {
		return totalAuditorReq;
	}


	public void setTotalAuditorReq(String totalAuditorReq) {
		this.totalAuditorReq = totalAuditorReq;
	}


	public List<DropDownModel> getDocList() {
		return docList;
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


	public void setDocList(List<DropDownModel> docList) {
		this.docList = docList;
	}


	public List<AuditNoteModel> getAuditNoteModel() {
		return auditNoteModel;
	}


	public void setAuditNoteModel(List<AuditNoteModel> auditNoteModel) {
		this.auditNoteModel = auditNoteModel;
	}

	private String docPath;
	private List<AuditNoteModel>auditNoteModel=new ArrayList<AuditNoteModel>();

	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getDocPath() {
		return docPath;
	}


	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}


	public String getNoteId() {
		return noteId;
	}


	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}


	public String getDoc() {
		return doc;
	}


	public void setDoc(String doc) {
		this.doc = doc;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getOrganisation() {
		return organisation;
	}


	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getDesigntion() {
		return designtion;
	}


	public void setDesigntion(String designtion) {
		this.designtion = designtion;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
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

