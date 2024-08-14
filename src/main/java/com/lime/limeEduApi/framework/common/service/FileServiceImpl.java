package com.lime.limeEduApi.framework.common.service;

import com.lime.limeEduApi.framework.common.dao.FileDAO;
import com.lime.limeEduApi.framework.common.util.FtpFileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(transactionManager = "transactionManager",rollbackFor = Exception.class)
public class FileServiceImpl implements FileService{
	// Set the prompt when logging in for the first time. Optional value: (ask | yes | no)
	private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

	private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Autowired
	private FtpFileTransfer ftpFileTransfer;

	@Autowired
	private FileDAO fileDAO;

	@Override
	public HashMap<String, Object> selectFile(HashMap<String, Object> params) throws Exception {
		return fileDAO.selectFile(params);
	}

	@Override
	public HashMap<String,Object> insertFileListForMultiField(HttpServletRequest request, String filePath) throws Exception {
		List<HashMap<String, Object>> map = ftpFileTransfer.ftpFileUpload(request, filePath);
		HashMap<String,Object> resultMap = new HashMap<>();
		List<String> fieldList = new ArrayList<>();
		List<Integer> fileSeqList = new ArrayList<>();
		for (HashMap<String, Object> file : map) {
			file.put("REG_SEQ", "");
			insertFile(file);
			HashMap<String,Object> result = new HashMap<>();
			fileSeqList.add((Integer) file.get("FILE_SEQ"));
			fieldList.add((String) file.get("FIELD_NAME"));
		}
		resultMap.put("fieldList", fieldList);
		resultMap.put("fileSeqList", fileSeqList);
		return resultMap;
	}

	@Override
	public List<Integer> insertFileList(HttpServletRequest request, String filePath) throws Exception {
		List<HashMap<String, Object>> map = ftpFileTransfer.ftpFileUpload(request, filePath);
		List<Integer> fileSeqList = new ArrayList<>();
		for (HashMap<String, Object> file : map) {
			file.put("REG_SEQ", "");
			insertFile(file);
			fileSeqList.add((Integer) file.get("FILE_SEQ"));
		}
		return fileSeqList;
	}

	@Override
	public Integer insertFile(HashMap<String, Object> file) throws Exception {
		fileDAO.insertFile(file);
		return (Integer) file.get("FILE_SEQ");
	}

	@Override
	public int deleteFile(HashMap<String,Object> params) throws Exception {
		return 0; //fileDAO.deleteFile(params);
	}
}
