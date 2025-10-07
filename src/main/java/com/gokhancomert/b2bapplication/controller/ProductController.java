package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Tüm Ürünleri Listeler.
    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    // Ürünleri ID'ye göre Listeler.
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Ürünleri kategori id'sine göre filtreleme
    @GetMapping("/category/{category_id}")
    public List<Product> getProductCategoryById(@PathVariable Long categoryId) {
        return productService.getProductCategoryById(categoryId);
    }

    // Arama
    @GetMapping("/search")
    public List<Product> search(@RequestParam String search) {
        return productService.search(search);
    }


}
