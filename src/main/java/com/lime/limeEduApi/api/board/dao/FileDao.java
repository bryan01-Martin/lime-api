package com.lime.limeEduApi.api.board.dao;

import org.apache.ibatis.annotations.Mapper;

import com.lime.limeEduApi.api.board.dto.FileDto;

@Mapper
public interface FileDao {
	public FileDto selectFile(int seq);
	public int insertFile(FileDto fileDto);
	public int deleteFile(FileDto fileDto);
}