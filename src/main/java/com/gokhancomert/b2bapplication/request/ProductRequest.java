package com.gokhancomert.b2bapplication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Integer productStock;
    private Long categoryId;
}
