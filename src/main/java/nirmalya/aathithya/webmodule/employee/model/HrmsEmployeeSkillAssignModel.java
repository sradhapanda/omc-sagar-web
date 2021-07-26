package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmsEmployeeSkillAssignModel {

	private String employeeName;
	private String employeeSkill;
	private String employeeSkillDesc;
	private String skillId;
	private String employeeId;
	private String createdby;
	private String companyId;
	private String action;
	private String editId;
	private List<String> skillList = new ArrayList<String>();
	public HrmsEmployeeSkillAssignModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeSkill() {
		return employeeSkill;
	}
	public void setEmployeeSkill(String employeeSkill) {
		this.employeeSkill = employeeSkill;
	}
	public String getEmployeeSkillDesc() {
		return employeeSkillDesc;
	}
	public void setEmployeeSkillDesc(String employeeSkillDesc) {
		this.employeeSkillDesc = employeeSkillDesc;
	}
	public String getSkillId() {
		return skillId;
	}
	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
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
	public String getEditId() {
		return editId;
	}
	public void setEditId(String editId) {
		this.editId = editId;
	}
	public List<String> getSkillList() {
		return skillList;
	}
	public void setSkillList(List<String> skillList) {
		this.skillList = skillList;
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
