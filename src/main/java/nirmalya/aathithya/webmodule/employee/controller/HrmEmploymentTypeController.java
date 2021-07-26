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
import nirmalya.aathithya.webmodule.employee.model.HrmEmploymentTypeModel;
/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmEmploymentTypeController {
	Logger logger = LoggerFactory.getLogger(HrmEmploymentTypeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Employment view page
	 */
	@GetMapping("/add-employment-master")
	public String addEmploymentMaster(Model model, HttpSession session) {

		logger.info("Method : addEmploymentMaster starts");

		HrmEmploymentTypeModel employment = new HrmEmploymentTypeModel();
		HrmEmploymentTypeModel sessionemployment = (HrmEmploymentTypeModel) session.getAttribute("sessionemployment");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionemployment != null) {
			model.addAttribute("employment", sessionemployment);
			session.setAttribute("sessionemployment", null);
		} else {
			model.addAttribute("employment", employment);
		}

		

		logger.info("Method : addEmploymentMaster ends");

		return "employee/add-employement-master";
	}

	/*
	 * Post Mapping for adding new Employment
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-employment-master")
	public String addEmploymentMasterPost(@ModelAttribute HrmEmploymentTypeModel employment, Model model, HttpSession session) {

		logger.info("Method : addEmploymentMasterPost starts");
		
		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		employment.setCompanyId(companyId);
		employment.setCreatedBy(userId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddemployments", employment, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionemployment", employment);
			return "redirect:/employee/add-employment-master";
		}
		logger.info("Method : addEmploymentMasterPost ends");

		return "redirect:/employee/view-employment-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-employment-master")
	public String viewemploymentMaster(Model model, HttpSession session) {

		logger.info("Method : viewemploymentMaster starts");


		logger.info("Method : viewemploymentMaster ends");

		return "employee/view-employment-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employment-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewemploymentMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewemploymentMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			

			JsonResponse<List<HrmEmploymentTypeModel>> jsonResponse = new JsonResponse<List<HrmEmploymentTypeModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getemploymentDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmEmploymentTypeModel> employment = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmEmploymentTypeModel>>() {
					});

			String s = "";

			for (HrmEmploymentTypeModel m : employment) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getEmploymentId().getBytes());
				s = s + "<a href='view-employment-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteEmployment(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getEmploymentStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(employment);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewemploymentMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit Employment
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employment-master-edit")
	public String editemployment(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editemployment starts");

		HrmEmploymentTypeModel employment = new HrmEmploymentTypeModel();
		JsonResponse<HrmEmploymentTypeModel> jsonResponse = new JsonResponse<HrmEmploymentTypeModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl()+ "getEmploymentById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		
		ObjectMapper mapper = new ObjectMapper();

		employment = mapper.convertValue(jsonResponse.getBody(), HrmEmploymentTypeModel.class);
		session.setAttribute("message", "");

		model.addAttribute("employment", employment);

		logger.info("Method : editemployment ends");

		return "employee/add-employement-master";
	}

	/*
	 * For Delete Employment
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employment-master-delete")
	public @ResponseBody JsonResponse<Object> getemploymentForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getemploymentForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deleteEmploymentById?id=" + id1+"&createdBy="+createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteemployment  ends");
		
		return resp;
	}

	/*
	 * For Modal Employment View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employment-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : modalEmployement starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmploymentById?id=" + id, JsonResponse.class);
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
