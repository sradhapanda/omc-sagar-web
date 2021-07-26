package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class ModuleMasterModel {

	private String tModule;
	private String tModlName;
	private String tModlDescription;
	private Boolean tModlStatus;
	private String tModlCreatedBy;
	private String tModuleLogo;
	private String status;
	private String action;

	public ModuleMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettModule() {
		return tModule;
	}

	public void settModule(String tModule) {
		this.tModule = tModule;
	}

	public String gettModlName() {
		return tModlName;
	}

	public void settModlName(String tModlName) {
		this.tModlName = tModlName;
	}

	public String gettModlDescription() {
		return tModlDescription;
	}

	public void settModlDescription(String tModlDescription) {
		this.tModlDescription = tModlDescription;
	}

	public Boolean gettModlStatus() {
		return tModlStatus;
	}

	public void settModlStatus(Boolean tModlStatus) {
		this.tModlStatus = tModlStatus;
	}

	
	
	public String gettModlCreatedBy() {
		return tModlCreatedBy;
	}

	public void settModlCreatedBy(String tModlCreatedBy) {
		this.tModlCreatedBy = tModlCreatedBy;
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

	
	public String gettModuleLogo() {
		return tModuleLogo;
	}

	public void settModuleLogo(String tModuleLogo) {
		this.tModuleLogo = tModuleLogo;
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
