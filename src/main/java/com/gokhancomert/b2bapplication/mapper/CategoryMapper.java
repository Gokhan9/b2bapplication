package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.request.CategoryRequest;
import com.gokhancomert.b2bapplication.response.CategoryResponse;
import com.gokhancomert.b2bapplication.model.Category;

public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toEnntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }
}
