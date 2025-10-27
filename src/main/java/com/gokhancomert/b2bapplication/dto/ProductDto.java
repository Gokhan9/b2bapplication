package com.gokhancomert.b2bapplication.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;

    private Long viewCount = 0L;
    private String name;
    private String description;
    private Double price;
    private Boolean inStock;
    private String imageUrl;

    private Long categoryId;
}
