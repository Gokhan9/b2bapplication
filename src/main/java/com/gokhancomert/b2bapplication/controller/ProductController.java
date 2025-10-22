package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /*
    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAll();
    }*/

    @GetMapping
    public ResponseEntity<Page<ProductDto>> searchProducts(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) Long categoryId,
                                                           Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(name, categoryId, pageable));
    }

    /*
    @GetMapping("/{id}")
    public ProductDto findByProductId(@PathVariable Long id) {
        return productService.findByProductId(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByProductId(id));
    }
}
