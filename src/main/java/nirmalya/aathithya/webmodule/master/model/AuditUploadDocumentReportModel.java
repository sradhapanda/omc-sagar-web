package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditUploadDocumentReportModel {
	private Integer slNo;
	private Integer documentUploadId;
	private String desc;
	private String document;
	private List<String> file = new ArrayList<String>();
	private String year;
	private String auditType;
	private String action;
	public AuditUploadDocumentReportModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDocument() {
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public Integer getDocumentUploadId() {
		return documentUploadId;
	}
	public void setDocumentUploadId(Integer documentUploadId) {
		this.documentUploadId = documentUploadId;
	}
	
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
