package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsJobTitleAssignModel {
	private String employeeNo;
	private String tDepartment;
	private String tJobTitle;
	private String tDeptAssignDesc;
	private Boolean tDeptAssignStatus;
	private String action;
	private String createdBy;
	private String companyId;
	private String isedit;
	private String deptId;
	private String jobTitleId;
	private List<String> roleList = new ArrayList<String>();
	public HrmsJobTitleAssignModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String gettDepartment() {
		return tDepartment;
	}

	public void settDepartment(String tDepartment) {
		this.tDepartment = tDepartment;
	}

	public String gettJobTitle() {
		return tJobTitle;
	}

	public void settJobTitle(String tJobTitle) {
		this.tJobTitle = tJobTitle;
	}

	public String gettDeptAssignDesc() {
		return tDeptAssignDesc;
	}

	public void settDeptAssignDesc(String tDeptAssignDesc) {
		this.tDeptAssignDesc = tDeptAssignDesc;
	}

	public Boolean gettDeptAssignStatus() {
		return tDeptAssignStatus;
	}

	public void settDeptAssignStatus(Boolean tDeptAssignStatus) {
		this.tDeptAssignStatus = tDeptAssignStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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
	
	

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(String jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
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
