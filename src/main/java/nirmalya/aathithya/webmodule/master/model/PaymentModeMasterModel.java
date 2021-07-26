/**
 * web model for Payment mode master 
 */
package nirmalya.aathithya.webmodule.master.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentModeMasterModel {
	private String paymentMode;
	private String paytModName;
	private String paytModDescription;
	private Boolean payModActive;
	private String payModCreatedBy;
	private String action;
	private String payStatusName;
	
	public PaymentModeMasterModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaytModName() {
		return paytModName;
	}
	public void setPaytModName(String paytModName) {
		this.paytModName = paytModName;
	}
	public String getPaytModDescription() {
		return paytModDescription;
	}
	public void setPaytModDescription(String paytModDescription) {
		this.paytModDescription = paytModDescription;
	}
	public String getPayModCreatedBy() {
		return payModCreatedBy;
	}
	public void setPayModCreatedBy(String payModCreatedBy) {
		this.payModCreatedBy = payModCreatedBy;
	}
	public Boolean getPayModActive() {
		return payModActive;
	}
	public void setPayModActive(Boolean payModActive) {
		this.payModActive = payModActive;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPayStatusName() {
		return payStatusName;
	}
	public void setPayStatusName(String payStatusName) {
		this.payStatusName = payStatusName;
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
