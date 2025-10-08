package com.gokhancomert.b2bapplication.dto.mapper;

import com.gokhancomert.b2bapplication.dto.request.CategoryRequest;
import com.gokhancomert.b2bapplication.dto.response.CategoryResponse;
import com.gokhancomert.b2bapplication.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getCategoryName());
        category.setDescription(request.getCategoryDescription());
        return category;
    }

    public static CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setCategoryName(category.getName());
        response.setCategoryDescription(category.getDescription());
        return response;
    }
}
