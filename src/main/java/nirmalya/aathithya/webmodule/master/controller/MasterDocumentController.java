package nirmalya.aathithya.webmodule.master.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
public class MasterDocumentController {
	
	@Autowired
	EnvironmentVaribles env;

	Logger logger = LoggerFactory.getLogger(MasterDocumentController.class);

	/**
	 * document controller to load images instantly
	 *
	 */
	@RequestMapping(value = "document/function/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentFun(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : getDocument controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadMaster());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}

	@RequestMapping(value = "document/function/thumb/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentThumbFun(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : image controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadMaster()+"/thumb");
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}
	
	
	@RequestMapping(value = "document/module/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentMod(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : getDocument controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadMaster());
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}

	@RequestMapping(value = "document/module/thumb/{docname}")
	@ResponseBody
	public ResponseEntity<byte[]> getDocumentThumbMod(@PathVariable(value = "docname") String docname) throws IOException {
		logger.info("Method : image controller function starts");

		File dir = ResourceUtils.getFile(env.getFileUploadMaster()+"/thumb");
		File file = new File(dir.getAbsolutePath() + "/" + docname);
		byte[] bytearr = Files.readAllBytes(file.toPath());
		if (docname.endsWith(".png")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
		} else if (docname.endsWith(".jpeg")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
		} else if (docname.endsWith(".pdf")) {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
		} else {
			logger.info("Method : getDocument controller function end");
			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
		}
	}
	
	
}