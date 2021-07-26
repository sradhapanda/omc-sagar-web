/**
 * Class Define SacMaster Entity
 */
package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class UserSacCodeModel {
		private String sacId;
		private String sacCodeName;
		private String sacService;
		private Float sacGST;
		private String sacDescription;
		private String sacEffDate;
		private Boolean sacActive;
		private Date sacCreatedOn;
		private Date sacUpdatedOn;
		private String sacServiceName;
		private String action;
		private String status;
		
		public UserSacCodeModel() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getSacId() {
			return sacId;
		}

		public void setSacId(String sacId) {
			this.sacId = sacId;
		}

		public String getSacCodeName() {
			return sacCodeName;
		}

		public void setSacCodeName(String sacCodeName) {
			this.sacCodeName = sacCodeName;
		}

		public String getSacService() {
			return sacService;
		}

		public void setSacService(String sacService) {
			this.sacService = sacService;
		}

		public Float getSacGST() {
			return sacGST;
		}

		public void setSacGST(Float sacGST) {
			this.sacGST = sacGST;
		}

		public String getSacDescription() {
			return sacDescription;
		}

		public void setSacDescription(String sacDescription) {
			this.sacDescription = sacDescription;
		}

		public String getSacEffDate() {
			return sacEffDate;
		}

		public void setSacEffDate(String sacEffDate) {
			this.sacEffDate = sacEffDate;
		}

		public Boolean getSacActive() {
			return sacActive;
		}

		public void setSacActive(Boolean sacActive) {
			this.sacActive = sacActive;
		}

		public Date getSacCreatedOn() {
			return sacCreatedOn;
		}

		public void setSacCreatedOn(Date sacCreatedOn) {
			this.sacCreatedOn = sacCreatedOn;
		}

		public Date getSacUpdatedOn() {
			return sacUpdatedOn;
		}

		public void setSacUpdatedOn(Date sacUpdatedOn) {
			this.sacUpdatedOn = sacUpdatedOn;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
		
		public String getSacServiceName() {
			return sacServiceName;
		}

		public void setSacServiceName(String sacServiceName) {
			this.sacServiceName = sacServiceName;
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
