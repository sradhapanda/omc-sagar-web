package nirmalya.aathithya.webmodule.common.pagination;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class DataTableResponse {

	private Object data;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private Integer draw;
	private Boolean searchable = true;
	private Boolean orderable = true;
	
	public DataTableResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}


	public Integer getRecordsTotal() {
		return recordsTotal;
	}


	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}


	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}


	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}


	public Integer getDraw() {
		return draw;
	}


	public void setDraw(Integer draw) {
		this.draw = draw;
	}


	public Boolean getSearchable() {
		return searchable;
	}


	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}


	public Boolean getOrderable() {
		return orderable;
	}


	public void setOrderable(Boolean orderable) {
		this.orderable = orderable;
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
