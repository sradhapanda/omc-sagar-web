/**
 * Defines State table entity
 *
 */

package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author nirmalya Labs
 *
 */
public class UserStateModel {

	

	private String stateId;//primary key
	private String stateName;
	private Boolean stateStatus;
	private Date stateCreatedOn;
	private Date stateUpdatedOn;
	private String action;
	private String createdBy;
	private String stateShowActive;
	private String countryName;
	
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public UserStateModel() {
		super();
	}
	public UserStateModel(String stateId, String stateName, 
			Boolean stateStatus, Date stateCreatedOn, String countryName, String action) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
		this.stateStatus = stateStatus;
		this.stateCreatedOn = stateCreatedOn;
		this.countryName = countryName;
		this.action = action;
	}
	

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	
	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Boolean getStateStatus() {
		return stateStatus;
	}

	public void setStateStatus(Boolean stateStatus) {
		this.stateStatus = stateStatus;
	}

	public Date getStateCreatedOn() {
		return stateCreatedOn;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setStateCreatedOn(Date stateCreatedOn) {
		this.stateCreatedOn = stateCreatedOn;
	}

	public Date getStateUpdatedOn() {
		return stateUpdatedOn;
	}

	public void setStateUpdatedOn(Date stateUpdatedOn) {
		this.stateUpdatedOn = stateUpdatedOn;
	}
	
	public String getStateShowActive() {
		return stateShowActive;
	}
	
	public void setStateShowActive(String stateShowActive) {
		this.stateShowActive = stateShowActive;
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
