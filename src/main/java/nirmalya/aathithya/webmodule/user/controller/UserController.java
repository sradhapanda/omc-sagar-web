/**
 * 
 */
package nirmalya.aathithya.webmodule.user.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import nirmalya.aathithya.webmodule.master.model.UserStateModel;
import nirmalya.aathithya.webmodule.user.model.UserModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "user/")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * GET USER ADD
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("add-user")
	public String addUser(Model model, HttpSession session) {
		logger.info("Method : WEBMODULE UserController addUser starts");
		 
		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> countrydata = new ArrayList<DropDownModel>();
	    try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getCountryName?Action=" + "getCountryName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblMstr = respTblMstr.getMessage();
		
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		countrydata = mapper.convertValue(respTblMstr.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("countrydata", countrydata);
		
		model.addAttribute("adduser", new UserModel());
		UserModel userForm = (UserModel) session.getAttribute("userservice");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (userForm != null) {
			model.addAttribute("adduser", userForm);
		} else {
			model.addAttribute("adduser", new UserModel());
		}

	
		/**
		 * DROPDOWN USER ROLE LIST
		 *
		 */
		try {
			DropDownModel[] UserRole = restClient.getForObject(env.getUserUrl() + "restGetUserRole",
					DropDownModel[].class);
			List<DropDownModel> RoleList = Arrays.asList(UserRole);

			model.addAttribute("roleList", RoleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * DROPDOWN UserType LIST
		 *
		 */
		try {
			DropDownModel[] UserRole = restClient.getForObject(env.getUserUrl() + "rest-get-user-type-list",
					DropDownModel[].class);
			List<DropDownModel> userList = Arrays.asList(UserRole);

			model.addAttribute("userList", userList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : WEBMODULE UserController addUser ends");
		return "user/AddUser";
	}

	/**
	 * GET USER VIEW
	 *
	 */
	@GetMapping("view-user")
	public String viewUser(Model model, HttpSession session) {

		logger.info("Method : WEBMODULE UserController viewUser starts");
		logger.info("Method : WEBMODULE UserController viewUser end");
		return "user/ViewUser";
	}
	
	
	/**
	 * GET USER STAFF REPORT
	 *
	 */
	@GetMapping("view-user-generate-report-staff")
	public String userStaffReport(Model model, HttpSession session) {

		logger.info("Method : WEBMODULE UserController userStaffReport starts");
		logger.info("Method : WEBMODULE UserController userStaffReport end");
		return "user/StaffReport";
	}

	/**
	 * POST MAPPING SUBMIT ADD USER 
	 *
	 */

	@SuppressWarnings("rawtypes")
	@PostMapping("add-user")
	public String submitUser(@ModelAttribute UserModel userManage, Model model, HttpSession session) {
		logger.info("Method : WEBMODULE UserController submitUser starts");
		JsonResponse resp = new JsonResponse();
		userManage.setCreatedBy("u0001");
		try {
			userManage.setCreatedBy("user001");
			String s = "";

			for (String m : userManage.getUserRoles()) {
				s = s + m + ",";
			}

			if (s != "") {
				s = s.substring(0, s.length() - 1);
				userManage.setUserRole(s);
			}

			String enc = userManage.getUserPassword();
			if (enc != null && enc != "") {
				enc = passwordEncoder.encode(enc);
				userManage.setUserPassword(enc);
			}
			
			
			String enc1 = userManage.getUserPINno();
			if (enc1 != null && enc1 != "") {
				enc1 = passwordEncoder.encode(enc1);
				userManage.setUserPINno(enc1);
			}

			resp = restClient.postForObject(env.getUserUrl() + "restAddUser", userManage, JsonResponse.class);

		} catch (RestClientException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("userservice", userManage);
			return "redirect:/user/add-user";
		}
		session.setAttribute("userservice", null);
		logger.info("Method : WEBMODULE UserController submitUser end");
		return "redirect:/user/view-user";
	}

	/**
	 * VIEW USER
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-user-through-ajax")
	public @ResponseBody DataTableResponse viewUserThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {
		logger.info("Method : WEBMODULE UserController viewUserThroughAjax starts");

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
			tableRequest.setParam3(param3);

			JsonResponse<List<UserModel>> jsonResponse = new JsonResponse<List<UserModel>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "restGetAllUsers", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserModel> form = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<UserModel>>() {
			});
			String s = "";

			for (UserModel m : form) {

				/**
				 * ENCODE STARTS
				 *
				 */

				byte[] pId = Base64.getEncoder().encode(m.getUser().getBytes());

				/**
				 * ENCODE ENDS
				 *
				 */

				s = "";
				s = s + "&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "&nbsp;&nbsp;<a href='edit-user?id1=" + new String(pId)
						+ "' ><i class='fa fa-edit edit'></i></a>";
				 
				s = s + "&nbsp;&nbsp;<a href='javascript:void' onclick='DeleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash trash'></i></a>";

				m.setAction(s);
				s = "";
				//System.out.println("view users : " + m.getUserType());
				if (m.getUserStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}
				
				String fullName;

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : WEBMODULE UserController viewUserThroughAjax end");
		return response;
	}

	/**
	 * EDIT USER
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("edit-user")
	public String editUser(Model model, @RequestParam String id1, HttpSession session) {
		logger.info("Method : WEBMODULE UserController editUser starts");

		/**
		 * DROPDOWN UserType LIST
		 *
		 */
		try {
			DropDownModel[] UserRole = restClient.getForObject(env.getUserUrl() + "rest-get-user-type-list",
					DropDownModel[].class);
			List<DropDownModel> userList = Arrays.asList(UserRole);

			model.addAttribute("userList", userList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> countrydata = new ArrayList<DropDownModel>();
	    try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getCountryName?Action=" + "getCountryName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String messageForTblMstr = respTblMstr.getMessage();
		
		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		countrydata = mapper.convertValue(respTblMstr.getBody(),
				new TypeReference<List<DropDownModel>>() {
				});
		model.addAttribute("countrydata", countrydata);


		
		/**
		 * DROPDOWN USER ROLE LIST
		 *
		 */
		try {
			DropDownModel[] UserRole = restClient.getForObject(env.getUserUrl() + "restGetUserRole",
					DropDownModel[].class);
			List<DropDownModel> RoleList = Arrays.asList(UserRole);

			model.addAttribute("roleList", RoleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * DECODE STARTS
		 *
		 */
		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());

		String id = (new String(encodeByte));
		/**
		 * DECODE ENDS
		 *
		 */
		model.addAttribute("adduser", new UserModel());
		UserModel user = new UserModel();
		JsonResponse<UserModel> jsonResponse = new JsonResponse<UserModel>();

		try {

			jsonResponse = restClient.getForObject(env.getUserUrl() + "restGetUserById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		user = mapper1.convertValue(jsonResponse.getBody(), UserModel.class);

		// SPLITING OF USER ROLE SELECTED DATA
		if(user.getUserRole()!=null) {
			user.setUserRoles(Arrays.asList(user.getUserRole().split("\\s*,\\s*")));
		}
		session.setAttribute("message", "");
		String dataforcntid=user.getUserCountry();
		String dataforstid=user.getUserState();
		//System.out.println("user data for edit country-----"+dataforcntid);
		//System.out.println("user data for edit state-----"+dataforstid);
		 
		 
		 
		 JsonResponse<List<DropDownModel>> respTblMstr1 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> statedata1 = new ArrayList<DropDownModel>();
		    try {
				respTblMstr1 = restClient.getForObject(env.getUserUrl() + "getStateName1?id=" +dataforcntid ,
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			String messageForTblMstr1 = respTblMstr1.getMessage();
			
			if (messageForTblMstr1 != null || messageForTblMstr1 != "") {
				model.addAttribute("message", messageForTblMstr1);
			}
			
			ObjectMapper mapper2 = new ObjectMapper();
			
			statedata1 = mapper2.convertValue(respTblMstr1.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			model.addAttribute("statedata1", statedata1);

			
			
			
			 JsonResponse<List<DropDownModel>> respTblMstr3 = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> distdata1 = new ArrayList<DropDownModel>();
		    try {
				respTblMstr1 = restClient.getForObject(env.getUserUrl() + "getDistName1?id=" +dataforstid ,
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			
			String messageForTblMstr3 = respTblMstr1.getMessage();
			
			if (messageForTblMstr3 != null || messageForTblMstr3 != "") {
				model.addAttribute("message", messageForTblMstr3);
			}
			
			ObjectMapper mapper3 = new ObjectMapper();
			
			distdata1 = mapper3.convertValue(respTblMstr1.getBody(),
					new TypeReference<List<DropDownModel>>() {
					});
			model.addAttribute("distdata1", distdata1);
		
		model.addAttribute("adduser", user);
		logger.info("Method : WEBMODULE UserController editUser end");
		return "user/AddUser";
	}

	/**
	 * DELETE USER
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("view-user-delete")
	public @ResponseBody JsonResponse<Object> deleteUserData(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method : WEBMODULE UserController deleteUserData starts");

		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));

		//System.out.println("response delete " + id);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getUserUrl() + "restDeleteUser?id=" + index, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("response delete in web controller "+resp);
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : WEBMODULE UserController deleteUserData ends");
		return resp;
	}

	/**
	 * MODAL USER
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-user-modal" })
	public @ResponseBody JsonResponse<Object> modalViewUser(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : WEBMODULE UserController modalViewUser starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getUserUrl() + "restGetModelById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : WEBMODULE UserController modalViewUser end");
		return res;
	}
	
	
	/**
	 * DROP DOWN DATA FOR STATE TO DISTRICT NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-user-district-list" })
	public @ResponseBody JsonResponse<DropDownModel> getDistrictList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :webcontroller getDistrictList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getUserUrl() + "restGetDistrictListById?proCat=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : webcontroller getDistrictList ends");
		return res;
	}
	
	
	/**
	 * DROP DOWN DATA FOR STATE TO DISTRICT NAME ONCHANGE
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-user-state-list" })
	public @ResponseBody JsonResponse<DropDownModel> getStateList(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method :webcontroller getStateList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			
			res = restClient.getForObject(env.getUserUrl() + "restGetStateListById?proCat=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : webcontroller getStateList ends");
		return res;
	}
	
}
