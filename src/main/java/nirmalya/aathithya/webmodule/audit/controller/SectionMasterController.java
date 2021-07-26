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
import nirmalya.aathithya.webmodule.audit.model.SectionMasterModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class SectionMasterController {
	Logger logger = LoggerFactory.getLogger(SectionMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@GetMapping(value = { "add-section-master" })
	public String addSection(Model model, HttpSession session) {
		logger.info("Method : addSection starts");
		SectionMasterModel sectionMasterModel = new SectionMasterModel();

		SectionMasterModel ssectionMasterModel = (SectionMasterModel) session.getAttribute("sectionMasterModel");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (ssectionMasterModel != null) {
			model.addAttribute("sectionMasterModel", ssectionMasterModel);
			session.setAttribute("sectionMasterModel", null);

		} else {
			model.addAttribute("sectionMasterModel", sectionMasterModel);
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
		logger.info("Method : addSection ends");
		return "audit/addSectionMaster";
	}

	/**
	 * Add Section Master Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-section-master" })
	public String addNewSectionMaster(@ModelAttribute SectionMasterModel sectionMasterModel, Model model,
			HttpSession session) {
		logger.info("Method : addNewSectionMaster starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			sectionMasterModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "addSection", sectionMasterModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("sectionMasterModel", sectionMasterModel);
			return "redirect:add-section-master";
		}
		logger.info("Method : addNewSectionMaster ends");
		return "redirect:view-section-master";
	}

	@GetMapping(value = { "view-section-master" })
	public String viewSection(Model model) {
		logger.info("Method : viewSection starts");
		logger.info("Method : viewSection ends");
		return "audit/viewSectionMaster";
	}

	/**
	 * call All Service through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-section-master-throughAjax")
	public @ResponseBody DataTableResponse viewSectionThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewSectionThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<SectionMasterModel>> jsonResponse = new JsonResponse<List<SectionMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllSection", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SectionMasterModel> sectionMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SectionMasterModel>>() {
					});

			String s = "";
			for (SectionMasterModel m : sectionMaster) {
				byte[] pId = Base64.getEncoder().encode(m.getSection().getBytes());
				if (m.getSectionStatus()) {
					s = "";
					s = s + "Active";
				} else {
					s = "";
					s = s + "Inactive";
				}
				m.setStatus(s);
				;

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
				+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";
				s = s + "<a title='Edit' href='view-section-master-edit?id=" + new String(pId)
				+ "' '><i class='fa fa-edit edit' style=\"font-size:20px\"></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
				+ "\")'><i class='fa fa-trash' style=\"font-size:20px\"></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(sectionMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewSectionThroughAjax ends");

		return response;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/view-section-master-model")
	public @ResponseBody JsonResponse<Object> viewSectionModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewSectionModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewSectionModelView?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewSectionModal ends");
		return res;
	}

	/*
	 * Edit Selected Service
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-section-master-edit")
	public String editSection(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editSection starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<SectionMasterModel> res = new JsonResponse<SectionMasterModel>();
		SectionMasterModel sectionMasterModel = new SectionMasterModel();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewSectionModal?id=" + id, JsonResponse.class);
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
		sectionMasterModel = mapper.convertValue(res.getBody(), SectionMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("sectionMasterModel", sectionMasterModel);
		logger.info("Method : editSection ends");
		return "audit/addSectionMaster";
	}
	/*
	 * Delete selected service in Modal
	 *
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-section-master-delete" })
	public @ResponseBody JsonResponse<Object> deleteSection(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {

		logger.info("Method : deleteSection starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(env.getMasterUrl() + "deleteSection?id=" + id + "&createdBy=" + createdBy,
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
}
