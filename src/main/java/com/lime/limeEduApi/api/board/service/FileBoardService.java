package com.lime.limeEduApi.api.board.service;


import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.dto.BoardDto;


public interface FileBoardService {

	public int insertFile(BoardDto boardDto, MultipartFile file) throws Exception;
	public File downloadImageFile(BoardDto boardDto);
}
