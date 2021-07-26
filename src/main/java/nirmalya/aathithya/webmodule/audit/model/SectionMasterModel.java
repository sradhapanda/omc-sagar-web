package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SectionMasterModel {
	private String section;
	private String department;
	private String sectionName;
	private Boolean sectionStatus;
	private String createdBy;
	private String status;
	private String action;
	
	public SectionMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Boolean getSectionStatus() {
		return sectionStatus;
	}
	public void setSectionStatus(Boolean sectionStatus) {
		this.sectionStatus = sectionStatus;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
