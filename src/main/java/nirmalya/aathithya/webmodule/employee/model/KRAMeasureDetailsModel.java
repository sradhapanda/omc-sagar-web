package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KRAMeasureDetailsModel {

	private String kDepartment;
	private String kJobTitle;
	private String kGoal;
	private String kMeasure;
	private Double kTarget;
	private String kCompanyId;
	private String kCreatedBy;
	private String isedit;
	private String deptId;
	private String jobTitleId;
	private String action;
	private String status;

	public KRAMeasureDetailsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getkDepartment() {
		return kDepartment;
	}

	public void setkDepartment(String kDepartment) {
		this.kDepartment = kDepartment;
	}

	public String getkJobTitle() {
		return kJobTitle;
	}

	public void setkJobTitle(String kJobTitle) {
		this.kJobTitle = kJobTitle;
	}

	public String getkGoal() {
		return kGoal;
	}

	public void setkGoal(String kGoal) {
		this.kGoal = kGoal;
	}

	public String getkMeasure() {
		return kMeasure;
	}

	public void setkMeasure(String kMeasure) {
		this.kMeasure = kMeasure;
	}


	public Double getkTarget() {
		return kTarget;
	}

	public void setkTarget(Double kTarget) {
		this.kTarget = kTarget;
	}

	public String getkCompanyId() {
		return kCompanyId;
	}

	public void setkCompanyId(String kCompanyId) {
		this.kCompanyId = kCompanyId;
	}

	public String getkCreatedBy() {
		return kCreatedBy;
	}

	public void setkCreatedBy(String kCreatedBy) {
		this.kCreatedBy = kCreatedBy;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
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
