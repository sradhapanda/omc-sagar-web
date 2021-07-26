package nirmalya.aathithya.webmodule.audit.model;


public class AuditDashboardModel {

	private String totalImprovement;
	private String totalPom;
	private String totalNc;
	private String totalOpen;
	private String totalPending;
	private String pendingreplied;
	private String pendingObservation;
	private String pendingRequistion;
	
	private String totalNotObserved;
	private String totalOpenStatus;
	private String totalRepliedStatus;
	private String totalReOpen;
	private String totalClosed;

	private String monthname;
	private String monthData;
	
	private String auditSectionStatus;
	private String repliedByConcernAudit;
	private String replied;
	private String pendingReply;
	private String totalAudit;
	private String section;
	private String department;
	private String auditId;
	private String auditType;
	private String date;
	private String reqNo;
	private String raisedBy;
	private String raisedTo;
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getReqNo() {
		return reqNo;
	}


	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}


	public String getRaisedBy() {
		return raisedBy;
	}


	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}


	public String getRaisedTo() {
		return raisedTo;
	}


	public void setRaisedTo(String raisedTo) {
		this.raisedTo = raisedTo;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getAuditId() {
		return auditId;
	}


	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}


	public String getAuditType() {
		return auditType;
	}


	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}


	public AuditDashboardModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	public String getTotalImprovement() {
		return totalImprovement;
	}


	public void setTotalImprovement(String totalImprovement) {
		this.totalImprovement = totalImprovement;
	}


	public String getTotalNc() {
		return totalNc;
	}

	public void setTotalNc(String totalNc) {
		this.totalNc = totalNc;
	}

	public String getTotalOpen() {
		return totalOpen;
	}

	public void setTotalOpen(String totalOpen) {
		this.totalOpen = totalOpen;
	}

	public String getTotalPending() {
		return totalPending;
	}

	public void setTotalPending(String totalPending) {
		this.totalPending = totalPending;
	}

	public String getPendingreplied() {
		return pendingreplied;
	}

	public void setPendingreplied(String pendingreplied) {
		this.pendingreplied = pendingreplied;
	}

	
	public String getTotalPom() {
		return totalPom;
	}


	public void setTotalPom(String totalPom) {
		this.totalPom = totalPom;
	}


	public String getPendingObservation() {
		return pendingObservation;
	}


	public void setPendingObservation(String pendingObservation) {
		this.pendingObservation = pendingObservation;
	}


	public String getPendingRequistion() {
		return pendingRequistion;
	}


	public void setPendingRequistion(String pendingRequistion) {
		this.pendingRequistion = pendingRequistion;
	}


	public String getTotalNotObserved() {
		return totalNotObserved;
	}


	public void setTotalNotObserved(String totalNotObserved) {
		this.totalNotObserved = totalNotObserved;
	}


	public String getTotalOpenStatus() {
		return totalOpenStatus;
	}


	public void setTotalOpenStatus(String totalOpenStatus) {
		this.totalOpenStatus = totalOpenStatus;
	}


	public String getTotalRepliedStatus() {
		return totalRepliedStatus;
	}


	public void setTotalRepliedStatus(String totalRepliedStatus) {
		this.totalRepliedStatus = totalRepliedStatus;
	}


	public String getTotalReOpen() {
		return totalReOpen;
	}


	public void setTotalReOpen(String totalReOpen) {
		this.totalReOpen = totalReOpen;
	}


	public String getTotalClosed() {
		return totalClosed;
	}


	public void setTotalClosed(String totalClosed) {
		this.totalClosed = totalClosed;
	}


	public String getMonthname() {
		return monthname;
	}


	public void setMonthname(String monthname) {
		this.monthname = monthname;
	}


	public String getMonthData() {
		return monthData;
	}


	public void setMonthData(String monthData) {
		this.monthData = monthData;
	}


	public String getAuditSectionStatus() {
		return auditSectionStatus;
	}


	public void setAuditSectionStatus(String auditSectionStatus) {
		this.auditSectionStatus = auditSectionStatus;
	}


	public String getRepliedByConcernAudit() {
		return repliedByConcernAudit;
	}


	public void setRepliedByConcernAudit(String repliedByConcernAudit) {
		this.repliedByConcernAudit = repliedByConcernAudit;
	}


	public String getReplied() {
		return replied;
	}


	public void setReplied(String replied) {
		this.replied = replied;
	}


	public String getPendingReply() {
		return pendingReply;
	}


	public void setPendingReply(String pendingReply) {
		this.pendingReply = pendingReply;
	}

	public String getTotalAudit() {
		return totalAudit;
	}


	public void setTotalAudit(String totalAudit) {
		this.totalAudit = totalAudit;
	}


	public String getSection() {
		return section;
	}


	public void setSection(String section) {
		this.section = section;
	}


	@Override
	public String toString() {
		return "AuditDashboardModel [totalImprovement=" + totalImprovement + ", totalPom=" + totalPom + ", totalNc="
				+ totalNc + ", totalOpen=" + totalOpen + ", totalPending=" + totalPending + ", pendingreplied=" + pendingreplied + ", pendingObservation=" + pendingObservation +  ", pendingRequistion=" + pendingRequistion + ", totalNotObserved=" + totalNotObserved +", totalOpenStatus=" + totalOpenStatus +", totalRepliedStatus=" + totalRepliedStatus +", totalReOpen=" + totalReOpen +", totalClosed=" + totalClosed +", monthname=" + monthname +", monthData=" + monthData +", auditSectionStatus=" + auditSectionStatus+", replied=" + replied+", repliedByConcernAudit=" + repliedByConcernAudit +", totalAudit=" + totalAudit +", section=" + section +", pendingReply=" + pendingReply +"]";
	}

	
}
