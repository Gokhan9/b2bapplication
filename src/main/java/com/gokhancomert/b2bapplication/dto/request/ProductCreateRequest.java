package com.gokhancomert.b2bapplication.dto.request;

import lombok.Data;

@Data
public class ProductCreateRequest {
    private String name;
    private String description;
    private Double price;
    private Boolean inStock;
    private String label;
    private String imageUrl;
    private Long categoryId;
}
