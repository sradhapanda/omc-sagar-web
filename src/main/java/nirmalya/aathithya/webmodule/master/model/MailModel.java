package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MailModel{
	private Boolean mailStatus;

	public Boolean getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(Boolean mailStatus) {
		this.mailStatus = mailStatus;
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
