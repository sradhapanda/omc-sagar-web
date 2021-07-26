package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmEmploymentTypeModel {
	
	private String employmentId;
	private String employmentName;
	private String employmentDesc;
	private Boolean employmentStatus;
	private String statusName;
	private String action;
	private String createdBy;
	private String companyId;
	public HrmEmploymentTypeModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmploymentId() {
		return employmentId;
	}
	public void setEmploymentId(String employmentId) {
		this.employmentId = employmentId;
	}
	public String getEmploymentName() {
		return employmentName;
	}
	public void setEmploymentName(String employmentName) {
		this.employmentName = employmentName;
	}
	public String getEmploymentDesc() {
		return employmentDesc;
	}
	public void setEmploymentDesc(String employmentDesc) {
		this.employmentDesc = employmentDesc;
	}
	public Boolean getEmploymentStatus() {
		return employmentStatus;
	}
	public void setEmploymentStatus(Boolean employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
