package com.gokhancomert.b2bapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "Ürün adı boş olamaz.")
    @Size(min = 3, message = "Ürün adı en az 3 karakter olmalıdır.")
    private String name;

    @NotBlank(message = "Açıklama alanı boş olamaz.")
    private String description;

    @NotNull(message = "Fiyat boş olamaz.")
    @Positive(message = "Fiyat 0'dan büyük olmalıdır.")
    private Double price;

    @NotNull(message = "Stok durumu boş olamaz.")
    private Boolean inStock;

    @NotBlank(message = "Etiket boş olamaz.")
    private String label;

    private String imageUrl;

    @NotNull(message = "Kategori ID boş olamaz.")
    private Long categoryId;
}
