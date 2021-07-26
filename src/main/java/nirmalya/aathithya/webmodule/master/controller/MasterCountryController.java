/**
 * 
 */
package nirmalya.aathithya.webmodule.master.controller;

import java.util.Base64;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.MasterCountryModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = { "master/" })
public class MasterCountryController {
	Logger logger = LoggerFactory.getLogger(MasterCountryController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;
	
	/*
	 * GetMapping for Adding new country
	 *
	 */
	@GetMapping(value = { "add-country" })
	public String addCountry(Model model, HttpSession session) {
	logger.info("Method : add country starts");
	MasterCountryModel masterCountryModel = new MasterCountryModel();
	MasterCountryModel mcount = (MasterCountryModel) session.getAttribute("mastercountry");
	String message = (String) session.getAttribute("message");
		
	if (message != null && message != "") {
		model.addAttribute("message", message);
	}

	session.setAttribute("message", "");

	if (mcount != null) {
		model.addAttribute("masterCountryModel", mcount);
	} else {
		model.addAttribute("masterCountryModel", masterCountryModel);
	}
	logger.info("Method : add country ends");
	return "master/add-country";
	}
	/*
	 * post Mapping for add Country
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-country" })
	public String addNewCountry(@ModelAttribute MasterCountryModel masterCountryModel, Model model, HttpSession session) {
		logger.info("Method : addNewCountry starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			masterCountryModel.setCountryCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getMasterUrl() + "rest-addnew-country", masterCountryModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("mastercountry", masterCountryModel);
			return "redirect:add-country";
		}
		logger.info("Method : addNew Country ends");
		return "redirect:view-country";
	}
	/*
	 * 
	 * GetMApping For Listing Country
	 * 
	 * 
	 */
	@GetMapping(value = { "view-country" })
	public String viewCountry(Model model) {
		logger.info("Method : view add country starts");
		JsonResponse<Object> country = new JsonResponse<Object>();
		model.addAttribute("country", country);
		logger.info("Method : view add country ends");
		return "master/view-country";
	}
	
	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-country-throughAjax" })
	public @ResponseBody DataTableResponse viewCountry(Model model, HttpServletRequest request,HttpSession session,
			@RequestParam String param1, @RequestParam String param2 ) {
		logger.info("Method : view Country (through ajax) starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		List<String> uList = (List<String>)session.getAttribute("URL_LIST");
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
			JsonResponse<List<MasterCountryModel>> jsonResponse = new JsonResponse<List<MasterCountryModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getMasterUrl() + "get-all-country",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<MasterCountryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<MasterCountryModel>>() {
					});
			String s = "";
			for (MasterCountryModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getCountryId().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+  new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				
				
				if(uList.stream().anyMatch(x -> x.contains("edit-country")))
				s = s + " &nbsp;&nbsp <a href='edit-country?id=" + new String(pId)
						+ "' ><i class='fa fa-edit'></i></a> ";
				
				if(uList.stream().anyMatch(x -> x.contains("delete-country")))
				s = s + " &nbsp;&nbsp; <a href='javascript:void(0)' onclick='deleteCountry(\"" +  new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getCountryActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}
			}
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : view Country (through ajax) ends");
		return response;
	}
	
	/**
	 * View selected Country in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-country-model" })
	public @ResponseBody JsonResponse<MasterCountryModel> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		
		JsonResponse<MasterCountryModel> resp = new JsonResponse<MasterCountryModel>();
		
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getMasterUrl() + "get-country-byId?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : modelView ends");
		return resp;
	}
	
	
	/*
	 * 
	 * GetMapping for delete Country
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-country" })
	public @ResponseBody JsonResponse<Object> deleteCountry(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		
		logger.info("Method : deleteCountry starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
	
		try {
			resp = restTemplate.getForObject(environmentVaribles.getMasterUrl() + "delete-country?id=" + id+"&createdBy="+userId, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteCountry ends");
		return resp;
	}
	
	/*
	 * 
	 * 
	 * GetMApping for Edit Country
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-country" })
	public String editCountry(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editCountry starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		MasterCountryModel masterCountryModel = new MasterCountryModel();
		JsonResponse<MasterCountryModel> jsonResponse = new JsonResponse<MasterCountryModel>();

		try {
			
			jsonResponse = restTemplate.getForObject(environmentVaribles.getMasterUrl() + "get-country-byId?id=" + id, JsonResponse.class);
		
		} catch (RestClientException e) {

			e.printStackTrace();
		}
		
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		masterCountryModel = mapper.convertValue(jsonResponse.getBody(), MasterCountryModel.class);
		session.setAttribute("message", "");
		model.addAttribute("masterCountryModel", masterCountryModel);
		logger.info("Method : editCountry ends");
		return "master/add-country";
	}
	
}