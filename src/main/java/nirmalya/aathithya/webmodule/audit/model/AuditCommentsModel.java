package nirmalya.aathithya.webmodule.audit.model;

public class AuditCommentsModel {

	private String approvedBy;
	
	private String user;
	
	private String date;
	
	private String comments;

	public AuditCommentsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
