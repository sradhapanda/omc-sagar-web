/*
 * Defines District Master  related method calls 
 */
package nirmalya.aathithya.webmodule.master.controller;

import java.util.ArrayList;
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
import nirmalya.aathithya.webmodule.master.model.UserDistrictModel;
import nirmalya.aathithya.webmodule.master.model.UserStateModel;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
@RequestMapping(value = "master/")
public class UserStateController {

	Logger logger = LoggerFactory.getLogger(UserStateController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * 
	 * 'Add State Master' Page
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/add-state-master")
	public String addState(Model model, HttpSession session) {

		logger.info("Method : addState starts");

		//

		UserStateModel tableSession = (UserStateModel) session.getAttribute("sstate");

		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> countrydata = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getMasterUrl() + "getCountryName?Action=" + "getCountryName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		countrydata = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("countrydata", countrydata);

		//

		UserStateModel state = new UserStateModel();
		try {

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (tableSession != null) {
				model.addAttribute("state", tableSession);
				session.setAttribute("sstate", null);
			} else {
				model.addAttribute("state", state);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : addState ends");

		return "master/AddFormState";
	}

	/*
	 * 
	 * 'View State Master' Page
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-state-master")
	public String viewState(Model model) {

		JsonResponse<Object> state = new JsonResponse<Object>();
		model.addAttribute("state", state);

		return "master/ListingFormState";
	}

	/*
	 * 
	 * PostMapping 'Add State Master' Page
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/add-state-master")
	public String addstate(@ModelAttribute UserStateModel state, Model model, HttpSession session) {

		logger.info("Method : addState starts");
		// System.out.println("posted state data" + state);

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			state.setCreatedBy("user001");
			resp = restClient.postForObject(env.getMasterUrl() + "/restAddState", state, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sstate", state);
			return "redirect:/master/add-state-master";
		}

		logger.info("Method : addstate ends");
		return "redirect:/master/view-state-master";
	}

	/*
	 * View all 'State Data' through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-state-master-through-ajax")
	public @ResponseBody DataTableResponse viewStateThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewStateThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<UserStateModel>> jsonResponse = new JsonResponse<List<UserStateModel>>();

			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getStateData", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<UserStateModel> state = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<UserStateModel>>() {
					});

			String s = "";

			for (UserStateModel m : state) {
				byte[] pId = Base64.getEncoder().encode(m.getStateId().getBytes());

				if (m.getStateStatus()) {
					m.setStateShowActive("Active");
				} else {
					m.setStateShowActive("Inactive");

				}

				s = s + "<a href='edit-state-master?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)'" + "' onclick='DeleteItem(\"" + m.getStateId()
						+ "\")' ><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp; "
						+ "<a data-toggle='modal' title='View'  " + "href='javascript:void' onclick='viewInModel(\""
						+ m.getStateId() + "\")'><i class='fa fa-search search'></i></a>";
				m.setAction(s);
				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(state);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewStateThroughAjax ends");
		return response;
	}

	/*
	 * 
	 * 'Delete State Master' Page by Id
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("delete-state-master")
	public @ResponseBody JsonResponse<Object> deleteState(@RequestBody String id, HttpSession session) {

		logger.info("Method : deleteTable starts");
		// System.out.println("response delete "+id);
		JsonResponse<Object> resp = new JsonResponse<Object>();

		try {
			resp = restClient.getForObject(env.getMasterUrl() + "deleteStateById?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("response delete in web controller "+resp);
		if (resp.getMessage() == null || resp.getMessage() == "") {
			resp.setMessage("Success");
		} else {
			resp.setMessage("Unsuccess");
		}

		logger.info("Method : deleteState ends");
		return resp;
	}

	/*
	 * 
	 * 'Edit State Master' Page
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/edit-state-master")
	public String editState(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editState starts");

		//

		UserStateModel tableSession = (UserStateModel) session.getAttribute("sstate");

		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> countrydata = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getMasterUrl() + "getCountryName?Action=" + "getCountryName",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		countrydata = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});
		model.addAttribute("countrydata", countrydata);

		//

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));
		/* System.out.println(encodedIndex); */

		UserStateModel state = new UserStateModel();
		JsonResponse<UserStateModel> jsonResponse = new JsonResponse<UserStateModel>();

		try {

			jsonResponse = restClient.getForObject(
					env.getMasterUrl() + "getStateById?id=" + id + "&Action=viewEditState", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper1 = new ObjectMapper();

		state = mapper1.convertValue(jsonResponse.getBody(), UserStateModel.class);
		session.setAttribute("message", "");

		// System.out.println(state);
		model.addAttribute("state", state);

		logger.info("Method : editState ends");
		return "master/AddFormState";
	}

	/*
	 * 
	 * Modal View of 'State Master'
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-state-master-model" })
	public @ResponseBody JsonResponse<Object> modalState(Model model, @RequestBody String index, BindingResult result) {

		logger.info("Method : modalState starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getMasterUrl() + "getStateById?id=" + index + "&Action=" + "ModelView",
					JsonResponse.class);
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
		logger.info("Method : modalState ends");
		return res;
	}

}
