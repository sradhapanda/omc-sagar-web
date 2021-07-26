package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditReportBasicsQualifyModel {

	private String qlfyFileName;;
	private String qlfyDocument;
	private String qlfyComment;
	private List<String> qlfyfile = new ArrayList<String>();
	private String qlfyDocumentName;
	public String getQlfyDocumentName() {
		return qlfyDocumentName;
	}

	public void setQlfyDocumentName(String qlfyDocumentName) {
		this.qlfyDocumentName = qlfyDocumentName;
	}

	public AuditReportBasicsQualifyModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getQlfyFileName() {
		return qlfyFileName;
	}

	public void setQlfyFileName(String qlfyFileName) {
		this.qlfyFileName = qlfyFileName;
	}

	public String getQlfyDocument() {
		return qlfyDocument;
	}

	public void setQlfyDocument(String qlfyDocument) {
		this.qlfyDocument = qlfyDocument;
	}

	public String getQlfyComment() {
		return qlfyComment;
	}

	public void setQlfyComment(String qlfyComment) {
		this.qlfyComment = qlfyComment;
	}

	public List<String> getQlfyfile() {
		return qlfyfile;
	}

	public void setQlfyfile(List<String> qlfyfile) {
		this.qlfyfile = qlfyfile;
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
