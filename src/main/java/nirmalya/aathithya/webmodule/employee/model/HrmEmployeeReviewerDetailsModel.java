package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmEmployeeReviewerDetailsModel {

	private String tUserRole;
	private String tEmployee;
	private Integer tSetAuthorityStageNo;
	private String tSetAuthorityTAT;
	private String tSetAuthorityNFA;
	private String tAppraisalFrequency;

	private Integer tAppraisalDueDate;

	private String tCompanyId;
	private String tSetAuthorityCreatedBy;
	private String isedit;
	private String tStaff;
	private String staff;

	private String tNFA;
	private String action;
	private String userId;
	private String financialDate;
	private String financialFromDate;
	private String financialToDate;
	private List<String> roleList = new ArrayList<String>();
	private List<String> roleName = new ArrayList<String>();
	private List<String> tat = new ArrayList<String>();

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public List<String> getRoleName() {
		return roleName;
	}

	public void setRoleName(List<String> roleName) {
		this.roleName = roleName;
	}

	public List<String> getTat() {
		return tat;
	}

	public void setTat(List<String> tat) {
		this.tat = tat;
	}

	public HrmEmployeeReviewerDetailsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettUserRole() {
		return tUserRole;
	}

	public void settUserRole(String tUserRole) {
		this.tUserRole = tUserRole;
	}

	public String gettEmployee() {
		return tEmployee;
	}

	public void settEmployee(String tEmployee) {
		this.tEmployee = tEmployee;
	}

	public Integer gettSetAuthorityStageNo() {
		return tSetAuthorityStageNo;
	}

	public void settSetAuthorityStageNo(Integer tSetAuthorityStageNo) {
		this.tSetAuthorityStageNo = tSetAuthorityStageNo;
	}

	public String gettSetAuthorityTAT() {
		return tSetAuthorityTAT;
	}

	public void settSetAuthorityTAT(String tSetAuthorityTAT) {
		this.tSetAuthorityTAT = tSetAuthorityTAT;
	}

	public String gettSetAuthorityNFA() {
		return tSetAuthorityNFA;
	}

	public void settSetAuthorityNFA(String tSetAuthorityNFA) {
		this.tSetAuthorityNFA = tSetAuthorityNFA;
	}

	public String getFinancialDate() {
		return financialDate;
	}

	public void setFinancialDate(String financialDate) {
		this.financialDate = financialDate;
	}

	public Integer gettAppraisalDueDate() {
		return tAppraisalDueDate;
	}

	public void settAppraisalDueDate(Integer tAppraisalDueDate) {
		this.tAppraisalDueDate = tAppraisalDueDate;
	}

	public String gettCompanyId() {
		return tCompanyId;
	}

	public void settCompanyId(String tCompanyId) {
		this.tCompanyId = tCompanyId;
	}

	public String gettSetAuthorityCreatedBy() {
		return tSetAuthorityCreatedBy;
	}

	public void settSetAuthorityCreatedBy(String tSetAuthorityCreatedBy) {
		this.tSetAuthorityCreatedBy = tSetAuthorityCreatedBy;
	}

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}

	public String gettStaff() {
		return tStaff;
	}

	public void settStaff(String tStaff) {
		this.tStaff = tStaff;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String gettNFA() {
		return tNFA;
	}

	public void settNFA(String tNFA) {
		this.tNFA = tNFA;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String gettAppraisalFrequency() {
		return tAppraisalFrequency;
	}

	public void settAppraisalFrequency(String tAppraisalFrequency) {
		this.tAppraisalFrequency = tAppraisalFrequency;
	}

	public String getFinancialFromDate() {
		return financialFromDate;
	}

	public void setFinancialFromDate(String financialFromDate) {
		this.financialFromDate = financialFromDate;
	}

	public String getFinancialToDate() {
		return financialToDate;
	}

	public void setFinancialToDate(String financialToDate) {
		this.financialToDate = financialToDate;
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