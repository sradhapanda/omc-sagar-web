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
import nirmalya.aathithya.webmodule.employee.model.HrmsCertificationMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsCertificationMasterController {

	Logger logger = LoggerFactory.getLogger(HrmsCertificationMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add certification view page
	 */
	@GetMapping("/add-certification-master")
	public String addcertificationMaster(Model model, HttpSession session) {

		logger.info("Method : addcertificationMaster starts");

		HrmsCertificationMasterModel certification = new HrmsCertificationMasterModel();
		HrmsCertificationMasterModel sessioncertification = (HrmsCertificationMasterModel) session.getAttribute("sessioncertification");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessioncertification != null) {
			model.addAttribute("certification", sessioncertification);
			session.setAttribute("sessioncertification", null);
		} else {
			model.addAttribute("certification", certification);
		}

		logger.info("Method : addcertificationMaster ends");

		return "employee/add-certification-master";
	}

	/*
	 * Post Mapping for adding new certification
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-certification-master")
	public String addcertificationMasterPost(@ModelAttribute HrmsCertificationMasterModel certification, Model model, HttpSession session) {

		logger.info("Method : addcertificationMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		certification.setCreatedBy(userId);
		certification.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddcertifications", certification, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessioncertification", certification);
			return "redirect:/employee/add-certification-master";
		}
		logger.info("Method : addcertificationMasterPost ends");

		return "redirect:/employee/view-certification-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-certification-master")
	public String viewcertificationMaster(Model model, HttpSession session) {

		logger.info("Method : viewcertificationMaster starts");

		logger.info("Method : viewcertificationMaster ends");

		return "employee/view-certification-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-certification-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewcertificationMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewcertificationMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsCertificationMasterModel>> jsonResponse = new JsonResponse<List<HrmsCertificationMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getcertificationDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsCertificationMasterModel> certification = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsCertificationMasterModel>>() {
					});

			String s = "";

			for (HrmsCertificationMasterModel m : certification) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getCertificationId().getBytes());
				s = s + "<a href='view-certification-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletecertification(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getCertificationStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(certification);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewcertificationMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit certification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-certification-master-edit")
	public String editcertification(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editcertification starts");

		HrmsCertificationMasterModel certification = new HrmsCertificationMasterModel();
		JsonResponse<HrmsCertificationMasterModel> jsonResponse = new JsonResponse<HrmsCertificationMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getcertificationById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		certification = mapper.convertValue(jsonResponse.getBody(), HrmsCertificationMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("certification", certification);

		logger.info("Method : editcertification ends");

		return "employee/add-certification-master";
	}

	/*
	 * For Delete certification
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-certification-master-delete")
	public @ResponseBody JsonResponse<Object> getcertificationForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getcertificationForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deletecertificationById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletecertification  ends");

		return resp;
	}

	/*
	 * For Modal certification View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-certification-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalcertification starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getcertificationById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modalcertification ends");
		return res;
	}

}
