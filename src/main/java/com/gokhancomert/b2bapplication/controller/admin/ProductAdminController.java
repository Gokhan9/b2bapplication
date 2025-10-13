package com.gokhancomert.b2bapplication.controller.admin;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.service.ProductService;
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
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    // Ürünü Güncelle
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id,
                                 @RequestBody ProductDto productDto) {
        productDto.setId(id);
        return productService.updateProduct(id, productDto);
    }

    // Ürünü Sil
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }
}
