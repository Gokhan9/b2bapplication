package com.gokhancomert.b2bapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String imageUrl;
    private Double price;
    private Boolean inStock;
    private String tag;

    @ManyToOne //Bir ürün → bir kategoriye ait, Bir kategori → birden fazla ürüne sahip olabilir.
    @JoinColumn(name = "category_id") //products tablosunda foreign key sütunu category_id olarak oluşturulur.
    private Category category;
}
