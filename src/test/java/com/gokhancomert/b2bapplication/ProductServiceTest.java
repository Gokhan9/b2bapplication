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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void searchProducts_whenNoFilter_shouldReturnPaginatedProducts() {

        Pageable pageable = PageRequest.of(0, 10);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        List<Product> productList = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        ProductDto dto1 = new ProductDto();
        dto1.setId(1L);

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toDto(any(Product.class))).thenReturn(dto1);

        Page<ProductDto> result = productService.searchProducts(null, null, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(dto1.getId(), result.getContent().get(0).getId());
    }

    @Test
    void searchProducts_whenFiltered_shouldReturnMatchingPaginatedProducts() {

        Pageable pageable = PageRequest.of(0, 10);
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Milk");
        List<Product> productList = Collections.singletonList(product1);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        ProductDto dto1 = new ProductDto();
        dto1.setId(1L);
        dto1.setName("Test Milk");

        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(productPage);
        when(productMapper.toDto(product1)).thenReturn(dto1);

        Page<ProductDto> result = productService.searchProducts("Milk", 1L, null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(dto1.getName(), result.getContent().get(0).getName());
    }

    @Test
    void getById_whenProductDoesNotExist_shouldThrowResourceNotFoundException() {

        // given: Sadece var olmayan bir ürün ID'si (nonExistentId) tanımlıyoruz.
        final Long nonExistentId = 999L;

        // when: productRepository'nin findById metodu bu ID ile çağrıldığında, "kayıt bulunamadı" anlamına gelen Optional.empty() döndürmesini sağlıyoruz.
        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // then & verify: assertThrows kullanarak productService.findByProductId metodunun ResourceNotFoundException fırlattığını tek adımda hem çalıştırıyor hem de doğruluyoruz.
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findByProductId(nonExistentId);
        });
    }

    @Test
    void getById_whenProductExists_shouldReturnProduct() {

        // given
        final Long productId = 1L;
        Product foundProduct = new Product();
        foundProduct.setId(productId);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(productId);

        // when
        when(productRepository.findById(productId)).thenReturn(Optional.of(foundProduct));
        when(productMapper.toDto(foundProduct)).thenReturn(expectedDto);

        // then
        ProductDto result = productService.findByProductId(productId);

        // verify
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }

    @Test
    void createProduct_whenProductIsSavedSuccessfully_shouldReturnSavedProduct() {

        // given
        ProductCreateRequest createRequest = new ProductCreateRequest();
        createRequest.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(1L);

        // when
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toProduct(createRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(expectedDto);

        // then
        ProductDto result = productService.createProduct(createRequest);

        // verify
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
    }




    @Test
    void update_whenProductExists_shouldReturnUpdatedProduct() {

        // given - Hazırlık Aşaması
        final Long existingId = 1L; //Güncellemek istediğimiz, veritabanında "var olduğunu" varsaydığımız ürünün ID'sini tanımlıyoruz.
        //Servisimize göndereceğimiz "güncelleme isteğini" temsil eden nesneyi oluşturuyoruz. İçine ürünün yeni adı olarak "Updated Name" değerini koyuyoruz.
        ProductUpdateRequest updateRequest = new ProductUpdateRequest();
        updateRequest.setCategoryId(1L);
        updateRequest.setName("Updated Name");

        //Bu, ürünün veritabanındaki güncellenmeden önceki halini temsil ediyor. Adının "Old Name" olduğuna dikkat edin. findById metodu çağrıldığında repository'nin bu nesneyi döndürmesini sağlayacağız.
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

        // when - Davranış Belirleme Aşaması
        when(productRepository.findById(existingId)).thenReturn(Optional.of(existingProduct));      //Eğer productRepository'nin findById metodu 1L ID'si ile çağrılırsa, ona existingProduct nesnemizi (yani ürünün eski halini) içeren bir Optional döndür" diyoruz. Bu, ürünün veritabanında bulunduğu senaryoyu canlandırır.
        when(categoryRepository.findById(updateRequest.getCategoryId())).thenReturn(Optional.of(category)); //Benzer şekilde, kategori deposu 1L ID'si ile sorgulandığında, hazırladığımız sahte category nesnesini döndürmesini söylüyoruz.
        when(productRepository.save(existingProduct)).thenReturn(existingProduct); //Servis, ürünü güncelledikten sonra save metodunu çağırdığında, repository'nin bu kaydı başarıyla yaptığını ve güncellenmiş nesneyi geri döndürdüğünü varsayıyoruz.
        when(productMapper.toDto(existingProduct)).thenReturn(expectedDto); //Son adımda, servis productMapper'dan kaydettiği ürünü DTO'ya çevirmesini istediğinde, mapper'ın bizim hazırladığımız expectedDto'yu (yani "Updated Name" içeren nesneyi) döndürmesini sağlıyoruz.

        // then - Çalıştırma Aşaması
        ProductDto result = productService.updateProduct(existingId,updateRequest);

        // verify - Doğrulama Aşaması
        assertNotNull(result); //Metodun null bir sonuç döndürmediğinden emin oluyoruz.
        assertEquals(expectedDto.getName(), result.getName()); //Dönen sonucun (result) adının, bizim beklediğimiz expectedDto'nun adıyla ("Updated Name") aynı olup olmadığını kontrol ediyoruz. Bu, güncellemenin başarılı olduğunun en önemli kanıtıdır.

        // Ayrıca mapper'ın update metodunun çağrıldığını da doğrulayabiliriz
        verify(productMapper).updateProductFromDto(updateRequest, existingProduct); //Bu özel bir Mockito doğrulama metodudur. "Ben bu testin sonunda, productMapper'ın updateProductFromDto metodunun, updateRequest ve existingProduct parametreleriyle en az bir kez çağrıldığından emin olmak istiyorum" demektir. Bu, servisin sadece sonucu doğru üretmekle kalmayıp, bunu yaparken doğru adımları izlediğini (yani mapper'ı kullandığını) da garanti altına alır.
    }

    @Test
    void delete_whenProductExists_shouldDeleteSuccessfully() {

        // given
        final Long existingId = 1L;

        // when
        when(productRepository.existsById(existingId)).thenReturn(true);

        // then
        productService.deleteProductById(existingId);

        // verify
        verify(productRepository).deleteById(existingId);
    }

    @Test
    void delete_whenProductDoesNotExist_shouldThrowResourceNotFoundException() {

        // given
        final Long nonExistendId = 999L;

        // when
        //Ürünün var olmadığını simüle ediyoruz.
        when(productRepository.existsById(nonExistendId)).thenReturn(false);

        // then
        //Servisin, ürün olmadığında ResourceNotFoundException fırlattığını doğrulayalım..
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProductById(nonExistendId);
        });
    }
}
