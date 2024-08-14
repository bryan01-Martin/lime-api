package com.lime.limeEduApi.api.board.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lime.limeEduApi.api.board.dao.BoardDao;
import com.lime.limeEduApi.api.board.dto.BoardDto;
import com.lime.limeEduApi.api.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
	
	private final BoardDao boardDao;
	
	@Override
	public int createBoard(BoardDto boardDto) {
		return boardDao.createBoard(boardDto);
	}
	
	@Override
	public List<BoardDto> getBoardList(BoardDto boardDto) {
		return boardDao.getBoardList(boardDto); 
	}
	
	@Override
	public BoardDto getBoardInfo(int seq) {
		return boardDao.getBoardInfo(seq); 
	}
	
	@Override
	public int modBoard(BoardDto boardDto) {
		return boardDao.modBoard(boardDto);
	}
	
	@Override
	public int deleteBoard(int seq) {
		return boardDao.deleteBoard(seq);
	}
}