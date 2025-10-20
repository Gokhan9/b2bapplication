package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.dto.request.CategoryCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.CategoryUpdateRequest;
import com.gokhancomert.b2bapplication.mapper.CategoryMapper;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import com.gokhancomert.b2bapplication.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CategoryCreateRequest createRequest;
    private CategoryUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Electronics", null);
        categoryDto = new CategoryDto(1L, "Electronics", null);
        createRequest = new CategoryCreateRequest("Electronics", null, true);
        updateRequest = new CategoryUpdateRequest("Updated Electronics", null);
    }
}
