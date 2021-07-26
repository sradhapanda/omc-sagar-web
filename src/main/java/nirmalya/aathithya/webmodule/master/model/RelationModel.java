package nirmalya.aathithya.webmodule.master.model;
/**
 * @author Nirmalya Labs
 *
 */
public class RelationModel {
private String relationId;
private String rltnName;
private String rltnDescription;
private Boolean rltnActive;
private String rltnCreatedBy;
private String action;
private String rltnStatus;
private Boolean isEditable;
public RelationModel() {
	super();
	// TODO Auto-generated constructor stub
}
public String getRelationId() {
	return relationId;
}
public void setRelationId(String relationId) {
	this.relationId = relationId;
}
public String getRltnName() {
	return rltnName;
}
public void setRltnName(String rltnName) {
	this.rltnName = rltnName;
}
public String getRltnDescription() {
	return rltnDescription;
}
public void setRltnDescription(String rltnDescription) {
	this.rltnDescription = rltnDescription;
}
public Boolean getRltnActive() {
	return rltnActive;
}
public void setRltnActive(Boolean rltnActive) {
	this.rltnActive = rltnActive;
}
public String getRltnCreatedBy() {
	return rltnCreatedBy;
}
public void setRltnCreatedBy(String rltnCreatedBy) {
	this.rltnCreatedBy = rltnCreatedBy;
}
public String getAction() {
	return action;
}
public void setAction(String action) {
	this.action = action;
}
public String getRltnStatus() {
	return rltnStatus;
}
public void setRltnStatus(String rltnStatus) {
	this.rltnStatus = rltnStatus;
}
public Boolean getIsEditable() {
	return isEditable;
}
public void setIsEditable(Boolean isEditable) {
	this.isEditable = isEditable;
}

}
