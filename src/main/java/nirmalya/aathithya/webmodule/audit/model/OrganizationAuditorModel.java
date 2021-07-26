package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrganizationAuditorModel {

	private String tOrgId;
	private String tOrgName;
	private String tOrgFName;
	private String tOrgMobile;
	private String tOrgEmail;
	private String tUserType;
	private String tOrgCountry;
	private String tOrgDist;
	private String tOrgAddress;
	private Boolean tStatus;
	private String tOrgLName;
	private String tOrgPINno;
	private String tPassword;
	private String tAuthNo;
	private String tOrgState;
	private String tDesc;
	private String tIMEI;
	private String tCreatedBy;
	private String tCreatedOn;
	private String designation;
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	private String action;
	private String statusName;

	public OrganizationAuditorModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettOrgId() {
		return tOrgId;
	}

	public void settOrgId(String tOrgId) {
		this.tOrgId = tOrgId;
	}

	public String gettOrgName() {
		return tOrgName;
	}

	public void settOrgName(String tOrgName) {
		this.tOrgName = tOrgName;
	}

	public String gettOrgFName() {
		return tOrgFName;
	}

	public void settOrgFName(String tOrgFName) {
		this.tOrgFName = tOrgFName;
	}

	public String gettOrgMobile() {
		return tOrgMobile;
	}

	public void settOrgMobile(String tOrgMobile) {
		this.tOrgMobile = tOrgMobile;
	}

	public String gettOrgEmail() {
		return tOrgEmail;
	}

	public void settOrgEmail(String tOrgEmail) {
		this.tOrgEmail = tOrgEmail;
	}

	public String gettUserType() {
		return tUserType;
	}

	public void settUserType(String tUserType) {
		this.tUserType = tUserType;
	}

	public String gettOrgCountry() {
		return tOrgCountry;
	}

	public void settOrgCountry(String tOrgCountry) {
		this.tOrgCountry = tOrgCountry;
	}

	public String gettOrgDist() {
		return tOrgDist;
	}

	public void settOrgDist(String tOrgDist) {
		this.tOrgDist = tOrgDist;
	}

	public String gettOrgAddress() {
		return tOrgAddress;
	}

	public void settOrgAddress(String tOrgAddress) {
		this.tOrgAddress = tOrgAddress;
	}

	public Boolean gettStatus() {
		return tStatus;
	}

	public void settStatus(Boolean tStatus) {
		this.tStatus = tStatus;
	}

	public String gettOrgLName() {
		return tOrgLName;
	}

	public void settOrgLName(String tOrgLName) {
		this.tOrgLName = tOrgLName;
	}

	public String gettOrgPINno() {
		return tOrgPINno;
	}

	public void settOrgPINno(String tOrgPINno) {
		this.tOrgPINno = tOrgPINno;
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

	public String gettOrgState() {
		return tOrgState;
	}

	public void settOrgState(String tOrgState) {
		this.tOrgState = tOrgState;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
