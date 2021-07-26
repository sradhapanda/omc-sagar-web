package nirmalya.aathithya.webmodule.constant;


import org.springframework.beans.factory.annotation.Autowired;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

public class Constant {
	@Autowired
	static
	EnvironmentVaribles env;
	
	public static String host="Smtp.live.com";
	 public static String port="587";
	 public static String mailFrom="debendumohapatra@odishamining.in";
	 public static String password="Guk20000#";


	 /*public static String host="Smtp.gmail.com";
	 public static String port="587";
	 public static String mailFrom="welcomesuprava878@gmail.com";
	 public static String password="niranjanswain@123";*/
	 
	 
	}