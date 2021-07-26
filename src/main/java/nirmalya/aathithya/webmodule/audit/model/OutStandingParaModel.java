package nirmalya.aathithya.webmodule.audit.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OutStandingParaModel {
	private Integer id;
	private String year;
	private Integer irs;
	private Integer paras;
	private Double moneyValue;
	private String createdBy;
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getIrs() {
		return irs;
	}
	public void setIrs(Integer irs) {
		this.irs = irs;
	}
	public Integer getParas() {
		return paras;
	}
	public void setParas(Integer paras) {
		this.paras = paras;
	}
	public Double getMoneyValue() {
		return moneyValue;
	}
	public void setMoneyValue(Double moneyValue) {
		this.moneyValue = moneyValue;
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
