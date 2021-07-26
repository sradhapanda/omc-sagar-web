package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class ServiceMasterModel {

	private String tServiceId;
	private String tServiceName;
	private String tServiceDesc;
	private Boolean tServiceStatus;
	private String tCreatedBy;
	private String status;
	private String action;

	public ServiceMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettServiceId() {
		return tServiceId;
	}

	public void settServiceId(String tServiceId) {
		this.tServiceId = tServiceId;
	}

	public String gettServiceName() {
		return tServiceName;
	}

	public void settServiceName(String tServiceName) {
		this.tServiceName = tServiceName;
	}

	public String gettServiceDesc() {
		return tServiceDesc;
	}

	public void settServiceDesc(String tServiceDesc) {
		this.tServiceDesc = tServiceDesc;
	}

	public Boolean gettServiceStatus() {
		return tServiceStatus;
	}

	public void settServiceStatus(Boolean tServiceStatus) {
		this.tServiceStatus = tServiceStatus;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
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
