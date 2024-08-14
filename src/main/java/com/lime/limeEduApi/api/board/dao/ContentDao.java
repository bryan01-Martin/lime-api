package com.lime.limeEduApi.api.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.board.domain.BoardContentListReq;
import com.lime.limeEduApi.api.board.dto.ContentDto;

@Mapper
public interface ContentDao {
	public List<ContentDto> selectContentList(BoardContentListReq boardContentListReq);
	public int getContentListTotalCnt(BoardContentListReq boardContentListReq);
	public void createContent(ContentDto contentDto);
	public ContentDto getContentInfo(int seq);
	public int modContent(ContentDto contentDto);
}
