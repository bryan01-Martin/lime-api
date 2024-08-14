package com.lime.limeEduApi.framework.common.domain;

import java.util.HashMap;

import lombok.Data;

@Data
public class MessageVO {
	public static String saveS = "저장에 성공하였습니다.";
	public static String saveF = "저장에 실패하였습니다.";
	public static String updateS = "수정에 성공하였습니다.";
	public static String updateF = "수정에 실패하였습니다.";
	public static String deleteS = "삭제에 성공하였습니다.";
	public static String deleteF = "삭제에 실패하였습니다.";
	public static String searchS = "조회에 성공하였습니다.";
	public static String searchF = "조회에 실패하였습니다.";
	
	public HashMap<String,String> getMessage(){
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("save.success", saveS);
		map.put("save.fail", saveF);
		map.put("update.success", updateS);
		map.put("update.fail", updateF);
		map.put("delete.success", deleteS);
		map.put("delete.fail", deleteF);
		map.put("search.success", searchS);
		map.put("search.fail", searchF);
		return map;
	}

}