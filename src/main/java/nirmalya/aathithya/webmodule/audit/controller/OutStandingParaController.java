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

import nirmalya.aathithya.webmodule.audit.model.OutStandingParaModel;
import nirmalya.aathithya.webmodule.audit.model.RegionMasterModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;


@Controller
@RequestMapping(value = "audit")

public class OutStandingParaController {

	Logger logger = LoggerFactory.getLogger(OutStandingParaController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/add-outstanding-paras")
	public String addoutStandingParas(Model model, HttpSession session) {

		logger.info("Method : addoutStandingParas starts");
		DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
		List<DropDownModel>financialYearList = Arrays.asList(audit);
		
		model.addAttribute("financialYearList", financialYearList);

		OutStandingParaModel paras = new OutStandingParaModel();
		try {
			OutStandingParaModel regionSession = (OutStandingParaModel) session.getAttribute("sregion");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (regionSession != null) {
				model.addAttribute("region", regionSession);
				session.setAttribute("sregion", null);
			} else {
				model.addAttribute("paras", paras);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : addoutStandingParas ends");
		return "audit/addOutStandingParas.html";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-outstanding-paras")
	public String postoutStandingParas(@ModelAttribute OutStandingParaModel paras, Model model, HttpSession session) {

		logger.info("Method : postoutStandingParas starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			paras.setCreatedBy(userId);
			resp = restClient.postForObject(env.getAuditUrl() + "restAddOutStandingPara", paras, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sregion", paras);
			return "redirect:/audit/add-outstanding-paras";
		}

		logger.info("Method : postoutStandingParas ends");
		return "redirect:/audit/view-outstanding-paras";
	}

	/**
	 * Default 'View Region Master' page
	 *
	 */
	@GetMapping("/view-outstanding-paras")
	public String viewOutStandingParas(Model model, HttpSession session) {

		logger.info("Method : viewOutStandingParas starts");
		DropDownModel[] audit = restClient.getForObject(env.getAuditUrl() + "getFinancialYear", DropDownModel[].class);
		List<DropDownModel>financialYearList = Arrays.asList(audit);
		
		model.addAttribute("financialYearList", financialYearList);
		logger.info("Method : viewOutStandingParas ends");
		return "audit/viewOutStandingParas.html";
	}

	/**
	 * Web Controller - View Region Details Through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-outstanding-paras-through-ajax")
	public @ResponseBody DataTableResponse viewOutStandingParasThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewOutStandingParasThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<OutStandingParaModel>> jsonResponse = new JsonResponse<List<OutStandingParaModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getOutStandingParas", tableRequest, JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<OutStandingParaModel> region = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<OutStandingParaModel>>() {
					});

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(region);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : viewOutStandingParasThroughAjax ends");
		System.out.println(response);
		return response;
	}

	/**
	 * Web Controller - Delete Region
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-outstanding-paras-delete")
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
	@GetMapping("/view-outstanding-paras-edit")
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

		return "audit/addOutStandingParas.html";
	}

}

