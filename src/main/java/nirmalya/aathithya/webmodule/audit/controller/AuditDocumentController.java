package nirmalya.aathithya.webmodule.audit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;

/**
 * @author Nirmalya Labs
 *
 */
@Controller
public class AuditDocumentController {

	Logger logger = LoggerFactory.getLogger(AuditDocumentController.class);
	@Autowired
	EnvironmentVaribles env;

	/**
	 * document controller to load images instantly
	 *
	 */
	@RequestMapping(value = "document/audit/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocument(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : getDocument controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadAudit());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".doc")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else if (docname.endsWith(".docx")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}

	@RequestMapping(value = "document/audit/thumb/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentThumb(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : image controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadAudit());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else if (docname.endsWith(".doc")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else if (docname.endsWith(".docx")) {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function starts");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}

	/**
	 * document controller to load images instantly
	 *
	 */
	@RequestMapping(value = "document/audit/excel/{docname}")
	@ResponseBody
	public HttpServletResponse getDocument1(@PathVariable(value = "docname") String docname,
			HttpServletResponse response) throws IOException {
		logger.info("Method : getDocument controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadAudit());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		/* Bikash changes */
		System.out.println("bikash's "+docname);
		
		String part2="";
		try {
			String[] parts = docname.split("-");
			//String part1 = parts[0]; // 004
			 part2 = parts[1]; // 034556
			 response.setHeader("Content-disposition", "attachment; filename=" + part2);		
			System.out.println("bikash's new docname "+part2);
		}
		catch(ArrayIndexOutOfBoundsException exception) {
			logger.info("Method : getDocument controller function starts");
			response.setHeader("Content-disposition", "attachment; filename=" + docname);
		}

		logger.info("Method : getDocument controller function starts");
		

		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		int length = 0;
		byte[] buffer = new byte[8192];
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();
		return response;

	}
}
