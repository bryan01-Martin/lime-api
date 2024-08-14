package com.lime.limeEduApi.framework.common.service;

import java.awt.Color;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.springframework.stereotype.Service;
 
@Service
public class ExcelService {

	/**
	 * 기본 엑셀 생성
	 * @param model
	 * @param workbook
	 */
	@SuppressWarnings("unchecked")
	public void createDefaultWorkbook(Map<String, Object> model, Workbook workbook) {
		List<Map<String,Object>> sheetMapList = (List<Map<String,Object>>) model.get("sheetMapList");
		for(Map<String,Object> sheetMap : sheetMapList) {
			List<LinkedHashMap<String,Object>> headerMapList = (List<LinkedHashMap<String,Object>>) sheetMap.get("headerMapList");
			List<Map<String, Object>> data = (List<Map<String, Object>>) sheetMap.get("data");
			Map<String,Object> styleMap = (Map<String, Object>) sheetMap.get("styles")==null?
											createStyles(workbook):(Map<String, Object>) sheetMap.get("styles");
			final XSSFCellStyle headerStyle = (XSSFCellStyle) styleMap.get("headerStyle"); //헤더 스타일
			final XSSFCellStyle oddStyle = (XSSFCellStyle) styleMap.get("oddStyle");		//홀수행
			final XSSFCellStyle evenStyle = (XSSFCellStyle) styleMap.get("evenStyle");	//짝수행
			Sheet sheet = workbook.createSheet();
			int rowIdx = 1;
			
			if(headerMapList!= null && headerMapList.size() > 0) {
				rowIdx = drawHeader(sheet, rowIdx, headerMapList, headerStyle);
			}
			
			LinkedHashMap<String,Object> headerMap = null;
			if(headerMapList.size()>0)
				headerMap  = headerMapList.get(headerMapList.size()-1);
			
			rowIdx = drawData(sheet, rowIdx, data, headerMap, oddStyle, evenStyle);
		}
	}
	
	/**
	 * 헤더그리기
	 * @param sheet
	 * @param rowIdx
	 * @param headerMapList
	 * @param headerStyle
	 */
	public int drawHeader(Sheet sheet,int rowIdx, List<LinkedHashMap<String,Object>> headerMapList, XSSFCellStyle headerStyle) {
		Row row;
		Cell cell;
		int colIdx = 0;
		Iterator<String> iter = null;
		Iterator<String> tempIter = null;
		//header 그리기
		for(LinkedHashMap<String,Object> headerMap: headerMapList) { // headerMapList 에있는 값을 headerMap 에 치환 
			row = sheet.createRow(rowIdx++); // 
			colIdx = 1;
			iter = headerMap.keySet().iterator();
			if(tempIter==null) {headerMap.keySet().iterator();}
			while(iter.hasNext()) { //헤더 사이즈 만큼 헤더 작성
				String key = iter.next();
				cell = row.createCell(colIdx++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue((String)headerMap.get(key));
			}
		}
		return rowIdx;
	}
	
	/**
	 * 데이터 그리기
	 * @param sheet
	 * @param rowIdx
	 * @param data
	 * @param headerMap
	 * @param oddStyle
	 * @param evenStyle
	 * @return
	 */
	public int drawData(Sheet sheet, 
						int rowIdx, 
						List<Map<String,Object>> data, 
						LinkedHashMap<String,Object> headerMap 
						,XSSFCellStyle oddStyle
						,XSSFCellStyle evenStyle) {
		Row row;
		Cell cell;
		int firstRow = 1;
		int firstCol = 1;
		int colIdx = 1;
		int lastCol = 1;
		firstRow = rowIdx-1;
		Iterator<String> iter = null;
		for(Map<String,Object> rowData: data) { // 데이터 사이즈 만큼 데이터
			row = sheet.createRow(rowIdx++);
			colIdx = 1;
			iter = headerMap!=null ? headerMap.keySet().iterator() : rowData.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				if(lastCol < colIdx) {
					lastCol = colIdx;
				}
				cell = row.createCell(colIdx++);
				XSSFCellStyle nowStyle = oddStyle;
				if(( rowIdx % 2 ) == 0) {
					nowStyle = evenStyle;
				}
				Object val = rowData.get(key);
				setCellValue(cell, val, nowStyle);
			} 
		}
		// Auto size the column widths
		for(int columnIndex = firstCol; columnIndex <= lastCol; columnIndex++) {
		     sheet.autoSizeColumn(columnIndex);
		}
		sheet.setAutoFilter(new CellRangeAddress(firstRow, sheet.getLastRowNum(), firstCol, lastCol));
		return rowIdx;
	}
	
