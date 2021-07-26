
package nirmalya.aathithya.webmodule.user.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.DropDownModel;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.UserMemberDataRegistrationModel;
import nirmalya.aathithya.webmodule.user.model.UserMemberDepCountModel;
import nirmalya.aathithya.webmodule.user.model.UserMembershipRegistrationModel;


/**
 * @author Nirmalya Labs
 *
 */


@Controller
@RequestMapping(value = "user/")
public class UserMemberShipRegistrationController {
	
	Logger logger = LoggerFactory.getLogger(UserMemberShipRegistrationController.class);

	@Autowired
	RestTemplate restClient;

	@Autowired
	EnvironmentVaribles env;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/*
	 * 
	 *  'Add Membership Registration' Page
	 *
	 */
	
	@SuppressWarnings("unchecked")
	@GetMapping("/add-membership-registration")
	public String addMembershipRegistration(Model model, HttpSession session) {
	
		logger.info("Method : addMembershipRegistration starts");
 
		UserMembershipRegistrationModel tableSession = (UserMembershipRegistrationModel) session.getAttribute("smembership");
		 
		UserMembershipRegistrationModel membership= new UserMembershipRegistrationModel();
		try {
			
	
			String message = (String) session.getAttribute("message");
	
			if (message != null && message != "") {
				model.addAttribute("message", message);
				
			}
	
			session.setAttribute("message", "");
			if (tableSession != null) {
				model.addAttribute("membership",tableSession);
				session.setAttribute("membership", null);
			} else {
				model.addAttribute("membership", membership);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//start membertype drop down
		JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> memberData = new ArrayList<DropDownModel>();
		try {
			respTblMstr = restClient.getForObject(env.getUserUrl() + "getMemberData?Action=" + "getMemberData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstr = respTblMstr.getMessage();

		if (messageForTblMstr != null || messageForTblMstr != "") {
			model.addAttribute("message", messageForTblMstr);
		}

		ObjectMapper mapper = new ObjectMapper();

		 
		memberData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("memberData", memberData);
		
		//End memberType drop down
		
        //start paymentModeData drop down
		JsonResponse<List<DropDownModel>> respTblMstrpay = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> paymentModeData = new ArrayList<DropDownModel>();
		try {
			respTblMstrpay = restClient.getForObject(env.getUserUrl() + "getPaymentModeData?Action=" + "getMemberData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstrpay = respTblMstrpay.getMessage();

		if (messageForTblMstrpay != null || messageForTblMstrpay != "") {
			model.addAttribute("message", messageForTblMstrpay);
		}

		ObjectMapper mapper3 = new ObjectMapper();
		 
		paymentModeData = mapper3.convertValue(respTblMstrpay.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("paymentModeData", paymentModeData);
		
		//End paymentModeData drop down
		
		
		
		//start relation drop down
		JsonResponse<List<DropDownModel>> respTblMstrRel = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> relationData = new ArrayList<DropDownModel>();
		try {
			respTblMstrRel = restClient.getForObject(env.getUserUrl() + "getRelationData?Action=" + "getRelationData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstrRel = respTblMstrRel.getMessage();

		if (messageForTblMstrRel != null || messageForTblMstrRel != "") {
			model.addAttribute("message", messageForTblMstrRel);
		}

		ObjectMapper mapper4 = new ObjectMapper();
		 
		relationData = mapper4.convertValue(respTblMstrRel.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("relationData", relationData);
		
		//End relationData drop down
		
		
		//start relation drop down self only
		JsonResponse<List<DropDownModel>> respTblMstrRelself = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> relationselfData = new ArrayList<DropDownModel>();
		try {
			respTblMstrRelself = restClient.getForObject(env.getUserUrl() + "getRelationSelfData?Action=" + "getRelationSelfData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstrRelself = respTblMstrRelself.getMessage();

		if (messageForTblMstrRelself != null || messageForTblMstrRelself != "") {
			model.addAttribute("message", messageForTblMstrRelself);
		}

		ObjectMapper mapper9 = new ObjectMapper();
		 
		relationselfData = mapper9.convertValue(respTblMstrRelself.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("relationselfData", relationselfData);
		
		//End relationData self only drop down
		
		//start user type drop down
		JsonResponse<List<DropDownModel>> respTblMstrUType = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> UTypedata = new ArrayList<DropDownModel>();
		try {
			respTblMstrUType = restClient.getForObject(env.getUserUrl() + "getUTypeData?Action=" + "getUTypeData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstrUType = respTblMstrUType.getMessage();

		if (messageForTblMstrUType != null || messageForTblMstrUType != "") {
			model.addAttribute("message", messageForTblMstrUType);
		}

		ObjectMapper mapper6 = new ObjectMapper();
		 
		UTypedata = mapper6.convertValue(respTblMstrUType.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("UTypedata", UTypedata);
		
		//End user type drop down
		
		
		//start state drop down
		JsonResponse<List<DropDownModel>> respTblMstrState = new JsonResponse<List<DropDownModel>>();
		List<DropDownModel> stateData = new ArrayList<DropDownModel>();
		try {
			respTblMstrState = restClient.getForObject(env.getUserUrl() + "getStateData?Action=" + "getStateData",
					JsonResponse.class);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		String messageForTblMstrState = respTblMstrState.getMessage();

		if (messageForTblMstrState != null || messageForTblMstrState != "") {
			model.addAttribute("message", messageForTblMstrState);
		}

		ObjectMapper mapper2 = new ObjectMapper();

		 
		stateData = mapper2.convertValue(respTblMstrState.getBody(), new TypeReference<List<DropDownModel>>() {
		});

		model.addAttribute("stateData", stateData);
		
		//End state drop down
	 
		logger.info("Method : addMembershipRegistration ends");	
		
		 return "user/AddMembershipRegistration";
	}

	
	/* 
	 * 
	 * add-membership-registration
	 *  
	 */  
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="add-membership-registration", method={RequestMethod.POST})
	public @ResponseBody JsonResponse<Object> addMembershipRegistration(@RequestBody List<UserMembershipRegistrationModel> formData, 
			Model model,HttpSession session){
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : addMembershipRegistration function starts");
		try {
			
			for (UserMembershipRegistrationModel m : formData) {
				m.setCreatedBy("mem001");
				String encpass =m.getPassword_m(); 
				encpass = passwordEncoder.encode(encpass);
				m.setPassword_m(encpass);
				String encpinM=m.getPin_m();
				encpinM=passwordEncoder.encode(encpinM);
				m.setPin_m(encpinM);
				String encdeppass=m.getPassword_();
				encdeppass=passwordEncoder.encode(encdeppass);
				m.setPassword_(encdeppass);
				String encdeppin=m.getPin_();
				encdeppin=passwordEncoder.encode(encdeppin);
				m.setPin_(encdeppin);	
			}
			res = restClient.postForObject(env.getUserUrl() + "restAddMemberReg",  formData, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = res.getMessage();
		if (message != null && message != "") {
		}else{
			res.setMessage("Success");
		}
		logger.info("Method : addMembershipRegistration function Ends"); 
		return res;
	}
	
	

	/* 
	 * 
	 * update-membership-registration
	 *  
	 */  
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="view-membership-registration-edit-registration-update-membership-registration", method={RequestMethod.POST})
	public @ResponseBody JsonResponse<Object> updateMembershipRegistration(@RequestBody List<UserMembershipRegistrationModel> formData, 
			Model model,HttpSession session){
		JsonResponse<Object> res = new JsonResponse<Object>();
		logger.info("Method : updateMembershipRegistration function starts");
		try {
			String DepID="";
			String DepName="";
			String DepName1="new";
             for (UserMembershipRegistrationModel m : formData) {
            	 DepID=m.getDepId();
            	 if(m.getDepId().equalsIgnoreCase("new")) {
            		 DepName=m.getFirst_();
            	     String encdeppass=m.getPassword_();
  					 m.setPassword_(encdeppass);
  					 String encdeppin=m.getPin_();
  					 encdeppin=passwordEncoder.encode(encdeppin);
  					 m.setPin_(encdeppin);
            	 }else {
 
            	 }
				 
			}
			res = restClient.postForObject(env.getUserUrl() + "restUpdateMemberReg",  formData, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		
		String message = res.getMessage();

		if (message != null && message != "") {
			
		}else{
			res.setMessage("Success");
		}
		logger.info("Method : updateMembershipRegistration function Ends");
		return res;
	}
	 
	  
		/*
		 * 
		 * View Membership Registration List
		 * 
		 */
		
		@SuppressWarnings("unchecked")
		@GetMapping("/view-membership-registration-list")
		public String viewMemberRegistration(Model model) {
			JsonResponse<Object> membership = new JsonResponse<Object>();
			model.addAttribute("membership", membership);
			JsonResponse<List<DropDownModel>> respTblMstr = new JsonResponse<List<DropDownModel>>();
			List<DropDownModel> memberData = new ArrayList<DropDownModel>();
			try {
				respTblMstr = restClient.getForObject(env.getUserUrl() + "getMemberData?Action=" + "getMemberData",
						JsonResponse.class);

			} catch (RestClientException e) {
				e.printStackTrace();
			}
			String messageForTblMstr = respTblMstr.getMessage();

			if (messageForTblMstr != null || messageForTblMstr != "") {
				model.addAttribute("message", messageForTblMstr);
			}

			ObjectMapper mapper = new ObjectMapper();

			 
			memberData = mapper.convertValue(respTblMstr.getBody(), new TypeReference<List<DropDownModel>>() {
			});

			model.addAttribute("memberData", memberData);
			return "user/ListingMemberRegistrationList";
		}
		
	
		
		
		/*
		 *
		 * View all Member Registration data through AJAX
		 *
		 */
		@SuppressWarnings("unchecked")
		@GetMapping("/view-membership-registration-list-through-ajax")
		public @ResponseBody DataTableResponse viewMemberRegistrationThroughAjax(Model model, HttpServletRequest request,
				@RequestParam String param1,@RequestParam String param2,@RequestParam String param3) {
			
			logger.info("Method : viewMemberRegistrationThroughAjax starts");
			
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
				tableRequest.setParam3(param3);
				
			 	JsonResponse<List<UserMembershipRegistrationModel>> jsonResponse = new JsonResponse<List<UserMembershipRegistrationModel>>();

				jsonResponse = restClient.postForObject(env.getUserUrl() + "getMemberRegData", tableRequest,
						JsonResponse.class);

				ObjectMapper mapper = new ObjectMapper();

				List<UserMembershipRegistrationModel> membership = mapper.convertValue(jsonResponse.getBody(),
						new TypeReference<List<UserMembershipRegistrationModel>>() {
						});

				String s = "";
				String rel = "1";

				for (UserMembershipRegistrationModel m : membership) {
					byte[] pId = Base64.getEncoder().encode(m.getMemId().getBytes());
				
					
						  if(m.getStatus_m()) {
							  m.setMemberShowActive("Active"); 
						  }else{
						   m.setMemberShowActive("Inactive");
						   
						  }
						  
						  rel=m.getRelation_m();
					 		if (rel == m.getRelation_m()) {
								m.setRelation_name("Self");
							}  else{
								   m.setRelation_name("Others");
								   
							  }
					 
					
					s = s + "<a href='view-membership-registration-edit-registration?id=" + new String(pId)
							+ "' ><i class=\"fa fa-edit\"></i></a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)'" 
							+ "' onclick='DeleteItem(\"" + m.getMemId() + "\")' ><i class=\"fa fa-trash\"></i></a>&nbsp;&nbsp; "
							+ "<a data-toggle='modal' title='View'  "
							+ "href='javascript:void' onclick='viewInModel(\"" + m.getMemId()
							+ "\")'><i class='fa fa-search search'></i></a>";
					m.setAction(s);
					s = "";
				 }

				
				response.setRecordsTotal(jsonResponse.getTotal());
				response.setRecordsFiltered(jsonResponse.getTotal());
				response.setDraw(Integer.parseInt(draw));
				response.setData(membership);

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			logger.info("Method : viewMemberRegistrationThroughAjax ends");	
			return response;
		}
		
		/*
		 * 
		 *  'Delete membership Registration by Id
		 *
		 */
		@SuppressWarnings("unchecked")
		 @PostMapping("delete-membership-registration") 
		 public @ResponseBody JsonResponse<Object> deleteMembershipdtls(@RequestBody String id,HttpSession session)
		 {
		 
		 logger.info("Method : deleteMembershipdtls starts");
		 JsonResponse<Object> resp = new JsonResponse<Object>();
			String deletedBy = "u0001";
			try {
				resp = restClient.getForObject(env.getUserUrl() + "deleteMembershipRegById?id=" + id + "&deletedBy=" + deletedBy, JsonResponse.class);

			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		 if(resp.getMessage()== null || resp.getMessage()=="") 
		 {
			 resp.setMessage("Success");
		 }else
		 {
			 resp.setMessage("Unsuccess");
		 }
				
		 logger.info("Method : deleteMembershipdtls ends");	
		 return resp;
		 }
		
		
		/**
		 * View selected Membership Registration in Modal
		 *
		 */


		@SuppressWarnings("unchecked")
		@PostMapping(value = { "view-membership-registration-list-model" })
		public @ResponseBody JsonResponse<UserMembershipRegistrationModel> modelviewForMembershipReg(Model model, @RequestBody String index,
				BindingResult result) {
			logger.info("Method : modelviewForMembershipReg starts");
		 	JsonResponse<UserMembershipRegistrationModel> res = new JsonResponse<UserMembershipRegistrationModel>();
			
			try {
				res = restClient.getForObject(
						env.getUserUrl() + "getMembershipRegistrationModel?id=" + index,
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
			logger.info("Method : modelviewForMembershipReg ends");
			return res;
		}
		
		/*
		 * 'Edit Member Registration Master' By Id
		 * 
		 */
		@SuppressWarnings("unchecked")
		@GetMapping("/view-membership-registration-edit-registration")
		public String editMemberReg(Model model, @RequestParam("id") String encodedIndex, HttpSession session) {

			logger.info("Method : editMemberReg starts");
			byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
			String id = (new String(encodeByte));
			JsonResponse<List<UserMemberDataRegistrationModel>> jsonResponse = new JsonResponse<List<UserMemberDataRegistrationModel>>();
	        try {

				jsonResponse = restClient.getForObject(
						env.getUserUrl() + "getMembershipRegistrationEdit?id=" + id + "&Action=ViewEditMember",
						JsonResponse.class);

			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String message = (String) session.getAttribute("message");

			if (message != null && message != "") {
				model.addAttribute("message", message);
			}

			  ObjectMapper mapper = new ObjectMapper();
			  
			  List<UserMemberDataRegistrationModel> membershipResult = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<UserMemberDataRegistrationModel>>() {});
			  session.setAttribute("message", "");
			  Integer countRow=0;
		      model.addAttribute("membershipResult", membershipResult);
			  model.addAttribute("countRow", countRow);
			  JsonResponse<List<UserMemberDataRegistrationModel>> jsonResponsefordep = new
			  JsonResponse<List<UserMemberDataRegistrationModel>>();
			  
			  try {
			  
			  jsonResponsefordep = restClient.getForObject( env.getUserUrl() +
			  "getMemberRegistEditdep?id=" + id + "&Action=ViewEditMemberdep",
			  JsonResponse.class);
			 
			  } catch (RestClientException e) { // TODO Auto-generated catch block
			  e.printStackTrace(); }
			  
			  String message1 = (String) session.getAttribute("message1");
			  if (message1 != null && message1 != "") { model.addAttribute("message",
			  message1); }
			  
			  ObjectMapper mapper1 = new ObjectMapper();
			  
			  List<UserMemberDataRegistrationModel> membershipResultdep =
			  mapper1.convertValue(jsonResponsefordep.getBody(), new
			  TypeReference<List<UserMemberDataRegistrationModel>>() {});
			  session.setAttribute("message", "");
			  
			  Integer count=0; 
			   model.addAttribute("membershipResultdep", membershipResultdep);
			  model.addAttribute("count", count);
			  
			    //start state drop down
				JsonResponse<List<DropDownModel>> respTblMstrState = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> stateData = new ArrayList<DropDownModel>();
				try {
					respTblMstrState = restClient.getForObject(env.getUserUrl() + "getStateData?Action=" + "getStateData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrState = respTblMstrState.getMessage();
	
				if (messageForTblMstrState != null || messageForTblMstrState != "") {
					model.addAttribute("message", messageForTblMstrState);
				}
	
				ObjectMapper mapper2 = new ObjectMapper();
	            stateData = mapper2.convertValue(respTblMstrState.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("stateData", stateData);
				
				//End state drop down
				
				//start membertype drop down
				JsonResponse<List<DropDownModel>> respTblMstrmem = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> memberDatamem = new ArrayList<DropDownModel>();
				try {
					respTblMstrmem = restClient.getForObject(env.getUserUrl() + "getMemberData?Action=" + "getMemberData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrmem = respTblMstrmem.getMessage();
	
				if (messageForTblMstrmem != null || messageForTblMstrmem != "") {
					model.addAttribute("message", messageForTblMstrmem);
				}
	
				ObjectMapper mappermem = new ObjectMapper();
	
				 
				memberDatamem = mappermem.convertValue(respTblMstrmem.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("memberDatamem", memberDatamem);
				
				//End memberType drop down
				
				//start paymentModeData drop down
				JsonResponse<List<DropDownModel>> respTblMstrpay = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> paymentModeData = new ArrayList<DropDownModel>();
				try {
					respTblMstrpay = restClient.getForObject(env.getUserUrl() + "getPaymentModeData?Action=" + "getMemberData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrpay = respTblMstrpay.getMessage();
	
				if (messageForTblMstrpay != null || messageForTblMstrpay != "") {
					model.addAttribute("message", messageForTblMstrpay);
				}
	
				ObjectMapper mapper3 = new ObjectMapper();
				 
				paymentModeData = mapper3.convertValue(respTblMstrpay.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("paymentModeData", paymentModeData);
				
				//End paymentModeData drop down
				
				
				//start relation drop down
				JsonResponse<List<DropDownModel>> respTblMstrRel = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> relationData = new ArrayList<DropDownModel>();
				try {
					respTblMstrRel = restClient.getForObject(env.getUserUrl() + "getRelationData?Action=" + "getRelationData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrRel = respTblMstrRel.getMessage();
	
				if (messageForTblMstrRel != null || messageForTblMstrRel != "") {
					model.addAttribute("message", messageForTblMstrRel);
				}
	
				ObjectMapper mapper4 = new ObjectMapper();
				 
				relationData = mapper4.convertValue(respTblMstrRel.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("relationData", relationData);
				
				//End relationData drop down
				
				//start relation drop down self only
				JsonResponse<List<DropDownModel>> respTblMstrRelself = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> relationselfData = new ArrayList<DropDownModel>();
				try {
					respTblMstrRelself = restClient.getForObject(env.getUserUrl() + "getRelationSelfData?Action=" + "getRelationSelfData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrRelself = respTblMstrRelself.getMessage();
	
				if (messageForTblMstrRelself != null || messageForTblMstrRelself != "") {
					model.addAttribute("message", messageForTblMstrRelself);
				}
	
				ObjectMapper mapper9 = new ObjectMapper();
				 
				relationselfData = mapper9.convertValue(respTblMstrRelself.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("relationselfData", relationselfData);
				
				//End relationData self only drop down
				
				
				//start user type drop down
				JsonResponse<List<DropDownModel>> respTblMstrUType = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> UTypedata = new ArrayList<DropDownModel>();
				try {
					respTblMstrUType = restClient.getForObject(env.getUserUrl() + "getUTypeData?Action=" + "getUTypeData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrUType = respTblMstrUType.getMessage();
	
				if (messageForTblMstrUType != null || messageForTblMstrUType != "") {
					model.addAttribute("message", messageForTblMstrUType);
				}
	
				ObjectMapper mapper6 = new ObjectMapper();
				 
				UTypedata = mapper6.convertValue(respTblMstrUType.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("UTypedata", UTypedata);
				
				//End user type drop down
				
				//start District drop down
				JsonResponse<List<DropDownModel>> respTblMstrDis = new JsonResponse<List<DropDownModel>>();
				List<DropDownModel> disData = new ArrayList<DropDownModel>();
				try {
					respTblMstrDis = restClient.getForObject(env.getUserUrl() + "getDisData?Action=" + "getDisData",
							JsonResponse.class);
	
				} catch (RestClientException e) {
					e.printStackTrace();
				}
				String messageForTblMstrDis = respTblMstrDis.getMessage();
	
				if (messageForTblMstrDis != null || messageForTblMstrDis != "") {
					model.addAttribute("message", messageForTblMstrDis);
				}
	
				ObjectMapper mapper5 = new ObjectMapper();
				 
				disData = mapper5.convertValue(respTblMstrDis.getBody(), new TypeReference<List<DropDownModel>>() {
				});
	
				model.addAttribute("disData", disData);
				
				//End district drop down
			 
				
				logger.info("Method : editMemberReg ends");
				return "user/EditAddMemberRegistration";  
		}
		
		
		/**
		 * get DistrictName by the onChange of state select add time
		 *
		 */

		@SuppressWarnings("unchecked")
		@PostMapping(value = { "add-membership-registration-getDistrictName-throughAjax" })
		public @ResponseBody JsonResponse<DropDownModel> getDistrictName(Model model, @RequestBody String stateid,
				BindingResult result) {
			logger.info("Method : getDistrictName starts");
			JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

			try {
				res = restClient.getForObject(
						env.getUserUrl() + "getDistrictName?id=" + stateid,
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
			logger.info("Method : getDistrictName ends");
			return res;
		}
		
		

		/**
		 * get DistrictName by the onChange of state select edit time
		 *
		 */

		@SuppressWarnings("unchecked")
		@PostMapping(value = { "view-membership-registration-edit-registration-getDistrictName-throughAjax" })
		public @ResponseBody JsonResponse<DropDownModel> getDistrictNameEdit(Model model, @RequestBody String stateid,
				BindingResult result) {
			logger.info("Method : getDistrictNameEdit starts");
			JsonResponse<DropDownModel> res = new JsonResponse<DropDownModel>();

			try {
				res = restClient.getForObject(
						env.getUserUrl() + "getDistrictName?id=" + stateid,
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
			logger.info("Method : getDistrictNameEdit ends");
			return res;
		}
		
		
		
		/**
		 * get total dep for adding by the onChange of member type select add time
		 *
		 */

		@SuppressWarnings("unchecked")
		@PostMapping(value = { "add-membership-registration-getDepCount-throughAjax" })
		public @ResponseBody JsonResponse<UserMemberDepCountModel> getTotalDepAdd(Model model, @RequestBody String memid,
				BindingResult result) {
			logger.info("Method : getTotalDepAdd starts");
			JsonResponse<UserMemberDepCountModel> res = new JsonResponse<UserMemberDepCountModel>();

			try {
				res = restClient.getForObject(
						env.getUserUrl() + "getTotalDepAdd?id=" + memid,
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
			logger.info("Method : getTotalDepAdd ends");
			return res;
		}
		 
		
		/**
		 * get total dep for adding by the onChange of member type select edit time
		 *
		 */

		@SuppressWarnings("unchecked")
		@PostMapping(value = { "view-membership-registration-edit-registration-getDepCount-throughAjax" })
		public @ResponseBody JsonResponse<UserMemberDepCountModel> getTotalDepAddEdit(Model model, @RequestBody String memid,
				BindingResult result) {
			logger.info("Method : getTotalDepAddEdit starts");
			JsonResponse<UserMemberDepCountModel> res = new JsonResponse<UserMemberDepCountModel>();

			try {
				res = restClient.getForObject(
						env.getUserUrl() + "getTotalDepAdd?id=" + memid,
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
			logger.info("Method : getTotalDepAddEdit ends");
			return res;
		}
	
}
