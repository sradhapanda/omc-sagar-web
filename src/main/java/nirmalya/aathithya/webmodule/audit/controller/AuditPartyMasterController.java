package nirmalya.aathithya.webmodule.audit.controller;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.audit.model.AuditPartyMasterModel;
import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;



@Controller
@RequestMapping(value = "master")
public class AuditPartyMasterController {

	
Logger logger = LoggerFactory.getLogger(AuditPartyMasterController.class);
	
	@Autowired
	RestTemplate restClient;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;
	
	@Autowired
	EnvironmentVaribles env;
	
	
	//VIEW AUDIT PARTY 
	@GetMapping("/view-auditparty-master")
	public String viewAuditParty(Model model, HttpSession session) {
		
		logger.info("Method : viewAuditParty starts");
		
		logger.info("Method : viewAuditParty ends");
		
		return "audit/view-auditparty-master";
	}
	
	//VIEW THROUGH AJAX
	@SuppressWarnings("unchecked")
	@GetMapping("/view-auditparty-master-through-ajax")
	public @ResponseBody DataTableResponse viewAuditPartyThroughAjax(Model model, HttpServletRequest request, @RequestParam String param1)
	{
		logger.info("Method : viewAuditPartyThroughAjax starts");
		
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			
			JsonResponse<List<AuditPartyMasterModel>> jsonResponse = new JsonResponse<List<AuditPartyMasterModel>>();
			
			jsonResponse = restClient.postForObject(env.getAuditUrl()+ "getAuditParty", tableRequest, JsonResponse.class);
			
			System.out.println("JSON@@@"+jsonResponse);
			ObjectMapper mapper = new ObjectMapper();

			List<AuditPartyMasterModel> audit = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AuditPartyMasterModel>>() {
					});
			
			String s = "";
			
			for (AuditPartyMasterModel m : audit) {
				byte[] pId = Base64.getEncoder().encode(m.getAuditPartyId().getBytes());
				
				/*
				 * if (m.getAuditPartyActive()) { s = ""; s = s + "Active"; } else { s = ""; s =
				 * s + "Inactive"; } m.setStatusName(s);
				 */
				
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "<a  title='Edit' href='view-auditparty-master-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit'></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId) +
						"\")'><i class='fa fa-trash'></i></a> ";
				m.setAction(s);

				s = "";
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(audit);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Method : viewAuditPartyThroughAjax ends");
		
		return response;
	}
	
}