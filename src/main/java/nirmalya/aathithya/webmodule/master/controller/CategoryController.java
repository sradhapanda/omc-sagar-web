/**
 * 
 */
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
import nirmalya.aathithya.webmodule.master.model.CategoryModel;

/**
 * @author NirmalyaLabs
 *
 */
@Controller
@RequestMapping(value = {"master"})
public class CategoryController {
	Logger logger = LoggerFactory.getLogger(CategoryController.class);
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	EnvironmentVaribles environmentVaribles;
	/*
	 * GetMapping for Adding new Category
	 *
	 */
	@GetMapping(value = { "add-category" })
	public String addCategory(Model model, HttpSession session) {
	logger.info("Method : add Category starts");
	CategoryModel categoryModel = new CategoryModel();
	CategoryModel mcount = (CategoryModel) session.getAttribute("categoryModel");
	String message = (String) session.getAttribute("message");
		
	if (message != null && message != "") {
		model.addAttribute("message", message);
	}
	session.setAttribute("message", "");

	if (mcount != null) {
		model.addAttribute("categoryModel", mcount);
	} else {
		model.addAttribute("categoryModel", categoryModel);
	}
	logger.info("Method : add Category ends");
	return "master/addCategory";
	}
	/*
	 * post Mapping for add category
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "add-category" })
	public String addNewCategory(@ModelAttribute CategoryModel categoryModel, Model model, HttpSession session) {
		logger.info("Method : addNewCategory starts");
		JsonResponse<Object> jsonResponse = new JsonResponse<Object>();
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			categoryModel.setCreatedBy(userId);
			jsonResponse = restTemplate.postForObject(
					environmentVaribles.getMasterUrl()+ "rest-addnew-category", categoryModel,JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		if (jsonResponse.getMessage() != "") {
			session.setAttribute("message", jsonResponse.getMessage());
			session.setAttribute("categoryModel", categoryModel);
			return "redirect:add-category";
		}
		logger.info("Method : add New Category ends");
		return "redirect:view-category";
	}
	/*
	 * 
	 * GetMApping For Listing Country
	 * 
	 * 
	 */
	@GetMapping(value = { "view-category" })
	public String viewCategory(Model model) {
		logger.info("Method : view Category starts");
		JsonResponse<Object> category = new JsonResponse<Object>();
		model.addAttribute("category", category);
		logger.info("Method : view Category ends");
		return "master/viewCategory";
	}
	/*
	 * view Through ajax
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "view-category-throughAjax" })
	public @ResponseBody DataTableResponse viewCategory(Model model, HttpServletRequest request,
			@RequestParam String param1 ) {
		logger.info("Method : view Category (through ajax) starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");
			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);
			JsonResponse<List<CategoryModel>> jsonResponse = new JsonResponse<List<CategoryModel>>();
			jsonResponse = restTemplate.postForObject(environmentVaribles.getMasterUrl() + "get-all-category",
					tableRequest, JsonResponse.class);
			ObjectMapper mapper = new ObjectMapper();
			List<CategoryModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<CategoryModel>>() {
					});
			String s = "";
			for (CategoryModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getCategory().getBytes());

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+  new String(pId) + "\")'><i class='fa fa-search search' style='font-size:20px'></i></a>";
				s = s + " &nbsp;&nbsp <a href='edit-category?id=" + new String(pId)
						+ "' ><i class='fa fa-edit' style='font-size:20px'></i></a> &nbsp;&nbsp; ";
				s = s + "<a href='javascript:void(0)' onclick='deleteCategory(\"" +  new String(pId)
						+ "\")'><i class='fa fa-trash' style='font-size:20px'></i></a> ";
				m.setAction(s);
				s = "";

				
			}
			
			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(form);

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method : view Category (through ajax) ends");
		return response;
	}
	/**
	 * View selected Country in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-category-model" })
	public @ResponseBody JsonResponse<CategoryModel> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		
		JsonResponse<CategoryModel> resp = new JsonResponse<CategoryModel>();
		
		try {
			resp = restTemplate.getForObject(
					environmentVaribles.getMasterUrl() + "get-category-byId?id=" + index, JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resp.getMessage() != null) {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		
		logger.info("Method : modelView ends");
		return resp;
	}

	/*
	 * 
	 * GetMapping for delete Country
	 * 
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "delete-category" })
	public @ResponseBody JsonResponse<Object> deleteCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		
		logger.info("Method : deleteCategory starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
	
		try {
			resp = restTemplate.getForObject(environmentVaribles.getMasterUrl() + "delete-category?id=" + id+"&createdBy="+userId, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		if (resp.getMessage() != null && resp.getMessage() != "") {
			resp.setCode(resp.getMessage());
			resp.setMessage("Unsuccess");
		} else {
			resp.setMessage("success");
		}
		logger.info("Method : deleteCategory ends");
		return resp;
	}
	/*
	 * 
	 * 
	 * GetMApping for Edit Country
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = { "edit-category" })
	public String editCategory(Model model, @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editCategory starts");

		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());

		String id = (new String(encodeByte));
		CategoryModel categoryModel = new CategoryModel();
		JsonResponse<CategoryModel> jsonResponse = new JsonResponse<CategoryModel>();

		try {
			
			jsonResponse = restTemplate.getForObject(environmentVaribles.getMasterUrl() + "get-category-byId?id=" + id, JsonResponse.class);
		
		} catch (RestClientException e) {

			e.printStackTrace();
		}
		
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}
		ObjectMapper mapper = new ObjectMapper();
		categoryModel = mapper.convertValue(jsonResponse.getBody(), CategoryModel.class);
		session.setAttribute("message", "");
		model.addAttribute("categoryModel", categoryModel);
		logger.info("Method : editCategory ends");
		return "master/addCategory";
	}
	
}
