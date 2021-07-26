package nirmalya.aathithya.webmodule.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DropDownModel {
	private String key;

	private String name;
	private String docName;

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public DropDownModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
