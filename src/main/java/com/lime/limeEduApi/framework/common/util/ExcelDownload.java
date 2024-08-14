package com.lime.limeEduApi.framework.common.util;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

public class ExcelDownload {

	protected static final Logger log = LoggerFactory.getLogger(ExcelDownload.class);
	
	/*
	public static void excelDownload () throws Exception{
		
		// .xlsx 확장자 지원
		
		XSSFWorkbook xssfWb = null; 
		XSSFSheet xssfSheet = null; 
		XSSFRow xssfRow = null; 
		XSSFCell xssfCell = null;

		
		try {
			
			int rowNo = 0;
			
			xssfWb = new XSSFWorkbook(); //XSSFWorkbook 객체 생성 
			xssfSheet = xssfWb.createSheet("워크 시트1"); // 워크시트 이름 설정

			// 폰트 스타일 
			XSSFFont font = xssfWb.createFont(); 
			font.setFontName(HSSFFont.FONT_ARIAL); // 폰트 스타일 
			font.setFontHeightInPoints((short)20); // 폰트 크기 
			font.setBold(true); // Bold 설정 
			font.setColor(new XSSFColor(Color.decode("#457ba2"))); // 폰트 색 지정

			//테이블 셀 스타일 
			CellStyle cellStyle = xssfWb.createCellStyle(); 
			xssfSheet.setColumnWidth(0, 
					//(xssfSheet.getColumnWidth(0))+(short)2048
					10
					); // 0번째 컬럼 넓이 조절

			cellStyle.setFont(font); // cellStyle에 font를 적용 
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 정렬

			//셀병합 
			xssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 4));

			// 타이틀 생성 
			xssfRow = xssfSheet.createRow(rowNo++); // 행 객체 추가 
			xssfCell = xssfRow.createCell((short) 0); // 추가한 행에 셀 객체 추가 
			xssfCell.setCellStyle(cellStyle); // 셀에 스타일 지정 
			xssfCell.setCellValue("타이틀 입니다"); // 데이터 입력 
			xssfSheet.createRow(rowNo++); xssfRow = xssfSheet.createRow(rowNo++); // 빈행 추가 
			
			//테이블 스타일 설정 
			CellStyle tableCellStyle = xssfWb.createCellStyle(); 
			tableCellStyle.setBorderTop((short) 5); // 테두리 위쪽 
			tableCellStyle.setBorderBottom((short) 5); // 테두리 아래쪽 
			tableCellStyle.setBorderLeft((short) 5); // 테두리 왼쪽 
			tableCellStyle.setBorderRight((short) 5); // 테두리 오른쪽 
			xssfRow = xssfSheet.createRow(rowNo++); 
			xssfCell = xssfRow.createCell((short) 0); 
			xssfCell.setCellStyle(tableCellStyle); 
			xssfCell.setCellValue("셀1"); xssfCell = xssfRow.createCell((short) 1); 
			xssfCell.setCellStyle(tableCellStyle); xssfCell.setCellValue("셀2"); 
			xssfCell = xssfRow.createCell((short) 2); 
			xssfCell.setCellStyle(tableCellStyle); 
			xssfCell.setCellValue("셀3"); 
			xssfCell = xssfRow.createCell((short) 3); 
			xssfCell.setCellStyle(tableCellStyle); 
			xssfCell.setCellValue("셀4"); 
			xssfCell = xssfRow.createCell((short) 4); 
			xssfCell.setCellStyle(tableCellStyle); 
			String localFile = "/Users/LIME/" + "excelDownTest" + ".xlsx"; 
			File file = new File(localFile); 
			FileOutputStream fos = null; 
			fos = new FileOutputStream(file); 
			xssfWb.write(fos); 
			if (fos != null) fos.close();

		}catch(Exception e) {
			
		}finally {
			
		}
		
		
	}
	*/	
	public static void excelDownload2(HttpServletResponse response, Map<String, Object> resultMap) throws Exception {
		
		File file = null;
		
		file = new File("/Users/LIME/limeCashDisbursementVoucher.xlsx");
		
		
		if(file.exists()) {

			InputStream is = new BufferedInputStream(new FileInputStream("/Users/LIME/limeCashDisbursementVoucher.xlsx"));			
			
			XLSTransformer xls = new XLSTransformer();
			Workbook workbook = xls.transformXLS(is, resultMap);
		     
			response.setHeader("Content-Disposition", "attachment; filename=\""+"filetempFile.xlsx");
			
			OutputStream os = response.getOutputStream();
			
			workbook.write(os);
			
			
			

		}
		
	}

