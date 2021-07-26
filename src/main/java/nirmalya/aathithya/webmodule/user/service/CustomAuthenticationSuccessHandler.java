package nirmalya.aathithya.webmodule.user.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.user.model.Activity;
import nirmalya.aathithya.webmodule.user.model.Function;
import nirmalya.aathithya.webmodule.user.model.Menu;
import nirmalya.aathithya.webmodule.user.model.Module;
import nirmalya.aathithya.webmodule.user.model.User;

@Service
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// do some logic here if you want something to be done whenever
		// the user successfully logs in.
		HttpSession session = request.getSession();
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		User user = userDetails.getUser();
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
	    String strDate = sdf.format(cal.getTime());
	    //System.out.println("Current date in String Format: "+strDate);

	    SimpleDateFormat sdf1 = new SimpleDateFormat();
	    sdf1.applyPattern("dd/MM/yyyy HH:mm:ss.SS");
	    Date date = null;
		try {
			date = sdf1.parse(strDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	  //  System.out.println("Current date in Date Format: "+date);
		session.setAttribute("SESSION_AGENT", request.getHeader("User-Agent"));
		session.setAttribute("SESSION_ID", session.getId());
		session.setAttribute("SESSION_HOST", request.getRemoteHost());
		session.setAttribute("SESSION_PORT", request.getRemotePort());
		session.setAttribute("USER_ID", user.getUser());
		//System.out.println("userName"+user.getUserName());
		//System.out.println("lastName"+user.getLastName());
		if(user.getUserName()!=null && user.getUserName()!="" ) {
			user.setUserName(user.getUserName()+"  "+user.getLastName());
		}
		//System.out.println("user123 "+user);
		session.setAttribute("USER_NAME", user.getUserName());
		session.setAttribute("USER_EMAIL", user.getUserEmail());
		session.setAttribute("USER_PASSWORD", user.getUserPassword());
		session.setAttribute("LOCATION", user.getRegion());
		
		if (user.getUserType().contains("SuperAdmin")) {
			session.setAttribute("USER_ROLETYPE", "SuperAdmin");
		} else {
			session.setAttribute("USER_ROLETYPE", user.getUserType());
		}
		//System.out.println("User Role Dashboar: " + user);
		session.setAttribute("USER_MOBILE", user.getUserMobile());
		session.setAttribute("AUTHORITIES", authentication.getAuthorities());
		session.setAttribute("JOB_TITLE", user.getUserParent());
		session.setAttribute("DEPARTMENT", user.getUserAddress());
		session.setAttribute("USER_ROLES", user.getRoles());
		
		session.setAttribute("DASHBOARD", user.getRoleDashboard());
		session.setAttribute("USER_LOGINTIME", "Last Logged on  "+date);
		session.setAttribute("MAILSTATUS", user.getEmailStatus());
//System.out.println("deepak"+ session.getAttribute("MAILSTATUS"));
		JsonResponse<List<Menu>> jsonResponse = new JsonResponse<List<Menu>>();

		List<Module> module = new ArrayList<Module>();
		List<Menu> mList = new ArrayList<Menu>();
		List<String> uList = new ArrayList<String>();

		try {

			jsonResponse = restTemplate.postForObject(env.getUserUrl() + "getMenu", user.getRoles(),
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();
			mList = mapper.convertValue(jsonResponse.getBody(), new TypeReference<List<Menu>>() {
			});

			String pMod = "";
			if (mList != null && mList.size() > 0) {
				for (Menu m : mList) {

					String mod = m.getModule();

					if (mod.equals(pMod)) {

					} else {

						pMod = mod;

						String pFun = "";

						Module newMod = new Module();
						newMod.setName(mod);
						newMod.setModuleLogoName(m.getModuleLogo());

						List<Menu> fList = mList.stream().filter(s -> s.getModule().equals(mod))
								.collect(Collectors.toList());

						List<Function> funList = new ArrayList<Function>();

						for (Menu f : fList) {
							String fun = f.getFunction();

							if (fun.equals(pFun)) {

							} else {

								pFun = fun;
								List<Menu> aList = fList.stream().filter(s -> s.getFunction().equals(fun))
										.collect(Collectors.toList());

								List<Activity> sList = new ArrayList<Activity>();
								for (Menu a : aList) {
									Activity newAct = new Activity();
									newAct.setName(a.getActivity());
									newAct.setActivity(a.getUrl());

									uList.add(a.getUrl());
									// System.out.println("url : "+ a.getUrl());
									// sList.add(newAct); commented to remove non-viewable function
									if (a.getActivityStatus()) { // checking only active functions
										sList.add(newAct);
									}

								}

								Function newFun = new Function();
								newFun.setName(fun);
								newFun.setFunction(sList);

								funList.add(newFun);

							}

						}

						newMod.setModule(funList);

						module.add(newMod);
					}
				}

			}

			/*
			 * adding extra urls to be accessed by all.
			 * 
			 */
			String dashboard = (String) session.getAttribute("DASHBOARD");
			uList.add(dashboard);
//			uList.add("/sales/sales-dashboard");

			session.setAttribute("MENU", module);
			// System.out.println("menu :" + module);
			session.setAttribute("URL_LIST", uList);
			session.setAttribute("loginMessage", null);

		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// set our response to OK status
		response.setStatus(HttpServletResponse.SC_OK);

		// since we have created our custom success handler, its up to us to
		// where
		// we will redirect the user after successfully login

		String dashboard = (String) session.getAttribute("DASHBOARD");
		response.sendRedirect(dashboard);
	}

}
