package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEmployeeAppraisalFormModel {
	private Integer tAppraisalSetupId;
	private String empId;
	private String empName;
	private String dept;
	private String jobTitle;
	private String fromDate;
	private String toDate;

	private Integer dueDate;
	private String frequency;
	private String createdDate;
	List<HrmsEmployeeAppraisalFormListModel> appraisalList;
	private String tCompanyId;
	private String tCreatedBy;
	private String isedit;
	private String action;
	private String userId;
	private Integer count;
	private Boolean tStatus;
	private String goal;
	private String kraMeasure;
	private Double target;
	private Double selfMarked;
	private String goalName;
	private String deptName;
	private String jobName;
	private String empDueDate;
	

	public HrmsEmployeeAppraisalFormModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpDueDate() {
		return empDueDate;
	}

	public void setEmpDueDate(String empDueDate) {
		this.empDueDate = empDueDate;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Integer gettAppraisalSetupId() {
		return tAppraisalSetupId;
	}

	public void settAppraisalSetupId(Integer tAppraisalSetupId) {
		this.tAppraisalSetupId = tAppraisalSetupId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getDueDate() {
		return dueDate;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getKraMeasure() {
		return kraMeasure;
	}

	public void setKraMeasure(String kraMeasure) {
		this.kraMeasure = kraMeasure;
	}

	public Double getTarget() {
		return target;
	}

	public void setTarget(Double target) {
		this.target = target;
	}

	public Double getSelfMarked() {
		return selfMarked;
	}

	public void setSelfMarked(Double selfMarked) {
		this.selfMarked = selfMarked;
	}

	public void setDueDate(Integer dueDate) {
		this.dueDate = dueDate;
	}

	public String gettCompanyId() {
		return tCompanyId;
	}

	public void settCompanyId(String tCompanyId) {
		this.tCompanyId = tCompanyId;
	}

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public List<HrmsEmployeeAppraisalFormListModel> getAppraisalList() {
		return appraisalList;
	}

	public void setAppraisalList(List<HrmsEmployeeAppraisalFormListModel> appraisalList) {
		this.appraisalList = appraisalList;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
	}

	public Boolean gettStatus() {
		return tStatus;
	}

	public void settStatus(Boolean tStatus) {
		this.tStatus = tStatus;
	}

	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
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
