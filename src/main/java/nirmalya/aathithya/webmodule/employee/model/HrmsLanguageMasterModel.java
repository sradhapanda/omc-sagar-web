package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsLanguageMasterModel {
	private String languageId;
	private String languageName;
	private String languageDesc;
	private Boolean languageStatus;
	private String statusName;
	private String action;
	private String createdBy;
	private String companyId;

	public HrmsLanguageMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getLanguageDesc() {
		return languageDesc;
	}

	public void setLanguageDesc(String languageDesc) {
		this.languageDesc = languageDesc;
	}

	public Boolean getLanguageStatus() {
		return languageStatus;
	}

	public void setLanguageStatus(Boolean languageStatus) {
		this.languageStatus = languageStatus;
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
