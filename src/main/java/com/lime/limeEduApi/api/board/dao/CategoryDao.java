package com.lime.limeEduApi.api.board.dao;


import com.lime.limeEduApi.api.board.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDao {
	//게시판 글
	public int insertCategory(CategoryDto categoryDto);
	public int updateCategory(CategoryDto categoryDto);
	public List<CategoryDto> selectCategoryAll();
	public CategoryDto selectOneCategory(CategoryDto categoryDto);
}
