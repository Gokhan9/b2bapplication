package com.gokhancomert.b2bapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_views")
public class ProductView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId; //Görüntülenen ürünün ID’si. Ürün kaç kere görüntülendi takip ederiz.
    private LocalDateTime createdDate; //Görüntüleme tarih ve saati.
}
