package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuditReportAdditionalInfoModel {

	private String infoFileName;
	private String infoDocument;
	private String infoComment;
	private Integer commentId;
	private List<String> infofile = new ArrayList<String>();
	private String infoDocumentName;
	

	

	public String getInfoDocumentName() {
		return infoDocumentName;
	}

	public void setInfoDocumentName(String infoDocumentName) {
		this.infoDocumentName = infoDocumentName;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public AuditReportAdditionalInfoModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getInfoFileName() {
		return infoFileName;
	}

	public void setInfoFileName(String infoFileName) {
		this.infoFileName = infoFileName;
	}

	public String getInfoDocument() {
		return infoDocument;
	}

	public void setInfoDocument(String infoDocument) {
		this.infoDocument = infoDocument;
	}

	public String getInfoComment() {
		return infoComment;
	}

	public void setInfoComment(String infoComment) {
		this.infoComment = infoComment;
	}

	public List<String> getInfofile() {
		return infofile;
	}

	public void setInfofile(List<String> infofile) {
		this.infofile = infofile;
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
