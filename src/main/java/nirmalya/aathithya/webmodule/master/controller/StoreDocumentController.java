///**
// * 
// */
//package nirmalya.aathithya.webmodule.master.controller;
//
//import java.io.File;
//
//import java.io.IOException;
//import java.nio.file.Files;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.ResourceUtils;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
//
//
//
///**
// * @author Nirmalya Labs
// *
// */
//@Controller
//public class StoreDocumentController {
//	
//	Logger logger = LoggerFactory.getLogger(StoreDocumentController.class);
//	/**
//	 * document controller to load images instantly
//	 *
//	 */
//	@Autowired
//	EnvironmentVaribles env;
//	
//	@RequestMapping(value="document/store/{docname}")
//	@ResponseBody
//	public ResponseEntity<byte[]> getDocument(@PathVariable(value="docname")String docname)throws IOException{
//		logger.info("Method : getDocument controller function starts");
//		
//		File dir = ResourceUtils.getFile(env.getFileUploadStoreUrl());
//		File file = new File(dir.getAbsolutePath() + "/" + docname);
//		byte[] bytearr = Files.readAllBytes(file.toPath());
//		if(docname.endsWith(".png")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
//		}
//		else if(docname.endsWith(".jpeg")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
//		}
//		else if(docname.endsWith(".pdf")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
//		}
//		else {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
//		}
//	}
//	
//	@RequestMapping(value="document/store/thumb/{docname}")
//	@ResponseBody
//	public ResponseEntity<byte[]> getDocumentThumb(@PathVariable(value="docname")String docname)throws IOException{
//		logger.info("Method : image controller function starts");
//		
//		File dir = ResourceUtils.getFile(env.getFileUploadStoreUrl()+"thumb");
//		File file = new File(dir.getAbsolutePath() + "/" + docname);
//		byte[] bytearr = Files.readAllBytes(file.toPath());
//		if(docname.endsWith(".png")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(bytearr);
//		}
//		else if(docname.endsWith(".jpeg")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytearr);
//		}
//		else if(docname.endsWith(".pdf")) {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(bytearr);
//		}
//		else {
//			logger.info("Method : getDocument controller function starts");
//			return ResponseEntity.ok().contentType(MediaType.ALL).body(bytearr);
//		}
//	}
//}
