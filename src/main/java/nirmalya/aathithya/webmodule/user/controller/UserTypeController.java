/**
 * web Controller for User Type 
 */
package nirmalya.aathithya.webmodule.user.controller;

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
import nirmalya.aathithya.webmodule.user.model.UserTypeModel;

@Controller
@RequestMapping(value = "user")
public class UserTypeController {
	Logger logger = LoggerFactory.getLogger(UserTypeController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * GEt Mapping for Add User Type
	 * 
	 */
	@GetMapping("/add-user-type")
	public String addUserType(Model model, HttpSession session) {
		logger.info("Method : addUserType starts");

		UserTypeModel userType = new UserTypeModel();

		UserTypeModel form = (UserTypeModel) session.getAttribute("c");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("userType", form);
			session.setAttribute("userType", null);

		} else {
			model.addAttribute("userType", userType);
		}
		return "user/addUserType";
	}

	/**
	 * post mapping for adding user type
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-user-type")
	public String postUserType(@ModelAttribute UserTypeModel userType, Model model, HttpSession session) {
		logger.info("Method : PostUserType starts");
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			userType.setCreatedBy(userId);
			resp = restClient.postForObject(env.getUserUrl() + "addUserType", userType, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("suserType", userType);
			return "redirect:/user/add-user-type";
		}
		session.setAttribute("suserType", null);
		logger.info("Method : postUserTypr end");
		return "redirect:/user/view-user-type";
	}

	/**
	 * get mapping for view user type
	 */

	@GetMapping("/view-user-type")
	public String listUserType(Model model, HttpSession session) {
		logger.info("Method : listUserType starts");
		logger.info("Method : listUserType starts");
		return "user/listUserType";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-user-type-throughajax")
	public @ResponseBody DataTableResponse viewUserTypeThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewUserTypeThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {

			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<UserTypeModel>> jsonResponse = new JsonResponse<List<UserTypeModel>>();
			jsonResponse = restClient.postForObject(env.getUserUrl() + "getAllUserTypes", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserTypeModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserTypeModel>>() {
					});

			String s = "";
			for (UserTypeModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getUserType().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getUserType() + "\")'><i class='fa fa-search search'></i></a>";
				
				s = s + " &nbsp;&nbsp <a href='view-user-type-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>";
				
				/* Removing the Delete section
				 s = s + " &nbsp;&nbsp <a href='view-user-type-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href='javascript:void(0)'\"+ \"' onclick='deleteUserType(  \""
						+ m.getUserType() + "\")' ><i class=\"fa fa-trash\"></i></a>";
						*/

				m.setAction(s);
				s = "";

				if (m.getUserTypeActive()) {
					m.setUserStatus("Active");
				} else {
					m.setUserStatus("Inactive");

				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewUserTypeThroughAjax end");
		return response;
	}

	/**
	 * get mapping for delete user type
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-user-type-delete")
	public @ResponseBody JsonResponse<Object> deleteUserType(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteUserType starts");
		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			String createdBy = "u0002";
			resp = restClient.getForObject(env.getUserUrl() + "deleteUserTypeById?id=" + id+ "&createdBy=" + userId, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteUserType ends");
		return resp;
	}

	/**
	 * get mapping for edit user type
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-user-type-edit")
	public String editUserType(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editUserType starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		UserTypeModel userType = new UserTypeModel();
		JsonResponse<UserTypeModel> jsonResponse = new JsonResponse<UserTypeModel>();

		try {
			jsonResponse = restClient.getForObject(env.getUserUrl() + "getUserTypeById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		userType = mapper.convertValue(jsonResponse.getBody(), UserTypeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("userType", userType);

		logger.info("Method : editusertype end");
		return "user/addUserType";
	}

	/**
	 * get mapping for viewInModelData property floor
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-user-type-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		/* List<Floor> res = new ArrayList<Floor>(); */

		try {
			res = restClient.getForObject(env.getUserUrl() + "getUserTypeById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : modelView end");
		return res;
	}
}
