package nirmalya.aathithya.webmodule.audit.model;

public class AuditNotificationModel {

	private String auditInitiate;
	private String auditType;
	private String auditCreated;
	private String action;

	public String getAuditInitiate() {
		return auditInitiate;
	}

	public void setAuditInitiate(String auditInitiate) {
		this.auditInitiate = auditInitiate;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAuditCreated() {
		return auditCreated;
	}

	public void setAuditCreated(String auditCreated) {
		this.auditCreated = auditCreated;
	}
	
	
	
}
