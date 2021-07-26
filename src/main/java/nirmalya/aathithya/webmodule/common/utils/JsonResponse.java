/**
 *  Defines wrapper class for transferring data.
 */
package nirmalya.aathithya.webmodule.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Nirmalya Labs
 *
 */
public class JsonResponse<E> {

	private E body;
	private String message;
	private String code;
	private Integer total;
	private E secondBody;
	public E getSecondBody() {
		return secondBody;
	}


	public void setSecondBody(E secondBody) {
		this.secondBody = secondBody;
	}


	public JsonResponse() {
		super();
		// TODO Auto-generated constructor stub
	}


	public E getBody() {
		return body;
	}

	public void setBody(E body) {
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
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
