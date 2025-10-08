package com.gokhancomert.b2bapplication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private boolean inStock;
    private String categoryName;
}
