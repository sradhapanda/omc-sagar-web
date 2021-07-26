package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEmployeeModel {
	private String employeeNo;
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
	private String employeeSupervisor;
	private String employeeIndSupervisor;
	private String employeeFirstApp;
	private String employeeSecApp;
	private String employeeThirdApp;
	private String employeeNotes;
	private String createdBy;
	private String companyId;
	private String action;
	private String employeepPin;
	private Boolean status;
	private String statusName;
	private String empDesignation;
	private List<String> roleList = new ArrayList<String>();
	private String empDesignationName;
	private String employeeState;
	private String employeeCountyName;
	private String employeeStateName;
	private String employeeGenderName;
	private String employeeCountryName;
	private String employeePaygradeName;
	private String employeeMaritalName;
	private String employeeRegion;
	public HrmsEmployeeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
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

	public String getEmployeeRegion() {
		return employeeRegion;
	}

	public void setEmployeeRegion(String employeeRegion) {
		this.employeeRegion = employeeRegion;
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

	public String getEmployeePassword() {
		return employeePassword;
	}

	public void setEmployeePassword(String employeePassword) {
		this.employeePassword = employeePassword;
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

	public String getEmployeeSupervisor() {
		return employeeSupervisor;
	}

	public void setEmployeeSupervisor(String employeeSupervisor) {
		this.employeeSupervisor = employeeSupervisor;
	}

	public String getEmployeeIndSupervisor() {
		return employeeIndSupervisor;
	}

	public void setEmployeeIndSupervisor(String employeeIndSupervisor) {
		this.employeeIndSupervisor = employeeIndSupervisor;
	}

	public String getEmployeeFirstApp() {
		return employeeFirstApp;
	}

	public void setEmployeeFirstApp(String employeeFirstApp) {
		this.employeeFirstApp = employeeFirstApp;
	}

	public String getEmployeeSecApp() {
		return employeeSecApp;
	}

	public void setEmployeeSecApp(String employeeSecApp) {
		this.employeeSecApp = employeeSecApp;
	}

	public String getEmployeeThirdApp() {
		return employeeThirdApp;
	}

	public void setEmployeeThirdApp(String employeeThirdApp) {
		this.employeeThirdApp = employeeThirdApp;
	}

	public String getEmployeeNotes() {
		return employeeNotes;
	}

	public void setEmployeeNotes(String employeeNotes) {
		this.employeeNotes = employeeNotes;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEmployeeAdhar() {
		return employeeAdhar;
	}

	public void setEmployeeAdhar(String employeeAdhar) {
		this.employeeAdhar = employeeAdhar;
	}

	public String getEmployeeEsicNo() {
		return employeeEsicNo;
	}

	public void setEmployeeEsicNo(String employeeEsicNo) {
		this.employeeEsicNo = employeeEsicNo;
	}

	public String getEmployeepPin() {
		return employeepPin;
	}

	public void setEmployeepPin(String employeepPin) {
		this.employeepPin = employeepPin;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	public String getEmpDesignationName() {
		return empDesignationName;
	}

	public String getEmployeeState() {
		return employeeState;
	}

	public void setEmployeeState(String employeeState) {
		this.employeeState = employeeState;
	}

	public String getEmployeeCountyName() {
		return employeeCountyName;
	}

	public void setEmployeeCountyName(String employeeCountyName) {
		this.employeeCountyName = employeeCountyName;
	}

	public String getEmployeeStateName() {
		return employeeStateName;
	}

	public void setEmployeeStateName(String employeeStateName) {
		this.employeeStateName = employeeStateName;
	}

	public String getEmployeeGenderName() {
		return employeeGenderName;
	}

	public void setEmployeeGenderName(String employeeGenderName) {
		this.employeeGenderName = employeeGenderName;
	}

	public String getEmployeeCountryName() {
		return employeeCountryName;
	}

	public void setEmployeeCountryName(String employeeCountryName) {
		this.employeeCountryName = employeeCountryName;
	}

	public String getEmployeePaygradeName() {
		return employeePaygradeName;
	}

	public void setEmployeePaygradeName(String employeePaygradeName) {
		this.employeePaygradeName = employeePaygradeName;
	}

	public String getEmployeeMaritalName() {
		return employeeMaritalName;
	}

	public void setEmployeeMaritalName(String employeeMaritalName) {
		this.employeeMaritalName = employeeMaritalName;
	}

	public void setEmpDesignationName(String empDesignationName) {
		this.empDesignationName = empDesignationName;
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
