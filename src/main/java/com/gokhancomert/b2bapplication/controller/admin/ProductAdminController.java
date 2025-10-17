package com.gokhancomert.b2bapplication.controller.admin;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.dto.request.ProductCreateRequest;
import com.gokhancomert.b2bapplication.dto.request.ProductUpdateRequest;
import com.gokhancomert.b2bapplication.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    // Yeni Ürün Ekle
    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productService.createProduct(productCreateRequest);
    }

    // Ürünü Güncelle
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                 @RequestBody ProductUpdateRequest productUpdateRequest) {
        return productService.updateProduct(id, productUpdateRequest);
    }

    // Ürünü Sil
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
}
