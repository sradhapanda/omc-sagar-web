package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserApprovalActionModel {
	private String approvalId;
	private String actName;
	private String buttonClass;
	private String description;
	private Boolean actStatus;
	private String status;
	private String action;
	private String createdBy;

	public UserApprovalActionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserApprovalActionModel(String actName, String buttonClass, String description, Boolean actStatus) {
		super();
		this.actName = actName;
		this.buttonClass = buttonClass;
		this.description = description;
		this.actStatus = actStatus;

	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public Boolean getActStatus() {
		return actStatus;
	}

	public void setActStatus(Boolean actStatus) {
		this.actStatus = actStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
