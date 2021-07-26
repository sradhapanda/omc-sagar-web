package nirmalya.aathithya.webmodule.user.controller;

/*
 * for Aproval Activity web Controller
 */

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
import nirmalya.aathithya.webmodule.user.model.UserApprovalActionModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "user")

public class UserAprovalActivityController {

	Logger logger = LoggerFactory.getLogger(UserAprovalActivityController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Theme view page
	 */
	@GetMapping("/add-aproval-action")
	public String addPropertyTheme(Model model, HttpSession session) {

		logger.info("Method : add-aproval-action starts");

		UserApprovalActionModel aproval = new UserApprovalActionModel();
		UserApprovalActionModel sessionAproval = (UserApprovalActionModel) session.getAttribute("saproval");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionAproval != null) {
			model.addAttribute("aproval", sessionAproval);
			session.setAttribute("saproval", null);
		} else {
			model.addAttribute("aproval", aproval);
		}

		logger.info("Method : add-aproval-action ends");

		return "user/add-aproval-action";
	}

	/*
	 * Get Mapping view approval action
	 */
	@GetMapping("/view-aproval-action")
	public String lstpropertyTheme(Model model, HttpSession session) {

		logger.info("Method : lstPropertyTheme starts");

		JsonResponse<Object> aproval = new JsonResponse<Object>();
		model.addAttribute("aproval", aproval);

		logger.info("Method : lstPropertyTheme ends");

		return "user/list-aproval-action";
	}

	/*
	 * Post Mapping for adding new approval action
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-aproval-action")
	public String addApprovalAction(@ModelAttribute UserApprovalActionModel aproval, Model model, HttpSession session) {

		logger.info("Method : add-approval-action starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		aproval.setCreatedBy("my name");
		try {

			resp = restClient.postForObject(env.getUserUrl() + "restAddApproval", aproval, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("aproval", aproval);
			return "redirect:/user/add-aproval-action";
		}
		logger.info("Method : add-approval-action ends");

		return "redirect:/user/view-aproval-action";
	}

	/*
	 * for Edit Property Theme
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-aproval-action-edit-approval")
	public String editApprovalAction(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method : editApprovalAction starts");

		UserApprovalActionModel aproval = new UserApprovalActionModel();
		JsonResponse<UserApprovalActionModel> jsonResponse = new JsonResponse<UserApprovalActionModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getUserUrl() + "getApprovalById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		aproval = mapper.convertValue(jsonResponse.getBody(), UserApprovalActionModel.class);
		session.setAttribute("message", "");

		model.addAttribute("aproval", aproval);

		logger.info("Method : editApprovalAction ends");

		return "user/add-aproval-action";
	}

	/*
	 * For Delete Theme
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-aproval-action-deleteApproval")
	public @ResponseBody JsonResponse<Object> deleteApprovalById(@RequestParam String id, HttpSession session) {

		logger.info("Method : delete Approval action Starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "Delete by me";
		try {
			resp = restClient.getForObject(
					env.getUserUrl() + "deleteApprovalById?id=" + id1 + "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : delete Approval action ends");
		// return "property/ListPropertyType";
		return resp;
	}

	/*
	 * For viewTheme for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-aproval-action-ThroughAjax")
	public @ResponseBody DataTableResponse viewAprrovalThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : view approval action ThrowAjax statrs");

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

			JsonResponse<List<UserApprovalActionModel>> jsonResponse = new JsonResponse<List<UserApprovalActionModel>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "getAllApprovals", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserApprovalActionModel> aproval = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserApprovalActionModel>>() {
					});

			String s = "";

			for (UserApprovalActionModel m : aproval) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getApprovalId().getBytes());
				s = s + "<a href='view-aproval-action-edit-approval?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteApproval(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getActStatus()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(aproval);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : view throw ajax approval action ends");

		return response;
	}

	/*
	 * For Modal Theme View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-aproval-action-modalView" })
	public @ResponseBody JsonResponse<Object> modalApproavlAction(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modal approval action starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getUserUrl() + "getApprovalById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			System.out.println("if block getmsg() not false : " + res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modal approval action ends");
		return res;
	}

}
