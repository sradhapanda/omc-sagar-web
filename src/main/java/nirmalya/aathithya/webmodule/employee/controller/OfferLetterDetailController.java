package nirmalya.aathithya.webmodule.employee.controller;

import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Base64;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;

import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.employee.model.EmployeeOfferLetterSalaryDetailsModel;
import nirmalya.aathithya.webmodule.employee.model.OfferLetterDetailModel;

/*
 * @author Nirmalya labs
 */

@Controller
@RequestMapping(value = "employee")
public class OfferLetterDetailController {
	Logger logger = LoggerFactory.getLogger(OfferLetterDetailController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	/***************************************************************************************************************************************************/
	/********************************************
	 * OFFER LETTER DETAILS
	 **************************************************/
	/***************************************************************************************************************************************************/
	/*
	 * GetMapping for Add Grade Salary Master - add page
	 */

	@GetMapping("/add-offer-letter-dtls")
	public String addOfferLetterDetails(Model model, HttpSession session) {

		logger.info("Method : addOfferLetterDetails starts");

		OfferLetterDetailModel offerLetterDtls = new OfferLetterDetailModel();
		OfferLetterDetailModel offerLetterDtlsSession = (OfferLetterDetailModel) session
				.getAttribute("sessionOfferLetterDtls");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");

		if (offerLetterDtlsSession != null) {
			model.addAttribute("offerLetterDtls", offerLetterDtlsSession);
			session.setAttribute("offerLetterDtlsSession", null);
		} else {
			model.addAttribute("offerLetterDtls", offerLetterDtls);
		}
		/*
		 * dropDown value for Nationality
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-nationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(dropDownModel);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Pay Grade
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-payGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(dropDownModel);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Job Title list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-jobList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(dropDownModel);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Country list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-countryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] genderListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeGenderList", DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(genderListModel);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] maritalListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeMaritalList", DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(maritalListModel);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addOfferLetterDetails ends");

		return "employee/add-offer-letter-dtls";
	}
	/*
	 * dropDown value for State Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-offer-letter-dtls-stateName-ajax" })
	public @ResponseBody JsonResponse<Object> getStateNameAjax(Model model, @RequestBody String tCountry,
			BindingResult result) {
		logger.info("Method : getStateNameAjax starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-stateNameList?id=" + tCountry,
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
		logger.info("Method : getStateNameAjax ends");
		return res;

	}
	/*
	 * dropDown value for District Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-offer-letter-dtls-districtName-ajax" })
	public @ResponseBody JsonResponse<Object> getDistrictNameAjax(Model model, @RequestBody String tState,
			BindingResult result) {
		logger.info("Method : getDistrictNameAjax starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-districtNameList?id=" + tState,
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
		logger.info("Method : getDistrictNameAjax ends");
		return res;

	}
	/*
	 * dropDown value for Chair Name through Ajax
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-offer-letter-dtls-salaryComponent-ajax" })
	public @ResponseBody JsonResponse<OfferLetterDetailModel> getSalaryComponentAJAX(Model model,
			@RequestBody String tPayGrade, BindingResult result) {
		logger.info("Method : getSalaryComponentAJAX starts");
		JsonResponse<OfferLetterDetailModel> res = new JsonResponse<OfferLetterDetailModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-salaryComponent?id=" + tPayGrade,
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
		logger.info("Method : getSalaryComponentAJAX ends");

		return res;

	}

	@PostMapping(value = { "/uploadhotelFile" })
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("quotationPFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	/*
	 * post Mapping for add goodsReceivenote
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "add-offer-letter-dtls", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<Object> addOfferLetterDetailsPost(
			@RequestBody List<OfferLetterDetailModel> offerLetterDtls, Model model, HttpSession session) {
		logger.info("Method : addOfferLetterDetailsPost function starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		String userId = "";
		String fileFormat = "";

		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
		try {
			offerLetterDtls.get(0).settCreatedBy(userId);
			MultipartFile inputFile = (MultipartFile) session.getAttribute("quotationPFile");
			// System.out.println(inputFile);
			if (inputFile != null) {
				long nowTime = new Date().getTime();

				byte[] bytes = inputFile.getBytes();
				String[] fileType = inputFile.getContentType().split("/");
				String contentName = nowTime + "." + fileType[1];

				// System.out.println(contentName);
				for (OfferLetterDetailModel m : offerLetterDtls) {
					
					
					m.settEmpImage(contentName);
				}
				offerLetterDtls.get(0).settEmpImage(contentName);
				// System.out.println("offerLetterDtls" + offerLetterDtls);
				res = restClient.postForObject(env.getEmployeeUrl() + "add-offer-letter-dtls", offerLetterDtls,
						JsonResponse.class);

				if ((res.getCode() == null || res.getCode() == "")
						&& (res.getMessage() == null || res.getMessage() == "")) {
					Path path = Paths.get(env.getFileUploadEmployee() + contentName);

					Files.write(path, bytes);
					ByteArrayInputStream in = new ByteArrayInputStream(bytes);
					Integer height = 50;
					Integer width = 50;

					try {
						BufferedImage img = ImageIO.read(in);
						if (height == 0) {
							height = (width * img.getHeight()) / img.getWidth();
						}
						if (width == 0) {
							width = (height * img.getWidth()) / img.getHeight();
						}

						Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
						BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
						imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

						ByteArrayOutputStream buffer = new ByteArrayOutputStream();

						ImageIO.write(imageBuff, "png", buffer);

						byte[] thumb = buffer.toByteArray();
						Path pathThumb = Paths.get(env.getFileUploadEmployee() + "thumb\\" + contentName);
						Files.write(pathThumb, thumb);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			} else {
				res = restClient.postForObject(env.getEmployeeUrl() + "add-offer-letter-dtls", offerLetterDtls,
						JsonResponse.class);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null && res.getMessage() != "") {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("Success");
		}

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : addOfferLetterDetailsPost ends");
		return res;
	}

	/*
	 * Get Mapping view Offer Letter Details
	 */
	@GetMapping("/View-offer-letter-dtls")
	public String viewOfferLetterDetails(Model model, HttpSession session) {

		logger.info("Method : viewOfferLetterDetails starts");

		logger.info("Method : viewOfferLetterDetails ends");

		return "employee/view-offer-letter-dtls";
	}

	/*
	 * For view viewOfferLetterDetails for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/View-offer-letter-dtls-ThroughAjax")
	public @ResponseBody DataTableResponse viewOfferLetterDetailsAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {

		logger.info("Method : viewOfferLetterDetailsAjax statrs");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<OfferLetterDetailModel>> jsonResponse = new JsonResponse<List<OfferLetterDetailModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "viewOfferLetterDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<OfferLetterDetailModel> offerLetterDtls = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<OfferLetterDetailModel>>() {
					});

			String s = "";

			for (OfferLetterDetailModel m : offerLetterDtls) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.gettOfferLetterId().getBytes());
				byte[] encodeId1 = Base64.getEncoder().encode(m.gettPayGrade().getBytes());
				s = s + "<a href='View-offer-letter-dtls-edit?id=" + new String(encodeId) + "&grade="
						+ new String(encodeId1)
						+ "' ><i class=\"fa fa-edit\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteDetails(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" aria-hidden=\"true\" style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a data-toggle='modal' title='View'  href='javascript:void' onclick='viewInModel(\""
						+ new String(encodeId)
						+ "\")'><i class='fa fa-search search' style=\"font-size:24px\"></i></a>&nbsp;&nbsp;<a href='View-offer-letter-dtls-EmployeeForm?id="
						+ new String(encodeId)
						+ "'><i class='fa fa-files-o' title='Generate Appointment Letter' style=\"font-size:20px\"></i></a>";

				m.settOfferLetterId("<a href='javascript:void' onclick='pdfCreate(\"" + new String(encodeId) + "\")'>"
						+ m.gettOfferLetterId() + "</a>");

				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(offerLetterDtls);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewOfferLetterDetailsAjax Theme ends");

		return response;
	}

	/*
	 * for Edit Goal Master
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/View-offer-letter-dtls-edit")

	public String editOfferLetterDtls(Model model, @RequestParam("id") String id1, @RequestParam("grade") String id2,
			HttpSession session) {

		logger.info("Method : editOfferLetterDtls starts");
		byte[] encodeByte = Base64.getDecoder().decode(id1.getBytes());
		String id = (new String(encodeByte));
		byte[] encodeByte2 = Base64.getDecoder().decode(id2.getBytes());
		String grade = (new String(encodeByte2));

		try {

			OfferLetterDetailModel[] offerLetterDtls = restClient.getForObject(
					env.getEmployeeUrl() + "editOfferLetterDtlsById?id=" + id + "&grade=" + grade,
					OfferLetterDetailModel[].class);
			List<OfferLetterDetailModel> offerLetterDtlsList = Arrays.asList(offerLetterDtls);

			try {
				DropDownModel[] dropDownModel = restClient.getForObject(
						env.getEmployeeUrl() + "rest-get-editDistList?id=" + offerLetterDtlsList.get(0).gettState(),
						DropDownModel[].class);
				List<DropDownModel> distList = Arrays.asList(dropDownModel);
				model.addAttribute("distList", distList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			/*
			 * dropDown value for state list for edit
			 */
			try {
				DropDownModel[] dropDownModel = restClient.getForObject(
						env.getEmployeeUrl() + "rest-get-editStateList?id=" + offerLetterDtlsList.get(0).gettCountry(),
						DropDownModel[].class);
				List<DropDownModel> stateList = Arrays.asList(dropDownModel);
				model.addAttribute("stateList", stateList);

			} catch (RestClientException e) {
				e.printStackTrace();
			}

			session.setAttribute("message", "");
			if (offerLetterDtlsList.get(0).gettEmpImage() == null || offerLetterDtlsList.get(0).gettEmpImage() == "") {
				offerLetterDtlsList.get(0).settEmpImage(null);
			} else {
				String image = offerLetterDtlsList.get(0).gettEmpImage();
				session.setAttribute("imageNameFromDnForEdit", image);
				String variable = env.getBaseURL();
				System.out.println(variable + "document/employee/" + image + "");
				model.addAttribute("image", variable + "document/employee/" + image + "");
				if (image != null) {
					String action = "/document/employee/" + image;
					offerLetterDtlsList.get(0).setAction(action);
				}
			}
			model.addAttribute("id", offerLetterDtlsList.get(0).gettOfferLetterId());
			model.addAttribute("offerLetterDtlsList", offerLetterDtlsList);
		} catch (RestClientException e) {

			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		/*
		 * dropDown value for Nationality
		 */
		try {

			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-nationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(dropDownModel);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Pay Grade
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-payGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(dropDownModel);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Job Title list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-jobList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(dropDownModel);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * dropDown value for Country list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-countryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] genderListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeGenderList", DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(genderListModel);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] maritalListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeMaritalList", DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(maritalListModel);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : editOfferLetterDtls ends");

		return "employee/add-offer-letter-dtls";
	}

	/*
	 * For Offer Letter Details Modal View
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/View-offer-letter-dtls-modalView" })

	public @ResponseBody JsonResponse<Object> viewOfferLetterDetailsModal(Model model, @RequestBody String index,
			BindingResult result) {

		logger.info("Method : viewBeautyChairStaffAssignModel starts");
		byte[] decodeId = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(decodeId));

		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "offerLetterDetailsModal?id=" + id,
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

		logger.info("Method : viewOfferLetterDetailsModal ends");
		return res;
	}

	/*
	 * For Delete particular offer letter detail
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/View-offer-letter-dtls-delete")
	public @ResponseBody JsonResponse<Object> viewOfferLetterDetailsDelete(@RequestParam String id,
			HttpSession session) {

		logger.info("Method : viewOfferLetterDetailsDelete ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(id.getBytes());

		String id1 = (new String(decodeId));
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp = restClient.getForObject(
					env.getEmployeeUrl() + "deleteLetterDetailsById?id=" + id1 + "&createdBy=" + createdBy,
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
		logger.info("Method : viewOfferLetterDetailsDelete  ends");

		return resp;
	}

	/***************************************************************************************************************************************************/
	/********************************************
	 * APPOINTMENT LETTER CONTROLLER
	 **************************************************/
	/***************************************************************************************************************************************************/

	/*
	 * for Edit employee-appraisal-details
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/View-offer-letter-dtls-EmployeeForm" })
	public String addEmployeeDetails(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {
		logger.info("Method : addEmployeeDetails starts");

		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id1 = (new String(decodeId));

		JsonResponse<List<EmployeeOfferLetterSalaryDetailsModel>> jsonresponse = new JsonResponse<List<EmployeeOfferLetterSalaryDetailsModel>>();

		try {
			jsonresponse = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeDetails?id=" + id1,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<EmployeeOfferLetterSalaryDetailsModel> employeeComponentDetails = mapper
				.convertValue(jsonresponse.getBody(), new TypeReference<List<EmployeeOfferLetterSalaryDetailsModel>>() {
				});

		String image = employeeComponentDetails.get(0).getEmployeeImage();
		String variable = env.getBaseURL();
		model.addAttribute("image", variable + "document/employee/" + image + "");

		model.addAttribute("employeeComponentDetails", employeeComponentDetails);
		System.out.println("employeeComponentDetails" + employeeComponentDetails);
		/*
		 * dropDown value for Nationality
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-nationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(dropDownModel);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Pay Grade
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-payGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(dropDownModel);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		/*
		 * dropDown value for Country list
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-countryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] genderListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeGenderList", DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(genderListModel);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] maritalListModel = restClient
					.getForObject(env.getEmployeeUrl() + "rest-get-employeeMaritalList", DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(maritalListModel);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for viewing employment list
		 */
		try {
			DropDownModel[] Employment = restClient.getForObject(env.getEmployeeUrl() + "getEmploymentList",
					DropDownModel[].class);
			List<DropDownModel> EmploymentList = Arrays.asList(Employment);
			model.addAttribute("EmploymentList", EmploymentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] jobTitle = restClient.getForObject(env.getEmployeeUrl() + "getJobTitleList",
					DropDownModel[].class);
			List<DropDownModel> jobTitleList = Arrays.asList(jobTitle);
			model.addAttribute("jobTitleList", jobTitleList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * dropDown value for state list for edit
		 */
		try {
			DropDownModel[] dropDownModel = restClient.getForObject(env.getEmployeeUrl() + "rest-get-editStateList?id="
					+ employeeComponentDetails.get(0).getEmployeeCountry(), DropDownModel[].class);
			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
			model.addAttribute("stateList", stateList);
			System.out.println(stateList);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : addEmployeeDetails ends");
		return "employee/add-employee-details-form";
	}

	/*
	 * drop down for Designation list onChange of department
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "View-offer-letter-dtls-EmployeeForm-designation-ajax" })
	public @ResponseBody JsonResponse<Object> getDesignationAjax(Model model, @RequestBody String employeeDepartment,
			BindingResult result) {
		logger.info("Method : getDesignationAjax starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "rest-get-designation?id=" + employeeDepartment,
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
		logger.info("Method : getDesignationAjax ends");
		return res;

	}

	/**
	 * upload employee image
	 * 
	 */
	@PostMapping(value = { "/uploadEmployeeImageFile" })
	public @ResponseBody JsonResponse<Object> uploadEmployeeImageFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadEmployeeImageFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("quotationPFile", inputFile);
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadEmployeeImageFile controller function 'post-mapping' ends");
		return response;
	}

	/*
	 * for First Stage Approval - Submit
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "View-offer-letter-dtls-EmployeeForm-submit", method = { RequestMethod.POST })
	public @ResponseBody JsonResponse<DropDownModel> employeeDetailsFormSubmit(
			@RequestBody List<EmployeeOfferLetterSalaryDetailsModel> employeeComponentDetails, Model model,
			HttpSession session) {
		logger.info("Method : employeeDetailsFormSubmit  starts");
		System.out.println("employeeComponentDetails" + employeeComponentDetails);

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		String userId = "";
		String companyId = "";
		String s = "";

		for (String m : employeeComponentDetails.get(0).getRoleList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			employeeComponentDetails.get(0).setEmployeeJobTitle(s);
			;
		}

		try {
			userId = (String) session.getAttribute("USER_ID");
			companyId = (String) session.getAttribute("COMPANY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			employeeComponentDetails.get(0).setCreatedBy(userId);
			employeeComponentDetails.get(0).setCompanyId(companyId);

			res = restClient.postForObject(env.getEmployeeUrl() + "submit-employee-details-form",
					employeeComponentDetails, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String message = res.getMessage();

		if (message != null && message != "") {

		} else {
			res.setMessage("Success");
		}
		logger.info("Method : employeeDetailsFormSubmit  Ends");
		return res;
	}

}