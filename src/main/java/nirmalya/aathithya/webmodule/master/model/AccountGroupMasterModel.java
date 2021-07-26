package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountGroupMasterModel {
private String accGroup;
private String accGroupName;
private String accGroupCode;
private String accGrupDescription;
private Boolean accGrupActive;
private String accGrupCreatedBy;
private String accGrupStatus;
private String action;
public AccountGroupMasterModel() {
	super();
	// TODO Auto-generated constructor stub
}

public String getAccGroup() {
	return accGroup;
}
public void setAccGroup(String accGroup) {
	this.accGroup = accGroup;
}
public String getAccGroupName() {
	return accGroupName;
}
public void setAccGroupName(String accGroupName) {
	this.accGroupName = accGroupName;
}
public String getAccGroupCode() {
	return accGroupCode;
}
public void setAccGroupCode(String accGroupCode) {
	this.accGroupCode = accGroupCode;
}
public String getAccGrupDescription() {
	return accGrupDescription;
}
public void setAccGrupDescription(String accGrupDescription) {
	this.accGrupDescription = accGrupDescription;
}
public Boolean getAccGrupActive() {
	return accGrupActive;
}
public void setAccGrupActive(Boolean accGrupActive) {
	this.accGrupActive = accGrupActive;
}
public String getAccGrupCreatedBy() {
	return accGrupCreatedBy;
}
public void setAccGrupCreatedBy(String accGrupCreatedBy) {
	this.accGrupCreatedBy = accGrupCreatedBy;
}

public String getAccGrupStatus() {
	return accGrupStatus;
}

public void setAccGrupStatus(String accGrupStatus) {
	this.accGrupStatus = accGrupStatus;
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
