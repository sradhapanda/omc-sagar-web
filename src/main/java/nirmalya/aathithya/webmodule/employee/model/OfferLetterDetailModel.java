package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OfferLetterDetailModel {

	private String tOfferLetterId;
	private String tEmpFName;
	private String tEmpMName;
	private String tEmpLName;
	private String tEmpDOB;
	private String tNationality;
	private String tMobileNo;
	private String tEmpImage;
	private String tEmpGender;
	private String tEmpMarritalStatus;
	private String tJobTitle;
	private String tPayGrade;
	private String tCountry;
	private String tState;
	private String tDistrict;
	private String tPinCode;
	private String tAddress;
	private String tEmailId;
	private String tEmpDOJ;
	private Double tAnnualCTC;
	private String tComponentId;
	private Double tAnnualAmount;
	private String salaryComponent;
	private Integer calculationType;
	private Double monthlyAmnt;
	private Double annualAmnt;
	private Double variableAmnt;
	private String tCompanyId;
	private String tCreatedBy;
	private String action;
	private Integer calculationTypeId;

	public OfferLetterDetailModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCalculationTypeId() {
		return calculationTypeId;
	}

	public void setCalculationTypeId(Integer calculationTypeId) {
		this.calculationTypeId = calculationTypeId;
	}

	public String gettOfferLetterId() {
		return tOfferLetterId;
	}

	public void settOfferLetterId(String tOfferLetterId) {
		this.tOfferLetterId = tOfferLetterId;
	}

	public String gettEmpFName() {
		return tEmpFName;
	}

	public void settEmpFName(String tEmpFName) {
		this.tEmpFName = tEmpFName;
	}

	public String gettEmpMName() {
		return tEmpMName;
	}

	public void settEmpMName(String tEmpMName) {
		this.tEmpMName = tEmpMName;
	}

	public String gettEmpLName() {
		return tEmpLName;
	}

	public void settEmpLName(String tEmpLName) {
		this.tEmpLName = tEmpLName;
	}

	public String gettEmpDOB() {
		return tEmpDOB;
	}

	public void settEmpDOB(String tEmpDOB) {
		this.tEmpDOB = tEmpDOB;
	}

	public String gettNationality() {
		return tNationality;
	}

	public void settNationality(String tNationality) {
		this.tNationality = tNationality;
	}

	public String gettMobileNo() {
		return tMobileNo;
	}

	public void settMobileNo(String tMobileNo) {
		this.tMobileNo = tMobileNo;
	}

	public String gettEmpImage() {
		return tEmpImage;
	}

	public void settEmpImage(String tEmpImage) {
		this.tEmpImage = tEmpImage;
	}

	public String gettEmpGender() {
		return tEmpGender;
	}

	public void settEmpGender(String tEmpGender) {
		this.tEmpGender = tEmpGender;
	}

	public String gettEmpMarritalStatus() {
		return tEmpMarritalStatus;
	}

	public void settEmpMarritalStatus(String tEmpMarritalStatus) {
		this.tEmpMarritalStatus = tEmpMarritalStatus;
	}

	public String gettJobTitle() {
		return tJobTitle;
	}

	public void settJobTitle(String tJobTitle) {
		this.tJobTitle = tJobTitle;
	}

	public String gettPayGrade() {
		return tPayGrade;
	}

	public void settPayGrade(String tPayGrade) {
		this.tPayGrade = tPayGrade;
	}

	public String gettCountry() {
		return tCountry;
	}

	public void settCountry(String tCountry) {
		this.tCountry = tCountry;
	}

	public String gettState() {
		return tState;
	}

	public void settState(String tState) {
		this.tState = tState;
	}

	public String gettDistrict() {
		return tDistrict;
	}

	public void settDistrict(String tDistrict) {
		this.tDistrict = tDistrict;
	}

	public String gettPinCode() {
		return tPinCode;
	}

	public void settPinCode(String tPinCode) {
		this.tPinCode = tPinCode;
	}

	public String gettAddress() {
		return tAddress;
	}

	public void settAddress(String tAddress) {
		this.tAddress = tAddress;
	}

	public String gettEmailId() {
		return tEmailId;
	}

	public void settEmailId(String tEmailId) {
		this.tEmailId = tEmailId;
	}

	public String gettEmpDOJ() {
		return tEmpDOJ;
	}

	public void settEmpDOJ(String tEmpDOJ) {
		this.tEmpDOJ = tEmpDOJ;
	}

	public Double gettAnnualCTC() {
		return tAnnualCTC;
	}

	public void settAnnualCTC(Double tAnnualCTC) {
		this.tAnnualCTC = tAnnualCTC;
	}

	public String gettComponentId() {
		return tComponentId;
	}

	public void settComponentId(String tComponentId) {
		this.tComponentId = tComponentId;
	}

	public Double gettAnnualAmount() {
		return tAnnualAmount;
	}

	public void settAnnualAmount(Double tAnnualAmount) {
		this.tAnnualAmount = tAnnualAmount;
	}

	public String getSalaryComponent() {
		return salaryComponent;
	}

	public void setSalaryComponent(String salaryComponent) {
		this.salaryComponent = salaryComponent;
	}

	public Integer getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(Integer calculationType) {
		this.calculationType = calculationType;
	}

	public Double getMonthlyAmnt() {
		return monthlyAmnt;
	}

	public void setMonthlyAmnt(Double monthlyAmnt) {
		this.monthlyAmnt = monthlyAmnt;
	}

	public Double getAnnualAmnt() {
		return annualAmnt;
	}

	public void setAnnualAmnt(Double annualAmnt) {
		this.annualAmnt = annualAmnt;
	}

	public Double getVariableAmnt() {
		return variableAmnt;
	}

	public void setVariableAmnt(Double variableAmnt) {
		this.variableAmnt = variableAmnt;
	}

	public String gettCompanyId() {
		return tCompanyId;
	}

	public void settCompanyId(String tCompanyId) {
		this.tCompanyId = tCompanyId;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
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
