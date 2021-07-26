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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.RegionMasterModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;


@Controller
@RequestMapping(value = "audit")

public class RegionMasterController {

	Logger logger = LoggerFactory.getLogger(RegionMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/add-region")
	public String defaultDepartmentDetails(Model model, HttpSession session) {

		logger.info("Method : defaultRegion starts");
		/**
		 * DEPARTMENT NAME DROP DOWN
		 *
		 */
		try {
			DropDownModel[] departmentName = restClient.getForObject(env.getAuditUrl() + "dropDownDepartmentDiv",
					DropDownModel[].class);
			List<DropDownModel> departmentNameList = Arrays.asList(departmentName);

			model.addAttribute("departmentNameList", departmentNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		RegionMasterModel region = new RegionMasterModel();
		try {
			RegionMasterModel regionSession = (RegionMasterModel) session.getAttribute("sregion");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (regionSession != null) {
				model.addAttribute("region", regionSession);
				session.setAttribute("sregion", null);
			} else {
				model.addAttribute("region", region);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : defaultRegion ends");
		return "audit/add-region";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-region")
	public String addProgram(@ModelAttribute RegionMasterModel region, Model model, HttpSession session) {

		logger.info("Method : addRegion starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			region.setCreatedBy(userId);
			resp = restClient.postForObject(env.getAuditUrl() + "restAddRegion", region, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sregion", region);
			return "redirect:/audit/add-region";
		}

		logger.info("Method : addAudit ends");
		return "redirect:/audit/view-region";
	}

	/**
	 * Default 'View Region Master' page
	 *
	 */
	@GetMapping("/view-region")
	public String viewProgram(Model model, HttpSession session) {

		logger.info("Method : viewRegion starts");

		logger.info("Method : viewRegion ends");
		return "audit/view-region";
	}

	/**
	 * Web Controller - View Region Details Through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-through-ajax")
	public @ResponseBody DataTableResponse viewRegionThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewRegionThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<RegionMasterModel>> jsonResponse = new JsonResponse<List<RegionMasterModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getRegion", tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<RegionMasterModel> region = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<RegionMasterModel>>() {
					});

			String s = "";
			for (RegionMasterModel m : region) {
				byte[] pId = Base64.getEncoder().encode(m.getRegionId().getBytes());
				//System.out.println(m.getStatus());

				if (m.getStatus()) {
					m.setStatusName("Active");
				} else {
					m.setStatusName("Inactive");
				}

				s = "<a href='view-region-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)' onclick='deleteRegion(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\" style=\"font-size:20px\" aria-hidden=\"true\"></i></a>";
				m.setAction(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(region);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewRegionThroughAjax ends");
		//System.out.println(response);
		return response;
	}

	/**
	 * Web Controller - Delete Region
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-delete")
	public @ResponseBody JsonResponse<Object> deleteRegion(Model model, @RequestParam String id, HttpSession session) {

		logger.info("Method : deleteRegion starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		//System.out.println(index);
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resp = restClient.getForObject(env.getAuditUrl() + "deleteRegionById?id=" + index + "&createdBy=" + userId,
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
		logger.info("Method : deleteRegion ends");

		return resp;
	}

	/**
	 * Web Controller - Edit Region
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-edit")
	public String editRegion(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editRegion starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		try {
			DropDownModel[] departmentName = restClient.getForObject(env.getAuditUrl() + "dropDownDepartmentDiv",
					DropDownModel[].class);
			List<DropDownModel> departmentNameList = Arrays.asList(departmentName);

			model.addAttribute("departmentNameList", departmentNameList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		RegionMasterModel region = new RegionMasterModel();
		JsonResponse<RegionMasterModel> jsonResponse = new JsonResponse<RegionMasterModel>();

		try {

			jsonResponse = restClient.getForObject(env.getAuditUrl() + "getRegionById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		region = mapper.convertValue(jsonResponse.getBody(), RegionMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("region", region);

		logger.info("Method : editRegion ends");

		return "audit/add-region";
	}

}
