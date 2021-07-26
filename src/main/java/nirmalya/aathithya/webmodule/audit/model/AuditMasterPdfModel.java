package nirmalya.aathithya.webmodule.audit.model;

public class AuditMasterPdfModel {

	private Integer auditId;
	private String auditTo;
	private String summery;
	private String question;
	private String observation;
	private String auditType;
	private String agreedAction;
	private Byte severity;
	private String severityName;
	private String auditName;
	
	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
	}

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public String getAuditTo() {
		return auditTo;
	}

	public void setAuditTo(String auditTo) {
		this.auditTo = auditTo;
	}

	public String getSummery() {
		return summery;
	}

	public void setSummery(String summery) {
		this.summery = summery;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAgreedAction() {
		return agreedAction;
	}

	public void setAgreedAction(String agreedAction) {
		this.agreedAction = agreedAction;
	}

	public Byte getSeverity() {
		return severity;
	}

	public void setSeverity(Byte severity) {
		this.severity = severity;
	}

	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}

	@Override
	public String toString() {
		return "AuditMasterPdfModel [auditId=" + auditId + ", auditTo=" + auditTo + ", summery=" + summery
				+ ", question=" + question + ", observation=" + observation + ", auditType=" + auditType + "]";
	}

}
