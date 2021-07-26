package nirmalya.aathithya.webmodule.employee.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMethod;
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
import nirmalya.aathithya.webmodule.employee.model.HrmEmployeeReviewerDetailsModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmEmployeeReviewerDetailsController {
	Logger logger = LoggerFactory.getLogger(HrmEmployeeReviewerDetailsController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/*
	 * GetMapping for Add Appraisal Details
	 */
	@GetMapping("/add-employee-appraisal-details")
	public String addEmployeeAppraisalReviewer(Model model, HttpSession session) {

		logger.info("Method : addEmployeeAppraisalReviewer starts");

		HrmEmployeeReviewerDetailsModel reviewerAssign = new HrmEmployeeReviewerDetailsModel();
		HrmEmployeeReviewerDetailsModel reviewerAssignSession = (HrmEmployeeReviewerDetailsModel) session
				.getAttribute("sessionReviewerAssign");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (reviewerAssignSession != null) {
			model.addAttribute("reviewerAssign", reviewerAssignSession);
			session.setAttribute("reviewerAssignSession", null);
		} else {
			model.addAttribute("reviewerAssign", reviewerAssign);
		}
		/*
		 * for viewing drop down list for Frequency
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getAppraisalPolicyList",
					DropDownModel[].class);
			List<DropDownModel> policy = Arrays.asList(dropDownModel);
			model.addAttribute("policy", policy);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list for Financial Year
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getAppraisalFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYear = Arrays.asList(dropDownModel);
			model.addAttribute("financialYear", financialYear);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list for role
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeRoleList",
					DropDownModel[].class);
			List<DropDownModel> roleList = Arrays.asList(dropDownModel);
			model.addAttribute("roleList", roleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addEmployeeAppraisalReviewer ends");

		return "employee/add-Empoyee-Appraisal-Details";
	}

	/*
	 * post Mapping for Get Employee Auto Complete List
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-employee-appraisal-details-employeeAutocompleteList" })

	public @ResponseBody JsonResponse<List<DropDownModel>> getEmployeeListAutoComplete(Model model,
			@RequestBody String searchValue, BindingResult result) {
		logger.info("Method : getEmployeeListAutoComplete starts");

		JsonResponse<List<DropDownModel>> res = new JsonResponse<List<DropDownModel>>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getEmployeeListAutoComplete ends");
		return res;
	}

	/*
	 * dropDown value for Employee Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-employee-appraisal-details-name-throughAjax" })
	public @ResponseBody JsonResponse<DropDownModel> getEmployeeNameAJAX(Model model, @RequestBody String tUserRole,
			BindingResult result) {
		logger.info("Method : getEmployeeNameAJAX starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-emp-name?id=" + tUserRole,
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
		logger.info("Method : getEmployeeNameAJAX ends");

		return res;
	}

	/*
	 * post Mapping for add employee-appraisal-details
	 * 
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-employee-appraisal-details", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> addEmployeeAppraisalDetails(
			@RequestBody List<HrmEmployeeReviewerDetailsModel> reviewerAssign, Model model, HttpSession session) {

		logger.info("Method : addEmployeeAppraisalDetails function starts");
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
		String companyId = "";
		List<HrmEmployeeReviewerDetailsModel> reviewerAssign1 = new ArrayList<HrmEmployeeReviewerDetailsModel>();
		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int size = reviewerAssign.size();
			int count = 0;
			for (HrmEmployeeReviewerDetailsModel q : reviewerAssign) {
				/*****************************************************************/
				/*
				 * String name = q.gettAppraisalFrequency();
				 * 
				 * if (name.contentEquals("dp089")) {
				 * 
				 * count = 1; } else if (name.contentEquals("dp090")) {
				 * 
				 * count = 2;
				 * 
				 * } else if (name.contentEquals("dp091")) {
				 * 
				 * count = 4; } else if (name.contentEquals("dp092")) {
				 * 
				 * count = 12; }
				 * 
				 * for (int i = 0; i < count; i++) {
				 * 
				 * reviewerAssign1.add(q); System.out.println("reviewerAssign1"
				 * + reviewerAssign1); }
				 */
				/******************************************************************/

				q.settSetAuthorityCreatedBy(userId);
				q.settCompanyId(companyId);

			}

			res = restClient.postForObject(env.getEmployeeUrl() + "add-employee-appraisal-details", reviewerAssign,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : addEmployeeAppraisalDetails function Ends");
		return res;
	}

	/*
	 * Get Mapping view employee-appraisal-details
	 */
	@GetMapping("/view-employee-appraisal-details")
	public String viewEmployeeAppraisalDetails(Model model, HttpSession session) {

		logger.info("Method : viewEmployeeAppraisalDetails  starts");

		logger.info("Method : viewEmployeeAppraisalDetails  ends");

		return "employee/view-Empoyee-Appraisal-Details";
	}

	/*
	 * view employee-appraisal-details 'Datatable' call
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-appraisal-details-ThroughAjax")
	public @ResponseBody DataTableResponse viewEmployeeAppraisalDetailsThroughAjax(Model model,
			HttpServletRequest request, @RequestParam String param1, HttpSession session) {
		logger.info("Method : viewEmployeeAppraisalDetailsThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			// String userId = (String) session.getAttribute("USER_ID");
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			// tableRequest.setUserId(userId);

			JsonResponse<List<HrmEmployeeReviewerDetailsModel>> jsonResponse = new JsonResponse<List<HrmEmployeeReviewerDetailsModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getEmployeereviewDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmEmployeeReviewerDetailsModel> reviewerAssign = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmEmployeeReviewerDetailsModel>>() {
					});

			String s = "";
			for (HrmEmployeeReviewerDetailsModel m : reviewerAssign) {

				s = "";
				byte[] pId = Base64.getEncoder().encode(m.gettNFA().getBytes());
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>";
				s = s + "<a  title='Edit' href='view-employee-appraisal-details-edit?id=" + new String(pId)
						+ "' '><i class='fa fa-edit edit' style=\"font-size:24px\"></i></a>";
				s = s + "<a href='javascript:void(0)' onclick='deleteAppraisalDetails(\"" + new String(pId)
						+ "\")'><i class='fa fa-trash' style=\"font-size:24px\"></i></a> ";
				
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(reviewerAssign);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewEmployeeAppraisalDetailsThroughAjax ends");

		return response;
	}

	/*
	 * For Delete employee-appraisal-details
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-appraisal-details-delete")
	public @ResponseBody JsonResponse<Object> deleteAppraisalDetails(@RequestParam String id, HttpSession session) {

		logger.info("Method : deleteAppraisalDetails ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "deleteAppraisalDetails?id=" + id1,
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteAppraisalDetails  ends");

		return resp;
	}

	/*
	 * View selected employee-appraisal-details in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-employee-appraisal-details-model")
	public @ResponseBody JsonResponse<HrmEmployeeReviewerDetailsModel> viewAppraisalDetailsModel(Model model,
			@RequestBody String id, BindingResult result) {
		logger.info("Method : viewAppraisalDetailsModel starts");
		JsonResponse<HrmEmployeeReviewerDetailsModel> res = new JsonResponse<HrmEmployeeReviewerDetailsModel>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "viewAppraisalDetailsModel?id=" + id1,
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
		logger.info("Method : viewAppraisalDetailsModel ends");

		return res;
	}

	/*
	 * for Edit employee-appraisal-details
	 */

	@GetMapping("view-employee-appraisal-details-edit")
	public String editAppraisalDetails(Model model, @RequestParam("id") String index, HttpSession session) {

		logger.info("Method : editAppraisalDetails starts");
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = new String(encodeByte);

		try {

			HrmEmployeeReviewerDetailsModel[] reviewerAssign = restClient.getForObject(
					env.getEmployeeUrl() + "edit-appraisal-details?id=" + id, HrmEmployeeReviewerDetailsModel[].class);
			List<HrmEmployeeReviewerDetailsModel> reviewerAssignList = Arrays.asList(reviewerAssign);

			List<Object> newObj = new ArrayList<Object>();
			List<Object> selectObj = new ArrayList<Object>();
			List<Object> activity2 = new ArrayList<Object>();
			for (HrmEmployeeReviewerDetailsModel m : reviewerAssignList) {
				try {

					DropDownModel[] dropDownModel = restClient.getForObject(
							env.getEmployeeUrl() + "get-user-nameEdit-list?id=" + m.gettUserRole(),
							DropDownModel[].class);
					List<DropDownModel> userList = Arrays.asList(dropDownModel);
					newObj.add(userList);

				} catch (RestClientException e) {
					e.printStackTrace();
				}
				try {

					DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl()
							+ "get-user-selected-list?id=" + m.gettSetAuthorityNFA() + "&role=" + m.gettUserRole(),
							DropDownModel[].class);
					List<DropDownModel> selectedList = Arrays.asList(dropDownModel);
					selectObj.add(selectedList);

					for (DropDownModel p : selectedList) {
						activity2.add(p.getKey());
					}

				} catch (RestClientException e) {
					e.printStackTrace();
				}
			}

			List<String> roleName = new ArrayList<String>();

			String[] roles1 = reviewerAssignList.get(0).gettEmployee().split(",");
			for (int i = 0; i < roles1.length; i++) {
				roleName.add(roles1[i]);
			}
			reviewerAssignList.get(0).setRoleName(roleName);
			model.addAttribute("roleName", roleName);
			model.addAttribute("userList", newObj);
			model.addAttribute("activity2", activity2);
			model.addAttribute("id", reviewerAssignList.get(0).getIsedit());
			model.addAttribute("reviewerAssignList", reviewerAssignList);

		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		/*
		 * for viewing drop down list for role
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeRoleList",
					DropDownModel[].class);
			List<DropDownModel> roleList = Arrays.asList(dropDownModel);
			model.addAttribute("roleList", roleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list for Frequency
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getAppraisalPolicyList",
					DropDownModel[].class);
			List<DropDownModel> policy = Arrays.asList(dropDownModel);
			model.addAttribute("policy", policy);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing drop down list for Financial Year
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "getAppraisalFinancialYear",
					DropDownModel[].class);
			List<DropDownModel> financialYear = Arrays.asList(dropDownModel);
			model.addAttribute("financialYear", financialYear);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : editAppraisalDetails ends");
		return "employee/add-Empoyee-Appraisal-Details";

	}

	/*
	 * dropDown value for From Date through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-employee-appraisal-details-fromDate-throughAjax" })
	public @ResponseBody JsonResponse<HrmEmployeeReviewerDetailsModel> getFinancialFronDateAJAX(Model model,
			@RequestBody String financialDate, BindingResult result) {
		logger.info("Method : getFinancialFronDateAJAX starts");
		JsonResponse<HrmEmployeeReviewerDetailsModel> res = new JsonResponse<HrmEmployeeReviewerDetailsModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-financial-fromdate?id=" + financialDate,
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

		logger.info("Method : getFinancialFronDateAJAX ends");

		return res;

	}

}
