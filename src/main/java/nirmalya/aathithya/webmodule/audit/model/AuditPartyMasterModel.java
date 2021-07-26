package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditPartyMasterModel {

	private String auditPartyId;
	
	private String auditPartyFirstName;
	private String auditPartyLastName;
	
	private String auditPartyDesc;

	private Boolean auditPartyActive;

	private String createdBy;
	
	private  Integer slNo;
	
	private String action;
	
	private String statusName;
	

	public String getAuditPartyId() {
		return auditPartyId;
	}

	public void setAuditPartyId(String auditPartyId) {
		this.auditPartyId = auditPartyId;
	}

	

	public String getAuditPartyFirstName() {
		return auditPartyFirstName;
	}

	public void setAuditPartyFirstName(String auditPartyFirstName) {
		this.auditPartyFirstName = auditPartyFirstName;
	}

	public String getAuditPartyLastName() {
		return auditPartyLastName;
	}

	public void setAuditPartyLastName(String auditPartyLastName) {
		this.auditPartyLastName = auditPartyLastName;
	}

	public String getAuditPartyDesc() {
		return auditPartyDesc;
	}

	public void setAuditPartyDesc(String auditPartyDesc) {
		this.auditPartyDesc = auditPartyDesc;
	}

	public Boolean getAuditPartyActive() {
		return auditPartyActive;
	}

	public void setAuditPartyActive(Boolean auditPartyActive) {
		this.auditPartyActive = auditPartyActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
