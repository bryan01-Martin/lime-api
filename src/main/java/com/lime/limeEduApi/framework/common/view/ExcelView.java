package com.lime.limeEduApi.framework.common.view;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lime.limeEduApi.framework.common.service.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;


@Component
public class ExcelView extends AbstractView{

	@Autowired
	ExcelService excelService;
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = (String) model.get("fileName");
		Workbook workbook;
		if(model.get("workbook") != null) {
			workbook = (Workbook)model.get("workbook");
			downloadFile(request, response, (Workbook)model.get("workbook"), fileName);
		}else {
			workbook = new XSSFWorkbook();
			excelService.createDefaultWorkbook(model, workbook);
			downloadFile(request, response, workbook, fileName);
		}
		
		
	}

	public void downloadFile(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String fileName) throws Exception{
		SimpleDateFormat  dateFomatter = new SimpleDateFormat("yyMMdd_HHmmss");
		fileName += dateFomatter.format(new Date());
		response.addHeader("X-FRAME-OPTIONS", "DENY");
		response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        String userAgent = request.getHeader("User-Agent");
        boolean ie = userAgent.indexOf("MSIE") > -1;
        if(ie){
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", " ");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }// end if;
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "Attachment; Filename=\"" + fileName + ".xlsx\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        
        ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		workbook.close();
	}




}
