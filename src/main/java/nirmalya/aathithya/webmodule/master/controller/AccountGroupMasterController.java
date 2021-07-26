package nirmalya.aathithya.webmodule.master.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.AccountGroupMasterModel;
//import nirmalya.aathithya.webmodule.master.model.PaymentModeMasterModel;
import nirmalya.aathithya.webmodule.master.model.PaymentModeMasterModel;

@Controller
@RequestMapping(value = "master")
public class AccountGroupMasterController {
	Logger logger = LoggerFactory.getLogger(AccountGroupMasterController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	/**
	 * GetMapping for add Group Code  master
	 */
	@GetMapping("/add-account-group-master")
	public String addGroupCode(Model model, HttpSession session) {
		logger.info("Method : addGroupCode start");
		AccountGroupMasterModel accountGroup = new AccountGroupMasterModel();
		AccountGroupMasterModel form = (AccountGroupMasterModel) session.getAttribute("saccountGroup");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("accountGroup", form);
			session.setAttribute("saccountGroup", null);

		} else {
			model.addAttribute("accountGroup", accountGroup);
		}
		logger.info("Method : addGroupCode end");
		return "master/addAccountGroupMaster";
	}
	/*
	 * post mapping for adding master payment mode
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-account-group-master")
	public String postGroupMaster(@ModelAttribute AccountGroupMasterModel accountGroup, Model model, HttpSession session) {
		logger.info("Method : postGroupMaster starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			accountGroup.setAccGrupCreatedBy(userId);
			resp = restClient.postForObject(env.getMasterUrl() + "addAccountGroup", accountGroup, JsonResponse.class);
			//System.out.println(accountGroup);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("saccountGroup", accountGroup);
			return "redirect:/master/add-account-group-master";
		}
		session.setAttribute("saccountGroup", null);
		logger.info("Method : postGroupMaster end");
		return "redirect:/master/view-account-group-master";
	}
	/**
	 * get mapping for account group master
	 */

	@GetMapping("/view-account-group-master")
	public String viewAccountGroup(Model model) {
		logger.info("Method : viewAccountGroup starts");

		/*JsonResponse<Object> payment = new JsonResponse<Object>();
		model.addAttribute("payment", payment);*/

		logger.info("Method : viewAccountGroup end");
		return "master/listAccountGroupMaster";

	}
	/**
	 * get mapping for viewPayment mode through ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-group-master-throughajax")
	public @ResponseBody DataTableResponse viewAccountGroupThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAccountGroupThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<AccountGroupMasterModel>> jsonResponse = new JsonResponse<List<AccountGroupMasterModel>>();
			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getAllGroup", tableRequest,
					JsonResponse.class);
			//System.out.println("json+++++++++++++++++++++" + jsonResponse);
			ObjectMapper mapper = new ObjectMapper();

			List<AccountGroupMasterModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<AccountGroupMasterModel>>() {
					});

			String s = "";
			for (AccountGroupMasterModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getAccGroup().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='view-account-group-master-edit?id=" +  new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href= 'javascript:void(0)' "
						+ "' onclick='deleteAccountGroup(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\"></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getAccGrupActive()) {
					m.setAccGrupStatus("Active");
				} else {
					m.setAccGrupStatus("Inactive");
				}
			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);
				//System.out.println(x);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : viewAccountGroupThroughAjax end");
		return response;
	}
	/**
	 * get mapping for edit master payment mode
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-group-master-edit")
	public String editAccountGroup(Model model,  @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editAccountGroup starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		AccountGroupMasterModel accountGroup = new AccountGroupMasterModel();
		JsonResponse<AccountGroupMasterModel> jsonResponse = new JsonResponse<AccountGroupMasterModel>();

		try {
			jsonResponse = restClient.getForObject(env.getMasterUrl() + "getAccountGroupById?id=" + id + "&Action=editGroup", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		accountGroup = mapper.convertValue(jsonResponse.getBody(), AccountGroupMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("accountGroup", accountGroup);

		logger.info("Method : editAccountGroup end");
		return "master/addAccountGroupMaster";
	}
	/**
	 * GetMapping for delete account group master
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-account-group-master-delete")
	public @ResponseBody JsonResponse<Object> deleteAccountGroup(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteAccountGroup starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(id.getBytes());
		String id1 = (new String(encodeByte));
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			
			resp = restClient.getForObject(
					env.getMasterUrl() + "deleteAccountById?id=" + id1 + "&createdBy=" + userId,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			System.out.println("if block getmsg() not false : " + resp.getMessage());
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteAccountGroup ends");
		//System.out.println(resp);
		return resp;
	}
	/**
	 * post Mapping for viewInModelData in account group
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-account-group-master-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getMasterUrl() + "getAccountGroupById?id=" + id + "&Action=" + "ModelViewGroup",
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

		logger.info("Method : modelView end");
		return res;
	}
}
