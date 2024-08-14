package com.lime.limeEduApi.api.board.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.domain.BoardContentListReq;
import com.lime.limeEduApi.api.board.dto.ContentDto;

public interface ContentService{
	public List<ContentDto> selectContentList(BoardContentListReq boardContentListReq);
	public int getContentListTotalCnt(BoardContentListReq boardContentListReq);
	public void createContent(ContentDto contentDto);
	public ContentDto getContentInfo(int seq);
	public int modContent(ContentDto contentDto);
	public int createContent(ContentDto contentDto, MultipartFile file) throws IOException;
	public int modContent(ContentDto contentDto, MultipartFile file) throws IOException;
	
}
