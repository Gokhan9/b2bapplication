package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Listelenen kategorileri getir.
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // ID ile Kategori bul.
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Kategoriyi Kaydet
    public Category saveAllCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Kategoriyi Sil
    public void deleteAllCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
