/**
 * web Controller for User set authority 
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.UserSetAuthority;

@Controller
@RequestMapping(value = "user")
public class UserSetAuthorityController {
	Logger logger = LoggerFactory.getLogger(UserSetAuthorityController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * GEt Mapping for Add set Authority
	 * 
	 */
	@GetMapping("/add-set-authority")
	public String addSetAuthority(Model model, HttpSession session) {
		logger.info("Method : addSetAuthority starts");

		UserSetAuthority setAuthority = new UserSetAuthority();

		UserSetAuthority form = (UserSetAuthority) session.getAttribute("ssetAuthority");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("setAuthority", form);
			session.setAttribute("ssetAuthority", null);
		} else {
			model.addAttribute("setAuthority", setAuthority);
		}
		/**
		 * dropdown data for process
		 */
		try {
			DropDownModel[] process = restClient.getForObject(env.getUserUrl() + "getProcess", DropDownModel[].class);

			List<DropDownModel> processList = Arrays.asList(process);
			model.addAttribute("processList", processList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * dropdown data for user role
		 */
		try {
			DropDownModel[] userRole = restClient.getForObject(env.getUserUrl() + "getUserRole", DropDownModel[].class);

			List<DropDownModel> userRoleList = Arrays.asList(userRole);
			model.addAttribute("userRoleList", userRoleList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		/**
		 * dropdown data for user
		 */
		try {
			DropDownModel[] userdata = restClient.getForObject(env.getUserUrl() + "getUserData", DropDownModel[].class);

			List<DropDownModel> userdataList = Arrays.asList(userdata);
			model.addAttribute("userdataList", userdataList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * dropdown data for approval action
		 */
		try {
			DropDownModel[] approval = restClient.getForObject(env.getUserUrl() + "getApproval", DropDownModel[].class);

			List<DropDownModel> approvalList = Arrays.asList(approval);
			model.addAttribute("approvalList", approvalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addSetAuthority end");
		return "user/addSetAuthority";
	}

	/* onchange function for get user name from user role */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-set-authority-get-username-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getUserNamesAjax(Model model, @RequestBody String userRole,
			BindingResult result) {
		logger.info("Method : getUserNamesAjax starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getUserUrl() + "get-username-from-userrole?id=" + userRole,
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getUserNamesAjax ends");

		return res;
	}

	/**
	 * post Mapping for submit data
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-set-authority", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> postSetAuthority(@RequestBody List<UserSetAuthority> setAuthority,
			Model model, HttpSession session) {

		logger.info("Method : saveNightClubPackageDetails function starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.postForObject(env.getUserUrl() + "addSetAuthority", setAuthority, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : postSetAuthority function Ends");
		return res;
	}

	/**
	 * view set authority
	 */

	@GetMapping("/view-set-authority")
	public String viewSetAuthority(Model model, HttpSession session) {
		/**
		 * dropdown data for process
		 */
		try {
			DropDownModel[] process = restClient.getForObject(env.getUserUrl() + "getProcess", DropDownModel[].class);

			List<DropDownModel> processList = Arrays.asList(process);
			model.addAttribute("processList", processList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * dropdown data for user
		 */
		try {
			DropDownModel[] userdata = restClient.getForObject(env.getUserUrl() + "getUserData", DropDownModel[].class);

			List<DropDownModel> userdataList = Arrays.asList(userdata);
			model.addAttribute("userdataList", userdataList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * dropdown data for user role
		 */
		try {
			DropDownModel[] userRole = restClient.getForObject(env.getUserUrl() + "getUserRole", DropDownModel[].class);

			List<DropDownModel> userRoleList = Arrays.asList(userRole);
			model.addAttribute("userRoleList", userRoleList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return "user/listSetAuthority";
	}

	/**
	 * view set authority through ajax
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-set-authority-throughajax")
	public @ResponseBody DataTableResponse viewAuthorityThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2, @RequestParam String param3) {
		logger.info("Method : viewAuthorityThroughAjax starts");
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
			JsonResponse<List<UserSetAuthority>> jsonResponse = new JsonResponse<List<UserSetAuthority>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "getAllSetAuthority", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserSetAuthority> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserSetAuthority>>() {
					});

			String s = "";
			for (UserSetAuthority m : form) {
				byte[] encodeid = Base64.getEncoder().encode(m.getProcessId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ m.getProcessId() + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='view-set-authority-edit?id=" + new String(encodeid)
						+ "' ><i class=\"fa fa-edit\"></i></a>  ";

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewAuthorityThroughAjax end");
		return response;
	}

	/**
	 * Edit set authority
	 */
	@GetMapping("view-set-authority-edit")
	public String editSetAuthority(Model model, @RequestParam("id") String index, HttpSession session) {
		logger.info("Method : editSetAuthority starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		/**
		 * get DropDown value for process Name
		 *
		 */

		try {
			DropDownModel[] process = restClient.getForObject(env.getUserUrl() + "getProcess", DropDownModel[].class);

			List<DropDownModel> processList = Arrays.asList(process);
			model.addAttribute("processList", processList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * dropdown data for user role
		 */
		try {
			DropDownModel[] userRole = restClient.getForObject(env.getUserUrl() + "getUserRole", DropDownModel[].class);

			List<DropDownModel> userRoleList = Arrays.asList(userRole);
			model.addAttribute("userRoleList", userRoleList);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		try {
			UserSetAuthority[] setAuthority = restClient
					.getForObject(env.getUserUrl() + "edit-set-authority-ById?id=" + id, UserSetAuthority[].class);
			List<UserSetAuthority> userSetAuthority = Arrays.asList(setAuthority);

			int c = 0;
			List<Object> newObj = new ArrayList<Object>();
			List<Object> actObj = new ArrayList<Object>();
			List<Object> activity2 = new ArrayList<Object>();
			for (UserSetAuthority m : userSetAuthority) {
				try {

					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getUserUrl() + "get-user-edit-list?id=" + m.getUserRole(), DropDownModel[].class);
					List<DropDownModel> userList = Arrays.asList(dropDownModel);
					newObj.add(userList);
					c = c + 1;

				} catch (RestClientException e) {
					e.printStackTrace();
				}

				try {

					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getUserUrl() + "rest-selected-user?id=" + m.getProcess() + "&catId=" + m.getUserRole(),
							DropDownModel[].class);
					List<DropDownModel> selectedList = Arrays.asList(dropDownModel);
					actObj.add(selectedList);

					for (DropDownModel p : selectedList) {
						activity2.add(p.getKey());
					}

					c = c + 1;
				} catch (RestClientException e) {
					e.printStackTrace();
				}

			}
			model.addAttribute("userSetAuthority", userSetAuthority);
			model.addAttribute("id", userSetAuthority.get(0).getIsEdit());
			model.addAttribute("userList", newObj);
			model.addAttribute("activity2", activity2);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		logger.info("Method : editSetAuthority starts");

		return "user/addSetAuthority";
	}

	/*
	 * for model view of set authority
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-set-authority-model" })
	public @ResponseBody JsonResponse<UserSetAuthority> ModelView(@RequestParam String index) {
		logger.info("Method : ModelView starts");
		JsonResponse<UserSetAuthority> response = new JsonResponse<UserSetAuthority>();
		try {
			response = restClient.getForObject(env.getUserUrl() + "get-set-authority-ById-model?id=" + index,
					JsonResponse.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + response.getMessage());
			response.setCode(response.getMessage());
			response.setMessage("Unsuccess");
		} else {
			response.setMessage("success");
		}

		logger.info("Method : ModelView ends");
		return response;
	}

}
