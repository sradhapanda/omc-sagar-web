/**
 * 
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
import nirmalya.aathithya.webmodule.user.model.UserPrefixManagementModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "user/")
public class UserPrefixManagementController {

	Logger logger = LoggerFactory.getLogger(UserPrefixManagementController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * GET USER ADD PREFIX MANAGEMENT
	 *
	 */
	@GetMapping("add-prefix-management")
	public String addPrefixManagement(Model model, HttpSession session) {
		logger.info("Method : WEBMODULE UserPrefixManagementController addPrefixManagement starts");
		model.addAttribute("addprefix", new UserPrefixManagementModel());
		UserPrefixManagementModel prefixForm = (UserPrefixManagementModel) session.getAttribute("prefixservice");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (prefixForm != null) {
			model.addAttribute("addprefix", prefixForm);
		} else {
			model.addAttribute("addprefix", new UserPrefixManagementModel());
		}
		logger.info("Method : WEBMODULE UserPrefixManagementController addPrefixManagement ends");
		return "user/AddPrefixManagement";
	}

	/**
	 * GET USER VIEW PREFIX MANAGEMENT
	 *
	 */
	@GetMapping("view-prefix-management")
	public String viewPrefixManagement(Model model, HttpSession session) {

		logger.info("Method : WEBMODULE UserPrefixManagementController viewPrefixManagement starts");
		logger.info("Method : WEBMODULE UserPrefixManagementController viewPrefixManagement end");
		return "user/ViewPrefixManagement";
	}

	/**
	 * POST USER PREFIX
	 *
	 */

	@PostMapping("add-prefix-management")
	public String submitUserPrefix(@ModelAttribute UserPrefixManagementModel addUserPrefix, Model model,
			HttpSession session) {
		logger.info("Method : WEBMODULE UserPrefixManagementController submitUserPrefix starts");
		JsonResponse resp = new JsonResponse();

		try {
			resp = restClient.postForObject(env.getUserUrl() + "restUpdateUserPrefix", addUserPrefix,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("prefixservice", addUserPrefix);
			return "redirect:/user/add-prefix-management";
		}
		session.setAttribute("prefixservice", null);
		logger.info("Method : WEBMODULE UserPrefixManagementController submitUserPrefix end");
		return "redirect:/user/view-prefix-management";
	}

	/**
	 * VIEW PREFIX MANAGEMENT AJAX CALL
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-prefix-management-through-ajax")
	public @ResponseBody DataTableResponse viewPrefixThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : WEBMODULE UserPrefixManagementController viewPrefixThroughAjax starts");

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

			JsonResponse<List<UserPrefixManagementModel>> jsonResponse = new JsonResponse<List<UserPrefixManagementModel>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "restGetAllUserPrefix", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserPrefixManagementModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserPrefixManagementModel>>() {
					});
			String s = "";

			for (UserPrefixManagementModel m : form) {
				/**
				 * ENCODE STARTS
				 *
				 */

				// byte[] pId = Base64.getEncoder().encode(m.getPrefix().get);

				/**
				 * ENCODE ENDS
				 *
				 */
				s = "";
				s = s + "<a href='edit-user-prefix?id=" + m.getPrefix() + "' ><i class='fa fa-edit edit'></i></a>";
				//s = s + "<a href='delete-user-prefix?id=" + m.getPrefix() + "' ><i class='fa fa-trash trash'></i></a>";
				s = s + "<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ m.getPrefix() + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

				if (m.getPrfxActive()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

				if (m.getPrfxFinYearStatus()) {
					m.setFinYearStatusName("Yes");
				} else {
					m.setFinYearStatusName("No");
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
		logger.info("Method : WEBMODULE UserPrefixManagementController viewPrefixThroughAjax end");
		return response;
	}

	/**
	 * EDIT USER PREFIX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("edit-user-prefix")
	public String editUserPrefix(Model model, @RequestParam String id, HttpSession session) {
		logger.info("Method : WEBMODULE UserPrefixManagementController editUserPrefix start");

		/**
		 * DECODE STARTS
		 *
		 */
		// byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());

		// String id = (new String(encodeByte));
		/**
		 * DECODE ENDS
		 *
		 */
		model.addAttribute("addprefix", new UserPrefixManagementModel());

		UserPrefixManagementModel prefixservice = new UserPrefixManagementModel();
		JsonResponse<UserPrefixManagementModel> jsonResponse = new JsonResponse<UserPrefixManagementModel>();

		try {
			jsonResponse = restClient.getForObject(env.getUserUrl() + "restGetUserPrefixById?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		prefixservice = mapper.convertValue(jsonResponse.getBody(), UserPrefixManagementModel.class);

		session.setAttribute("message", "");

		model.addAttribute("addprefix", prefixservice);

		logger.info("Method : WEBMODULE UserPrefixManagementController editUserPrefix end");
		return "user/AddPrefixManagement";
	}

	/**
	 * DELETE USER PREFIX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("delete-user-prefix")
	public String deleteUserPrefix(@RequestParam String id, HttpSession session) {
		logger.info("Method : WEBMODULE UserPrefixManagementController deleteUserPrefix starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getUserUrl() + "restDeleteUserPrefix?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
		}
		logger.info("Method : WEBMODULE UserPrefixManagementController deleteUserPrefix end");
		return "redirect:/user/view-prefix-management";
	}

	/**
	 * MODAL USER PREFIX
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "modal-view-user-prefix" })
	public @ResponseBody JsonResponse<Object> modalUserPrefix(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : WEBMODULE UserPrefixManagementController modalUserPrefix starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getUserUrl() + "restGetUserPrefixById?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : WEBMODULE UserPrefixManagementController modalUserPrefix end");
		return res;
	}

}
