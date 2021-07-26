package nirmalya.aathithya.webmodule.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSetForFunction {

	private String functionId;
	
	private String parentFunctionId;
	
	private String functionName;
	
	
	public DataSetForFunction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getParentFunctionId() {
		return parentFunctionId;
	}

	public void setParentFunctionId(String parentFunctionId) {
		this.parentFunctionId = parentFunctionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
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
