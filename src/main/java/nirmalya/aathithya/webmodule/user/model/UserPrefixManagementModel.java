/**
 * 
 */
package nirmalya.aathithya.webmodule.user.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class UserPrefixManagementModel {
	
	private Integer prefix;
	private String prfxTblName;
	private String prfxTypeName;
	private String prfxCode;
	private Integer prfxNumberLength;
	private Integer prfxStartNo;
	private Boolean prfxFinYearStatus;
	private Boolean prfxIsEditable;
	private Boolean prfxActive;
	
	private String finYearStatusName;
	private String statusName;
	private String action;
	
	
	/**
	 * 
	 */
	public UserPrefixManagementModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * GETTERS SETTERS
	 *
	 */
	public Integer getPrefix() {
		return prefix;
	}
	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}
	public String getPrfxTblName() {
		return prfxTblName;
	}
	public void setPrfxTblName(String prfxTblName) {
		this.prfxTblName = prfxTblName;
	}
	public String getPrfxTypeName() {
		return prfxTypeName;
	}
	public void setPrfxTypeName(String prfxTypeName) {
		this.prfxTypeName = prfxTypeName;
	}
	public String getPrfxCode() {
		return prfxCode;
	}
	public void setPrfxCode(String prfxCode) {
		this.prfxCode = prfxCode;
	}
	public Integer getPrfxNumberLength() {
		return prfxNumberLength;
	}
	public void setPrfxNumberLength(Integer prfxNumberLength) {
		this.prfxNumberLength = prfxNumberLength;
	}
	public Integer getPrfxStartNo() {
		return prfxStartNo;
	}
	public void setPrfxStartNo(Integer prfxStartNo) {
		this.prfxStartNo = prfxStartNo;
	}
	public Boolean getPrfxFinYearStatus() {
		return prfxFinYearStatus;
	}
	public void setPrfxFinYearStatus(Boolean prfxFinYearStatus) {
		this.prfxFinYearStatus = prfxFinYearStatus;
	}
	public Boolean getPrfxIsEditable() {
		return prfxIsEditable;
	}
	public void setPrfxIsEditable(Boolean prfxIsEditable) {
		this.prfxIsEditable = prfxIsEditable;
	}
	public Boolean getPrfxActive() {
		return prfxActive;
	}
	public void setPrfxActive(Boolean prfxActive) {
		this.prfxActive = prfxActive;
	}
	public String getFinYearStatusName() {
		return finYearStatusName;
	}
	public void setFinYearStatusName(String finYearStatusName) {
		this.finYearStatusName = finYearStatusName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
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
