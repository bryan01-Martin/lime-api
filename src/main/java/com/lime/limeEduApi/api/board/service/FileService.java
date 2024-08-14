package com.lime.limeEduApi.api.board.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.dto.ContentDto;
import com.lime.limeEduApi.api.board.dto.FileDto;

public interface FileService {
	public FileDto selectFile(int seq);
	public int insertFile(FileDto fileDto);
	public int deleteFile(FileDto fileDto);
	public int updateFile(ContentDto contentDto, MultipartFile mfile) throws IOException;
	public int downloadFile(int seq) throws IOException;
}
