package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmGoalMasterModel {

	private String tGoalId;
	private String tGoalName;
	private String tGoalDesc;
	private Boolean tGoalStatus;
	private String tGoalCreatedBy;
	private String action;
	private String status;

	public HrmGoalMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettGoalId() {
		return tGoalId;
	}

	public void settGoalId(String tGoalId) {
		this.tGoalId = tGoalId;
	}

	public String gettGoalName() {
		return tGoalName;
	}

	public void settGoalName(String tGoalName) {
		this.tGoalName = tGoalName;
	}

	public String gettGoalDesc() {
		return tGoalDesc;
	}

	public void settGoalDesc(String tGoalDesc) {
		this.tGoalDesc = tGoalDesc;
	}

	public Boolean gettGoalStatus() {
		return tGoalStatus;
	}

	public void settGoalStatus(Boolean tGoalStatus) {
		this.tGoalStatus = tGoalStatus;
	}

	public String gettGoalCreatedBy() {
		return tGoalCreatedBy;
	}

	public void settGoalCreatedBy(String tGoalCreatedBy) {
		this.tGoalCreatedBy = tGoalCreatedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
