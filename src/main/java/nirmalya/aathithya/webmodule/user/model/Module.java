package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class Module {

	private String name;
	private String moduleLogoName;
	private List<Function> module;

	public Module() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Function> getModule() {
		return module;
	}

	public void setModule(List<Function> module) {
		this.module = module;
	}
	
	
	
	public String getModuleLogoName() {
		return moduleLogoName;
	}


	public void setModuleLogoName(String moduleLogoName) {
		this.moduleLogoName = moduleLogoName;
	}


	/**
	 * Overrides toString method for converting class to string and back 
	**/
	@Override
	public String toString() {
		ObjectMapper  mapperObj=new ObjectMapper();
		String jsonStr;
		try{
			jsonStr=mapperObj.writeValueAsString(this);
		}catch(IOException ex){
			
			jsonStr=ex.toString();
		}
		return jsonStr;
	}
}
