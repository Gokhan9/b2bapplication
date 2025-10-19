package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.dto.request.CategoryCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.CategoryUpdateRequest;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.CategoryMapper;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDto> findAll() {
        logger.info("Attempting to find all categories");
        List<Category> categories = categoryRepository.findAll();
        logger.info("Found {} categories", categories.size());
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        logger.info("Attempting to get category by id: {}", id);
        return categoryRepository.findById(id)
                .map(category -> {
                    logger.info("Found category with id: {}", id);
                    return categoryMapper.toDto(category);
                })
                .orElseThrow(() -> {
                    logger.warn("Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });
    }

    public CategoryDto createCategory(CategoryCreateRequest categoryCreateRequest) {
        logger.info("Attempting to create category with name: {}", categoryCreateRequest.getName());
        Category category = categoryMapper.toCategory(categoryCreateRequest);

        Category savedCategory = categoryRepository.save(category);
        logger.info("Created category with id: {} and name: {}", savedCategory.getId(), savedCategory.getName());
        return categoryMapper.toDto(savedCategory);
    }

    public CategoryDto updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        logger.info("Attempting to update category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Updated Failed. Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        category.setName(categoryUpdateRequest.getName());

        Category updatedCategory = categoryRepository.save(category);
        logger.info("Successfully Updated category with id: {}", id);
        return categoryMapper.toDto(updatedCategory);
    }

    public void deleteCategoryById(Long id) {
        logger.info("Attempting to delete category with id: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Deleted failed. Category not found with id: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        categoryRepository.delete(category);
        logger.info("Successfully deleted category with id: {}", id);
    }
}
