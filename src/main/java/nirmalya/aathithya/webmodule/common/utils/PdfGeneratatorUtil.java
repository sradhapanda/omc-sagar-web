/**
 * 
 */
package nirmalya.aathithya.webmodule.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author NirmalyaLabs
 *
 */
@Component
public class PdfGeneratatorUtil {
	@Autowired
	private TemplateEngine templateEngine;
	@SuppressWarnings("rawtypes")
	
	public File createPdf(String templateName,Map map)throws Exception{
		Assert.notNull(templateName,"The templateName can not be null");
		Context ctx = new Context();
		if(map != null){
			Iterator iteratorMap = map.entrySet().iterator();
			while(iteratorMap.hasNext()){
				Map.Entry pair = (Map.Entry) iteratorMap.next();
				ctx.setVariable(pair.getKey().toString(), pair.getValue());
			}
		}
		String processedHTML = templateEngine.process(templateName, ctx);
		FileOutputStream os = null;
		String fileName = UUID.randomUUID().toString();
		final File outputFile = new File(fileName);
		try{
			os = new FileOutputStream(outputFile);
			ITextRenderer renderer= new ITextRenderer();
			renderer.setDocumentFromString(processedHTML);
			renderer.layout();
			renderer.createPDF(os,false);
			renderer.finishPDF();
		}finally{
			if(os !=null){
				try{
					os.close();
				}catch(IOException e){}
			}
		}
		return outputFile;
	}
	
	
}
