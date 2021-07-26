/**
 * Defines User related method call for SacMaster
 */
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
import nirmalya.aathithya.webmodule.master.model.UserSacCodeModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "master")
public class UserSacCodeController {

	Logger logger = LoggerFactory.getLogger(UserSacCodeController.class);

	@Autowired
	RestTemplate restClient;
	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMApping for Adding new sacMaster
	 *
	 */
	@GetMapping("add-sac-code")
	public String addSacCode(Model model, HttpSession session) {

		logger.info("Method : addSacCode starts");

		UserSacCodeModel sacCodeModel = new UserSacCodeModel();
		UserSacCodeModel form = (UserSacCodeModel) session.getAttribute("sitems");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("sacCodeModel", form);
		} else {
			model.addAttribute("sacCodeModel", sacCodeModel);
		}
		/*
		 * dropDown value for service name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getMasterUrl() + "rest-get-serviceName",
					DropDownModel[].class);
			List<DropDownModel> serviceList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceList", serviceList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		logger.info("Method : addSacCode ends");

		return "master/addSacCode";
	}

	/*
	 * get Mapping for view SacMaster
	 * 
	 */

	@GetMapping("/view-sac-master")
	public String viewSacCode(Model model, HttpSession session) {

		logger.info("Method : viewSacCode starts");
		logger.info("Method : viewSacCode ends");

		return "master/listSacCode";
	}

	/*
	 * post Mapping for add SacMaster
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("add-sac-code")
	public String addSacCodePost(@ModelAttribute UserSacCodeModel sacCodeModel, Model model, HttpSession session) {

		logger.info("Method : addSacCodePost starts");

		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		try {
			jsonResponse = restClient.postForObject(env.getMasterUrl() + "addNewSacMaster", sacCodeModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("sacCodeModel", sacCodeModel);
			return "redirect:master/addSacCode";
		}

		logger.info("Method : addSacCodePost ends");
		return "redirect:view-sac-master";

	}

	/*
	 * get Mapping for view SacMaster Through Ajax
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("view-sac-master-throughAjax")
	public @ResponseBody DataTableResponse viewSacMasterThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1 /* , @RequestParam String param2 */) {

		logger.info("Method : viewSacMasterThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			// tableRequest.setParam2(param2);

			JsonResponse<List<UserSacCodeModel>> jsonResponse = new JsonResponse<List<UserSacCodeModel>>();

			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getAllSacMaster", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserSacCodeModel> sacCodeModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserSacCodeModel>>() {
					});

			String s = "";

			for (UserSacCodeModel m : sacCodeModel) {

				byte[] pId = Base64.getEncoder().encode(m.getSacId().getBytes());

				s = "";
				s = s + "<a href='edit-sac-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:25px\"></i></a>&nbsp;&nbsp;";
				s = s + "<a href='javascript:void(0)' onclick='DeleteSacCode(\"" + m.getSacId()
						+ "\")'><i class='fa fa-trash' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;";
				s = s + "<a data-toggle='modal' title='View' href='javascript:void' onclick='viewInModel(\""
						+ m.getSacId() + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";

				if (m.getSacActive()) {
					m.setStatus("Active");
				} else {
					m.setStatus("InActive");
				}

				/*
				 * s = s +
				 * "<a href='deleteFloor?id="+m.getFloorId()+"' >Delete</a>";
				 * m.setDelete(s); s = "";
				 */
			}
			// System.out.println("Total=="+jsonResponse.getTotal());
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(sacCodeModel);

		} catch (Exception e) {

			e.printStackTrace();
		}

		logger.info("Method : viewSacMasterThroughAjax ends");
		return response;
	}

	/*
	 * get Mapping for Delete SacMaster
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-sac-master" })
	public @ResponseBody JsonResponse<Object> deleteSacMaster(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteSacMaster Starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteSacMaster?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteSacMaster ends");

		// System.out.println(resp);
		return resp;

	}

	/*
	 * 
	 * 
	 * GetMApping for Edit SacMaster
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("edit-sac-master")

	public String editSacMaster(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editSacMaster starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));

		UserSacCodeModel sacCodeModel = new UserSacCodeModel();
		JsonResponse<UserSacCodeModel> jsonResponse = new JsonResponse<UserSacCodeModel>();

		try {
			jsonResponse = restClient.getForObject(env.getMasterUrl() + "getSacMaster?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		sacCodeModel = mapper.convertValue(jsonResponse.getBody(), UserSacCodeModel.class);
		// System.out.println("SubcategoryData : " + itemSubCategoryModel);
		session.setAttribute("message", "");
		model.addAttribute("sacCodeModel", sacCodeModel);
		/*
		 * dropDown value for service name
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getMasterUrl() + "rest-get-serviceName",
					DropDownModel[].class);
			List<DropDownModel> serviceList = Arrays.asList(dropDownModel);
			model.addAttribute("serviceList", serviceList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : editSacMaster ends");
		return "master/addSacCode";
	}

	/*
	 * Post Mapping for Modalview SacMaster
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-sac-master-modalview" })
	public @ResponseBody JsonResponse<Object> modalViewSacMaster(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : modalViewSacMaster starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getMasterUrl() + "getSacMaster?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			// System.out.println("if block getmsg() not false : " +
			// res.getMessage());
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		// System.out.println("response from rest :" + res);
		logger.info("Method : modalViewSacMaster ends");
		return res;
	}


}
