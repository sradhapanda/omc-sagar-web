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
import nirmalya.aathithya.webmodule.master.model.SubGroupMasterModal;


@Controller
@RequestMapping(value = "master")
public class SubGroupMasterController {

	Logger logger = LoggerFactory.getLogger(SubGroupMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	// * *View Default 'Add sub group ' page

	@GetMapping("/add-subgroup-master")
	public String addGroup(Model model, HttpSession session) {

		logger.info("Method : addGroupName starts");

		SubGroupMasterModal GroupType = new SubGroupMasterModal();
		try {
			SubGroupMasterModal GroupTypeSession = (SubGroupMasterModal) session.getAttribute("sGroupType");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (GroupTypeSession != null) {

				model.addAttribute("GroupType", GroupTypeSession);
				session.setAttribute("sGroupType", null);
			} else {

				model.addAttribute("GroupType", GroupType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}try {
			DropDownModel[] groupName = restClient.getForObject(env.getMasterUrl() + "get-group-list-Name",
					DropDownModel[].class);

			List<DropDownModel> grouplist = Arrays.asList(groupName);
			model.addAttribute("grouplist", grouplist);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addGroupName ends");
		return "master/addSubGroupMaster";
	}
	/**
	 * Web controller view product type mAster post mapping
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-subgroup-master")
	public String addGroup(@ModelAttribute SubGroupMasterModal GroupTypeMaster, Model model, HttpSession session) {

		logger.info("Method : addGroupName starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			GroupTypeMaster.setCreatedBy("u0010");
			
			resp = restClient.postForObject(env.getMasterUrl() + "restAddSubGroupType", GroupTypeMaster, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sGroupType", GroupTypeMaster);
			return "redirect:/master/add-sub-group-master";
		}

		logger.info("Method :addGroupName ends");
		return "redirect:/master/view-subgroup-master";
	}
	/**
	 * // * Default 'View sub Group Name ' page // * //
	 */
	@GetMapping("/view-subgroup-master")
	public String viewSubGroupType(Model model, HttpSession session) {

		logger.info("Method : viewSubGroupType starts");

		JsonResponse<Object> GroupType = new JsonResponse<Object>();
		model.addAttribute("GroupType", GroupType);

		logger.info("Method : viewSubGroupType ends");
		return "master/viewSubGroupMaster";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/view-subgroup-master-through-ajax")
	public @ResponseBody DataTableResponse viewSubGroupTypeviewThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewSubGroupTypeviewThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<SubGroupMasterModal>> jsonResponse = new JsonResponse<List<SubGroupMasterModal>>();

			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getSubGroupType", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<SubGroupMasterModal> GroupType = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<SubGroupMasterModal>>() {
					});

			String s = "";

			for (SubGroupMasterModal m : GroupType) {
				byte[] pId = Base64.getEncoder().encode(m.getSubGroupId().getBytes());
				/*if (m.getProductTypeStatus()) {
					m.setStatus("Active");
				} else {
					m.setStatus("Inactive");
				}*/
				s = s + "<a href='view-subgroup-master-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:24px\" aria-hidden=\"true\"></i></a>&nbsp;&nbsp; "
						+ " <a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(GroupType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewSubGroupTypeviewThroughAjax ends");
		return response;
	}
	/**
	 * Delete sub group type data from browser
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-subgroup-master-delete")
	public @ResponseBody JsonResponse<Object> deleteSubGroup(Model model, @RequestParam String id,
			HttpSession session) {

		logger.info("Method : deleteSubGroup starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		String createdBy = "u0001";

		try {
			resp = restClient.getForObject(
					env.getMasterUrl() + "deleteSubGroupTypeById?id=" + index + "&createdBy=" + createdBy,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {

			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteSubGroup ends");

		return resp;
	}
	/**
	 * edit sub group type data
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-subgroup-master-edit")
	public String editSubGroupType(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editSubGroupType starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		SubGroupMasterModal GroupType = new SubGroupMasterModal();
		JsonResponse<SubGroupMasterModal> jsonResponse = new JsonResponse<SubGroupMasterModal>();

		try {

			jsonResponse = restClient.getForObject(env.getMasterUrl() + "getSupGroupTypeById?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		GroupType = mapper.convertValue(jsonResponse.getBody(), SubGroupMasterModal.class);
		session.setAttribute("message", "");

		model.addAttribute("GroupType", GroupType);
		try {
			DropDownModel[] groupName = restClient.getForObject(env.getMasterUrl() + "get-group-list-Name",
					DropDownModel[].class);

			List<DropDownModel> grouplist = Arrays.asList(groupName);
			model.addAttribute("grouplist", grouplist);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editSubGroupType ends");
		return "master/addSubGroupMaster";
	}
	/*
	 * for All view model Master
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-subgroup-master-modal" })
	public @ResponseBody JsonResponse<Object> modalSubGroupType(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalSubGroupType starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getMasterUrl() + "viewSubGroupTypeById?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {

			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		System.out.println("in res" + res);
		logger.info("Method : modalSubGroupType ends");
		return res;
	}

}
