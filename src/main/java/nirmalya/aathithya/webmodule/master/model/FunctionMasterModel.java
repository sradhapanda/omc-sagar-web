package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class FunctionMasterModel {

	private String tFunction;
	private String tModule;
	private String tFunctnName;
	private String tFunctnDescription;
	private Boolean tFunctnStatus;
	private String tFunctnCreatedBy;
	private String tFunctionLogo;
	private String status;
	private String action;

	public FunctionMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettFunction() {
		return tFunction;
	}

	public void settFunction(String tFunction) {
		this.tFunction = tFunction;
	}

	public String gettModule() {
		return tModule;
	}

	public void settModule(String tModule) {
		this.tModule = tModule;
	}

	public String gettFunctnName() {
		return tFunctnName;
	}

	public void settFunctnName(String tFunctnName) {
		this.tFunctnName = tFunctnName;
	}

	public String gettFunctnDescription() {
		return tFunctnDescription;
	}

	public void settFunctnDescription(String tFunctnDescription) {
		this.tFunctnDescription = tFunctnDescription;
	}

	public Boolean gettFunctnStatus() {
		return tFunctnStatus;
	}

	public void settFunctnStatus(Boolean tFunctnStatus) {
		this.tFunctnStatus = tFunctnStatus;
	}

	
	
	public String gettFunctnCreatedBy() {
		return tFunctnCreatedBy;
	}

	public void settFunctnCreatedBy(String tFunctnCreatedBy) {
		this.tFunctnCreatedBy = tFunctnCreatedBy;
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

	public String gettFunctionLogo() {
		return tFunctionLogo;
	}

	public void settFunctionLogo(String tFunctionLogo) {
		this.tFunctionLogo = tFunctionLogo;
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
