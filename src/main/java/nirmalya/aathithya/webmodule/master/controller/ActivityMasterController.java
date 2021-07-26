package nirmalya.aathithya.webmodule.master.controller;

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
import nirmalya.aathithya.webmodule.master.model.ActivityMasterModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class ActivityMasterController {

	Logger logger = LoggerFactory.getLogger(ActivityMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Activity Master' page
	 *
	 */
	@GetMapping(value = { "view-activity-master" })
	public String viewActivity(Model model) {
		logger.info("Method : viewActivity starts");

		logger.info("Method : viewActivity ends");
		return "master/viewActivityMaster";
	}

	/**
	 * call All module through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-activity-master-throughAjax")
	public @ResponseBody DataTableResponse viewAllActivityThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAllActivityThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<ActivityMasterModel>> jsonResponse = new JsonResponse<List<ActivityMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllActivity", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ActivityMasterModel> activityMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ActivityMasterModel>>() {
					});

			String s = "";
			for (ActivityMasterModel m : activityMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettActivity().getBytes());
				if (m.gettActvtyStatus()) {
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

				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(activityMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAllActivityThroughAjax ends");
		return response;
	}

	/**
	 * 
	 * View selected User Process in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-activity-master-model")
	public @ResponseBody JsonResponse<Object> viewActivityModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewActivityModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewActivityModal?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewActivityModal ends");
		return res;
	}

}
