
/**
 * web Module for User Type
 */
package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserTypeModel {

	private String userType;
	private String userTypeName;
	private String userTypeDescrptn;
	private Boolean userTypeActive;
	private String action;
	private String userStatus;
	private String createdBy;

	public UserTypeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserTypeDescrptn() {
		return userTypeDescrptn;
	}

	public void setUserTypeDescrptn(String userTypeDescrptn) {
		this.userTypeDescrptn = userTypeDescrptn;
	}

	public Boolean getUserTypeActive() {
		return userTypeActive;
	}

	public void setUserTypeActive(Boolean userTypeActive) {
		this.userTypeActive = userTypeActive;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
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
