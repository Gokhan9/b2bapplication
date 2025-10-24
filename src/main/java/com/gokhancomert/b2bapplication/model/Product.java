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

    private String name;
    private String title;
    private String description;
    private String imageUrl;
    private Double price;

    //stok miktarı: 0 ise tükenmiş kabul edilir.
    private Integer stockQuantity;

    //fiyat birimini tutar. örn: "kg","adet","litre".
    private String unit;

    //ürün etiketi. örn: "yeni", "indirimde".
    private String label;

    //ürün görüntülenme sayısı. varsayılan = 0
    private Long viewCount = 0L;

    @ManyToOne //Bir ürün → bir kategoriye ait, Bir kategori → birden fazla ürüne sahip.
    @JoinColumn(name = "category_id") //products tablosunda foreign key sütunu category_id olarak oluşturulur.
    private Category category;
}
