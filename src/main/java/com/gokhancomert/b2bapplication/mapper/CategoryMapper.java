package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.dto.request.CategoryCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.CategoryUpdateRequest;
import com.gokhancomert.b2bapplication.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toCategory(CategoryCreateRequest request);

    Category toCategory(CategoryUpdateRequest request);
}


    
