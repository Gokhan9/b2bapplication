package com.gokhancomert.b2bapplication.controller.admin;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.dto.request.CategoryCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.CategoryUpdateRequest;
import com.gokhancomert.b2bapplication.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Yeni Kategori Ekleyebilsin
    @PostMapping
    public CategoryDto createCategory(@RequestBody CategoryCreateRequest categoryCreateRequest) {
        return categoryService.createCategory(categoryCreateRequest);
    }

    // Kategorileri güncelleyebilsin.
    @PutMapping("/{id}")
    public CategoryDto updateCategoryById(@PathVariable Long id,
                                   @RequestBody CategoryUpdateRequest categoryUpdateRequest) {
        return categoryService.updateCategory(id, categoryUpdateRequest);
    }

    // Kategoriyi silebilsin.
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
