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
import nirmalya.aathithya.webmodule.employee.model.HrmJobTypeMasterModel;
/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmJobTypeMasterController {
	Logger logger = LoggerFactory.getLogger(HrmJobTypeMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add jobType view page
	 */
	@GetMapping("/add-job-type-master")
	public String addjobTypeMaster(Model model, HttpSession session) {

		logger.info("Method : addjobTypeMaster starts");

		HrmJobTypeMasterModel jobType = new HrmJobTypeMasterModel();
		HrmJobTypeMasterModel sessionjobType = (HrmJobTypeMasterModel) session.getAttribute("sessionjobType");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionjobType != null) {
			model.addAttribute("jobType", sessionjobType);
			session.setAttribute("sessionjobType", null);
		} else {
			model.addAttribute("jobType", jobType);
		}

		

		logger.info("Method : addjobTypeMaster ends");

		return "employee/add-job-type-master";
	}

	/*
	 * Post Mapping for adding new jobType
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-job-type-master")
	public String addjobTypeMasterPost(@ModelAttribute HrmJobTypeMasterModel jobType, Model model, HttpSession session) {

		logger.info("Method : addjobTypeMasterPost starts");
		
		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		jobType.setCreatedBy(userId);
		jobType.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddjobTypes", jobType, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionjobType", jobType);
			return "redirect:/employee/add-job-type-master";
		}
		logger.info("Method : addjobTypeMasterPost ends");

		return "redirect:/employee/view-job-type-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-job-type-master")
	public String viewjobTypeMaster(Model model, HttpSession session) {

		logger.info("Method : viewjobTypeMaster starts");


		logger.info("Method : viewjobTypeMaster ends");

		return "employee/view-job-type-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-type-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewjobTypeMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewjobTypeMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			

			JsonResponse<List<HrmJobTypeMasterModel>> jsonResponse = new JsonResponse<List<HrmJobTypeMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getjobTypeDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmJobTypeMasterModel> jobType = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmJobTypeMasterModel>>() {
					});

			String s = "";

			for (HrmJobTypeMasterModel m : jobType) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getJobTypeId().getBytes());
				s = s + "<a href='view-job-type-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletejobType(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getJobTypeStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(jobType);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewjobTypeMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit jobType
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-type-master-edit")
	public String editjobType(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editjobType starts");

		HrmJobTypeMasterModel jobType = new HrmJobTypeMasterModel();
		JsonResponse<HrmJobTypeMasterModel> jsonResponse = new JsonResponse<HrmJobTypeMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl()+ "getjobTypeById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		
		ObjectMapper mapper = new ObjectMapper();

		jobType = mapper.convertValue(jsonResponse.getBody(), HrmJobTypeMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("jobType", jobType);

		logger.info("Method : editjobType ends");

		return "employee/add-job-type-master";
	}

	/*
	 * For Delete jobType
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-job-type-master-delete")
	public @ResponseBody JsonResponse<Object> getjobTypeForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getjobTypeForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deletejobTypeById?id=" + id1+"&createdBy="+createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deletejobType  ends");
		
		return resp;
	}

	/*
	 * For Modal jobType View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-job-type-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : modalEmployement starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getjobTypeById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalEmployement ends");
		return res;
	}
	


}

