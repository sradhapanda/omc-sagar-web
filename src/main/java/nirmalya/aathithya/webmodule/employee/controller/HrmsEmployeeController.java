package nirmalya.aathithya.webmodule.employee.controller;

import java.awt.Color;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
import nirmalya.aathithya.webmodule.employee.model.HrmsEmployeeModel;

/*
 * @author Nirmalya labs
 */
@Controller
@RequestMapping(value = "employee")
public class HrmsEmployeeController {
	Logger logger = LoggerFactory.getLogger(HrmsEmployeeController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PasswordEncoder passwordEncoder;

	/*
	 * @Autowired MailService mail;
	 */

	/*
	 * GetMapping for Add employee view page
	 */
	@GetMapping("/add-employee")
	public String addemployee(Model model, HttpSession session) {

		logger.info("Method : addemployee  starts");

		HrmsEmployeeModel employee = new HrmsEmployeeModel();
		HrmsEmployeeModel sessionemployee = (HrmsEmployeeModel) session.getAttribute("sessionemployee");

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);

		}

		session.setAttribute("message", "");
		System.out.println("check" + sessionemployee);
		if (sessionemployee != null) {
			try {
				DropDownModel[] state = restClient.getForObject(
						env.getEmployeeUrl() + "getStateforEdit?id=" + sessionemployee.getEmployeeCountry(),
						DropDownModel[].class);
				List<DropDownModel> stateList = Arrays.asList(state);
				model.addAttribute("stateList", stateList);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("employee", sessionemployee);
			session.setAttribute("sessionemployee", null);
			List<String> roleList = new ArrayList<String>();
			String[] roles = sessionemployee.getEmployeeJobTitle().split(",");

			for (int i = 0; i < roles.length; i++) {
				roleList.add(roles[i]);
			}
			employee.setRoleList(roleList);
			if (roleList.isEmpty()) {
				model.addAttribute("selectedRoles", "");
			} else {
				model.addAttribute("selectedRoles", roleList);
			}

			/*
			 * String image = sessionemployee.getEmployeeImage(); if (image != null) {
			 * String variable = env.getBaseUrlPath(); model.addAttribute("image", variable
			 * + "document/employee/" + image + ""); }
			 */

		} else {
			model.addAttribute("employee", employee);
		}
		/*
		 * for viewing drop down list
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
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] region = restClient.getForObject(env.getEmployeeUrl() + "getRegionList",
					DropDownModel[].class);
			List<DropDownModel> RegiontList = Arrays.asList(region);
			model.addAttribute("RegiontList", RegiontList);
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
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getNationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(nationality);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getGenderList",
					DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(nationality);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get Country drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getCountryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(nationality);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getMaritalList",
					DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(nationality);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getPayGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(nationality);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for get designation drop down list
		 */
		try {
			DropDownModel[] getUserRole = restClient.getForObject(env.getEmployeeUrl() + "getUserRole",
					DropDownModel[].class);
			List<DropDownModel> userRole = Arrays.asList(getUserRole);
			model.addAttribute("userRole", userRole);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Method : addemployee  ends");

		return "employee/add-employee-form";
	}

	/*
	 * Post Mapping for adding new employee
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@PostMapping("/add-employee")
	public String addemployeePost(@ModelAttribute HrmsEmployeeModel employee, Model model, HttpSession session) {

		logger.info("Method : addemployee Post starts");
		JsonResponse<Object> response = new JsonResponse<Object>();
		String s = "";

		for (String m : employee.getRoleList()) {
			s = s + m + ",";
		}

		if (s != "") {
			s = s.substring(0, s.length() - 1);
			employee.setEmployeeJobTitle(s);
			;
		}

		try {
			session.setAttribute("password", employee.getEmployeePassword());
			String imageName = null;
			byte[] bytes = (byte[]) session.getAttribute("employeeImageFile");
			if (bytes != null) {
				long nowTime = new Date().getTime();
				imageName = nowTime + ".png";
				employee.setEmployeeImage(imageName);
			} else {
				imageName = (String) session.getAttribute("imageNameFromDnForEdit");
				employee.setEmployeeImage(imageName);

			}
			String enc = employee.getEmployeePassword();
			if (enc != null && enc != "") {
				enc = passwordEncoder.encode(enc);
				employee.setEmployeePassword(enc);
			}

			String enc1 = employee.getEmployeepPin();
			if (enc1 != null && enc1 != "") {
				enc1 = passwordEncoder.encode(enc1);
				employee.setEmployeepPin(enc1);
			}
			String userId = (String) session.getAttribute("USER_ID");
			String companyId = (String) session.getAttribute("COMPANY_ID");
			employee.setCreatedBy(userId);
			employee.setCompanyId(companyId);
			response = restClient.postForObject(env.getEmployeeUrl() + "restAddemployee", employee, JsonResponse.class);

			if ((response.getCode() == null || response.getCode() == "")
					&& (response.getMessage() == null || response.getMessage() == "")) {
				Path path = Paths.get(env.getFileUploadEmployee() + imageName);
				if (bytes != null) {
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
						BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
						imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

						ByteArrayOutputStream buffer = new ByteArrayOutputStream();

						ImageIO.write(imageBuff, "jpg", buffer);

						byte[] thumb = buffer.toByteArray();
						Path pathThumb = Paths.get(env.getFileUploadEmployee() + "thumb\\" + imageName);
						Files.write(pathThumb, thumb);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			if (response.getBody() != null) {
				ObjectMapper mapper = new ObjectMapper();

				List<HrmsEmployeeModel> savedEmployee = mapper.convertValue(response.getBody(),
						new TypeReference<List<HrmsEmployeeModel>>() {
						});
				if (!savedEmployee.isEmpty()) {
					String regId = savedEmployee.get(0).getEmployeeNo();
					String sendEmail = savedEmployee.get(0).getEmployeePersonalEmail();

					String password = (String) session.getAttribute("password");
					/*
					 * try { mail.sendEmail(sendEmail, "From Nirmalya Labs", "Congrats " +
					 * savedEmployee.get(0).getEmployeeFname() +
					 * "Your Registration is Successful. Your LogIn Id Is :" + regId +
					 * " . Password is :" + password); } catch (Exception e) {
					 * 
					 * }
					 */
				}

