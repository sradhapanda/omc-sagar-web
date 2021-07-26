package nirmalya.aathithya.webmodule.common.utils;

import java.io.IOException;
import groovyjarjarantlr.collections.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSetForPropType1 {
	
	private List idList;

	private String ptype1;
	
	private String ptype2;
	
	private String key;

	private String name;

	
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



	public DataSetForPropType1() {
		super();
	}

	
	
	public String getPtype1() {
		return ptype1;
	}



	public void setPtype1(String ptype1) {
		this.ptype1 = ptype1;
	}



	public String getPtype2() {
		return ptype2;
	}



	public void setPtype2(String ptype2) {
		this.ptype2 = ptype2;
	}



	public List getIdList() {
		return idList;
	}

	public void setIdList(List idList) {
		this.idList = idList;
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
