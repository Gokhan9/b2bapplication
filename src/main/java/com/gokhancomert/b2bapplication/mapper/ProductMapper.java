package com.gokhancomert.b2bapplication.mapper;

import com.gokhancomert.b2bapplication.dto.ProductDto;
import com.gokhancomert.b2bapplication.request.ProductRequest;
import com.gokhancomert.b2bapplication.response.ProductResponse;
import com.gokhancomert.b2bapplication.model.Category;
import com.gokhancomert.b2bapplication.model.Product;

public class ProductMapper {

    public ProductDto toDto(Product product) {
        ProductDto toDto = new ProductDto();
        toDto.setId(product.getId());
        toDto.setName(product.getName());
        toDto.setDescription(product.getDescription());
        toDto.setPrice(product.getPrice());
        toDto.setStock(product.getStock());
        toDto.setImageUrl(product.getImageUrl());
        toDto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        return toDto;
    }

    public Product toEntity(ProductDto dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        return product;
    }
}
