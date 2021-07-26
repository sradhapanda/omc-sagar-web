package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SubGroupMasterModal {
	private String subGroupId;
	private String subGroupName;
	private String groupName;
	private String groupNameDesc;
	private String createdBy;
	private String action;
	public SubGroupMasterModal() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(String subGroupId) {
		this.subGroupId = subGroupId;
	}
	public String getSubGroupName() {
		return subGroupName;
	}
	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupNameDesc() {
		return groupNameDesc;
	}
	public void setGroupNameDesc(String groupNameDesc) {
		this.groupNameDesc = groupNameDesc;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	
}
}