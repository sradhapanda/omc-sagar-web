
package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class FolderMasterModel {
	private String folder;
	private String folderName;
	private String description;
	private String createdBy;
	private Boolean active;
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	private String action;

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
