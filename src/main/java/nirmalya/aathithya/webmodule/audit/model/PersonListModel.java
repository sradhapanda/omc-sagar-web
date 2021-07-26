/**
 * 
 */
package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author USER
 *
 */
public class PersonListModel {
	private String id;
	private String name;
	private String role;
	private String action;
	private String query;
	private String replies;
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getReplies() {
		return replies;
	}
	public void setReplies(String replies) {
		this.replies = replies;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public PersonListModel() {
		super();
		// TODO Auto-generated constructor stub
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
