/*

 * Defines User Role related method calls 
 */
package nirmalya.aathithya.webmodule.user.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.DataSetForActivity;
import nirmalya.aathithya.webmodule.common.utils.DataSetForFunction;
import nirmalya.aathithya.webmodule.common.utils.DataSetForModule;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.UserRoleModel;
/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "user")
public class UserRoleController {
	Logger logger = LoggerFactory.getLogger(UserRoleController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/**
	 * Add User Role
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/add-user-role")
	public String addUserRole(Model model, HttpSession session) {
	
		logger.info("Method : addUserRole starts");
		UserRoleModel tableSession = (UserRoleModel) session.getAttribute("surole");
		
		/* costCenterdata */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> costCenterdata = new ArrayList<DropDownModel>();
		
		try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getCostCenterName?Action=" + "getCostCenterName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblMstr = respTblMstr.getMessage();
		
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		costCenterdata = mapper.convertValue(respTblMstr.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("costCenterdata", costCenterdata);  
		
		/* parentRoledata */
 
		JsonResponse<List<DropDownModel>> respTbl = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> parentRoledata = new ArrayList<DropDownModel>();
		
		try {
			respTbl = restClient.getForObject(env.getUserUrl() + "getParentName?Action=" + "getParentName",
					JsonResponse.class);

			
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTbl = respTbl.getMessage();
		
		if (messageForTbl != null || messageForTbl != "") {
			model.addAttribute("message", messageForTbl);
		}
		
		ObjectMapper mapper1 = new ObjectMapper();
		
		parentRoledata = mapper1.convertValue(respTbl.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("parentRoledata", parentRoledata);  
		
		
		
		/* moduleDetails */
		JsonResponse<List<DropDownModel>> respTblMdl = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel>moduleData = new ArrayList<DropDownModel>();
		
		try {
			respTblMdl = restClient.getForObject(env.getUserUrl() + "getModuleDetails?Action=" + "getModuleDetails",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblMdl = respTblMdl.getMessage();
		
		if (messageForTblMdl != null || messageForTblMdl != "") {
			model.addAttribute("message", messageForTblMdl);
		}
		
		ObjectMapper mapper2 = new ObjectMapper();
		
		moduleData = mapper2.convertValue(respTblMdl.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("moduleData", moduleData);  
		
		
		/* functionDetails */
		JsonResponse<List<DataSetForFunction>> respTblfnc = new JsonResponse<List<DataSetForFunction>>();
		List<DataSetForFunction>functionData = new ArrayList<DataSetForFunction>();
		
		try {
			respTblfnc = restClient.getForObject(env.getUserUrl() + "getfunctionDetails?Action=" + "getfunctionDetails",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblfnc = respTblfnc.getMessage();
		
		if (messageForTblfnc != null || messageForTblfnc != "") {
			model.addAttribute("message", messageForTblfnc);
		}
		
		ObjectMapper mapper3 = new ObjectMapper();
		
		functionData = mapper3.convertValue(respTblfnc.getBody(),
				new TypeReference<List<DataSetForFunction>>() {
				});
		model.addAttribute("functionData", functionData);  
		
		/* ActivityDetails */
		JsonResponse<List<DataSetForActivity>> respTblact = new JsonResponse<List<DataSetForActivity>>();
		List<DataSetForActivity>activityData = new ArrayList<DataSetForActivity>();
		
		try {
			respTblact = restClient.getForObject(env.getUserUrl() + "getActivityDetails?Action=" + "getActivityDetails",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblact = respTblact.getMessage();
		
		if (messageForTblact != null || messageForTblact != "") {
			model.addAttribute("message", messageForTblact);
		}
		
		ObjectMapper mapper4 = new ObjectMapper();
		
		activityData = mapper4.convertValue(respTblact.getBody(),
				new TypeReference<List<DataSetForActivity>>() {
				});
		model.addAttribute("activityData", activityData); 
		
		
		List<String> module = new ArrayList<String>();
		model.addAttribute("selectedModule", module);
		
		List<String> function = new ArrayList<String>();
		model.addAttribute("selectedFunction", function);
		
		List<String> activity = new ArrayList<String>();
		model.addAttribute("selectedActivity", activity);
		
	//	UserRoleModel tableSession = (UserRoleModel) session.getAttribute("surole");
		 		
		UserRoleModel urole= new UserRoleModel();
		try {
			
	
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);
				
			}
	
			session.setAttribute("message", "");
			if (tableSession != null) {
				model.addAttribute("urole",tableSession);
				session.setAttribute("urole", null);
			} else {
				model.addAttribute("urole", urole);
				System.out.println(urole);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : addUserRole ends");	
		
		 return "user/AddUserRole";
	}
	
	/* 
	 * 
	 * Add Postmapping add-user-role
	 *  
	 */  
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="add-user-role", method={RequestMethod.POST})
	public @ResponseBody JsonResponse<Object> addUserRole(@RequestBody List<UserRoleModel> checkBoxData, 
			Model model,HttpSession session){
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addUserRole function starts");
		//System.out.println("posted addUserRole data" + checkBoxData);
		try {
		
			for(UserRoleModel i:checkBoxData) {
				i.setCreatedBy("User001");
				
			}
			res = restClient.postForObject(env.getUserUrl() + "restAddUserRole",  checkBoxData, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = res.getMessage();

		if (message != null && message != "") {
			
		}else{
			res.setMessage("Success");
		}
		logger.info("Method : addUserRole function Ends");
		return res;
	}

	  
	/*
	 * 
	 * View User Role
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-user-role")
	public String viewUserRole(Model model) {
	//	session.setAttribute("urole", "");
		JsonResponse<Object> urole = new JsonResponse<Object>();
		model.addAttribute("urole", urole);
		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> costCenterData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getCostCenterName?Action=" + "getCostCenterName",
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
		costCenterData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("costCenterData", costCenterData);

		return "user/ViewUserRole";
	}
	
	
	
	/**
	 * View all data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-user-role-through-ajax")
	public @ResponseBody DataTableResponse viewUserRoleThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1,@RequestParam String param2) {
		
		logger.info("Method : viewUserRoleThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);

			JsonResponse<List<UserRoleModel>> jsonResponse = new JsonResponse<List<UserRoleModel>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "getUserRoleData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserRoleModel> urole = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserRoleModel>>() {
					});

			String s = "";

			for (UserRoleModel m : urole) {
				byte[] pId = Base64.getEncoder().encode(m.getUserRoleId().getBytes());
		 	
				//logger.info("Method : viewUserRoleThroughAjax ends");
				
	
			 
//			       byte x = m.getUserType();
//			       int y = x;
//				
//				if(y==1){
//					m.setUserShow("Admin");
//				}else if(y==2){
//					m.setUserShow("User");
//				}
			
			  if(m.getUserRoleStatus())
			  {
				  m.setShowActiveRoleStatus("Active");
			 }else{
				  m.setShowActiveRoleStatus("Inactive");
			  
			     }
				 
				 
				
				s = s + "<a href='edit-user-role?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style='font-size:20px'></i></a>&nbsp;&nbsp;"
						+"<a href='javascript:void(0)'" 
						+ "' onclick='DeleteItem(\"" + m.getUserRoleId() + "\")' ><i class=\"fa fa-trash\" style='font-size:20px'></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  "
						+ "href='javascript:void' onclick='viewInModel(\"" + m.getUserRoleId()
						+ "\")'><i class='fa fa-search search' style='font-size:20px'></i></a>";
				m.setAction(s);
				s = "";
				//System.out.println("data in status   "+m.getUserRoleStatus());
				
			//	System.out.println("data in UserType   "+m.getUserType());
				
				
			}

			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(urole);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewUserRoleThroughAjax ends");	
		return response;
	}
	
	/*
	 *
	 *  'Delete User Role' By Id
	 * 
	 */
	  
	
	@SuppressWarnings("unchecked")
	 @PostMapping("view-user-role-delete") 
	 public @ResponseBody JsonResponse<Object> deleteUserRole(@RequestBody String id,HttpSession session)
	 {
	 
	 logger.info("Method : deleteUserRole starts");
	// System.out.println("response delete "+id);
	 JsonResponse<Object> resp = new JsonResponse<Object>();
	  
	 try { 
		 resp = restClient.getForObject(env.getUserUrl()+"deleteUserRoleById?id="+id,JsonResponse.class);
		} catch (RestClientException e) { 
		 e.printStackTrace(); 
		}  
		 if(resp.getMessage()== null || resp.getMessage()=="") 
		 {
			 resp.setMessage("Success");
		 }else
			 {
				 resp.setMessage("Unsuccess");
			 }
			
	 logger.info("Method : deleteUserRoleById ends");	
	 return resp;
	 }
	
	
	/*
	 * 'Edit User Role' By Id
	 * 
	 */
@SuppressWarnings("unchecked")
@GetMapping("/edit-user-role")
public String editUserRole(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

	logger.info("Method : editUserRole starts");

	/* costCenterdata */
	JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel> costCenterdata = new ArrayList<DropDownModel>();
	
	try {
		respTblMstr = restClient.getForObject(env.getUserUrl() + "getCostCenterName?Action=" + "getCostCenterName",
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTblMstr = respTblMstr.getMessage();
	
	if (messageForTblMstr != null || messageForTblMstr != "") {
		model.addAttribute("message", messageForTblMstr);
	}
	
	ObjectMapper mapper = new ObjectMapper();
	
	costCenterdata = mapper.convertValue(respTblMstr.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	model.addAttribute("costCenterdata", costCenterdata);  
	
	/* moduleDetails with all module */
	JsonResponse<List<DropDownModel>> respTblMdl = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel>moduleData = new ArrayList<DropDownModel>();
	
	try {
		respTblMdl = restClient.getForObject(env.getUserUrl() + "getModuleDetails?Action=" + "getModuleDetails",
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTblMdl = respTblMdl.getMessage();
	
	if (messageForTblMdl != null || messageForTblMdl != "") {
		model.addAttribute("message", messageForTblMdl);
	}
	
	ObjectMapper mapper2 = new ObjectMapper();
	
	moduleData = mapper2.convertValue(respTblMdl.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	model.addAttribute("moduleData", moduleData);  
	
	
	
	 
	/* functionDetails */
	JsonResponse<List<DataSetForFunction>> respTblfnc = new JsonResponse<List<DataSetForFunction>>();
	List<DataSetForFunction>functionData = new ArrayList<DataSetForFunction>();
	
	try {
		respTblfnc = restClient.getForObject(env.getUserUrl() + "getfunctionDetails?Action=" + "getfunctionDetails",
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTblfnc = respTblfnc.getMessage();
	
	if (messageForTblfnc != null || messageForTblfnc != "") {
		model.addAttribute("message", messageForTblfnc);
	}
	
	ObjectMapper mapper3 = new ObjectMapper();
	
	functionData = mapper3.convertValue(respTblfnc.getBody(),
			new TypeReference<List<DataSetForFunction>>() {
			});
	model.addAttribute("functionData", functionData);  
	
	/* ActivityDetails */
	JsonResponse<List<DataSetForActivity>> respTblact = new JsonResponse<List<DataSetForActivity>>();
	List<DataSetForActivity>activityData = new ArrayList<DataSetForActivity>();
	
	try {
		respTblact = restClient.getForObject(env.getUserUrl() + "getActivityDetails?Action=" + "getActivityDetails",
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTblact = respTblact.getMessage();
	
	if (messageForTblact != null || messageForTblact != "") {
		model.addAttribute("message", messageForTblact);
	}
	
	ObjectMapper mapper4 = new ObjectMapper();
	
	activityData = mapper4.convertValue(respTblact.getBody(),
			new TypeReference<List<DataSetForActivity>>() {
			});
	model.addAttribute("activityData", activityData);  
	

	
	/* parentRoledata */

	JsonResponse<List<DropDownModel>> respTbl = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel> parentRoledata = new ArrayList<DropDownModel>();
	
	try {
		respTbl = restClient.getForObject(env.getUserUrl() + "getParentName?Action=" + "getParentName",
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTbl = respTbl.getMessage();
	
	if (messageForTbl != null || messageForTbl != "") {
		model.addAttribute("message", messageForTbl);
	}
	
	ObjectMapper mapper1 = new ObjectMapper();
	
	parentRoledata = mapper1.convertValue(respTbl.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	model.addAttribute("parentRoledata", parentRoledata);
	
	/*	end for drop down */

	byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
	String id = (new String(encodeByte));
	/* System.out.println(encodedIndex); */
	//System.out.println("id in edit: "+id);

	
	
	/* moduleDetails which is checked*/	
	JsonResponse<List<DropDownModel>> modulewithid = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel>moduleDataid = new ArrayList<DropDownModel>();
	
	try {														
		modulewithid = restClient.getForObject(env.getUserUrl() + "getModuleCheckDtls?id=" + id,
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageForTblMdlwithid = modulewithid.getMessage();
	
	if (messageForTblMdlwithid != null || messageForTblMdlwithid != "") {
		model.addAttribute("message", messageForTblMdlwithid);
	}
	
	ObjectMapper mapper6 = new ObjectMapper();
	
	moduleDataid = mapper6.convertValue(modulewithid.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	//model.addAttribute("moduleDataid", moduleDataid); 
	/*##############################end for moduleDataId#########*/
	
	/* moduleFunctiondts which is checked*/	
	JsonResponse<List<DropDownModel>> modulefunid = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel>moduleFunData = new ArrayList<DropDownModel>();
	
	try {														
		modulefunid = restClient.getForObject(env.getUserUrl() + "getFunctionCheckDtls?id=" + id,
				JsonResponse.class);

	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageFuncwithid = modulefunid.getMessage();
	
	if (messageFuncwithid != null || messageFuncwithid != "") {
		model.addAttribute("message", messageFuncwithid);
	}
	
	ObjectMapper mapper7 = new ObjectMapper();
	
	moduleFunData = mapper7.convertValue(modulefunid.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	//model.addAttribute("moduleFunData", moduleFunData); 
	/*##############################end for moduleDataId#########*/

	
	/* moduleActivitydtls which is checked*/	
	JsonResponse<List<DropDownModel>> moduleActid = new JsonResponse<List<DropDownModel>>();
	List<DropDownModel>moduleActData = new ArrayList<DropDownModel>();
	
	try {														
		moduleActid = restClient.getForObject(env.getUserUrl() + "getActivityCheckDtls?id=" + id,
				JsonResponse.class);
		
	} catch (RestClientException e) {
		e.printStackTrace();
	}
	
	String messageActwithid = moduleActid.getMessage();
	
	if (messageActwithid != null || messageActwithid != "") {
		model.addAttribute("message", messageActwithid);
	}
	
	ObjectMapper mapper8 = new ObjectMapper();
	
	moduleActData = mapper8.convertValue(moduleActid.getBody(),
			new TypeReference<List<DropDownModel>>() {
			});
	List<String> activity = new ArrayList<String>();
	for(DropDownModel m:moduleActData ) {
		activity.add(m.getKey());
	}
	
	List<String> function = new ArrayList<String>();
	for(DropDownModel f:moduleFunData ) {
		function.add(f.getKey());
	}
	
	List<String> module = new ArrayList<String>();
	for(DropDownModel f:moduleDataid ) {
		module.add(f.getKey());
	}
	
//	System.out.println("module== "+module);
//	System.out.println("function== "+function);
//	System.out.println("activity== "+activity);
//	System.out.println("moduleActData=="+moduleActData);
	//model.addAttribute("moduleActData", moduleActData);
	model.addAttribute("selectedActivity", activity);
	model.addAttribute("selectedFunction", function);
	model.addAttribute("selectedModule", module);
	/*##################.###########end for moduleDataId#########*/
	
	
	UserRoleModel urole = new UserRoleModel();
	JsonResponse<UserRoleModel> jsonResponse = new JsonResponse<UserRoleModel>();

	try {

		jsonResponse = restClient.getForObject(
				env.getUserUrl() + "getUserRoleById?id=" + id + "&Action=viewEditUserRole",
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

	urole = mapper.convertValue(jsonResponse.getBody(), UserRoleModel.class);
	session.setAttribute("message", "");
	/* System.out.println(meal); */
	model.addAttribute("urole", urole);

	logger.info("Method : editUserRole ends");
	return "user/AddUserRole";
}

/*
 * 
 * Modal View of User Role
 *
 */
@SuppressWarnings("unchecked")
@PostMapping(value = { "/view-user-role-model" })
public @ResponseBody JsonResponse<Object> modalUserRole(Model model, @RequestBody String index, BindingResult result) {
	
	logger.info("Method : modalUserRole starts");
	
	JsonResponse<Object> res = new JsonResponse<Object>();

	try {
		 
		res = restClient.getForObject(env.getUserUrl() + "getUserRoleById?id=" + index+"&Action="+"ModelView", JsonResponse.class);
	} catch (Exception e) {
		e.printStackTrace();
	}

	if (res.getMessage() != null) {
		//System.out.println("if block getmsg() not false : " + res.getMessage());
		res.setCode(res.getMessage());
		res.setMessage("Unsuccess");
	} else {
		res.setMessage("success");
	}		
	logger.info("Method : modalUserRole ends");	
	return res;
}



}
