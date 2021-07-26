package nirmalya.aathithya.webmodule.employee.model;

import java.math.BigInteger;

public class HrmsEmployeeCertificationModel {

	private String empId;
	private String certifId;
	private String insti;
	private String grantDate;
	private String validDate;
	private String editId;
	private String empName;
	private String certifName;
	private String action;
	private BigInteger slNo;
    private String createdBy;
    private String companyId;
    
	public HrmsEmployeeCertificationModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getCertifId() {
		return certifId;
	}

	public void setCertifId(String certifId) {
		this.certifId = certifId;
	}

	public String getInsti() {
		return insti;
	}

	public void setInsti(String insti) {
		this.insti = insti;
	}

	public String getGrantDate() {
		return grantDate;
	}

	public void setGrantDate(String grantDate) {
		this.grantDate = grantDate;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCertifName() {
		return certifName;
	}

	public void setCertifName(String certifName) {
		this.certifName = certifName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BigInteger getSlNo() {
		return slNo;
	}

	public void setSlNo(BigInteger slNo) {
		this.slNo = slNo;
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

}
