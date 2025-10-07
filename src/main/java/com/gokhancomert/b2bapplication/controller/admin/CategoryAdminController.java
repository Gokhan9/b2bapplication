package com.gokhancomert.b2bapplication.controller.admin;

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
    public Category createCategory(@RequestBody Category category) {
        return categoryService.saveAllCategory(category);
    }

    // Kategorileri güncelleyebilsin.
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id,
                                   @RequestBody Category category) {
        category.setId(id);
        return categoryService.saveAllCategory(category);
    }

    // Kategoriyi silebilsin.
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteAllCategory(id);
    }
}
