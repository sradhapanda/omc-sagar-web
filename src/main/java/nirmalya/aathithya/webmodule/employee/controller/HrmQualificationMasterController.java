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
import nirmalya.aathithya.webmodule.employee.model.HrmsQualificationMasterModel;
/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmQualificationMasterController {

	Logger logger = LoggerFactory.getLogger(HrmQualificationMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add qualification view page
	 */
	@GetMapping("/add-qualification-master")
	public String addqualificationMaster(Model model, HttpSession session) {

		logger.info("Method : addqualificationMaster starts");

		HrmsQualificationMasterModel qualification = new HrmsQualificationMasterModel();
		HrmsQualificationMasterModel sessionqualification = (HrmsQualificationMasterModel) session.getAttribute("sessionqualification");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionqualification != null) {
			model.addAttribute("qualification", sessionqualification);
			session.setAttribute("sessionqualification", null);
		} else {
			model.addAttribute("qualification", qualification);
		}

		

		logger.info("Method : addqualificationMaster ends");

		return "employee/add-qualification-master";
	}

	/*
	 * Post Mapping for adding new qualification
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-qualification-master")
	public String addqualificationMasterPost(@ModelAttribute HrmsQualificationMasterModel qualification, Model model, HttpSession session) {

		logger.info("Method : addqualificationMasterPost starts");
		
		String userId ="";
		String companyId = "";
		try {
		userId = (String)session.getAttribute("USER_ID");
		companyId = (String) session.getAttribute("COMPANY_ID");
		}catch(Exception e) {
		e.printStackTrace();
		}
		qualification.setCreatedBy(userId);
		qualification.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddqualifications", qualification, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionqualification", qualification);
			return "redirect:/employee/add-qualification-master";
		}
		logger.info("Method : addqualificationMasterPost ends");

		return "redirect:/employee/view-qualification-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-qualification-master")
	public String viewqualificationMaster(Model model, HttpSession session) {

		logger.info("Method : viewqualificationMaster starts");


		logger.info("Method : viewqualificationMaster ends");

		return "employee/view-qualification-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-qualification-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewqualificationMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewqualificationMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			

			JsonResponse<List<HrmsQualificationMasterModel>> jsonResponse = new JsonResponse<List<HrmsQualificationMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getqualificationDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsQualificationMasterModel> qualification = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsQualificationMasterModel>>() {
					});

			String s = "";

			for (HrmsQualificationMasterModel m : qualification) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getQualificationId().getBytes());
				s = s + "<a href='view-qualification-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletequalification(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getQualificationStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(qualification);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewqualificationMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit qualification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-qualification-master-edit")
	public String editqualification(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editqualification starts");

		HrmsQualificationMasterModel qualification = new HrmsQualificationMasterModel();
		JsonResponse<HrmsQualificationMasterModel> jsonResponse = new JsonResponse<HrmsQualificationMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl()+ "getqualificationById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		
		ObjectMapper mapper = new ObjectMapper();

		qualification = mapper.convertValue(jsonResponse.getBody(), HrmsQualificationMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("qualification", qualification);

		logger.info("Method : editqualification ends");

		return "employee/add-qualification-master";
	}

	/*
	 * For Delete qualification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-qualification-master-delete")
	public @ResponseBody JsonResponse<Object> getqualificationForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getqualificationForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deletequalificationById?id=" + id1+"&createdBy="+createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deletequalification  ends");
		
		return resp;
	}

	/*
	 * For Modal qualification View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-qualification-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : modalqualification starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getqualificationById?id=" + id, JsonResponse.class);
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
		logger.info("Method :modalqualification ends");
		return res;
	}
	


}


