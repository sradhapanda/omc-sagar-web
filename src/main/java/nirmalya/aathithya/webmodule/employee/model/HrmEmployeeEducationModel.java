package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HrmEmployeeEducationModel {

	private String empId;
	private String qualifId;
	private String insti;
	private String startDate;
	private String endDate;
	private String editId;
	private String emplName;
	private String qualifName;
	private String action;
	private String createdBy;
	private String companyId;
	private BigInteger slNo;

	public HrmEmployeeEducationModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getQualifId() {
		return qualifId;
	}

	public void setQualifId(String qualifId) {
		this.qualifId = qualifId;
	}

	public String getInsti() {
		return insti;
	}

	public void setInsti(String insti) {
		this.insti = insti;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getEmplName() {
		return emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}

	public String getQualifName() {
		return qualifName;
	}

	public void setQualifName(String qualifName) {
		this.qualifName = qualifName;
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

	public BigInteger getSlNo() {
		return slNo;
	}

	public void setSlNo(BigInteger slNo) {
		this.slNo = slNo;
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
