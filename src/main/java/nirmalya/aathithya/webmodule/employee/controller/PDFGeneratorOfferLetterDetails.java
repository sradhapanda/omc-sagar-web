package nirmalya.aathithya.webmodule.employee.controller;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Base64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.common.utils.PdfGeneratatorUtil;
import nirmalya.aathithya.webmodule.employee.model.OfferLetterDetailModel;

@Controller
@RequestMapping(value = "employee")
public class PDFGeneratorOfferLetterDetails {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	@Autowired
	PdfGeneratatorUtil pdfGeneratorUtil;

	Logger logger = LoggerFactory.getLogger(PDFGeneratorOfferLetterDetails.class);
	/*
	 * pdf for assignment of seating plan
	 */

	@SuppressWarnings("unchecked")
	@GetMapping(value = { "/View-offer-letter-dtls-genetate-offer-letter-pdf" })
	public void offerLetterDetailsPDF(HttpServletResponse response, @RequestParam("id") String encodedParam1,
			Model model) {

		logger.info("Method :offerLetterDetailsPDF starts");
		String encodeId = encodedParam1;
		// String curDate;
		byte[] decodeId = Base64.getDecoder().decode(encodeId.getBytes());

		String id1 = (new String(decodeId));

		JsonResponse<List<OfferLetterDetailModel>> jsonresponse = new JsonResponse<List<OfferLetterDetailModel>>();

		try {
			jsonresponse = restTemplate.getForObject(env.getEmployeeUrl() + "offerLetterDetailsPDF?id=" + id1,
					JsonResponse.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();

		List<OfferLetterDetailModel> offerLetterDtls = mapper.convertValue(jsonresponse.getBody(),
				new TypeReference<List<OfferLetterDetailModel>>() {
				});

		Map<String, Object> data = new HashMap<String, Object>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();
		String currDate = dtf.format(now);
		data.put("currDate", currDate);
		data.put("offerLetterDtls", offerLetterDtls);

		// String variable = env.getBaseUrlPath();

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=OffereLetterDetails.pdf");
		File file;
		byte[] fileData = null;
		try {
			file = pdfGeneratorUtil.createPdf("employee/offerLetterDetailsPDF", data);
			InputStream in = new FileInputStream(file);
			fileData = IOUtils.toByteArray(in);
			response.setContentLength(fileData.length);
			response.getOutputStream().write(fileData);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Method :offerLetterDetailsPDF ends");
	}
}
