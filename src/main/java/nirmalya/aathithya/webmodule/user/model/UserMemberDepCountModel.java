package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserMemberDepCountModel {
	
	private String key;

	private Integer limit;
	
	private Integer agelimit;
	

	public Integer getAgelimit() {
		return agelimit;
	}
	public void setAgelimit(Integer agelimit) {
		this.agelimit = agelimit;
	}
	public UserMemberDepCountModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
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
