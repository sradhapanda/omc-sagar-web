/*
 * Defines District Master  related method calls 
 */
package nirmalya.aathithya.webmodule.master.controller;

import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.UserDistrictModel; 
/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "master")
public class UserDistrictController {
	
	Logger logger = LoggerFactory.getLogger(UserDistrictController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	 
	
	/*
	 *
	 *  Add District Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-district-master")
	public String addDistrict(Model model, HttpSession session) {
	
		logger.info("Method : addDistrict starts");
		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> statedata = new ArrayList<DropDownModel>();
		UserDistrictModel tableSession = (UserDistrictModel) session.getAttribute("sdistrict");
		try {
			respTblMstr = restClient.getForObject(env.getMasterUrl() + "getStateName?Action=" + "getStateName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblMstr = respTblMstr.getMessage();
		
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		statedata = mapper.convertValue(respTblMstr.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("statedata", statedata);
		
		UserDistrictModel district = new UserDistrictModel();
		try {	
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);	
			}
			session.setAttribute("message", "");
			if (tableSession != null) {
				model.addAttribute("district",tableSession);
				session.setAttribute("sdistrict", null);
			} else {
				model.addAttribute("district", district);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : addTableMaster ends");	
		
		return "master/AddFormDistrict";
	}
	
	/* 
	 * 
	 * Post Mapping add-district-master
	 *  
	 */  
	@SuppressWarnings("unchecked")
	@PostMapping("/add-district-master")
	public String addDistrict(@ModelAttribute UserDistrictModel district, Model model, HttpSession session) {

		logger.info("Method : addDistrict starts");
		
	//	System.out.println("posted District data" + district);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			district.setCreatedBy("user001");
			resp = restClient.postForObject(env.getMasterUrl() + "/restAddDistrict", district, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sdistrict", district);
			return "redirect:/master/add-district-master";
		}

		logger.info("Method : addDistrict ends");
		return "redirect:/master/view-district-master";
	}
	
	/*
	 *
	 *  View District Master' page
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-district-master")
	public String viewDistrict(Model model) {

		JsonResponse<Object> district = new JsonResponse<Object>();
		model.addAttribute("district", district);
		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> statedata = new ArrayList<DropDownModel>();
		//UserDistrictModel tableSession = (UserDistrictModel) session.getAttribute("sdistrict");
		try {
			respTblMstr = restClient.getForObject(env.getMasterUrl() + "getStateName?Action=" + "getStateName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		/* Dropdown For State Name */
		statedata = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("statedata", statedata);

		
		//
		JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> distdata = new ArrayList<DropDownModel>();

		try {
			respTblMstr1 = restClient.getForObject(env.getMasterUrl() + "getDistName?Action=" + "getDistName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr1 = respTblMstr1.getMessage();

		if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
			model.addAttribute("message", messageForTblMstr1);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		
		distdata = mapper1.convertValue(respTblMstr1.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("distdata", distdata);
		//
		return "master/ListingFormDistrict";
	}
	
	/*
	 *
	 * View all District data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-district-master-through-ajax")
	public @ResponseBody DataTableResponse viewDistrictThroughAjax(Model model, HttpServletRequest request, @RequestParam String param1) {
		
		logger.info("Method : viewDistrictThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
		 
			
			
			JsonResponse<List<UserDistrictModel>> jsonResponse = new JsonResponse<List<UserDistrictModel>>();

			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getDistrictData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserDistrictModel> district = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserDistrictModel>>() {
					});

			String s = "";
			

			for (UserDistrictModel m : district) {
				byte[] pId = Base64.getEncoder().encode(m.getDistrictId().getBytes());
			
				
					  if(m.getDistrictStatus()) {
						  m.setStateShowActive("Active"); 
					  }else{
					   m.setStateShowActive("Inactive");
					   
					  }
				 
				
				s = s + "<a href='edit-district-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;"
						+"<a href='javascript:void(0)'" 
						+ "' onclick='DeleteItem(\"" + m.getDistrictId() + "\")' ><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInModel(\"" + m.getDistrictId()
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
			//	System.out.println("data in status   "+m.getDistrictStatus());
			}

			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(district);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewDistrictThroughAjax ends");	
		return response;
	}
	
	
	
	/*
	 *
	 *  'Delete District Master' By Id
	 * 
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping("delete-district-master") public @ResponseBody
	 * JsonResponse<Object> deleteDistrict(@RequestBody String id,HttpSession
	 * session) {
	 * 
	 * logger.info("Method : deleteDistrict starts");
	 * System.out.println("response deleted "+id);
	 * 
	 * JsonResponse<Object> resp = new JsonResponse<Object>();
	 * 
	 * try { resp =
	 * restClient.getForObject(env.getUserUrl()+"deleteDistrictById?id="+id,
	 * JsonResponse.class); } catch (RestClientException e) { e.printStackTrace(); }
	 * System.out.println("response delete in web controller "+resp);
	 * if(resp.getMessage()== null || resp.getMessage()=="") {
	 * resp.setMessage("Success"); }else { resp.setMessage("unsuccess"); }
	 * 
	 * logger.info("Method : deleteDistrict ends"); return resp; }
	 */
	
	/*
	 * 
	 *  'Delete District Master' Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	 @PostMapping("delete-district-master") 
	 public @ResponseBody JsonResponse<Object> deleteDistrict(@RequestBody String id,HttpSession session)
	 {
	 
	 logger.info("Method : deleteDistrict starts");
	// System.out.println("response delete "+id);
	 JsonResponse<Object> resp = new JsonResponse<Object>();
	  
	 try { 
		 resp = restClient.getForObject(env.getMasterUrl()+"deleteDistrictById?id="+id,JsonResponse.class);
	 } catch (RestClientException e) { 
		 e.printStackTrace(); 
	 } 
	// System.out.println("response delete in web controller "+resp);
	 if(resp.getMessage()== null || resp.getMessage()=="") 
	 {
		 resp.setMessage("Success");
	 }else
	 {
		 resp.setMessage("Unsuccess");
	 }
			
	 logger.info("Method : deleteDistrict ends");	
	 return resp;
	 }
	
	
	
		/*
		 * 'Edit District Master' By Id
		 * 
		 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-district-master")
	public String editDistrict(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editDistrict starts");

		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> statedata = new ArrayList<DropDownModel>();
		//UserDistrictModel tableSession = (UserDistrictModel) session.getAttribute("sdistrict");
		try {
			respTblMstr = restClient.getForObject(env.getMasterUrl() + "getStateName?Action=" + "getStateName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		/* Dropdown For Property Name */
		statedata = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("statedata", statedata);

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		/* System.out.println(encodedIndex); */

		UserDistrictModel district = new UserDistrictModel();
		JsonResponse<UserDistrictModel> jsonResponse = new JsonResponse<UserDistrictModel>();

		try {

			jsonResponse = restClient.getForObject(
					env.getMasterUrl() + "getDistrictById?id=" + id + "&Action=viewEditDistrict",
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		/* ObjectMapper mapper = new ObjectMapper(); */

		district = mapper.convertValue(jsonResponse.getBody(), UserDistrictModel.class);
		session.setAttribute("message", "");
		/* System.out.println(meal); */
		model.addAttribute("district", district);

		logger.info("Method : editDistrict ends");
		return "master/AddFormDistrict";
	}
	
	/*
	 * 
	 * Modal View of District Master
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-district-master-model" })
	public @ResponseBody JsonResponse<Object> modalDistrict(Model model, @RequestBody String index, BindingResult result) {
		
		logger.info("Method : modalDistrict starts");
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			 
			res = restClient.getForObject(env.getMasterUrl() + "getDistrictById?id=" + index+"&Action="+"ModelView", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
		 	res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}		
		logger.info("Method : modalDistrict ends");	
		return res;
	}

}
