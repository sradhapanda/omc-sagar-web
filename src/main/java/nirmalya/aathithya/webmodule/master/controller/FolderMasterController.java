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
import nirmalya.aathithya.webmodule.master.model.FolderMasterModel;

@Controller
@RequestMapping(value = "master")

public class FolderMasterController {

	Logger logger = LoggerFactory.getLogger(FolderMasterController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	@GetMapping("/add-folder")
	public String defaultDepartmentDetails(Model model, HttpSession session) {

		logger.info("Method : Folder starts");

		FolderMasterModel folder = new FolderMasterModel();
		try {
			FolderMasterModel regionSession = (FolderMasterModel) session.getAttribute("sfolder");

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);

			}

			session.setAttribute("message", "");
			if (regionSession != null) {
				model.addAttribute("folder", regionSession);
				session.setAttribute("sfolder", null);
			} else {
				model.addAttribute("folder", folder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Method : Folder ends");
		return "master/add-folder";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/add-folder")
	public String addFolder(@ModelAttribute FolderMasterModel folder, Model model, HttpSession session) {

		logger.info("Method : addFolder starts");

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			folder.setCreatedBy(userId);
			resp = restClient.postForObject(env.getAuditUrl() + "restAddFolder", folder, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != "" && resp.getMessage() != null) {

			session.setAttribute("message", resp.getMessage());
			session.setAttribute("sFolder", folder);
			return "redirect:/master/add-folder";
		}

		logger.info("Method : addFolder ends");
		return "redirect:/master/view-folder";
	}

	/**
	 * Default 'View folder Master' page
	 *
	 */
	@GetMapping("/view-folder")
	public String viewfolder(Model model, HttpSession session) {

		logger.info("Method : viewfolder starts");

		logger.info("Method : viewfolder ends");
		return "master/view-folder";
	}

	/**
	 * Web Controller - View Region Details Through AJAX
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-folder-through-ajax")
	public @ResponseBody DataTableResponse viewFolderThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewFolderThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<FolderMasterModel>> jsonResponse = new JsonResponse<List<FolderMasterModel>>();

			jsonResponse = restClient.postForObject(env.getAuditUrl() + "getAllFolder", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<FolderMasterModel> region = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<FolderMasterModel>>() {
					});
			/* System.out.println(region); */
			String s = "";
			for (FolderMasterModel m : region) {
				System.out.println(m.getFolder()); 
				byte[] pId = Base64.getEncoder().encode(m.getFolder().getBytes());
				// System.out.println(m.getStatus());

				s = "<a href='view-folder-edit?id=" + new String(pId)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a href='javascript:void(0)' onclick='deleteFolder(\"" + new String(pId)
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

		logger.info("Method : viewFolderThroughAjax ends");
		System.out.println(response);
		return response;
	}

	/**
	 * Web Controller - Delete Region
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-folder-delete")
	public @ResponseBody JsonResponse<Object> deleteFolder(Model model, @RequestParam String id, HttpSession session) {

		logger.info("Method : deleteFolder starts");
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String index = (new String(encodeByte));
		JsonResponse<Object> resp = new JsonResponse<Object>();

		// System.out.println(index);
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resp = restClient.getForObject(env.getAuditUrl() + "deleteFolderById?id=" + index + "&createdBy=" + userId,
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
		logger.info("Method : deleteFolder ends");

		return resp;
	}

	/**
	 * Web Controller - Edit Folder
	 *
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-folder-edit")
	public String editFolder(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

		logger.info("Method : editFolder starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = (new String(encodeByte));

		FolderMasterModel folder = new FolderMasterModel();
		JsonResponse<FolderMasterModel> jsonResponse = new JsonResponse<FolderMasterModel>();

		try {

			jsonResponse = restClient.getForObject(env.getAuditUrl() + "getFolderById?id=" + id, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		folder = mapper.convertValue(jsonResponse.getBody(), FolderMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("folder", folder);

		logger.info("Method : editFolder ends");

		return "master/add-folder";
	}

}
