/**
 * Defines District table entity
 *
 */

package nirmalya.aathithya.webmodule.master.model;
/**
 * @author nirmalya Labs
 *
 */
import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserDistrictModel {

	private String districtId;//primary key
	
	private String stateName;
	
	private String districtName;
	
	private String stateId;
	
	
	private Boolean districtStatus;
	
	private Date districtCreatedOn;
	
	private Date districtUpdatedOn;
	private String action;
	
	private String stateShowActive;
	
	private String createdBy;
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public UserDistrictModel() {
		super();
	}
	
	public UserDistrictModel(String districtId,  String districtName, String stateId, String stateName,
			Boolean districtStatus, Date districtUpdatedOn, String action) {
		super();
		this.districtId = districtId;
		this.districtName = districtName;
		this.stateId = stateId;
		this.stateName = stateName;
		this.districtStatus = districtStatus;
		this.districtUpdatedOn = districtUpdatedOn;
		this.action = action;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
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

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Boolean getDistrictStatus() {
		return districtStatus;
	}

	public void setDistrictStatus(Boolean districtStatus) {
		this.districtStatus = districtStatus;
	}

	public Date getDistrictCreatedOn() {
		return districtCreatedOn;
	}

	public void setDistrictCreatedOn(Date districtCreatedOn) {
		this.districtCreatedOn = districtCreatedOn;
	}

	public Date getDistrictUpdatedOn() {
		return districtUpdatedOn;
	}

	public void setDistrictUpdatedOn(Date districtUpdatedOn) {
		this.districtUpdatedOn = districtUpdatedOn;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
