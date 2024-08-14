package com.lime.limeEduApi.api.board.service;

import java.util.List;

import com.lime.limeEduApi.api.board.dto.BoardDto;

public interface BoardService{
	public int createBoard(BoardDto boardDto);
	public List<BoardDto> getBoardList(BoardDto boardDto);
	public BoardDto getBoardInfo(int seq);
	public int modBoard(BoardDto boardDto);
	public int deleteBoard(int seq);
}