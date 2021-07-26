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
import nirmalya.aathithya.webmodule.master.model.PaymentModeMasterModel;

/**
* @author NirmalyaLabs
*
*/


@Controller
@RequestMapping(value = "master")
public class PaymentModeMasterController {
	Logger logger = LoggerFactory.getLogger(PaymentModeMasterController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	/**
	 * GetMapping for add payment mode master
	 */
	@GetMapping("/add-payment-mode-master")
	public String addPaymentMode(Model model, HttpSession session) {
		logger.info("Method : addPaymentMode starts");
		PaymentModeMasterModel payment = new PaymentModeMasterModel();
		PaymentModeMasterModel form = (PaymentModeMasterModel) session.getAttribute("spayment");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("payment", form);
			session.setAttribute("spayment", null);

		} else {
			model.addAttribute("payment", payment);
		}
		logger.info("Method : addPaymentMode end");
		return "master/addPaymentModeMaster";
	}
	/*
	 * post mapping for adding master payment mode
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-payment-mode-master")
	public String postPayment(@ModelAttribute PaymentModeMasterModel payment, Model model, HttpSession session) {
		logger.info("Method : postPayment starts");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			payment.setPayModCreatedBy(userId);
			resp = restClient.postForObject(env.getMasterUrl() + "addPaymentMode", payment, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("spayment", payment);
			return "redirect:/master/add-payment-mode-master";
		}
		session.setAttribute("spayment", null);
		logger.info("Method : postPayment end");
		return "redirect:/master/view-payment-mode-master";
	}
	/**
	 * get mapping for view payment mode master
	 */

	@GetMapping("/view-payment-mode-master")
	public String viewPaymentMode(Model model) {
		logger.info("Method : viewPaymentMode starts");

		/*JsonResponse<Object> payment = new JsonResponse<Object>();
		model.addAttribute("payment", payment);*/

		logger.info("Method : viewPaymentMode end");
		return "master/listPaymentModeMaster";

	}
	/**
	 * get mapping for viewPayment mode through ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-payment-mode-master-throughajax")
	public @ResponseBody DataTableResponse viewPaymentThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewPaymentThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<PaymentModeMasterModel>> jsonResponse = new JsonResponse<List<PaymentModeMasterModel>>();
			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getAllPayment", tableRequest,
					JsonResponse.class);
			//System.out.println("json+++++++++++++++++++++" + jsonResponse);
			ObjectMapper mapper = new ObjectMapper();

			List<PaymentModeMasterModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<PaymentModeMasterModel>>() {
					});

			String s = "";
			for (PaymentModeMasterModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getPaymentMode().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + " &nbsp;&nbsp <a href='view-payment-mode-master-edit?id=" +  new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> &nbsp;&nbsp; <a href= 'javascript:void(0)' "
						+ "' onclick='deletePaymentMode(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\"></i></a> ";
				m.setAction(s);
				s = "";

				if (m.getPayModActive()) {
					m.setPayStatusName("Active");
				} else {
					m.setPayStatusName("Inactive");
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
		logger.info("Method : viewPaymentThroughAjax end");
		return response;
	}
	/**
	 * get mapping for edit master payment mode
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-payment-mode-master-edit")
	public String editPayment(Model model,  @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editPayment starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		PaymentModeMasterModel payment = new PaymentModeMasterModel();
		JsonResponse<PaymentModeMasterModel> jsonResponse = new JsonResponse<PaymentModeMasterModel>();

		try {
			jsonResponse = restClient.getForObject(env.getMasterUrl() + "getPaymentById?id=" + id + "&Action=editPayment", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		payment = mapper.convertValue(jsonResponse.getBody(), PaymentModeMasterModel.class);
		session.setAttribute("message", "");

		model.addAttribute("payment", payment);

		logger.info("Method : editPayment end");
		return "master/addPaymentModeMaster";
	}
	/**
	 * GetMapping for delete master payment mode
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-payment-mode-master-delete")
	public @ResponseBody JsonResponse<Object> deletePaymentMode(@RequestParam String id, HttpSession session) {
		logger.info("Method : deletePaymentMode starts");

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
					env.getMasterUrl() + "deletePaymentById?id=" + id1 + "&createdBy=" + userId,
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
		logger.info("Method : deletePaymentMode ends");

		return resp;
	}
	/**
	 * post Mapping for viewInModelData in master payment mode
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-payment-mode-master-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getMasterUrl() + "getPaymentById?id=" + id + "&Action=" + "ModelViewPayment",
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
