/**
 * Defines UserRole entity
 *
 */

package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import groovyjarjarantlr.collections.List;
/**
 * @author nirmalya Labs
 *
 */
public class UserRoleModel {
	
	private List checkBoxData;
	
	private String userRoleId;//primary key
	
	private String userRoleName;
	
	/* private Byte userType; */
	
	private String userCostCenter;
	
	private Boolean userRoleStatus;
	
	private String userDescription;
	
	private String userParentUserRole;
	
	private Boolean userParentRoleStatus;
	
    private Date districtCreatedOn;
	
	private Date districtUpdatedOn;
	
	private String action;
	
	private String showActiveRoleStatus;
	
	private Boolean showActiveParentRoleStatus;
	
	private String userShow;  
	
	private String moduleName;
	
	private String moduleId;
	
	private String functionId;
	
	private String activityId;
	
	private String createdBy;




	public UserRoleModel() {
		super();
	}
	
	//public UserRoleModel(String userRoleId,  String userRoleName, Byte userType, String userCostCenter,
		//	Boolean userRoleStatus, String userDescription, String userParentUserRole, String action) {
		
		public UserRoleModel(String userRoleId,  String userRoleName, String userCostCenter,
				Boolean userRoleStatus, String userDescription, String userParentUserRole, String action) {
		super();
		this.userRoleId = userRoleId;
		this.userRoleName = userRoleName;		
		//this.userType = userType;
		this.userCostCenter = userCostCenter;
		this.userRoleStatus = userRoleStatus;
		this.userDescription = userDescription;
		this.userParentUserRole = userParentUserRole;
		this.action = action;
	}
		

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
  

	public String getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserRoleName() {
		return userRoleName;
	}

	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}

	/*
	 * public Byte getUserType() { return userType; }
	 * 
	 * public void setUserType(Byte userType) { this.userType = userType; }
	 */

	public String getUserCostCenter() {
		return userCostCenter;
	}

	public void setUserCostCenter(String userCostCenter) {
		this.userCostCenter = userCostCenter;
	}

	public Boolean getUserRoleStatus() {
		return userRoleStatus;
	}

	public void setUserRoleStatus(Boolean userRoleStatus) {
		this.userRoleStatus = userRoleStatus;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public String getUserParentUserRole() {
		return userParentUserRole;
	}
	
	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setUserParentUserRole(String userParentUserRole) {
		this.userParentUserRole = userParentUserRole;
	}

	public Boolean getUserParentRoleStatus() {
		return userParentRoleStatus;
	}

	public void setUserParentRoleStatus(Boolean userParentRoleStatus) {
		this.userParentRoleStatus = userParentRoleStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getShowActiveRoleStatus() {
		return showActiveRoleStatus;
	}

	public void setShowActiveRoleStatus(String showActiveRoleStatus) {
		this.showActiveRoleStatus = showActiveRoleStatus;
	}

	public Boolean getShowActiveParentRoleStatus() {
		return showActiveParentRoleStatus;
	}

	public void setShowActiveParentRoleStatus(Boolean showActiveParentRoleStatus) {
		this.showActiveParentRoleStatus = showActiveParentRoleStatus;
	}
	
	public String getUserShow() {
		return userShow;
	}
	
	public void setUserShow(String userShow) {
		this.userShow = userShow;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public List getCheckBoxData() {
		return checkBoxData;
	}

	public void setCheckBoxData(List checkBoxData) {
		this.checkBoxData = checkBoxData;
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
