package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditAttachmentNewModel {
	private String fYear;
	private String type;
	private String attachDate;
	private String attachfileName;
	private String attachDocument;
	private String attachComment;
	private String attachDocumentName;
	private List<String> attachfile = new ArrayList<String>();

	public AuditAttachmentNewModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getfYear() {
		return fYear;
	}

	public void setfYear(String fYear) {
		this.fYear = fYear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttachDate() {
		return attachDate;
	}

	public void setAttachDate(String attachDate) {
		this.attachDate = attachDate;
	}

	public String getAttachfileName() {
		return attachfileName;
	}

	public void setAttachfileName(String attachfileName) {
		this.attachfileName = attachfileName;
	}

	public String getAttachDocument() {
		return attachDocument;
	}

	public void setAttachDocument(String attachDocument) {
		this.attachDocument = attachDocument;
	}

	public String getAttachComment() {
		return attachComment;
	}

	public void setAttachComment(String attachComment) {
		this.attachComment = attachComment;
	}

	public List<String> getAttachfile() {
		return attachfile;
	}

	public void setAttachfile(List<String> attachfile) {
		this.attachfile = attachfile;
	}

	public String getAttachDocumentName() {
		return attachDocumentName;
	}

	public void setAttachDocumentName(String attachDocumentName) {
		this.attachDocumentName = attachDocumentName;
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
