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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.RegionAuditPartyMasterModel;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping(value = "master")
public class MasterRegionAuditPartyController {

	Logger logger = LoggerFactory.getLogger(MasterRegionAuditPartyController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	
	@GetMapping(value = { "add-region-auditparty-master" })
	public String addRegionAuditParty(Model model, HttpSession session) {
		logger.info("Method : addRegionAuditor starts");
		RegionAuditPartyMasterModel regAudParMaster = new RegionAuditPartyMasterModel();

		RegionAuditPartyMasterModel sregAudParMaster = (RegionAuditPartyMasterModel) session
				.getAttribute("sregAudParMaster");

		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (sregAudParMaster != null) {
			model.addAttribute("regAudParMaster", sregAudParMaster);
			session.setAttribute("regAudParMaster", null);

		} else {
			model.addAttribute("regAudParMaster", regAudParMaster);
		}
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl()+ "get-region-list-Name",
					DropDownModel[].class);

			List<DropDownModel> regionList = Arrays.asList(region);
			model.addAttribute("regionList", regionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl() + "get-audit-list-Name",
					DropDownModel[].class);

			List<DropDownModel> auditList = Arrays.asList(region);
			model.addAttribute("auditorList", auditList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : addRegionAuditor ends");
		return "audit/add-region-auditparty-master";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-region-auditparty-master-add-desc" })
	public String addRegionalOfficer(@ModelAttribute RegionAuditPartyMasterModel regionAuditPartyMasterModel, Model model,
			HttpSession session) {
		logger.info("Method : addRegionalOfficer starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			regionAuditPartyMasterModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "addRegionalOfficer", regionAuditPartyMasterModel,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("regAudParMaster", regionAuditPartyMasterModel);
			return "redirect:add-region-auditparty-master";
		}
		logger.info("Method : addRegionalOfficer ends");
		return "redirect:view-region-auditparty-master";
	}
	
	@GetMapping(value = { "view-region-auditparty-master" })
	public String viewDepartmentAuditor(Model model) {
		logger.info("Method : viewRegionalOfficer starts");
		
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl() + "get-region-list-Name",
					DropDownModel[].class);

			List<DropDownModel> regionList = Arrays.asList(region);
			model.addAttribute("regionList", regionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl() + "get-audit-list-Name",
					DropDownModel[].class);

			List<DropDownModel> auditList = Arrays.asList(region);
			model.addAttribute("auditorList", auditList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Method : viewRegionalOfficer ends");
		return "audit/view-region-auditparty-master";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-auditparty-master-throughAjax")
	public @ResponseBody DataTableResponse viewRegionalOfficerThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {
		logger.info("Method : viewRegionalOfficerThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			tableRequest.setParam2(param2);
	
			JsonResponse<List<RegionAuditPartyMasterModel>> jsonResponse = new JsonResponse<List<RegionAuditPartyMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllRegionalMaster", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<RegionAuditPartyMasterModel> regOfficerModel = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<RegionAuditPartyMasterModel>>() {
					});

			String s = "";
			for (RegionAuditPartyMasterModel m : regOfficerModel) {
				byte[] pId = Base64.getEncoder().encode(m.getRegionId().getBytes());
				byte[] aId = Base64.getEncoder().encode(m.getAuditId().getBytes());

				if (m.getStatus()) {
					s = "";
					s = s + "Active";
				} else {
					s = "";
					s = s + "Inactive";
				}
				m.setStatusName(s);
				
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "," + new String(aId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "<a  title='Edit' href='view-region-auditparty-master-edit?id=" + new String(pId)
						+ "," + new String(aId) + "' '><i class='fa fa-edit edit'></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId) + "," + new String(aId) +
						"\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);

				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(regOfficerModel);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewRegionalOfficerThroughAjax ends");

		return response;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/view-region-auditparty-master-model")
	public @ResponseBody JsonResponse<Object> viewRegionalModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewRegionalModal starts");

		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String audId = new String(encodeByte1);
	

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getMasterUrl() + "viewRegionalModelView?id=" + id + "&audId=" + audId ,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewRegionalModal ends");
		return res;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-auditparty-master-edit")
	public String editRegional(Model model, HttpSession session, @RequestParam("id") String encodedIndex) {
		logger.info("Method : editRegional starts");
		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String audId = new String(encodeByte1);
		
		JsonResponse<RegionAuditPartyMasterModel> res = new JsonResponse<RegionAuditPartyMasterModel>();
		RegionAuditPartyMasterModel regionAuditPartyModel = new RegionAuditPartyMasterModel();

		try {

			res = restTemplate.getForObject(env.getMasterUrl() + "regionalEdit?id=" + id + "&audId=" + audId,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		ObjectMapper mapper1 = new ObjectMapper();

		regionAuditPartyModel = mapper1.convertValue(res.getBody(), RegionAuditPartyMasterModel.class);

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl() + "get-region-list-Name",
					DropDownModel[].class);

			List<DropDownModel> regionList = Arrays.asList(region);
			model.addAttribute("regionList", regionList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			DropDownModel[] region = restTemplate.getForObject(env.getMasterUrl() + "get-audit-list-Name",
					DropDownModel[].class);

			List<DropDownModel> auditList = Arrays.asList(region);
			model.addAttribute("auditorList", auditList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		regionAuditPartyModel = mapper.convertValue(res.getBody(), RegionAuditPartyMasterModel.class);
		session.setAttribute("message", "");
		model.addAttribute("regAudParMaster", regionAuditPartyModel);
		logger.info("Method : editRegional ends");
		return "audit/add-region-auditparty-master";
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/view-region-auditparty-master-delete")
	public @ResponseBody JsonResponse<Object> deleteRegion(Model model, HttpSession session,
			@RequestParam("id") String encodedIndex) {
		logger.info("Method : deleteRegion starts");
		String[] x1 = encodedIndex.split(",");

		byte[] encodeByte = Base64.getDecoder().decode(x1[0].getBytes());
		String id = new String(encodeByte);

		byte[] encodeByte1 = Base64.getDecoder().decode(x1[1].getBytes());
		String audId = new String(encodeByte1);

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {

			res = restTemplate.getForObject(env.getMasterUrl() + "deleteRegion?id=" + id + "&audId=" + audId  ,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Success");
		} else {
			res.setMessage("Unsuccess");
		}
		logger.info("Method : deleteRegion ends");
		return res;
	}
	
	
}
