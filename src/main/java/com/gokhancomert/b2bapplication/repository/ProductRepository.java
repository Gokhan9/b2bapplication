package com.gokhancomert.b2bapplication.repository;

import com.gokhancomert.b2bapplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByTitleContainingIgnoreCase(String keyword);
}
