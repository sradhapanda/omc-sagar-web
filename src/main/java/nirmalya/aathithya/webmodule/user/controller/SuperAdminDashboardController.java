package nirmalya.aathithya.webmodule.user.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.MapModel;
import nirmalya.aathithya.webmodule.common.utils.MapModel1;
import nirmalya.aathithya.webmodule.user.model.SuperAdminDashboardModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "user")
public class SuperAdminDashboardController {

	Logger logger = LoggerFactory.getLogger(SuperAdminDashboardController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@GetMapping("super-admin-dashboard")
	public String superAdminDashboard(Model model, HttpSession session) {
		logger.info("Method : WEB MODULE SuperAdminDashboardController superAdminDashboard starts");

		ObjectMapper mapper = new ObjectMapper();
		/**
		 * @get TOTAL INVOICE OF SPA
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllSpaCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel spaInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllSpaCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-spa-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount = respGetAllSpaCount.getMessage();

		if (messageForCount != null || messageForCount != "") {
			model.addAttribute("message", messageForCount);
		}

		spaInvoice = mapper.convertValue(respGetAllSpaCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("spaInvoice", spaInvoice);
		
		/**
		 * @get TOTAL INVOICE OF GYM
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllgymCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel gymInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllgymCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-gym-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount1 = respGetAllgymCount.getMessage();

		if (messageForCount1 != null || messageForCount1 != "") {
			model.addAttribute("message", messageForCount1);
		}

		gymInvoice = mapper.convertValue(respGetAllgymCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("gymInvoice", gymInvoice);
		/**
		 * @get TOTAL INVOICE OF BEAUTY PARLOUR
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllBeautyCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel beautyInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllBeautyCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-beauty-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount2 = respGetAllBeautyCount.getMessage();

		if (messageForCount2 != null || messageForCount2 != "") {
			model.addAttribute("message", messageForCount2);
		}

		beautyInvoice = mapper.convertValue(respGetAllBeautyCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("beautyInvoice", beautyInvoice);
		
		/**
		 * @get TOTAL INVOICE OF LAUNDRY
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllLaundryCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel laundryInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllLaundryCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-laundry-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount3 = respGetAllLaundryCount.getMessage();

		if (messageForCount3 != null || messageForCount3 != "") {
			model.addAttribute("message", messageForCount3);
		}

		laundryInvoice = mapper.convertValue(respGetAllLaundryCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("laundryInvoice", laundryInvoice);

		/**
		 * @get TOTAL INVOICE OF NIGHT  CLUB
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllNightClubCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel nightClubInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllNightClubCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-night-club-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount4 = respGetAllNightClubCount.getMessage();

		if (messageForCount4 != null || messageForCount4 != "") {
			model.addAttribute("message", messageForCount4);
		}

		nightClubInvoice = mapper.convertValue(respGetAllNightClubCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("nightClubInvoice", nightClubInvoice);


		/**
		 * @get TOTAL INVOICE OF RESTAURANT
		 *
		 */

		JsonResponse<SuperAdminDashboardModel> respGetAllRestaurantCount = new JsonResponse<SuperAdminDashboardModel>();
		SuperAdminDashboardModel restaurantInvoice = new SuperAdminDashboardModel();

		try {

			respGetAllRestaurantCount = restTemplate.getForObject(env.getUserUrl() + "rest-total-restaurant-invoice",
					JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		String messageForCount5 = respGetAllRestaurantCount.getMessage();

		if (messageForCount5 != null || messageForCount5 != "") {
			model.addAttribute("message", messageForCount5);
		}

		restaurantInvoice = mapper.convertValue(respGetAllRestaurantCount.getBody(), new TypeReference<SuperAdminDashboardModel>() {
		});

		model.addAttribute("restaurantInvoice", restaurantInvoice);


		logger.info("Method : WEB MODULE SuperAdminDashboardController superAdminDashboard ends");
		return "user/SuperAdminDashboard";

	}
	
	/**
	 * OCCUPANCY REPORT GRAPH
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "occupancy-graph-request" })
	public @ResponseBody JsonResponse<MapModel> getOccupancyReportGraph(Model model) {
		logger.info("Method : getOccupancyReportGraph starts");
		JsonResponse<MapModel> res = new JsonResponse<MapModel>();
	
		try {
			res = restTemplate.getForObject(env.getUserUrl() + "viewOccupancyReport", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getOccupancyReportGraph ends");

		return res;
	}
	/**
	 * REVENUE REPORT GRAPH
	 * 
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = { "revenue-report-graph-request" })
	public @ResponseBody JsonResponse<MapModel1> getRevenueReportGraph(Model model) {
		logger.info("Method : getRevenueReportGraph starts");
		JsonResponse<MapModel1> res = new JsonResponse<MapModel1>();

		try {
			res = restTemplate.getForObject(env.getUserUrl() + "viewRevenueReportGraph", JsonResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : getRevenueReportGraph ends");

		return res;
	}
}
