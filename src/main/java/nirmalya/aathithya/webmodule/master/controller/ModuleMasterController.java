package nirmalya.aathithya.webmodule.master.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nirmalya.aathithya.webmodule.common.pagination.DataTableRequest;
import nirmalya.aathithya.webmodule.common.pagination.DataTableResponse;
import nirmalya.aathithya.webmodule.common.utils.EnvironmentVaribles;
import nirmalya.aathithya.webmodule.common.utils.JsonResponse;
import nirmalya.aathithya.webmodule.master.model.ModuleMasterModel;

/**
 * @author NirmalyaLabs
 *
 */

@Controller
@RequestMapping(value = "master")
public class ModuleMasterController {

	Logger logger = LoggerFactory.getLogger(ModuleMasterController.class);
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	EnvironmentVaribles env;

	/**
	 * View Default 'Module Master' page
	 *
	 */
	@GetMapping(value = { "view-module-master" })
	public String viewModule(Model model) {
		logger.info("Method : viewModule starts");

		logger.info("Method : viewModule ends");
		return "master/viewModuleMaster";
	}

	/**
	 * call All module through ajax
	 *
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/view-module-master-throughAjax")
	public @ResponseBody DataTableResponse viewAllModuleThroughAjax(Model model, HttpServletRequest request,
			@RequestParam String param1) {
		logger.info("Method : viewAllModuleThroughAjax starts");
		DataTableResponse response = new DataTableResponse();
		DataTableRequest tableRequest = new DataTableRequest();
		try {
			String start = request.getParameter("start");
			String length = request.getParameter("length");
			String draw = request.getParameter("draw");

			tableRequest.setStart(Integer.parseInt(start));
			tableRequest.setLength(Integer.parseInt(length));
			tableRequest.setParam1(param1);

			JsonResponse<List<ModuleMasterModel>> jsonResponse = new JsonResponse<List<ModuleMasterModel>>();

			jsonResponse = restTemplate.postForObject(env.getMasterUrl() + "getAllModule", tableRequest,
					JsonResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			List<ModuleMasterModel> moduleMaster = mapper.convertValue(jsonResponse.getBody(),
					new TypeReference<List<ModuleMasterModel>>() {
					});

			String s = "";
			for (ModuleMasterModel m : moduleMaster) {
				byte[] pId = Base64.getEncoder().encode(m.gettModule().getBytes());
				if (m.gettModlStatus()) {
					s = "";
					s = s + "Active";
				} else {
					s = "";
					s = s + "Inactive";
				}
				m.setStatus(s);
				;

				s = "";
				s = "<a data-toggle='modal' title='View' data-target='#myModal' href='javascript:void(0)' onclick='viewInModel(\""
						+ new String(pId) + "\")'><i class='fa fa-search search'></i></a>";
				s = s + "&nbsp;&nbsp;<a data-toggle='modalImage' title='Edit' data-target='#myModalImg' href='javascript:void(0)' onclick='viewInModelImage(\""
						+ new String(pId)
						+ "\")'><i class='fa fa-upload'></i></a>";
				m.setAction(s);

				s = "";
				s = "<a class='example-image-link' href='/document/module/" + m.gettModuleLogo() + "' title='"
						+ m.gettModuleLogo() + "' data-lightbox='" + m.gettModuleLogo() + "'>"
						+ "<img src='/document/module/thumb/" + m.gettModuleLogo() + "'/>" + "</a>";
				m.settModuleLogo(s);

			}

			response.setRecordsTotal(jsonResponse.getTotal());
			response.setRecordsFiltered(jsonResponse.getTotal());
			response.setDraw(Integer.parseInt(draw));
			response.setData(moduleMaster);

		} catch (RestClientException e) {
			e.printStackTrace();
		}
		logger.info("Method : viewAllModuleThroughAjax ends");
		return response;
	}

	/**
	 * View selected User Process in Modal
	 *
	 */

	@SuppressWarnings("unchecked")
	@PostMapping("/view-module-master-model")
	public @ResponseBody JsonResponse<Object> viewModuleModal(Model model, HttpSession session,
			@RequestBody String encodedIndex) {
		logger.info("Method : viewModuleModal starts");
		byte[] encodeByte = Base64.getDecoder().decode(encodedIndex.getBytes());
		String id = new String(encodeByte);
		JsonResponse<Object> res = new JsonResponse<Object>();

		try {
			res = restTemplate.getForObject(env.getMasterUrl() + "viewModuleModal?id=" + id, JsonResponse.class);
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		logger.info("Method : viewModuleModal ends");
		return res;
	}

	@PostMapping("/view-module-master-uploadfile")
	public @ResponseBody JsonResponse<Object> uploadFile(@RequestParam("file") MultipartFile inputFile,
			HttpSession session) {
		logger.info("Method : uploadFile controller function 'post-mapping' starts");

		JsonResponse<Object> response = new JsonResponse<Object>();

		try {

			response.setMessage(inputFile.getOriginalFilename());
			session.setAttribute("menuItemsMasterFile", inputFile.getBytes());
		} catch (RestClientException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Method : uploadFile controller function 'post-mapping' ends");
		return response;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = { "view-module-master-saveImage" })
	public @ResponseBody JsonResponse<Object> getIssueItemName(Model model, @RequestParam("fileName") String fileName,
			@RequestParam("moduleId") String moduleId, HttpSession session) {
		logger.info("Method : upload image starts");
		JsonResponse<Object> res = new JsonResponse<Object>();

		byte[] encodeByte = Base64.getDecoder().decode(moduleId.getBytes());
		String id = new String(encodeByte);
		try {

			byte[] bytes = (byte[]) session.getAttribute("menuItemsMasterFile");
			long nowTime = new Date().getTime();
			String imageName = nowTime + ".png";
			fileName = imageName;
			res = restTemplate.getForObject(env.getMasterUrl() + "getEditImage?id=" + id + "&fileName=" + fileName,
					JsonResponse.class);

			if ((res.getCode() == null || res.getCode() == "")
					&& (res.getMessage() == null || res.getMessage() == "")) {
				Path path = Paths.get(env.getFileUploadMaster() + imageName);
				Files.write(path, bytes);

				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				Integer height = 20;
				Integer width = 20;

				try {
					BufferedImage img = ImageIO.read(in);
					if (height == 0) {
						height = (width * img.getHeight()) / img.getWidth();
					}
					if (width == 0) {
						width = (height * img.getWidth()) / img.getHeight();
					}

					Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
					BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					imageBuff.getGraphics().drawImage(scaledImage, 0, 0, null);

					ByteArrayOutputStream buffer = new ByteArrayOutputStream();

					ImageIO.write(imageBuff, "png", buffer);

					// ByteArrayOutputStream out = new ByteArrayOutputStream();
					byte[] thumb = buffer.toByteArray();
					Path pathThumb = Paths.get(env.getFileUploadMaster() + "thumb\\" + imageName);
					Files.write(pathThumb, thumb);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (res.getMessage() != null) {
			res.setCode(res.getMessage());
			res.setMessage("Unsuccess");
		} else {
			res.setMessage("success");
		}
		
		try {
			TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		
		logger.info("Method : upload image ends");
		return res;
	}

}
