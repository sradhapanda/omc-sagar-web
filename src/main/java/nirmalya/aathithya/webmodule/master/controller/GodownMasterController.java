//package nirmalya.aathithya.webmodule.master.controller;
//
//import java.awt.Color;
//
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import javax.imageio.ImageIO;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
//import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
//import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
//import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
//import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
//import nirmalya.aathithya.webmodule.master.model.GodownMasterModel;
//
///**
// * @author NirmalyaLabs
// *
// */
//
//@Controller
//@RequestMapping(value = "master")
//public class GodownMasterController {
//	Logger logger = LoggerFactory.getLogger(GodownMasterController.class);
//	@Autowired
//	RestTemplate restTemplate;
//
//	@Autowired
//	EnvironmentVaribles env;
//
//	/**
//	 * View Default 'Add Godown Master' page
//	 *
//	 */
//	@GetMapping(value = { "add-godown-master" })
//	public String addGodownMaster(Model model, HttpSession session) {
//		logger.info("Method : addGodownMaster starts");
//		GodownMasterModel godownMaster = new GodownMasterModel();
//
//		GodownMasterModel godownMasterSession = (GodownMasterModel) session.getAttribute("godownMaster");
//
//		String message = (String) session.getAttribute("message");
//		if (message != null && message != "") {
//			model.addAttribute("message", message);
//		}
//
//		session.setAttribute("message", "");
//
//		if (godownMasterSession != null) {
//			String ImgName = godownMasterSession.gettGodownLogo();
//			String s = "";
//			s = "<a class='example-image-link' href='document/store/" + ImgName + "' title='" + ImgName + "' data-lightbox='"
//					+ ImgName + "'>" + ImgName + "</a>";
//			godownMasterSession.setAction(s);
//			model.addAttribute("godownMaster", godownMasterSession);
//			session.setAttribute("godownMaster", null);
//
//		} else {
//			model.addAttribute("godownMaster", godownMaster);
//		}
//		/*
//		 * dropDown value for Country Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-country-name-list",
//					DropDownModel[].class);
//			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
//			model.addAttribute("countryList", countryList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addGodownMaster ends");
//		return "master/addGodown";
//	}
//
//	/*
//	 * dropDown value for State Name through Ajax
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-godown-master-stateName-ajax" })
//	public @ResponseBody JsonResponse<Object> getGodownStateName(Model model, @RequestBody String tCountry,
//			BindingResult result) {
//		logger.info("Method : getGodownStateName starts");
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "get-state-name-list?id=" + tCountry,
//					JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : getGodownStateName ends");
//		return res;
//
//	}
//
//	/*
//	 * dropDown value for District Name through Ajax
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-godown-master-districtName-ajax" })
//	public @ResponseBody JsonResponse<Object> getGodownDistrictName(Model model, @RequestBody String tDistrict,
//			BindingResult result) {
//		logger.info("Method : getGodownDistrictName starts");
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "get-district-name-list?id=" + tDistrict,
//					JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : getGodownDistrictName ends");
//		return res;
//
//	}
//
//	/**
//	 * Add Godown Master Form Post
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-godown-master" })
//	public String addNewGodown(@ModelAttribute GodownMasterModel godownMaster, Model model, HttpSession session) {
//		logger.info("Method : addNewGodown starts");
//		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
//		String imageName = null;
//		try {
//			byte[] bytes = (byte[]) session.getAttribute("logoFile");
//			if (bytes != null) {
//
//				long nowTime = new Date().getTime();
//				imageName = nowTime + ".png";
//				godownMaster.settGodownLogo(imageName);
//			} else {
//				imageName = (String) session.getAttribute("logoImageNameForEdit");
//
//				godownMaster.settGodownLogo(imageName);
//			}
//			// godownMaster.setmTaskCreatedBy("u0001");
//			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "restAddNewGodown", godownMaster,
//					JsonResponse.class);
//			System.out.println("godownMaster" + godownMaster);
//			if (jsonResponse.getCode().contains("Data Saved Successfully")) {
//
//				Path path = Paths.get(env.getFileUploadStoreUrl() + imageName);
//				if (bytes != null) {
//					try {
//						Files.write(path, bytes);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					ByteArrayInputStream in = new ByteArrayInputStream(bytes);
//
//					Integer height = 50;
//					Integer width = 50;
//
//					try {
//						BufferedImage img = ImageIO.read(in);
//						if (height == 0) {
//							height = (width * img.getHeight()) / img.getWidth();
//						}
//						if (width == 0) {
//							width = (height * img.getWidth()) / img.getHeight();
//						}
//
//						Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//						BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//						imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
//
//						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//
//						ImageIO.write(imageBuff, "jpg", buffer);
//
//						// ByteArrayOutputStream out = new
//						// ByteArrayOutputStream();
//						byte[] thumb = buffer.toByteArray();
//						System.out.println(env.getFileUploadStoreUrl() + "thumb\\" + imageName);
//						Path pathThumb = Paths.get(env.getFileUploadStoreUrl() + "thumb\\" + imageName);
//						Files.write(pathThumb, thumb);
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		if (jsonResponse.getMessage() != "") {
//			session.setAttribute("message", jsonResponse.getMessage());
//			session.setAttribute("godownMaster", godownMaster);
//			return "redirect:add-godown-master";
//		}
//
//		session.removeAttribute("logoFile");
//		try {
//			TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("Method : addNewLogo controller function 'post-mapping' ends");
//		logger.info("Method : addNewGodown ends");
//		return "redirect:view-godown-master";
//	}
//
//	/*
//	 * Upload File
//	 * 
//	 */
//	@PostMapping("/add-godown-master-uploadGodown")
//	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
//			HttpSession session) {
//		logger.info("Method : uploadFile controller function 'post-mapping' starts");
//
//		JsonResponse<Object> response = new JsonResponse<Object>();
//
//		try {
//
//			response.setMessage(inputFile.getOriginalFilename());
//			session.setAttribute("logoFile", inputFile.getBytes());
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : uploadFile controller function 'post-mapping' ends");
//		return response;
//	}
//
//	/*
//	 * View Default 'view All Godown Details' page
//	 *
//	 */
//	@GetMapping(value = { "view-godown-master" })
//	public String viewGodownMaster(Model model) {
//		logger.info("Method : viewGodownMaster starts");
//		/*
//		 * dropDown value for Godown Name search param
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-godown-name",
//					DropDownModel[].class);
//			List<DropDownModel> godownList = Arrays.asList(dropDownModel);
//			model.addAttribute("godownList", godownList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		/*
//		 * dropDown value for State Name search param
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-state-name-param",
//					DropDownModel[].class);
//			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
//			model.addAttribute("stateList", stateList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : viewGodownMaster ends");
//		return "master/viewGodown";
//	}
//
//	/*
//	 * view All Godown Details 'Datatable' call
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-godown-master-throughAjax")
//	public @ResponseBody DataTableResponse viewGodownMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1,@RequestParam String param2) {
//		logger.info("Method : viewGodownMasterThroughAjax starts");
//		DataTableResponse response = new DataTableResponse();
//		DataTableRequest tableRequest = new DataTableRequest();
//		try {
//			String start = request.getParameter("start");
//			String length = request.getParameter("length");
//			String draw = request.getParameter("draw");
//
//			tableRequest.setStart(Integer.parseInt(start));
//			tableRequest.setLength(Integer.parseInt(length));
//			tableRequest.setParam1(param1);
//			tableRequest.setParam2(param2);
//
//			JsonResponse<List<GodownMasterModel>> jsonResponse = new JsonResponse<List<GodownMasterModel>>();
//
//			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllGodownDetails", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<GodownMasterModel> godownMaster = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<GodownMasterModel>>() {
//					});
//
//			String s = "";
//			for (GodownMasterModel m : godownMaster) {
//
//				byte[] pId = Base64.getEncoder().encode(m.gettGodown().getBytes());
//
//				s = "";
//				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
//						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
//				s = s + "<a  title='Edit' href='view-godown-master-edit?id=" + new String(pId)
//						+ "' '><i class='fa fa-edit edit'></i></a>";
//				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
//						+ "\")'><i class='fa fa-trash'></i></a> ";
//
//				m.setAction(s);
//				s = "";
//				s = "<a class='example-image-link' href='/document/store/" + m.gettGodownLogo() + "' title='"
//						+ m.gettGodownLogo() + "' data-lightbox='" + m.gettGodownLogo() + "'>"
//						+ "<img src='/document/store/thumb/" + m.gettGodownLogo() + "'/>" + "</a>";
//				m.settGodownLogo(s);
//
//			}
//
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(godownMaster);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : viewGodownMasterThroughAjax ends");
//		return response;
//	}
//
//	/*
//	 * View selected Godown Details in Modal
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping("/view-godown-master-model")
//	public @ResponseBody JsonResponse<Object> viewGodownDetailsModal(Model model, HttpSession session,
//			@RequestBody String encodedIndex) {
//		logger.info("Method : viewGodownDetailsModal starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
//		String id = new String(encodeByte);
//		JsonResponse<Object> res = new JsonResponse<Object>();
//
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "viewGodownDetailsModal?id=" + id, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : viewGodownDetailsModal ends");
//		return res;
//	}
//
//	/*
//	 * Delete selected Godown Details in Modal
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-godown-master-delete" })
//	public @ResponseBody JsonResponse<Object> deleteGodownDetails(Model model, HttpSession session,
//			@RequestParam("id") String encodedIndex) {
//
//		logger.info("Method : deleteGodownDetails starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
//		String id = new String(encodeByte);
//		// String createdBy = "u0002";
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//
//		try {
//			resp = restTemplate.getForObject(env.getMasterUrl() + "deleteGodownDetails?id=" + id, JsonResponse.class);
//
//		} catch (RestClientException e) {
//
//			e.printStackTrace();
//		}
//
//		if (resp.getMessage() != null && resp.getMessage() != "") {
//
//			resp.setCode(resp.getMessage());
//			resp.setMessage("Unsuccess");
//		} else {
//			resp.setMessage("success");
//		}
//		logger.info("Method : deleteGodownDetails ends");
//		return resp;
//	}
//
//	/*
//	 * Edit Selected Godown
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-godown-master-edit")
//	public String editGodownDetails(Model model, @RequestParam("id") String encodeId, HttpSession session) {
//		logger.info("Method : editGodownDetails starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
//		String id = (new String(encodeByte));
//		GodownMasterModel godownMaster = new GodownMasterModel();
//		JsonResponse<GodownMasterModel> jsonResponse = new JsonResponse<GodownMasterModel>();
//
//		try {
//			jsonResponse = restTemplate.getForObject(env.getMasterUrl() + "editGodownDetails?id=" + id,
//					JsonResponse.class);
//
//		} catch (RestClientException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		String message = (String) session.getAttribute("message");
//
//		if (message != null && message != "") {
//			model.addAttribute("message", message);
//		}
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		godownMaster = mapper.convertValue(jsonResponse.getBody(), GodownMasterModel.class);
//		session.setAttribute("message", "");
//		String ImgName = godownMaster.gettGodownLogo();
//		if (ImgName != null || ImgName != "") {
//			String s = "";
//			s = "/document/store/" + ImgName;
//			godownMaster.setAction(s);
//			session.setAttribute("logoImageNameForEdit", godownMaster.gettGodownLogo());
//		}
//		model.addAttribute("godownMaster", godownMaster);
//		/*
//		 * dropDown value for Country Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-country-name-list",
//					DropDownModel[].class);
//			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
//			model.addAttribute("countryList", countryList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		/*
//		 * dropDown value for State Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(
//					env.getMasterUrl() + "get-state-name-edit?id=" + godownMaster.gettCountry(), DropDownModel[].class);
//			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
//			model.addAttribute("stateList", stateList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		/*
//		 * dropDown value for District Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(
//					env.getMasterUrl() + "rest-get-distName-edit?id=" + godownMaster.gettState(),
//					DropDownModel[].class);
//			List<DropDownModel> districtList = Arrays.asList(dropDownModel);
//			model.addAttribute("districtList", districtList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : editGodownDetails end");
//		return "master/addGodown";
//	}
//}
