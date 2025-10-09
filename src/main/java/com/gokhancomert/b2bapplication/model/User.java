package com.gokhancomert.b2bapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id //primary key olarak işaretledik
    @GeneratedValue(strategy = GenerationType.IDENTITY) //veritabanı bu alanı otomatik arttırır(auto increment) 1,2,3 şeklinde her yeni kullanıcıya ID atanır.
    private Long id;

    @Column(unique = true, nullable = false) //unique:1 username sadece 1 kişide olabilir. nullable:Bu alan boş bırakılamaz.Kullanıcı login olurken genellikle bu alan kullanılır.
    private String username;
    private String email;

    private String password; //kullanıcı şifresi tutulur, her zaman hash'li. Düz yazı(plain text) kullanılamaz.

    @ElementCollection(fetch = FetchType.EAGER) //ekledik çünkü roles alanının JPA'da direkt kaydedilsin istiyoruz.
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles; // Set<String> olarak tanımlı. Kullanıcının birden fazla rolü olabilir. Normal kullanıcı için "user", yönetici için "admin"
}
