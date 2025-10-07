package com.gokhancomert.b2bapplication.service;

import com.gokhancomert.b2bapplication.model.Product;
import com.gokhancomert.b2bapplication.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Tüm Ürünleri Listeler
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ID ile ürün getirir.
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Ürünleri Kategori'de ki ID'ye göre getirir/filtreler.
    public List<Product> getProductCategoryById(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // Başlık içerisinde "Arama"
    public List<Product> search(String keyword) {
        return productRepository.findByTitleContainingIgnoreCase(keyword);
    }

    //yeni ürünleri kaydet.
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    //Ürün sil
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
