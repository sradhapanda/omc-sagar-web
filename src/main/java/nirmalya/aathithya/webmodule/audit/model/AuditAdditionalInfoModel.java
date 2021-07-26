package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditAdditionalInfoModel {
	private String docName;
	private String doc;
	private String info;
	private List<String> file = new ArrayList<String>();
	private String hiddenDoc;
	
	public String getHiddenDoc() {
		return hiddenDoc;
	}
	public void setHiddenDoc(String hiddenDoc) {
		this.hiddenDoc = hiddenDoc;
	}
	public List<String> getFile() {
		return file;
	}
	public void setFile(List<String> file) {
		this.file = file;
	}
	public AuditAdditionalInfoModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
