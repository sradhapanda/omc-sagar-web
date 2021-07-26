package nirmalya.aathithya.webmodule.common.utils;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Nirmalya Labs
 *
 */
public class EnvironmentVaribles {

	@Value("${service.url.user}")
	private String userUrl;

	

	@Value("${service.url.master}")
	private String masterUrl;

	

	
	
	
	@Value("${service.url.employee}")
	private String employeeUrl;
	
	
	
	@Value("${service.url.document}")
	private String documentUrl;
	
	@Value("${service.url.leave}")
	private String leaveUrl;
	

	@Value("${service.url.audit}")
	private String auditUrl;
	
	
	
	@Value("${service.url.fileUpload-audit}")
	private String fileUploadAudit;


	
	
	@Value("${service.url.fileUpload-master}")
	private String fileUploadMaster;
	
	@Value("${service.url.baseURL}")
	private String baseURL;
	
	@Value("${service.url.hrms}")
	private String hrmsUrl ;
	
	@Value("${service.url.fileUpload-uploadEmployee}")
	private String fileUploadEmployee;
	
	
	
	@Value("${service.url.fileUpload-document}")
	private String fileUploadDocumenttUrl;



	public String getUserUrl() {
		return userUrl;
	}



	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}



	public String getMasterUrl() {
		return masterUrl;
	}



	public void setMasterUrl(String masterUrl) {
		this.masterUrl = masterUrl;
	}



	public String getEmployeeUrl() {
		return employeeUrl;
	}



	public void setEmployeeUrl(String employeeUrl) {
		this.employeeUrl = employeeUrl;
	}



	public String getDocumentUrl() {
		return documentUrl;
	}



	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}



	public String getLeaveUrl() {
		return leaveUrl;
	}



	public void setLeaveUrl(String leaveUrl) {
		this.leaveUrl = leaveUrl;
	}



	public String getAuditUrl() {
		return auditUrl;
	}



	public void setAuditUrl(String auditUrl) {
		this.auditUrl = auditUrl;
	}



	public String getFileUploadAudit() {
		return fileUploadAudit;
	}



	public void setFileUploadAudit(String fileUploadAudit) {
		this.fileUploadAudit = fileUploadAudit;
	}



	public String getFileUploadMaster() {
		return fileUploadMaster;
	}



	public void setFileUploadMaster(String fileUploadMaster) {
		this.fileUploadMaster = fileUploadMaster;
	}



	public String getBaseURL() {
		return baseURL;
	}



	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}



	public String getHrmsUrl() {
		return hrmsUrl;
	}



	public void setHrmsUrl(String hrmsUrl) {
		this.hrmsUrl = hrmsUrl;
	}



	public String getFileUploadEmployee() {
		return fileUploadEmployee;
	}



	public void setFileUploadEmployee(String fileUploadEmployee) {
		this.fileUploadEmployee = fileUploadEmployee;
	}



	public String getFileUploadDocumenttUrl() {
		return fileUploadDocumenttUrl;
	}



	public void setFileUploadDocumenttUrl(String fileUploadDocumenttUrl) {
		this.fileUploadDocumenttUrl = fileUploadDocumenttUrl;
	}
	
	



}