	/**
	 * 셀에 값셋팅
	 * @param cell
	 * @param val
	 * @param nowStyle
	 */
	public void setCellValue(Cell cell, Object val, XSSFCellStyle nowStyle) {
		if(val == null) {
			val = "";
		}
		switch(val.getClass().getName()){
			case "java.sql.Timestamp" :
				Timestamp timeVal = (Timestamp)val; 
				SimpleDateFormat fullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				val = fullDate.format(new Date(timeVal.getTime())).replace(" 00:00:00","");
				nowStyle.setAlignment(HorizontalAlignment.CENTER);
				break;
			case "java.lang.Integer" :
				int num = (Integer)val;
				DecimalFormat formatter = new DecimalFormat("###,###");
				val = formatter.format(num);
				nowStyle.setAlignment(HorizontalAlignment.RIGHT);
				break;
			default:
				nowStyle.setAlignment(HorizontalAlignment.LEFT);
				break;
		}
		cell.setCellStyle(nowStyle);
		cell.setCellValue(val.toString());
	}
	
	
	public Map<String,Object> createStyles(Workbook workbook) {
		Map<String,Object> styles= new HashMap<String,Object>();
		styles.put("titleStyle", createTitleCellStyle(workbook));
		styles.put("headerStyle", createHeaderCellStyle(workbook));
		styles.put("oddStyle", createOddCellStyle(workbook));
		styles.put("evenStyle", createEvenCellStyle(workbook));
		return styles;
	}
	
	public XSSFCellStyle createTitleCellStyle(Workbook workbook) {
		XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		return titleStyle;
	}
	
	public XSSFCellStyle createHeaderCellStyle(Workbook workbook) {
		XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontName("맑은 고딕");
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		headerStyle.setFont(font);
		setBorderColor(headerStyle, 14,44,68);
		setCustomForegroundColor(headerStyle, 14,44,68);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		return headerStyle;
	}
	
	public XSSFCellStyle createOddCellStyle(Workbook workbook) {
		XSSFCellStyle oddStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontName("맑은 고딕");
		font.setColor(IndexedColors.BLACK.getIndex());
		oddStyle.setFont(font);
		setBorderColor(oddStyle, 14,44,68);
		setCustomForegroundColor(oddStyle, 210,210,210);

		oddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return oddStyle;
	}
	
	public XSSFCellStyle createEvenCellStyle(Workbook workbook) {
		XSSFCellStyle evenStyle = (XSSFCellStyle) workbook.createCellStyle();
		XSSFFont font = (XSSFFont) workbook.createFont();
		font.setFontName("맑은 고딕");
		font.setColor(IndexedColors.BLACK.getIndex());
		evenStyle.setFont(font);
		setBorderColor(evenStyle, 14,44,68);
		evenStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		evenStyle.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
		evenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return evenStyle;
	}
	
	/**
	 * style 배경색 지정
	 * @param style
	 * @param r
	 * @param g
	 * @param b
	 */
	@SuppressWarnings("deprecation")
	public void setBorderColor(XSSFCellStyle style, int r,int g,int b) {
		XSSFColor color = new XSSFColor(new Color(r,g,b));
		style.setFillForegroundColor(color);
		style.setBorderColor(BorderSide.TOP, color);
		style.setBorderColor(BorderSide.BOTTOM, color);
		style.setBorderColor(BorderSide.LEFT, color);
		style.setBorderColor(BorderSide.RIGHT, color);
	}
	
	/**
	 * style 배경색 지정
	 * @param style
	 * @param r
	 * @param g
	 * @param b
	 */
	@SuppressWarnings("deprecation")
	public void setCustomForegroundColor(XSSFCellStyle style, int r,int g,int b) {
		XSSFColor myColor = new XSSFColor(new Color(r,g,b));
		style.setFillForegroundColor(myColor);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	}
}
	
