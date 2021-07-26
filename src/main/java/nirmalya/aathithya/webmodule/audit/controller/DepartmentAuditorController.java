package nirmalya.aathithya.webmodule.audit.controller;

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
import nirmalya.aathithya.webmodule.audit.model.DepartmentAuditorModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class DepartmentAuditorController {
	Logger logger = LoggerFactory.getLogger(DepartmentAuditorController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "add-department-auditor" })
	public String addDepartmentAuditor(Model model, HttpSession session) {
		logger.info("Method : addDepartmentAuditor starts");
		DepartmentAuditorModel departmentAuditorModel = new DepartmentAuditorModel();

		DepartmentAuditorModel sDepartmentAuditorModel = (DepartmentAuditorModel) session
				.getAttribute("sDepartmentAuditorModel");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sDepartmentAuditorModel != null) {
			model.addAttribute("departmentAuditorModel", sDepartmentAuditorModel);
			session.setAttribute("departmentAuditorModel", null);

		} else {
			model.addAttribute("departmentAuditorModel", departmentAuditorModel);
		}
		try {
			DropDownModel[] department = restTemplate.getForObject(env.getMasterUrl() + "get-department-list-Name",
					DropDownModel[].class);

			List<DropDownModel> departmentList = Arrays.asList(department);
			model.addAttribute("departmentList", departmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] auditor = restTemplate.getForObject(env.getMasterUrl() + "get-concernAuditee-Name",
					DropDownModel[].class);

			List<DropDownModel> auditeeList = Arrays.asList(auditor);
			model.addAttribute("auditeeList", auditeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DropDownModel[] departmentHead = restTemplate.getForObject(env.getMasterUrl() + "get-dep-head-list-Name",
					DropDownModel[].class);

			List<DropDownModel> depHeadList = Arrays.asList(departmentHead);
			model.addAttribute("depHeadList", depHeadList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addDepartmentAuditor ends");
		return "audit/addDepartmentAuditor";
	}

	/**
	 * Add Section Master Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-department-auditor" })
	public String addDepartmentMaster(@ModelAttribute DepartmentAuditorModel departmentAuditorModel, Model model,
			HttpSession session) {
		logger.info("Method : addDepartmentMaster starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			departmentAuditorModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "addDepartment", departmentAuditorModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("departmentAuditorModel", departmentAuditorModel);
			return "redirect:add-department-auditor";
		}
		logger.info("Method : addDepartmentMaster ends");
		return "redirect:view-department-auditor";
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-department-auditor-getDepartment" })
	public @ResponseBody JsonResponse<DropDownModel> getSectionFromDepartment(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getSectionFromDepartment starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "getSectionByDepartmentAudit?department=" + index,
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

		logger.info("Method : getSectionFromDepartment ends");
		return res;
	}

	@GetMapping(value = { "view-department-auditor" })
	public String viewDepartmentAuditor(Model model) {
		logger.info("Method : viewDepartmentAuditor starts");
		try {
			DropDownModel[] department = restTemplate.getForObject(env.getMasterUrl() + "get-department-list-Name",
					DropDownModel[].class);

			List<DropDownModel> departmentList = Arrays.asList(department);
			model.addAttribute("departmentList", departmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] auditor = restTemplate.getForObject(env.getMasterUrl() + "get-auditor-list-Name",
					DropDownModel[].class);

			List<DropDownModel> auditorList = Arrays.asList(auditor);
			model.addAttribute("auditorList", auditorList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] auditee = restTemplate.getForObject(env.getMasterUrl() + "get-auditee-list-Name",
					DropDownModel[].class);

			List<DropDownModel> auditeeList = Arrays.asList(auditee);
			model.addAttribute("auditeeList", auditeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] departmentHead = restTemplate.getForObject(env.getMasterUrl() + "get-dep-head-list-Name",
					DropDownModel[].class);

			List<DropDownModel> depHeadList = Arrays.asList(departmentHead);
			model.addAttribute("depHeadList", depHeadList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] section = restTemplate.getForObject(env.getMasterUrl() + "get-section-list-Name",
					DropDownModel[].class);

			List<DropDownModel> sectionList = Arrays.asList(section);
			model.addAttribute("sectionList", sectionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewDepartmentAuditor ends");
		return "audit/viewDepartmentAuditor";
	}

	/**
	 * call All Service through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-auditor-throughAjax")
	public @ResponseBody DataTableResponse viewDepartmentAuditorThroughAjax(Model model, HttpServletRequest request,
			 @RequestParam String param2, @RequestParam String param3,
			@RequestParam String param4) {
		logger.info("Method : viewDepartmentAuditorThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			/* tableRequest.setParam1(param1); */
			tableRequest.setParam2(param2);
			tableRequest.setParam3(param3);
			tableRequest.setParam4(param4);
			/* tableRequest.setParam5(param5);*/

			JsonResponse<List<DepartmentAuditorModel>> jsonResponse = new JsonResponse<List<DepartmentAuditorModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllDepartmentAuditor", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<DepartmentAuditorModel> deptAuditorModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<DepartmentAuditorModel>>() {
					});

			String s = "";
			for (DepartmentAuditorModel m : deptAuditorModel) {
				byte[] pId = Base64.getEncoder().encode(m.getDepId().getBytes());
				byte[] sId = Base64.getEncoder().encode(m.getSecId().getBytes());
				if (m.getStatus()) {
					s = "";
					s = s + "Active";
				} else {
					s = "";
					s = s + "Inactive";
				}
				m.setStatusName(s);
				;
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
				+ new String(pId) + "," + new String(sId) + "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				s = s + "<a title='Edit' href='view-department-auditor-edit?id=" + new String(pId) + ","
				+ new String(sId) + "' '><i class='fa fa-edit edit' style=\"font-size:20px\"></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId) + "," + new String(sId)
				+ "\")'><i class='fa fa-trash' style=\"font-size:20px\"></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(deptAuditorModel);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewDepartmentAuditorThroughAjax ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-department-auditor-edit")
	public String editDepAuditor(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editDepAuditor starts");
		// System.out.println(encodedIndex);
		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String secId = new String(encodeByte1);

		JsonResponse<DepartmentAuditorModel> res = new JsonResponse<DepartmentAuditorModel>();
		DepartmentAuditorModel departmentAuditorModel = new DepartmentAuditorModel();

		try {

			res = restTemplate.getForObject(env.getMasterUrl() + "viewDepAuditorEdit?id=" + id + "&secId=" + secId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper1 = new ObjectMapper();

		departmentAuditorModel = mapper1.convertValue(res.getBody(), DepartmentAuditorModel.class);

		try {
			DropDownModel[] section = restTemplate.getForObject(env.getMasterUrl()
					+ "getSectionByDepartmentAuditEdit?department=" + departmentAuditorModel.getDepartmentId(),
					DropDownModel[].class);

			List<DropDownModel> sectionList = Arrays.asList(section);
			model.addAttribute("sectionList", sectionList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			DropDownModel[] department = restTemplate.getForObject(env.getMasterUrl() + "get-department-list-Name",
					DropDownModel[].class);

			List<DropDownModel> departmentList = Arrays.asList(department);
			model.addAttribute("departmentList", departmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] auditor = restTemplate.getForObject(env.getMasterUrl() + "get-concernAuditee-Name",
					DropDownModel[].class);

			List<DropDownModel> auditeeList = Arrays.asList(auditor);
			model.addAttribute("auditeeList", auditeeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * try { DropDownModel[] auditee = restTemplate.getForObject(env.getMasterUrl()
		 * + "get-auditee-list-Name", DropDownModel[].class);
		 * 
		 * List<DropDownModel> auditeeList = Arrays.asList(auditee);
		 * model.addAttribute("auditeeList", auditeeList); } catch (RestClientException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		try {
			DropDownModel[] departmentHead = restTemplate.getForObject(env.getMasterUrl() + "get-dep-head-list-Name",
					DropDownModel[].class);

			List<DropDownModel> depHeadList = Arrays.asList(departmentHead);
			model.addAttribute("depHeadList", depHeadList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentAuditorModel = mapper.convertValue(res.getBody(), DepartmentAuditorModel.class);
		session.setAttribute("message", "");
		model.addAttribute("departmentAuditorModel", departmentAuditorModel);
		logger.info("Method : editDepAuditor ends");
		return "audit/addDepartmentAuditor";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/view-department-auditor-model")
	public @ResponseBody JsonResponse<Object> viewDepartmentAuditorModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewDepartmentAuditorModal starts");
		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String secId = new String(encodeByte1);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getMasterUrl() + "viewDepAuditorModelView?id=" + id + "&secId=" + secId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewDepartmentAuditorModal ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-department-auditor-delete" })
	public @ResponseBody JsonResponse<Object> deleteAuditor(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {

		logger.info("Method : deleteAuditor starts");
		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String secId = new String(encodeByte1);

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "deleteDeptAuditor?id=" + id + "&secId=" + secId,
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
		logger.info("Method : deleteSection ends");
		return resp;
	}
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "view-department-auditor-secname-ajax" })
	 * public @ResponseBody JsonResponse<Object> getSecName(Model
	 * model, @RequestBody String tAduitNewState, BindingResult result) {
	 * logger.info("Method : getSecNameAJAX starts"); JsonResponse<Object> res = new
	 * JsonResponse<Object>(); try { res =
	 * restTemplate.getForObject(env.getMasterUrl() + "SecName?id=" +
	 * tAduitNewState, JsonResponse.class); } catch (Exception e) {
	 * e.printStackTrace(); } if (res.getMessage() != null) {
	 * res.setCode(res.getMessage()); res.setMessage("Unsuccess"); } else {
	 * res.setMessage("success"); } System.out.println(res);
	 * logger.info("Method : getSecNameAJAX ends"); return res;
	 * 
	 * }
	 */
}
