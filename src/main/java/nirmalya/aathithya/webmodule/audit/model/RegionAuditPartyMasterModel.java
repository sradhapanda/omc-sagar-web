package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RegionAuditPartyMasterModel {

	private String regionId;
	private String regionName;
	private String auditId;
	private String auditor;
	private String desc;
	private Boolean status;
	private String createdBy;
	private String action;
	private String isEdit;
	private String statusName;
	
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
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
	public String getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
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
