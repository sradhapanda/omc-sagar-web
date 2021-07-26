package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */

public class ActivityMasterModel {

	private String tActivity;
	private String tModule;
	private String tFunction;
	private String tActvtyName;
	private String tActvtyDescription;
	private Boolean tActvtyStatus;
	private String tActvtyCreatedBy;
	private String status;
	private String action;

	public ActivityMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettActivity() {
		return tActivity;
	}

	public void settActivity(String tActivity) {
		this.tActivity = tActivity;
	}

	public String gettModule() {
		return tModule;
	}

	public void settModule(String tModule) {
		this.tModule = tModule;
	}

	public String gettFunction() {
		return tFunction;
	}

	public void settFunction(String tFunction) {
		this.tFunction = tFunction;
	}

	public String gettActvtyName() {
		return tActvtyName;
	}

	public void settActvtyName(String tActvtyName) {
		this.tActvtyName = tActvtyName;
	}

	public String gettActvtyDescription() {
		return tActvtyDescription;
	}

	public void settActvtyDescription(String tActvtyDescription) {
		this.tActvtyDescription = tActvtyDescription;
	}

	
	public String gettActvtyCreatedBy() {
		return tActvtyCreatedBy;
	}

	public void settActvtyCreatedBy(String tActvtyCreatedBy) {
		this.tActvtyCreatedBy = tActvtyCreatedBy;
	}

	public Boolean gettActvtyStatus() {
		return tActvtyStatus;
	}

	public void settActvtyStatus(Boolean tActvtyStatus) {
		this.tActvtyStatus = tActvtyStatus;
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
