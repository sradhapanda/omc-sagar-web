/**
 * 
 */
package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author NirmalyaLabs
 *
 */
public class CategoryModel {
	private String category;
	private String categoryName;
	private String description;
	private String categoryCreatedOn;
	private String categoryUpdatedOn;
	private String createdBy;
	private String action;
public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCreatedOn() {
		return categoryCreatedOn;
	}

	public void setCategoryCreatedOn(String categoryCreatedOn) {
		this.categoryCreatedOn = categoryCreatedOn;
	}

	public String getCategoryUpdatedOn() {
		return categoryUpdatedOn;
	}

	public void setCategoryUpdatedOn(String categoryUpdatedOn) {
		this.categoryUpdatedOn = categoryUpdatedOn;
	}

public String getCreatedBy() {
		return createdBy;
	}

	public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
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
