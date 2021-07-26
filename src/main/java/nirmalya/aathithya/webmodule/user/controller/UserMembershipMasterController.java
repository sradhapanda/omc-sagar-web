package nirmalya.aathithya.webmodule.user.controller;

import java.util.ArrayList;
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

import nirmalya.aathithya.webmodule.user.model.UserMembershipMstrModel;

@Controller
@RequestMapping(value = "user/")
public class UserMembershipMasterController {

	Logger logger = LoggerFactory.getLogger(UserMembershipMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("/add-membership-type-master")
	public String addMembershipMaster(Model model, HttpSession session) {

		logger.info("Method : addMembershipMaster starts");
		UserMembershipMstrModel tableSession = (UserMembershipMstrModel) session.getAttribute("smembershipmstr");

		UserMembershipMstrModel membershipmstr = new UserMembershipMstrModel();
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (tableSession != null) {
				model.addAttribute("membershipmstr", tableSession);
				session.setAttribute("smembershipmstr", null);
			} else {
				model.addAttribute("membershipmstr", membershipmstr);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Validity Dropdown Data */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> validityData = new ArrayList<DropDownModel>();

		try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getValidityName?Action=" + "getValidityName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		validityData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		List<String> propertyCategory = new ArrayList<String>();
		for (DropDownModel f : validityData) {
			propertyCategory.add(f.getName());
		}
		model.addAttribute("validityData", validityData);

		logger.info("Method : addMembershipMaster ends");

		return "user/AddMembershipMaster";
	}

	/*
	 * 
	 * PostMapping 'Add State Master' Page
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-membership-type-master")
	public String addMembershipMstr(@ModelAttribute UserMembershipMstrModel membershipmstr, Model model,
			HttpSession session) {

		logger.info("Method : addMembershipMstr starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			membershipmstr.settCMemberTypCreatedBy("u0001");
			resp = restClient.postForObject(env.getUserUrl() + "/restAddMemberMstr", membershipmstr,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("smembershipmstr", membershipmstr);
			return "redirect:/user/add-membership-type-master";
		}

		logger.info("Method : addMembershipMstr ends");
		return "redirect:/user/view-membership-type-master";
	}

	/*
	 *
	 * View Membership Master' page
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-membership-type-master")
	public String viewMembershipMstr(Model model) {

	/*	JsonResponse<Object> membershipmstr = new JsonResponse<Object>();
		model.addAttribute("membershipmstr", membershipmstr);
*/
		
		
		logger.info("Method : viewMembershipMstr starts");
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getUserUrl() + "restGetMemberType",
					DropDownModel[].class);
			List<DropDownModel> memberList = Arrays.asList(dropDownModel);
			model.addAttribute("memberList", memberList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewMembershipMstr ends");
		return "user/ListingMembershipMstr";
	}

	/*
	 *
	 * View all data through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-membership-type-master-through-ajax")
	public @ResponseBody DataTableResponse viewMembershipMstrThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method :WEB viewMembershipMstrThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<UserMembershipMstrModel>> jsonResponse = new JsonResponse<List<UserMembershipMstrModel>>();

			jsonResponse = restClient.postForObject(env.getUserUrl() + "getMembershipMstrData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserMembershipMstrModel> membershipmstr = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserMembershipMstrModel>>() {
					});

			String s = "";

			for (UserMembershipMstrModel m : membershipmstr) {
				byte[] pId = Base64.getEncoder().encode(m.getMemId().getBytes());

				if (m.getMemStatus()) {
					m.setMembershipShowActive("Active");
				} else {
					m.setMembershipShowActive("Inactive");

				}

				s = s + "<a href='edit-membership-type-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + m.getMemId()
						+ "\")' ><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getMemId() + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(membershipmstr);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method :WEB viewMembershipMstrThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * Modal View of Membership Master
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-membership-type-master-details" })
	public @ResponseBody JsonResponse<Object> modalMembershipMstr(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : modalMembershipMstr starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restClient.getForObject(env.getUserUrl() + "getMemberMstrById?id=" + index + "&Action=" + "ModelView",
					JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : modalMembershipMstr ends");
		return res;
	}

	/*
	 * 'Edit Membership Master' By Id
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-membership-type-master")
	public String editMembershipMstr(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editMembershipMstr starts");

		
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		UserMembershipMstrModel membershipmstr = new UserMembershipMstrModel();
		JsonResponse<UserMembershipMstrModel> jsonResponse = new JsonResponse<UserMembershipMstrModel>();

		try {
			jsonResponse = restClient.getForObject(
					env.getUserUrl() + "getMemberMstrById?id=" + id + "&Action=viwEdtMmbrMstr", JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();

		membershipmstr = mapper.convertValue(jsonResponse.getBody(), UserMembershipMstrModel.class);
		session.setAttribute("message", "");

		model.addAttribute("membershipmstr", membershipmstr);


		/* Validity Dropdown Data */
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> validityData = new ArrayList<DropDownModel>();

		try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getValidityName?Action=" + "getValidityName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		

		validityData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		List<String> propertyCategory = new ArrayList<String>();
		for (DropDownModel f : validityData) {
			propertyCategory.add(f.getName());
		}
		model.addAttribute("validityData", validityData);

		logger.info("Method : editMembershipMstr ends");
		return "user/AddMembershipMaster";
	}

	/*
	 * 
	 * 'Delete Membership Master' Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("delete-membership-type-master")
	public @ResponseBody JsonResponse<Object> deleteMembershipMstr(@RequestBody String id, HttpSession session) {

		logger.info("Method : deleteMembershipMstr starts");
		String createdBy = "u0002";
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getUserUrl() + "deleteMmbrspById?id=" + id + "&createdBy=" + createdBy,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : deleteMembershipMstr ends");
		return resp;
	}
}
