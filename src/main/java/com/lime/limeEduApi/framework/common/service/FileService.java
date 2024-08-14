package com.lime.limeEduApi.framework.common.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
	public HashMap<String,Object> selectFile(HashMap<String,Object> params) throws Exception;
	public List<Integer> insertFileList(HttpServletRequest request, String filePath) throws Exception;
	public HashMap<String,Object> insertFileListForMultiField(HttpServletRequest request, String filePath) throws Exception;
	public Integer insertFile(HashMap<String, Object> file) throws Exception;
	public int deleteFile(HashMap<String, Object> params) throws Exception;

}
