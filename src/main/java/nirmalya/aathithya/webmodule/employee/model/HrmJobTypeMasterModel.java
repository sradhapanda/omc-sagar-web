package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmJobTypeMasterModel {

	private String jobTypeId;
	private String jobTypeName;
	private String jobTypeDesc;
	private Boolean jobTypeStatus;
	private String statusName;
	private String action;
	private String createdBy;
	private String companyId;

	public HrmJobTypeMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public String getJobTypeDesc() {
		return jobTypeDesc;
	}

	public void setJobTypeDesc(String jobTypeDesc) {
		this.jobTypeDesc = jobTypeDesc;
	}

	public Boolean getJobTypeStatus() {
		return jobTypeStatus;
	}

	public void setJobTypeStatus(Boolean jobTypeStatus) {
		this.jobTypeStatus = jobTypeStatus;
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
