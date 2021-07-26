package nirmalya.aathithya.webmodule.master.controller;

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

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

import nirmalya.aathithya.webmodule.master.model.ServiceMasterModel;


/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class ServiceMasterController {
	Logger logger = LoggerFactory.getLogger(ServiceMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Add Service Master' page
	 *
	 */
	@GetMapping(value = { "add-service-master" })
	public String addServiceMaster(Model model, HttpSession session) {
		logger.info("Method : addServiceMaster starts");
		ServiceMasterModel serviceMaster = new ServiceMasterModel();

		ServiceMasterModel serviceMasterSession = (ServiceMasterModel) session.getAttribute("serviceMaster");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (serviceMasterSession != null) {
			model.addAttribute("serviceMaster", serviceMasterSession);
			session.setAttribute("serviceMaster", null);

		} else {
			model.addAttribute("serviceMaster", serviceMaster);
		}

		logger.info("Method : addServiceMaster ends");
		return "master/addServiceMaster";
	}

	/**
	 * Add Service Master Form Post
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-service-master" })
	public String addNewServiceMaster(@ModelAttribute ServiceMasterModel serviceMaster, Model model,
			HttpSession session) {
		logger.info("Method : addNewServiceMaster starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();

		try {
			serviceMaster.settCreatedBy("u0001");
			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "restAddNewService", serviceMaster,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("serviceMaster", serviceMaster);
			return "redirect:add-service-master";
		}
		logger.info("Method : addNewServiceMaster ends");
		return "redirect:view-service-master";
	}
	
	/**
	 * View Default 'Service Master' page
	 *
	 */
	@GetMapping(value = { "view-service-master" })
	public String viewService(Model model) {
		logger.info("Method : viewService starts");

		/*try {
			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "getServiceNameList",
					DropDownModel[].class);
			List<DropDownModel> serviceList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceList", serviceList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}*/

		logger.info("Method : viewService ends");
		return "master/viewServiceMaster";
	}

	/**
	 * call All Service through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-service-master-throughAjax")
	public @ResponseBody DataTableResponse viewAllServiceThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAllServiceThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
		

			JsonResponse<List<ServiceMasterModel>> jsonResponse = new JsonResponse<List<ServiceMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllService", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ServiceMasterModel> serviceMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ServiceMasterModel>>() {
					});

			String s = "";
			for (ServiceMasterModel m : serviceMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettServiceId().getBytes());
				if (m.gettServiceStatus()) {
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
				s = s + "<a  title='Edit' href='view-service-master-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit'></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(serviceMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAllServiceThroughAjax ends");
		
		return response;
	}
	
	/*
	 * View selected HouseKeeping Task in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-service-master-model")
	public @ResponseBody JsonResponse<Object> viewServiceModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewServiceModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewServiceModal?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewServiceModal ends");
		return res;
	}
	/*
	 * Edit Selected Service
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-service-master-edit")
	public String editService(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editService starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);

		JsonResponse<ServiceMasterModel> res = new JsonResponse<ServiceMasterModel>();
		ServiceMasterModel serviceMaster = new ServiceMasterModel();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewServiceModal?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		serviceMaster = mapper.convertValue(res.getBody(), ServiceMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("serviceMaster", serviceMaster);
		logger.info("Method : editService ends");
		return "master/addServiceMaster";
	}
	/*
	 * Delete selected service in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-service-master-delete" })
	public @ResponseBody JsonResponse<Object> deleteService(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {

		logger.info("Method : deleteService starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		String createdBy = "u0002";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restTemplate.getForObject(
					env.getMasterUrl() + "deleteService?id=" + id + "&createdBy=" + createdBy,
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
		logger.info("Method : deleteService ends");
		return resp;
	}
}
