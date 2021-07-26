package nirmalya.aathithya.webmodule.user.controller;

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
import nirmalya.aathithya.webmodule.user.model.ProcessMasterModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "user")
public class ProcessMasterController {

	Logger logger = LoggerFactory.getLogger(ProcessMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * View Default 'Process Master' page
	 *
	 */
	@GetMapping(value = { "add-process-master" })
	public String addProcessMaster(Model model, HttpSession session) {
		logger.info("Method : addProcess starts");
		ProcessMasterModel processMaster = new ProcessMasterModel();

		ProcessMasterModel form = (ProcessMasterModel) session.getAttribute("suser");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("processMaster", form);
			session.setAttribute("processMaster", null);
		} else {
			model.addAttribute("processMaster", processMaster);
		}
		logger.info("Method : addProcess ends");
		return "user/addProcessMaster";
	}

	/**
	 * Process Master Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-process-master" })
	public String addNewHouseTask(@ModelAttribute ProcessMasterModel processMaster, Model model, HttpSession session) {
		logger.info("Method : addNewProcess starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			processMaster.settProcessCreatedBy("u0001");
			jsonResponse = restTemplate.postForObject(env.getUserUrl() + "restAddNewProcess", processMaster,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("processMaster", processMaster);
			return "user/addProcessMaster";
		}
		logger.info("Method : addNewProcess ends");
		return "redirect:view-process-master";
	}

	/**
	 * View Default 'view Process Master' page
	 *
	 */
	@GetMapping(value = { "view-process-master" })
	public String viewProcess(Model mode) {
		logger.info("Method : viewProcess starts");
		try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getUserUrl() + "restGetProcess",
					DropDownModel[].class);
			List<DropDownModel> processList = Arrays.asList(dropDownModel);
			mode.addAttribute("processList", processList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewProcess ends");
		return "user/viewProcessMaster";
	}

	/**
	 * call All Process through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-process-master-throughAjax")
	public @ResponseBody DataTableResponse viewAllProcessThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAllProcessThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<ProcessMasterModel>> jsonResponse = new JsonResponse<List<ProcessMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getUserUrl() + "getAllProcess", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ProcessMasterModel> processMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ProcessMasterModel>>() {
					});

			String s = "";
			for (ProcessMasterModel m : processMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettProcess().getBytes());
				if (m.gettProcessStatus()) {
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
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "<a  title='Edit' href='edit-process?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit'></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(processMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAllProcessThroughAjax ends");
		return response;
	}

	/*
	 * View selected User Process in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-process-master-model")
	public @ResponseBody JsonResponse<Object> viewProcessModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewProcessModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getUserUrl() + "viewThisProcess?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewProcessModal ends");
		return res;
	}

	/*
	 * Delete selected Process in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-Process" })
	public @ResponseBody JsonResponse<Object> deleteProcess(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {

		logger.info("Method : deleteProcess starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		String createdBy = "u0002";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(env.getUserUrl() + "deleteProcess?id=" + id + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteProcess ends");
		return resp;
	}

	/*
	 * Edit Selected Process
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-process")
	public String editProcess(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editProcess starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<ProcessMasterModel> res = new JsonResponse<ProcessMasterModel>();
		ProcessMasterModel processMaster = new ProcessMasterModel();

		try {
			res = restTemplate.getForObject(env.getUserUrl() + "viewThisProcess?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		processMaster = mapper.convertValue(res.getBody(), ProcessMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("processMaster", processMaster);
		logger.info("Method : editProcess ends");
		return "user/addProcessMaster";
	}

}
