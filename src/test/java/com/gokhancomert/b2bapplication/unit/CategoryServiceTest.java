package com.gokhancomert.b2bapplication.unit;

import com.gokhancomert.b2bapplication.dto.CategoryDto;
import com.gokhancomert.b2bapplication.dto.request.CategoryCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.CategoryUpdateRequest;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.CategoryMapper;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import com.gokhancomert.b2bapplication.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    //Tüm Kategorileri Getirme: getAllCategories metodunun, veritabanındaki tüm kategorileri CategoryDto listesi olarak doğru bir şekilde döndürmek.
    @Test
    void getAllCategories_shouldReturnAllCategoriesAsDtoList() {

        //given
        List<Category> categoryList = Arrays.asList(category, new Category(2L, "Books", null));
        List<CategoryDto> categoryDtoList = Arrays.asList(categoryDto, new CategoryDto(2L, "Books", null));

        //when
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toDto(new Category(2L, "Books", null))).thenReturn(new CategoryDto(2L, "Books", null));

        //then
        List<CategoryDto> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());

        //verify
        verify(categoryRepository, times(1)).findAll(); //1 kez çağrılmış olmalı.
        verify(categoryMapper, times(2)).toDto(any(Category.class)); //2 kez çağrılmış olmalı (çünkü iki kategori var).
    }

    //ID ile Kategori Getirme (Başarılı): getCategoryById metodunun, mevcut bir ID verildiğinde ilgili CategoryDto nesnesini döndürmesi.
    @Test
    void getCategoryById_shouldReturnCategoryDto_whenCategoryExists() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Electronics", result.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryMapper, times(1)).toDto(category);
    }

    //ID ile Kategori Getirme (Başarısız): getCategoryById metodunun, mevcut olmayan bir ID verildiğinde ResourceNotFoundException gönder.
    @Test
    void getCategoryById_shouldThrowResourceNotFoundException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(99L));

        verify(categoryRepository, times(1)).findById(99L);
        verify(categoryMapper, never()).toDto(any(Category.class));
    }

    //Yeni Kategori Oluşturma: createCategory metodunun, yeni bir kategori oluşturma isteğini doğru bir şekilde işleyip oluşturulan CategoryDto'yu döndürdüğünü test et.
    @Test
    void createCategory_shouldReturnCreatedCategoryDto() {

        Category newCategory = new Category(null, createRequest.getName(), null);
        Category savedCategory = new Category(1L, createRequest.getName(), null);
        CategoryDto savedCategoryDto = new CategoryDto(1L, createRequest.getName(), null);

        when(categoryMapper.toCategory(createRequest)).thenReturn(newCategory);
        when(categoryRepository.save(newCategory)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto result = categoryService.createCategory(createRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());

        verify(categoryMapper, times(1)).toCategory(createRequest);
        verify(categoryRepository, times(1)).save(newCategory);
        verify(categoryMapper, times(1)).toDto(savedCategory);
    }

    //Kategori Güncelleme (Başarılı): updateCategory metodunun, mevcut bir kategoriyi verilen bilgilerle güncelleyip güncellenmiş CategoryDto'yu döndürdüğünü test et.
    @Test
    void updateCategory_shouldReturnUpdatedCategoryDto_whenCategoryExists() {

        Category existingCategory = new Category(1L, "Electronics", null);
        Category updatedCategory = new Category(1L, updateRequest.getName(), null);
        CategoryDto updatedCategoryDto = new CategoryDto(1L, updateRequest.getName(), null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);

        CategoryDto result = categoryService.updateCategory(1L, updateRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Updated Electronics", result.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    //Kategori Güncelleme (Başarısız): updateCategory metodunun, mevcut olmayan bir kategoriyi güncellemeye çalışırken ResourceNotFoundException fırlattığını test et.
    @Test
    void updateCategory_shouldThrowResourceNotFoundException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(99L, updateRequest));

        verify(categoryRepository, times(1)).findById(99L);
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).toDto(any(Category.class));
    }

    //Kategori Silme (Başarılı): deleteCategory metodunun, mevcut bir kategoriyi başarıyla sildiğini ve herhangi bir hata döndürmediğini test et.
    @Test
    void deleteCategoryById_shouldDeleteCategory_whenCategoryExists() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(category);
    }

    //Kategori Silme (Başarısız): deleteCategory metodunun, mevcut olmayan bir kategoriyi silmeye çalışırken ResourceNotFoundException fırlattığını test et.
    @Test
    void deleteCategoryById_shouldThrowResourceNotFoundException_whenCategoryDoesNotExist() {

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategoryById(99L));

        verify(categoryRepository, times(1)).findById(99L);
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
