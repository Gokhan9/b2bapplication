package com.gokhancomert.b2bapplication.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    //private String label;
    private String imageUrl;

    private Long categoryId;
}
