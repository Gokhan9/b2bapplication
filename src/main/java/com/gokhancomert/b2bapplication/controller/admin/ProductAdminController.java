package com.gokhancomert.b2bapplication.controller.admin;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.dto.request.ProductCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.ProductUpdateRequest;
import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.service.FileUploadService;
import com.gokhancomert.b2bapplication.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    private final ProductService productService;
    private final FileUploadService fileUploadService;

    public ProductAdminController(ProductService productService, FileUploadService fileUploadService) {
        this.productService = productService;
        this.fileUploadService = fileUploadService;
    }

    /**
     * Create Product
     */
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productService.createProduct(productCreateRequest);
    }

    /**
     * Update Product
     */
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                 @RequestBody ProductUpdateRequest productUpdateRequest) {
        return productService.updateProduct(id, productUpdateRequest);
    }

    /**
     * Delete Product
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    /**
     * Belirli bir ürün için görsel yüklemek ve ürünün imageUrl alanını update eder.
     * @param "productId" Görselin yükleneceği ürünin ID'si
     * @param "file" yüklenecek görsel dosyası
     * @return Güncellenmiş ürünün "DTO'su"
     * @throws "IOException" dosya yükleme sırasında bir hata oluşması durumu
     */
    @PostMapping("/{productId}/uploadImage")
    public ResponseEntity<ProductDto> uploadProductImage(@PathVariable Long productId,
                                                         @RequestParam("file") MultipartFile file) throws IOException {

        String imageUrl = fileUploadService.uploadFile(file);
        // Ürünün imageUrl alanını güncellemek için ProductService'te yeni bir metod gerekecek.
        // Şimdilik ProductUpdateRequest kullanarak güncelleyebiliriz veya ProductService'e özel bir metod ekleyebiliriz.
        // Basitlik adına, ProductService'e yeni bir metod ekleyelim: updateProductImageUrl
        ProductDto updatedProduct = productService.updateProductImageUrl(productId, imageUrl);
        return ResponseEntity.ok(updatedProduct);
    }
}
