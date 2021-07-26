package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsCertificationMasterModel {
	private String certificationId;
	private String certificationName;
	private String certificationDesc;
	private Boolean certificationStatus;
	private String statusName;
	private String action;
	private String createdBy;
	private String companyId;

	public HrmsCertificationMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCertificationId() {
		return certificationId;
	}

	public void setCertificationId(String certificationId) {
		this.certificationId = certificationId;
	}

	public String getCertificationName() {
		return certificationName;
	}

	public void setCertificationName(String certificationName) {
		this.certificationName = certificationName;
	}

	public String getCertificationDesc() {
		return certificationDesc;
	}

	public void setCertificationDesc(String certificationDesc) {
		this.certificationDesc = certificationDesc;
	}

	public Boolean getCertificationStatus() {
		return certificationStatus;
	}

	public void setCertificationStatus(Boolean certificationStatus) {
		this.certificationStatus = certificationStatus;
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
