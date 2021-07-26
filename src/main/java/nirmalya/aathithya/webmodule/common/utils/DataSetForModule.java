package nirmalya.aathithya.webmodule.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataSetForModule {

	private String moduleId;
	
	private String moduleName;

	private String functionId;
	
	private String functionParentId;
	
	private String functionName;

	private String activityId;

	private String moduleIdInActivity;
	
	private String activityParentId;
	
	private String activityName;

	public DataSetForModule() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getFunctionParentId() {
		return functionParentId;
	}

	public void setFunctionParentId(String functionParentId) {
		this.functionParentId = functionParentId;
	}

	public String getModuleIdInActivity() {
		return moduleIdInActivity;
	}

	public void setModuleIdInActivity(String moduleIdInActivity) {
		this.moduleIdInActivity = moduleIdInActivity;
	}

	public String getActivityParentId() {
		return activityParentId;
	}

	public void setActivityParentId(String activityParentId) {
		this.activityParentId = activityParentId;
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
