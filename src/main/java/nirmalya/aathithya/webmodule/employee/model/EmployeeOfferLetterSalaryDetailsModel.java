package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeeOfferLetterSalaryDetailsModel {
	// salry components details
	private String tEmployeeId;
	private Double tAnnualCTC;
	private String tSalaryComponent;
	private String tEffectiveDate;
	private Double tSalaryComponentValue;
	private String tPayGrade;
	private String tJobTitle;
	private Double tBasicPay;
	private Double tDearnessAllowance;
	private Double tHouseRentAllowance;
	private Double tConveyanceAllowance;
	private Double tLeaveTravelAllowance;
	private Double tMedicalAllowance;
	private Double tEmployeeProvidentFund;
	private Double tESICscheme;
	private Double tPerquisites;
	private String tSalaryComponentName;
	// employee details
	private String employeeLetterId;
	private String employeeFname;
	private String employeeMname;
	private String employeeLname;
	private String employeeNationality;
	private String employeeDob;
	private String employeeImage;
	private String employeeGender;
	private String employeeMarital;
	private String employeeEsicNo;
	private String employeePassword;
	private String employeeAdhar;
	private String employeeOtherId;
	private String employeeDl;
	private String employeeEmployment;
	private String employeeJobTitle;
	private String employeePayGrade;
	private String employeeWorkStationId;
	private String employeeAddress;
	private String employeeCity;
	private String employeeState;
	private String employeeCountry;
	private String employeePin;
	private String employeeHomePhoneNo;
	private String employeeMobilePhoneNo;
	private String employeeWorkPhoneNo;
	private String employeeWorkEmail;
	private String employeePersonalEmail;
	private String employeeJoinDate;
	private String employeeConfirmDate;
	private String employeeTerminateDate;
	private String employeeDepartment;
	private String empDesignation;
	private String createdBy;
	private String companyId;
	private Boolean status;
	private String action;
	private String passPin;
	private Double tTaxableAmount;
	private Double tPayableAmount;
	private List<String> roleList = new ArrayList<String>();

	public EmployeeOfferLetterSalaryDetailsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Double gettTaxableAmount() {
		return tTaxableAmount;
	}

	public void settTaxableAmount(Double tTaxableAmount) {
		this.tTaxableAmount = tTaxableAmount;
	}

	public Double gettPayableAmount() {
		return tPayableAmount;
	}

	public void settPayableAmount(Double tPayableAmount) {
		this.tPayableAmount = tPayableAmount;
	}

	public String gettEmployeeId() {
		return tEmployeeId;
	}

	public void settEmployeeId(String tEmployeeId) {
		this.tEmployeeId = tEmployeeId;
	}

	public Double gettAnnualCTC() {
		return tAnnualCTC;
	}

	public void settAnnualCTC(Double tAnnualCTC) {
		this.tAnnualCTC = tAnnualCTC;
	}

	public String getEmployeeLetterId() {
		return employeeLetterId;
	}

	public void setEmployeeLetterId(String employeeLetterId) {
		this.employeeLetterId = employeeLetterId;
	}

	public String getPassPin() {
		return passPin;
	}

	public void setPassPin(String passPin) {
		this.passPin = passPin;
	}

	public String gettSalaryComponent() {
		return tSalaryComponent;
	}

	public void settSalaryComponent(String tSalaryComponent) {
		this.tSalaryComponent = tSalaryComponent;
	}

	public String gettEffectiveDate() {
		return tEffectiveDate;
	}

	public void settEffectiveDate(String tEffectiveDate) {
		this.tEffectiveDate = tEffectiveDate;
	}

	public Double gettSalaryComponentValue() {
		return tSalaryComponentValue;
	}

	public void settSalaryComponentValue(Double tSalaryComponentValue) {
		this.tSalaryComponentValue = tSalaryComponentValue;
	}

	public String gettPayGrade() {
		return tPayGrade;
	}

	public void settPayGrade(String tPayGrade) {
		this.tPayGrade = tPayGrade;
	}

	public String gettJobTitle() {
		return tJobTitle;
	}

	public void settJobTitle(String tJobTitle) {
		this.tJobTitle = tJobTitle;
	}

	public Double gettBasicPay() {
		return tBasicPay;
	}

	public void settBasicPay(Double tBasicPay) {
		this.tBasicPay = tBasicPay;
	}

	public Double gettDearnessAllowance() {
		return tDearnessAllowance;
	}

	public void settDearnessAllowance(Double tDearnessAllowance) {
		this.tDearnessAllowance = tDearnessAllowance;
	}

	public Double gettHouseRentAllowance() {
		return tHouseRentAllowance;
	}

	public void settHouseRentAllowance(Double tHouseRentAllowance) {
		this.tHouseRentAllowance = tHouseRentAllowance;
	}

	public Double gettConveyanceAllowance() {
		return tConveyanceAllowance;
	}

	public void settConveyanceAllowance(Double tConveyanceAllowance) {
		this.tConveyanceAllowance = tConveyanceAllowance;
	}

	public Double gettLeaveTravelAllowance() {
		return tLeaveTravelAllowance;
	}

	public void settLeaveTravelAllowance(Double tLeaveTravelAllowance) {
		this.tLeaveTravelAllowance = tLeaveTravelAllowance;
	}

	public Double gettMedicalAllowance() {
		return tMedicalAllowance;
	}

	public void settMedicalAllowance(Double tMedicalAllowance) {
		this.tMedicalAllowance = tMedicalAllowance;
	}

	public Double gettEmployeeProvidentFund() {
		return tEmployeeProvidentFund;
	}

	public void settEmployeeProvidentFund(Double tEmployeeProvidentFund) {
		this.tEmployeeProvidentFund = tEmployeeProvidentFund;
	}

	public Double gettESICscheme() {
		return tESICscheme;
	}

	public void settESICscheme(Double tESICscheme) {
		this.tESICscheme = tESICscheme;
	}

	public Double gettPerquisites() {
		return tPerquisites;
	}

	public void settPerquisites(Double tPerquisites) {
		this.tPerquisites = tPerquisites;
	}

	public String gettSalaryComponentName() {
		return tSalaryComponentName;
	}

	public void settSalaryComponentName(String tSalaryComponentName) {
		this.tSalaryComponentName = tSalaryComponentName;
	}

	public String getEmployeeFname() {
		return employeeFname;
	}

	public void setEmployeeFname(String employeeFname) {
		this.employeeFname = employeeFname;
	}

	public String getEmployeeMname() {
		return employeeMname;
	}

	public void setEmployeeMname(String employeeMname) {
		this.employeeMname = employeeMname;
	}

	public String getEmployeeLname() {
		return employeeLname;
	}

	public void setEmployeeLname(String employeeLname) {
		this.employeeLname = employeeLname;
	}

	public String getEmployeeNationality() {
		return employeeNationality;
	}

	public void setEmployeeNationality(String employeeNationality) {
		this.employeeNationality = employeeNationality;
	}

	public String getEmployeeDob() {
		return employeeDob;
	}

	public void setEmployeeDob(String employeeDob) {
		this.employeeDob = employeeDob;
	}

	public String getEmployeeImage() {
		return employeeImage;
	}

	public void setEmployeeImage(String employeeImage) {
		this.employeeImage = employeeImage;
	}

	public String getEmployeeGender() {
		return employeeGender;
	}

	public void setEmployeeGender(String employeeGender) {
		this.employeeGender = employeeGender;
	}

	public String getEmployeeMarital() {
		return employeeMarital;
	}

	public void setEmployeeMarital(String employeeMarital) {
		this.employeeMarital = employeeMarital;
	}

	public String getEmployeeEsicNo() {
		return employeeEsicNo;
	}

	public void setEmployeeEsicNo(String employeeEsicNo) {
		this.employeeEsicNo = employeeEsicNo;
	}

	public String getEmployeePassword() {
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
	}

	public String getEmployeeAdhar() {
		return employeeAdhar;
	}

	public void setEmployeeAdhar(String employeeAdhar) {
		this.employeeAdhar = employeeAdhar;
	}

	public String getEmployeeOtherId() {
		return employeeOtherId;
	}

	public void setEmployeeOtherId(String employeeOtherId) {
		this.employeeOtherId = employeeOtherId;
	}

	public String getEmployeeDl() {
		return employeeDl;
	}

	public void setEmployeeDl(String employeeDl) {
		this.employeeDl = employeeDl;
	}

	public String getEmployeeEmployment() {
		return employeeEmployment;
	}

	public void setEmployeeEmployment(String employeeEmployment) {
		this.employeeEmployment = employeeEmployment;
	}

	public String getEmployeeJobTitle() {
		return employeeJobTitle;
	}

	public void setEmployeeJobTitle(String employeeJobTitle) {
		this.employeeJobTitle = employeeJobTitle;
	}

	public String getEmployeePayGrade() {
		return employeePayGrade;
	}

	public void setEmployeePayGrade(String employeePayGrade) {
		this.employeePayGrade = employeePayGrade;
	}

	public String getEmployeeWorkStationId() {
		return employeeWorkStationId;
	}

	public void setEmployeeWorkStationId(String employeeWorkStationId) {
		this.employeeWorkStationId = employeeWorkStationId;
	}

	public String getEmployeeAddress() {
		return employeeAddress;
	}

	public void setEmployeeAddress(String employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

	public String getEmployeeCity() {
		return employeeCity;
	}

	public void setEmployeeCity(String employeeCity) {
		this.employeeCity = employeeCity;
	}

	public String getEmployeeState() {
		return employeeState;
	}

	public void setEmployeeState(String employeeState) {
		this.employeeState = employeeState;
	}

	public String getEmployeeCountry() {
		return employeeCountry;
	}

	public void setEmployeeCountry(String employeeCountry) {
		this.employeeCountry = employeeCountry;
	}

	public String getEmployeePin() {
		return employeePin;
	}

	public void setEmployeePin(String employeePin) {
		this.employeePin = employeePin;
	}

	public String getEmployeeHomePhoneNo() {
		return employeeHomePhoneNo;
	}

	public void setEmployeeHomePhoneNo(String employeeHomePhoneNo) {
		this.employeeHomePhoneNo = employeeHomePhoneNo;
	}

	public String getEmployeeMobilePhoneNo() {
		return employeeMobilePhoneNo;
	}

	public void setEmployeeMobilePhoneNo(String employeeMobilePhoneNo) {
		this.employeeMobilePhoneNo = employeeMobilePhoneNo;
	}

	public String getEmployeeWorkPhoneNo() {
		return employeeWorkPhoneNo;
	}

	public void setEmployeeWorkPhoneNo(String employeeWorkPhoneNo) {
		this.employeeWorkPhoneNo = employeeWorkPhoneNo;
	}

	public String getEmployeeWorkEmail() {
		return employeeWorkEmail;
	}

	public void setEmployeeWorkEmail(String employeeWorkEmail) {
		this.employeeWorkEmail = employeeWorkEmail;
	}

	public String getEmployeePersonalEmail() {
		return employeePersonalEmail;
	}

	public void setEmployeePersonalEmail(String employeePersonalEmail) {
		this.employeePersonalEmail = employeePersonalEmail;
	}

	public String getEmployeeJoinDate() {
		return employeeJoinDate;
	}

	public void setEmployeeJoinDate(String employeeJoinDate) {
		this.employeeJoinDate = employeeJoinDate;
	}

	public String getEmployeeConfirmDate() {
		return employeeConfirmDate;
	}

	public void setEmployeeConfirmDate(String employeeConfirmDate) {
		this.employeeConfirmDate = employeeConfirmDate;
	}

	public String getEmployeeTerminateDate() {
		return employeeTerminateDate;
	}

	public void setEmployeeTerminateDate(String employeeTerminateDate) {
		this.employeeTerminateDate = employeeTerminateDate;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
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
