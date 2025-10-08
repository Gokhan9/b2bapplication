package com.gokhancomert.b2bapplication.dto.mapper;

import com.gokhancomert.b2bapplication.dto.request.ProductRequest;
import com.gokhancomert.b2bapplication.dto.response.ProductResponse;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.model.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest productRequest, Category category) {
        Product product = new Product();
        product.setName(productRequest.getProductName());
        product.setDescription(productRequest.getProductDescription());
        product.setPrice(productRequest.getProductPrice());
        product.setStock(productRequest.getProductStock());
        product.setCategory(category);
        return product;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setProductName(product.getName());
        response.setProductDescription(product.getDescription());
        response.setProductPrice(product.getPrice());
        response.setInStock(product.getStock() > 0);
        response.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        return response;
    }
}
