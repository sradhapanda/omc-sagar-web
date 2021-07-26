package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class User {

	private String user;
	private String userName;
	private String userPassword;
	private String userMobile;
	private String userEmail;
	private String userParent;
	private String userAddress;
	private String userState;
	private String userCountry;
	private String userDist;
	private String userPin;
	private Boolean userStatus;
	private Date userCreatedOn;
	private Date userUpdatedOn;
	

	private String roleDashboard;
	private List<String> roles;
	private String userType;
	private String lastName;
	private String emailStatus;
	private String region;
	
	
	public String getLastName() {
		return lastName;
	}

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserParent() {
		return userParent;
	}

	public void setUserParent(String userParent) {
		this.userParent = userParent;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

	public String getUserCountry() {
		return userCountry;
	}

	public void setUserCountry(String userCountry) {
		this.userCountry = userCountry;
	}

	public String getUserDist() {
		return userDist;
	}

	public void setUserDist(String userDist) {
		this.userDist = userDist;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public Boolean getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Boolean userStatus) {
		this.userStatus = userStatus;
	}

	public Date getUserCreatedOn() {
		return userCreatedOn;
	}

	public void setUserCreatedOn(Date userCreatedOn) {
		this.userCreatedOn = userCreatedOn;
	}

	public Date getUserUpdatedOn() {
		return userUpdatedOn;
	}

	public void setUserUpdatedOn(Date userUpdatedOn) {
		this.userUpdatedOn = userUpdatedOn;
	}
	
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public String getRoleDashboard() {
		return roleDashboard;
	}

	public void setRoleDashboard(String roleDashboard) {
		this.roleDashboard = roleDashboard;
	}
	
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Overrides toString method for converting class to string and back 
	**/
	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	}
	
}
