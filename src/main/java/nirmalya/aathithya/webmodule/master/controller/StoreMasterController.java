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
//import nirmalya.aathithya.webmodule.master.model.StoreMasterModel;
//
///**
// * @author NirmalyaLabs
// *
// */
//
//@Controller
//@RequestMapping(value = "master")
//public class StoreMasterController {
//	Logger logger = LoggerFactory.getLogger(StoreMasterController.class);
//	@Autowired
//	RestTemplate restTemplate;
//
//	@Autowired
//	EnvironmentVaribles env;
//
//	/**
//	 * View Default 'Add Service Master' page
//	 *
//	 */
//	@GetMapping(value = { "add-store-master" })
//	public String addStoreMaster(Model model, HttpSession session) {
//		logger.info("Method : addStoreMaster starts");
//		StoreMasterModel storeMaster = new StoreMasterModel();
//
//		StoreMasterModel storeMasterSession = (StoreMasterModel) session.getAttribute("storeMaster");
//
//		String message = (String) session.getAttribute("message");
//		if (message != null && message != "") {
//			model.addAttribute("message", message);
//		}
//
//		session.setAttribute("message", "");
//
//		if (storeMasterSession != null) {
//			String ImgName = storeMasterSession.gettStoreLogo();
//			String s = "";
//			s = "<a class='example-image-link' href='/document/store/" + ImgName + "' title='" + ImgName + "' data-lightbox='"
//					+ ImgName + "'>" + ImgName + "</a>";
//			storeMasterSession.setAction(s);
//			model.addAttribute("storeMaster", storeMasterSession);
//			session.setAttribute("storeMaster", null);
//
//		} else {
//			model.addAttribute("storeMaster", storeMaster);
//		}
//		/*
//		 * dropDown value for Country Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-country-name",
//					DropDownModel[].class);
//			List<DropDownModel> countryList = Arrays.asList(dropDownModel);
//			model.addAttribute("countryList", countryList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//
//		logger.info("Method : addStoreMaster ends");
//		return "master/addStore";
//	}
//	/*
//	 * dropDown value for State Name through Ajax
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-store-master-stateName-ajax" })
//	public @ResponseBody JsonResponse<Object> getStoreStateName(Model model, @RequestBody String tCountry,
//			BindingResult result) {
//		logger.info("Method : getStateNameAJAX starts");
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "get-state-name?id=" + tCountry, JsonResponse.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : getStateNameAJAX ends");
//		return res;
//
//	}
//
//	/*
//	 * dropDown value for District Name through Ajax
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-store-master-districtName-ajax" })
//	public @ResponseBody JsonResponse<Object> getStoreDistrictName(Model model, @RequestBody String tDistrict,
//			BindingResult result) {
//		logger.info("Method : getdistrictNameAJAX starts");
//		JsonResponse<Object> res = new JsonResponse<Object>();
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "get-district-name?id=" + tDistrict,
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
//		logger.info("Method : getdistrictNameAJAX ends");
//		return res;
//
//	}
//
//	/**
//	 * Add Store Master Form Post
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@PostMapping(value = { "add-store-master" })
//	public String addNewStore(@ModelAttribute StoreMasterModel storeMaster, Model model, HttpSession session) {
//		logger.info("Method : addNewStore starts");
//		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
//		String imageName = null;
//		try {
//			byte[] bytes = (byte[]) session.getAttribute("logoFile");
//			if (bytes != null) {
//
//				long nowTime = new Date().getTime();
//				imageName = nowTime + ".png";
//				storeMaster.settStoreLogo(imageName);
//			} else {
//				imageName = (String) session.getAttribute("logoImageNameForEdit");
//
//				storeMaster.settStoreLogo(imageName);
//			}
//			// storeMaster.setmTaskCreatedBy("u0001");
//			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "restAddNewStore", storeMaster,
//					JsonResponse.class);
//			System.out.println("storeMaster" + storeMaster);
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
//			session.setAttribute("storeMaster", storeMaster);
//			return "redirect:add-store-master";
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
//		logger.info("Method : addNewStore ends");
//		return "redirect:view-store-master";
//	}
//
//	/*
//	 * Upload File
//	 * 
//	 */
//	@PostMapping("/add-store-master-uploadStore")
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
//	 * View Default 'view All Store Details' page
//	 *
//	 */
//	@GetMapping(value = { "view-store-master" })
//	public String viewStoreMaster(Model model) {
//		logger.info("Method : viewStoreMaster starts");
//		/*
//		 * dropDown value for Store Name search param
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-store-name",
//					DropDownModel[].class);
//			List<DropDownModel> storeList = Arrays.asList(dropDownModel);
//			model.addAttribute("storeList", storeList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		
//		/*
//		 * dropDown value for State Name search param
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-state-search-param",
//					DropDownModel[].class);
//			List<DropDownModel> stateList = Arrays.asList(dropDownModel);
//			model.addAttribute("stateList", stateList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : viewStoreMaster ends");
//		return "master/viewStore";
//	}
//
//	/*
//	 * view All Store Details 'Datatable' call
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-store-master-throughAjax")
//	public @ResponseBody DataTableResponse viewStoreMasterThroughAjax(Model model, HttpServletRequest request,
//			@RequestParam String param1,@RequestParam String param2) {
//		logger.info("Method : viewStoreMasterThroughAjax starts");
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
//			JsonResponse<List<StoreMasterModel>> jsonResponse = new JsonResponse<List<StoreMasterModel>>();
//
//			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllStoreDetails", tableRequest,
//					JsonResponse.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//
//			List<StoreMasterModel> storeMaster = mapper.convertValue(jsonResponse.getBody(),
//					new TypeReference<List<StoreMasterModel>>() {
//					});
//
//			String s = "";
//			for (StoreMasterModel m : storeMaster) {
//
//				byte[] pId = Base64.getEncoder().encode(m.gettStore().getBytes());
//
//				;
//
//				s = "";
//				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
//						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
//				s = s + "<a  title='Edit' href='view-store-master-edit?id=" + new String(pId)
//						+ "' '><i class='fa fa-edit edit'></i></a>";
//				s = s + "<a href='javascript:void(0)' onclick='deleteItem(\"" + new String(pId)
//						+ "\")'><i class='fa fa-trash'></i></a> ";
//
//				m.setAction(s);
//				s = "";
//				s = "<a class='example-image-link' href='/document/store/" + m.gettStoreLogo() + "' title='" + m.gettStoreLogo()
//						+ "' data-lightbox='" + m.gettStoreLogo() + "'>" + "<img src='/document/store/thumb/" + m.gettStoreLogo()
//						+ "'/>" + "</a>";
//				m.settStoreLogo(s);
//
//			}
//
//			response.setRecordsTotal(jsonResponse.getTotal());
//			response.setRecordsFiltered(jsonResponse.getTotal());
//			response.setDraw(Integer.parseInt(draw));
//			response.setData(storeMaster);
//
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : viewStoreMasterThroughAjax ends");
//		return response;
//	}
//
//	/*
//	 * View selected Store Details in Modal
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@PostMapping("/view-store-master-model")
//	public @ResponseBody JsonResponse<Object> viewStoreDetailsModal(Model model, HttpSession session,
//			@RequestBody String encodedIndex) {
//		logger.info("Method : viewStoreDetailsModal starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
//		String id = new String(encodeByte);
//		JsonResponse<Object> res = new JsonResponse<Object>();
//
//		try {
//			res = restTemplate.getForObject(env.getMasterUrl() + "viewStoreDetailsModal?id=" + id, JsonResponse.class);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		if (res.getMessage() != null) {
//			res.setCode(res.getMessage());
//			res.setMessage("Unsuccess");
//		} else {
//			res.setMessage("success");
//		}
//		logger.info("Method : viewStoreDetailsModal ends");
//		return res;
//	}
//
//	/*
//	 * Delete selected Store Details in Modal
//	 *
//	 */
//
//	@SuppressWarnings("unchecked")
//	@GetMapping(value = { "view-store-master-delete" })
//	public @ResponseBody JsonResponse<Object> deleteStoreDetails(Model model, HttpSession session,
//			@RequestParam("id") String encodedIndex) {
//
//		logger.info("Method : deleteStoreDetails starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
//		String id = new String(encodeByte);
//		// String createdBy = "u0002";
//		JsonResponse<Object> resp = new JsonResponse<Object>();
//
//		try {
//			resp = restTemplate.getForObject(env.getMasterUrl() + "deleteStoreDetails?id=" + id, JsonResponse.class);
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
//		logger.info("Method : deleteStoreDetails ends");
//		return resp;
//	}
//
//	/*
//	 * Edit Selected Store
//	 *
//	 */
//	@SuppressWarnings("unchecked")
//	@GetMapping("/view-store-master-edit")
//	public String editStoreDetails(Model model, @RequestParam("id") String encodeId, HttpSession session) {
//		logger.info("Method : editStoreDetails starts");
//		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
//		String id = (new String(encodeByte));
//		StoreMasterModel storeMaster = new StoreMasterModel();
//		JsonResponse<StoreMasterModel> jsonResponse = new JsonResponse<StoreMasterModel>();
//
//		try {
//			jsonResponse = restTemplate.getForObject(env.getMasterUrl() + "editStoreDetails?id=" + id,
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
//		storeMaster = mapper.convertValue(jsonResponse.getBody(), StoreMasterModel.class);
//		session.setAttribute("message", "");
//		String ImgName = storeMaster.gettStoreLogo();
//		if (ImgName != null || ImgName != "") {
//			String s = "";
//			s = "/document/store/" + ImgName;
//			storeMaster.setAction(s);
//			session.setAttribute("logoImageNameForEdit", storeMaster.gettStoreLogo());
//		}
//		model.addAttribute("storeMaster", storeMaster);
//		/*
//		 * dropDown value for Country Name
//		 */
//		try {
//			DropDownModel[] dropDownModel = restTemplate.getForObject(env.getMasterUrl() + "get-country-name",
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
//					env.getMasterUrl() + "get-state-name-editList?id=" + storeMaster.gettCountry(),
//					DropDownModel[].class);
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
//					env.getMasterUrl() + "rest-get-distName-editList?id=" + storeMaster.gettState(),
//					DropDownModel[].class);
//			List<DropDownModel> districtList = Arrays.asList(dropDownModel);
//			model.addAttribute("districtList", districtList);
//		} catch (RestClientException e) {
//			e.printStackTrace();
//		}
//		logger.info("Method : editStoreDetails end");
//		return "master/addStore";
//	}
//}
