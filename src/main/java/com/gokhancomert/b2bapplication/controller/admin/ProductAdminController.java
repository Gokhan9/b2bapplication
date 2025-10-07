package com.gokhancomert.b2bapplication.controller.admin;

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
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Ürünü Güncelle
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product product) {
        product.setId(id);
        return productService.saveProduct(product);
    }

    // Ürünü Sil
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
