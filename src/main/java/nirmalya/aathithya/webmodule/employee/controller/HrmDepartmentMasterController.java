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
import nirmalya.aathithya.webmodule.employee.model.HrmDepartmentMasterModel;



/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmDepartmentMasterController {

	Logger logger = LoggerFactory.getLogger(HrmDepartmentMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add department view page
	 */
	@GetMapping("/add-department-master")
	public String adddepartmentMaster(Model model, HttpSession session) {

		logger.info("Method : adddepartmentMaster starts");

		HrmDepartmentMasterModel department = new HrmDepartmentMasterModel();
		HrmDepartmentMasterModel sessiondepartment = (HrmDepartmentMasterModel) session
				.getAttribute("sessiondepartment");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessiondepartment != null) {
			model.addAttribute("department", sessiondepartment);
			session.setAttribute("sessiondepartment", null);
		} else {
			model.addAttribute("department", department);
		}

		logger.info("Method : adddepartmentMaster ends");

		return "employee/add-department-master";
	}

	/*
	 * Post Mapping for adding new department
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-department-master")
	public String adddepartmentMasterPost(@ModelAttribute HrmDepartmentMasterModel department, Model model,
			HttpSession session) {

		logger.info("Method : adddepartmentMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		department.setCreatedBy(userId);
		department.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAdddepartments", department, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessiondepartment", department);
			return "redirect:/employee/add-department-master";
		}
		logger.info("Method : adddepartmentMasterPost ends");

		return "redirect:/employee/view-department-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-department-master")
	public String viewdepartmentMaster(Model model, HttpSession session) {

		logger.info("Method : viewdepartmentMaster starts");

		logger.info("Method : viewdepartmentMaster ends");

		return "employee/view-department-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewdepartmentMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewdepartmentMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmDepartmentMasterModel>> jsonResponse = new JsonResponse<List<HrmDepartmentMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getdepartmentDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmDepartmentMasterModel> department = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmDepartmentMasterModel>>() {
					});

			String s = "";

			for (HrmDepartmentMasterModel m : department) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getDepartmentId().getBytes());
				s = s + "<a href='view-department-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletedepartment(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getDepartmentStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(department);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewdepartmentMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-master-edit")
	public String editdepartment(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editdepartment starts");

		HrmDepartmentMasterModel department = new HrmDepartmentMasterModel();
		JsonResponse<HrmDepartmentMasterModel> jsonResponse = new JsonResponse<HrmDepartmentMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getdepartmentById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		department = mapper.convertValue(jsonResponse.getBody(), HrmDepartmentMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("department", department);

		logger.info("Method : editdepartment ends");

		return "employee/add-department-master";
	}

	/*
	 * For Delete department
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-master-delete")
	public @ResponseBody JsonResponse<Object> getdepartmentForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getdepartmentForDelete ends");

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
					env.getEmployeeUrl() + "deletedepartmentById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletedepartment  ends");

		return resp;
	}

	/*
	 * For Modal department View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-department-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalDepartment starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getdepartmentById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalDepartment ends");
		return res;
	}

}
