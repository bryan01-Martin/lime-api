package com.lime.limeEduApi.framework.common.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileDAO {
	public HashMap<String,Object> selectFile(HashMap<String,Object> params) throws Exception;
	public int insertFile(HashMap<String,Object> params) throws Exception;
	public int deleteFile(HashMap<String,Object> params) throws Exception;
}

