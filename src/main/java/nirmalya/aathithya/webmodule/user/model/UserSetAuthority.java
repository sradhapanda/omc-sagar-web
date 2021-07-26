/**
 * web Module for set authority
 */
package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserSetAuthority {
	private String process;
	private String user;
	private String approvalAction;
	private Integer stageNo;
	private Boolean status;
	private String action;
	private String authorityStatus;
	private String processId;
	private String userId;
	private String approvalId;
	private String createdBy;
	private String userRole;
	private List<String> approvalActionList = new ArrayList<String>();
	private String tat;
	private String isEdit;

	public UserSetAuthority() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getApprovalAction() {
		return approvalAction;
	}

	public void setApprovalAction(String approvalAction) {
		this.approvalAction = approvalAction;
	}

	public Integer getStageNo() {
		return stageNo;
	}

	public void setStageNo(Integer stageNo) {
		this.stageNo = stageNo;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAuthorityStatus() {
		return authorityStatus;
	}

	public void setAuthorityStatus(String authorityStatus) {
		this.authorityStatus = authorityStatus;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<String> getApprovalActionList() {
		return approvalActionList;
	}

	public void setApprovalActionList(List<String> approvalActionList) {
		this.approvalActionList = approvalActionList;
	}

	public String getTat() {
		return tat;
	}

	public void setTat(String tat) {
		this.tat = tat;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
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
