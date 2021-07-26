package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author NirmalyaLabs
 *
 */
public class GodownMasterModel {

	private String tGodown;
	private String tGodownName;
	private String tGodownDescription;
	private Boolean tGodownActive;
	private String tAddress;
	private String tCity;
	private String tDistrict;
	private String tState;
	private String tCountry;
	private String tPinCode;
	private String tPhoneNo;
	private String tGSTNo;
	private String tEmailId;
	private String tTinNo;
	private String tGodownLogo;
	private String createdBy;
	private String status;
	private String action;

	public GodownMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettGodown() {
		return tGodown;
	}

	public void settGodown(String tGodown) {
		this.tGodown = tGodown;
	}

	public String gettGodownName() {
		return tGodownName;
	}

	public void settGodownName(String tGodownName) {
		this.tGodownName = tGodownName;
	}

	public String gettGodownDescription() {
		return tGodownDescription;
	}

	public void settGodownDescription(String tGodownDescription) {
		this.tGodownDescription = tGodownDescription;
	}

	public Boolean gettGodownActive() {
		return tGodownActive;
	}

	public void settGodownActive(Boolean tGodownActive) {
		this.tGodownActive = tGodownActive;
	}

	public String gettAddress() {
		return tAddress;
	}

	public void settAddress(String tAddress) {
		this.tAddress = tAddress;
	}

	public String gettCity() {
		return tCity;
	}

	public void settCity(String tCity) {
		this.tCity = tCity;
	}

	public String gettDistrict() {
		return tDistrict;
	}

	public void settDistrict(String tDistrict) {
		this.tDistrict = tDistrict;
	}

	public String gettState() {
		return tState;
	}

	public void settState(String tState) {
		this.tState = tState;
	}

	public String gettCountry() {
		return tCountry;
	}

	public void settCountry(String tCountry) {
		this.tCountry = tCountry;
	}

	public String gettPinCode() {
		return tPinCode;
	}

	public void settPinCode(String tPinCode) {
		this.tPinCode = tPinCode;
	}

	public String gettPhoneNo() {
		return tPhoneNo;
	}

	public void settPhoneNo(String tPhoneNo) {
		this.tPhoneNo = tPhoneNo;
	}

	public String gettGSTNo() {
		return tGSTNo;
	}

	public void settGSTNo(String tGSTNo) {
		this.tGSTNo = tGSTNo;
	}

	public String gettEmailId() {
		return tEmailId;
	}

	public void settEmailId(String tEmailId) {
		this.tEmailId = tEmailId;
	}

	public String gettTinNo() {
		return tTinNo;
	}

	public void settTinNo(String tTinNo) {
		this.tTinNo = tTinNo;
	}

	public String gettGodownLogo() {
		return tGodownLogo;
	}

	public void settGodownLogo(String tGodownLogo) {
		this.tGodownLogo = tGodownLogo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
