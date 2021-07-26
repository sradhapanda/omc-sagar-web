package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AddAuditPartyNewMoldel {

	private String tAduitNewId;
	private String tAduitNewName;
	private String tAduitNewFName;
	private String tAduitNewLName;
	private String tAduitNewMobile;
	private String tAduitNewEmail;
	private String tUserType;
	private String tAduitNewCountry;
	private String tAduitNewDist;
	private String tAduitNewAddress;
	private String tStatus;
	
	private String tAduitNewPINno;
	private String tPassword;
	private String tAuthNo;
	private String tAduitNewState;
	private String tDesc;
	private String tIMEI;
	private String tCreatedBy;
	private String tCreatedOn;
	private String designation;
	private String tUpdatedBy;
	private String tUpdatedOn;
	private String option;
	
	
	public String gettAduitNewId() {
		return tAduitNewId;
	}

	public void settAduitNewId(String tAduitNewId) {
		this.tAduitNewId = tAduitNewId;
	}

	public String gettAduitNewName() {
		return tAduitNewName;
	}

	public void settAduitNewName(String tAduitNewName) {
		this.tAduitNewName = tAduitNewName;
	}

	public String gettAduitNewFName() {
		return tAduitNewFName;
	}

	public void settAduitNewFName(String tAduitNewFName) {
		this.tAduitNewFName = tAduitNewFName;
	}

	public String gettAduitNewLName() {
		return tAduitNewLName;
	}

	public void settAduitNewLName(String tAduitNewLName) {
		this.tAduitNewLName = tAduitNewLName;
	}

	public String gettAduitNewMobile() {
		return tAduitNewMobile;
	}

	public void settAduitNewMobile(String tAduitNewMobile) {
		this.tAduitNewMobile = tAduitNewMobile;
	}

	public String gettAduitNewEmail() {
		return tAduitNewEmail;
	}

	public void settAduitNewEmail(String tAduitNewEmail) {
		this.tAduitNewEmail = tAduitNewEmail;
	}

	public String gettUserType() {
		return tUserType;
	}

	public void settUserType(String tUserType) {
		this.tUserType = tUserType;
	}

	public String gettAduitNewCountry() {
		return tAduitNewCountry;
	}

	public void settAduitNewCountry(String tAduitNewCountry) {
		this.tAduitNewCountry = tAduitNewCountry;
	}

	public String gettAduitNewDist() {
		return tAduitNewDist;
	}

	public void settAduitNewDist(String tAduitNewDist) {
		this.tAduitNewDist = tAduitNewDist;
	}

	public String gettAduitNewAddress() {
		return tAduitNewAddress;
	}

	public void settAduitNewAddress(String tAduitNewAddress) {
		this.tAduitNewAddress = tAduitNewAddress;
	}

	public String gettStatus() {
		return tStatus;
	}

	public void settStatus(String tStatus) {
		this.tStatus = tStatus;
	}

	public String gettAduitNewPINno() {
		return tAduitNewPINno;
	}

	public void settAduitNewPINno(String tAduitNewPINno) {
		this.tAduitNewPINno = tAduitNewPINno;
	}

	public String gettPassword() {
		return tPassword;
	}

	public void settPassword(String tPassword) {
		this.tPassword = tPassword;
	}

	public String gettAuthNo() {
		return tAuthNo;
	}

	public void settAuthNo(String tAuthNo) {
		this.tAuthNo = tAuthNo;
	}

	public String gettAduitNewState() {
		return tAduitNewState;
	}

	public void settAduitNewState(String tAduitNewState) {
		this.tAduitNewState = tAduitNewState;
	}

	public String gettDesc() {
		return tDesc;
	}

	public void settDesc(String tDesc) {
		this.tDesc = tDesc;
	}

	public String gettIMEI() {
		return tIMEI;
	}

	public void settIMEI(String tIMEI) {
		this.tIMEI = tIMEI;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
	}

	public String gettCreatedOn() {
		return tCreatedOn;
	}

	public void settCreatedOn(String tCreatedOn) {
		this.tCreatedOn = tCreatedOn;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String gettUpdatedBy() {
		return tUpdatedBy;
	}

	public void settUpdatedBy(String tUpdatedBy) {
		this.tUpdatedBy = tUpdatedBy;
	}

	public String gettUpdatedOn() {
		return tUpdatedOn;
	}

	public void settUpdatedOn(String tUpdatedOn) {
		this.tUpdatedOn = tUpdatedOn;
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

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
