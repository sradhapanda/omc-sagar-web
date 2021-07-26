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
import nirmalya.aathithya.webmodule.employee.model.HrmsSkillMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsSkillMasterController {

	Logger logger = LoggerFactory.getLogger(HrmsSkillMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add skill view page
	 */
	@GetMapping("/add-skill-master")
	public String addskillMaster(Model model, HttpSession session) {

		logger.info("Method : addskillMaster starts");

		HrmsSkillMasterModel skill = new HrmsSkillMasterModel();
		HrmsSkillMasterModel sessionskill = (HrmsSkillMasterModel) session.getAttribute("sessionskill");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionskill != null) {
			model.addAttribute("skill", sessionskill);
			session.setAttribute("sessionskill", null);
		} else {
			model.addAttribute("skill", skill);
		}

		logger.info("Method : addskillMaster ends");

		return "employee/add-skill-master";
	}

	/*
	 * Post Mapping for adding new skill
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-skill-master")
	public String addskillMasterPost(@ModelAttribute HrmsSkillMasterModel skill, Model model, HttpSession session) {

		logger.info("Method : addskillMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		skill.setCreatedBy(userId);
		skill.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddskills", skill, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionskill", skill);
			return "redirect:/employee/add-skill-master";
		}
		logger.info("Method : addskillMasterPost ends");

		return "redirect:/employee/view-skill-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-skill-master")
	public String viewskillMaster(Model model, HttpSession session) {

		logger.info("Method : viewskillMaster starts");

		logger.info("Method : viewskillMaster ends");

		return "employee/view-skill-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-skill-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewskillMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewskillMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsSkillMasterModel>> jsonResponse = new JsonResponse<List<HrmsSkillMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getskillDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsSkillMasterModel> skill = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsSkillMasterModel>>() {
					});

			String s = "";

			for (HrmsSkillMasterModel m : skill) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getSkillId().getBytes());
				s = s + "<a href='view-skill-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" ></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteskill(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getSkillStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(skill);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewskillMasterjax  ends");

		return response;
	}

	/*
	 * for Edit skill
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-skill-master-edit")
	public String editskill(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editskill starts");

		HrmsSkillMasterModel skill = new HrmsSkillMasterModel();
		JsonResponse<HrmsSkillMasterModel> jsonResponse = new JsonResponse<HrmsSkillMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getskillById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		skill = mapper.convertValue(jsonResponse.getBody(), HrmsSkillMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("skill", skill);

		logger.info("Method : editskill ends");

		return "employee/add-skill-master";
	}

	/*
	 * For Delete skill
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-skill-master-delete")
	public @ResponseBody JsonResponse<Object> getskillForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getskillForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deleteskillById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteskill  ends");

		return resp;
	}

	/*
	 * For Modal skill View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-skill-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalskill starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getskillById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalskill ends");
		return res;
	}

}
