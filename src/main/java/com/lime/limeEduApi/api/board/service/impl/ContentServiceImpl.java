package com.lime.limeEduApi.api.board.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lime.limeEduApi.api.board.dao.ContentDao;
import com.lime.limeEduApi.api.board.domain.BoardContentListReq;
import com.lime.limeEduApi.api.board.dto.ContentDto;
import com.lime.limeEduApi.api.board.dto.FileDto;
import com.lime.limeEduApi.api.board.service.ContentService;
import com.lime.limeEduApi.api.board.service.FileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService{
	private final ContentDao contentDao;
	private final FileService fileService;
	
	@Override
	public List<ContentDto> selectContentList(BoardContentListReq boardContentListReq) {
		return contentDao.selectContentList(boardContentListReq);
	}
	
	@Override
	public int getContentListTotalCnt(BoardContentListReq boardContentListReq) {
		return contentDao.getContentListTotalCnt(boardContentListReq);
	}
	
	@Override
    public void createContent(ContentDto contentDto){
		contentDao.createContent(contentDto);
    }
	
	@Override
    public ContentDto getContentInfo(int seq){
		return contentDao.getContentInfo(seq);
    }
	
	@Override
    public int modContent(ContentDto contentDto){
		return contentDao.modContent(contentDto);
    }
	
	@Override
	@Transactional(rollbackFor = Exception.class)
    public int createContent(ContentDto contentDto, MultipartFile file) throws IOException{
		createContent(contentDto);
		if(file != null && !file.isEmpty()) {
			fileService.updateFile(contentDto, file);
		}
		return 1;
    }
		
	
	  @Override
	  @Transactional(rollbackFor = Exception.class) 
	  public int modContent(ContentDto contentDto, MultipartFile file) throws IOException{
		  modContent(contentDto); 
		  if(file != null && !file.isEmpty()) { 
			  fileService.deleteFile(FileDto.builder() 
					  				.contentSeq(contentDto.getSeq()) 
					  				.build());
			  fileService.updateFile(contentDto, file); 
		  } 
		  return 0; 
	  }
	 
}
