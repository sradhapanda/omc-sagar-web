/**
 * 
 */
package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class MasterCountryModel {
	
	private String countryId;
	private String countryName; 
	private String countryCode;
	private Boolean countryActive; 
	private String countryCreatedBy; 
	private String countryUpdatedOn;
	private String status;
	private String action;
	
	public MasterCountryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public Boolean getCountryActive() {
		return countryActive;
	}

	public void setCountryActive(Boolean countryActive) {
		this.countryActive = countryActive;
	}
	
	public String getCountryCreatedBy() {
		return countryCreatedBy;
	}
	
	public void setCountryCreatedBy(String countryCreatedBy) {
		this.countryCreatedBy = countryCreatedBy;
	}
	
	public String getCountryUpdatedOn() {
		return countryUpdatedOn;
	}
	
	public void setCountryUpdatedOn(String countryUpdatedOn) {
		this.countryUpdatedOn = countryUpdatedOn;
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
