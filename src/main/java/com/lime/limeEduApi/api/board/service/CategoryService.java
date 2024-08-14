package com.lime.limeEduApi.api.board.service;


import com.lime.limeEduApi.api.board.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
	public int insertCategory(CategoryDto categoryDto);
	public int updateCategory(CategoryDto categoryDto);
	public List<CategoryDto> selectCategoryAll();
	public CategoryDto selectOneCategory(CategoryDto categoryDto);
}
