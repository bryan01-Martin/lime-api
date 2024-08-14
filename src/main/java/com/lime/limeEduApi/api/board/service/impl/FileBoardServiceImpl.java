package com.lime.limeEduApi.api.board.service.impl;

import com.lime.limeEduApi.api.board.dao.BoardDao;
import com.lime.limeEduApi.api.board.domain.BoardContentListReq;
import com.lime.limeEduApi.api.board.dto.BoardDto;
import com.lime.limeEduApi.api.board.service.BoardService;
import com.lime.limeEduApi.api.board.service.FileBoardService;
import com.lime.limeEduApi.api.code.dao.CodeDao;
import com.lime.limeEduApi.api.code.domain.CodeListByDepthReqDomain;
import com.lime.limeEduApi.api.code.domain.CodeListReqDomain;
import com.lime.limeEduApi.api.code.dto.CodeDto;
import com.lime.limeEduApi.api.code.service.CodeService;
import com.lime.limeEduApi.framework.config.FtpConfig;

import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
@Service
@RequiredArgsConstructor
public class FileBoardServiceImpl implements FileBoardService {
	
	private final BoardDao boardDao;
	private final FtpConfig ftpConfig;
	
	private String targetPath = "/IMAGE";

	@Override
	public int insertFile(BoardDto boardDto, MultipartFile file) throws Exception {
		String fileName = boardDto.getOriginalFileName();
		uploadFile(fileName,file);
		return boardDao.insertFileContent(boardDto);
		
	}
	
	public boolean uploadFile(String fileName, MultipartFile file){
		boolean result = false;
		try {
			result = ftpConfig.uploadFile(targetPath, fileName, file);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	@Override
	public File downloadImageFile(BoardDto boardDto){
		try {
			File tempFile = ftpConfig.getFile(targetPath, boardDto.getOriginalFileName());
			return tempFile;
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error occurred", e);
		}
	}
}
