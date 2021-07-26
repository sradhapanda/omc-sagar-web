/**
 * Defines user process master model
 *
 */
package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class ProcessMasterModel {

	private String tProcess;
	private String tProcessName;
	private String tProcessDescription;
	private Boolean tProcessStatus;
	private String tProcessCreatedBy;
	private String status;
	private String action;

	public ProcessMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String gettProcess() {
		return tProcess;
	}

	public void settProcess(String tProcess) {
		this.tProcess = tProcess;
	}

	public String gettProcessName() {
		return tProcessName;
	}

	public void settProcessName(String tProcessName) {
		this.tProcessName = tProcessName;
	}

	public String gettProcessDescription() {
		return tProcessDescription;
	}

	public void settProcessDescription(String tProcessDescription) {
		this.tProcessDescription = tProcessDescription;
	}

	public Boolean gettProcessStatus() {
		return tProcessStatus;
	}

	public void settProcessStatus(Boolean tProcessStatus) {
		this.tProcessStatus = tProcessStatus;
	}

	public String gettProcessCreatedBy() {
		return tProcessCreatedBy;
	}

	public void settProcessCreatedBy(String tProcessCreatedBy) {
		this.tProcessCreatedBy = tProcessCreatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
