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
import nirmalya.aathithya.webmodule.master.model.RelationModel;


@Controller
@RequestMapping(value = "master")
public class RelationController {
	Logger logger = LoggerFactory.getLogger(RelationController.class);
	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * GetMapping for add relation
	 */
	@GetMapping("/add-relation")
	public String addRelation(Model model, HttpSession session) {
		logger.info("Method : addRelation starts");
		RelationModel relation = new RelationModel();
		RelationModel form = (RelationModel) session.getAttribute("srelation");
		String message = (String) session.getAttribute("message");
		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		session.setAttribute("message", "");

		if (form != null) {
			model.addAttribute("relation", form);
			session.setAttribute("srelation", null);

		} else {
			model.addAttribute("relation", relation);
		}
		logger.info("Method : addRelation end");
		return "master/addRelation";
	}
	/*
	 * post mapping for adding relation
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/add-relation")
	public String postRelation(@ModelAttribute RelationModel relation, Model model, HttpSession session) {
		logger.info("Method : postRelation starts");
		String userId = null;
		try {
			userId = (String)session.getAttribute("USER_ID");
		}catch(Exception e) {
			e.printStackTrace();
		}
		JsonResponse<Object> resp = new JsonResponse<Object>();
		try {
			relation.setRltnCreatedBy(userId);
			resp = restClient.postForObject(env.getMasterUrl() + "addRelation", relation, JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resp.getMessage() != "") {
			session.setAttribute("message", resp.getMessage());
			session.setAttribute("srelation", relation);
			return "redirect:/user/add-relation";
		}
		session.setAttribute("srelation", null);
		logger.info("Method : postRelation end");
		return "redirect:/user/view-relation";
	}
	/**
	 * get mapping for view payment mode master
	 */

	@GetMapping("/view-relation")
	public String viewRelation(Model model) {
		logger.info("Method : viewRelation starts");


		logger.info("Method : viewRelation end");
		return "master/listRelation";

	}
	/**
	 * get mapping for view  Relation through ajax
	 */

	@SuppressWarnings("unchecked")
	@GetMapping("/view-relation-throughajax")
	public @ResponseBody DataTableResponse viewRelationThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewRelationThroughAjax starts");

		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();

		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<RelationModel>> jsonResponse = new JsonResponse<List<RelationModel>>();
			jsonResponse = restClient.postForObject(env.getMasterUrl() + "getAllRelation", tableRequest,
					JsonResponse.class);
			//System.out.println("json+++++++++++++++++++++" + jsonResponse);
			ObjectMapper mapper = new ObjectMapper();

			List<RelationModel> form = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<RelationModel>>() {
					});

			String s = "";
			for (RelationModel m : form) {
				byte[] pId = Base64.getEncoder().encode(m.getRelationId().getBytes());
				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModelData(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "  &nbsp;&nbsp; <a href= 'javascript:void(0)' "
						+ "' onclick='deleteRelation(\"" + new String(pId)
						+ "\")' ><i class=\"fa fa-trash\"></i></a> ";
				
				

				if (m.getRltnActive()) {
					m.setRltnStatus("Active");
				} else {
					m.setRltnStatus("Inactive");
				}
				//System.out.println(m.getIsEditable());
				if(m.getIsEditable())
				{
					s = s + " &nbsp;&nbsp <a href='view-relation-edit?id=" +  new String(pId)
						+ "' ><i class=\"fa fa-edit\"></i></a> ";
				}
				else
				{
					s = s + " &nbsp;&nbsp <a href='view-relation-edit?id=" +  new String(pId)
							+ "' ><i class=\"icon-pencil-slash\"></i></a> ";
				}
				m.setAction(s);
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
		logger.info("Method : viewRelationThroughAjax end");
		return response;
	}
	/**
	 * get mapping for edit relation
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-relation-edit")
	public String editRelation(Model model,  @RequestParam("id") String encodeId, HttpSession session) {
		logger.info("Method : editRelation starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodeId.getBytes());
		String id = (new String(encodeByte));
		RelationModel relation = new RelationModel();
		JsonResponse<RelationModel> jsonResponse = new JsonResponse<RelationModel>();

		try {
			jsonResponse = restClient.getForObject(env.getMasterUrl() + "geRelationById?id=" + id + "&Action=editRelation", JsonResponse.class);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String message = (String) session.getAttribute("message");

		if (message != null && message != "") {
			model.addAttribute("message", message);
		}

		ObjectMapper mapper = new ObjectMapper();

		relation = mapper.convertValue(jsonResponse.getBody(), RelationModel.class);
		session.setAttribute("message", "");

		model.addAttribute("relation", relation);

		logger.info("Method : editRelation end");
		return "user/addRelation";
	}
	/**
	 * post Mapping for viewInModelData in relation
	 * 
	 */

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "/view-relation-modeldata" })
	public @ResponseBody JsonResponse<Object> modelView(Model model, @RequestBody String index, BindingResult result) {
		logger.info("Method : modelView starts");
		JsonResponse<Object> res = new JsonResponse<Object>();
		byte[] encodeByte = Base64.getDecoder().decode(index.getBytes());
		String id = (new String(encodeByte));

		try {
			res = restClient.getForObject(
					env.getMasterUrl() + "geRelationById?id=" + id + "&Action=" + "ModelViewRltn",
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
	/**
	 * GetMapping for delete relation
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-relation-delete")
	public @ResponseBody JsonResponse<Object> deleteRelation(@RequestParam String id, HttpSession session) {
		logger.info("Method : deleteRelation starts");

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
					env.getMasterUrl() + "deleteRltnById?id=" + id1 + "&createdBy=" + userId,
					JsonResponse.class);

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
		logger.info("Method : deleteRelation ends");

		return resp;
	}
}
