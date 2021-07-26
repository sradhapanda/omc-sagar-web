package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditHistoryModel {

	private Integer histId;
	private String auditId;
	private String createdBy;
	private String createdOn;
	private String action;
	private String time;
	private String slNo;

	public String getTime() {
		return time;
	}



	public void setTime(String time) {
		this.time = time;
	}



	public String getSlNo() {
		return slNo;
	}



	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}



	public AuditHistoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Integer getHistId() {
		return histId;
	}



	public void setHistId(Integer histId) {
		this.histId = histId;
	}



	public String getAuditId() {
		return auditId;
	}



	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}



	public String getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}



	public String getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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
