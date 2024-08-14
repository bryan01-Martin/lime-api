package com.lime.limeEduApi.api.board.service.impl;

import com.lime.limeEduApi.api.board.dao.CategoryDao;
import com.lime.limeEduApi.api.board.dto.CategoryDto;
import com.lime.limeEduApi.api.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryDao categoryDao;

	@Override
	public int insertCategory(CategoryDto categoryDto) {
		return categoryDao.insertCategory(categoryDto);
	}

	@Override
	public int updateCategory(CategoryDto categoryDto) {
		return categoryDao.updateCategory(categoryDto);
	}

	@Override
	public List<CategoryDto> selectCategoryAll() {
		return categoryDao.selectCategoryAll();
	}

	@Override
	public CategoryDto selectOneCategory(CategoryDto categoryDto) {
		return categoryDao.selectOneCategory(categoryDto);
	}
	
}
