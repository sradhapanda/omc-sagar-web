package nirmalya.aathithya.webmodule.employee.controller;

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
import nirmalya.aathithya.webmodule.employee.model.HrmGoalMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmGoalMasterController {
	Logger logger = LoggerFactory.getLogger(HrmGoalMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add department view page
	 */
	@GetMapping("/add-goal-master")
	public String addGoalMaster(Model model, HttpSession session) {

		logger.info("Method : addGoalMaster starts");

		HrmGoalMasterModel goalMaster = new HrmGoalMasterModel();
		HrmGoalMasterModel sessionGoal = (HrmGoalMasterModel) session.getAttribute("sessiondepartment");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionGoal != null) {
			model.addAttribute("goalMaster", sessionGoal);
			session.setAttribute("sessionGoal", null);
		} else {
			model.addAttribute("goalMaster", goalMaster);
		}

		logger.info("Method : addGoalMaster ends");

		return "employee/add-goal-master";
	}

	/*
	 * Post Mapping for adding new department
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-goal-master")
	public String addGoalMasterPost(@ModelAttribute HrmGoalMasterModel goalMaster, Model model, HttpSession session) {

		logger.info("Method : addGoalMasterPost starts");

		String userId = "";

		try {
			userId = (String) session.getAttribute("USER_ID");

		} catch (Exception e) {
			e.printStackTrace();
		}
		goalMaster.settGoalCreatedBy(userId);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddGoal", goalMaster, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionGoal", goalMaster);
			return "redirect:/employee/add-goal-master";
		}
		logger.info("Method : addGoalMasterPost ends");

		return "redirect:/employee/view-goal-master";
	}

	/*
	 * Get Mapping view Goal Master
	 */
	@GetMapping("/view-goal-master")
	public String viewGoalMaster(Model model, HttpSession session) {

		logger.info("Method : viewGoalMaster starts");

		logger.info("Method : viewGoalMaster ends");

		return "employee/view-goal-master";
	}

	/*
	 * For view goal master for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-goal-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewGoalMasterAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewGoalMasterAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmGoalMasterModel>> jsonResponse = new JsonResponse<List<HrmGoalMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getGoalDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmGoalMasterModel> goalMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmGoalMasterModel>>() {
					});

			String s = "";

			for (HrmGoalMasterModel m : goalMaster) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.gettGoalId().getBytes());
				s = s + "<a href='view-goal-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteGoalMaster(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
				if (m.gettGoalStatus()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(goalMaster);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewGoalMasterAjax Theme ends");

		return response;
	}

	/*
	 * for Edit Goal Master
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-goal-master-edit")
	public String editGoalMaster(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editGoalMaster starts");

		HrmGoalMasterModel goalMaster = new HrmGoalMasterModel();
		JsonResponse<HrmGoalMasterModel> jsonResponse = new JsonResponse<HrmGoalMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getGoalMasterById?id=" + id,
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

		goalMaster = mapper.convertValue(jsonResponse.getBody(), HrmGoalMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("goalMaster", goalMaster);

		logger.info("Method : editGoalMaster ends");

		return "employee/add-goal-master";
	}

	/*
	 * For Delete department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-goal-master-delete")
	public @ResponseBody JsonResponse<Object> getGoalMasterForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getGoalMasterForDelete ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "deleteGoalMasterById?id=" + id1 + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : getGoalMasterForDelete  ends");

		return resp;
	}

	/*
	 * For Modal Goal Master View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-goal-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalGoalMaster(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalGoalMaster starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getGoalMasterById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalGoalMaster ends");
		return res;
	}

}
