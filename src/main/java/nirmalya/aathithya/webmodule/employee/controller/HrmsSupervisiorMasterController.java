package nirmalya.aathithya.webmodule.employee.controller;

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
import nirmalya.aathithya.webmodule.employee.model.HrmsSupervisorMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsSupervisiorMasterController {

	Logger logger = LoggerFactory.getLogger(HrmsSupervisiorMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add supervisor view page
	 */
	@GetMapping("/add-supervisor-master")
	public String addsupervisorMaster(Model model, HttpSession session) {

		logger.info("Method : addsupervisorMaster starts");

		HrmsSupervisorMasterModel supervisor = new HrmsSupervisorMasterModel();
		HrmsSupervisorMasterModel sessionsupervisor = (HrmsSupervisorMasterModel) session
				.getAttribute("sessionsupervisor");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionsupervisor != null) {
			model.addAttribute("supervisor", sessionsupervisor);
			session.setAttribute("sessionsupervisor", null);
		} else {
			model.addAttribute("supervisor", supervisor);
		}
		try {
			DropDownModel[] dept = restClient.getForObject(env.getEmployeeUrl() + "getDeptList", DropDownModel[].class);
			List<DropDownModel> DeptList = Arrays.asList(dept);
			model.addAttribute("DeptList", DeptList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addsupervisorMaster ends");

		return "employee/add-supervisor-master";
	}

	/*
	 * Post Mapping for adding new supervisor
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-supervisor-master")
	public String addsupervisorMasterPost(@ModelAttribute HrmsSupervisorMasterModel supervisor, Model model,
			HttpSession session) {

		logger.info("Method : addsupervisorMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		supervisor.setCreatedBy(userId);
		supervisor.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddsupervisors", supervisor, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionsupervisor", supervisor);
			return "redirect:/employee/add-supervisor-master";
		}
		logger.info("Method : addsupervisorMasterPost ends");

		return "redirect:/employee/view-supervisor-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-supervisor-master")
	public String viewsupervisorMaster(Model model, HttpSession session) {

		logger.info("Method : viewsupervisorMaster starts");

		logger.info("Method : viewsupervisorMaster ends");

		return "employee/view-supervisor-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-supervisor-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewsupervisorMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewsupervisorMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsSupervisorMasterModel>> jsonResponse = new JsonResponse<List<HrmsSupervisorMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getsupervisorDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsSupervisorMasterModel> supervisor = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsSupervisorMasterModel>>() {
					});

			String s = "";

			for (HrmsSupervisorMasterModel m : supervisor) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getSpId().getBytes());
				s = s + "<a href='view-supervisor-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletesupervisor(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getStatus()) {
					m.setStstusName("Active");
				} else {
					m.setStstusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(supervisor);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewsupervisorMasterjax   ends");

		return response;
	}

	/*
	 * for Edit supervisor
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-supervisor-master-edit")
	public String editsupervisor(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editsupervisor starts");

		HrmsSupervisorMasterModel supervisor = new HrmsSupervisorMasterModel();
		JsonResponse<HrmsSupervisorMasterModel> jsonResponse = new JsonResponse<HrmsSupervisorMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getsupervisorById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		try {
			DropDownModel[] dept = restClient.getForObject(env.getEmployeeUrl() + "getDeptList", DropDownModel[].class);
			List<DropDownModel> DeptList = Arrays.asList(dept);
			model.addAttribute("DeptList", DeptList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		supervisor = mapper.convertValue(jsonResponse.getBody(), HrmsSupervisorMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("supervisor", supervisor);

		logger.info("Method : editsupervisor ends");

		return "employee/add-supervisor-master";
	}

	/*
	 * For Delete supervisor
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-supervisor-master-delete")
	public @ResponseBody JsonResponse<Object> getsupervisorForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getsupervisorForDelete ends");

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
					env.getEmployeeUrl() + "deletesupervisorById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletesupervisor  ends");

		return resp;
	}

	/*
	 * For Modal supervisor View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-supervisor-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalsupervisor starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getsupervisorById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalsupervisor ends");
		return res;
	}
	
	/*
	 * for Employee list auto complete
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-supervisor-master-employeeAutoComplete" })
	public @ResponseBody JsonResponse<DropDownModel> getGuestList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : get Employee list  starts");

		JsonResponse<DropDownModel> response = new JsonResponse<DropDownModel>();
		try {
			response = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeByAutosearch?id=" + searchValue,
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
		logger.info("Method : get Employee list  ends");
		return response;
	}

}
