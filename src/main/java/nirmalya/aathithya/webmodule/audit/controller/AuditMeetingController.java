package nirmalya.aathithya.webmodule.audit.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import nirmalya.aathithya.webmodule.audit.model.AuditMeetingModel;
import nirmalya.aathithya.webmodule.audit.model.PersonListModel;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;

@Controller
@RequestMapping(value = "audit")
public class AuditMeetingController {
	Logger logger = LoggerFactory.getLogger(AuditHistoryController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	/*
	 * Get Mapping auditHistory
	 */
	@GetMapping("/add-meeting-schedule")
	public String scheduleMeeting(Model model,@RequestParam("id") String id, HttpSession session) {

		logger.info("Method : scheduleMeeting starts");
		byte[] array = Base64.getDecoder().decode(id.getBytes());
		String decodedId = new String(array);

		/** Department Emp List **/
		try {
			DropDownModel[] program = restClient.getForObject(env.getAuditUrl() + "getempList", DropDownModel[].class);
			List<DropDownModel> programList = Arrays.asList(program);
			model.addAttribute("empList", programList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/** persons to forward **/
		try {
			PersonListModel[] dept = restClient.getForObject(env.getAuditUrl() + "getPersonList",
					PersonListModel[].class);
			List<PersonListModel> personList = Arrays.asList(dept);

			model.addAttribute("personList", personList);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/** Department **/
		try {
			DropDownModel[] dept = restClient.getForObject(env.getAuditUrl() + "getDeptList",
					DropDownModel[].class);
			List<DropDownModel> listDept = Arrays.asList(dept);
			model.addAttribute("listDept",listDept);
		} catch (RestClientException e) {
			e.printStackTrace();
		} /** Department **/
		AuditMeetingModel[] meetingDetails = restClient.getForObject(env.getAuditUrl() + "getMeetingDetails?id=" + "1"+"&auditType="+decodedId,
				AuditMeetingModel[].class);
		List<AuditMeetingModel> meetingDtl = Arrays.asList(meetingDetails);
		for (AuditMeetingModel a : meetingDtl) {
			DropDownModel[] meetingDetailsDoc = restClient.getForObject(env.getAuditUrl() + "getMeetingDetailsDoc?id=" + a.getId(),
					DropDownModel[].class);
			List<DropDownModel> meetingDtlDoc = Arrays.asList(meetingDetailsDoc);
			if(a.getMeetingType()!=null && a.getMeetingType()!="") {
			if(a.getMeetingType().equals("1")) {
				a.setMeetingType("Entry Meeting");
			}
			if(a.getMeetingType().equals("2")) {
				a.setMeetingType("Exit Meeting");
			}
			}
			for(DropDownModel m:meetingDtlDoc) {
			if (m.getKey() != null && m.getKey() != "") {
				String[] x = m.getKey().split("\\.");
				
				if(x[1].length()>1) {
				if (x[1].equals("xls")||x[1].equals("xlsx")) {

					String docPath = "<a href=\"/document/audit/excel/" + m.getKey()
							+ "\"><i class=\"fa fa-file-excel-o fa-2x excel\" title='"+m.getName()+"'></i> </a>";

					m.setKey(docPath);
				}
				if (x[1].equals("pdf")) {
					String docPath = "<a href=\"/document/audit/excel/" +  m.getKey()
							+ "\"><i class=\"fa fa-file-pdf-o fa-2x  pdf\" title='"+m.getName()+"'></i> </a>";

					m.setKey(docPath);
				}
				if (x[1].equals("docx")) {
					String docPath = "<a href=\"/document/audit/excel/" +  m.getKey()
							+ "\"><i class=\"fa fa-file-word-o  fa-2x  word \" aria-hidden=\"true\"  title='"+m.getName()+"'></i> </a>";
					m.setKey(docPath);
				}
				if (x[1].equals("jpg")||x[1].equals("png")) {
					String docPath = "<a href=\"/document/audit/excel/" +  m.getKey()
					+ "\"><i class=\"fa fa-file-image-o fa-2x   \" aria-hidden=\"true\"  title='"+m.getName()+"'></i> </a>";
					m.setKey(docPath);
				}
				}
				
			}
			a.setDocuments(meetingDtlDoc);
		}
		
		}
	
		model.addAttribute("meetingDtl",meetingDtl);
		model.addAttribute("auditTypeId",decodedId);
		logger.info("Method : scheduleMeeting ends");

		return "audit/scheduleMeeting";
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-meeting-schedule-modal-meeting-details" })
	public @ResponseBody JsonResponse<Object> modalMeetingDetailsAfterAttend(Model model, @RequestBody Integer index, BindingResult result) {
		logger.info("Method : modalMeetingDetailsAfterAttend starts");
		
//		byte[] encodeByte=Base64.getDecoder().decode(index.getBytes());
//		String id = (new String(encodeByte));
		
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getAuditUrl() + "modalMeetingDetailsAfterAttend?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		logger.info("Method : modalMeetingDetailsAfterAttend ends");
		return res;
	}
	
}
