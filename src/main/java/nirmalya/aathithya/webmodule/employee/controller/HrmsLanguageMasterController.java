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

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.HrmsLanguageMasterModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsLanguageMasterController {


	Logger logger = LoggerFactory.getLogger(HrmsLanguageMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add language view page
	 */
	@GetMapping("/add-language-master")
	public String addlanguageMaster(Model model, HttpSession session) {

		logger.info("Method : addlanguageMaster starts");

		HrmsLanguageMasterModel language = new HrmsLanguageMasterModel();
		HrmsLanguageMasterModel sessionlanguage = (HrmsLanguageMasterModel) session.getAttribute("sessionlanguage");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (sessionlanguage != null) {
			model.addAttribute("language", sessionlanguage);
			session.setAttribute("sessionlanguage", null);
		} else {
			model.addAttribute("language", language);
		}

		logger.info("Method : addlanguageMaster ends");

		return "employee/add-language-master";
	}

	/*
	 * Post Mapping for adding new language
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-language-master")
	public String addlanguageMasterPost(@ModelAttribute HrmsLanguageMasterModel language, Model model, HttpSession session) {

		logger.info("Method : addlanguageMasterPost starts");

		String userId = "";
		String companyId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		language.setCreatedBy(userId);
		language.setCompanyId(companyId);
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {

			resp = restClient.postForObject(env.getEmployeeUrl() + "restAddlanguages", language, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sessionlanguage", language);
			return "redirect:/employee/add-language-master";
		}
		logger.info("Method : addlanguageMasterPost ends");

		return "redirect:/employee/view-language-master";
	}

	/*
	 * Get Mapping view work type master
	 */
	@GetMapping("/view-language-master")
	public String viewlanguageMaster(Model model, HttpSession session) {

		logger.info("Method : viewlanguageMaster starts");

		logger.info("Method : viewlanguageMaster ends");

		return "employee/view-language-master";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-language-master-ThroughAjax")
	public @ResponseBody DataTableResponse viewlanguageMasterjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewlanguageMasterjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<HrmsLanguageMasterModel>> jsonResponse = new JsonResponse<List<HrmsLanguageMasterModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getlanguageDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsLanguageMasterModel> language = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsLanguageMasterModel>>() {
					});

			String s = "";

			for (HrmsLanguageMasterModel m : language) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getLanguageId().getBytes());
				s = s + "<a href='view-language-master-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" ></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deletelanguage(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" ></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
				if (m.getLanguageStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(language);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewlanguageMasterjax Theme ends");

		return response;
	}

	/*
	 * for Edit language
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-language-master-edit")
	public String editlanguage(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editlanguage starts");

		HrmsLanguageMasterModel language = new HrmsLanguageMasterModel();
		JsonResponse<HrmsLanguageMasterModel> jsonResponse = new JsonResponse<HrmsLanguageMasterModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getlanguageById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		language = mapper.convertValue(jsonResponse.getBody(), HrmsLanguageMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("language", language);

		logger.info("Method : editlanguage ends");

		return "employee/add-language-master";
	}

	/*
	 * For Delete language
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-language-master-delete")
	public @ResponseBody JsonResponse<Object> getlanguageForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getlanguageForDelete ends");

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
			resp = restClient.getForObject(env.getEmployeeUrl() + "deletelanguageById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : deletelanguage  ends");

		return resp;
	}

	/*
	 * For Modal language View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-language-master-modalView" })
	public @ResponseBody JsonResponse<Object> modalEmployement(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modallanguage starts");

		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());

		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getlanguageById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method :modallanguage ends");
		return res;
	}

}
