package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserMembershipMstrModel {

	private String memId;
	private String memName;
	private Float memMonthlyFee;
	private Float memRegistrationFee;
	private String memDescription;
	private String memValidity;
	private Integer tDependentsLimit;
	private Integer tChildrenAgeLimit;
	private String tEffectiveFromDate;
	private Boolean memStatus;
	private String tCMemberTypCreatedBy;
	private String membershipShowActive;
	private String action;
	
	public UserMembershipMstrModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public String getMemId() {
		return memId;
	}



	public void setMemId(String memId) {
		this.memId = memId;
	}



	public String getMemName() {
		return memName;
	}



	public void setMemName(String memName) {
		this.memName = memName;
	}



	public Float getMemMonthlyFee() {
		return memMonthlyFee;
	}



	public void setMemMonthlyFee(Float memMonthlyFee) {
		this.memMonthlyFee = memMonthlyFee;
	}



	public Float getMemRegistrationFee() {
		return memRegistrationFee;
	}



	public void setMemRegistrationFee(Float memRegistrationFee) {
		this.memRegistrationFee = memRegistrationFee;
	}



	public String getMemDescription() {
		return memDescription;
	}



	public void setMemDescription(String memDescription) {
		this.memDescription = memDescription;
	}



	public String getMemValidity() {
		return memValidity;
	}



	public void setMemValidity(String memValidity) {
		this.memValidity = memValidity;
	}



	public Integer gettDependentsLimit() {
		return tDependentsLimit;
	}



	public void settDependentsLimit(Integer tDependentsLimit) {
		this.tDependentsLimit = tDependentsLimit;
	}



	public Integer gettChildrenAgeLimit() {
		return tChildrenAgeLimit;
	}



	public void settChildrenAgeLimit(Integer tChildrenAgeLimit) {
		this.tChildrenAgeLimit = tChildrenAgeLimit;
	}



	public String gettEffectiveFromDate() {
		return tEffectiveFromDate;
	}



	public void settEffectiveFromDate(String tEffectiveFromDate) {
		this.tEffectiveFromDate = tEffectiveFromDate;
	}



	public Boolean getMemStatus() {
		return memStatus;
	}



	public void setMemStatus(Boolean memStatus) {
		this.memStatus = memStatus;
	}



	public String gettCMemberTypCreatedBy() {
		return tCMemberTypCreatedBy;
	}



	public void settCMemberTypCreatedBy(String tCMemberTypCreatedBy) {
		this.tCMemberTypCreatedBy = tCMemberTypCreatedBy;
	}



	public String getMembershipShowActive() {
		return membershipShowActive;
	}



	public void setMembershipShowActive(String membershipShowActive) {
		this.membershipShowActive = membershipShowActive;
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
