package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.dto.request.ProductCreateRequest;
import com.gokhancomert.b2bapplication.exception.ResourceNotFoundException;
import com.gokhancomert.b2bapplication.mapper.ProductMapper;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.repository.CategoryRepository;
import com.gokhancomert.b2bapplication.repository.ProductRepository;
import com.gokhancomert.b2bapplication.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_whenProductIsSavedSuccessfully_shouldReturnSavedProduct() {

        // Given
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(1L);

        // When
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toProduct(createRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(expectedDto);

        // Then
        ProductDto result = productService.createProduct(createRequest);

        // Verify
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }

    @Test
    void getById_whenProductDoesNotExist_shouldThrowResourceNotFoundException() {

        // Given: Sadece var olmayan bir ürün ID'si (nonExistentId) tanımlıyoruz.
        final Long nonExistentId = 999L;

        // When: productRepository'nin findById metodu bu ID ile çağrıldığında, "kayıt bulunamadı" anlamına gelen Optional.empty() döndürmesini sağlıyoruz.
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Then & Verify: assertThrows kullanarak productService.findByProductId metodunun ResourceNotFoundException fırlattığını tek adımda hem çalıştırıyor hem de doğruluyoruz.
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findByProductId(nonExistentId);
        });
    }

    @Test
    void getById_whenProductExists_shouldReturnProduct() {

        // Given
        final Long productId = 1L;
        Product foundProduct = new Product();
        foundProduct.setId(productId);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(productId);

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));
        when(productMapper.toDto(foundProduct)).thenReturn(expectedDto);

        // Then
        ProductDto result = productService.findByProductId(productId);

        // Verify
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }
}
