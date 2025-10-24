package com.gokhancomert.b2bapplication.specification;

import com.gokhancomert.b2bapplication.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class ProductSpecification {

    /**
     * Ürün adına göre 'like' sorgusu (büyük/küçük harf duyarsız) oluşturan bir specification döner.
     * criteriaBuilder.conjunction(); - Her zaman "doğru” bir koşuldur, Böylece null olduğunda tüm ürünler döner.
     */
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    /**
     * Belirtilen kategori ID'sine sahip ürünleri filtreleyen bir specification döner.
     * //SQL karşılığı-WHERE category.id = :categoryId
     */
    public static Specification<Product> inCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    /**
     * Sadece stokta olan veya olmayan ürünleri filtreleyen bir specification döner.
     * stockQuantity > 0 ise "stokta" kabul edilir.
     * ürün stok adedi 0'dan büyük olmalı. SQL:WHERE stock_quantity > 0
     * stok miktari bilinmiyorsa
     * ya da 0'dan az. SQL:WHERE stock_quantity IS NULL OR stock_quantity <= 0
     */
    public static Specification<Product> isAvailable(Boolean inStock) {
        return (root, query, criteriaBuilder) -> {
            if (inStock == null) {
                return criteriaBuilder.conjunction();
            }
            if (inStock) {
                return criteriaBuilder.greaterThan(root.get("stockQuantity"), 0);
            } else {
                return criteriaBuilder.or(
                        criteriaBuilder.isNull(root.get("stockQuantity")),
                        criteriaBuilder.lessThanOrEqualTo(root.get("stockQuantity"), 0)
                );
            }
        };
    }

    /**
     * Belirtilen fiyat aralığındaki ürünleri filtreleyen bir Specification döner.
     * Hem minPrice hem maxPrice verilmişse → aralıktaki ürünler.
     * Sadece biri verilmişse → o sınıra göre filtreleme.
     * Hiçbiri verilmemişse → filtre uygulanmaz (tüm ürünler).
     * criteriaBuilder.between(root.get("price"), minPrice, maxPrice); → Yani fiyatı belirlenen aralıkta olan ürünler döner.
     */
    public static Specification<Product> hasPriceBeetwen(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.conjunction();
        };
    }
}
