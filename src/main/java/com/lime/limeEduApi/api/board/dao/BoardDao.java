package com.lime.limeEduApi.api.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.board.dto.BoardDto;

@Mapper
public interface BoardDao {
	public int createBoard(BoardDto boardDto);
	public List<BoardDto> getBoardList(BoardDto boardDto);
	public BoardDto getBoardInfo(int seq);
	public int modBoard(BoardDto boardDto);
	public int deleteBoard(int seq);
}
