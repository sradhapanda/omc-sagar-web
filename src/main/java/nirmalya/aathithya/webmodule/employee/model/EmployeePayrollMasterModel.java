package nirmalya.aathithya.webmodule.employee.model;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EmployeePayrollMasterModel {
	private String tId;
	private String tEmployeeId;
	private String tEmployeeName;
	private Double tNetSalary;
	private String tPayDate;
	private String tPayGrade;
	private BigDecimal tLeaveDays;
	private Double tBasicPay;
	private Double tDearnessAllowance;
	private Double tHouseRentAllowance;
	private Double tConveyanceAllowance;
	private Double tLeaveTravelAllowance;
	private Double tMedicalAllowance;
	private Double tEmployeeProvidentFund;
	private Double tESICscheme;
	private Double tEarnings;
	private Double tDeductions;
	private Boolean tStatus;
	private String tCreatedBy;
	private String status;
	private String action;
	private Integer approvalStatus;
	private Integer lastDay;
	private Double tCTC;
	private String tCity;
	private String tMobile;
	private String tEmailId;
	private BigDecimal tTaxableAmnt;
	private Double tPerquisites;
	private Integer currentStageNo;
	private Integer approverStageNo;
	private Integer currentLevelNo;
	private Integer approverLevelNo;
	private Byte approveStatus;
	private String approveStatusName;

	public EmployeePayrollMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCurrentStageNo() {
		return currentStageNo;
	}

	public void setCurrentStageNo(Integer currentStageNo) {
		this.currentStageNo = currentStageNo;
	}

	public Integer getApproverStageNo() {
		return approverStageNo;
	}

	public void setApproverStageNo(Integer approverStageNo) {
		this.approverStageNo = approverStageNo;
	}

	public Integer getCurrentLevelNo() {
		return currentLevelNo;
	}

	public void setCurrentLevelNo(Integer currentLevelNo) {
		this.currentLevelNo = currentLevelNo;
	}

	public Integer getApproverLevelNo() {
		return approverLevelNo;
	}

	public void setApproverLevelNo(Integer approverLevelNo) {
		this.approverLevelNo = approverLevelNo;
	}

	public Byte getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Byte approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getApproveStatusName() {
		return approveStatusName;
	}

	public void setApproveStatusName(String approveStatusName) {
		this.approveStatusName = approveStatusName;
	}

	public Double gettPerquisites() {
		return tPerquisites;
	}

	public void settPerquisites(Double tPerquisites) {
		this.tPerquisites = tPerquisites;
	}

	public Double gettCTC() {
		return tCTC;
	}

	public void settCTC(Double tCTC) {
		this.tCTC = tCTC;
	}

	public String gettCity() {
		return tCity;
	}

	public void settCity(String tCity) {
		this.tCity = tCity;
	}

	public String gettMobile() {
		return tMobile;
	}

	public void settMobile(String tMobile) {
		this.tMobile = tMobile;
	}

	public String gettEmailId() {
		return tEmailId;
	}

	public void settEmailId(String tEmailId) {
		this.tEmailId = tEmailId;
	}

	public BigDecimal gettTaxableAmnt() {
		return tTaxableAmnt;
	}

	public void settTaxableAmnt(BigDecimal tTaxableAmnt) {
		this.tTaxableAmnt = tTaxableAmnt;
	}

	public Integer getLastDay() {
		return lastDay;
	}

	public void setLastDay(Integer lastDay) {
		this.lastDay = lastDay;
	}

	public String gettId() {
		return tId;
	}

	public void settId(String tId) {
		this.tId = tId;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String gettEmployeeId() {
		return tEmployeeId;
	}

	public void settEmployeeId(String tEmployeeId) {
		this.tEmployeeId = tEmployeeId;
	}

	public String gettEmployeeName() {
		return tEmployeeName;
	}

	public void settEmployeeName(String tEmployeeName) {
		this.tEmployeeName = tEmployeeName;
	}

	public Double gettNetSalary() {
		return tNetSalary;
	}

	public void settNetSalary(Double tNetSalary) {
		this.tNetSalary = tNetSalary;
	}

	public String gettPayDate() {
		return tPayDate;
	}

	public void settPayDate(String tPayDate) {
		this.tPayDate = tPayDate;
	}

	public String gettPayGrade() {
		return tPayGrade;
	}

	public void settPayGrade(String tPayGrade) {
		this.tPayGrade = tPayGrade;
	}

	public BigDecimal gettLeaveDays() {
		return tLeaveDays;
	}

	public void settLeaveDays(BigDecimal tLeaveDays) {
		this.tLeaveDays = tLeaveDays;
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

	public Double gettEarnings() {
		return tEarnings;
	}

	public void settEarnings(Double tEarnings) {
		this.tEarnings = tEarnings;
	}

	public Double gettDeductions() {
		return tDeductions;
	}

	public void settDeductions(Double tDeductions) {
		this.tDeductions = tDeductions;
	}

	public Boolean gettStatus() {
		return tStatus;
	}

	public void settStatus(Boolean tStatus) {
		this.tStatus = tStatus;
	}

	public String gettCreatedBy() {
		return tCreatedBy;
	}

	public void settCreatedBy(String tCreatedBy) {
		this.tCreatedBy = tCreatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
