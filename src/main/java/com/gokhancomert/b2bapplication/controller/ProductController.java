package com.gokhancomert.b2bapplication.controller;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     21      * Ürünleri arama, filtreleme, sıralama ve sayfalama işlemlerini yapan endpoint.
     22      * Örnek Kullanımlar:
     23      * /api/products?name=kalem -> Adında "kalem" geçen ürünler
     24      * /api/products?categoryId=1 -> 1 ID'li kategorideki ürünler
     25      * /api/products?inStock=true -> Sadece stokta olan ürünler
     26      * /api/products?minPrice=100&maxPrice=250 -> Fiyatı 100-250 TL arası olanlar
     27      * /api/products?sort=price,asc -> Fiyata göre artan sıralama
     28      * /api/products?sort=name,desc -> İsme göre azalan sıralama
     29      * /api/products?page=0&size=10 -> İlk sayfada 10 ürün
     30      * Tüm parametreler birleştirilebilir.
     31      */
    @GetMapping
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(name, categoryId, inStock, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findByProductId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findByProductId(id));
    }

    /**
     * En çok görüntülenen ürünleri sayfalamak
     * @param "pageable" sayfalama bilgileri
     * @return en çok görüntülenen ürünlerin DTO'larının sayfası
     */
}
