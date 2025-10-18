package com.gokhancomert.b2bapplication;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.dto.request.ProductCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.ProductUpdateRequest;
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
import static org.mockito.Mockito.verify;
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

    @Test
    void update_whenProductExists_shouldReturnUpdatedProduct() {

        // Given - Hazırlık Aşaması
        final Long existingId = 1L; //Güncellemek istediğimiz, veritabanında "var olduğunu" varsaydığımız ürünün ID'sini tanımlıyoruz.
        //Servisimize göndereceğimiz "güncelleme isteğini" temsil eden nesneyi oluşturuyoruz. İçine ürünün yeni adı olarak "Updated Name" değerini koyuyoruz.
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setCategoryId(1L);
        updateRequest.setName("Updated Name");

        //Bu, ürünün veritabanındaki güncellenmeden önceki halini temsil ediyor. Adının "Old Name" olduğuna dikkat edin. findById metodu çağrıldığında repository'nin bu nesneyi döndürmesini
        //     sağlayacağız.
        Product existingProduct = new Product();
        existingProduct.setId(existingId);
        existingProduct.setName("Old Name");

        //sahte bir Category nesnesi oluşturuyoruz.
        Category category = new Category();
        category.setId(1L);

        //Testin sonunda, updateProduct metodunun bize geri döndürmesini beklediğimiz sonucu (DTO) hazırlıyoruz. Bu nesnenin adı, isteğimizdeki gibi "Updated Name" olmalı
        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(existingId);
        expectedDto.setName("Updated Name");

        // When - Davranış Belirleme Aşaması
        when(productRepository.findById(existingId)).thenReturn(Optional.of(existingProduct));      //Eğer productRepository'nin findById metodu 1L ID'si ile çağrılırsa, ona existingProduct nesnemizi (yani ürünün eski halini) içeren bir Optional döndür" diyoruz. Bu, ürünün veritabanında bulunduğu senaryoyu canlandırır.
        when(categoryRepository.findById(updateRequest.getCategoryId())).thenReturn(Optional.of(category)); //Benzer şekilde, kategori deposu 1L ID'si ile sorgulandığında, hazırladığımız sahte category nesnesini döndürmesini söylüyoruz.
        when(productRepository.save(existingProduct)).thenReturn(existingProduct); //Servis, ürünü güncelledikten sonra save metodunu çağırdığında, repository'nin bu kaydı başarıyla yaptığını ve güncellenmiş nesneyi geri döndürdüğünü varsayıyoruz.
        when(productMapper.toDto(existingProduct)).thenReturn(expectedDto); //Son adımda, servis productMapper'dan kaydettiği ürünü DTO'ya çevirmesini istediğinde, mapper'ın bizim hazırladığımız expectedDto'yu (yani "Updated Name" içeren nesneyi) döndürmesini sağlıyoruz.

        // Then - Çalıştırma Aşaması
        ProductDto result = productService.updateProduct(existingId,updateRequest);

        // Verify - Doğrulama Aşaması
        assertNotNull(result); //Metodun null bir sonuç döndürmediğinden emin oluyoruz.
        assertEquals(expectedDto.getName(), result.getName()); //Dönen sonucun (result) adının, bizim beklediğimiz expectedDto'nun adıyla ("Updated Name") aynı olup olmadığını kontrol ediyoruz. Bu, güncellemenin başarılı olduğunun en önemli kanıtıdır.

        // Ayrıca mapper'ın update metodunun çağrıldığını da doğrulayabiliriz
        verify(productMapper).updateProductFromDto(updateRequest, existingProduct); //Bu özel bir Mockito doğrulama metodudur. "Ben bu testin sonunda, productMapper'ın updateProductFromDto metodunun, updateRequest ve existingProduct parametreleriyle en az bir kez çağrıldığından emin olmak istiyorum" demektir. Bu, servisin sadece sonucu doğru üretmekle kalmayıp, bunu yaparken doğru adımları izlediğini (yani mapper'ı kullandığını) da garanti altına alır.
    }

    @Test
    void delete_whenProductExists_shouldDeleteSuccessfully() {

        // Given
        final Long existingId = 1L;

        // When
        when(productRepository.existsById(existingId)).thenReturn(true);

        // Then
        productService.deleteProductById(existingId);

        // Verify
        verify(productRepository).deleteById(existingId);
    }

    @Test
    void delete_whenProductDoesNotExist_shouldThrowResourceNotFoundException() {

        //Given
        final Long nonExistendId = 999L;

        //When
        //Ürünün var olmadığını simüle edeceğiz.
        when(productRepository.existsById(nonExistendId)).thenReturn(false);

        //Then
        //Servisin, ürün olmadığında ResourceNotFoundException fırlattığını doğrulayalım..
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProductById(nonExistendId);
        });
    }
}