				session.removeAttribute("password");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response.getMessage() != "") {
			session.setAttribute("message", response.getMessage());
			session.setAttribute("sessionemployee", employee);
			return "redirect:/employee/add-employee";
		}

		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.removeAttribute("employeeImageFile");
		session.removeAttribute("imageNameFromDnForEdit");
		logger.info("Method : Add employee ends");
		System.out.println("Controller Response=" + response);
		return "redirect:/employee/view-employee";
	}

	/*
	 * Get Mapping view work type
	 */
	@GetMapping("/view-employee")
	public String viewemployee(Model model, HttpSession session) {

		logger.info("Method : viewemployee  starts");

		logger.info("Method : viewemployee  ends");

		return "employee/view-employee-form";
	}

	/*
	 * For view work type for dataTable Ajaxcall
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-ThroughAjax")
	public @ResponseBody DataTableResponse viewemployeeAjax(Model model, HttpServletRequest request,
			@RequestParam String param1, @RequestParam String param2) {

		logger.info("Method : viewemployee ajax statrs");

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

			JsonResponse<List<HrmsEmployeeModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeModel>>();

			jsonResponse = restClient.postForObject(env.getEmployeeUrl() + "getemployeeDetails", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<HrmsEmployeeModel> employee = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<HrmsEmployeeModel>>() {
					});

			String s = "";

			for (HrmsEmployeeModel m : employee) {
				s = "";
				byte[] encodeId = Base64.getEncoder().encode(m.getEmployeeNo().getBytes());

				if (m.getStatus() != null) {
					if (m.getStatus()) {
						m.setStatusName("Active");
					} else {
						if (m.getEmployeeTerminateDate() != null && m.getEmployeeTerminateDate() != "") {
							m.setStatusName(
									"<span>Inactive<br><b class='viewRed'>Terminated</b><br><b class='viewRed'>("
											+ m.getEmployeeTerminateDate() + ")</b></span>");
						} else {
							m.setStatusName("Inactive");
						}
					}
				}

				s = s + "<a href='view-employee-edit?id=" + new String(encodeId)
						+ "' ><i class=\"fa fa-edit\" title='Edit' style=\"font-size:20px\"></i></a>&nbsp;&nbsp;<a href='javascript:void(0)'"
						+ "' onclick='deleteemployee(\"" + new String(encodeId)
						+ "\")'><i class=\"fa fa-trash\" title='Delete' aria-hidden=\"true\" style=\"font-size:20px\"></i></a>&nbsp;&nbsp;"
						+ "<a title='View' href='view-employee-master-details?id=" + new String(encodeId)
						+ "'><i class='fa fa-search search' style=\"font-size:20px\"></i></a>";

				/*
				 * s = s +
				 * "&nbsp;&nbsp;<a data-toggle='modal' title='Reset Password'  href='javascript:void' onclick='resetPassword(\""
				 * + new String(encodeId) +
				 * "\")'><i class='fa fa-key' style=\"font-size:20px\"></i></a>";
				 */
				m.setAction(s);
				s = "";

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(employee);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.info("Method : viewemployee ajax  ends");

		return response;
	}

	/*
	 * for Edit employee
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-edit")
	public String editemployee(Model model, @RequestParam("id") String encodeId, HttpSession session) {

		logger.info("Method :editemployee starts");

		HrmsEmployeeModel employee = new HrmsEmployeeModel();
		JsonResponse<HrmsEmployeeModel> jsonResponse = new JsonResponse<HrmsEmployeeModel>();

		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));

		try {

			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getemployeeById?id=" + id,
					JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
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
			DropDownModel[] department = restClient.getForObject(env.getEmployeeUrl() + "getDepartmentList",
					DropDownModel[].class);
			List<DropDownModel> DepartmentList = Arrays.asList(department);
			model.addAttribute("DepartmentList", DepartmentList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] region = restClient.getForObject(env.getEmployeeUrl() + "getRegionList",
					DropDownModel[].class);
			List<DropDownModel> RegiontList = Arrays.asList(region);
			model.addAttribute("RegiontList", RegiontList);
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
		 * for designation
		 * 
		 * try { DropDownModel[] design = restClient.getForObject(env.getEmployeeUrl() +
		 * "getDesingnationList", DropDownModel[].class); List<DropDownModel> designList
		 * = Arrays.asList(design); model.addAttribute("designList", designList); }
		 * catch (RestClientException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * for viewing drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getNationalityList",
					DropDownModel[].class);
			List<DropDownModel> nationalityList = Arrays.asList(nationality);
			model.addAttribute("nationalityList", nationalityList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getGenderList",
					DropDownModel[].class);
			List<DropDownModel> genderList = Arrays.asList(nationality);
			model.addAttribute("genderList", genderList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get Country drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getCountryList",
					DropDownModel[].class);
			List<DropDownModel> countryList = Arrays.asList(nationality);
			model.addAttribute("countryList", countryList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get marital status drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getMaritalList",
					DropDownModel[].class);
			List<DropDownModel> maritalList = Arrays.asList(nationality);
			model.addAttribute("maritalList", maritalList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * for get gender drop down list
		 */
		try {
			DropDownModel[] nationality = restClient.getForObject(env.getEmployeeUrl() + "getPayGradeList",
					DropDownModel[].class);
			List<DropDownModel> payGradeList = Arrays.asList(nationality);
			model.addAttribute("payGradeList", payGradeList);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * for get designation drop down list
		 */
		try {
			DropDownModel[] getUserRole = restClient.getForObject(env.getEmployeeUrl() + "getUserRole",
					DropDownModel[].class);
			List<DropDownModel> userRole = Arrays.asList(getUserRole);
			model.addAttribute("userRole", userRole);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		employee = mapper.convertValue(jsonResponse.getBody(), HrmsEmployeeModel.class);

		if (employee != null) {
			try {
				DropDownModel[] state = restClient.getForObject(
						env.getEmployeeUrl() + "getStateforEdit?id=" + employee.getEmployeeCountry(),
						DropDownModel[].class);
				List<DropDownModel> stateList = Arrays.asList(state);
				model.addAttribute("stateList", stateList);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<String> roleList = new ArrayList<String>();
			String[] roles = employee.getEmployeeJobTitle().split(",");

			for (int i = 0; i < roles.length; i++) {
				roleList.add(roles[i]);
			}
			employee.setRoleList(roleList);
			if (roleList.isEmpty()) {
				model.addAttribute("selectedRoles", "");
			} else {
				model.addAttribute("selectedRoles", roleList);
			}
		}

		session.setAttribute("message", "");
		if (employee.getEmployeeImage() == null || employee.getEmployeeImage() == "") {
			employee.setEmployeeImage(null);
		} else {
			String image = employee.getEmployeeImage();
			session.setAttribute("imageNameFromDnForEdit", image);

			// String variable = env.getBaseUrlPath();

			// model.addAttribute("image", variable + "document/employee/" + image + "");

		}

		model.addAttribute("employee", employee);

		logger.info("Method : editemployee ends");

		return "employee/add-employee-form";
	}

	/*
	 * For Delete employee
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-delete")
	public @ResponseBody JsonResponse<Object> getemployeeForDelete(@RequestParam String id, HttpSession session) {

		logger.info("Method : getemployeeForDelete ends");

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
					env.getEmployeeUrl() + "deleteemployee?id=" + id1 + "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteemployee  ends");

		return resp;
	}

	/**
	 * Web Controller - View Details Of Employee modal
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/view-employee-master-details" })
	public String viewEmployeeDetails(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {
		logger.info("Method : viewEmployeeDetails starts");

		String encodeId = encodedParam1;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(decodeId));

		JsonResponse<List<HrmsEmployeeModel>> jsonResponse = new JsonResponse<List<HrmsEmployeeModel>>();

		try {
			jsonResponse = restClient.getForObject(env.getEmployeeUrl() + "getEmployeeByIdForModalView?id=" + id,
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<HrmsEmployeeModel> employee = mapper.convertValue(jsonResponse.getBody(),
				new TypeReference<List<HrmsEmployeeModel>>() {
				});
		for (HrmsEmployeeModel q : employee) {
			if (q.getStatus()) {
				q.setStatusName("Active");
			} else {
				q.setStatusName("Inactive");
			}
		}
		model.addAttribute("image", "/document/employee/" + employee.get(0).getEmployeeImage() + "");
		model.addAttribute("employee", employee);
		logger.info("Method : viewEmployeeDetails ends");
		return "employee/view-employee-modal-details";
	}

	/*
	 * for file upload
	 */
	@PostMapping("/uploadFile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {
			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("employeeImageFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	/*
	 * drop down for supervisor by job title
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/add-employee-jobtitle" }) public @ResponseBody
	 * JsonResponse<DropDownModel> getPropertyType(Model model, @RequestBody String
	 * index, BindingResult result) {
	 * logger.info("Method : get User Role List starts");
	 * 
	 * JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
	 * 
	 * try { res = restClient.getForObject(env.getEmployeeUrl() + "c?deptId=" +
	 * index, JsonResponse.class); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * if (res.getMessage() != null) {
	 * 
	 * res.setCode(res.getMessage()); res.setMessage("Unsuccess"); } else {
	 * res.setMessage("success"); }
	 * 
	 * logger.info("Method : get User Role List starts  ends"); return res; }
	 */
	/*
	 * drop down for supervisor by job title
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/add-employee-state-change" })
	public @ResponseBody JsonResponse<DropDownModel> getStateChange(Model model, @RequestBody String index,
			BindingResult result) {
		logger.info("Method : getStateChange List starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getStateChange?countryId=" + index,
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

		logger.info("Method :getStateChange List starts  ends");
		return res;
	}

	/**
	 * Web Controller - Change The Status Of Employee
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-employee-change-status")
	public @ResponseBody JsonResponse<Object> changeStatus(@RequestParam("id") String encodeId,
			@RequestParam("status") Boolean status, HttpSession session) {

		logger.info("Method : changeStatus ends");

		JsonResponse<Object> resp = new JsonResponse<Object>();
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(decodeId));
		String createdBy = "";
		try {
			createdBy = (String) session.getAttribute("USER_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			resp = restClient.getForObject(env.getEmployeeUrl() + "changeStatusById?id=" + id + "&status=" + status
					+ "&createdBy=" + createdBy, JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : changeStatus ends");

		return resp;
	}
	/*
	 * for Edit employee-appraisal-details
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @PostMapping(value = { "/view-employee-Update-Salary" }) public @ResponseBody
	 * JsonResponse<EmployeeOfferLetterSalaryDetailsModel>
	 * updateEmployeeSalaryDetails(Model model, @RequestBody String index,
	 * BindingResult result) {
	 * logger.info("Method : updateEmployeeSalaryDetails starts");
	 * System.out.println("index" + index);
	 * 
	 * String encodeId = index; byte[] decodeId =
	 * Base64.getDecoder().decode(encodeId.getBytes()); String id = (new
	 * String(decodeId)); System.out.println("id" + id);
	 * 
	 * JsonResponse<EmployeeOfferLetterSalaryDetailsModel> res = new
	 * JsonResponse<EmployeeOfferLetterSalaryDetailsModel>(); ObjectMapper mapper =
	 * new ObjectMapper(); try { res = restClient.getForObject(env.getEmployeeUrl()
	 * + "getEmployeeSalaryDetails?id=" + id, JsonResponse.class);
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * List<EmployeeOfferLetterSalaryDetailsModel> salaryDetails =
	 * mapper.convertValue(res.getBody(), new
	 * TypeReference<List<EmployeeOfferLetterSalaryDetailsModel>>() { }); if
	 * (res.getMessage() != null) { res.setCode(res.getMessage());
	 * res.setMessage("Unsuccess"); } else { res.setMessage("success"); }
	 * model.addAttribute("salaryDetails", salaryDetails);
	 * logger.info("Method : updateEmployeeSalaryDetails ends"); return res; }
	 */
	/*
	 * for Edit employee-salary-components-details
	 * 
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @GetMapping(value = { "/view-employee-update-salary-components" }) public
	 * String updateSalaryComponents(HttpServletResponse
	 * response, @RequestParam("id") String encodedParam1, Model model) {
	 * 
	 * logger.info("Method :updateSalaryComponents starts");
	 * 
	 * String encodeId = encodedParam1; byte[] decodeId =
	 * Base64.getDecoder().decode(encodeId.getBytes()); String id1 = (new
	 * String(decodeId));
	 * 
	 * JsonResponse<EmployeeOfferLetterSalaryDetailsModel> jsonresponse = new
	 * JsonResponse<EmployeeOfferLetterSalaryDetailsModel>();
	 * 
	 * try {
	 * 
	 * jsonresponse = restClient.getForObject(env.getEmployeeUrl() +
	 * "getEmployeeSalaryDetails?id=" + id1, JsonResponse.class); } catch
	 * (RestClientException e) { e.printStackTrace(); }
	 * /////////////////////////////////////////////////////////////////////////////
	 * ///////// List<IncomeTaxModel> incomeTaxList1 = new
	 * ArrayList<IncomeTaxModel>(); try { IncomeTaxModel[] incomeTax =
	 * restClient.getForObject(env.getEmployeeUrl() + "getIncomeTaxList",
	 * IncomeTaxModel[].class); List<IncomeTaxModel> incomeTaxList =
	 * Arrays.asList(incomeTax); model.addAttribute("incomeTaxList", incomeTaxList);
	 * 
	 * incomeTaxList1.addAll(incomeTaxList);
	 * 
	 * } catch (RestClientException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * /////////////////////////////////////////////////////////////////////////////
	 * //////// ObjectMapper mapper = new ObjectMapper();
	 * 
	 * EmployeeOfferLetterSalaryDetailsModel salaryDetails =
	 * mapper.convertValue(jsonresponse.getBody(), new
	 * TypeReference<EmployeeOfferLetterSalaryDetailsModel>() { }); Double ctc =
	 * salaryDetails.gettAnnualCTC();
	 * 
	 * int i = 0; double taxableAmnt = 0.0; double payable = 0.0; for
	 * (IncomeTaxModel q : incomeTaxList1) { Double max = q.gettMaxValue(); Double
	 * min = q.gettMinValue();
	 * 
	 * if (ctc > min && ctc <= max) { double rateId = q.gettTaxRate(); double minId
	 * = q.gettMinValue();
	 * 
	 * taxableAmnt = (rateId / 100 * (ctc - minId));
	 * 
	 * for (int j = i - 1; j < incomeTaxList1.size(); j--) { taxableAmnt =
	 * taxableAmnt + incomeTaxList1.get(j).gettTaxableAmount();
	 * 
	 * if (j == 0) { break; } } }
	 * 
	 * i++; payable = ctc - taxableAmnt; } model.addAttribute("payable", payable);
	 * model.addAttribute("taxableAmnt", taxableAmnt);
	 * model.addAttribute("salaryDetails", salaryDetails);
	 * 
	 * logger.info("Method :updateSalaryComponents ends");
	 * 
	 * return "employee/update-empSalary-component"; }
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-employee-change-password" })
	public @ResponseBody JsonResponse<Object> postChangePassword(Model model, @RequestBody DropDownModel obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : postChangePassword starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] decodeId = Base64.getDecoder().decode(obj.getKey().getBytes());
		String id = (new String(decodeId));

		String userId = "";
		try {
			userId = (String) session.getAttribute("USER_ID");
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String password = "";
		password = obj.getName();

		try {
			if (password != null && password != "") {
				password = passwordEncoder.encode(password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String currentPassword = "";
		try {
			currentPassword = (String) session.getAttribute("USER_PASSWORD");
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		// System.out.println("currentPassword "+currentPassword);

		String confirmpassword = "";
		confirmpassword = obj.getDocName();

		// System.out.println("confirmpassword "+confirmpassword);

		boolean isPasswordMatch = passwordEncoder.matches(confirmpassword, currentPassword);

		// System.out.println(" isPasswordMatch : " + isPasswordMatch);

		/*
		 * String cid = ""; cid = obj.getDocName();
		 * 
		 * try { if (cid != null && cid != "") { cid = passwordEncoder.encode(cid); } }
		 * catch (Exception e) { e.printStackTrace(); }
		 */

		if (isPasswordMatch == true) {
			//System.out.println("hi");
			try {
				/*
				 * res = restClient.getForObject(env.getEmployeeUrl() + "changePassword?id=" +
				 * id + "&oldPassword=" + cid + "&password=" + password, JsonResponse.class);
				 */
				res = restClient.getForObject(
						env.getEmployeeUrl() + "changePassword?id=" + id + "&password=" + password, JsonResponse.class);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (res.getMessage() != null) {
				res.setCode(res.getMessage());
				res.setMessage("Unsuccess");
			} else {
				res.setMessage("success");
				if (userId.contentEquals(id)) {
					session.setAttribute("USER_PASSWORD", password);
				}
			}
		}

		else {
			res.setMessage("Unsuccess");

		}

		logger.info("Method : postChangePassword ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-employee-add-termination-date" })
	public @ResponseBody JsonResponse<Object> addTerminationDate(Model model, @RequestBody DropDownModel obj,
			BindingResult result, HttpSession session) {
		logger.info("Method : addTerminationDate starts");

		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] decodeId = Base64.getDecoder().decode(obj.getKey().getBytes());
		String id = (new String(decodeId));

		try {
			res = restClient.getForObject(
					env.getEmployeeUrl() + "addTerminationDate?id=" + id + "&date=" + obj.getName(),
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

		logger.info("Method : addTerminationDate ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-getEmpNameAutocompleteList" })
	public @ResponseBody JsonResponse<DropDownModel> getItemNameList(Model model, @RequestBody String searchValue,
			BindingResult result) {
		logger.info("Method : getEmpNameList starts");

		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmpNameListByAutoSearch?id=" + searchValue,
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

		logger.info("Method : getEmpNameList ends");
		return res;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-employee-name" })
	public @ResponseBody JsonResponse<DropDownModel> getEmpNameList(Model model, @RequestBody String id,
			BindingResult result) {
		logger.info("Method : getEmpNameList1 starts");
		// System.out.println("Bikash1"+id);
		JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();
		try {
			res = restClient.getForObject(env.getEmployeeUrl() + "getEmpNameByAutoSearch?id=" + id, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}

		logger.info("Method : getEmpNameList1 ends");
		return res;
	}
}