	/**
	 * 프로젝트 워드로 다운로드
	 * @param response
	 * @param paramMap
	 * @throws Exception
	 */
	public static void projectWordDownload (HttpServletResponse response, Map<String,Object> paramMap) throws Exception{
	
	
		String header = String.valueOf(paramMap.get("header"));
		String filename = String.valueOf(paramMap.get("projectName"));
		if(header.contains("MSIE") || header.contains("Trident")) {
			filename = URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment; filename="+filename+".docx;");
		}else {
			filename = new String(filename.getBytes("UTF-8"),"ISO-8859-1");
			
			response.setHeader("Content-Disposition", "attachment; filename=\""+filename+".docx");
		}
		
		
		response.setContentType("application/octet-stream; charset=UTF-8");
		OutputStream outputStream = response.getOutputStream();
		
		XWPFDocument xwpfDocument = new XWPFDocument();
		
		XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph(); 
		xwpfParagraph.setAlignment(ParagraphAlignment.CENTER); 
		XWPFRun xwpfRun = xwpfParagraph.createRun(); 
		xwpfRun.setBold(true); 
		xwpfRun.setFontSize(26); 
		xwpfRun.setText(String.valueOf(paramMap.get("projectName")));
		
		xwpfRun.addBreak();
		

		XWPFRun bigComp = xwpfParagraph.createRun(); 
		bigComp.setFontSize(24); 
		bigComp.setText(String.valueOf(paramMap.get("bidComp")));
		bigComp.addBreak();
		
		
		XWPFRun projectDate = xwpfParagraph.createRun(); 
		projectDate.setFontSize(16); 
		projectDate.setText(String.valueOf(paramMap.get("startDate"))+" ~ "+String.valueOf(paramMap.get("endDate")));
		projectDate.addBreak();
		projectDate.addBreak();

		
		XWPFRun basicData = xwpfParagraph.createRun(); 
		basicData.setFontSize(18); 
		basicData.setBold(true);
		basicData.setText("[기본 정보]");
		basicData.addBreak();
		
		
		
		XWPFParagraph xwpfParagraphBody = xwpfDocument.createParagraph();
		xwpfParagraphBody.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun developEnviroment = xwpfParagraphBody.createRun(); 
		developEnviroment.setFontSize(16); 
		developEnviroment.setBold(true);
		developEnviroment.setText("개 발 환 경");
		developEnviroment.addBreak();
		developEnviroment.setText("-기종/   "+String.valueOf(paramMap.get("devModel")));
		developEnviroment.addBreak();
		developEnviroment.setText("-O.S/   "+String.valueOf(paramMap.get("devOs")));
		developEnviroment.addBreak();
		developEnviroment.setText("-언어/   "+String.valueOf(paramMap.get("devLanguage")));
		developEnviroment.addBreak();
		developEnviroment.setText("-DBMS/   "+String.valueOf(paramMap.get("devDbms")));
		developEnviroment.addBreak();
		developEnviroment.setText("-TOOL/   "+String.valueOf(paramMap.get("devTool")));
		developEnviroment.addBreak();
		developEnviroment.setText("-Framework/   "+String.valueOf(paramMap.get("devFramework")));
		developEnviroment.addBreak();
		developEnviroment.setText("-WAS/   "+String.valueOf(paramMap.get("devWas")));
		developEnviroment.addBreak();
		developEnviroment.setText("-플랫폼/   "+String.valueOf(paramMap.get("devPlatform")));
		developEnviroment.addBreak();
		developEnviroment.setText("-프로젝트명(업무명)/   "+String.valueOf(paramMap.get("projectName")));
		developEnviroment.addBreak();
		developEnviroment.setText("-참 여 기 간/   "+String.valueOf(paramMap.get("startDate"))+" ~ "+String.valueOf(paramMap.get("endDate")));
		developEnviroment.addBreak();
		developEnviroment.setText("-근 무 환 경/   "+String.valueOf(paramMap.get("startTime"))+" ~ "+String.valueOf(paramMap.get("endTime")));
		developEnviroment.addBreak();
		developEnviroment.setText("-근 무 지 역/   "+String.valueOf(paramMap.get("workLocation")));
		developEnviroment.addBreak();
		developEnviroment.setText("-담 당 업 무/   "+String.valueOf(paramMap.get("devWork")));
		developEnviroment.addBreak();
		developEnviroment.setText("-고 객 사/   "+String.valueOf(paramMap.get("devCustomer")));
		developEnviroment.addBreak();
		developEnviroment.setText("-수 주 사/   "+String.valueOf(paramMap.get("bidComp")));
		developEnviroment.addBreak();
		developEnviroment.setText("-팀 원 수/   "+String.valueOf(paramMap.get("memberCount"))+"명");
		developEnviroment.addBreak();
		developEnviroment.setText("-역   할/   "+String.valueOf(paramMap.get("position")));
		developEnviroment.addBreak();
		if(null == paramMap.get("devMemo")) developEnviroment.setText("-기   타/   ");
		else developEnviroment.setText("-기   타/   "+String.valueOf(paramMap.get("devMemo")));
		developEnviroment.addBreak();
		developEnviroment.addBreak();
		
		
		
		XWPFParagraph xwpfParagraphcharacteristicIntro = xwpfDocument.createParagraph();
		xwpfParagraphcharacteristicIntro.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun characteristicIntro = xwpfParagraphcharacteristicIntro.createRun(); 
		characteristicIntro.setBold(true);
		characteristicIntro.setFontSize(18);
		characteristicIntro.setText("[프로젝트의 특징과 단점]");
		characteristicIntro.addBreak();
		characteristicIntro.addBreak();
		
		XWPFParagraph xwpfParagraphcharacteristicbody = xwpfDocument.createParagraph();
		xwpfParagraphcharacteristicbody.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun characteristicbody = xwpfParagraphcharacteristicbody.createRun(); 
		characteristicbody.setFontSize(16);
		characteristicbody.setText("-특   징/    ");
		
		// 개행문자를 기준으로 줄바꿈을 하는 작업
		String charcteristic =  paramMap.get("charcteristic").toString();
		if(charcteristic.contains("\n")) {
			String [] charcteristicLines = charcteristic.split("\n");
			
			for (int i = 0; i < charcteristicLines.length ; i++) {
				String charcteristicPart = charcteristicLines[i];
				characteristicbody.setText(charcteristicPart);
				characteristicbody.addBreak();
			}
		}else {
			characteristicbody.setText(String.valueOf(paramMap.get("charcteristic")));
		}
		
		characteristicbody.addBreak();
		characteristicbody.setText("-단   점/    ");
		
		String weakness = String.valueOf(paramMap.get("weakness"));

		// 개행문자를 기준으로 줄바꿈을 하는 작업
		if(weakness.contains("\n")) {
			String [] weaknessLines = weakness.split("\n");
			
			for(int i = 0 ; i < weaknessLines.length; i++) {
				String weaknessLine = weaknessLines[i];
				characteristicbody.setText(weaknessLine);
				characteristicbody.addBreak();
			}
		}else {
			characteristicbody.setText(weakness);
		}
		
		characteristicbody.addBreak();
		characteristicbody.addBreak();
		
		
		XWPFParagraph xwpfParagraphworkFlowIntro = xwpfDocument.createParagraph();
		xwpfParagraphworkFlowIntro.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun workFlowIntro = xwpfParagraphworkFlowIntro.createRun(); 
		workFlowIntro.setBold(true);
		workFlowIntro.setFontSize(18);
		workFlowIntro.setText("[업무의 흐름 및 진행 상태]");
		workFlowIntro.addBreak();
		workFlowIntro.addBreak();
		
		XWPFParagraph xwpfParagraphworkFlow = xwpfDocument.createParagraph();
		xwpfParagraphworkFlow.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun workFlow = xwpfParagraphworkFlow.createRun(); 
		workFlow.setFontSize(16);
		
		String workFlowStr = String.valueOf(paramMap.get("workFlow"));
		
		// 개행문자를 기준으로 줄바꿈을 하는 작업
		if(workFlowStr.contains("\n")) {
			String [] workFlowStrLines = workFlowStr.split("\n");
			
			for(int i = 0 ; i < workFlowStrLines.length; i++) {
				String workFlowStrLine = workFlowStrLines[i];
				workFlow.setText(workFlowStrLine);
				workFlow.addBreak();
			}
		}else {
			workFlow.setText(workFlowStr);
		}
		workFlow.addBreak();
		workFlow.addBreak();
		
		
		XWPFParagraph xwpfParagraphdevContentIntro = xwpfDocument.createParagraph();
		xwpfParagraphdevContentIntro.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun devContentIntro = xwpfParagraphdevContentIntro.createRun(); 
		devContentIntro.setBold(true);
		devContentIntro.setFontSize(18);
		devContentIntro.setText("[개발한 화면명]");
		devContentIntro.addBreak();
		devContentIntro.addBreak();
		
		
		XWPFParagraph xwpfParagraphdevContent = xwpfDocument.createParagraph();
		xwpfParagraphdevContent.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun devContent = xwpfParagraphdevContent.createRun(); 
		devContent.setFontSize(16);
		
		String devContentStr = String.valueOf(paramMap.get("devContent"));
		
		// 개행문자를 기준으로 줄바꿈을 하는 작업
		if(devContentStr.contains("\n")) {
			String [] devContentStrLines = devContentStr.split("\n");
			
			for(int i = 0 ; i < devContentStrLines.length; i++) {
				String devContentStrLine = devContentStrLines[i];
				devContent.setText(devContentStrLine);
				devContent.addBreak();
			}
		}else {
			devContent.setText(devContentStr);
		}
		devContent.addBreak();
		devContent.addBreak();
		
		XWPFParagraph xwpfParagraphdevExprienceIntro = xwpfDocument.createParagraph();
		xwpfParagraphdevExprienceIntro.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun devExprienceIntro = xwpfParagraphdevExprienceIntro.createRun(); 
		devExprienceIntro.setBold(true);
		devExprienceIntro.setFontSize(18);
		devExprienceIntro.setText("[프로젝트 경험]");
		devExprienceIntro.addBreak();
		devExprienceIntro.addBreak();
		
		XWPFParagraph xwpfParagraphdevExprience = xwpfDocument.createParagraph();
		xwpfParagraphdevExprience.setAlignment(ParagraphAlignment.LEFT);
		XWPFRun devExprience = xwpfParagraphdevExprience.createRun(); 
		devExprience.setFontSize(16);
		
		String devExprienceStr = String.valueOf(paramMap.get("devExprience"));
		
		// 개행문자를 기준으로 줄바꿈을 하는 작업
		if(devExprienceStr.contains("\n")) {
			String [] devExprienceStrLines = devExprienceStr.split("\n");
			
			for(int i = 0 ; i < devExprienceStrLines.length; i++) {
				String devExprienceStrLine = devExprienceStrLines[i];
				devExprience.setText(devExprienceStrLine);
				devExprience.addBreak();
			}
		}else {
			devExprience.setText(devExprienceStr);
		}
		devExprience.addBreak();
		devExprience.addBreak();
		
		
		xwpfDocument.write(outputStream);
		outputStream.close();
	}
	
}
