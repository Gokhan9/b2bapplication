package com.gokhancomert.b2bapplication.controller.admin;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.model.Category;
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
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    // Kategorileri güncelleyebilsin.
    @PutMapping("/{id}")
    public CategoryDto updateCategoryById(@PathVariable Long id,
                                   @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        return categoryService.updateCategory(id, categoryDto);
    }

    // Kategoriyi silebilsin.
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
